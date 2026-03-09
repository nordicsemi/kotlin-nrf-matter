// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

import com.google.errorprone.annotations.Immutable
import com.google.home.BitmapDescriptor
import com.google.home.ClusterStruct
import com.google.home.CommandDescriptor
import com.google.home.Descriptor as HomeDescriptor
import com.google.home.DescriptorMap
import com.google.home.EnumDescriptor
import com.google.home.EnumEntry
import com.google.home.Field
import com.google.home.HomeException
import com.google.home.NoOpDescriptor
import com.google.home.StructDescriptor
import com.google.home.TagId
import com.google.home.Type as FieldType
import com.google.home.annotation.HomeExperimentalGenericApi
import com.google.home.automation.TypedExpression
import com.google.home.automation.fieldSelect
import com.google.home.matter.serialization.Bitmap
import com.google.home.matter.serialization.BitmapAdapter
import com.google.home.matter.serialization.CanMutate
import com.google.home.matter.serialization.ClusterBitmap
import com.google.home.matter.serialization.ClusterBitmapFlag
import com.google.home.matter.serialization.ClusterEnum
import com.google.home.matter.serialization.ClusterId
import com.google.home.matter.serialization.ClusterPayloadReader
import com.google.home.matter.serialization.ClusterPayloadWriter
import com.google.home.matter.serialization.EnumAdapter
import com.google.home.matter.serialization.MutableBitmap
import com.google.home.matter.serialization.OptionalValue
import com.google.home.matter.serialization.ScopedCommandId
import com.google.home.matter.serialization.StructAdapter
import com.google.home.matter.serialization.camelToSnakeUpper
import com.google.home.matter.serialization.unwrapPayload
import com.google.home.matter.serialization.upperCamelToSnakeUpper
import com.google.home.matter.serialization.wrapPayload
import com.google.home.toBitmapDescriptor
import com.google.home.toDescriptorMap
import com.google.home.toEnumDescriptor
import javax.annotation.processing.Generated
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode

/*
 * Serialization object for NetworkCommissioningTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for NetworkCommissioningTrait. */
@Generated("GoogleHomePlatformCodegen")
object NetworkCommissioningTrait {
  val Id = ClusterId(49u, "NetworkCommissioning")

  // Enums
  enum class NetworkCommissioningStatusEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(49u).traitId,
    override val typeName: String = "NetworkCommissioningStatusEnum",
  ) : ClusterEnum {
    Success(0u),
    OutOfRange(1u),
    BoundsExceeded(2u),
    NetworkIDNotFound(3u),
    DuplicateNetworkID(4u),
    NetworkNotFound(5u),
    RegulatoryError(6u),
    AuthFailure(7u),
    UnsupportedSecurity(8u),
    OtherConnectionFailure(9u),
    IPV6Failed(10u),
    IPBindFailed(11u),
    UnknownError(12u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    ; //UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @OptIn(HomeExperimentalGenericApi::class)
    fun toDescription(): String {
      return "NetworkCommissioningStatusEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @OptIn(HomeExperimentalGenericApi::class)
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "NetworkCommissioningStatusEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class WiFiBandEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(49u).traitId,
    override val typeName: String = "WiFiBandEnum",
  ) : ClusterEnum {
    Num2G4(0u),
    Num3G65(1u),
    Num5G(2u),
    Num6G(3u),
    Num60G(4u),
    Num1G(5u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    ; //UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @OptIn(HomeExperimentalGenericApi::class)
    fun toDescription(): String {
      return "WiFiBandEnum".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @OptIn(HomeExperimentalGenericApi::class)
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "WiFiBandEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps
  data class Feature(
    val wiFiNetworkInterface: Boolean = false,
    val threadNetworkInterface: Boolean = false,
    val ethernetNetworkInterface: Boolean = false,
    val perDeviceCredentials: Boolean = false,
  ) : ClusterBitmap(traitId = ClusterId(49u).traitId, bitmapName = "Feature") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      WiFiNetworkInterface(0x1u),
      ThreadNetworkInterface(0x2u),
      EthernetNetworkInterface(0x4u),
      PerDeviceCredentials(0x8u),
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<Feature> {
          override fun toRaw(value: Feature): Bitmap =
            MutableBitmap().also {
              it[MaskFlags.WiFiNetworkInterface.value] = value.wiFiNetworkInterface
              it[MaskFlags.ThreadNetworkInterface.value] = value.threadNetworkInterface
              it[MaskFlags.EthernetNetworkInterface.value] = value.ethernetNetworkInterface
              it[MaskFlags.PerDeviceCredentials.value] = value.perDeviceCredentials
            }

          override fun toRuntime(value: Bitmap): Feature =
            Feature(
              value[MaskFlags.WiFiNetworkInterface.value],
              value[MaskFlags.ThreadNetworkInterface.value],
              value[MaskFlags.EthernetNetworkInterface.value],
              value[MaskFlags.PerDeviceCredentials.value],
            )
        }

      val BitmapDescriptor =
        object : BitmapDescriptor {
          override val name: String = "Feature"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = MaskFlags.entries.toBitmapDescriptor()
        }
    }
  }

  data class ThreadCapabilitiesBitmap(
    val isBorderRouterCapable: Boolean = false,
    val isRouterCapable: Boolean = false,
    val isSleepyEndDeviceCapable: Boolean = false,
    val isFullThreadDevice: Boolean = false,
    val isSynchronizedSleepyEndDeviceCapable: Boolean = false,
  ) : ClusterBitmap(traitId = ClusterId(49u).traitId, bitmapName = "ThreadCapabilitiesBitmap") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      IsBorderRouterCapable(0x1u),
      IsRouterCapable(0x2u),
      IsSleepyEndDeviceCapable(0x4u),
      IsFullThreadDevice(0x8u),
      IsSynchronizedSleepyEndDeviceCapable(0x10u),
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<ThreadCapabilitiesBitmap> {
          override fun toRaw(value: ThreadCapabilitiesBitmap): Bitmap =
            MutableBitmap().also {
              it[MaskFlags.IsBorderRouterCapable.value] = value.isBorderRouterCapable
              it[MaskFlags.IsRouterCapable.value] = value.isRouterCapable
              it[MaskFlags.IsSleepyEndDeviceCapable.value] = value.isSleepyEndDeviceCapable
              it[MaskFlags.IsFullThreadDevice.value] = value.isFullThreadDevice
              it[MaskFlags.IsSynchronizedSleepyEndDeviceCapable.value] =
                value.isSynchronizedSleepyEndDeviceCapable
            }

          override fun toRuntime(value: Bitmap): ThreadCapabilitiesBitmap =
            ThreadCapabilitiesBitmap(
              value[MaskFlags.IsBorderRouterCapable.value],
              value[MaskFlags.IsRouterCapable.value],
              value[MaskFlags.IsSleepyEndDeviceCapable.value],
              value[MaskFlags.IsFullThreadDevice.value],
              value[MaskFlags.IsSynchronizedSleepyEndDeviceCapable.value],
            )
        }

      val BitmapDescriptor =
        object : BitmapDescriptor {
          override val name: String = "ThreadCapabilitiesBitmap"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = MaskFlags.entries.toBitmapDescriptor()
        }
    }
  }

  data class WiFiSecurityBitmap(
    val unencrypted: Boolean = false,
    val wep: Boolean = false,
    val wpaPersonal: Boolean = false,
    val wpa2Personal: Boolean = false,
    val wpa3Personal: Boolean = false,
    val wpa3MatterPdc: Boolean = false,
  ) : ClusterBitmap(traitId = ClusterId(49u).traitId, bitmapName = "WiFiSecurityBitmap") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      Unencrypted(0x1u),
      WEP(0x2u),
      WPAPersonal(0x4u),
      WPA2Personal(0x8u),
      WPA3Personal(0x10u),
      WPA3MatterPDC(0x20u),
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<WiFiSecurityBitmap> {
          override fun toRaw(value: WiFiSecurityBitmap): Bitmap =
            MutableBitmap().also {
              it[MaskFlags.Unencrypted.value] = value.unencrypted
              it[MaskFlags.WEP.value] = value.wep
              it[MaskFlags.WPAPersonal.value] = value.wpaPersonal
              it[MaskFlags.WPA2Personal.value] = value.wpa2Personal
              it[MaskFlags.WPA3Personal.value] = value.wpa3Personal
              it[MaskFlags.WPA3MatterPDC.value] = value.wpa3MatterPdc
            }

          override fun toRuntime(value: Bitmap): WiFiSecurityBitmap =
            WiFiSecurityBitmap(
              value[MaskFlags.Unencrypted.value],
              value[MaskFlags.WEP.value],
              value[MaskFlags.WPAPersonal.value],
              value[MaskFlags.WPA2Personal.value],
              value[MaskFlags.WPA3Personal.value],
              value[MaskFlags.WPA3MatterPDC.value],
            )
        }

      val BitmapDescriptor =
        object : BitmapDescriptor {
          override val name: String = "WiFiSecurityBitmap"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = MaskFlags.entries.toBitmapDescriptor()
        }
    }
  }

  // Events

  // Structs
  class NetworkInfoStruct(
    val networkId: ByteArray = ByteArray(0),
    val connected: Boolean = false,
    val networkIdentifier: OptionalValue<ByteArray?> = OptionalValue.absent(),
    val clientIdentifier: OptionalValue<ByteArray?> = OptionalValue.absent(),
  ) : ClusterStruct {

    /** Descriptor enum for this struct's fields. */
    @OptIn(HomeExperimentalGenericApi::class)
    enum class StructFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : com.google.home.Field {
      /** The [networkId] command request field. */
      networkId("networkId", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
      /** The [connected] command request field. */
      connected("connected", 1u, "Boolean", FieldType.Boolean, false, NoOpDescriptor, false),
      /** The [networkIdentifier] command request field. */
      networkIdentifier(
        "networkIdentifier",
        2u,
        "ByteArray",
        FieldType.ByteArray,
        false,
        NoOpDescriptor,
        true,
      ),
      /** The [clientIdentifier] command request field. */
      clientIdentifier(
        "clientIdentifier",
        3u,
        "ByteArray",
        FieldType.ByteArray,
        false,
        NoOpDescriptor,
        true,
      ),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.networkId.tag -> networkId
        StructFields.connected.tag -> connected
        StructFields.networkIdentifier.tag -> networkIdentifier
        StructFields.clientIdentifier.tag -> clientIdentifier
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<NetworkInfoStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: NetworkInfoStruct) {
        writer.bytearray.write(0u, value.networkId)
        writer.boolean.write(1u, value.connected)
        writer.bytearray.write(2u, value.networkIdentifier)
        writer.bytearray.write(3u, value.clientIdentifier)
      }

      override fun read(reader: ClusterPayloadReader): NetworkInfoStruct {
        val data = reader.readPayload()
        return NetworkInfoStruct(
          data.bytearray.get(0u, "NetworkId"),
          data.boolean.get(1u, "Connected"),
          data.bytearray.getOptionalNullable(2u, "NetworkIdentifier"),
          data.bytearray.getOptionalNullable(3u, "ClientIdentifier"),
        )
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return NetworkInfoStruct(
          networkId = fields[StructFields.networkId] as ByteArray,
          connected = fields[StructFields.connected] as Boolean,
          networkIdentifier = fields[StructFields.networkIdentifier] as OptionalValue<ByteArray?>,
          clientIdentifier = fields[StructFields.clientIdentifier] as OptionalValue<ByteArray?>,
        )
      }

      val TypedExpression<out NetworkInfoStruct?>.networkId: TypedExpression<ByteArray>
        get() = fieldSelect<NetworkInfoStruct, ByteArray>(this, StructFields.networkId)

      val TypedExpression<out NetworkInfoStruct?>.connected: TypedExpression<Boolean>
        get() = fieldSelect<NetworkInfoStruct, Boolean>(this, StructFields.connected)

      val TypedExpression<out NetworkInfoStruct?>.networkIdentifier:
        TypedExpression<OptionalValue<ByteArray?>>
        get() =
          fieldSelect<NetworkInfoStruct, OptionalValue<ByteArray?>>(
            this,
            StructFields.networkIdentifier,
          )

      val TypedExpression<out NetworkInfoStruct?>.clientIdentifier:
        TypedExpression<OptionalValue<ByteArray?>>
        get() =
          fieldSelect<NetworkInfoStruct, OptionalValue<ByteArray?>>(
            this,
            StructFields.clientIdentifier,
          )
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is NetworkInfoStruct) return false
      if (!(networkId contentEquals other.networkId)) {
        return false
      }
      if (connected != other.connected) {
        return false
      }
      if (networkIdentifier != other.networkIdentifier) {
        return false
      }
      if (clientIdentifier != other.clientIdentifier) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + networkId.contentHashCode()
      result = 31 * result + connected.hashCode()
      result = 31 * result + networkIdentifier.hashCode()
      result = 31 * result + clientIdentifier.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "NetworkInfoStruct(networkId=$networkId, connected=$connected, networkIdentifier=$networkIdentifier, clientIdentifier=$clientIdentifier)"
    }
  }

  class ThreadInterfaceScanResultStruct(
    val panId: UShort = 0u,
    val extendedPanId: ULong = 0u,
    val networkName: String = "",
    val channel: UShort = 0u,
    val version: UByte = 0u,
    val extendedAddress: ByteArray = ByteArray(0),
    val rssi: Byte = 0,
    val lqi: UByte = 0u,
  ) : ClusterStruct {

    /** Descriptor enum for this struct's fields. */
    @OptIn(HomeExperimentalGenericApi::class)
    enum class StructFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : com.google.home.Field {
      /** The [panId] command request field. */
      panId("panId", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [extendedPanId] command request field. */
      extendedPanId("extendedPanId", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
      /** The [networkName] command request field. */
      networkName("networkName", 2u, "String", FieldType.String, false, NoOpDescriptor, false),
      /** The [channel] command request field. */
      channel("channel", 3u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [version] command request field. */
      version("version", 4u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
      /** The [extendedAddress] command request field. */
      extendedAddress(
        "extendedAddress",
        5u,
        "ByteArray",
        FieldType.ByteArray,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [rssi] command request field. */
      rssi("rssi", 6u, "Byte", FieldType.Byte, false, NoOpDescriptor, false),
      /** The [lqi] command request field. */
      lqi("lqi", 7u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.panId.tag -> panId
        StructFields.extendedPanId.tag -> extendedPanId
        StructFields.networkName.tag -> networkName
        StructFields.channel.tag -> channel
        StructFields.version.tag -> version
        StructFields.extendedAddress.tag -> extendedAddress
        StructFields.rssi.tag -> rssi
        StructFields.lqi.tag -> lqi
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<ThreadInterfaceScanResultStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: ThreadInterfaceScanResultStruct) {
        writer.ushort.write(0u, value.panId)
        writer.ulong.write(1u, value.extendedPanId)
        writer.string.write(2u, value.networkName)
        writer.ushort.write(3u, value.channel)
        writer.ubyte.write(4u, value.version)
        writer.bytearray.write(5u, value.extendedAddress)
        writer.byte.write(6u, value.rssi)
        writer.ubyte.write(7u, value.lqi)
      }

      override fun read(reader: ClusterPayloadReader): ThreadInterfaceScanResultStruct {
        val data = reader.readPayload()
        return ThreadInterfaceScanResultStruct(
          data.ushort.get(0u, "PanId"),
          data.ulong.get(1u, "ExtendedPanId"),
          data.string.get(2u, "NetworkName"),
          data.ushort.get(3u, "Channel"),
          data.ubyte.get(4u, "Version"),
          data.bytearray.get(5u, "ExtendedAddress"),
          data.byte.get(6u, "Rssi"),
          data.ubyte.get(7u, "Lqi"),
        )
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return ThreadInterfaceScanResultStruct(
          panId = fields[StructFields.panId] as UShort,
          extendedPanId = fields[StructFields.extendedPanId] as ULong,
          networkName = fields[StructFields.networkName] as String,
          channel = fields[StructFields.channel] as UShort,
          version = fields[StructFields.version] as UByte,
          extendedAddress = fields[StructFields.extendedAddress] as ByteArray,
          rssi = fields[StructFields.rssi] as Byte,
          lqi = fields[StructFields.lqi] as UByte,
        )
      }

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.panId: TypedExpression<UShort>
        get() = fieldSelect<ThreadInterfaceScanResultStruct, UShort>(this, StructFields.panId)

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.extendedPanId:
        TypedExpression<ULong>
        get() =
          fieldSelect<ThreadInterfaceScanResultStruct, ULong>(this, StructFields.extendedPanId)

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.networkName: TypedExpression<String>
        get() = fieldSelect<ThreadInterfaceScanResultStruct, String>(this, StructFields.networkName)

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.channel: TypedExpression<UShort>
        get() = fieldSelect<ThreadInterfaceScanResultStruct, UShort>(this, StructFields.channel)

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.version: TypedExpression<UByte>
        get() = fieldSelect<ThreadInterfaceScanResultStruct, UByte>(this, StructFields.version)

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.extendedAddress:
        TypedExpression<ByteArray>
        get() =
          fieldSelect<ThreadInterfaceScanResultStruct, ByteArray>(
            this,
            StructFields.extendedAddress,
          )

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.rssi: TypedExpression<Byte>
        get() = fieldSelect<ThreadInterfaceScanResultStruct, Byte>(this, StructFields.rssi)

      val TypedExpression<out ThreadInterfaceScanResultStruct?>.lqi: TypedExpression<UByte>
        get() = fieldSelect<ThreadInterfaceScanResultStruct, UByte>(this, StructFields.lqi)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ThreadInterfaceScanResultStruct) return false
      if (panId != other.panId) {
        return false
      }
      if (extendedPanId != other.extendedPanId) {
        return false
      }
      if (networkName != other.networkName) {
        return false
      }
      if (channel != other.channel) {
        return false
      }
      if (version != other.version) {
        return false
      }
      if (!(extendedAddress contentEquals other.extendedAddress)) {
        return false
      }
      if (rssi != other.rssi) {
        return false
      }
      if (lqi != other.lqi) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + panId.hashCode()
      result = 31 * result + extendedPanId.hashCode()
      result = 31 * result + networkName.hashCode()
      result = 31 * result + channel.hashCode()
      result = 31 * result + version.hashCode()
      result = 31 * result + extendedAddress.contentHashCode()
      result = 31 * result + rssi.hashCode()
      result = 31 * result + lqi.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "ThreadInterfaceScanResultStruct(panId=$panId, extendedPanId=$extendedPanId, networkName=$networkName, channel=$channel, version=$version, extendedAddress=$extendedAddress, rssi=$rssi, lqi=$lqi)"
    }
  }

  class WiFiInterfaceScanResultStruct(
    val security: WiFiSecurityBitmap = WiFiSecurityBitmap(),
    val ssid: ByteArray = ByteArray(0),
    val bssid: ByteArray = ByteArray(0),
    val channel: UShort = 0u,
    val wiFiBand: WiFiBandEnum = WiFiBandEnum.Num2G4,
    val rssi: Byte = 0,
  ) : ClusterStruct {

    /** Descriptor enum for this struct's fields. */
    @OptIn(HomeExperimentalGenericApi::class)
    enum class StructFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : com.google.home.Field {
      /** The [security] command request field. */
      security(
        "security",
        0u,
        "WiFiSecurityBitmap",
        FieldType.Bitmap,
        false,
        WiFiSecurityBitmap.BitmapDescriptor,
        false,
      ),
      /** The [ssid] command request field. */
      ssid("ssid", 1u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
      /** The [bssid] command request field. */
      bssid("bssid", 2u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
      /** The [channel] command request field. */
      channel("channel", 3u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [wiFiBand] command request field. */
      wiFiBand(
        "wiFiBand",
        4u,
        "WiFiBandEnum",
        FieldType.Enum,
        false,
        WiFiBandEnum.EnumDescriptor,
        false,
      ),
      /** The [rssi] command request field. */
      rssi("rssi", 5u, "Byte", FieldType.Byte, false, NoOpDescriptor, false),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.security.tag -> security
        StructFields.ssid.tag -> ssid
        StructFields.bssid.tag -> bssid
        StructFields.channel.tag -> channel
        StructFields.wiFiBand.tag -> wiFiBand
        StructFields.rssi.tag -> rssi
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<WiFiInterfaceScanResultStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: WiFiInterfaceScanResultStruct) {
        writer.bitmap(WiFiSecurityBitmap.Adapter).write(0u, value.security)
        writer.bytearray.write(1u, value.ssid)
        writer.bytearray.write(2u, value.bssid)
        writer.ushort.write(3u, value.channel)
        writer.enum(WiFiBandEnum.Adapter).write(4u, value.wiFiBand)
        writer.byte.write(5u, value.rssi)
      }

      override fun read(reader: ClusterPayloadReader): WiFiInterfaceScanResultStruct {
        val data = reader.readPayload()
        return WiFiInterfaceScanResultStruct(
          data.bitmap(WiFiSecurityBitmap.Adapter).get(0u, "Security"),
          data.bytearray.get(1u, "Ssid"),
          data.bytearray.get(2u, "Bssid"),
          data.ushort.get(3u, "Channel"),
          data.enum(WiFiBandEnum.Adapter).get(4u, "WiFiBand"),
          data.byte.get(5u, "Rssi"),
        )
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return WiFiInterfaceScanResultStruct(
          security = fields[StructFields.security] as WiFiSecurityBitmap,
          ssid = fields[StructFields.ssid] as ByteArray,
          bssid = fields[StructFields.bssid] as ByteArray,
          channel = fields[StructFields.channel] as UShort,
          wiFiBand = fields[StructFields.wiFiBand] as WiFiBandEnum,
          rssi = fields[StructFields.rssi] as Byte,
        )
      }

      val TypedExpression<out WiFiInterfaceScanResultStruct?>.security:
        TypedExpression<WiFiSecurityBitmap>
        get() =
          fieldSelect<WiFiInterfaceScanResultStruct, WiFiSecurityBitmap>(
            this,
            StructFields.security,
          )

      val TypedExpression<out WiFiInterfaceScanResultStruct?>.ssid: TypedExpression<ByteArray>
        get() = fieldSelect<WiFiInterfaceScanResultStruct, ByteArray>(this, StructFields.ssid)

      val TypedExpression<out WiFiInterfaceScanResultStruct?>.bssid: TypedExpression<ByteArray>
        get() = fieldSelect<WiFiInterfaceScanResultStruct, ByteArray>(this, StructFields.bssid)

      val TypedExpression<out WiFiInterfaceScanResultStruct?>.channel: TypedExpression<UShort>
        get() = fieldSelect<WiFiInterfaceScanResultStruct, UShort>(this, StructFields.channel)

      val TypedExpression<out WiFiInterfaceScanResultStruct?>.wiFiBand:
        TypedExpression<WiFiBandEnum>
        get() =
          fieldSelect<WiFiInterfaceScanResultStruct, WiFiBandEnum>(this, StructFields.wiFiBand)

      val TypedExpression<out WiFiInterfaceScanResultStruct?>.rssi: TypedExpression<Byte>
        get() = fieldSelect<WiFiInterfaceScanResultStruct, Byte>(this, StructFields.rssi)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is WiFiInterfaceScanResultStruct) return false
      if (security != other.security) {
        return false
      }
      if (!(ssid contentEquals other.ssid)) {
        return false
      }
      if (!(bssid contentEquals other.bssid)) {
        return false
      }
      if (channel != other.channel) {
        return false
      }
      if (wiFiBand != other.wiFiBand) {
        return false
      }
      if (rssi != other.rssi) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + security.hashCode()
      result = 31 * result + ssid.contentHashCode()
      result = 31 * result + bssid.contentHashCode()
      result = 31 * result + channel.hashCode()
      result = 31 * result + wiFiBand.hashCode()
      result = 31 * result + rssi.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "WiFiInterfaceScanResultStruct(security=$security, ssid=$ssid, bssid=$bssid, channel=$channel, wiFiBand=$wiFiBand, rssi=$rssi)"
    }
  }

  /** Attributes for the NetworkCommissioning cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {
    val maxNetworks: UByte?
    val networks: List<NetworkInfoStruct>?
    val scanMaxTimeSeconds: UByte?
    val connectMaxTimeSeconds: UByte?
    val interfaceEnabled: Boolean?
    val lastNetworkingStatus: NetworkCommissioningStatusEnum?
    val lastNetworkId: ByteArray?
    val lastConnectErrorValue: Int?
    val supportedWiFiBands: List<WiFiBandEnum>?
    val supportedThreadFeatures: ThreadCapabilitiesBitmap?
    val threadVersion: UShort?

    /**
     * A list of server-generated commands (server to client) which are supported by this cluster
     * server instance.
     */
    val generatedCommandList: List<UInt>

    /** A list of client-generated commands which are supported by this cluster server instance. */
    val acceptedCommandList: List<UInt>

    /** A list of the attribute IDs of the attributes supported by the cluster instance. */
    val attributeList: List<UInt>

    /**
     * Whether the server supports zero or more optional cluster features. A cluster feature is a
     * set of cluster elements that are mandatory or optional for a defined feature of the cluster.
     * If a cluster feature is supported by the cluster instance, then the corresponding bit is set
     * to 1, otherwise the bit is set to 0 (zero).
     */
    val featureMap: Feature

    /** The revision of the server cluster specification supported by the cluster instance. */
    val clusterRevision: UShort

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getDescriptor(): StructDescriptor = NetworkCommissioning.Attribute.StructDescriptor

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        NetworkCommissioning.Attribute.maxNetworks.tag -> maxNetworks
        NetworkCommissioning.Attribute.networks.tag -> networks
        NetworkCommissioning.Attribute.scanMaxTimeSeconds.tag -> scanMaxTimeSeconds
        NetworkCommissioning.Attribute.connectMaxTimeSeconds.tag -> connectMaxTimeSeconds
        NetworkCommissioning.Attribute.interfaceEnabled.tag -> interfaceEnabled
        NetworkCommissioning.Attribute.lastNetworkingStatus.tag -> lastNetworkingStatus
        NetworkCommissioning.Attribute.lastNetworkId.tag -> lastNetworkId
        NetworkCommissioning.Attribute.lastConnectErrorValue.tag -> lastConnectErrorValue
        NetworkCommissioning.Attribute.supportedWiFiBands.tag -> supportedWiFiBands
        NetworkCommissioning.Attribute.supportedThreadFeatures.tag -> supportedThreadFeatures
        NetworkCommissioning.Attribute.threadVersion.tag -> threadVersion
        NetworkCommissioning.Attribute.generatedCommandList.tag -> generatedCommandList
        NetworkCommissioning.Attribute.acceptedCommandList.tag -> acceptedCommandList
        NetworkCommissioning.Attribute.attributeList.tag -> attributeList
        NetworkCommissioning.Attribute.featureMap.tag -> featureMap
        NetworkCommissioning.Attribute.clusterRevision.tag -> clusterRevision
        else -> null
      }
    }

    /** @suppress */
    companion object Adapter : StructAdapter<Attributes> {

      override fun write(writer: ClusterPayloadWriter, value: Attributes) {
        if (value is MutableAttributes) {
          MutableAttributes.Adapter.write(writer, value)
          return
        }
        writer.wrapPayload(id = Id)
        if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
          writer.ubyte.write(0u, value.maxNetworks)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.struct(NetworkInfoStruct.Adapter).writeList(1u, value.networks)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.ubyte.write(2u, value.scanMaxTimeSeconds)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.ubyte.write(3u, value.connectMaxTimeSeconds)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
          writer.boolean.write(4u, value.interfaceEnabled)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
          writer.enum(NetworkCommissioningStatusEnum.Adapter).write(5u, value.lastNetworkingStatus)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(6u)) {
          writer.bytearray.write(6u, value.lastNetworkId)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(7u)) {
          writer.int.write(7u, value.lastConnectErrorValue)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(8u)) {
          writer.enum(WiFiBandEnum.Adapter).writeList(8u, value.supportedWiFiBands)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(9u)) {
          writer.bitmap(ThreadCapabilitiesBitmap.Adapter).write(9u, value.supportedThreadFeatures)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(10u)) {
          writer.ushort.write(10u, value.threadVersion)
        }
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.bitmap(Feature.Adapter).write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload(mapOf(1u to NetworkInfoStruct.Adapter))
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data.ubyte
            .getOptionalNullable(0u, "MaxNetworks")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data
            .struct { NetworkInfoStruct() }
            .getOptionalNullableList(1u, "Networks")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data.ubyte
            .getOptionalNullable(2u, "ScanMaxTimeSeconds")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.ubyte
            .getOptionalNullable(3u, "ConnectMaxTimeSeconds")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(4u, "InterfaceEnabled")
            .also { if (it.isPresent && it.value != null) attributeList.add(4u) }
            .getOrNull(),
          data
            .enum(NetworkCommissioningStatusEnum.Adapter)
            .getOptionalNullable(5u, "LastNetworkingStatus")
            .also { if (it.isPresent) attributeList.add(5u) }
            .getOrNull(),
          data.bytearray
            .getOptionalNullable(6u, "LastNetworkId")
            .also { if (it.isPresent) attributeList.add(6u) }
            .getOrNull(),
          data.int
            .getOptionalNullable(7u, "LastConnectErrorValue")
            .also { if (it.isPresent) attributeList.add(7u) }
            .getOrNull(),
          data
            .enum(WiFiBandEnum.Adapter)
            .getOptionalNullableList(8u, "SupportedWiFiBands")
            .also { if (it.isPresent && it.value != null) attributeList.add(8u) }
            .getOrNull(),
          data
            .bitmap(ThreadCapabilitiesBitmap.Adapter)
            .getOptionalNullable(9u, "SupportedThreadFeatures")
            .also { if (it.isPresent && it.value != null) attributeList.add(9u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(10u, "ThreadVersion")
            .also { if (it.isPresent && it.value != null) attributeList.add(10u) }
            .getOrNull(),
          data.uint.getList(65528u, "GeneratedCommandList").also { attributeList.add(65528u) },
          data.uint.getList(65529u, "AcceptedCommandList").also { attributeList.add(65529u) },
          attributeList.also { attributeList.add(65531u) },
          data.bitmap(Feature.Adapter).get(65532u, "FeatureMap").also { attributeList.add(65532u) },
          data.ushort.get(65533u, "ClusterRevision").also { attributeList.add(65533u) },
        )
      }
    }
  }

  /** @suppress */
  open class AttributesImpl(
    override val maxNetworks: UByte? = null,
    override val networks: List<NetworkInfoStruct>? = null,
    override val scanMaxTimeSeconds: UByte? = null,
    override val connectMaxTimeSeconds: UByte? = null,
    override val interfaceEnabled: Boolean? = null,
    override val lastNetworkingStatus: NetworkCommissioningStatusEnum? = null,
    override val lastNetworkId: ByteArray? = null,
    override val lastConnectErrorValue: Int? = null,
    override val supportedWiFiBands: List<WiFiBandEnum>? = null,
    override val supportedThreadFeatures: ThreadCapabilitiesBitmap? = null,
    override val threadVersion: UShort? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 3u, 4u, 5u, 6u, 7u, 8u, 9u, 10u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: Feature = Feature(),
    override val clusterRevision: UShort = 0u,
  ) : Attributes, CanMutate<Attributes, MutableAttributes> {

    constructor(
      other: Attributes
    ) : this(
      maxNetworks = other.maxNetworks,
      networks = other.networks,
      scanMaxTimeSeconds = other.scanMaxTimeSeconds,
      connectMaxTimeSeconds = other.connectMaxTimeSeconds,
      interfaceEnabled = other.interfaceEnabled,
      lastNetworkingStatus = other.lastNetworkingStatus,
      lastNetworkId = other.lastNetworkId,
      lastConnectErrorValue = other.lastConnectErrorValue,
      supportedWiFiBands = other.supportedWiFiBands,
      supportedThreadFeatures = other.supportedThreadFeatures,
      threadVersion = other.threadVersion,
      generatedCommandList = other.generatedCommandList,
      acceptedCommandList = other.acceptedCommandList,
      attributeList = other.attributeList,
      featureMap = other.featureMap,
      clusterRevision = other.clusterRevision,
    )

    override fun mutate(init: MutableAttributes.() -> Unit): Attributes =
      AttributesImpl(MutableAttributes(this).apply(init))

    companion object {
      val Adapter = Attributes.Adapter
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Attributes) return false
      if (maxNetworks != other.maxNetworks) {
        return false
      }
      if (networks != other.networks) {
        return false
      }
      if (scanMaxTimeSeconds != other.scanMaxTimeSeconds) {
        return false
      }
      if (connectMaxTimeSeconds != other.connectMaxTimeSeconds) {
        return false
      }
      if (interfaceEnabled != other.interfaceEnabled) {
        return false
      }
      if (lastNetworkingStatus != other.lastNetworkingStatus) {
        return false
      }
      if (!(lastNetworkId contentEquals other.lastNetworkId)) {
        return false
      }
      if (lastConnectErrorValue != other.lastConnectErrorValue) {
        return false
      }
      if (supportedWiFiBands != other.supportedWiFiBands) {
        return false
      }
      if (supportedThreadFeatures != other.supportedThreadFeatures) {
        return false
      }
      if (threadVersion != other.threadVersion) {
        return false
      }
      if (generatedCommandList != other.generatedCommandList) {
        return false
      }
      if (acceptedCommandList != other.acceptedCommandList) {
        return false
      }
      if (attributeList != other.attributeList) {
        return false
      }
      if (featureMap != other.featureMap) {
        return false
      }
      if (clusterRevision != other.clusterRevision) {
        return false
      }

      return true
    }

    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (maxNetworks?.hashCode() ?: 0)
      result = 31 * result + (networks?.hashCode() ?: 0)
      result = 31 * result + (scanMaxTimeSeconds?.hashCode() ?: 0)
      result = 31 * result + (connectMaxTimeSeconds?.hashCode() ?: 0)
      result = 31 * result + (interfaceEnabled?.hashCode() ?: 0)
      result = 31 * result + (lastNetworkingStatus?.hashCode() ?: 0)
      result = 31 * result + (lastNetworkId?.contentHashCode() ?: 0)
      result = 31 * result + (lastConnectErrorValue?.hashCode() ?: 0)
      result = 31 * result + (supportedWiFiBands?.hashCode() ?: 0)
      result = 31 * result + (supportedThreadFeatures?.hashCode() ?: 0)
      result = 31 * result + (threadVersion?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "NetworkCommissioning(maxNetworks=$maxNetworks, networks=$networks, scanMaxTimeSeconds=$scanMaxTimeSeconds, connectMaxTimeSeconds=$connectMaxTimeSeconds, interfaceEnabled=$interfaceEnabled, lastNetworkingStatus=$lastNetworkingStatus, lastNetworkId=$lastNetworkId, lastConnectErrorValue=$lastConnectErrorValue, supportedWiFiBands=$supportedWiFiBands, supportedThreadFeatures=$supportedThreadFeatures, threadVersion=$threadVersion, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      maxNetworks: UByte? = this.maxNetworks,
      networks: List<NetworkInfoStruct>? = this.networks,
      scanMaxTimeSeconds: UByte? = this.scanMaxTimeSeconds,
      connectMaxTimeSeconds: UByte? = this.connectMaxTimeSeconds,
      interfaceEnabled: Boolean? = this.interfaceEnabled,
      lastNetworkingStatus: NetworkCommissioningStatusEnum? = this.lastNetworkingStatus,
      lastNetworkId: ByteArray? = this.lastNetworkId,
      lastConnectErrorValue: Int? = this.lastConnectErrorValue,
      supportedWiFiBands: List<WiFiBandEnum>? = this.supportedWiFiBands,
      supportedThreadFeatures: ThreadCapabilitiesBitmap? = this.supportedThreadFeatures,
      threadVersion: UShort? = this.threadVersion,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: Feature = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        maxNetworks = maxNetworks,
        networks = networks,
        scanMaxTimeSeconds = scanMaxTimeSeconds,
        connectMaxTimeSeconds = connectMaxTimeSeconds,
        interfaceEnabled = interfaceEnabled,
        lastNetworkingStatus = lastNetworkingStatus,
        lastNetworkId = lastNetworkId,
        lastConnectErrorValue = lastConnectErrorValue,
        supportedWiFiBands = supportedWiFiBands,
        supportedThreadFeatures = supportedThreadFeatures,
        threadVersion = threadVersion,
        generatedCommandList = generatedCommandList,
        acceptedCommandList = acceptedCommandList,
        attributeList = attributeList,
        featureMap = featureMap,
        clusterRevision = clusterRevision,
      )
  }

  /** @suppress */
  class MutableAttributes(attributes: Attributes) :
    AttributesImpl(
      maxNetworks = attributes.maxNetworks,
      networks = attributes.networks,
      scanMaxTimeSeconds = attributes.scanMaxTimeSeconds,
      connectMaxTimeSeconds = attributes.connectMaxTimeSeconds,
      interfaceEnabled = attributes.interfaceEnabled,
      lastNetworkingStatus = attributes.lastNetworkingStatus,
      lastNetworkId = attributes.lastNetworkId,
      lastConnectErrorValue = attributes.lastConnectErrorValue,
      supportedWiFiBands = attributes.supportedWiFiBands,
      supportedThreadFeatures = attributes.supportedThreadFeatures,
      threadVersion = attributes.threadVersion,
      generatedCommandList = attributes.generatedCommandList,
      acceptedCommandList = attributes.acceptedCommandList,
      attributeList = attributes.attributeList,
      featureMap = attributes.featureMap,
      clusterRevision = attributes.clusterRevision,
    ) {
    internal var _interfaceEnabled: Boolean? = null
    override val interfaceEnabled: Boolean?
      get() {
        return _interfaceEnabled ?: super.interfaceEnabled
      }

    fun setInterfaceEnabled(value: Boolean) {
      _interfaceEnabled = value
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is MutableAttributes) return false
      return super.equals(other)
    }

    override fun toString(): String {
      return "NetworkCommissioning.MutableAttributes(${super.toString()})"
    }

    companion object Adapter : StructAdapter<MutableAttributes> {
      override fun write(writer: ClusterPayloadWriter, value: MutableAttributes) {
        writer.wrapPayload(id = Id)
        if (value._interfaceEnabled != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
            writer.boolean.write(4u, value._interfaceEnabled)
          } else {
            throw HomeException.invalidArgument("interfaceEnabled")
          }
        }
      }

      override fun read(reader: ClusterPayloadReader): MutableAttributes =
        MutableAttributes(Attributes.Adapter.read(reader))
    }
  }

  // Commands

  object ScanNetworksCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(NetworkCommissioningTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "ScanNetworksCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(NetworkCommissioningTrait.Id, 1u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val ssid: OptionalValue<ByteArray?> = OptionalValue.absent(),
      val breadcrumb: OptionalValue<ULong> = OptionalValue.absent(),
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [ssid] command request field. */
        ssid("ssid", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, true),
        /** The [breadcrumb] command request field. */
        breadcrumb("breadcrumb", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  ssid = fields[CommandFields.ssid] as OptionalValue<ByteArray?>,
                  breadcrumb = fields[CommandFields.breadcrumb] as OptionalValue<ULong>,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.ssid.tag -> ssid
          CommandFields.breadcrumb.tag -> breadcrumb
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.ssid)
          writer.ulong.write(1u, value.breadcrumb)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.getOptionalNullable(0u, "Ssid"),
            data.ulong.getOptional(1u, "Breadcrumb"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (ssid != other.ssid) {
          return false
        }
        if (breadcrumb != other.breadcrumb) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + ssid.hashCode()
        result = 31 * result + breadcrumb.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ScanNetworksCommand.Request(ssid=$ssid, breadcrumb=$breadcrumb)"
      }
    }

    /** Optional arguments for the command ScanNetworksCommand Request */
    interface OptionalArgs {
      var ssid: ByteArray?
      var breadcrumb: ULong
    }

    class Response(
      val networkingStatus: NetworkCommissioningStatusEnum = NetworkCommissioningStatusEnum.Success,
      val debugText: String? = null,
      val wiFiScanResults: List<WiFiInterfaceScanResultStruct>? = null,
      val threadScanResults: List<ThreadInterfaceScanResultStruct>? = null,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkingStatus] command request field. */
        networkingStatus(
          "networkingStatus",
          0u,
          "NetworkCommissioningStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [wiFiScanResults] command request field. */
        wiFiScanResults(
          "wiFiScanResults",
          2u,
          "WiFiInterfaceScanResultStruct",
          FieldType.Struct,
          true,
          WiFiInterfaceScanResultStruct.Adapter,
          false,
        ),
        /** The [threadScanResults] command request field. */
        threadScanResults(
          "threadScanResults",
          3u,
          "ThreadInterfaceScanResultStruct",
          FieldType.Struct,
          true,
          ThreadInterfaceScanResultStruct.Adapter,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  networkingStatus =
                    fields[CommandFields.networkingStatus] as NetworkCommissioningStatusEnum,
                  debugText = fields[CommandFields.debugText] as String?,
                  wiFiScanResults =
                    fields[CommandFields.wiFiScanResults] as List<WiFiInterfaceScanResultStruct>?,
                  threadScanResults =
                    fields[CommandFields.threadScanResults]
                      as List<ThreadInterfaceScanResultStruct>?,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkingStatus.tag -> networkingStatus
          CommandFields.debugText.tag -> debugText
          CommandFields.wiFiScanResults.tag -> wiFiScanResults
          CommandFields.threadScanResults.tag -> threadScanResults
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NetworkCommissioningStatusEnum.Adapter).write(0u, value.networkingStatus)
          writer.string.write(1u, value.debugText)
          writer.struct(WiFiInterfaceScanResultStruct.Adapter).writeList(2u, value.wiFiScanResults)
          writer
            .struct(ThreadInterfaceScanResultStruct.Adapter)
            .writeList(3u, value.threadScanResults)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data =
            reader.readPayload(
              mapOf(
                2u to WiFiInterfaceScanResultStruct.Adapter,
                3u to ThreadInterfaceScanResultStruct.Adapter,
              )
            )
          return Response(
            data.enum(NetworkCommissioningStatusEnum.Adapter).get(0u, "NetworkingStatus"),
            data.string.getOptionalNullable(1u, "DebugText").getOrNull(),
            data
              .struct { WiFiInterfaceScanResultStruct() }
              .getOptionalNullableList(2u, "WiFiScanResults")
              .getOrNull(),
            data
              .struct { ThreadInterfaceScanResultStruct() }
              .getOptionalNullableList(3u, "ThreadScanResults")
              .getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (networkingStatus != other.networkingStatus) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }
        if (wiFiScanResults != other.wiFiScanResults) {
          return false
        }
        if (threadScanResults != other.threadScanResults) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkingStatus.hashCode()
        result = 31 * result + (debugText?.hashCode() ?: 0)
        result = 31 * result + (wiFiScanResults?.hashCode() ?: 0)
        result = 31 * result + (threadScanResults?.hashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ScanNetworksCommand.Response(networkingStatus=$networkingStatus, debugText=$debugText, wiFiScanResults=$wiFiScanResults, threadScanResults=$threadScanResults)"
      }
    }
  }

  object AddOrUpdateWiFiNetworkCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(NetworkCommissioningTrait.Id, 2u)
    override val commandId = requestId.toString()
    override val commandName = "AddOrUpdateWiFiNetworkCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(NetworkCommissioningTrait.Id, 5u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val ssid: ByteArray = ByteArray(0),
      val credentials: ByteArray = ByteArray(0),
      val breadcrumb: OptionalValue<ULong> = OptionalValue.absent(),
      val networkIdentity: OptionalValue<ByteArray> = OptionalValue.absent(),
      val clientIdentifier: OptionalValue<ByteArray> = OptionalValue.absent(),
      val possessionNonce: OptionalValue<ByteArray> = OptionalValue.absent(),
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [ssid] command request field. */
        ssid("ssid", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [credentials] command request field. */
        credentials(
          "credentials",
          1u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [breadcrumb] command request field. */
        breadcrumb("breadcrumb", 2u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
        /** The [networkIdentity] command request field. */
        networkIdentity(
          "networkIdentity",
          3u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [clientIdentifier] command request field. */
        clientIdentifier(
          "clientIdentifier",
          4u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [possessionNonce] command request field. */
        possessionNonce(
          "possessionNonce",
          5u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  ssid = fields[CommandFields.ssid] as ByteArray,
                  credentials = fields[CommandFields.credentials] as ByteArray,
                  breadcrumb = fields[CommandFields.breadcrumb] as OptionalValue<ULong>,
                  networkIdentity =
                    fields[CommandFields.networkIdentity] as OptionalValue<ByteArray>,
                  clientIdentifier =
                    fields[CommandFields.clientIdentifier] as OptionalValue<ByteArray>,
                  possessionNonce =
                    fields[CommandFields.possessionNonce] as OptionalValue<ByteArray>,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.ssid.tag -> ssid
          CommandFields.credentials.tag -> credentials
          CommandFields.breadcrumb.tag -> breadcrumb
          CommandFields.networkIdentity.tag -> networkIdentity
          CommandFields.clientIdentifier.tag -> clientIdentifier
          CommandFields.possessionNonce.tag -> possessionNonce
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.ssid)
          writer.bytearray.write(1u, value.credentials)
          writer.ulong.write(2u, value.breadcrumb)
          writer.bytearray.write(3u, value.networkIdentity)
          writer.bytearray.write(4u, value.clientIdentifier)
          writer.bytearray.write(5u, value.possessionNonce)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "Ssid"),
            data.bytearray.get(1u, "Credentials"),
            data.ulong.getOptional(2u, "Breadcrumb"),
            data.bytearray.getOptional(3u, "NetworkIdentity"),
            data.bytearray.getOptional(4u, "ClientIdentifier"),
            data.bytearray.getOptional(5u, "PossessionNonce"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(ssid contentEquals other.ssid)) {
          return false
        }
        if (!(credentials contentEquals other.credentials)) {
          return false
        }
        if (breadcrumb != other.breadcrumb) {
          return false
        }
        if (networkIdentity != other.networkIdentity) {
          return false
        }
        if (clientIdentifier != other.clientIdentifier) {
          return false
        }
        if (possessionNonce != other.possessionNonce) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + ssid.contentHashCode()
        result = 31 * result + credentials.contentHashCode()
        result = 31 * result + breadcrumb.hashCode()
        result = 31 * result + networkIdentity.hashCode()
        result = 31 * result + clientIdentifier.hashCode()
        result = 31 * result + possessionNonce.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AddOrUpdateWiFiNetworkCommand.Request(ssid=$ssid, credentials=$credentials, breadcrumb=$breadcrumb, networkIdentity=$networkIdentity, clientIdentifier=$clientIdentifier, possessionNonce=$possessionNonce)"
      }
    }

    /** Optional arguments for the command AddOrUpdateWiFiNetworkCommand Request */
    interface OptionalArgs {
      var breadcrumb: ULong
      var networkIdentity: ByteArray
      var clientIdentifier: ByteArray
      var possessionNonce: ByteArray
    }

    class Response(
      val networkingStatus: NetworkCommissioningStatusEnum = NetworkCommissioningStatusEnum.Success,
      val debugText: String? = null,
      val networkIndex: UByte? = null,
      val clientIdentity: ByteArray? = null,
      val possessionSignature: ByteArray? = null,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkingStatus] command request field. */
        networkingStatus(
          "networkingStatus",
          0u,
          "NetworkCommissioningStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [networkIndex] command request field. */
        networkIndex("networkIndex", 2u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [clientIdentity] command request field. */
        clientIdentity(
          "clientIdentity",
          3u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [possessionSignature] command request field. */
        possessionSignature(
          "possessionSignature",
          4u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  networkingStatus =
                    fields[CommandFields.networkingStatus] as NetworkCommissioningStatusEnum,
                  debugText = fields[CommandFields.debugText] as String?,
                  networkIndex = fields[CommandFields.networkIndex] as UByte?,
                  clientIdentity = fields[CommandFields.clientIdentity] as ByteArray?,
                  possessionSignature = fields[CommandFields.possessionSignature] as ByteArray?,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkingStatus.tag -> networkingStatus
          CommandFields.debugText.tag -> debugText
          CommandFields.networkIndex.tag -> networkIndex
          CommandFields.clientIdentity.tag -> clientIdentity
          CommandFields.possessionSignature.tag -> possessionSignature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NetworkCommissioningStatusEnum.Adapter).write(0u, value.networkingStatus)
          writer.string.write(1u, value.debugText)
          writer.ubyte.write(2u, value.networkIndex)
          writer.bytearray.write(3u, value.clientIdentity)
          writer.bytearray.write(4u, value.possessionSignature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NetworkCommissioningStatusEnum.Adapter).get(0u, "NetworkingStatus"),
            data.string.getOptionalNullable(1u, "DebugText").getOrNull(),
            data.ubyte.getOptionalNullable(2u, "NetworkIndex").getOrNull(),
            data.bytearray.getOptionalNullable(3u, "ClientIdentity").getOrNull(),
            data.bytearray.getOptionalNullable(4u, "PossessionSignature").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (networkingStatus != other.networkingStatus) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }
        if (networkIndex != other.networkIndex) {
          return false
        }
        if (!(clientIdentity contentEquals other.clientIdentity)) {
          return false
        }
        if (!(possessionSignature contentEquals other.possessionSignature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkingStatus.hashCode()
        result = 31 * result + (debugText?.hashCode() ?: 0)
        result = 31 * result + (networkIndex?.hashCode() ?: 0)
        result = 31 * result + (clientIdentity?.contentHashCode() ?: 0)
        result = 31 * result + (possessionSignature?.contentHashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AddOrUpdateWiFiNetworkCommand.Response(networkingStatus=$networkingStatus, debugText=$debugText, networkIndex=$networkIndex, clientIdentity=$clientIdentity, possessionSignature=$possessionSignature)"
      }
    }
  }

  object AddOrUpdateThreadNetworkCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(NetworkCommissioningTrait.Id, 3u)
    override val commandId = requestId.toString()
    override val commandName = "AddOrUpdateThreadNetworkCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(NetworkCommissioningTrait.Id, 5u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val operationalDataset: ByteArray = ByteArray(0),
      val breadcrumb: OptionalValue<ULong> = OptionalValue.absent(),
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [operationalDataset] command request field. */
        operationalDataset(
          "operationalDataset",
          0u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [breadcrumb] command request field. */
        breadcrumb("breadcrumb", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  operationalDataset = fields[CommandFields.operationalDataset] as ByteArray,
                  breadcrumb = fields[CommandFields.breadcrumb] as OptionalValue<ULong>,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.operationalDataset.tag -> operationalDataset
          CommandFields.breadcrumb.tag -> breadcrumb
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.operationalDataset)
          writer.ulong.write(1u, value.breadcrumb)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "OperationalDataset"),
            data.ulong.getOptional(1u, "Breadcrumb"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(operationalDataset contentEquals other.operationalDataset)) {
          return false
        }
        if (breadcrumb != other.breadcrumb) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + operationalDataset.contentHashCode()
        result = 31 * result + breadcrumb.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AddOrUpdateThreadNetworkCommand.Request(operationalDataset=$operationalDataset, breadcrumb=$breadcrumb)"
      }
    }

    /** Optional arguments for the command AddOrUpdateThreadNetworkCommand Request */
    interface OptionalArgs {
      var breadcrumb: ULong
    }

    class Response(
      val networkingStatus: NetworkCommissioningStatusEnum = NetworkCommissioningStatusEnum.Success,
      val debugText: String? = null,
      val networkIndex: UByte? = null,
      val clientIdentity: ByteArray? = null,
      val possessionSignature: ByteArray? = null,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkingStatus] command request field. */
        networkingStatus(
          "networkingStatus",
          0u,
          "NetworkCommissioningStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [networkIndex] command request field. */
        networkIndex("networkIndex", 2u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [clientIdentity] command request field. */
        clientIdentity(
          "clientIdentity",
          3u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [possessionSignature] command request field. */
        possessionSignature(
          "possessionSignature",
          4u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  networkingStatus =
                    fields[CommandFields.networkingStatus] as NetworkCommissioningStatusEnum,
                  debugText = fields[CommandFields.debugText] as String?,
                  networkIndex = fields[CommandFields.networkIndex] as UByte?,
                  clientIdentity = fields[CommandFields.clientIdentity] as ByteArray?,
                  possessionSignature = fields[CommandFields.possessionSignature] as ByteArray?,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkingStatus.tag -> networkingStatus
          CommandFields.debugText.tag -> debugText
          CommandFields.networkIndex.tag -> networkIndex
          CommandFields.clientIdentity.tag -> clientIdentity
          CommandFields.possessionSignature.tag -> possessionSignature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NetworkCommissioningStatusEnum.Adapter).write(0u, value.networkingStatus)
          writer.string.write(1u, value.debugText)
          writer.ubyte.write(2u, value.networkIndex)
          writer.bytearray.write(3u, value.clientIdentity)
          writer.bytearray.write(4u, value.possessionSignature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NetworkCommissioningStatusEnum.Adapter).get(0u, "NetworkingStatus"),
            data.string.getOptionalNullable(1u, "DebugText").getOrNull(),
            data.ubyte.getOptionalNullable(2u, "NetworkIndex").getOrNull(),
            data.bytearray.getOptionalNullable(3u, "ClientIdentity").getOrNull(),
            data.bytearray.getOptionalNullable(4u, "PossessionSignature").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (networkingStatus != other.networkingStatus) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }
        if (networkIndex != other.networkIndex) {
          return false
        }
        if (!(clientIdentity contentEquals other.clientIdentity)) {
          return false
        }
        if (!(possessionSignature contentEquals other.possessionSignature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkingStatus.hashCode()
        result = 31 * result + (debugText?.hashCode() ?: 0)
        result = 31 * result + (networkIndex?.hashCode() ?: 0)
        result = 31 * result + (clientIdentity?.contentHashCode() ?: 0)
        result = 31 * result + (possessionSignature?.contentHashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AddOrUpdateThreadNetworkCommand.Response(networkingStatus=$networkingStatus, debugText=$debugText, networkIndex=$networkIndex, clientIdentity=$clientIdentity, possessionSignature=$possessionSignature)"
      }
    }
  }

  object RemoveNetworkCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(NetworkCommissioningTrait.Id, 4u)
    override val commandId = requestId.toString()
    override val commandName = "RemoveNetworkCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(NetworkCommissioningTrait.Id, 5u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val networkId: ByteArray = ByteArray(0),
      val breadcrumb: OptionalValue<ULong> = OptionalValue.absent(),
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkId] command request field. */
        networkId("networkId", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [breadcrumb] command request field. */
        breadcrumb("breadcrumb", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  networkId = fields[CommandFields.networkId] as ByteArray,
                  breadcrumb = fields[CommandFields.breadcrumb] as OptionalValue<ULong>,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkId.tag -> networkId
          CommandFields.breadcrumb.tag -> breadcrumb
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.networkId)
          writer.ulong.write(1u, value.breadcrumb)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "NetworkId"),
            data.ulong.getOptional(1u, "Breadcrumb"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(networkId contentEquals other.networkId)) {
          return false
        }
        if (breadcrumb != other.breadcrumb) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkId.contentHashCode()
        result = 31 * result + breadcrumb.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "RemoveNetworkCommand.Request(networkId=$networkId, breadcrumb=$breadcrumb)"
      }
    }

    /** Optional arguments for the command RemoveNetworkCommand Request */
    interface OptionalArgs {
      var breadcrumb: ULong
    }

    class Response(
      val networkingStatus: NetworkCommissioningStatusEnum = NetworkCommissioningStatusEnum.Success,
      val debugText: String? = null,
      val networkIndex: UByte? = null,
      val clientIdentity: ByteArray? = null,
      val possessionSignature: ByteArray? = null,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkingStatus] command request field. */
        networkingStatus(
          "networkingStatus",
          0u,
          "NetworkCommissioningStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [networkIndex] command request field. */
        networkIndex("networkIndex", 2u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [clientIdentity] command request field. */
        clientIdentity(
          "clientIdentity",
          3u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [possessionSignature] command request field. */
        possessionSignature(
          "possessionSignature",
          4u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  networkingStatus =
                    fields[CommandFields.networkingStatus] as NetworkCommissioningStatusEnum,
                  debugText = fields[CommandFields.debugText] as String?,
                  networkIndex = fields[CommandFields.networkIndex] as UByte?,
                  clientIdentity = fields[CommandFields.clientIdentity] as ByteArray?,
                  possessionSignature = fields[CommandFields.possessionSignature] as ByteArray?,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkingStatus.tag -> networkingStatus
          CommandFields.debugText.tag -> debugText
          CommandFields.networkIndex.tag -> networkIndex
          CommandFields.clientIdentity.tag -> clientIdentity
          CommandFields.possessionSignature.tag -> possessionSignature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NetworkCommissioningStatusEnum.Adapter).write(0u, value.networkingStatus)
          writer.string.write(1u, value.debugText)
          writer.ubyte.write(2u, value.networkIndex)
          writer.bytearray.write(3u, value.clientIdentity)
          writer.bytearray.write(4u, value.possessionSignature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NetworkCommissioningStatusEnum.Adapter).get(0u, "NetworkingStatus"),
            data.string.getOptionalNullable(1u, "DebugText").getOrNull(),
            data.ubyte.getOptionalNullable(2u, "NetworkIndex").getOrNull(),
            data.bytearray.getOptionalNullable(3u, "ClientIdentity").getOrNull(),
            data.bytearray.getOptionalNullable(4u, "PossessionSignature").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (networkingStatus != other.networkingStatus) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }
        if (networkIndex != other.networkIndex) {
          return false
        }
        if (!(clientIdentity contentEquals other.clientIdentity)) {
          return false
        }
        if (!(possessionSignature contentEquals other.possessionSignature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkingStatus.hashCode()
        result = 31 * result + (debugText?.hashCode() ?: 0)
        result = 31 * result + (networkIndex?.hashCode() ?: 0)
        result = 31 * result + (clientIdentity?.contentHashCode() ?: 0)
        result = 31 * result + (possessionSignature?.contentHashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "RemoveNetworkCommand.Response(networkingStatus=$networkingStatus, debugText=$debugText, networkIndex=$networkIndex, clientIdentity=$clientIdentity, possessionSignature=$possessionSignature)"
      }
    }
  }

  object ConnectNetworkCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(NetworkCommissioningTrait.Id, 6u)
    override val commandId = requestId.toString()
    override val commandName = "ConnectNetworkCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(NetworkCommissioningTrait.Id, 7u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val networkId: ByteArray = ByteArray(0),
      val breadcrumb: OptionalValue<ULong> = OptionalValue.absent(),
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkId] command request field. */
        networkId("networkId", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [breadcrumb] command request field. */
        breadcrumb("breadcrumb", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  networkId = fields[CommandFields.networkId] as ByteArray,
                  breadcrumb = fields[CommandFields.breadcrumb] as OptionalValue<ULong>,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkId.tag -> networkId
          CommandFields.breadcrumb.tag -> breadcrumb
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.networkId)
          writer.ulong.write(1u, value.breadcrumb)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "NetworkId"),
            data.ulong.getOptional(1u, "Breadcrumb"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(networkId contentEquals other.networkId)) {
          return false
        }
        if (breadcrumb != other.breadcrumb) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkId.contentHashCode()
        result = 31 * result + breadcrumb.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ConnectNetworkCommand.Request(networkId=$networkId, breadcrumb=$breadcrumb)"
      }
    }

    /** Optional arguments for the command ConnectNetworkCommand Request */
    interface OptionalArgs {
      var breadcrumb: ULong
    }

    class Response(
      val networkingStatus: NetworkCommissioningStatusEnum = NetworkCommissioningStatusEnum.Success,
      val debugText: String? = null,
      val errorValue: Int? = null,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkingStatus] command request field. */
        networkingStatus(
          "networkingStatus",
          0u,
          "NetworkCommissioningStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [errorValue] command request field. */
        errorValue("errorValue", 2u, "Int", FieldType.Int, false, NoOpDescriptor, true);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  networkingStatus =
                    fields[CommandFields.networkingStatus] as NetworkCommissioningStatusEnum,
                  debugText = fields[CommandFields.debugText] as String?,
                  errorValue = fields[CommandFields.errorValue] as Int?,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkingStatus.tag -> networkingStatus
          CommandFields.debugText.tag -> debugText
          CommandFields.errorValue.tag -> errorValue
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NetworkCommissioningStatusEnum.Adapter).write(0u, value.networkingStatus)
          writer.string.write(1u, value.debugText)
          writer.int.write(2u, value.errorValue)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NetworkCommissioningStatusEnum.Adapter).get(0u, "NetworkingStatus"),
            data.string.getOptionalNullable(1u, "DebugText").getOrNull(),
            data.int.getNullable(2u, "ErrorValue"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (networkingStatus != other.networkingStatus) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }
        if (errorValue != other.errorValue) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkingStatus.hashCode()
        result = 31 * result + (debugText?.hashCode() ?: 0)
        result = 31 * result + (errorValue?.hashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ConnectNetworkCommand.Response(networkingStatus=$networkingStatus, debugText=$debugText, errorValue=$errorValue)"
      }
    }
  }

  object ReorderNetworkCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(NetworkCommissioningTrait.Id, 8u)
    override val commandId = requestId.toString()
    override val commandName = "ReorderNetworkCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(NetworkCommissioningTrait.Id, 5u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val networkId: ByteArray = ByteArray(0),
      val networkIndex: UByte = 0u,
      val breadcrumb: OptionalValue<ULong> = OptionalValue.absent(),
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkId] command request field. */
        networkId("networkId", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [networkIndex] command request field. */
        networkIndex("networkIndex", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [breadcrumb] command request field. */
        breadcrumb("breadcrumb", 2u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  networkId = fields[CommandFields.networkId] as ByteArray,
                  networkIndex = fields[CommandFields.networkIndex] as UByte,
                  breadcrumb = fields[CommandFields.breadcrumb] as OptionalValue<ULong>,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkId.tag -> networkId
          CommandFields.networkIndex.tag -> networkIndex
          CommandFields.breadcrumb.tag -> breadcrumb
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.networkId)
          writer.ubyte.write(1u, value.networkIndex)
          writer.ulong.write(2u, value.breadcrumb)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "NetworkId"),
            data.ubyte.get(1u, "NetworkIndex"),
            data.ulong.getOptional(2u, "Breadcrumb"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(networkId contentEquals other.networkId)) {
          return false
        }
        if (networkIndex != other.networkIndex) {
          return false
        }
        if (breadcrumb != other.breadcrumb) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkId.contentHashCode()
        result = 31 * result + networkIndex.hashCode()
        result = 31 * result + breadcrumb.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ReorderNetworkCommand.Request(networkId=$networkId, networkIndex=$networkIndex, breadcrumb=$breadcrumb)"
      }
    }

    /** Optional arguments for the command ReorderNetworkCommand Request */
    interface OptionalArgs {
      var breadcrumb: ULong
    }

    class Response(
      val networkingStatus: NetworkCommissioningStatusEnum = NetworkCommissioningStatusEnum.Success,
      val debugText: String? = null,
      val networkIndex: UByte? = null,
      val clientIdentity: ByteArray? = null,
      val possessionSignature: ByteArray? = null,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [networkingStatus] command request field. */
        networkingStatus(
          "networkingStatus",
          0u,
          "NetworkCommissioningStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [networkIndex] command request field. */
        networkIndex("networkIndex", 2u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [clientIdentity] command request field. */
        clientIdentity(
          "clientIdentity",
          3u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [possessionSignature] command request field. */
        possessionSignature(
          "possessionSignature",
          4u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  networkingStatus =
                    fields[CommandFields.networkingStatus] as NetworkCommissioningStatusEnum,
                  debugText = fields[CommandFields.debugText] as String?,
                  networkIndex = fields[CommandFields.networkIndex] as UByte?,
                  clientIdentity = fields[CommandFields.clientIdentity] as ByteArray?,
                  possessionSignature = fields[CommandFields.possessionSignature] as ByteArray?,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.networkingStatus.tag -> networkingStatus
          CommandFields.debugText.tag -> debugText
          CommandFields.networkIndex.tag -> networkIndex
          CommandFields.clientIdentity.tag -> clientIdentity
          CommandFields.possessionSignature.tag -> possessionSignature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NetworkCommissioningStatusEnum.Adapter).write(0u, value.networkingStatus)
          writer.string.write(1u, value.debugText)
          writer.ubyte.write(2u, value.networkIndex)
          writer.bytearray.write(3u, value.clientIdentity)
          writer.bytearray.write(4u, value.possessionSignature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NetworkCommissioningStatusEnum.Adapter).get(0u, "NetworkingStatus"),
            data.string.getOptionalNullable(1u, "DebugText").getOrNull(),
            data.ubyte.getOptionalNullable(2u, "NetworkIndex").getOrNull(),
            data.bytearray.getOptionalNullable(3u, "ClientIdentity").getOrNull(),
            data.bytearray.getOptionalNullable(4u, "PossessionSignature").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (networkingStatus != other.networkingStatus) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }
        if (networkIndex != other.networkIndex) {
          return false
        }
        if (!(clientIdentity contentEquals other.clientIdentity)) {
          return false
        }
        if (!(possessionSignature contentEquals other.possessionSignature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + networkingStatus.hashCode()
        result = 31 * result + (debugText?.hashCode() ?: 0)
        result = 31 * result + (networkIndex?.hashCode() ?: 0)
        result = 31 * result + (clientIdentity?.contentHashCode() ?: 0)
        result = 31 * result + (possessionSignature?.contentHashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ReorderNetworkCommand.Response(networkingStatus=$networkingStatus, debugText=$debugText, networkIndex=$networkIndex, clientIdentity=$clientIdentity, possessionSignature=$possessionSignature)"
      }
    }
  }

  object QueryIdentityCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(NetworkCommissioningTrait.Id, 9u)
    override val commandId = requestId.toString()
    override val commandName = "QueryIdentityCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(NetworkCommissioningTrait.Id, 10u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val keyIdentifier: ByteArray = ByteArray(0),
      val possessionNonce: OptionalValue<ByteArray> = OptionalValue.absent(),
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [keyIdentifier] command request field. */
        keyIdentifier(
          "keyIdentifier",
          0u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [possessionNonce] command request field. */
        possessionNonce(
          "possessionNonce",
          1u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  keyIdentifier = fields[CommandFields.keyIdentifier] as ByteArray,
                  possessionNonce =
                    fields[CommandFields.possessionNonce] as OptionalValue<ByteArray>,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.keyIdentifier.tag -> keyIdentifier
          CommandFields.possessionNonce.tag -> possessionNonce
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.keyIdentifier)
          writer.bytearray.write(1u, value.possessionNonce)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "KeyIdentifier"),
            data.bytearray.getOptional(1u, "PossessionNonce"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(keyIdentifier contentEquals other.keyIdentifier)) {
          return false
        }
        if (possessionNonce != other.possessionNonce) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + keyIdentifier.contentHashCode()
        result = 31 * result + possessionNonce.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "QueryIdentityCommand.Request(keyIdentifier=$keyIdentifier, possessionNonce=$possessionNonce)"
      }
    }

    /** Optional arguments for the command QueryIdentityCommand Request */
    interface OptionalArgs {
      var possessionNonce: ByteArray
    }

    class Response(
      val identity: ByteArray = ByteArray(0),
      val possessionSignature: ByteArray? = null,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @OptIn(HomeExperimentalGenericApi::class)
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [identity] command request field. */
        identity("identity", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [possessionSignature] command request field. */
        possessionSignature(
          "possessionSignature",
          1u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  identity = fields[CommandFields.identity] as ByteArray,
                  possessionSignature = fields[CommandFields.possessionSignature] as ByteArray?,
                )
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.identity.tag -> identity
          CommandFields.possessionSignature.tag -> possessionSignature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.bytearray.write(0u, value.identity)
          writer.bytearray.write(1u, value.possessionSignature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.bytearray.get(0u, "Identity"),
            data.bytearray.getOptionalNullable(1u, "PossessionSignature").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (!(identity contentEquals other.identity)) {
          return false
        }
        if (!(possessionSignature contentEquals other.possessionSignature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + identity.contentHashCode()
        result = 31 * result + (possessionSignature?.contentHashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "QueryIdentityCommand.Response(identity=$identity, possessionSignature=$possessionSignature)"
      }
    }
  }
}
