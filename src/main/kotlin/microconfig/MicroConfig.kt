package microconfig

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions
import io.github.config4k.extract
import microcollection.exceptions.MicroGenericException
import java.io.File

class MicroConfig {
    companion object {
        val configCommon = ConfigCommon()

        inline fun <reified T> parse(file: File): Pair<T, FrameworkSetting> {
            ConfigFactory.parseFile(
                file,
                ConfigParseOptions.defaults()
                    .setAllowMissing(true)
            ).let {
                if (it.isEmpty)
                    throw MicroGenericException("Config was empty in root")

                val frameworkSetting = configCommon.extractFrameWorkConfig(it)
                return Pair(configCommon.applyFrameworkSetting(it, frameworkSetting).extract(), frameworkSetting)
            }
        }
    }
}
