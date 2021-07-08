/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-7-8
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 * Use of this source code is governed by the GNU GPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

package io.github.starwishsama.twitchwatcher.config

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants.config
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import java.io.File

object ConfigLoader {
	val configLocation = File("./config.yml")

	fun load() {
		if (!configLocation.exists()) {
			configLocation.createNewFile()
			configLocation.writeText(Yaml.Default.encodeToString(config))
		} else {
			config = Yaml.Default.decodeFromString(configLocation.readText())
		}
	}

	fun save() {
		configLocation.writeText(Yaml.Default.encodeToString(config))
	}
}