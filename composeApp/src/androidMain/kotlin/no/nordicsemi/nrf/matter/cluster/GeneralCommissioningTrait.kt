// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter.cluster

import com.google.errorprone.annotations.Immutable
import com.google.home.BitmapDescriptor
import com.google.home.ClusterStruct
import com.google.home.CommandDescriptor
import com.google.home.Descriptor as HomeDescriptor
import com.google.home.DescriptorMap
import com.google.home.EnumDescriptor
import com.google.home.EnumEntry
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
import com.google.home.matter.serialization.ScopedCommandId
import com.google.home.matter.serialization.StructAdapter
import com.google.home.matter.serialization.camelToSnakeUpper
import com.google.home.matter.serialization.unwrapPayload
import com.google.home.matter.serialization.upperCamelToSnakeUpper
import com.google.home.matter.serialization.wrapPayload
import com.google.home.matter.standard.GeneralCommissioning
import com.google.home.toBitmapDescriptor
import com.google.home.toDescriptorMap
import com.google.home.toEnumDescriptor
import javax.annotation.processing.Generated
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode

/*
 * Serialization object for GeneralCommissioningTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for GeneralCommissioningTrait. */
@Generated("GoogleHomePlatformCodegen")
object GeneralCommissioningTrait {
  val Id = ClusterId(48u, "GeneralCommissioning")

  // Enums
  enum class CommissioningErrorEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(48u).traitId,
    override val typeName: String = "CommissioningErrorEnum",
  ) : ClusterEnum {
    OK(0u),
    ValueOutsideRange(1u),
    InvalidAuthentication(2u),
    NoFailSafe(3u),
    BusyWithOtherAdmin(4u),
    RequiredTCNotAccepted(5u),
    TCAcknowledgementsNotReceived(6u),
    TCMinVersionNotMet(7u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    ; //; //UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @OptIn(HomeExperimentalGenericApi::class)
    fun toDescription(): String {
      return "CommissioningErrorEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(entries.toTypedArray())

      @OptIn(HomeExperimentalGenericApi::class)
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "CommissioningErrorEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class NetworkRecoveryReasonEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(48u).traitId,
    override val typeName: String = "NetworkRecoveryReasonEnum",
  ) : ClusterEnum {
    Unspecified(0u),
    Auth(1u),
    Visibility(2u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    ; //UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @OptIn(HomeExperimentalGenericApi::class)
    fun toDescription(): String {
      return "NetworkRecoveryReasonEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @OptIn(HomeExperimentalGenericApi::class)
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "NetworkRecoveryReasonEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class RegulatoryLocationTypeEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(48u).traitId,
    override val typeName: String = "RegulatoryLocationTypeEnum",
  ) : ClusterEnum {
    Indoor(0u),
    Outdoor(1u),
    IndoorOutdoor(2u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    ; //UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @OptIn(HomeExperimentalGenericApi::class)
    fun toDescription(): String {
      return "RegulatoryLocationTypeEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @OptIn(HomeExperimentalGenericApi::class)
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "RegulatoryLocationTypeEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps
  data class Feature(
    val termsAndConditions: Boolean = false,
    val networkRecovery: Boolean = false,
  ) : ClusterBitmap(traitId = ClusterId(48u).traitId, bitmapName = "Feature") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      TermsAndConditions(0x1u),
      NetworkRecovery(0x2u),
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<Feature> {
          override fun toRaw(value: Feature): Bitmap =
            MutableBitmap().also {
              it[MaskFlags.TermsAndConditions.value] = value.termsAndConditions
              it[MaskFlags.NetworkRecovery.value] = value.networkRecovery
            }

          override fun toRuntime(value: Bitmap): Feature =
            Feature(
              value[MaskFlags.TermsAndConditions.value],
              value[MaskFlags.NetworkRecovery.value],
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

  // Events

  // Structs
  class BasicCommissioningInfo(
    val failSafeExpiryLengthSeconds: UShort = 0u,
    val maxCumulativeFailsafeSeconds: UShort = 0u,
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
      /** The [failSafeExpiryLengthSeconds] command request field. */
      failSafeExpiryLengthSeconds(
        "failSafeExpiryLengthSeconds",
        0u,
        "UShort",
        FieldType.UShort,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [maxCumulativeFailsafeSeconds] command request field. */
      maxCumulativeFailsafeSeconds(
        "maxCumulativeFailsafeSeconds",
        1u,
        "UShort",
        FieldType.UShort,
        false,
        NoOpDescriptor,
        false,
      ),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.failSafeExpiryLengthSeconds.tag -> failSafeExpiryLengthSeconds
        StructFields.maxCumulativeFailsafeSeconds.tag -> maxCumulativeFailsafeSeconds
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<BasicCommissioningInfo>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: BasicCommissioningInfo) {
        writer.ushort.write(0u, value.failSafeExpiryLengthSeconds)
        writer.ushort.write(1u, value.maxCumulativeFailsafeSeconds)
      }

      override fun read(reader: ClusterPayloadReader): BasicCommissioningInfo {
        val data = reader.readPayload()
        return BasicCommissioningInfo(
          data.ushort.get(0u, "FailSafeExpiryLengthSeconds"),
          data.ushort.get(1u, "MaxCumulativeFailsafeSeconds"),
        )
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return BasicCommissioningInfo(
          failSafeExpiryLengthSeconds = fields[StructFields.failSafeExpiryLengthSeconds] as UShort,
          maxCumulativeFailsafeSeconds = fields[StructFields.maxCumulativeFailsafeSeconds] as UShort,
        )
      }

      val TypedExpression<out BasicCommissioningInfo?>.failSafeExpiryLengthSeconds:
        TypedExpression<UShort>
        get() =
          fieldSelect<BasicCommissioningInfo, UShort>(
            this,
            StructFields.failSafeExpiryLengthSeconds,
          )

      val TypedExpression<out BasicCommissioningInfo?>.maxCumulativeFailsafeSeconds:
        TypedExpression<UShort>
        get() =
          fieldSelect<BasicCommissioningInfo, UShort>(
            this,
            StructFields.maxCumulativeFailsafeSeconds,
          )
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is BasicCommissioningInfo) return false
      if (failSafeExpiryLengthSeconds != other.failSafeExpiryLengthSeconds) {
        return false
      }
      if (maxCumulativeFailsafeSeconds != other.maxCumulativeFailsafeSeconds) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + failSafeExpiryLengthSeconds.hashCode()
      result = 31 * result + maxCumulativeFailsafeSeconds.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "BasicCommissioningInfo(failSafeExpiryLengthSeconds=$failSafeExpiryLengthSeconds, maxCumulativeFailsafeSeconds=$maxCumulativeFailsafeSeconds)"
    }
  }

  /** Attributes for the GeneralCommissioning cluster. */
  @OptIn(HomeExperimentalGenericApi::class)
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {
    val breadcrumb: ULong?
    val basicCommissioningInfo: BasicCommissioningInfo?
    val regulatoryConfig: RegulatoryLocationTypeEnum?
    val locationCapability: RegulatoryLocationTypeEnum?
    val supportsConcurrentConnection: Boolean?
    val tcAcceptedVersion: UShort?
    val tcMinRequiredVersion: UShort?
    val tcAcknowledgements: UShort?
    val tcAcknowledgementsRequired: Boolean?
    val tcUpdateDeadline: UInt?
    val recoveryIdentifier: ByteArray?
    val networkRecoveryReason: NetworkRecoveryReasonEnum?
    val isCommissioningWithoutPower: Boolean?

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
    override fun getDescriptor(): StructDescriptor = GeneralCommissioning.Attribute.StructDescriptor

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        GeneralCommissioning.Attribute.breadcrumb.tag -> breadcrumb
        GeneralCommissioning.Attribute.basicCommissioningInfo.tag -> basicCommissioningInfo
        GeneralCommissioning.Attribute.regulatoryConfig.tag -> regulatoryConfig
        GeneralCommissioning.Attribute.locationCapability.tag -> locationCapability
        GeneralCommissioning.Attribute.supportsConcurrentConnection.tag ->
          supportsConcurrentConnection
        GeneralCommissioning.Attribute.tcAcceptedVersion.tag -> tcAcceptedVersion
        GeneralCommissioning.Attribute.tcMinRequiredVersion.tag -> tcMinRequiredVersion
        GeneralCommissioning.Attribute.tcAcknowledgements.tag -> tcAcknowledgements
        GeneralCommissioning.Attribute.tcAcknowledgementsRequired.tag -> tcAcknowledgementsRequired
        GeneralCommissioning.Attribute.tcUpdateDeadline.tag -> tcUpdateDeadline
        GeneralCommissioning.Attribute.recoveryIdentifier.tag -> recoveryIdentifier
        GeneralCommissioning.Attribute.networkRecoveryReason.tag -> networkRecoveryReason
        GeneralCommissioning.Attribute.isCommissioningWithoutPower.tag ->
          isCommissioningWithoutPower
        GeneralCommissioning.Attribute.generatedCommandList.tag -> generatedCommandList
        GeneralCommissioning.Attribute.acceptedCommandList.tag -> acceptedCommandList
        GeneralCommissioning.Attribute.attributeList.tag -> attributeList
        GeneralCommissioning.Attribute.featureMap.tag -> featureMap
        GeneralCommissioning.Attribute.clusterRevision.tag -> clusterRevision
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
          writer.ulong.write(0u, value.breadcrumb)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.struct(BasicCommissioningInfo.Adapter).write(1u, value.basicCommissioningInfo)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.enum(RegulatoryLocationTypeEnum.Adapter).write(2u, value.regulatoryConfig)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.enum(RegulatoryLocationTypeEnum.Adapter).write(3u, value.locationCapability)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
          writer.boolean.write(4u, value.supportsConcurrentConnection)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
          writer.ushort.write(5u, value.tcAcceptedVersion)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(6u)) {
          writer.ushort.write(6u, value.tcMinRequiredVersion)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(7u)) {
          writer.ushort.write(7u, value.tcAcknowledgements)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(8u)) {
          writer.boolean.write(8u, value.tcAcknowledgementsRequired)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(9u)) {
          writer.uint.write(9u, value.tcUpdateDeadline)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(10u)) {
          writer.bytearray.write(10u, value.recoveryIdentifier)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(11u)) {
          writer.enum(NetworkRecoveryReasonEnum.Adapter).write(11u, value.networkRecoveryReason)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(12u)) {
          writer.boolean.write(12u, value.isCommissioningWithoutPower)
        }
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.bitmap(Feature.Adapter).write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload(mapOf(1u to BasicCommissioningInfo.Adapter))
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data.ulong
            .getOptionalNullable(0u, "Breadcrumb")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data
            .struct { BasicCommissioningInfo() }
            .getOptionalNullable(1u, "BasicCommissioningInfo")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data
            .enum(RegulatoryLocationTypeEnum.Adapter)
            .getOptionalNullable(2u, "RegulatoryConfig")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data
            .enum(RegulatoryLocationTypeEnum.Adapter)
            .getOptionalNullable(3u, "LocationCapability")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(4u, "SupportsConcurrentConnection")
            .also { if (it.isPresent && it.value != null) attributeList.add(4u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(5u, "TcAcceptedVersion")
            .also { if (it.isPresent && it.value != null) attributeList.add(5u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(6u, "TcMinRequiredVersion")
            .also { if (it.isPresent && it.value != null) attributeList.add(6u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(7u, "TcAcknowledgements")
            .also { if (it.isPresent && it.value != null) attributeList.add(7u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(8u, "TcAcknowledgementsRequired")
            .also { if (it.isPresent && it.value != null) attributeList.add(8u) }
            .getOrNull(),
          data.uint
            .getOptionalNullable(9u, "TcUpdateDeadline")
            .also { if (it.isPresent) attributeList.add(9u) }
            .getOrNull(),
          data.bytearray
            .getOptionalNullable(10u, "RecoveryIdentifier")
            .also { if (it.isPresent && it.value != null) attributeList.add(10u) }
            .getOrNull(),
          data
            .enum(NetworkRecoveryReasonEnum.Adapter)
            .getOptionalNullable(11u, "NetworkRecoveryReason")
            .also { if (it.isPresent) attributeList.add(11u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(12u, "IsCommissioningWithoutPower")
            .also { if (it.isPresent && it.value != null) attributeList.add(12u) }
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
    override val breadcrumb: ULong? = null,
    override val basicCommissioningInfo: BasicCommissioningInfo? = null,
    override val regulatoryConfig: RegulatoryLocationTypeEnum? = null,
    override val locationCapability: RegulatoryLocationTypeEnum? = null,
    override val supportsConcurrentConnection: Boolean? = null,
    override val tcAcceptedVersion: UShort? = null,
    override val tcMinRequiredVersion: UShort? = null,
    override val tcAcknowledgements: UShort? = null,
    override val tcAcknowledgementsRequired: Boolean? = null,
    override val tcUpdateDeadline: UInt? = null,
    override val recoveryIdentifier: ByteArray? = null,
    override val networkRecoveryReason: NetworkRecoveryReasonEnum? = null,
    override val isCommissioningWithoutPower: Boolean? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(
        0u,
        1u,
        2u,
        3u,
        4u,
        5u,
        6u,
        7u,
        8u,
        9u,
        10u,
        11u,
        12u,
        65528u,
        65529u,
        65531u,
        65532u,
        65533u,
      ),
    override val featureMap: Feature = Feature(),
    override val clusterRevision: UShort = 0u,
  ) : Attributes, CanMutate<Attributes, MutableAttributes> {

    constructor(
      other: Attributes
    ) : this(
      breadcrumb = other.breadcrumb,
      basicCommissioningInfo = other.basicCommissioningInfo,
      regulatoryConfig = other.regulatoryConfig,
      locationCapability = other.locationCapability,
      supportsConcurrentConnection = other.supportsConcurrentConnection,
      tcAcceptedVersion = other.tcAcceptedVersion,
      tcMinRequiredVersion = other.tcMinRequiredVersion,
      tcAcknowledgements = other.tcAcknowledgements,
      tcAcknowledgementsRequired = other.tcAcknowledgementsRequired,
      tcUpdateDeadline = other.tcUpdateDeadline,
      recoveryIdentifier = other.recoveryIdentifier,
      networkRecoveryReason = other.networkRecoveryReason,
      isCommissioningWithoutPower = other.isCommissioningWithoutPower,
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
      if (breadcrumb != other.breadcrumb) {
        return false
      }
      if (basicCommissioningInfo != other.basicCommissioningInfo) {
        return false
      }
      if (regulatoryConfig != other.regulatoryConfig) {
        return false
      }
      if (locationCapability != other.locationCapability) {
        return false
      }
      if (supportsConcurrentConnection != other.supportsConcurrentConnection) {
        return false
      }
      if (tcAcceptedVersion != other.tcAcceptedVersion) {
        return false
      }
      if (tcMinRequiredVersion != other.tcMinRequiredVersion) {
        return false
      }
      if (tcAcknowledgements != other.tcAcknowledgements) {
        return false
      }
      if (tcAcknowledgementsRequired != other.tcAcknowledgementsRequired) {
        return false
      }
      if (tcUpdateDeadline != other.tcUpdateDeadline) {
        return false
      }
      if (!(recoveryIdentifier contentEquals other.recoveryIdentifier)) {
        return false
      }
      if (networkRecoveryReason != other.networkRecoveryReason) {
        return false
      }
      if (isCommissioningWithoutPower != other.isCommissioningWithoutPower) {
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
      result = 31 * result + (breadcrumb?.hashCode() ?: 0)
      result = 31 * result + (basicCommissioningInfo?.hashCode() ?: 0)
      result = 31 * result + (regulatoryConfig?.hashCode() ?: 0)
      result = 31 * result + (locationCapability?.hashCode() ?: 0)
      result = 31 * result + (supportsConcurrentConnection?.hashCode() ?: 0)
      result = 31 * result + (tcAcceptedVersion?.hashCode() ?: 0)
      result = 31 * result + (tcMinRequiredVersion?.hashCode() ?: 0)
      result = 31 * result + (tcAcknowledgements?.hashCode() ?: 0)
      result = 31 * result + (tcAcknowledgementsRequired?.hashCode() ?: 0)
      result = 31 * result + (tcUpdateDeadline?.hashCode() ?: 0)
      result = 31 * result + (recoveryIdentifier?.contentHashCode() ?: 0)
      result = 31 * result + (networkRecoveryReason?.hashCode() ?: 0)
      result = 31 * result + (isCommissioningWithoutPower?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "GeneralCommissioning(breadcrumb=$breadcrumb, basicCommissioningInfo=$basicCommissioningInfo, regulatoryConfig=$regulatoryConfig, locationCapability=$locationCapability, supportsConcurrentConnection=$supportsConcurrentConnection, tcAcceptedVersion=$tcAcceptedVersion, tcMinRequiredVersion=$tcMinRequiredVersion, tcAcknowledgements=$tcAcknowledgements, tcAcknowledgementsRequired=$tcAcknowledgementsRequired, tcUpdateDeadline=$tcUpdateDeadline, recoveryIdentifier=$recoveryIdentifier, networkRecoveryReason=$networkRecoveryReason, isCommissioningWithoutPower=$isCommissioningWithoutPower, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      breadcrumb: ULong? = this.breadcrumb,
      basicCommissioningInfo: BasicCommissioningInfo? = this.basicCommissioningInfo,
      regulatoryConfig: RegulatoryLocationTypeEnum? = this.regulatoryConfig,
      locationCapability: RegulatoryLocationTypeEnum? = this.locationCapability,
      supportsConcurrentConnection: Boolean? = this.supportsConcurrentConnection,
      tcAcceptedVersion: UShort? = this.tcAcceptedVersion,
      tcMinRequiredVersion: UShort? = this.tcMinRequiredVersion,
      tcAcknowledgements: UShort? = this.tcAcknowledgements,
      tcAcknowledgementsRequired: Boolean? = this.tcAcknowledgementsRequired,
      tcUpdateDeadline: UInt? = this.tcUpdateDeadline,
      recoveryIdentifier: ByteArray? = this.recoveryIdentifier,
      networkRecoveryReason: NetworkRecoveryReasonEnum? = this.networkRecoveryReason,
      isCommissioningWithoutPower: Boolean? = this.isCommissioningWithoutPower,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: Feature = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        breadcrumb = breadcrumb,
        basicCommissioningInfo = basicCommissioningInfo,
        regulatoryConfig = regulatoryConfig,
        locationCapability = locationCapability,
        supportsConcurrentConnection = supportsConcurrentConnection,
        tcAcceptedVersion = tcAcceptedVersion,
        tcMinRequiredVersion = tcMinRequiredVersion,
        tcAcknowledgements = tcAcknowledgements,
        tcAcknowledgementsRequired = tcAcknowledgementsRequired,
        tcUpdateDeadline = tcUpdateDeadline,
        recoveryIdentifier = recoveryIdentifier,
        networkRecoveryReason = networkRecoveryReason,
        isCommissioningWithoutPower = isCommissioningWithoutPower,
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
      breadcrumb = attributes.breadcrumb,
      basicCommissioningInfo = attributes.basicCommissioningInfo,
      regulatoryConfig = attributes.regulatoryConfig,
      locationCapability = attributes.locationCapability,
      supportsConcurrentConnection = attributes.supportsConcurrentConnection,
      tcAcceptedVersion = attributes.tcAcceptedVersion,
      tcMinRequiredVersion = attributes.tcMinRequiredVersion,
      tcAcknowledgements = attributes.tcAcknowledgements,
      tcAcknowledgementsRequired = attributes.tcAcknowledgementsRequired,
      tcUpdateDeadline = attributes.tcUpdateDeadline,
      recoveryIdentifier = attributes.recoveryIdentifier,
      networkRecoveryReason = attributes.networkRecoveryReason,
      isCommissioningWithoutPower = attributes.isCommissioningWithoutPower,
      generatedCommandList = attributes.generatedCommandList,
      acceptedCommandList = attributes.acceptedCommandList,
      attributeList = attributes.attributeList,
      featureMap = attributes.featureMap,
      clusterRevision = attributes.clusterRevision,
    ) {
    internal var _breadcrumb: ULong? = null
    override val breadcrumb: ULong?
      get() {
        return _breadcrumb ?: super.breadcrumb
      }

    fun setBreadcrumb(value: ULong) {
      _breadcrumb = value
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is MutableAttributes) return false
      return super.equals(other)
    }

    override fun toString(): String {
      return "GeneralCommissioning.MutableAttributes(${super.toString()})"
    }

    companion object Adapter : StructAdapter<MutableAttributes> {
      override fun write(writer: ClusterPayloadWriter, value: MutableAttributes) {
        writer.wrapPayload(id = Id)
        if (value._breadcrumb != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
            writer.ulong.write(0u, value._breadcrumb)
          } else {
            throw HomeException.invalidArgument("breadcrumb")
          }
        }
      }

      override fun read(reader: ClusterPayloadReader): MutableAttributes =
        MutableAttributes(Attributes.Adapter.read(reader))
    }
  }

  // Commands

  object ArmFailSafeCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GeneralCommissioningTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "ArmFailSafeCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GeneralCommissioningTrait.Id, 1u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val expiryLengthSeconds: UShort = 0u, val breadcrumb: ULong = 0u) :
      ClusterStruct {

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
        /** The [expiryLengthSeconds] command request field. */
        expiryLengthSeconds(
          "expiryLengthSeconds",
          0u,
          "UShort",
          FieldType.UShort,
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
                  expiryLengthSeconds = fields[CommandFields.expiryLengthSeconds] as UShort,
                  breadcrumb = fields[CommandFields.breadcrumb] as ULong,
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
          CommandFields.expiryLengthSeconds.tag -> expiryLengthSeconds
          CommandFields.breadcrumb.tag -> breadcrumb
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.expiryLengthSeconds)
          writer.ulong.write(1u, value.breadcrumb)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.ushort.get(0u, "ExpiryLengthSeconds"),
            data.ulong.get(1u, "Breadcrumb"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (expiryLengthSeconds != other.expiryLengthSeconds) {
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
        result = 31 * result + expiryLengthSeconds.hashCode()
        result = 31 * result + breadcrumb.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ArmFailSafeCommand.Request(expiryLengthSeconds=$expiryLengthSeconds, breadcrumb=$breadcrumb)"
      }
    }

    class Response(
      val errorCode: CommissioningErrorEnum = CommissioningErrorEnum.OK,
      val debugText: String = "",
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
        /** The [errorCode] command request field. */
        errorCode(
          "errorCode",
          0u,
          "CommissioningErrorEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  errorCode = fields[CommandFields.errorCode] as CommissioningErrorEnum,
                  debugText = fields[CommandFields.debugText] as String,
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
          CommandFields.errorCode.tag -> errorCode
          CommandFields.debugText.tag -> debugText
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(CommissioningErrorEnum.Adapter).write(0u, value.errorCode)
          writer.string.write(1u, value.debugText)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(CommissioningErrorEnum.Adapter).get(0u, "ErrorCode"),
            data.string.get(1u, "DebugText"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (errorCode != other.errorCode) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + errorCode.hashCode()
        result = 31 * result + debugText.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ArmFailSafeCommand.Response(errorCode=$errorCode, debugText=$debugText)"
      }
    }
  }

  object SetRegulatoryConfigCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GeneralCommissioningTrait.Id, 2u)
    override val commandId = requestId.toString()
    override val commandName = "SetRegulatoryConfigCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GeneralCommissioningTrait.Id, 3u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val newRegulatoryConfig: RegulatoryLocationTypeEnum = RegulatoryLocationTypeEnum.Indoor,
      val countryCode: String = "",
      val breadcrumb: ULong = 0u,
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
        /** The [newRegulatoryConfig] command request field. */
        newRegulatoryConfig(
          "newRegulatoryConfig",
          0u,
          "RegulatoryLocationTypeEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [countryCode] command request field. */
        countryCode("countryCode", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [breadcrumb] command request field. */
        breadcrumb("breadcrumb", 2u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  newRegulatoryConfig =
                    fields[CommandFields.newRegulatoryConfig] as RegulatoryLocationTypeEnum,
                  countryCode = fields[CommandFields.countryCode] as String,
                  breadcrumb = fields[CommandFields.breadcrumb] as ULong,
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
          CommandFields.newRegulatoryConfig.tag -> newRegulatoryConfig
          CommandFields.countryCode.tag -> countryCode
          CommandFields.breadcrumb.tag -> breadcrumb
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.enum(RegulatoryLocationTypeEnum.Adapter).write(0u, value.newRegulatoryConfig)
          writer.string.write(1u, value.countryCode)
          writer.ulong.write(2u, value.breadcrumb)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.enum(RegulatoryLocationTypeEnum.Adapter).get(0u, "NewRegulatoryConfig"),
            data.string.get(1u, "CountryCode"),
            data.ulong.get(2u, "Breadcrumb"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (newRegulatoryConfig != other.newRegulatoryConfig) {
          return false
        }
        if (countryCode != other.countryCode) {
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
        result = 31 * result + newRegulatoryConfig.hashCode()
        result = 31 * result + countryCode.hashCode()
        result = 31 * result + breadcrumb.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "SetRegulatoryConfigCommand.Request(newRegulatoryConfig=$newRegulatoryConfig, countryCode=$countryCode, breadcrumb=$breadcrumb)"
      }
    }

    class Response(
      val errorCode: CommissioningErrorEnum = CommissioningErrorEnum.OK,
      val debugText: String = "",
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
        /** The [errorCode] command request field. */
        errorCode(
          "errorCode",
          0u,
          "CommissioningErrorEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  errorCode = fields[CommandFields.errorCode] as CommissioningErrorEnum,
                  debugText = fields[CommandFields.debugText] as String,
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
          CommandFields.errorCode.tag -> errorCode
          CommandFields.debugText.tag -> debugText
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(CommissioningErrorEnum.Adapter).write(0u, value.errorCode)
          writer.string.write(1u, value.debugText)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(CommissioningErrorEnum.Adapter).get(0u, "ErrorCode"),
            data.string.get(1u, "DebugText"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (errorCode != other.errorCode) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + errorCode.hashCode()
        result = 31 * result + debugText.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "SetRegulatoryConfigCommand.Response(errorCode=$errorCode, debugText=$debugText)"
      }
    }
  }

  object CommissioningCompleteCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GeneralCommissioningTrait.Id, 4u)
    override val commandId = requestId.toString()
    override val commandName = "CommissioningCompleteCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GeneralCommissioningTrait.Id, 5u)

    @Suppress("ClassShouldBeObject")
    class Request() : ClusterStruct {

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
        ;

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request()
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
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
        return "CommissioningCompleteCommand.Request()"
      }
    }

    class Response(
      val errorCode: CommissioningErrorEnum = CommissioningErrorEnum.OK,
      val debugText: String = "",
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
        /** The [errorCode] command request field. */
        errorCode(
          "errorCode",
          0u,
          "CommissioningErrorEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [debugText] command request field. */
        debugText("debugText", 1u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  errorCode = fields[CommandFields.errorCode] as CommissioningErrorEnum,
                  debugText = fields[CommandFields.debugText] as String,
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
          CommandFields.errorCode.tag -> errorCode
          CommandFields.debugText.tag -> debugText
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(CommissioningErrorEnum.Adapter).write(0u, value.errorCode)
          writer.string.write(1u, value.debugText)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(CommissioningErrorEnum.Adapter).get(0u, "ErrorCode"),
            data.string.get(1u, "DebugText"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (errorCode != other.errorCode) {
          return false
        }
        if (debugText != other.debugText) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + errorCode.hashCode()
        result = 31 * result + debugText.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "CommissioningCompleteCommand.Response(errorCode=$errorCode, debugText=$debugText)"
      }
    }
  }

  object SetTcAcknowledgementsCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GeneralCommissioningTrait.Id, 6u)
    override val commandId = requestId.toString()
    override val commandName = "SetTCAcknowledgementsCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GeneralCommissioningTrait.Id, 7u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val tcVersion: UShort = 0u, val tcUserResponse: UShort = 0u) : ClusterStruct {

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
        /** The [tcVersion] command request field. */
        tcVersion("tcVersion", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
        /** The [tcUserResponse] command request field. */
        tcUserResponse(
          "tcUserResponse",
          1u,
          "UShort",
          FieldType.UShort,
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
                  tcVersion = fields[CommandFields.tcVersion] as UShort,
                  tcUserResponse = fields[CommandFields.tcUserResponse] as UShort,
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
          CommandFields.tcVersion.tag -> tcVersion
          CommandFields.tcUserResponse.tag -> tcUserResponse
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.tcVersion)
          writer.ushort.write(1u, value.tcUserResponse)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.ushort.get(0u, "TcVersion"), data.ushort.get(1u, "TcUserResponse"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (tcVersion != other.tcVersion) {
          return false
        }
        if (tcUserResponse != other.tcUserResponse) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + tcVersion.hashCode()
        result = 31 * result + tcUserResponse.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "SetTCAcknowledgementsCommand.Request(tcVersion=$tcVersion, tcUserResponse=$tcUserResponse)"
      }
    }

    class Response(val errorCode: CommissioningErrorEnum = CommissioningErrorEnum.OK) :
      ClusterStruct {

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
        /** The [errorCode] command request field. */
        errorCode(
          "errorCode",
          0u,
          "CommissioningErrorEnum",
          FieldType.Enum,
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
                  errorCode = fields[CommandFields.errorCode] as CommissioningErrorEnum
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
          CommandFields.errorCode.tag -> errorCode
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(CommissioningErrorEnum.Adapter).write(0u, value.errorCode)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(data.enum(CommissioningErrorEnum.Adapter).get(0u, "ErrorCode"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (errorCode != other.errorCode) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + errorCode.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "SetTCAcknowledgementsCommand.Response(errorCode=$errorCode)"
      }
    }
  }
}
