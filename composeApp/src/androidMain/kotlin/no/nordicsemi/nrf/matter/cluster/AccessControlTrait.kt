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
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode

/*
 * Serialization object for AccessControlTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for AccessControlTrait. */
@Generated("GoogleHomePlatformCodegen")
object AccessControlTrait {
  val Id = ClusterId(31u, "AccessControl")

  // Enums
  enum class AccessControlEntryAuthModeEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(31u).traitId,
    override val typeName: String = "AccessControlEntryAuthModeEnum",
  ) : ClusterEnum {
    PASE(1u),
    CASE(2u),
    Group(3u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "AccessControlEntryAuthModeEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "AccessControlEntryAuthModeEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class AccessControlEntryPrivilegeEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(31u).traitId,
    override val typeName: String = "AccessControlEntryPrivilegeEnum",
  ) : ClusterEnum {
    View(1u),
    ProxyView(2u),
    Operate(3u),
    Manage(4u),
    Administer(5u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "AccessControlEntryPrivilegeEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "AccessControlEntryPrivilegeEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class AccessRestrictionTypeEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(31u).traitId,
    override val typeName: String = "AccessRestrictionTypeEnum",
  ) : ClusterEnum {
    AttributeAccessForbidden(0u),
    AttributeWriteForbidden(1u),
    CommandForbidden(2u),
    EventForbidden(3u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "AccessRestrictionTypeEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "AccessRestrictionTypeEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class ChangeTypeEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(31u).traitId,
    override val typeName: String = "ChangeTypeEnum",
  ) : ClusterEnum {
    Changed(0u),
    Added(1u),
    Removed(2u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "ChangeTypeEnum".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "ChangeTypeEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps
  data class Feature(val extension: Boolean = false, val managedDevice: Boolean = false) :
    ClusterBitmap(traitId = ClusterId(31u).traitId, bitmapName = "Feature") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      Extension(0x1u),
      ManagedDevice(0x2u),
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<Feature> {
          override fun toRaw(value: Feature): Bitmap =
            MutableBitmap().also {
              it[MaskFlags.Extension.value] = value.extension
              it[MaskFlags.ManagedDevice.value] = value.managedDevice
            }

          override fun toRuntime(value: Bitmap): Feature =
            Feature(value[MaskFlags.Extension.value], value[MaskFlags.ManagedDevice.value])
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
  interface AccessControlEntryChanged : ClusterStruct {
    val adminNodeId: ULong?
    val adminPasscodeId: UShort?
    val changeType: ChangeTypeEnum?
    val latestValue: AccessControlEntryStruct?
    val fabricIndex: UByte?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      AccessControl.AccessControlEntryChangedEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        AccessControl.AccessControlEntryChangedEvent.EventFields.adminNodeId.tag -> adminNodeId
        AccessControl.AccessControlEntryChangedEvent.EventFields.adminPasscodeId.tag ->
          adminPasscodeId
        AccessControl.AccessControlEntryChangedEvent.EventFields.changeType.tag -> changeType
        AccessControl.AccessControlEntryChangedEvent.EventFields.latestValue.tag -> latestValue
        AccessControl.AccessControlEntryChangedEvent.EventFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }
  }

  /** @suppress */
  class AccessControlEntryChangedImpl(
    override val adminNodeId: ULong? = null,
    override val adminPasscodeId: UShort? = null,
    override val changeType: ChangeTypeEnum? = null,
    override val latestValue: AccessControlEntryStruct? = null,
    override val fabricIndex: UByte? = null,
  ) : AccessControlEntryChanged {
    /** @suppress */
    companion object Adapter : StructAdapter<AccessControlEntryChanged> {
      val Id = ScopedEventId(AccessControlTrait.Id, 0u)

      override fun write(writer: ClusterPayloadWriter, value: AccessControlEntryChanged) {
        writer.wrapPayload(id = Id)
        writer.ulong.write(1u, value.adminNodeId)
        writer.ushort.write(2u, value.adminPasscodeId)
        writer.enum(ChangeTypeEnum.Adapter).write(3u, value.changeType)
        writer.struct(AccessControlEntryStruct.Adapter).write(4u, value.latestValue)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): AccessControlEntryChanged {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload(mapOf(4u to AccessControlEntryStruct.Adapter))
        return AccessControlEntryChangedImpl(
          data.ulong.getOptionalNullable(1u, "AdminNodeId").getOrNull(),
          data.ushort.getOptionalNullable(2u, "AdminPasscodeId").getOrNull(),
          data.enum(ChangeTypeEnum.Adapter).getOptionalNullable(3u, "ChangeType").getOrNull(),
          data
            .struct { AccessControlEntryStruct() }
            .getOptionalNullable(4u, "LatestValue")
            .getOrNull(),
          data.ubyte.getOptionalNullable(254u, "FabricIndex").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessControlEntryChanged) return false
      if (adminNodeId != other.adminNodeId) {
        return false
      }
      if (adminPasscodeId != other.adminPasscodeId) {
        return false
      }
      if (changeType != other.changeType) {
        return false
      }
      if (latestValue != other.latestValue) {
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
      result = 31 * result + (adminNodeId?.hashCode() ?: 0)
      result = 31 * result + (adminPasscodeId?.hashCode() ?: 0)
      result = 31 * result + (changeType?.hashCode() ?: 0)
      result = 31 * result + (latestValue?.hashCode() ?: 0)
      result = 31 * result + (fabricIndex?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "AccessControlEntryChanged(adminNodeId=$adminNodeId, adminPasscodeId=$adminPasscodeId, changeType=$changeType, latestValue=$latestValue, fabricIndex=$fabricIndex)"
    }
  }

  interface AccessControlExtensionChanged : ClusterStruct {
    val adminNodeId: ULong?
    val adminPasscodeId: UShort?
    val changeType: ChangeTypeEnum?
    val latestValue: AccessControlExtensionStruct?
    val fabricIndex: UByte?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      AccessControl.AccessControlExtensionChangedEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        AccessControl.AccessControlExtensionChangedEvent.EventFields.adminNodeId.tag -> adminNodeId
        AccessControl.AccessControlExtensionChangedEvent.EventFields.adminPasscodeId.tag ->
          adminPasscodeId
        AccessControl.AccessControlExtensionChangedEvent.EventFields.changeType.tag -> changeType
        AccessControl.AccessControlExtensionChangedEvent.EventFields.latestValue.tag -> latestValue
        AccessControl.AccessControlExtensionChangedEvent.EventFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }
  }

  /** @suppress */
  class AccessControlExtensionChangedImpl(
    override val adminNodeId: ULong? = null,
    override val adminPasscodeId: UShort? = null,
    override val changeType: ChangeTypeEnum? = null,
    override val latestValue: AccessControlExtensionStruct? = null,
    override val fabricIndex: UByte? = null,
  ) : AccessControlExtensionChanged {
    /** @suppress */
    companion object Adapter : StructAdapter<AccessControlExtensionChanged> {
      val Id = ScopedEventId(AccessControlTrait.Id, 1u)

      override fun write(writer: ClusterPayloadWriter, value: AccessControlExtensionChanged) {
        writer.wrapPayload(id = Id)
        writer.ulong.write(1u, value.adminNodeId)
        writer.ushort.write(2u, value.adminPasscodeId)
        writer.enum(ChangeTypeEnum.Adapter).write(3u, value.changeType)
        writer.struct(AccessControlExtensionStruct.Adapter).write(4u, value.latestValue)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): AccessControlExtensionChanged {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload(mapOf(4u to AccessControlExtensionStruct.Adapter))
        return AccessControlExtensionChangedImpl(
          data.ulong.getOptionalNullable(1u, "AdminNodeId").getOrNull(),
          data.ushort.getOptionalNullable(2u, "AdminPasscodeId").getOrNull(),
          data.enum(ChangeTypeEnum.Adapter).getOptionalNullable(3u, "ChangeType").getOrNull(),
          data
            .struct { AccessControlExtensionStruct() }
            .getOptionalNullable(4u, "LatestValue")
            .getOrNull(),
          data.ubyte.getOptionalNullable(254u, "FabricIndex").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessControlExtensionChanged) return false
      if (adminNodeId != other.adminNodeId) {
        return false
      }
      if (adminPasscodeId != other.adminPasscodeId) {
        return false
      }
      if (changeType != other.changeType) {
        return false
      }
      if (latestValue != other.latestValue) {
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
      result = 31 * result + (adminNodeId?.hashCode() ?: 0)
      result = 31 * result + (adminPasscodeId?.hashCode() ?: 0)
      result = 31 * result + (changeType?.hashCode() ?: 0)
      result = 31 * result + (latestValue?.hashCode() ?: 0)
      result = 31 * result + (fabricIndex?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "AccessControlExtensionChanged(adminNodeId=$adminNodeId, adminPasscodeId=$adminPasscodeId, changeType=$changeType, latestValue=$latestValue, fabricIndex=$fabricIndex)"
    }
  }

  interface FabricRestrictionReviewUpdate : ClusterStruct {
    val token: ULong?
    val instruction: String?
    val arlRequestFlowUrl: String?
    val fabricIndex: UByte?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      AccessControl.FabricRestrictionReviewUpdateEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        AccessControl.FabricRestrictionReviewUpdateEvent.EventFields.token.tag -> token
        AccessControl.FabricRestrictionReviewUpdateEvent.EventFields.instruction.tag -> instruction
        AccessControl.FabricRestrictionReviewUpdateEvent.EventFields.arlRequestFlowUrl.tag ->
          arlRequestFlowUrl
        AccessControl.FabricRestrictionReviewUpdateEvent.EventFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }
  }

  /** @suppress */
  class FabricRestrictionReviewUpdateImpl(
    override val token: ULong? = null,
    override val instruction: String? = null,
    override val arlRequestFlowUrl: String? = null,
    override val fabricIndex: UByte? = null,
  ) : FabricRestrictionReviewUpdate {
    /** @suppress */
    companion object Adapter : StructAdapter<FabricRestrictionReviewUpdate> {
      val Id = ScopedEventId(AccessControlTrait.Id, 2u)

      override fun write(writer: ClusterPayloadWriter, value: FabricRestrictionReviewUpdate) {
        writer.wrapPayload(id = Id)
        writer.ulong.write(0u, value.token)
        writer.string.write(1u, value.instruction)
        writer.string.write(2u, value.arlRequestFlowUrl)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): FabricRestrictionReviewUpdate {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return FabricRestrictionReviewUpdateImpl(
          data.ulong.getOptionalNullable(0u, "Token").getOrNull(),
          data.string.getOptionalNullable(1u, "Instruction").getOrNull(),
          data.string.getOptionalNullable(2u, "ArlRequestFlowUrl").getOrNull(),
          data.ubyte.getOptionalNullable(254u, "FabricIndex").getOrNull(),
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is FabricRestrictionReviewUpdate) return false
      if (token != other.token) {
        return false
      }
      if (instruction != other.instruction) {
        return false
      }
      if (arlRequestFlowUrl != other.arlRequestFlowUrl) {
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
      result = 31 * result + (token?.hashCode() ?: 0)
      result = 31 * result + (instruction?.hashCode() ?: 0)
      result = 31 * result + (arlRequestFlowUrl?.hashCode() ?: 0)
      result = 31 * result + (fabricIndex?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "FabricRestrictionReviewUpdate(token=$token, instruction=$instruction, arlRequestFlowUrl=$arlRequestFlowUrl, fabricIndex=$fabricIndex)"
    }
  }

  // Structs
  class AccessRestrictionStruct(
    val type: AccessRestrictionTypeEnum = AccessRestrictionTypeEnum.AttributeAccessForbidden,
    val id: UInt? = null,
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
      /** The [type] command request field. */
      type(
        "type",
        0u,
        "AccessRestrictionTypeEnum",
        FieldType.Enum,
        false,
        AccessRestrictionTypeEnum.EnumDescriptor,
        false,
      ),
      /** The [id] command request field. */
      id("id", 1u, "UInt", FieldType.UInt, false, NoOpDescriptor, true),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.type.tag -> type
        StructFields.id.tag -> id
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<AccessRestrictionStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: AccessRestrictionStruct) {
        writer.enum(AccessRestrictionTypeEnum.Adapter).write(0u, value.type)
        writer.uint.write(1u, value.id)
      }

      override fun read(reader: ClusterPayloadReader): AccessRestrictionStruct {
        val data = reader.readPayload()
        return AccessRestrictionStruct(
          data.enum(AccessRestrictionTypeEnum.Adapter).get(0u, "Type"),
          data.uint.getNullable(1u, "Id"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return AccessRestrictionStruct(
          type = fields[StructFields.type] as AccessRestrictionTypeEnum,
          id = fields[StructFields.id] as UInt?,
        )
      }

      val TypedExpression<out AccessRestrictionStruct?>.type:
        TypedExpression<AccessRestrictionTypeEnum>
        get() =
          fieldSelect<AccessRestrictionStruct, AccessRestrictionTypeEnum>(this, StructFields.type)

      val TypedExpression<out AccessRestrictionStruct?>.id: TypedExpression<UInt?>
        get() = fieldSelect<AccessRestrictionStruct, UInt?>(this, StructFields.id)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessRestrictionStruct) return false
      if (type != other.type) {
        return false
      }
      if (id != other.id) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + type.hashCode()
      result = 31 * result + (id?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "AccessRestrictionStruct(type=$type, id=$id)"
    }
  }

  class CommissioningAccessRestrictionEntryStruct(
    val endpoint: UShort = 0u,
    val cluster: UInt = 0u,
    val restrictions: List<AccessRestrictionStruct> = emptyList(),
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
      /** The [endpoint] command request field. */
      endpoint("endpoint", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [cluster] command request field. */
      cluster("cluster", 1u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
      /** The [restrictions] command request field. */
      restrictions(
        "restrictions",
        2u,
        "AccessRestrictionStruct",
        FieldType.Struct,
        true,
        AccessRestrictionStruct.Adapter,
        false,
      ),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.endpoint.tag -> endpoint
        StructFields.cluster.tag -> cluster
        StructFields.restrictions.tag -> restrictions
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter :
      StructAdapter<CommissioningAccessRestrictionEntryStruct>, StructDescriptor {
      override fun write(
        writer: ClusterPayloadWriter,
        value: CommissioningAccessRestrictionEntryStruct,
      ) {
        writer.ushort.write(0u, value.endpoint)
        writer.uint.write(1u, value.cluster)
        writer.struct(AccessRestrictionStruct.Adapter).writeList(2u, value.restrictions)
      }

      override fun read(reader: ClusterPayloadReader): CommissioningAccessRestrictionEntryStruct {
        val data = reader.readPayload(mapOf(2u to AccessRestrictionStruct.Adapter))
        return CommissioningAccessRestrictionEntryStruct(
          data.ushort.get(0u, "Endpoint"),
          data.uint.get(1u, "Cluster"),
          data.struct { AccessRestrictionStruct() }.getList(2u, "Restrictions"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return CommissioningAccessRestrictionEntryStruct(
          endpoint = fields[StructFields.endpoint] as UShort,
          cluster = fields[StructFields.cluster] as UInt,
          restrictions = fields[StructFields.restrictions] as List<AccessRestrictionStruct>,
        )
      }

      val TypedExpression<out CommissioningAccessRestrictionEntryStruct?>.endpoint:
        TypedExpression<UShort>
        get() =
          fieldSelect<CommissioningAccessRestrictionEntryStruct, UShort>(
            this,
            StructFields.endpoint,
          )

      val TypedExpression<out CommissioningAccessRestrictionEntryStruct?>.cluster:
        TypedExpression<UInt>
        get() =
          fieldSelect<CommissioningAccessRestrictionEntryStruct, UInt>(this, StructFields.cluster)

      val TypedExpression<out CommissioningAccessRestrictionEntryStruct?>.restrictions:
        TypedExpression<List<AccessRestrictionStruct>>
        get() =
          fieldSelect<CommissioningAccessRestrictionEntryStruct, List<AccessRestrictionStruct>>(
            this,
            StructFields.restrictions,
          )
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is CommissioningAccessRestrictionEntryStruct) return false
      if (endpoint != other.endpoint) {
        return false
      }
      if (cluster != other.cluster) {
        return false
      }
      if (restrictions != other.restrictions) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + endpoint.hashCode()
      result = 31 * result + cluster.hashCode()
      result = 31 * result + restrictions.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "CommissioningAccessRestrictionEntryStruct(endpoint=$endpoint, cluster=$cluster, restrictions=$restrictions)"
    }
  }

  class AccessRestrictionEntryStruct(
    val endpoint: UShort = 0u,
    val cluster: UInt = 0u,
    val restrictions: List<AccessRestrictionStruct> = emptyList(),
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
      /** The [endpoint] command request field. */
      endpoint("endpoint", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [cluster] command request field. */
      cluster("cluster", 1u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
      /** The [restrictions] command request field. */
      restrictions(
        "restrictions",
        2u,
        "AccessRestrictionStruct",
        FieldType.Struct,
        true,
        AccessRestrictionStruct.Adapter,
        false,
      ),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.endpoint.tag -> endpoint
        StructFields.cluster.tag -> cluster
        StructFields.restrictions.tag -> restrictions
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<AccessRestrictionEntryStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: AccessRestrictionEntryStruct) {
        writer.ushort.write(0u, value.endpoint)
        writer.uint.write(1u, value.cluster)
        writer.struct(AccessRestrictionStruct.Adapter).writeList(2u, value.restrictions)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): AccessRestrictionEntryStruct {
        val data = reader.readPayload(mapOf(2u to AccessRestrictionStruct.Adapter))
        return AccessRestrictionEntryStruct(
          data.ushort.get(0u, "Endpoint"),
          data.uint.get(1u, "Cluster"),
          data.struct { AccessRestrictionStruct() }.getList(2u, "Restrictions"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return AccessRestrictionEntryStruct(
          endpoint = fields[StructFields.endpoint] as UShort,
          cluster = fields[StructFields.cluster] as UInt,
          restrictions = fields[StructFields.restrictions] as List<AccessRestrictionStruct>,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out AccessRestrictionEntryStruct?>.endpoint: TypedExpression<UShort>
        get() = fieldSelect<AccessRestrictionEntryStruct, UShort>(this, StructFields.endpoint)

      val TypedExpression<out AccessRestrictionEntryStruct?>.cluster: TypedExpression<UInt>
        get() = fieldSelect<AccessRestrictionEntryStruct, UInt>(this, StructFields.cluster)

      val TypedExpression<out AccessRestrictionEntryStruct?>.restrictions:
        TypedExpression<List<AccessRestrictionStruct>>
        get() =
          fieldSelect<AccessRestrictionEntryStruct, List<AccessRestrictionStruct>>(
            this,
            StructFields.restrictions,
          )

      val TypedExpression<out AccessRestrictionEntryStruct?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<AccessRestrictionEntryStruct, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessRestrictionEntryStruct) return false
      if (endpoint != other.endpoint) {
        return false
      }
      if (cluster != other.cluster) {
        return false
      }
      if (restrictions != other.restrictions) {
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
      result = 31 * result + endpoint.hashCode()
      result = 31 * result + cluster.hashCode()
      result = 31 * result + restrictions.hashCode()
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "AccessRestrictionEntryStruct(endpoint=$endpoint, cluster=$cluster, restrictions=$restrictions, fabricIndex=$fabricIndex)"
    }
  }

  class AccessControlTargetStruct(
    val cluster: UInt? = null,
    val endpoint: UShort? = null,
    val deviceType: UInt? = null,
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
      /** The [cluster] command request field. */
      cluster("cluster", 0u, "UInt", FieldType.UInt, false, NoOpDescriptor, true),
      /** The [endpoint] command request field. */
      endpoint("endpoint", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, true),
      /** The [deviceType] command request field. */
      deviceType("deviceType", 2u, "UInt", FieldType.UInt, false, NoOpDescriptor, true),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.cluster.tag -> cluster
        StructFields.endpoint.tag -> endpoint
        StructFields.deviceType.tag -> deviceType
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<AccessControlTargetStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: AccessControlTargetStruct) {
        writer.uint.write(0u, value.cluster)
        writer.ushort.write(1u, value.endpoint)
        writer.uint.write(2u, value.deviceType)
      }

      override fun read(reader: ClusterPayloadReader): AccessControlTargetStruct {
        val data = reader.readPayload()
        return AccessControlTargetStruct(
          data.uint.getNullable(0u, "Cluster"),
          data.ushort.getNullable(1u, "Endpoint"),
          data.uint.getNullable(2u, "DeviceType"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return AccessControlTargetStruct(
          cluster = fields[StructFields.cluster] as UInt?,
          endpoint = fields[StructFields.endpoint] as UShort?,
          deviceType = fields[StructFields.deviceType] as UInt?,
        )
      }

      val TypedExpression<out AccessControlTargetStruct?>.cluster: TypedExpression<UInt?>
        get() = fieldSelect<AccessControlTargetStruct, UInt?>(this, StructFields.cluster)

      val TypedExpression<out AccessControlTargetStruct?>.endpoint: TypedExpression<UShort?>
        get() = fieldSelect<AccessControlTargetStruct, UShort?>(this, StructFields.endpoint)

      val TypedExpression<out AccessControlTargetStruct?>.deviceType: TypedExpression<UInt?>
        get() = fieldSelect<AccessControlTargetStruct, UInt?>(this, StructFields.deviceType)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessControlTargetStruct) return false
      if (cluster != other.cluster) {
        return false
      }
      if (endpoint != other.endpoint) {
        return false
      }
      if (deviceType != other.deviceType) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (cluster?.hashCode() ?: 0)
      result = 31 * result + (endpoint?.hashCode() ?: 0)
      result = 31 * result + (deviceType?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "AccessControlTargetStruct(cluster=$cluster, endpoint=$endpoint, deviceType=$deviceType)"
    }
  }

  class AccessControlEntryStruct(
    val privilege: AccessControlEntryPrivilegeEnum = AccessControlEntryPrivilegeEnum.View,
    val authMode: AccessControlEntryAuthModeEnum = AccessControlEntryAuthModeEnum.PASE,
    val subjects: List<ULong>? = null,
    val targets: List<AccessControlTargetStruct>? = null,
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
      /** The [privilege] command request field. */
      privilege(
        "privilege",
        1u,
        "AccessControlEntryPrivilegeEnum",
        FieldType.Enum,
        false,
        AccessControlEntryPrivilegeEnum.EnumDescriptor,
        false,
      ),
      /** The [authMode] command request field. */
      authMode(
        "authMode",
        2u,
        "AccessControlEntryAuthModeEnum",
        FieldType.Enum,
        false,
        AccessControlEntryAuthModeEnum.EnumDescriptor,
        false,
      ),
      /** The [subjects] command request field. */
      subjects("subjects", 3u, "ULong", FieldType.ULong, true, NoOpDescriptor, true),
      /** The [targets] command request field. */
      targets(
        "targets",
        4u,
        "AccessControlTargetStruct",
        FieldType.Struct,
        true,
        AccessControlTargetStruct.Adapter,
        true,
      ),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.privilege.tag -> privilege
        StructFields.authMode.tag -> authMode
        StructFields.subjects.tag -> subjects
        StructFields.targets.tag -> targets
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<AccessControlEntryStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: AccessControlEntryStruct) {
        writer.enum(AccessControlEntryPrivilegeEnum.Adapter).write(1u, value.privilege)
        writer.enum(AccessControlEntryAuthModeEnum.Adapter).write(2u, value.authMode)
        writer.ulong.writeList(3u, value.subjects)
        writer.struct(AccessControlTargetStruct.Adapter).writeList(4u, value.targets)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): AccessControlEntryStruct {
        val data = reader.readPayload(mapOf(4u to AccessControlTargetStruct.Adapter))
        return AccessControlEntryStruct(
          data.enum(AccessControlEntryPrivilegeEnum.Adapter).get(1u, "Privilege"),
          data.enum(AccessControlEntryAuthModeEnum.Adapter).get(2u, "AuthMode"),
          data.ulong.getNullableList(3u, "Subjects"),
          data.struct { AccessControlTargetStruct() }.getNullableList(4u, "Targets"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return AccessControlEntryStruct(
          privilege = fields[StructFields.privilege] as AccessControlEntryPrivilegeEnum,
          authMode = fields[StructFields.authMode] as AccessControlEntryAuthModeEnum,
          subjects = fields[StructFields.subjects] as List<ULong>?,
          targets = fields[StructFields.targets] as List<AccessControlTargetStruct>?,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out AccessControlEntryStruct?>.privilege:
        TypedExpression<AccessControlEntryPrivilegeEnum>
        get() =
          fieldSelect<AccessControlEntryStruct, AccessControlEntryPrivilegeEnum>(
            this,
            StructFields.privilege,
          )

      val TypedExpression<out AccessControlEntryStruct?>.authMode:
        TypedExpression<AccessControlEntryAuthModeEnum>
        get() =
          fieldSelect<AccessControlEntryStruct, AccessControlEntryAuthModeEnum>(
            this,
            StructFields.authMode,
          )

      val TypedExpression<out AccessControlEntryStruct?>.subjects: TypedExpression<List<ULong>?>
        get() = fieldSelect<AccessControlEntryStruct, List<ULong>?>(this, StructFields.subjects)

      val TypedExpression<out AccessControlEntryStruct?>.targets:
        TypedExpression<List<AccessControlTargetStruct>?>
        get() =
          fieldSelect<AccessControlEntryStruct, List<AccessControlTargetStruct>?>(
            this,
            StructFields.targets,
          )

      val TypedExpression<out AccessControlEntryStruct?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<AccessControlEntryStruct, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessControlEntryStruct) return false
      if (privilege != other.privilege) {
        return false
      }
      if (authMode != other.authMode) {
        return false
      }
      if (subjects != other.subjects) {
        return false
      }
      if (targets != other.targets) {
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
      result = 31 * result + privilege.hashCode()
      result = 31 * result + authMode.hashCode()
      result = 31 * result + (subjects?.hashCode() ?: 0)
      result = 31 * result + (targets?.hashCode() ?: 0)
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "AccessControlEntryStruct(privilege=$privilege, authMode=$authMode, subjects=$subjects, targets=$targets, fabricIndex=$fabricIndex)"
    }
  }

  class AccessControlExtensionStruct(
    val data: ByteArray = ByteArray(0),
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
      /** The [`data`] command request field. */
      `data`("`data`", 1u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.`data`.tag -> data
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<AccessControlExtensionStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: AccessControlExtensionStruct) {
        writer.bytearray.write(1u, value.data)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): AccessControlExtensionStruct {
        val data = reader.readPayload()
        return AccessControlExtensionStruct(
          data.bytearray.get(1u, "Data"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return AccessControlExtensionStruct(
          data = fields[StructFields.`data`] as ByteArray,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out AccessControlExtensionStruct?>.data: TypedExpression<ByteArray>
        get() = fieldSelect<AccessControlExtensionStruct, ByteArray>(this, StructFields.`data`)

      val TypedExpression<out AccessControlExtensionStruct?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<AccessControlExtensionStruct, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessControlExtensionStruct) return false
      if (!(data contentEquals other.data)) {
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
      result = 31 * result + data.contentHashCode()
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "AccessControlExtensionStruct(data=$data, fabricIndex=$fabricIndex)"
    }
  }

  /** Attributes for the AccessControl cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {
    val acl: List<AccessControlEntryStruct>?
    val extension: List<AccessControlExtensionStruct>?
    val subjectsPerAccessControlEntry: UShort?
    val targetsPerAccessControlEntry: UShort?
    val accessControlEntriesPerFabric: UShort?
    val commissioningArl: List<CommissioningAccessRestrictionEntryStruct>?
    val arl: List<AccessRestrictionEntryStruct>?

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
    override fun getDescriptor(): StructDescriptor = AccessControl.Attribute.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        AccessControl.Attribute.acl.tag -> acl
        AccessControl.Attribute.extension.tag -> extension
        AccessControl.Attribute.subjectsPerAccessControlEntry.tag -> subjectsPerAccessControlEntry
        AccessControl.Attribute.targetsPerAccessControlEntry.tag -> targetsPerAccessControlEntry
        AccessControl.Attribute.accessControlEntriesPerFabric.tag -> accessControlEntriesPerFabric
        AccessControl.Attribute.commissioningArl.tag -> commissioningArl
        AccessControl.Attribute.arl.tag -> arl
        AccessControl.Attribute.generatedCommandList.tag -> generatedCommandList
        AccessControl.Attribute.acceptedCommandList.tag -> acceptedCommandList
        AccessControl.Attribute.attributeList.tag -> attributeList
        AccessControl.Attribute.featureMap.tag -> featureMap
        AccessControl.Attribute.clusterRevision.tag -> clusterRevision
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
          writer.struct(AccessControlEntryStruct.Adapter).writeList(0u, value.acl)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.struct(AccessControlExtensionStruct.Adapter).writeList(1u, value.extension)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.ushort.write(2u, value.subjectsPerAccessControlEntry)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.ushort.write(3u, value.targetsPerAccessControlEntry)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
          writer.ushort.write(4u, value.accessControlEntriesPerFabric)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
          writer
            .struct(CommissioningAccessRestrictionEntryStruct.Adapter)
            .writeList(5u, value.commissioningArl)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(6u)) {
          writer.struct(AccessRestrictionEntryStruct.Adapter).writeList(6u, value.arl)
        }
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.bitmap(Feature.Adapter).write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data =
          reader.readPayload(
            mapOf(
              0u to AccessControlEntryStruct.Adapter,
              1u to AccessControlExtensionStruct.Adapter,
              5u to CommissioningAccessRestrictionEntryStruct.Adapter,
              6u to AccessRestrictionEntryStruct.Adapter,
            )
          )
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data
            .struct { AccessControlEntryStruct() }
            .getOptionalNullableList(0u, "Acl")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data
            .struct { AccessControlExtensionStruct() }
            .getOptionalNullableList(1u, "Extension")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(2u, "SubjectsPerAccessControlEntry")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(3u, "TargetsPerAccessControlEntry")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(4u, "AccessControlEntriesPerFabric")
            .also { if (it.isPresent && it.value != null) attributeList.add(4u) }
            .getOrNull(),
          data
            .struct { CommissioningAccessRestrictionEntryStruct() }
            .getOptionalNullableList(5u, "CommissioningArl")
            .also { if (it.isPresent && it.value != null) attributeList.add(5u) }
            .getOrNull(),
          data
            .struct { AccessRestrictionEntryStruct() }
            .getOptionalNullableList(6u, "Arl")
            .also { if (it.isPresent && it.value != null) attributeList.add(6u) }
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
    override val acl: List<AccessControlEntryStruct>? = null,
    override val extension: List<AccessControlExtensionStruct>? = null,
    override val subjectsPerAccessControlEntry: UShort? = null,
    override val targetsPerAccessControlEntry: UShort? = null,
    override val accessControlEntriesPerFabric: UShort? = null,
    override val commissioningArl: List<CommissioningAccessRestrictionEntryStruct>? = null,
    override val arl: List<AccessRestrictionEntryStruct>? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 3u, 4u, 5u, 6u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: Feature = Feature(),
    override val clusterRevision: UShort = 0u,
  ) : Attributes, CanMutate<Attributes, MutableAttributes> {

    constructor(
      other: Attributes
    ) : this(
      acl = other.acl,
      extension = other.extension,
      subjectsPerAccessControlEntry = other.subjectsPerAccessControlEntry,
      targetsPerAccessControlEntry = other.targetsPerAccessControlEntry,
      accessControlEntriesPerFabric = other.accessControlEntriesPerFabric,
      commissioningArl = other.commissioningArl,
      arl = other.arl,
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
      if (acl != other.acl) {
        return false
      }
      if (extension != other.extension) {
        return false
      }
      if (subjectsPerAccessControlEntry != other.subjectsPerAccessControlEntry) {
        return false
      }
      if (targetsPerAccessControlEntry != other.targetsPerAccessControlEntry) {
        return false
      }
      if (accessControlEntriesPerFabric != other.accessControlEntriesPerFabric) {
        return false
      }
      if (commissioningArl != other.commissioningArl) {
        return false
      }
      if (arl != other.arl) {
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
      result = 31 * result + (acl?.hashCode() ?: 0)
      result = 31 * result + (extension?.hashCode() ?: 0)
      result = 31 * result + (subjectsPerAccessControlEntry?.hashCode() ?: 0)
      result = 31 * result + (targetsPerAccessControlEntry?.hashCode() ?: 0)
      result = 31 * result + (accessControlEntriesPerFabric?.hashCode() ?: 0)
      result = 31 * result + (commissioningArl?.hashCode() ?: 0)
      result = 31 * result + (arl?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "AccessControl(acl=$acl, extension=$extension, subjectsPerAccessControlEntry=$subjectsPerAccessControlEntry, targetsPerAccessControlEntry=$targetsPerAccessControlEntry, accessControlEntriesPerFabric=$accessControlEntriesPerFabric, commissioningArl=$commissioningArl, arl=$arl, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      acl: List<AccessControlEntryStruct>? = this.acl,
      extension: List<AccessControlExtensionStruct>? = this.extension,
      subjectsPerAccessControlEntry: UShort? = this.subjectsPerAccessControlEntry,
      targetsPerAccessControlEntry: UShort? = this.targetsPerAccessControlEntry,
      accessControlEntriesPerFabric: UShort? = this.accessControlEntriesPerFabric,
      commissioningArl: List<CommissioningAccessRestrictionEntryStruct>? = this.commissioningArl,
      arl: List<AccessRestrictionEntryStruct>? = this.arl,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: Feature = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        acl = acl,
        extension = extension,
        subjectsPerAccessControlEntry = subjectsPerAccessControlEntry,
        targetsPerAccessControlEntry = targetsPerAccessControlEntry,
        accessControlEntriesPerFabric = accessControlEntriesPerFabric,
        commissioningArl = commissioningArl,
        arl = arl,
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
      acl = attributes.acl,
      extension = attributes.extension,
      subjectsPerAccessControlEntry = attributes.subjectsPerAccessControlEntry,
      targetsPerAccessControlEntry = attributes.targetsPerAccessControlEntry,
      accessControlEntriesPerFabric = attributes.accessControlEntriesPerFabric,
      commissioningArl = attributes.commissioningArl,
      arl = attributes.arl,
      generatedCommandList = attributes.generatedCommandList,
      acceptedCommandList = attributes.acceptedCommandList,
      attributeList = attributes.attributeList,
      featureMap = attributes.featureMap,
      clusterRevision = attributes.clusterRevision,
    ) {
    internal var _acl: List<AccessControlEntryStruct>? = null
    override val acl: List<AccessControlEntryStruct>?
      get() {
        return _acl ?: super.acl
      }

    fun setAcl(value: List<AccessControlEntryStruct>) {
      _acl = value
    }

    internal var _extension: List<AccessControlExtensionStruct>? = null
    override val extension: List<AccessControlExtensionStruct>?
      get() {
        return _extension ?: super.extension
      }

    fun setExtension(value: List<AccessControlExtensionStruct>) {
      _extension = value
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is MutableAttributes) return false
      return super.equals(other)
    }

    override fun toString(): String {
      return "AccessControl.MutableAttributes(${super.toString()})"
    }

    companion object Adapter : StructAdapter<MutableAttributes> {
      override fun write(writer: ClusterPayloadWriter, value: MutableAttributes) {
        writer.wrapPayload(id = Id)
        if (value._acl != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
            writer.struct(AccessControlEntryStruct.Adapter).writeList(0u, value._acl)
          } else {
            throw HomeException.invalidArgument("acl")
          }
        }
        if (value._extension != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
            writer.struct(AccessControlExtensionStruct.Adapter).writeList(1u, value._extension)
          } else {
            throw HomeException.invalidArgument("extension")
          }
        }
      }

      override fun read(reader: ClusterPayloadReader): MutableAttributes =
        MutableAttributes(Attributes.Adapter.read(reader))
    }
  }

  // Commands

  object ReviewFabricRestrictionsCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(AccessControlTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "ReviewFabricRestrictionsCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(AccessControlTrait.Id, 1u)

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val arl: List<CommissioningAccessRestrictionEntryStruct> = emptyList()) :
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
        /** The [arl] command request field. */
        arl(
          "arl",
          0u,
          "CommissioningAccessRestrictionEntryStruct",
          FieldType.Struct,
          true,
          CommissioningAccessRestrictionEntryStruct.Adapter,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  arl = fields[CommandFields.arl] as List<CommissioningAccessRestrictionEntryStruct>
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
          CommandFields.arl.tag -> arl
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.struct(CommissioningAccessRestrictionEntryStruct.Adapter).writeList(0u, value.arl)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data =
            reader.readPayload(mapOf(0u to CommissioningAccessRestrictionEntryStruct.Adapter))
          return Request(
            data.struct { CommissioningAccessRestrictionEntryStruct() }.getList(0u, "Arl")
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (arl != other.arl) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + arl.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ReviewFabricRestrictionsCommand.Request(arl=$arl)"
      }
    }

    class Response(val token: ULong = 0u) : ClusterStruct {

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
        /** The [token] command request field. */
        token("token", 0u, "ULong", FieldType.ULong, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(token = fields[CommandFields.token] as ULong)
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.token.tag -> token
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.ulong.write(0u, value.token)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(data.ulong.get(0u, "Token"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (token != other.token) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + token.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ReviewFabricRestrictionsCommand.Response(token=$token)"
      }
    }
  }
}
