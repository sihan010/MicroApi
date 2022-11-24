package microcollection.exceptions

class MicroGenericException(override val message: String, t: Throwable? = null) : Exception(message, t)