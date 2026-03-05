// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

import com.google.errorprone.annotations.Immutable
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
import com.google.home.matter.serialization.CanMutate
import com.google.home.matter.serialization.ClusterEnum
import com.google.home.matter.serialization.ClusterId
import com.google.home.matter.serialization.ClusterPayloadReader
import com.google.home.matter.serialization.ClusterPayloadWriter
import com.google.home.matter.serialization.EnumAdapter
import com.google.home.matter.serialization.OptionalValue
import com.google.home.matter.serialization.ScopedCommandId
import com.google.home.matter.serialization.ScopedEventId
import com.google.home.matter.serialization.StructAdapter
import com.google.home.matter.serialization.camelToSnakeUpper
import com.google.home.matter.serialization.unwrapPayload
import com.google.home.matter.serialization.upperCamelToSnakeUpper
import com.google.home.matter.serialization.wrapPayload
import com.google.home.toDescriptorMap
import com.google.home.toEnumDescriptor
import javax.annotation.processing.Generated

/*
 * Serialization object for OtaSoftwareUpdateRequestorTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for OtaSoftwareUpdateRequestorTrait. */
@Generated("GoogleHomePlatformCodegen")
object OtaSoftwareUpdateRequestorTrait {
  val Id = ClusterId(42u, "OtaSoftwareUpdateRequestor")

  // Enums
  /** The reason for an announcement. */
  enum class AnnouncementReasonEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(42u).traitId,
    override val typeName: String = "AnnouncementReasonEnum",
  ) : ClusterEnum {
    /** An OTA provider is announcing its presence. */
    SimpleAnnouncement(0u),
    /**
     * An OTA provider is announcing that a new software image may be available that needs to be
     * applied.
     */
    UpdateAvailable(1u),
    /**
     * An OTA provider is announcing that a new software image may be available that needs to be
     * applied urgently.
     */
    UrgentUpdateAvailable(2u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "AnnouncementReasonEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "AnnouncementReasonEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  /** The reason for an OTA change. */
  enum class ChangeReasonEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(42u).traitId,
    override val typeName: String = "ChangeReasonEnum",
  ) : ClusterEnum {
    /** An unknown reason. */
    Unknown(0u),
    /** A prior operation succeeded. */
    Success(1u),
    /** A prior operation failed. */
    Failure(2u),
    /** A timeout occurred. */
    TimeOut(3u),
    /** The OTA provider requests the client to wait. */
    DelayByProvider(4u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "ChangeReasonEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "ChangeReasonEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  /** The state of an OTA update. */
  enum class UpdateStateEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(42u).traitId,
    override val typeName: String = "UpdateStateEnum",
  ) : ClusterEnum {
    /** The current state is unknown. */
    Unknown(0u),
    /** The node is not yet in the process of software update. */
    Idle(1u),
    /** The node is in the process of querying an OTA provider. */
    Querying(2u),
    /** The node is waiting after a Busy response. */
    DelayedOnQuery(3u),
    /** The node is in the process of downloading a software update. */
    Downloading(4u),
    /** The node is in the process of verifying and applying a software update. */
    Applying(5u),
    /** The node is waiting due to an AwaitNextAction response. */
    DelayedOnApply(6u),
    /** The node is recovering to a previous version. */
    RollingBack(7u),
    /** The node is waiting on user consent. */
    DelayedOnUserConsent(8u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "UpdateStateEnum".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "UpdateStateEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps

  // Events
  interface StateTransition : ClusterStruct {
    val previousState: UpdateStateEnum?
    val newState: UpdateStateEnum?
    val reason: ChangeReasonEnum?
    val targetSoftwareVersion: UInt?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      OtaSoftwareUpdateRequestor.StateTransitionEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        OtaSoftwareUpdateRequestor.StateTransitionEvent.EventFields.previousState.tag ->
          previousState
        OtaSoftwareUpdateRequestor.StateTransitionEvent.EventFields.newState.tag -> newState
        OtaSoftwareUpdateRequestor.StateTransitionEvent.EventFields.reason.tag -> reason
        OtaSoftwareUpdateRequestor.StateTransitionEvent.EventFields.targetSoftwareVersion.tag ->
          targetSoftwareVersion
        else -> null
      }
    }
  }

  /** @suppress */
  class StateTransitionImpl(
    override val previousState: UpdateStateEnum? = null,
    override val newState: UpdateStateEnum? = null,
    override val reason: ChangeReasonEnum? = null,
    override val targetSoftwareVersion: UInt? = null,
  ) : StateTransition {
    /** @suppress */
    companion object Adapter : StructAdapter<StateTransition> {
      val Id = ScopedEventId(OtaSoftwareUpdateRequestorTrait.Id, 0u)

      override fun write(writer: ClusterPayloadWriter, value: StateTransition) {
        writer.wrapPayload(id = Id)
        writer.enum(UpdateStateEnum.Adapter).write(0u, value.previousState)
        writer.enum(UpdateStateEnum.Adapter).write(1u, value.newState)
        writer.enum(ChangeReasonEnum.Adapter).write(2u, value.reason)
        writer.uint.write(3u, value.targetSoftwareVersion)
      }

      override fun read(reader: ClusterPayloadReader): StateTransition {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return StateTransitionImpl(
          data.enum(UpdateStateEnum.Adapter).getOptionalNullable(0u, "PreviousState").getOrNull(),
          data.enum(UpdateStateEnum.Adapter).getOptionalNullable(1u, "NewState").getOrNull(),
          data.enum(ChangeReasonEnum.Adapter).getOptionalNullable(2u, "Reason").getOrNull(),
          data.uint.getOptionalNullable(3u, "TargetSoftwareVersion").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is StateTransition) return false
      if (previousState != other.previousState) {
        return false
      }
      if (newState != other.newState) {
        return false
      }
      if (reason != other.reason) {
        return false
      }
      if (targetSoftwareVersion != other.targetSoftwareVersion) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (previousState?.hashCode() ?: 0)
      result = 31 * result + (newState?.hashCode() ?: 0)
      result = 31 * result + (reason?.hashCode() ?: 0)
      result = 31 * result + (targetSoftwareVersion?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "StateTransition(previousState=$previousState, newState=$newState, reason=$reason, targetSoftwareVersion=$targetSoftwareVersion)"
    }
  }

  interface VersionApplied : ClusterStruct {
    val softwareVersion: UInt?
    val productId: UShort?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      OtaSoftwareUpdateRequestor.VersionAppliedEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        OtaSoftwareUpdateRequestor.VersionAppliedEvent.EventFields.softwareVersion.tag ->
          softwareVersion
        OtaSoftwareUpdateRequestor.VersionAppliedEvent.EventFields.productId.tag -> productId
        else -> null
      }
    }
  }

  /** @suppress */
  class VersionAppliedImpl(
    override val softwareVersion: UInt? = null,
    override val productId: UShort? = null,
  ) : VersionApplied {
    /** @suppress */
    companion object Adapter : StructAdapter<VersionApplied> {
      val Id = ScopedEventId(OtaSoftwareUpdateRequestorTrait.Id, 1u)

      override fun write(writer: ClusterPayloadWriter, value: VersionApplied) {
        writer.wrapPayload(id = Id)
        writer.uint.write(0u, value.softwareVersion)
        writer.ushort.write(1u, value.productId)
      }

      override fun read(reader: ClusterPayloadReader): VersionApplied {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return VersionAppliedImpl(
          data.uint.getOptionalNullable(0u, "SoftwareVersion").getOrNull(),
          data.ushort.getOptionalNullable(1u, "ProductId").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is VersionApplied) return false
      if (softwareVersion != other.softwareVersion) {
        return false
      }
      if (productId != other.productId) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (softwareVersion?.hashCode() ?: 0)
      result = 31 * result + (productId?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "VersionApplied(softwareVersion=$softwareVersion, productId=$productId)"
    }
  }

  interface DownloadError : ClusterStruct {
    val softwareVersion: UInt?
    val bytesDownloaded: ULong?
    val progressPercent: UByte?
    val platformCode: Long?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      OtaSoftwareUpdateRequestor.DownloadErrorEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        OtaSoftwareUpdateRequestor.DownloadErrorEvent.EventFields.softwareVersion.tag ->
          softwareVersion
        OtaSoftwareUpdateRequestor.DownloadErrorEvent.EventFields.bytesDownloaded.tag ->
          bytesDownloaded
        OtaSoftwareUpdateRequestor.DownloadErrorEvent.EventFields.progressPercent.tag ->
          progressPercent
        OtaSoftwareUpdateRequestor.DownloadErrorEvent.EventFields.platformCode.tag -> platformCode
        else -> null
      }
    }
  }

  /** @suppress */
  class DownloadErrorImpl(
    override val softwareVersion: UInt? = null,
    override val bytesDownloaded: ULong? = null,
    override val progressPercent: UByte? = null,
    override val platformCode: Long? = null,
  ) : DownloadError {
    /** @suppress */
    companion object Adapter : StructAdapter<DownloadError> {
      val Id = ScopedEventId(OtaSoftwareUpdateRequestorTrait.Id, 2u)

      override fun write(writer: ClusterPayloadWriter, value: DownloadError) {
        writer.wrapPayload(id = Id)
        writer.uint.write(0u, value.softwareVersion)
        writer.ulong.write(1u, value.bytesDownloaded)
        writer.ubyte.write(2u, value.progressPercent)
        writer.long.write(3u, value.platformCode)
      }

      override fun read(reader: ClusterPayloadReader): DownloadError {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return DownloadErrorImpl(
          data.uint.getOptionalNullable(0u, "SoftwareVersion").getOrNull(),
          data.ulong.getOptionalNullable(1u, "BytesDownloaded").getOrNull(),
          data.ubyte.getOptionalNullable(2u, "ProgressPercent").getOrNull(),
          data.long.getOptionalNullable(3u, "PlatformCode").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is DownloadError) return false
      if (softwareVersion != other.softwareVersion) {
        return false
      }
      if (bytesDownloaded != other.bytesDownloaded) {
        return false
      }
      if (progressPercent != other.progressPercent) {
        return false
      }
      if (platformCode != other.platformCode) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (softwareVersion?.hashCode() ?: 0)
      result = 31 * result + (bytesDownloaded?.hashCode() ?: 0)
      result = 31 * result + (progressPercent?.hashCode() ?: 0)
      result = 31 * result + (platformCode?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "DownloadError(softwareVersion=$softwareVersion, bytesDownloaded=$bytesDownloaded, progressPercent=$progressPercent, platformCode=$platformCode)"
    }
  }

  // Structs
  /**
   * A fabric-scoped location of an OTA provider on a given fabric.
   *
   * @constructor Creates the ProviderLocation class.
   */
  class ProviderLocation(
    val providerNodeId: ULong = 0u,
    /** The endpoint ID for the provider node that implements the OTA Provider cluster. */
    val endpoint: UShort = 0u,
    /** The identifier for the fabric where the OTA provider is located. */
    val fabricIndex: UByte = 0u,
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
      /** The [providerNodeId] command request field. */
      providerNodeId("providerNodeId", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
      /** The [endpoint] command request field. */
      endpoint("endpoint", 2u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.providerNodeId.tag -> providerNodeId
        StructFields.endpoint.tag -> endpoint
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<ProviderLocation>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: ProviderLocation) {
        writer.ulong.write(1u, value.providerNodeId)
        writer.ushort.write(2u, value.endpoint)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): ProviderLocation {
        val data = reader.readPayload()
        return ProviderLocation(
          data.ulong.get(1u, "ProviderNodeId"),
          data.ushort.get(2u, "Endpoint"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return ProviderLocation(
          providerNodeId = fields[StructFields.providerNodeId] as ULong,
          endpoint = fields[StructFields.endpoint] as UShort,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out ProviderLocation?>.providerNodeId: TypedExpression<ULong>
        get() = fieldSelect<ProviderLocation, ULong>(this, StructFields.providerNodeId)

      val TypedExpression<out ProviderLocation?>.endpoint: TypedExpression<UShort>
        get() = fieldSelect<ProviderLocation, UShort>(this, StructFields.endpoint)

      val TypedExpression<out ProviderLocation?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<ProviderLocation, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ProviderLocation) return false
      if (providerNodeId != other.providerNodeId) {
        return false
      }
      if (endpoint != other.endpoint) {
        return false
      }
      if (fabricIndex != other.fabricIndex) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + providerNodeId.hashCode()
      result = 31 * result + endpoint.hashCode()
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "ProviderLocation(providerNodeId=$providerNodeId, endpoint=$endpoint, fabricIndex=$fabricIndex)"
    }
  }

  /** Attributes for the OtaSoftwareUpdateRequestor cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {

    /**
     * A list of available OTA providers.
     *
     * __Access type:__ Write
     */
    val defaultOtaProviders: List<ProviderLocation>?

    /**
     * `true` indicates that the OTA requestor can be updated.
     *
     * __Access type:__ Read
     */
    val updatePossible: Boolean?

    /**
     * The current state of the OTA requestor with regard to obtaining software updates.
     *
     * __Access type:__ Read
     */
    val updateState: UpdateStateEnum?

    /**
     * The progress of the update as a percentage.
     *
     * __Access type:__ Read
     */
    val updateStateProgress: UByte?

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
    val featureMap: UInt

    /** The revision of the server cluster specification supported by the cluster instance. */
    val clusterRevision: UShort

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      OtaSoftwareUpdateRequestor.Attribute.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        OtaSoftwareUpdateRequestor.Attribute.defaultOtaProviders.tag -> defaultOtaProviders
        OtaSoftwareUpdateRequestor.Attribute.updatePossible.tag -> updatePossible
        OtaSoftwareUpdateRequestor.Attribute.updateState.tag -> updateState
        OtaSoftwareUpdateRequestor.Attribute.updateStateProgress.tag -> updateStateProgress
        OtaSoftwareUpdateRequestor.Attribute.generatedCommandList.tag -> generatedCommandList
        OtaSoftwareUpdateRequestor.Attribute.acceptedCommandList.tag -> acceptedCommandList
        OtaSoftwareUpdateRequestor.Attribute.attributeList.tag -> attributeList
        OtaSoftwareUpdateRequestor.Attribute.featureMap.tag -> featureMap
        OtaSoftwareUpdateRequestor.Attribute.clusterRevision.tag -> clusterRevision
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
          writer.struct(ProviderLocation.Adapter).writeList(0u, value.defaultOtaProviders)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.boolean.write(1u, value.updatePossible)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.enum(UpdateStateEnum.Adapter).write(2u, value.updateState)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.ubyte.write(3u, value.updateStateProgress)
        }
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.uint.write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload(mapOf(0u to ProviderLocation.Adapter))
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data
            .struct { ProviderLocation() }
            .getOptionalNullableList(0u, "DefaultOtaProviders")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(1u, "UpdatePossible")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data
            .enum(UpdateStateEnum.Adapter)
            .getOptionalNullable(2u, "UpdateState")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.ubyte
            .getOptionalNullable(3u, "UpdateStateProgress")
            .also { if (it.isPresent) attributeList.add(3u) }
            .getOrNull(),
          data.uint.getList(65528u, "GeneratedCommandList").also { attributeList.add(65528u) },
          data.uint.getList(65529u, "AcceptedCommandList").also { attributeList.add(65529u) },
          attributeList.also { attributeList.add(65531u) },
          data.uint.get(65532u, "FeatureMap").also { attributeList.add(65532u) },
          data.ushort.get(65533u, "ClusterRevision").also { attributeList.add(65533u) },
        )
      }
    }
  }

  /** @suppress */
  open class AttributesImpl(
    override val defaultOtaProviders: List<ProviderLocation>? = null,
    override val updatePossible: Boolean? = null,
    override val updateState: UpdateStateEnum? = null,
    override val updateStateProgress: UByte? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 3u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: UInt = 0u,
    override val clusterRevision: UShort = 0u,
  ) : Attributes, CanMutate<Attributes, MutableAttributes> {

    constructor(
      other: Attributes
    ) : this(
      defaultOtaProviders = other.defaultOtaProviders,
      updatePossible = other.updatePossible,
      updateState = other.updateState,
      updateStateProgress = other.updateStateProgress,
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
      if (defaultOtaProviders != other.defaultOtaProviders) {
        return false
      }
      if (updatePossible != other.updatePossible) {
        return false
      }
      if (updateState != other.updateState) {
        return false
      }
      if (updateStateProgress != other.updateStateProgress) {
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
      result = 31 * result + (defaultOtaProviders?.hashCode() ?: 0)
      result = 31 * result + (updatePossible?.hashCode() ?: 0)
      result = 31 * result + (updateState?.hashCode() ?: 0)
      result = 31 * result + (updateStateProgress?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "OtaSoftwareUpdateRequestor(defaultOtaProviders=$defaultOtaProviders, updatePossible=$updatePossible, updateState=$updateState, updateStateProgress=$updateStateProgress, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      defaultOtaProviders: List<ProviderLocation>? = this.defaultOtaProviders,
      updatePossible: Boolean? = this.updatePossible,
      updateState: UpdateStateEnum? = this.updateState,
      updateStateProgress: UByte? = this.updateStateProgress,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: UInt = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        defaultOtaProviders = defaultOtaProviders,
        updatePossible = updatePossible,
        updateState = updateState,
        updateStateProgress = updateStateProgress,
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
      defaultOtaProviders = attributes.defaultOtaProviders,
      updatePossible = attributes.updatePossible,
      updateState = attributes.updateState,
      updateStateProgress = attributes.updateStateProgress,
      generatedCommandList = attributes.generatedCommandList,
      acceptedCommandList = attributes.acceptedCommandList,
      attributeList = attributes.attributeList,
      featureMap = attributes.featureMap,
      clusterRevision = attributes.clusterRevision,
    ) {
    internal var _defaultOtaProviders: List<ProviderLocation>? = null
    override val defaultOtaProviders: List<ProviderLocation>?
      get() {
        return _defaultOtaProviders ?: super.defaultOtaProviders
      }

    fun setDefaultOtaProviders(value: List<ProviderLocation>) {
      _defaultOtaProviders = value
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is MutableAttributes) return false
      return super.equals(other)
    }

    override fun toString(): String {
      return "OtaSoftwareUpdateRequestor.MutableAttributes(${super.toString()})"
    }

    companion object Adapter : StructAdapter<MutableAttributes> {
      override fun write(writer: ClusterPayloadWriter, value: MutableAttributes) {
        writer.wrapPayload(id = Id)
        if (value._defaultOtaProviders != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
            writer.struct(ProviderLocation.Adapter).writeList(0u, value._defaultOtaProviders)
          } else {
            throw HomeException.invalidArgument("defaultOtaProviders")
          }
        }
      }

      override fun read(reader: ClusterPayloadReader): MutableAttributes =
        MutableAttributes(Attributes.Adapter.read(reader))
    }
  }

  // Commands

  /**
   * Announce the presence of a particular OTA provider.
   *
   * @see OtaSoftwareUpdateRequestorCommands.announceOtaProvider Use this command from the
   *   `trait(OtaSoftwareUpdateRequestor)` method.
   */
  object AnnounceOtaProviderCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OtaSoftwareUpdateRequestorTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "AnnounceOtaProviderCommand"

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

    /**
     * The request payload for the AnnounceOtaProvider command.
     *
     * @property announcementReason The reason for the announcement.
     * @property endpoint The endpoint ID of the node that implements the OTA Provider cluster.
     * @property metadataForNode A top-level anonymous list, where each list element contains a
     *   profile-specific tag encoded in fully-qualified form, and a manufacturer-specific payload.
     * @property providerNodeId The ID of a node implementing the OTA Provider cluster.
     * @property vendorId The assigned Vendor ID of the node invoking this command.
     * @constructor Creates a request payload for the AnnounceOtaProvider command.
     */
    class Request(
      val providerNodeId: ULong = 0u,
      val vendorId: UShort = 0u,
      val announcementReason: AnnouncementReasonEnum = AnnouncementReasonEnum.SimpleAnnouncement,
      val metadataForNode: OptionalValue<ByteArray> = OptionalValue.absent(),
      val endpoint: UShort = 0u,
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
        /** The [providerNodeId] command request field. */
        providerNodeId(
          "providerNodeId",
          0u,
          "ULong",
          FieldType.ULong,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [vendorId] command request field. */
        vendorId("vendorId", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
        /** The [announcementReason] command request field. */
        announcementReason(
          "announcementReason",
          2u,
          "AnnouncementReasonEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [metadataForNode] command request field. */
        metadataForNode(
          "metadataForNode",
          3u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [endpoint] command request field. */
        endpoint("endpoint", 4u, "UShort", FieldType.UShort, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  providerNodeId = fields[CommandFields.providerNodeId] as ULong,
                  vendorId = fields[CommandFields.vendorId] as UShort,
                  announcementReason =
                    fields[CommandFields.announcementReason] as AnnouncementReasonEnum,
                  metadataForNode =
                    fields[CommandFields.metadataForNode] as OptionalValue<ByteArray>,
                  endpoint = fields[CommandFields.endpoint] as UShort,
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
          CommandFields.providerNodeId.tag -> providerNodeId
          CommandFields.vendorId.tag -> vendorId
          CommandFields.announcementReason.tag -> announcementReason
          CommandFields.metadataForNode.tag -> metadataForNode
          CommandFields.endpoint.tag -> endpoint
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ulong.write(0u, value.providerNodeId)
          writer.ushort.write(1u, value.vendorId)
          writer.enum(AnnouncementReasonEnum.Adapter).write(2u, value.announcementReason)
          writer.bytearray.write(3u, value.metadataForNode)
          writer.ushort.write(4u, value.endpoint)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.ulong.get(0u, "ProviderNodeId"),
            data.ushort.get(1u, "VendorId"),
            data.enum(AnnouncementReasonEnum.Adapter).get(2u, "AnnouncementReason"),
            data.bytearray.getOptional(3u, "MetadataForNode"),
            data.ushort.get(4u, "Endpoint"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (providerNodeId != other.providerNodeId) {
          return false
        }
        if (vendorId != other.vendorId) {
          return false
        }
        if (announcementReason != other.announcementReason) {
          return false
        }
        if (metadataForNode != other.metadataForNode) {
          return false
        }
        if (endpoint != other.endpoint) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + providerNodeId.hashCode()
        result = 31 * result + vendorId.hashCode()
        result = 31 * result + announcementReason.hashCode()
        result = 31 * result + metadataForNode.hashCode()
        result = 31 * result + endpoint.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AnnounceOtaProviderCommand.Request(providerNodeId=$providerNodeId, vendorId=$vendorId, announcementReason=$announcementReason, metadataForNode=$metadataForNode, endpoint=$endpoint)"
      }
    }

    /** Optional arguments for the command AnnounceOtaProviderCommand Request */
    interface OptionalArgs {
      /**
       * A top-level anonymous list, where each list element contains a profile-specific tag encoded
       * in fully-qualified form, and a manufacturer-specific payload.
       */
      var metadataForNode: ByteArray
    }
  }
}
