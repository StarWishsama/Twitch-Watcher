/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-7-9
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.objects

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.logger
import io.github.starwishsama.twitchwatcher.utils.TwitchUtil
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration
import kotlin.system.exitProcess

class BrowserInstance {
    var driver: WebDriver

    init {
        logger.info("Launching browser...")

        try {
            val options = ChromeOptions()
            options.addArguments(config.startArgs)
            options.addArguments("user-agent=${config.userAgent}")

            driver = ChromeDriver(options)

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30))
        } catch (e: Exception) {
            logger.error("Launching failed! The process will be exited", e)
            exitProcess(1)
        }
    }

    fun openPage(page: String) {
        try {
            driver.navigate().to(page)

            driver.manage().addCookie(TwitchUtil.defaultCookie())

            driver.navigate().refresh()

        } catch (e: Exception) {
            logger.warning("A Error occurred while launching page", e)
        }
    }

    fun shutdown() {
        driver.quit()
    }
}