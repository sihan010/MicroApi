package microlog

import microconfig.FrameworkSetting
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory

class MicroLogger(className: String) {
    private val logger = LogManager.getLogger(className)

    fun info(message: String, block: () -> List<Pair<String, String>>? = { null }) {
        val ps = block()?.size ?: 0
        logger.info("$message ${if (ps > 0) "-> Parameters -> ${block().toString()}" else ""}")
    }

    fun error(message: String, t: Throwable?, block: () -> List<Pair<String, String>>? = { null }) {
        val ps = block()?.size ?: 0
        logger.error("$message ${if (ps > 0) "-> Parameters -> ${block().toString()}" else ""}", t)
    }

    fun debug(message: String, block: () -> List<Pair<String, String>>? = { null }) {
        val ps = block()?.size ?: 0
        logger.debug("$message ${if (ps > 0) "-> Parameters -> ${block().toString()}" else ""}")
    }

    companion object {
        fun setupLogger(config: MicroLogConfig, frameworkSetting: FrameworkSetting) {
            ConfigurationBuilderFactory.newConfigurationBuilder().apply {
                setConfigurationName("MicroLoggerBuilder")
                val rootLogger = newRootLogger(if (config.debug) Level.DEBUG else Level.ALL)
                if (config.async)
                    System.setProperty(
                        "log4j2.contextSelector",
                        "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
                    )

                config.appenders.forEach {
                    when (it) {
                        Appender.CONSOLE -> {
                            add(consoleAppender(this, it.name, config, frameworkSetting))
                            rootLogger.add(newAppenderRef(it.name))
                        }
                        Appender.ROLLING_FILE -> {
                            add(rollingFileAppender(this, it.name, config, frameworkSetting))
                            rootLogger.add(newAppenderRef(it.name))
                        }
                    }
                }
                add(rootLogger)
                Configurator.reconfigure(build())
            }
        }

        fun getLogger(): MicroLogger {
            val callerClass = Thread.currentThread().stackTrace[2].className
            return MicroLogger(callerClass)
        }
    }
}