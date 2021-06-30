/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-6-30
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.runner

import io.github.starwishsama.twitchwatcher.utils.TwitchUtil
import org.openqa.selenium.WebDriver

class TwitchLiveDetector(val url: String, val interestIn: List<String>, val driver: WebDriver) {
    var status: DetectorStatus = DetectorStatus.READY

    var nowWatching = ""

    fun viewRandomStreamer() {
        if (nowWatching.isEmpty()) {
            val streamers = TwitchUtil.checkLiveStreamers(driver, url)
            val interest = streamers.filter { interestIn.contains(it) }
            nowWatching = if (interest.isNotEmpty()) {
                interest.random()
            } else {
                streamers.random()
            }
        }

        TwitchUtil.viewStreamingPage(this, nowWatching)
    }
}