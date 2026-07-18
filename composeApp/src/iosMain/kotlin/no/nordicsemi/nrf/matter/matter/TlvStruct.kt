package no.nordicsemi.nrf.matter.matter

import platform.Foundation.NSNumber
import platform.Matter.MTRArrayValueType
import platform.Matter.MTRContextTagKey
import platform.Matter.MTRDataKey
import platform.Matter.MTRNullValueType
import platform.Matter.MTRStructureValueType
import platform.Matter.MTRTypeKey
import platform.Matter.MTRValueKey

/**
 * Raw-dictionary encode/decode for Matter's attribute value format, per the documented format in
 * `MTRBaseDevice.h`'s doc comment (the authoritative source, since the usual per-field-typed
 * decoding done automatically by the typed `MTRBaseCluster*` wrapper classes isn't available
 * here — see [StandardClusterIds] for why).
 *
 * Format (from the header): a "data-value" is `{type, value}` (`value` omitted for Null); a
 * "structure-value" (the `value` of a Structure-typed data-value) is an array of
 * `{contextTag, data: <data-value>}`; an "array-value" (the `value` of an Array-typed data-value)
 * is an array of `{data: <data-value>}`.
 */
internal data class TlvField(val contextTag: Int, val type: String, val value: Any?)

internal fun dataValue(type: String, value: Any?): Map<String, Any> =
    if (value == null) mapOf(MTRTypeKey to MTRNullValueType) else mapOf(MTRTypeKey to type, MTRValueKey to value)

internal fun structureValue(fields: List<TlvField>): Map<String, Any> = dataValue(
    MTRStructureValueType,
    fields.map { field -> mapOf(MTRContextTagKey to NSNumber(int = field.contextTag), MTRDataKey to dataValue(field.type, field.value)) },
)

/** The raw "array-value" list (list of `{data: <data-value>}`) — pass as a [TlvField]'s `value` alongside [MTRArrayValueType]. */
internal fun arrayElements(elementDataValues: List<Map<String, Any>>): List<Map<String, Any>> =
    elementDataValues.map { dataValue -> mapOf(MTRDataKey to dataValue) }

internal fun encodeStructArrayValue(structs: List<List<TlvField>>): Map<String, Any> =
    dataValue(MTRArrayValueType, arrayElements(structs.map { structureValue(it) }))

/** The raw `{type, value}` data-value dict for one field, keyed by context tag. */
internal typealias RawDataValue = Map<String, Any?>

internal fun RawDataValue?.rawValue(): Any? = this?.get(MTRValueKey)
internal fun RawDataValue?.rawType(): String? = this?.get(MTRTypeKey) as? String

/** Re-encodes a field previously decoded by [decodeStructArray]/[decodeStructFields], preserving its original type. */
internal fun RawDataValue.toTlvField(contextTag: Int): TlvField = TlvField(contextTag, rawType() ?: MTRNullValueType, rawValue())

@Suppress("UNCHECKED_CAST")
internal fun decodeStructFields(raw: Any?): Map<Int, RawDataValue> {
    val structureFields = raw as? List<*> ?: return emptyMap()
    val fields = mutableMapOf<Int, RawDataValue>()
    for (field in structureFields) {
        val fieldMap = field as? Map<String, *> ?: continue
        val tag = (fieldMap[MTRContextTagKey] as? NSNumber)?.intValue ?: continue
        val fieldData = fieldMap[MTRDataKey] as? RawDataValue ?: continue
        fields[tag] = fieldData
    }
    return fields
}

@Suppress("UNCHECKED_CAST")
internal fun decodeStructArray(raw: Any?): List<Map<Int, RawDataValue>> {
    val elements = raw as? List<*> ?: return emptyList()
    return elements.mapNotNull { element ->
        val elementMap = element as? Map<String, *> ?: return@mapNotNull null
        val elementData = elementMap[MTRDataKey] as? RawDataValue ?: return@mapNotNull null
        decodeStructFields(elementData.rawValue())
    }
}

/** Decodes an array-value whose elements are plain scalars (not structures), e.g. a list of node IDs. */
@Suppress("UNCHECKED_CAST")
internal fun decodeValueArray(raw: Any?): List<Any?> {
    val elements = raw as? List<*> ?: return emptyList()
    return elements.mapNotNull { element ->
        val elementMap = element as? Map<String, *> ?: return@mapNotNull null
        (elementMap[MTRDataKey] as? RawDataValue).rawValue()
    }
}
