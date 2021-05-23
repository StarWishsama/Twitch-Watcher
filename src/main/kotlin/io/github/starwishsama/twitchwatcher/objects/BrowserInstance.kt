/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-5-23
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.objects

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.logger
import io.github.starwishsama.twitchwatcher.utils.NetUtil
import org.openqa.selenium.Cookie
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration
import java.util.concurrent.TimeUnit

class BrowserInstance {
    private val driver: WebDriver

    init {
        logger.info("Launching browser...")

        val options = ChromeOptions()
        options.addArguments(config.startArgs)
        options.addArguments("user-agent=${config.userAgent}")

        driver = ChromeDriver()

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30))
    }

    fun launchTwitch() {
        try {
            NetUtil.parseCookieData().forEach {
                driver.manage().addCookie(it)
            }

            driver.get(config.streamerDetectUrl)

            NetUtil.checkLoginStatus(driver.manage().cookies)
        } catch (e: Exception) {
            logger.warning("A Error occurred while launching twitch", e)
        }
    }

    fun shutdown() {
        driver.quit()
    }
}