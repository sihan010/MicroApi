import com.typesafe.config.ConfigMemorySize
import microcollection.exceptions.MicroGenericException
import microconfig.FrameworkSetting
import microconfig.MicroConfig
import microlog.MicroLogConfig
import java.io.File
import java.time.Duration

data class AppConfig(
    val log: MicroLogConfig,
    val api: ApiConfig
) {
    companion object {
        fun load(fileName: String): Pair<AppConfig, FrameworkSetting> {
            if (fileName.isBlank())
                throw MicroGenericException("Config Filename was blank")
            val file = File(fileName)
            if (file.length() == 0L)
                throw MicroGenericException("Config file was empty")
            return MicroConfig.parse(file)
        }
    }
}

data class ApiConfig(
    val port: Int,
    val metricsPort: Int,
    val healthCheckDuration: Duration,
    val maxPayloadSize: ConfigMemorySize,
    val servers: Set<String>
)
