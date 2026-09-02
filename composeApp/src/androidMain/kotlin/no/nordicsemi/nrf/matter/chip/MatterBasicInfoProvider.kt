package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipClusters
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.model.DeviceId
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

data class MatterBasicInfo(
    val vendorId: Int?,
    val vendorName: String?,
    val productId: Int?,
    val productName: String?,
    val softwareVersion: String?,
    val serialNumber: String?,
    val specificationVersion: Long?,
    val uniqueId: String?
)

class MatterBasicInfoProvider(private val chipClient: ChipClient) {

    /**
     * Fetches all basic information attributes sequentially to prevent
     * overwhelming the Matter SDK's native JNI event loop.
     */
    suspend fun fetchBasicInfo(deviceId: DeviceId): MatterBasicInfo {
        // Read sequentially. This prevents native deadlocks.
        val vendorId = readVendorIDAttribute(deviceId)
        val vendorName = readVendorNameAttribute(deviceId)
        val productId = readProductIDAttribute(deviceId)
        val productName = readProductNameAttribute(deviceId)
        val softwareVersion = readSoftwareVersionAttribute(deviceId)
        val serialNumber = readSerialNumberAttribute(deviceId)
        val specificationVersion = readSpecificationVersionAttribute(deviceId)
        val uniqueId = readUniqueIdAttribute(deviceId)

        return MatterBasicInfo(
            vendorId = vendorId,
            vendorName = vendorName,
            productId = productId,
            productName = productName,
            softwareVersion = softwareVersion,
            serialNumber = serialNumber,
            specificationVersion = specificationVersion,
            uniqueId = uniqueId
        )
    }

    /**
     * Reads the vendor name attribute.
     *
     * @param deviceId the device identifier.
     * @return the vendor name
     */
    suspend fun readVendorNameAttribute(deviceId: DeviceId): String? {
        return readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readVendorNameAttribute(stringCallback(cb))
        }
    }

    /**
     * Reads the vendor id attribute.
     *
     * @param deviceId the device identifier.
     * @return the vendor id
     */
    suspend fun readVendorIDAttribute(deviceId: DeviceId): Int? {
        return readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readVendorIDAttribute(integerCallback(cb))
        }
    }


    /**
     * Reads the product id attribute.
     *
     * @param deviceId the device identifier.
     * @return the product id
     */
    suspend fun readProductIDAttribute(deviceId: DeviceId): Int? {
        return readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readProductIDAttribute(integerCallback(cb))
        }
    }

    /**
     * Reads the product name attribute.
     *
     * @param deviceId the device identifier.
     * @return the product name
     */
    suspend fun readProductNameAttribute(deviceId: DeviceId): String? {
        return readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readProductNameAttribute(stringCallback(cb))
        }
    }

    /**
     * Read software version attribute.
     *
     * @param deviceId the device identifier.
     * @return the software version
     */
    suspend fun readSoftwareVersionAttribute(deviceId: DeviceId): String? =
        readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readSoftwareVersionStringAttribute(stringCallback(cb))
        }

    /**
     * Read serial number attribute.
     *
     * @param deviceId the device identifier.
     * @return the serial number
     */
    suspend fun readSerialNumberAttribute(deviceId: DeviceId): String? =
        readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readSerialNumberAttribute(stringCallback(cb))
        }

    /**
     * Read unique id attribute.
     *
     * @param deviceId the device identifier.
     * @return the unique id
     */
    suspend fun readUniqueIdAttribute(deviceId: DeviceId): String? =
        readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readUniqueIDAttribute(stringCallback(cb))
        }

    /**
     * Read specification version attribute.
     *
     * @param deviceId the device identifier.
     * @return the specification version
     */
    suspend fun readSpecificationVersionAttribute(deviceId: DeviceId): Long? =
        readBasicInformationAttribute(deviceId) { ptr, cb ->
            ChipClusters.BasicInformationCluster(ptr, 0)
                .readSpecificationVersionAttribute(longCallback(cb))
        }

    private suspend fun <T> readBasicInformationAttribute(
        deviceId: DeviceId,
        reader: (Long, (Result<T>) -> Unit) -> Unit
    ): T? {
        return try {
            val devicePtr = chipClient.getConnectedDevicePointer(deviceId.longValue)

            suspendCancellableCoroutine { continuation ->
                reader(devicePtr) { result ->
                    result
                        .onSuccess { continuation.resume(it) }
                        .onFailure { continuation.resumeWithException(it) }
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            null
        }
    }

    private fun stringCallback(callback: (Result<String>) -> Unit) =
        object : ChipClusters.CharStringAttributeCallback {
            override fun onSuccess(value: String) {
                callback(Result.success(value))
            }

            override fun onError(ex: Exception) {
                callback(Result.failure(ex))
            }
        }

    private fun longCallback(callback: (Result<Long>) -> Unit) =
        object : ChipClusters.LongAttributeCallback {
            override fun onSuccess(value: Long) {
                callback(Result.success(value))
            }

            override fun onError(ex: Exception) {
                callback(Result.failure(ex))
            }
        }

    private fun integerCallback(callback: (Result<Int>) -> Unit) =
        object : ChipClusters.IntegerAttributeCallback {

            override fun onSuccess(value: Int) {
                callback(Result.success(value))
            }

            override fun onError(ex: Exception) {
                callback(Result.failure(ex))
            }
        }
}
