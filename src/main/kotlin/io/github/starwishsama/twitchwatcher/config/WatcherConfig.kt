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

package io.github.starwishsama.twitchwatcher.config

import kotlinx.serialization.Serializable
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * [WatcherConfig]
 *
 * Config of Twitch watcher.
 */
@Serializable
data class WatcherConfig(
    val token: String = "",
    val streamerDetectUrl: String = "",
    val watchFirstStreamer: Boolean = false,
    val highPriorityChannels: List<String> = listOf(),
    val userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.66",
    val scrollDelay: Long = 1000,
    val scrollTime: Int = 3,
    val minWatchTime: Long = 15,
    val maxWatchTime: Long = 45,
    val streamerRefreshTime: Long = 300,
    val timeUnit: TimeUnit = TimeUnit.MINUTES,
    val liveScreenshot: Boolean = false,
    val proxy: String = "ip:port",
    val proxyType: Proxy.Type = Proxy.Type.HTTP,
    val startArgs: List<String> = listOf(
        "--disable-dev-shm-usage",
        "--disable-accelerated-2d-canvas",
        "--no-first-run",
        "--no-zygote",
        "--disable-gpu",
        "--no-sandbox",
        "--disable-setuid-sandbox"
    )
)
