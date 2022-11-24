package microconfig

data class FrameworkSetting(
    val config: ConfigSetting,
    val log: LogSetting
) {
    companion object {
        fun defaultObject(): FrameworkSetting = FrameworkSetting(
            config = ConfigSetting(),
            log = LogSetting()
        )
    }
}

data class ConfigSetting(
    val underscored: Boolean = false
)

data class LogSetting(
    val prefixed: Boolean = true,
    val prefix: String = "MicroLog"
)
