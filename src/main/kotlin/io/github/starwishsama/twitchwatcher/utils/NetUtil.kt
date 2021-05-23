/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-5-23
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.utils

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.logger
import org.openqa.selenium.By
import org.openqa.selenium.Cookie
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions

object NetUtil {
    private val cookiePolicyQuery = "button[data-a-target=\"consent-banner-accept\"]"
    private val matureContentQuery = "button[data-a-target=\"player-overlay-mature-accept\"]"
    private val sidebarQuery = "*[data-test-selector=\"user-menu__toggle\"]"
    private val userStatusQuery = "span[data-a-target=\"presence-text\"]"
    private val channelsQuery = "a[data-test-selector*=\"ChannelLink\"]"
    private val streamPauseQuery = "button[data-a-target=\"player-play-pause-button\"]"
    private val streamSettingsQuery = "[data-a-target=\"player-settings-button\"]"
    private val streamQualitySettingQuery = "[data-a-target=\"player-settings-menu-item-quality\"]"
    private val streamQualityQuery = "input[data-a-target=\"tw-radio\"]"
    
    fun parseCookieData(): List<Cookie> {
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

    fun checkLiveStreamers(driver: WebDriver): List<String> {
        logger.info("Emulating scrolling...")

        val e = driver.findElement(By.className("scrollable-trigger__wrapper"))

        if (e != null) {
            val actions = Actions(driver)
            actions.moveToElement(e)
            actions.perform()

           config.timeUnit.sleep(config.scrollDelay)
        }


    }

    fun queryOnWebsite(driver: WebDriver, query: String): String {
        driver.findElement(By.tagName(channelsQuery))
    }
}
