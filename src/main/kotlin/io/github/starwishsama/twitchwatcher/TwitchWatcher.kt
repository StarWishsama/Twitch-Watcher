/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-7-3
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.browserInstance
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.logger
import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.watcher
import io.github.starwishsama.twitchwatcher.objects.BrowserInstance
import io.github.starwishsama.twitchwatcher.runner.TwitchLiveWatcher

object TwitchWatcher {
    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("Launching browser...")
        browserInstance = BrowserInstance()

        logger.info("Launching watcher...")
        watcher = TwitchLiveWatcher(config.streamerDetectUrl, config.highPriorityChannels, browserInstance.driver)

        watcher.viewRandomStreamer()
    }
}