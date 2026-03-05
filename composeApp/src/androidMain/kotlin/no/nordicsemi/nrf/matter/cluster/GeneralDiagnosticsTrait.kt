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
import com.google.home.NoOpDescriptor
import com.google.home.StructDescriptor
import com.google.home.TagId
import com.google.home.Type as FieldType
import com.google.home.annotation.HomeExperimentalGenericApi
import com.google.home.automation.TypedExpression
import com.google.home.automation.fieldSelect
import com.google.home.matter.serialization.Bitmap
import com.google.home.matter.serialization.BitmapAdapter
import com.google.home.matter.serialization.ClusterBitmap
import com.google.home.matter.serialization.ClusterBitmapFlag
import com.google.home.matter.serialization.ClusterEnum
import com.google.home.matter.serialization.ClusterId
import com.google.home.matter.serialization.ClusterPayloadReader
import com.google.home.matter.serialization.ClusterPayloadWriter
import com.google.home.matter.serialization.EnumAdapter
import com.google.home.matter.serialization.MutableBitmap
import com.google.home.matter.serialization.ScopedCommandId
import com.google.home.matter.serialization.ScopedEventId
import com.google.home.matter.serialization.StructAdapter
import com.google.home.matter.serialization.camelToSnakeUpper
import com.google.home.matter.serialization.unwrapPayload
import com.google.home.matter.serialization.upperCamelToSnakeUpper
import com.google.home.matter.serialization.wrapPayload
import com.google.home.toBitmapDescriptor
import com.google.home.toDescriptorMap
import com.google.home.toEnumDescriptor
import javax.annotation.processing.Generated
import kotlin.collections.contentDeepEquals
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode

/*
 * Serialization object for GeneralDiagnosticsTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for GeneralDiagnosticsTrait. */
@Generated("GoogleHomePlatformCodegen")
object GeneralDiagnosticsTrait {
  val Id = ClusterId(51u, "GeneralDiagnostics")

  // Enums
  enum class BootReasonEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(51u).traitId,
    override val typeName: String = "BootReasonEnum",
  ) : ClusterEnum {
    Unspecified(0u),
    PowerOnReboot(1u),
    BrownOutReset(2u),
    SoftwareWatchdogReset(3u),
    HardwareWatchdogReset(4u),
    SoftwareUpdateCompleted(5u),
    SoftwareReset(6u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "BootReasonEnum".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "BootReasonEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class HardwareFaultEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(51u).traitId,
    override val typeName: String = "HardwareFaultEnum",
  ) : ClusterEnum {
    Unspecified(0u),
    Radio(1u),
    Sensor(2u),
    ResettableOverTemp(3u),
    NonResettableOverTemp(4u),
    PowerSource(5u),
    VisualDisplayFault(6u),
    AudioOutputFault(7u),
    UserInterfaceFault(8u),
    NonVolatileMemoryError(9u),
    TamperDetected(10u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "HardwareFaultEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "HardwareFaultEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class InterfaceTypeEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(51u).traitId,
    override val typeName: String = "InterfaceTypeEnum",
  ) : ClusterEnum {
    Unspecified(0u),
    WiFi(1u),
    Ethernet(2u),
    Cellular(3u),
    Thread(4u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "InterfaceTypeEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "InterfaceTypeEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class NetworkFaultEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(51u).traitId,
    override val typeName: String = "NetworkFaultEnum",
  ) : ClusterEnum {
    Unspecified(0u),
    HardwareFailure(1u),
    NetworkJammed(2u),
    ConnectionFailed(3u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "NetworkFaultEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "NetworkFaultEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class RadioFaultEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(51u).traitId,
    override val typeName: String = "RadioFaultEnum",
  ) : ClusterEnum {
    Unspecified(0u),
    WiFiFault(1u),
    CellularFault(2u),
    ThreadFault(3u),
    NfcFault(4u),
    BLEFault(5u),
    EthernetFault(6u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "RadioFaultEnum".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "RadioFaultEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps
  data class Feature(val dataModelTest: Boolean = false) :
    ClusterBitmap(traitId = ClusterId(51u).traitId, bitmapName = "Feature") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      DataModelTest(0x1u)
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<Feature> {
          override fun toRaw(value: Feature): Bitmap =
            MutableBitmap().also { it[MaskFlags.DataModelTest.value] = value.dataModelTest }

          override fun toRuntime(value: Bitmap): Feature =
            Feature(value[MaskFlags.DataModelTest.value])
        }

      val BitmapDescriptor =
        object : BitmapDescriptor {
          override val name: String = "Feature"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = MaskFlags.entries.toBitmapDescriptor()
        }
    }
  }

  // Events
  interface HardwareFaultChange : ClusterStruct {
    val current: List<HardwareFaultEnum>?
    val previous: List<HardwareFaultEnum>?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      GeneralDiagnostics.HardwareFaultChangeEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        GeneralDiagnostics.HardwareFaultChangeEvent.EventFields.current.tag -> current
        GeneralDiagnostics.HardwareFaultChangeEvent.EventFields.previous.tag -> previous
        else -> null
      }
    }
  }

  /** @suppress */
  class HardwareFaultChangeImpl(
    override val current: List<HardwareFaultEnum>? = null,
    override val previous: List<HardwareFaultEnum>? = null,
  ) : HardwareFaultChange {
    /** @suppress */
    companion object Adapter : StructAdapter<HardwareFaultChange> {
      val Id = ScopedEventId(GeneralDiagnosticsTrait.Id, 0u)

      override fun write(writer: ClusterPayloadWriter, value: HardwareFaultChange) {
        writer.wrapPayload(id = Id)
        writer.enum(HardwareFaultEnum.Adapter).writeList(0u, value.current)
        writer.enum(HardwareFaultEnum.Adapter).writeList(1u, value.previous)
      }

      override fun read(reader: ClusterPayloadReader): HardwareFaultChange {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return HardwareFaultChangeImpl(
          data.enum(HardwareFaultEnum.Adapter).getOptionalNullableList(0u, "Current").getOrNull(),
          data.enum(HardwareFaultEnum.Adapter).getOptionalNullableList(1u, "Previous").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is HardwareFaultChange) return false
      if (current != other.current) {
        return false
      }
      if (previous != other.previous) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (current?.hashCode() ?: 0)
      result = 31 * result + (previous?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "HardwareFaultChange(current=$current, previous=$previous)"
    }
  }

  interface RadioFaultChange : ClusterStruct {
    val current: List<RadioFaultEnum>?
    val previous: List<RadioFaultEnum>?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      GeneralDiagnostics.RadioFaultChangeEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        GeneralDiagnostics.RadioFaultChangeEvent.EventFields.current.tag -> current
        GeneralDiagnostics.RadioFaultChangeEvent.EventFields.previous.tag -> previous
        else -> null
      }
    }
  }

  /** @suppress */
  class RadioFaultChangeImpl(
    override val current: List<RadioFaultEnum>? = null,
    override val previous: List<RadioFaultEnum>? = null,
  ) : RadioFaultChange {
    /** @suppress */
    companion object Adapter : StructAdapter<RadioFaultChange> {
      val Id = ScopedEventId(GeneralDiagnosticsTrait.Id, 1u)

      override fun write(writer: ClusterPayloadWriter, value: RadioFaultChange) {
        writer.wrapPayload(id = Id)
        writer.enum(RadioFaultEnum.Adapter).writeList(0u, value.current)
        writer.enum(RadioFaultEnum.Adapter).writeList(1u, value.previous)
      }

      override fun read(reader: ClusterPayloadReader): RadioFaultChange {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return RadioFaultChangeImpl(
          data.enum(RadioFaultEnum.Adapter).getOptionalNullableList(0u, "Current").getOrNull(),
          data.enum(RadioFaultEnum.Adapter).getOptionalNullableList(1u, "Previous").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is RadioFaultChange) return false
      if (current != other.current) {
        return false
      }
      if (previous != other.previous) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (current?.hashCode() ?: 0)
      result = 31 * result + (previous?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "RadioFaultChange(current=$current, previous=$previous)"
    }
  }

  interface NetworkFaultChange : ClusterStruct {
    val current: List<NetworkFaultEnum>?
    val previous: List<NetworkFaultEnum>?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      GeneralDiagnostics.NetworkFaultChangeEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        GeneralDiagnostics.NetworkFaultChangeEvent.EventFields.current.tag -> current
        GeneralDiagnostics.NetworkFaultChangeEvent.EventFields.previous.tag -> previous
        else -> null
      }
    }
  }

  /** @suppress */
  class NetworkFaultChangeImpl(
    override val current: List<NetworkFaultEnum>? = null,
    override val previous: List<NetworkFaultEnum>? = null,
  ) : NetworkFaultChange {
    /** @suppress */
    companion object Adapter : StructAdapter<NetworkFaultChange> {
      val Id = ScopedEventId(GeneralDiagnosticsTrait.Id, 2u)

      override fun write(writer: ClusterPayloadWriter, value: NetworkFaultChange) {
        writer.wrapPayload(id = Id)
        writer.enum(NetworkFaultEnum.Adapter).writeList(0u, value.current)
        writer.enum(NetworkFaultEnum.Adapter).writeList(1u, value.previous)
      }

      override fun read(reader: ClusterPayloadReader): NetworkFaultChange {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return NetworkFaultChangeImpl(
          data.enum(NetworkFaultEnum.Adapter).getOptionalNullableList(0u, "Current").getOrNull(),
          data.enum(NetworkFaultEnum.Adapter).getOptionalNullableList(1u, "Previous").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is NetworkFaultChange) return false
      if (current != other.current) {
        return false
      }
      if (previous != other.previous) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (current?.hashCode() ?: 0)
      result = 31 * result + (previous?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "NetworkFaultChange(current=$current, previous=$previous)"
    }
  }

  interface BootReason : ClusterStruct {
    val bootReason: BootReasonEnum?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      GeneralDiagnostics.BootReasonEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        GeneralDiagnostics.BootReasonEvent.EventFields.bootReason.tag -> bootReason
        else -> null
      }
    }
  }

  /** @suppress */
  class BootReasonImpl(override val bootReason: BootReasonEnum? = null) : BootReason {
    /** @suppress */
    companion object Adapter : StructAdapter<BootReason> {
      val Id = ScopedEventId(GeneralDiagnosticsTrait.Id, 3u)

      override fun write(writer: ClusterPayloadWriter, value: BootReason) {
        writer.wrapPayload(id = Id)
        writer.enum(BootReasonEnum.Adapter).write(0u, value.bootReason)
      }

      override fun read(reader: ClusterPayloadReader): BootReason {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return BootReasonImpl(
          data.enum(BootReasonEnum.Adapter).getOptionalNullable(0u, "BootReason").getOrNull()
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is BootReason) return false
      if (bootReason != other.bootReason) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (bootReason?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "BootReason(bootReason=$bootReason)"
    }
  }

  // Structs
  class NetworkInterface(
    val name: String = "",
    val isOperational: Boolean = false,
    val offPremiseServicesReachableIpv4: Boolean? = null,
    val offPremiseServicesReachableIpv6: Boolean? = null,
    val hardwareAddress: ByteArray = ByteArray(0),
    val ipv4Addresses: List<ByteArray> = emptyList(),
    val ipv6Addresses: List<ByteArray> = emptyList(),
    val type: InterfaceTypeEnum = InterfaceTypeEnum.Unspecified,
  ) : ClusterStruct {

    /** Descriptor enum for this struct's fields. */
    @HomeExperimentalGenericApi
    enum class StructFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : com.google.home.Field {
      /** The [Name] command request field. */
      Name("Name", 0u, "String", FieldType.String, false, NoOpDescriptor, false),
      /** The [isOperational] command request field. */
      isOperational(
        "isOperational",
        1u,
        "Boolean",
        FieldType.Boolean,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [offPremiseServicesReachableIpv4] command request field. */
      offPremiseServicesReachableIpv4(
        "offPremiseServicesReachableIpv4",
        2u,
        "Boolean",
        FieldType.Boolean,
        false,
        NoOpDescriptor,
        true,
      ),
      /** The [offPremiseServicesReachableIpv6] command request field. */
      offPremiseServicesReachableIpv6(
        "offPremiseServicesReachableIpv6",
        3u,
        "Boolean",
        FieldType.Boolean,
        false,
        NoOpDescriptor,
        true,
      ),
      /** The [hardwareAddress] command request field. */
      hardwareAddress(
        "hardwareAddress",
        4u,
        "ByteArray",
        FieldType.ByteArray,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [ipv4Addresses] command request field. */
      ipv4Addresses(
        "ipv4Addresses",
        5u,
        "ByteArray",
        FieldType.ByteArray,
        true,
        NoOpDescriptor,
        false,
      ),
      /** The [ipv6Addresses] command request field. */
      ipv6Addresses(
        "ipv6Addresses",
        6u,
        "ByteArray",
        FieldType.ByteArray,
        true,
        NoOpDescriptor,
        false,
      ),
      /** The [type] command request field. */
      type(
        "type",
        7u,
        "InterfaceTypeEnum",
        FieldType.Enum,
        false,
        InterfaceTypeEnum.EnumDescriptor,
        false,
      ),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.Name.tag -> name
        StructFields.isOperational.tag -> isOperational
        StructFields.offPremiseServicesReachableIpv4.tag -> offPremiseServicesReachableIpv4
        StructFields.offPremiseServicesReachableIpv6.tag -> offPremiseServicesReachableIpv6
        StructFields.hardwareAddress.tag -> hardwareAddress
        StructFields.ipv4Addresses.tag -> ipv4Addresses
        StructFields.ipv6Addresses.tag -> ipv6Addresses
        StructFields.type.tag -> type
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<NetworkInterface>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: NetworkInterface) {
        writer.string.write(0u, value.name)
        writer.boolean.write(1u, value.isOperational)
        writer.boolean.write(2u, value.offPremiseServicesReachableIpv4)
        writer.boolean.write(3u, value.offPremiseServicesReachableIpv6)
        writer.bytearray.write(4u, value.hardwareAddress)
        writer.bytearray.writeList(5u, value.ipv4Addresses)
        writer.bytearray.writeList(6u, value.ipv6Addresses)
        writer.enum(InterfaceTypeEnum.Adapter).write(7u, value.type)
      }

      override fun read(reader: ClusterPayloadReader): NetworkInterface {
        val data = reader.readPayload()
        return NetworkInterface(
          data.string.get(0u, "Name"),
          data.boolean.get(1u, "IsOperational"),
          data.boolean.getNullable(2u, "OffPremiseServicesReachableIpv4"),
          data.boolean.getNullable(3u, "OffPremiseServicesReachableIpv6"),
          data.bytearray.get(4u, "HardwareAddress"),
          data.bytearray.getList(5u, "Ipv4Addresses"),
          data.bytearray.getList(6u, "Ipv6Addresses"),
          data.enum(InterfaceTypeEnum.Adapter).get(7u, "Type"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return NetworkInterface(
          name = fields[StructFields.Name] as String,
          isOperational = fields[StructFields.isOperational] as Boolean,
          offPremiseServicesReachableIpv4 =
            fields[StructFields.offPremiseServicesReachableIpv4] as Boolean?,
          offPremiseServicesReachableIpv6 =
            fields[StructFields.offPremiseServicesReachableIpv6] as Boolean?,
          hardwareAddress = fields[StructFields.hardwareAddress] as ByteArray,
          ipv4Addresses = fields[StructFields.ipv4Addresses] as List<ByteArray>,
          ipv6Addresses = fields[StructFields.ipv6Addresses] as List<ByteArray>,
          type = fields[StructFields.type] as InterfaceTypeEnum,
        )
      }

      val TypedExpression<out NetworkInterface?>.name: TypedExpression<String>
        get() = fieldSelect<NetworkInterface, String>(this, StructFields.Name)

      val TypedExpression<out NetworkInterface?>.isOperational: TypedExpression<Boolean>
        get() = fieldSelect<NetworkInterface, Boolean>(this, StructFields.isOperational)

      val TypedExpression<out NetworkInterface?>.offPremiseServicesReachableIpv4:
        TypedExpression<Boolean?>
        get() =
          fieldSelect<NetworkInterface, Boolean?>(
            this,
            StructFields.offPremiseServicesReachableIpv4,
          )

      val TypedExpression<out NetworkInterface?>.offPremiseServicesReachableIpv6:
        TypedExpression<Boolean?>
        get() =
          fieldSelect<NetworkInterface, Boolean?>(
            this,
            StructFields.offPremiseServicesReachableIpv6,
          )

      val TypedExpression<out NetworkInterface?>.hardwareAddress: TypedExpression<ByteArray>
        get() = fieldSelect<NetworkInterface, ByteArray>(this, StructFields.hardwareAddress)

      val TypedExpression<out NetworkInterface?>.ipv4Addresses: TypedExpression<List<ByteArray>>
        get() = fieldSelect<NetworkInterface, List<ByteArray>>(this, StructFields.ipv4Addresses)

      val TypedExpression<out NetworkInterface?>.ipv6Addresses: TypedExpression<List<ByteArray>>
        get() = fieldSelect<NetworkInterface, List<ByteArray>>(this, StructFields.ipv6Addresses)

      val TypedExpression<out NetworkInterface?>.type: TypedExpression<InterfaceTypeEnum>
        get() = fieldSelect<NetworkInterface, InterfaceTypeEnum>(this, StructFields.type)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is NetworkInterface) return false
      if (name != other.name) {
        return false
      }
      if (isOperational != other.isOperational) {
        return false
      }
      if (offPremiseServicesReachableIpv4 != other.offPremiseServicesReachableIpv4) {
        return false
      }
      if (offPremiseServicesReachableIpv6 != other.offPremiseServicesReachableIpv6) {
        return false
      }
      if (!(hardwareAddress contentEquals other.hardwareAddress)) {
        return false
      }
      if (!(ipv4Addresses.toTypedArray() contentDeepEquals other.ipv4Addresses.toTypedArray())) {
        return false
      }
      if (!(ipv6Addresses.toTypedArray() contentDeepEquals other.ipv6Addresses.toTypedArray())) {
        return false
      }
      if (type != other.type) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + name.hashCode()
      result = 31 * result + isOperational.hashCode()
      result = 31 * result + (offPremiseServicesReachableIpv4?.hashCode() ?: 0)
      result = 31 * result + (offPremiseServicesReachableIpv6?.hashCode() ?: 0)
      result = 31 * result + hardwareAddress.contentHashCode()
      result = 31 * result + ipv4Addresses.toTypedArray().contentDeepHashCode()
      result = 31 * result + ipv6Addresses.toTypedArray().contentDeepHashCode()
      result = 31 * result + type.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "NetworkInterface(name=$name, isOperational=$isOperational, offPremiseServicesReachableIpv4=$offPremiseServicesReachableIpv4, offPremiseServicesReachableIpv6=$offPremiseServicesReachableIpv6, hardwareAddress=$hardwareAddress, ipv4Addresses=$ipv4Addresses, ipv6Addresses=$ipv6Addresses, type=$type)"
    }
  }

  /** Attributes for the GeneralDiagnostics cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {
    val networkInterfaces: List<NetworkInterface>?
    val rebootCount: UShort?
    val upTime: ULong?
    val totalOperationalHours: UInt?
    val bootReason: BootReasonEnum?
    val activeHardwareFaults: List<HardwareFaultEnum>?
    val activeRadioFaults: List<RadioFaultEnum>?
    val activeNetworkFaults: List<NetworkFaultEnum>?
    val testEventTriggersEnabled: Boolean?

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

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor = GeneralDiagnostics.Attribute.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        GeneralDiagnostics.Attribute.networkInterfaces.tag -> networkInterfaces
        GeneralDiagnostics.Attribute.rebootCount.tag -> rebootCount
        GeneralDiagnostics.Attribute.upTime.tag -> upTime
        GeneralDiagnostics.Attribute.totalOperationalHours.tag -> totalOperationalHours
        GeneralDiagnostics.Attribute.bootReason.tag -> bootReason
        GeneralDiagnostics.Attribute.activeHardwareFaults.tag -> activeHardwareFaults
        GeneralDiagnostics.Attribute.activeRadioFaults.tag -> activeRadioFaults
        GeneralDiagnostics.Attribute.activeNetworkFaults.tag -> activeNetworkFaults
        GeneralDiagnostics.Attribute.testEventTriggersEnabled.tag -> testEventTriggersEnabled
        GeneralDiagnostics.Attribute.generatedCommandList.tag -> generatedCommandList
        GeneralDiagnostics.Attribute.acceptedCommandList.tag -> acceptedCommandList
        GeneralDiagnostics.Attribute.attributeList.tag -> attributeList
        GeneralDiagnostics.Attribute.featureMap.tag -> featureMap
        GeneralDiagnostics.Attribute.clusterRevision.tag -> clusterRevision
        else -> null
      }
    }

    /** @suppress */
    companion object Adapter : StructAdapter<Attributes> {

      override fun write(writer: ClusterPayloadWriter, value: Attributes) {
        writer.wrapPayload(id = Id)
        if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
          writer.struct(NetworkInterface.Adapter).writeList(0u, value.networkInterfaces)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.ushort.write(1u, value.rebootCount)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.ulong.write(2u, value.upTime)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.uint.write(3u, value.totalOperationalHours)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
          writer.enum(BootReasonEnum.Adapter).write(4u, value.bootReason)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
          writer.enum(HardwareFaultEnum.Adapter).writeList(5u, value.activeHardwareFaults)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(6u)) {
          writer.enum(RadioFaultEnum.Adapter).writeList(6u, value.activeRadioFaults)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(7u)) {
          writer.enum(NetworkFaultEnum.Adapter).writeList(7u, value.activeNetworkFaults)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(8u)) {
          writer.boolean.write(8u, value.testEventTriggersEnabled)
        }
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.bitmap(Feature.Adapter).write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload(mapOf(0u to NetworkInterface.Adapter))
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data
            .struct { NetworkInterface() }
            .getOptionalNullableList(0u, "NetworkInterfaces")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(1u, "RebootCount")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data.ulong
            .getOptionalNullable(2u, "UpTime")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.uint
            .getOptionalNullable(3u, "TotalOperationalHours")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
            .getOrNull(),
          data
            .enum(BootReasonEnum.Adapter)
            .getOptionalNullable(4u, "BootReason")
            .also { if (it.isPresent && it.value != null) attributeList.add(4u) }
            .getOrNull(),
          data
            .enum(HardwareFaultEnum.Adapter)
            .getOptionalNullableList(5u, "ActiveHardwareFaults")
            .also { if (it.isPresent && it.value != null) attributeList.add(5u) }
            .getOrNull(),
          data
            .enum(RadioFaultEnum.Adapter)
            .getOptionalNullableList(6u, "ActiveRadioFaults")
            .also { if (it.isPresent && it.value != null) attributeList.add(6u) }
            .getOrNull(),
          data
            .enum(NetworkFaultEnum.Adapter)
            .getOptionalNullableList(7u, "ActiveNetworkFaults")
            .also { if (it.isPresent && it.value != null) attributeList.add(7u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(8u, "TestEventTriggersEnabled")
            .also { if (it.isPresent && it.value != null) attributeList.add(8u) }
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
    override val networkInterfaces: List<NetworkInterface>? = null,
    override val rebootCount: UShort? = null,
    override val upTime: ULong? = null,
    override val totalOperationalHours: UInt? = null,
    override val bootReason: BootReasonEnum? = null,
    override val activeHardwareFaults: List<HardwareFaultEnum>? = null,
    override val activeRadioFaults: List<RadioFaultEnum>? = null,
    override val activeNetworkFaults: List<NetworkFaultEnum>? = null,
    override val testEventTriggersEnabled: Boolean? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 3u, 4u, 5u, 6u, 7u, 8u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: Feature = Feature(),
    override val clusterRevision: UShort = 0u,
  ) : Attributes {

    constructor(
      other: Attributes
    ) : this(
      networkInterfaces = other.networkInterfaces,
      rebootCount = other.rebootCount,
      upTime = other.upTime,
      totalOperationalHours = other.totalOperationalHours,
      bootReason = other.bootReason,
      activeHardwareFaults = other.activeHardwareFaults,
      activeRadioFaults = other.activeRadioFaults,
      activeNetworkFaults = other.activeNetworkFaults,
      testEventTriggersEnabled = other.testEventTriggersEnabled,
      generatedCommandList = other.generatedCommandList,
      acceptedCommandList = other.acceptedCommandList,
      attributeList = other.attributeList,
      featureMap = other.featureMap,
      clusterRevision = other.clusterRevision,
    )

    companion object {
      val Adapter = Attributes.Adapter
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Attributes) return false
      if (networkInterfaces != other.networkInterfaces) {
        return false
      }
      if (rebootCount != other.rebootCount) {
        return false
      }
      if (upTime != other.upTime) {
        return false
      }
      if (totalOperationalHours != other.totalOperationalHours) {
        return false
      }
      if (bootReason != other.bootReason) {
        return false
      }
      if (activeHardwareFaults != other.activeHardwareFaults) {
        return false
      }
      if (activeRadioFaults != other.activeRadioFaults) {
        return false
      }
      if (activeNetworkFaults != other.activeNetworkFaults) {
        return false
      }
      if (testEventTriggersEnabled != other.testEventTriggersEnabled) {
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
      result = 31 * result + (networkInterfaces?.hashCode() ?: 0)
      result = 31 * result + (rebootCount?.hashCode() ?: 0)
      result = 31 * result + (upTime?.hashCode() ?: 0)
      result = 31 * result + (totalOperationalHours?.hashCode() ?: 0)
      result = 31 * result + (bootReason?.hashCode() ?: 0)
      result = 31 * result + (activeHardwareFaults?.hashCode() ?: 0)
      result = 31 * result + (activeRadioFaults?.hashCode() ?: 0)
      result = 31 * result + (activeNetworkFaults?.hashCode() ?: 0)
      result = 31 * result + (testEventTriggersEnabled?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "GeneralDiagnostics(networkInterfaces=$networkInterfaces, rebootCount=$rebootCount, upTime=$upTime, totalOperationalHours=$totalOperationalHours, bootReason=$bootReason, activeHardwareFaults=$activeHardwareFaults, activeRadioFaults=$activeRadioFaults, activeNetworkFaults=$activeNetworkFaults, testEventTriggersEnabled=$testEventTriggersEnabled, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      networkInterfaces: List<NetworkInterface>? = this.networkInterfaces,
      rebootCount: UShort? = this.rebootCount,
      upTime: ULong? = this.upTime,
      totalOperationalHours: UInt? = this.totalOperationalHours,
      bootReason: BootReasonEnum? = this.bootReason,
      activeHardwareFaults: List<HardwareFaultEnum>? = this.activeHardwareFaults,
      activeRadioFaults: List<RadioFaultEnum>? = this.activeRadioFaults,
      activeNetworkFaults: List<NetworkFaultEnum>? = this.activeNetworkFaults,
      testEventTriggersEnabled: Boolean? = this.testEventTriggersEnabled,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: Feature = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        networkInterfaces = networkInterfaces,
        rebootCount = rebootCount,
        upTime = upTime,
        totalOperationalHours = totalOperationalHours,
        bootReason = bootReason,
        activeHardwareFaults = activeHardwareFaults,
        activeRadioFaults = activeRadioFaults,
        activeNetworkFaults = activeNetworkFaults,
        testEventTriggersEnabled = testEventTriggersEnabled,
        generatedCommandList = generatedCommandList,
        acceptedCommandList = acceptedCommandList,
        attributeList = attributeList,
        featureMap = featureMap,
        clusterRevision = clusterRevision,
      )
  }

  // Commands

  object TestEventTriggerCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GeneralDiagnosticsTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "TestEventTriggerCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val enableKey: ByteArray = ByteArray(0), val eventTrigger: ULong = 0u) :
      ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @HomeExperimentalGenericApi
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [enableKey] command request field. */
        enableKey("enableKey", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [eventTrigger] command request field. */
        eventTrigger("eventTrigger", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  enableKey = fields[CommandFields.enableKey] as ByteArray,
                  eventTrigger = fields[CommandFields.eventTrigger] as ULong,
                )
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.enableKey.tag -> enableKey
          CommandFields.eventTrigger.tag -> eventTrigger
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.enableKey)
          writer.ulong.write(1u, value.eventTrigger)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.bytearray.get(0u, "EnableKey"), data.ulong.get(1u, "EventTrigger"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(enableKey contentEquals other.enableKey)) {
          return false
        }
        if (eventTrigger != other.eventTrigger) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + enableKey.contentHashCode()
        result = 31 * result + eventTrigger.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "TestEventTriggerCommand.Request(enableKey=$enableKey, eventTrigger=$eventTrigger)"
      }
    }
  }

  object TimeSnapshotCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GeneralDiagnosticsTrait.Id, 1u)
    override val commandId = requestId.toString()
    override val commandName = "TimeSnapshotCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GeneralDiagnosticsTrait.Id, 2u)

    @Suppress("ClassShouldBeObject")
    class Request() : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @HomeExperimentalGenericApi
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        ;

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request()
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
        }

        override fun read(reader: ClusterPayloadReader) = Request()
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + this.toString().hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "TimeSnapshotCommand.Request()"
      }
    }

    class Response(val systemTimeMs: ULong = 0u, val posixTimeMs: ULong? = null) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @HomeExperimentalGenericApi
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [systemTimeMs] command request field. */
        systemTimeMs("systemTimeMs", 0u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
        /** The [posixTimeMs] command request field. */
        posixTimeMs("posixTimeMs", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, true);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  systemTimeMs = fields[CommandFields.systemTimeMs] as ULong,
                  posixTimeMs = fields[CommandFields.posixTimeMs] as ULong?,
                )
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.systemTimeMs.tag -> systemTimeMs
          CommandFields.posixTimeMs.tag -> posixTimeMs
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.ulong.write(0u, value.systemTimeMs)
          writer.ulong.write(1u, value.posixTimeMs)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.ulong.get(0u, "SystemTimeMs"),
            data.ulong.getNullable(1u, "PosixTimeMs"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (systemTimeMs != other.systemTimeMs) {
          return false
        }
        if (posixTimeMs != other.posixTimeMs) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + systemTimeMs.hashCode()
        result = 31 * result + (posixTimeMs?.hashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "TimeSnapshotCommand.Response(systemTimeMs=$systemTimeMs, posixTimeMs=$posixTimeMs)"
      }
    }
  }

  object PayloadTestRequestCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GeneralDiagnosticsTrait.Id, 3u)
    override val commandId = requestId.toString()
    override val commandName = "PayloadTestRequestCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GeneralDiagnosticsTrait.Id, 4u)

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val enableKey: ByteArray = ByteArray(0),
      val value: UByte = 0u,
      val count: UShort = 0u,
    ) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @HomeExperimentalGenericApi
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [enableKey] command request field. */
        enableKey("enableKey", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [`value`] command request field. */
        `value`("`value`", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [count] command request field. */
        count("count", 2u, "UShort", FieldType.UShort, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  enableKey = fields[CommandFields.enableKey] as ByteArray,
                  value = fields[CommandFields.`value`] as UByte,
                  count = fields[CommandFields.count] as UShort,
                )
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.enableKey.tag -> enableKey
          CommandFields.`value`.tag -> value
          CommandFields.count.tag -> count
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.enableKey)
          writer.ubyte.write(1u, value.value)
          writer.ushort.write(2u, value.count)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "EnableKey"),
            data.ubyte.get(1u, "Value"),
            data.ushort.get(2u, "Count"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(enableKey contentEquals other.enableKey)) {
          return false
        }
        if (value != other.value) {
          return false
        }
        if (count != other.count) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + enableKey.contentHashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + count.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "PayloadTestRequestCommand.Request(enableKey=$enableKey, value=$value, count=$count)"
      }
    }

    class Response(val payload: ByteArray = ByteArray(0)) : ClusterStruct {

      /** Descriptor enum for this command's request fields. */
      @HomeExperimentalGenericApi
      enum class CommandFields(
        override val fieldName: String,
        override val tag: UInt,
        override val typeName: String,
        override val typeEnum: FieldType,
        override val isList: Boolean,
        override val descriptor: HomeDescriptor,
        val isNullable: Boolean,
      ) : com.google.home.Field {
        /** The [payload] command request field. */
        payload("payload", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(payload = fields[CommandFields.payload] as ByteArray)
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.payload.tag -> payload
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.bytearray.write(0u, value.payload)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(data.bytearray.get(0u, "Payload"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (!(payload contentEquals other.payload)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + payload.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "PayloadTestRequestCommand.Response(payload=$payload)"
      }
    }
  }
}
