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

package io.github.starwishsama.twitchwatcher.objects

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.baseUrl
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.logger
import io.github.starwishsama.twitchwatcher.utils.TwitchUtil
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class BrowserInstance {
    private val driver: WebDriver

    init {
        logger.info("Launching browser...")

        val options = ChromeOptions()
        options.addArguments(config.startArgs)
        options.addArguments("user-agent=${config.userAgent}")

        driver = ChromeDriver(options)

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30))
    }

    fun openTwitch() {
        try {
            TwitchUtil.generateCookie().forEach {
                driver.manage().addCookie(it)
            }

            driver.get(config.streamerDetectUrl)

            TwitchUtil.checkLoginStatus(driver.manage().cookies)
        } catch (e: Exception) {
            logger.warning("A Error occurred while launching twitch", e)
        }
    }

    fun viewRandomStreamers() {
        val streamers = TwitchUtil.checkLiveStreamers(driver)

        if (streamers.isEmpty()) {
            logger.info("No streamers now :(")
            return
        }

        val url = baseUrl + streamers.random()

        logger.info("Now watching streamer: $url")

        driver.get(url)
        val waiter = WebDriverWait(driver, Duration.ofSeconds(30))
        waiter.until {
            (it as JavascriptExecutor).executeScript("return document.readyState").equals("complete")
        }
    }

    fun shutdown() {
        driver.quit()
    }
}