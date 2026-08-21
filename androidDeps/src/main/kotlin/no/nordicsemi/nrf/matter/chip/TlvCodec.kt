package no.nordicsemi.nrf.matter.chip

import matter.tlv.AnonymousTag
import matter.tlv.ContextSpecificTag
import matter.tlv.Tag
import matter.tlv.TlvReader
import matter.tlv.TlvWriter
import no.nordicsemi.nrf.matter.logger.NordicLogger

/*
 * Copyright (c) 2025, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

private const val TAG = "TlvCodec"

/**
 * The tag of the single field carried by commands and command responses encoded by
 * [encodeCommandFields] and decoded by [decodeCommandResponse].
 */
private val SINGLE_FIELD_TAG = ContextSpecificTag(0)

internal fun TlvWriter.putValue(tag: Tag, value: Any?): TlvWriter = when (value) {
    null -> putNull(tag)
    is Boolean -> put(tag, value)
    is Byte -> put(tag, value)
    is Short -> put(tag, value)
    is Int -> put(tag, value)
    is Long -> put(tag, value)
    is UByte -> put(tag, value)
    is UShort -> put(tag, value)
    is UInt -> put(tag, value)
    is ULong -> put(tag, value)
    is Float -> put(tag, value)
    is Double -> put(tag, value)
    is String -> put(tag, value)
    is ByteArray -> put(tag, value)
    else -> throw IllegalArgumentException(
        "Unsupported Matter value type: ${value::class.simpleName}. Supported types are Boolean, " +
                "signed and unsigned integers, Float, Double, String and ByteArray."
    )
}

internal fun encodeAttributeValue(value: Any?): ByteArray =
    TlvWriter().putValue(AnonymousTag, value).getEncoded()

internal fun encodeCommandFields(value: Any?): ByteArray =
    TlvWriter().apply {
        startStructure(AnonymousTag)
        if (value != null && value != Unit) {
            putValue(SINGLE_FIELD_TAG, value)
        }
        endStructure()
    }.getEncoded()

internal fun decodeCommandResponse(tlv: ByteArray?): Any? {
    if (tlv == null || tlv.isEmpty()) return null
    return runCatching {
        val reader = TlvReader(tlv)
        reader.enterStructure(AnonymousTag)
        if (reader.isEndOfContainer()) null else reader.nextElement().value.toAny()
    }.onFailure {
        NordicLogger.error("Unable to decode command response payload", it as? Exception, tag = TAG)
    }.getOrNull()
}
