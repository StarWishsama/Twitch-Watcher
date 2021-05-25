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

package io.github.starwishsama.twitchwatcher.logger

import io.github.starwishsama.twitchwatcher.TwitchWatcherConstants
import org.fusesource.jansi.Ansi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("unused", "MemberVisibilityCanBePrivate")
class HinaLogger(
    val loggerName: String,
    val logAction: (String) -> Unit = {
        TwitchWatcherConstants.console.printAbove(it)
    },
    var debugMode: Boolean = false,
    var outputBeautyTrace: Boolean = false,
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy/M/dd HH:mm:ss")
) {

    // 时间 日志等级/日志等级缩写 logger名字 -> logger前缀 消息
    // 例: 21/2/18 19:18:32 N/MainLogger(i.g.s.c.t.ClassName) -> [Main] Logger Example
    fun log(
        level: HinaLogLevel,
        message: String?,
        stacktrace: Throwable? = null,
        prefix: String = "",
        bypass: Boolean = false
    ) {
        if (!debugMode && (level == HinaLogLevel.Debug && !bypass)) return

        val st = Thread.currentThread().stackTrace.toMutableList().also {
            it.subList(2, it.size)
        }

        val executor = st.firstOrNull {
            !it.className.startsWith("java.")
                    && !it.className.startsWith("io.github.starwishsama.comet.logger")
        } ?: st.getOrNull(0) ?: st[0]

        val executorInfo = "${simplyClassName(executor.className)}#${executor.methodName}:${executor.lineNumber}"

        var trace = ""

        if (stacktrace != null) {
            trace = try {
                formatStacktrace(stacktrace, null, outputBeautyTrace)
            } catch (e: Exception) {
                stacktrace.stackTraceToString()
            }
        }

        logAction(
            "${level.color}${dateTimeFormatter.format(LocalDateTime.now())} ${level.internalName}/${level.simpleName}" +
                    "${if (level != HinaLogLevel.Verbose && level != HinaLogLevel.Info && level != HinaLogLevel.Warn) "($executorInfo)" else ""} " +
                    "$loggerName -> ${if (prefix.isEmpty()) "" else "$prefix "}$message"
                    + if (trace.isNotEmpty()) "\n\n$trace\n" else "" + "${AnsiUtil.Color.RESET}"
        )
    }

    fun info(content: String?) {
        log(HinaLogLevel.Info, content)
    }

    fun info(content: String?, stacktrace: Throwable?) {
        log(HinaLogLevel.Info, content, stacktrace)
    }

    fun verbose(content: String?) {
        log(HinaLogLevel.Verbose, content)
    }

    fun verbose(content: String?, stacktrace: Throwable?) {
        log(HinaLogLevel.Verbose, content, stacktrace)
    }

    fun error(content: String?) {
        log(HinaLogLevel.Error, content)
    }

    fun error(content: String?, stacktrace: Throwable?) {
        log(HinaLogLevel.Error, content, stacktrace)
    }

    fun warning(content: String?) {
        log(HinaLogLevel.Warn, content)
    }

    fun warning(content: String?, stacktrace: Throwable?) {
        log(HinaLogLevel.Warn, content, stacktrace)
    }

    fun warning(stacktrace: Throwable?) {
        log(HinaLogLevel.Warn, stacktrace = stacktrace, message = null)
    }

    fun debug(content: String?) {
        log(HinaLogLevel.Debug, content)
    }

    fun debug(content: String?, stacktrace: Throwable?) {
        log(HinaLogLevel.Debug, content, stacktrace)
    }

    fun serve(content: String?, stacktrace: Throwable?) {
        log(HinaLogLevel.Serve, content, stacktrace)
    }

    fun serve(content: String?) {
        log(HinaLogLevel.Serve, content)
    }

    fun fatal(content: String?, stacktrace: Throwable?) {
        log(HinaLogLevel.Fatal, content, stacktrace)
    }

    fun fatal(content: String?) {
        log(HinaLogLevel.Fatal, content)
    }

    private fun simplyClassName(name: String): String {
        return buildString {
            val classPart = name.split(".")
            classPart.forEach {
                if (it != classPart.last()) {
                    append(it.substring(0, 1))
                    append(".")
                } else {
                    append(it)
                }
            }
        }
    }

    /**
     * https://github.com/Polar-Pumpkin/ParrotX/blob/master/src/main/java/org/serverct/parrot/parrotx/utils/i18n/PLogger.java#L126
     * 快速向控制台输出更优雅的堆栈跟踪。
     *
     * @param exception     Throwable 类型的异常.
     * @param packageFilter 包名关键词过滤, 不需要可以填写 null.
     * @param simpleMode    不进行格式化, 直接输出默认格式的堆栈文本.
     *
     * @author Polar-Pumpkin
     */
    internal fun formatStacktrace(exception: Throwable, packageFilter: String? = null, simpleMode: Boolean): String {
        if (simpleMode) return exception.stackTraceToString()

        return buildString {
            val msg = exception.localizedMessage
            append("========================= 发生了错误 =========================")
            append("异常类型 ▶")
            append(exception.javaClass.name)
            append(if (msg == null || msg.isEmpty()) "没有详细信息" else msg)
            // org.serverct.parrot.plugin.Plugin
            var currentPackage = ""
            for (elem in exception.stackTrace) {
                val key = elem.className
                var pass = true
                if (packageFilter != null) {
                    pass = key.contains(packageFilter)
                }
                val nameSet = key.split("[.]").toTypedArray()
                val className = nameSet[nameSet.size - 1]
                val packageSet = arrayOfNulls<String>(nameSet.size - 2)
                System.arraycopy(nameSet, 0, packageSet, 0, nameSet.size - 2)
                val packageName = StringBuilder()
                for ((counter, nameElem) in packageSet.withIndex()) {
                    packageName.append(nameElem)
                    if (counter < packageSet.size - 1) {
                        packageName.append(".")
                    }
                }
                if (pass) {
                    if (packageName.toString() != currentPackage) {
                        currentPackage = packageName.toString()
                        append("")
                        append("包 $packageName ▶")
                    }
                    append("  ▶ 在类 ${className}, 方法 ${elem.methodName} (${elem.fileName}) 行 ${elem.lineNumber}")
                }
            }
            append("========================= 发生了错误 =========================")
        }
    }

    sealed class HinaLogLevel(val internalName: String, val simpleName: String, val color: AnsiUtil.Color) {
        object Verbose : HinaLogLevel("VERBOSE", "V", AnsiUtil.Color.GRAY)
        object Info : HinaLogLevel("INFO", "I", AnsiUtil.Color.RESET)
        object Debug : HinaLogLevel("DEBUG", "D", AnsiUtil.Color.LIGHT_BLUE)
        object Error : HinaLogLevel("ERROR", "E", AnsiUtil.Color.LIGHT_RED)
        object Warn : HinaLogLevel("WARN", "W", AnsiUtil.Color.LIGHT_YELLOW)
        object Serve : HinaLogLevel("SERVE", "S", AnsiUtil.Color.RED)
        object Fatal : HinaLogLevel("FATAL", "F", AnsiUtil.Color.RED)
    }
}

@Suppress("unused")
object AnsiUtil {
    enum class Color(private val format: String) {
        RESET("\u001b[0m"),

        WHITE("\u001b[30m"),
        RED("\u001b[31m"),
        EMERALD_GREEN("\u001b[32m"),
        GOLD("\u001b[33m"),
        BLUE("\u001b[34m"),
        PURPLE("\u001b[35m"),
        GREEN("\u001b[36m"),

        GRAY("\u001b[90m"),
        LIGHT_RED("\u001b[91m"),
        LIGHT_GREEN("\u001b[92m"),
        LIGHT_YELLOW("\u001b[93m"),
        LIGHT_BLUE("\u001b[94m"),
        LIGHT_PURPLE("\u001b[95m"),
        LIGHT_CYAN("\u001b[96m"),

        DARK_RED(Ansi.ansi().fgRgb(170, 0, 0).toString());

        override fun toString(): String = format
    }
}