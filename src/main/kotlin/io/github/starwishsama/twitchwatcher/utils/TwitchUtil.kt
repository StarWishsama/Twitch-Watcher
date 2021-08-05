/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-8-5
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.utils

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.browserInstance
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.logger
import io.github.starwishsama.twitchwatcher.runner.DetectorStatus
import io.github.starwishsama.twitchwatcher.runner.TwitchLiveWatcher
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.Cookie
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * [TwitchUtil]
 *
 * Used to assist with processing Twitch watch.
 */
object TwitchUtil {

    // A series of element name in Twitch Streaming page.
    private const val cookiePolicyQuery = "button[data-a-target=\"consent-banner-accept\"]"
    private const val matureContentQuery = "button[data-a-target=\"player-overlay-mature-accept\"]"
    private const val sidebarQuery = "*[data-test-selector=\"user-menu__toggle\"]"
    private const val userStatusQuery = "span[data-a-target=\"presence-text\"]"
    private const val channelsQuery = "a[data-test-selector*=\"ChannelLink\"]"
    private const val streamPauseQuery = "button[data-a-target=\"player-play-pause-button\"]"
    private const val streamSettingsQuery = "[data-a-target=\"player-settings-button\"]"
    private const val streamQualitySettingQuery = "[data-a-target=\"player-settings-menu-item-quality\"]"
    private const val streamQualityQuery = "input[data-a-target=\"tw-radio\"]"

    /**
     * Parse config cookie to cookie for request
     *
     * @return default Cookies
     */
    fun defaultCookie(): Cookie {

        /**val defaultCookie = mutableListOf(
        Cookie("hostOnly", "false"),
        Cookie("session", "false"),
        Cookie("storeId", "0"),
        Cookie("id", "1")
        )*/

        return Cookie(
            "auth-token",
            config.token,
            ".twitch.tv",
            "/",
            Date.from(Instant.now().plus(30, ChronoUnit.DAYS)),
            true,
            false
        )
    }

    /**
     * Check Twitch login status
     *
     * @param cookies cookies
     * @return login status
     */
    fun checkLoginStatus(cookies: Set<Cookie>): Boolean {
        logger.verbose("Cookies: $cookies")

        val twilight = cookies.find { it.name == "twilight-user" }

        val userName = cookies.find { it.name == "login" }

        if (twilight != null) {
            val display: String = if (userName != null) {
                userName.value
            } else {
                "Unknown Name"
            }

            logger.info("✅ Login twitch successfully! Logon as $display")
            return true
        }

        logger.warning("\uD83D\uDED1 Login failed, invalid token (${config.token})!")
        logger.warning("Please check whether you have filled a valid twitch token.")

        return false
    }

    /**
     * Checks current streamers in live status and with Twitch Drop enabled.
     *
     * @param driver current WebDriver
     * @param url request streamer url
     *
     * @return streamers, empty when no matched streamer.
     */
    fun checkLiveStreamers(driver: WebDriver, url: String): List<String> {
        browserInstance.openPage(url)

        Thread.sleep(10_000)

        if (!checkLoginStatus(driver.manage().cookies)) {
            logger.warning("Twitch login status is malformed, cancel retrieve streamers.")
            return emptyList()
        }

        logger.info("Emulating scrolling...")

        val streamers = mutableListOf<String>()

        val e = driver.findElement(By.className("scrollable-trigger__wrapper"))

        if (e != null) {
            val actions = Actions(driver)
            actions.moveToElement(e)
            actions.perform()

            config.timeUnit.sleep(config.scrollDelay)

            val ele = doSelector(driver, channelsQuery)
            for (element in ele) {
                streamers.add(element.attr("href").split("/")[1])
            }
        }

        logger.verbose("Streamers list: $streamers")

        return streamers
    }

    /**
     * [viewStreamingPage]
     *
     * Open random streamer's live room in watcher.
     *
     * @param watcher [TwitchLiveWatcher]
     * @param url live room URL
     */
    fun viewStreamingPage(watcher: TwitchLiveWatcher, url: String) {
        if (url.isEmpty()) {
            logger.info("The request url cannot be empty!")
            watcher.driver.close()
            return
        }

        val driver = watcher.driver

        driver.get("https://www.twitch.tv/$url")

        logger.info("\uD83D\uDD17 Now watching: $url")

        val watchDuration = Random.nextLong(config.minWatchTime, config.maxWatchTime)

        // Click on cookie policy accept button
        clickButton(driver, cookiePolicyQuery)
        // Click on cookie mature content accept button
        clickButton(driver, matureContentQuery)

        if (watcher.status == DetectorStatus.RUNNING) {
            logger.info("Setting the resolution to lowest...")

            clickButton(driver, streamPauseQuery)
            HtmlUtil.waitForElement(driver, HtmlUtil.getWebElement(driver, By.cssSelector(streamPauseQuery)))

            clickButton(driver, streamSettingsQuery)
            HtmlUtil.waitForElement(driver, HtmlUtil.getWebElement(driver, By.cssSelector(streamSettingsQuery)))

            clickButton(driver, streamQualityQuery)
            HtmlUtil.waitForElement(driver, HtmlUtil.getWebElement(driver, By.cssSelector(streamQualityQuery)))

            val resolutions = doSelector(driver, streamQualitySettingQuery)
            val lowestResolution = resolutions.last().attributes()["id"]

            clickButton(driver, lowestResolution, By.tagName(lowestResolution))

            HtmlUtil.getWebElement(driver, By.cssSelector(streamPauseQuery)).sendKeys("m")

            watcher.status = DetectorStatus.READY
        }

        clickButton(driver, sidebarQuery)
        HtmlUtil.waitForElement(driver, HtmlUtil.getWebElement(driver, By.cssSelector(sidebarQuery)))
        val status = doSelector(driver, userStatusQuery) //status jQuery
        clickButton(driver, sidebarQuery); //Close sidebar

        logger.info("💡 Account status: ${if (status.isNotEmpty() && status[0] != null) status[0].children()[0].data() else "Unknown"}")
        logger.info("🕒 Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        logger.info("💤 Watching stream for " + watchDuration / 60000 + "minute(s)");

        val wait = WebDriverWait(driver, Duration.ofSeconds(5), Duration.ofMinutes(watchDuration))
        wait.withTimeout(Duration.ofMinutes(watchDuration))
    }

    /**
     * Click a button on webpage searching by selector.
     *
     * @param driver WebDriver
     * @param query query
     * @param by search Element way
     */
    fun clickButton(driver: WebDriver, query: String, by: By = By.cssSelector(query)) {
        val result = driver.findElements(by)

        if (result.isEmpty()) {
            return
        }

        try {
            val first = result[0]

            if (first.tagName == "tag" && first.accessibleName == "button") {
                first.click()
                TimeUnit.MILLISECONDS.sleep(200)
            }
        } catch (e: Exception) {
            logger.warning("A Error occurred when clicking button", e)
        }
    }

    /**
     * Use [Jsoup] to parse webpage and find element by query given.
     *
     * @param driver WebDriver
     * @param query query
     *
     * @return Elements
     */
    private fun doSelector(driver: WebDriver, query: String): Elements {
        return Jsoup.parse(driver.pageSource).select(query)
    }
}
