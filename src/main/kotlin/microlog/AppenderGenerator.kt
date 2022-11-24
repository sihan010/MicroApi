package microlog

import microconfig.FrameworkSetting
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration

fun consoleAppender(
    configBuilder: ConfigurationBuilder<BuiltConfiguration>,
    name: String,
    config: MicroLogConfig,
    frameworkSetting: FrameworkSetting
): AppenderComponentBuilder {
    val pattern =
        if (frameworkSetting.log.prefixed) "${frameworkSetting.log.prefix} -> ${config.patternConsole}" else config.patternConsole
    val consoleAppender = configBuilder.newAppender(name, "CONSOLE")
        .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
    return consoleAppender.add(
        configBuilder.newLayout("PatternLayout").addAttribute("pattern", pattern)
    )
}

fun rollingFileAppender(
    configBuilder: ConfigurationBuilder<BuiltConfiguration>,
    name: String,
    config: MicroLogConfig, frameworkSetting: FrameworkSetting
): AppenderComponentBuilder {
    val pattern =
        if (frameworkSetting.log.prefixed) "${frameworkSetting.log.prefix} -> ${config.patternConsole}" else config.patternRollingFile
    val filePattern = with(config.rollingFileName) {
        val lastDot = this.lastIndexOf('.')
        this.substring(0 until lastDot) + "_%i." + this.substring(lastDot + 1 until this.length)
    }
    return configBuilder.newAppender(name, "RollingFile")
        .addAttribute("fileName", config.rollingFileName)
        .addAttribute(
            "filePattern",
            filePattern
        )
        .addAttribute("bufferSize", config.rollingFileBufferSize.toBytes())
        .add(
            configBuilder.newLayout("PatternLayout").addAttribute("pattern", pattern)
        )
        .addComponent(
            configBuilder.newComponent("Policies")
                .addComponent(
                    configBuilder.newComponent("SizeBasedTriggeringPolicy").addAttribute(
                        "size",
                        "${config.rollingFileTriggerPolicy.toBytes()}B"
                    )
                )
        )
}