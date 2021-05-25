/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-5-25
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.utils

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.logger
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.Cookie
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import java.util.concurrent.TimeUnit

object TwitchUtil {

    // A series of element name in Twitch Streaming page.
    private val cookiePolicyQuery = "button[data-a-target=\"consent-banner-accept\"]"
    private val matureContentQuery = "button[data-a-target=\"player-overlay-mature-accept\"]"
    private val sidebarQuery = "*[data-test-selector=\"user-menu__toggle\"]"
    private val userStatusQuery = "span[data-a-target=\"presence-text\"]"
    private val channelsQuery = "a[data-test-selector*=\"ChannelLink\"]"
    private val streamPauseQuery = "button[data-a-target=\"player-play-pause-button\"]"
    private val streamSettingsQuery = "[data-a-target=\"player-settings-button\"]"
    private val streamQualitySettingQuery = "[data-a-target=\"player-settings-menu-item-quality\"]"
    private val streamQualityQuery = "input[data-a-target=\"tw-radio\"]"

    /**
     * Parse config cookie to request cookie
     *
     * @return default Cookies
     */
    fun generateCookie(): List<Cookie> {
        val defaultCookie = mutableListOf(
            Cookie("domain", ".twitch.tv"),
            Cookie("hostOnly", "false"),
            Cookie("httpOnly", "false"),
            Cookie("name", "auth-token"),
            Cookie("path", "/"),
            Cookie("sameSite", "no_restriction"),
            Cookie("secure", "true"),
            Cookie("session", "false"),
            Cookie("storeId", "0"),
            Cookie("id", "1")
        )

        defaultCookie.add(Cookie("auth-token", config.token))

        return defaultCookie
    }

    /**
     * Check Twitch login status
     *
     * @return login status
     */
    fun checkLoginStatus(cookies: Set<Cookie>): Boolean {
        cookies.forEach {
            if (it.name == "twilight-user") {
                logger.info("✅ Login twitch successfully!")
                return true;
            }
        }

        logger.warning("\uD83D\uDED1 Login failed, invalid token!")
        logger.warning("Please check whether you have filled a valid twitch token.")

        return false
    }

    /**
     * Checks current streamers in live status and with Twitch Drop enabled.
     *
     * @param driver current WebDriver
     *
     * @return streamers, empty when no matched streamer.
     */
    fun checkLiveStreamers(driver: WebDriver): List<String> {
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
     * Click a button on webpage searching by selector.
     *
     * @param driver WebDriver
     * @param query query
     */
    fun clickButton(driver: WebDriver, query: String) {
        val result = driver.findElements(By.cssSelector(query))

        try {
            val first = result[0]

            if (first.tagName == "tag" && first.accessibleName == "button") {
                first.click()
                TimeUnit.SECONDS.sleep(1)
            }
        } catch (ignored: Exception) {
            logger.verbose("A Error occurred when clicking button.")
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
