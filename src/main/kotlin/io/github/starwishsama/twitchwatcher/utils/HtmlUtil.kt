/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-8-6
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.utils

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

object HtmlUtil {
    fun waitForElement(driver: WebDriver, element: WebElement?, timeout: Duration = Duration.ofSeconds(5), sleepDuration: Duration = Duration.ofSeconds(2)) {
        val waitFor = WebDriverWait(driver, timeout, sleepDuration)

        waitFor.until(
            ExpectedConditions.visibilityOf(element)
        )
    }

    fun getWebElement(driver: WebDriver, by: By): WebElement? {
        return driver.findElement(by)
    }
}