/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-7-7
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher

import io.github.starwishsama.twitchwatcher.config.ConfigLoader
import io.github.starwishsama.twitchwatcher.config.WatcherConfig
import io.github.starwishsama.twitchwatcher.logger.HinaLogger
import io.github.starwishsama.twitchwatcher.logger.LoggerAppender
import io.github.starwishsama.twitchwatcher.logger.getLogLocation
import io.github.starwishsama.twitchwatcher.objects.BrowserInstance
import io.github.starwishsama.twitchwatcher.objects.LiveInfo
import io.github.starwishsama.twitchwatcher.runner.TwitchLiveWatcher

import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import org.jline.utils.InfoCmp

object TwitchWatcherConstants {
    val config = WatcherConfig()

    val console: LineReader = LineReaderBuilder
        .builder()
        .terminal(
            TerminalBuilder.builder()
                .jansi(true)
                .encoding(Charsets.UTF_8)
                .build()
                .also { it.puts(InfoCmp.Capability.exit_ca_mode) })
        .appName("Twitch-Watcher").build()
        .also {
            it.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION)
            it.unsetOpt(LineReader.Option.INSERT_TAB)
        }

    val baseUrl = "https://www.twitch.tv/"

    val streamers = mutableSetOf<LiveInfo>()

    val logger = HinaLogger("Twitch-Watcher", loggerAppender = LoggerAppender(getLogLocation("twitch")))

    lateinit var browserInstance: BrowserInstance

    lateinit var watcher: TwitchLiveWatcher
}