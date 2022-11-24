package microlog

import com.typesafe.config.ConfigMemorySize

data class MicroLogConfig(
    val debug: Boolean = true,
    val async: Boolean = true,
    val appenders: Set<Appender> = setOf(Appender.CONSOLE),
    val patternConsole: String = "%highlight{%d{DATE} -> %p -> %c{1.} -> [%t] -> Message: %msg%n%throwable}",
    val patternRollingFile: String = "%d{DATE} -> %p -> %c{1.} -> [%t] -> Message: %msg%n%throwable",
    val rollingFileName : String = "log/rolling_log.log",
    val rollingFileBufferSize:ConfigMemorySize = ConfigMemorySize.ofBytes(1024),
    val rollingFileTriggerPolicy:ConfigMemorySize = ConfigMemorySize.ofBytes(2*1024*1024)
)

enum class Appender(name:String){
    CONSOLE("console"),
    ROLLING_FILE("rolling_file")
}

