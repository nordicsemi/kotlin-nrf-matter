package no.nordicsemi.nrf.matter.matter

internal class WrongAttributeTypeException(message: String) : Exception(message)

/**
 * Mirrors `iosApp/iosApp/kotlin/local/AttributeParser.swift`'s `AttributeParser` protocol.
 * Swift used an associated-type protocol; Kotlin has no ObjC-compatibility constraint here
 * since this never crosses the cinterop boundary itself, so a plain generic interface suffices.
 */
internal interface AttributeParser<T> {
    fun parse(value: Any?): T
}

internal object StringAttributeParser : AttributeParser<String> {
    override fun parse(value: Any?): String =
        value as? String ?: throw WrongAttributeTypeException("Expected String, got $value")
}

internal object BoolAttributeParser : AttributeParser<Boolean> {
    override fun parse(value: Any?): Boolean = when (value) {
        is Boolean -> value
        is Long -> value != 0L
        is Int -> value != 0
        else -> throw WrongAttributeTypeException("Expected Bool or Int, got $value")
    }
}

internal object IntAttributeParser : AttributeParser<Int> {
    override fun parse(value: Any?): Int =
        (value as? Int) ?: (value as? Long)?.toInt() ?: throw WrongAttributeTypeException("Expected Int, got $value")
}

/** Passes the raw attribute value through unparsed — used for array/structure-typed attributes. */
internal object RawAttributeParser : AttributeParser<Any?> {
    override fun parse(value: Any?): Any? = value
}
