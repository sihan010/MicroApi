import microcollection.exceptions.MicroGenericException
import microlog.MicroLogger

fun main() {
    val (appConfig, frameworkSetting) = AppConfig.load("config/application.conf") // Parse HOCON config
    MicroLogger.setupLogger(appConfig.log, frameworkSetting) // SetUp logger
    val logger = MicroLogger.getLogger()
    logger.info("hello"){
        listOf(Pair("Yolo","oo"))
    }
    logger.error("Oooppss", MicroGenericException("Any Message")){
        listOf(Pair("Yolo","oo"))
    }
}