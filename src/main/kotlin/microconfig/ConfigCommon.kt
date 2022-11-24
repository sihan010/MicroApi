package microconfig

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import microcollection.exceptions.MicroGenericException
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class ConfigCommon {
    private inline infix fun <reified T : Any> T.merge(other: T): T {
        val nameToProperty = T::class.declaredMemberProperties.associateBy { it.name }
        val primaryConstructor = T::class.primaryConstructor!!
        val args = primaryConstructor.parameters.associateWith { parameter ->
            val property = nameToProperty[parameter.name]!!

            (property.get(other) ?: property.get(this))
        }
        return primaryConstructor.callBy(args)
    }

    private fun makeCamelCase(s:String): String {
        var camelcase = ""
        var foundUnderscore = false
        for (i in s.indices) {
            if (s[i] == '_') {
                foundUnderscore = true
                continue
            }
            if (foundUnderscore) {
                camelcase += s[i].uppercase()
                foundUnderscore = false
                continue
            }
            camelcase += s[i]
        }
        return camelcase
    }

    fun applyFrameworkSetting (config:Config, setting:FrameworkSetting):Config{
        return if(setting.config.underscored){
            ConfigFactory.parseMap(
                config.entrySet().associate { p ->
                    Pair(MicroConfig.configCommon.makeCamelCase(p.key), p.value)
                }
            )
        }
        else config
    }

    fun extractFrameWorkConfig(config: Config): FrameworkSetting {
        val frameworkConfig = config.getConfig("framework")
        return if (frameworkConfig != null) {
            if(!frameworkConfig.isResolved)
                throw MicroGenericException("Could not resolve framework config")
            return FrameworkSetting.defaultObject() merge frameworkConfig.extract()
        } else FrameworkSetting.defaultObject()
    }
}