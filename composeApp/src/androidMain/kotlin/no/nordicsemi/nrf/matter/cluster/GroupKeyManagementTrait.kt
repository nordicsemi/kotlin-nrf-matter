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
 * Serialization object for GroupKeyManagementTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for GroupKeyManagementTrait. */
@Generated("GoogleHomePlatformCodegen")
object GroupKeyManagementTrait {
  val Id = ClusterId(63u, "GroupKeyManagement")

  // Enums
  enum class GroupKeySecurityPolicyEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(63u).traitId,
    override val typeName: String = "GroupKeySecurityPolicyEnum",
  ) : ClusterEnum {
    TrustFirst(0u),
    CacheAndSync(1u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "GroupKeySecurityPolicyEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "GroupKeySecurityPolicyEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps
  data class Feature(val cacheAndSync: Boolean = false) :
    ClusterBitmap(traitId = ClusterId(63u).traitId, bitmapName = "Feature") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      CacheAndSync(0x1u)
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<Feature> {
          override fun toRaw(value: Feature): Bitmap =
            MutableBitmap().also { it[MaskFlags.CacheAndSync.value] = value.cacheAndSync }

          override fun toRuntime(value: Bitmap): Feature =
            Feature(value[MaskFlags.CacheAndSync.value])
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
  class GroupInfoMapStruct(
    val groupId: UShort = 0u,
    val endpoints: List<UShort> = emptyList(),
    val groupName: OptionalValue<String> = OptionalValue.absent(),
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
      /** The [groupId] command request field. */
      groupId("groupId", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [endpoints] command request field. */
      endpoints("endpoints", 2u, "UShort", FieldType.UShort, true, NoOpDescriptor, false),
      /** The [groupName] command request field. */
      groupName("groupName", 3u, "String", FieldType.String, false, NoOpDescriptor, false),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.groupId.tag -> groupId
        StructFields.endpoints.tag -> endpoints
        StructFields.groupName.tag -> groupName
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<GroupInfoMapStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: GroupInfoMapStruct) {
        writer.ushort.write(1u, value.groupId)
        writer.ushort.writeList(2u, value.endpoints)
        writer.string.write(3u, value.groupName)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): GroupInfoMapStruct {
        val data = reader.readPayload()
        return GroupInfoMapStruct(
          data.ushort.get(1u, "GroupId"),
          data.ushort.getList(2u, "Endpoints"),
          data.string.getOptional(3u, "GroupName"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return GroupInfoMapStruct(
          groupId = fields[StructFields.groupId] as UShort,
          endpoints = fields[StructFields.endpoints] as List<UShort>,
          groupName = fields[StructFields.groupName] as OptionalValue<String>,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out GroupInfoMapStruct?>.groupId: TypedExpression<UShort>
        get() = fieldSelect<GroupInfoMapStruct, UShort>(this, StructFields.groupId)

      val TypedExpression<out GroupInfoMapStruct?>.endpoints: TypedExpression<List<UShort>>
        get() = fieldSelect<GroupInfoMapStruct, List<UShort>>(this, StructFields.endpoints)

      val TypedExpression<out GroupInfoMapStruct?>.groupName: TypedExpression<OptionalValue<String>>
        get() = fieldSelect<GroupInfoMapStruct, OptionalValue<String>>(this, StructFields.groupName)

      val TypedExpression<out GroupInfoMapStruct?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<GroupInfoMapStruct, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is GroupInfoMapStruct) return false
      if (groupId != other.groupId) {
        return false
      }
      if (endpoints != other.endpoints) {
        return false
      }
      if (groupName != other.groupName) {
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
      result = 31 * result + groupId.hashCode()
      result = 31 * result + endpoints.hashCode()
      result = 31 * result + groupName.hashCode()
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "GroupInfoMapStruct(groupId=$groupId, endpoints=$endpoints, groupName=$groupName, fabricIndex=$fabricIndex)"
    }
  }

  class GroupKeyMapStruct(
    val groupId: UShort = 0u,
    val groupKeySetId: UShort = 0u,
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
      /** The [groupId] command request field. */
      groupId("groupId", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [groupKeySetId] command request field. */
      groupKeySetId("groupKeySetId", 2u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.groupId.tag -> groupId
        StructFields.groupKeySetId.tag -> groupKeySetId
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<GroupKeyMapStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: GroupKeyMapStruct) {
        writer.ushort.write(1u, value.groupId)
        writer.ushort.write(2u, value.groupKeySetId)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): GroupKeyMapStruct {
        val data = reader.readPayload()
        return GroupKeyMapStruct(
          data.ushort.get(1u, "GroupId"),
          data.ushort.get(2u, "GroupKeySetId"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return GroupKeyMapStruct(
          groupId = fields[StructFields.groupId] as UShort,
          groupKeySetId = fields[StructFields.groupKeySetId] as UShort,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out GroupKeyMapStruct?>.groupId: TypedExpression<UShort>
        get() = fieldSelect<GroupKeyMapStruct, UShort>(this, StructFields.groupId)

      val TypedExpression<out GroupKeyMapStruct?>.groupKeySetId: TypedExpression<UShort>
        get() = fieldSelect<GroupKeyMapStruct, UShort>(this, StructFields.groupKeySetId)

      val TypedExpression<out GroupKeyMapStruct?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<GroupKeyMapStruct, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is GroupKeyMapStruct) return false
      if (groupId != other.groupId) {
        return false
      }
      if (groupKeySetId != other.groupKeySetId) {
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
      result = 31 * result + groupId.hashCode()
      result = 31 * result + groupKeySetId.hashCode()
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "GroupKeyMapStruct(groupId=$groupId, groupKeySetId=$groupKeySetId, fabricIndex=$fabricIndex)"
    }
  }

  class GroupKeySetStruct(
    val groupKeySetId: UShort = 0u,
    val groupKeySecurityPolicy: GroupKeySecurityPolicyEnum = GroupKeySecurityPolicyEnum.TrustFirst,
    val epochKey0: ByteArray? = null,
    val epochStartTime0: ULong? = null,
    val epochKey1: ByteArray? = null,
    val epochStartTime1: ULong? = null,
    val epochKey2: ByteArray? = null,
    val epochStartTime2: ULong? = null,
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
      /** The [groupKeySetId] command request field. */
      groupKeySetId("groupKeySetId", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [groupKeySecurityPolicy] command request field. */
      groupKeySecurityPolicy(
        "groupKeySecurityPolicy",
        1u,
        "GroupKeySecurityPolicyEnum",
        FieldType.Enum,
        false,
        GroupKeySecurityPolicyEnum.EnumDescriptor,
        false,
      ),
      /** The [epochKey0] command request field. */
      epochKey0("epochKey0", 2u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, true),
      /** The [epochStartTime0] command request field. */
      epochStartTime0("epochStartTime0", 3u, "ULong", FieldType.ULong, false, NoOpDescriptor, true),
      /** The [epochKey1] command request field. */
      epochKey1("epochKey1", 4u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, true),
      /** The [epochStartTime1] command request field. */
      epochStartTime1("epochStartTime1", 5u, "ULong", FieldType.ULong, false, NoOpDescriptor, true),
      /** The [epochKey2] command request field. */
      epochKey2("epochKey2", 6u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, true),
      /** The [epochStartTime2] command request field. */
      epochStartTime2("epochStartTime2", 7u, "ULong", FieldType.ULong, false, NoOpDescriptor, true),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.groupKeySetId.tag -> groupKeySetId
        StructFields.groupKeySecurityPolicy.tag -> groupKeySecurityPolicy
        StructFields.epochKey0.tag -> epochKey0
        StructFields.epochStartTime0.tag -> epochStartTime0
        StructFields.epochKey1.tag -> epochKey1
        StructFields.epochStartTime1.tag -> epochStartTime1
        StructFields.epochKey2.tag -> epochKey2
        StructFields.epochStartTime2.tag -> epochStartTime2
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<GroupKeySetStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: GroupKeySetStruct) {
        writer.ushort.write(0u, value.groupKeySetId)
        writer.enum(GroupKeySecurityPolicyEnum.Adapter).write(1u, value.groupKeySecurityPolicy)
        writer.bytearray.write(2u, value.epochKey0)
        writer.ulong.write(3u, value.epochStartTime0)
        writer.bytearray.write(4u, value.epochKey1)
        writer.ulong.write(5u, value.epochStartTime1)
        writer.bytearray.write(6u, value.epochKey2)
        writer.ulong.write(7u, value.epochStartTime2)
      }

      override fun read(reader: ClusterPayloadReader): GroupKeySetStruct {
        val data = reader.readPayload()
        return GroupKeySetStruct(
          data.ushort.get(0u, "GroupKeySetId"),
          data.enum(GroupKeySecurityPolicyEnum.Adapter).get(1u, "GroupKeySecurityPolicy"),
          data.bytearray.getNullable(2u, "EpochKey0"),
          data.ulong.getNullable(3u, "EpochStartTime0"),
          data.bytearray.getNullable(4u, "EpochKey1"),
          data.ulong.getNullable(5u, "EpochStartTime1"),
          data.bytearray.getNullable(6u, "EpochKey2"),
          data.ulong.getNullable(7u, "EpochStartTime2"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return GroupKeySetStruct(
          groupKeySetId = fields[StructFields.groupKeySetId] as UShort,
          groupKeySecurityPolicy =
            fields[StructFields.groupKeySecurityPolicy] as GroupKeySecurityPolicyEnum,
          epochKey0 = fields[StructFields.epochKey0] as ByteArray?,
          epochStartTime0 = fields[StructFields.epochStartTime0] as ULong?,
          epochKey1 = fields[StructFields.epochKey1] as ByteArray?,
          epochStartTime1 = fields[StructFields.epochStartTime1] as ULong?,
          epochKey2 = fields[StructFields.epochKey2] as ByteArray?,
          epochStartTime2 = fields[StructFields.epochStartTime2] as ULong?,
        )
      }

      val TypedExpression<out GroupKeySetStruct?>.groupKeySetId: TypedExpression<UShort>
        get() = fieldSelect<GroupKeySetStruct, UShort>(this, StructFields.groupKeySetId)

      val TypedExpression<out GroupKeySetStruct?>.groupKeySecurityPolicy:
        TypedExpression<GroupKeySecurityPolicyEnum>
        get() =
          fieldSelect<GroupKeySetStruct, GroupKeySecurityPolicyEnum>(
            this,
            StructFields.groupKeySecurityPolicy,
          )

      val TypedExpression<out GroupKeySetStruct?>.epochKey0: TypedExpression<ByteArray?>
        get() = fieldSelect<GroupKeySetStruct, ByteArray?>(this, StructFields.epochKey0)

      val TypedExpression<out GroupKeySetStruct?>.epochStartTime0: TypedExpression<ULong?>
        get() = fieldSelect<GroupKeySetStruct, ULong?>(this, StructFields.epochStartTime0)

      val TypedExpression<out GroupKeySetStruct?>.epochKey1: TypedExpression<ByteArray?>
        get() = fieldSelect<GroupKeySetStruct, ByteArray?>(this, StructFields.epochKey1)

      val TypedExpression<out GroupKeySetStruct?>.epochStartTime1: TypedExpression<ULong?>
        get() = fieldSelect<GroupKeySetStruct, ULong?>(this, StructFields.epochStartTime1)

      val TypedExpression<out GroupKeySetStruct?>.epochKey2: TypedExpression<ByteArray?>
        get() = fieldSelect<GroupKeySetStruct, ByteArray?>(this, StructFields.epochKey2)

      val TypedExpression<out GroupKeySetStruct?>.epochStartTime2: TypedExpression<ULong?>
        get() = fieldSelect<GroupKeySetStruct, ULong?>(this, StructFields.epochStartTime2)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is GroupKeySetStruct) return false
      if (groupKeySetId != other.groupKeySetId) {
        return false
      }
      if (groupKeySecurityPolicy != other.groupKeySecurityPolicy) {
        return false
      }
      if (!(epochKey0 contentEquals other.epochKey0)) {
        return false
      }
      if (epochStartTime0 != other.epochStartTime0) {
        return false
      }
      if (!(epochKey1 contentEquals other.epochKey1)) {
        return false
      }
      if (epochStartTime1 != other.epochStartTime1) {
        return false
      }
      if (!(epochKey2 contentEquals other.epochKey2)) {
        return false
      }
      if (epochStartTime2 != other.epochStartTime2) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + groupKeySetId.hashCode()
      result = 31 * result + groupKeySecurityPolicy.hashCode()
      result = 31 * result + (epochKey0?.contentHashCode() ?: 0)
      result = 31 * result + (epochStartTime0?.hashCode() ?: 0)
      result = 31 * result + (epochKey1?.contentHashCode() ?: 0)
      result = 31 * result + (epochStartTime1?.hashCode() ?: 0)
      result = 31 * result + (epochKey2?.contentHashCode() ?: 0)
      result = 31 * result + (epochStartTime2?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "GroupKeySetStruct(groupKeySetId=$groupKeySetId, groupKeySecurityPolicy=$groupKeySecurityPolicy, epochKey0=$epochKey0, epochStartTime0=$epochStartTime0, epochKey1=$epochKey1, epochStartTime1=$epochStartTime1, epochKey2=$epochKey2, epochStartTime2=$epochStartTime2)"
    }
  }

  /** Attributes for the GroupKeyManagement cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {
    val groupKeyMap: List<GroupKeyMapStruct>?
    val groupTable: List<GroupInfoMapStruct>?
    val maxGroupsPerFabric: UShort?
    val maxGroupKeysPerFabric: UShort?

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
    override fun getDescriptor(): StructDescriptor = GroupKeyManagement.Attribute.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        GroupKeyManagement.Attribute.groupKeyMap.tag -> groupKeyMap
        GroupKeyManagement.Attribute.groupTable.tag -> groupTable
        GroupKeyManagement.Attribute.maxGroupsPerFabric.tag -> maxGroupsPerFabric
        GroupKeyManagement.Attribute.maxGroupKeysPerFabric.tag -> maxGroupKeysPerFabric
        GroupKeyManagement.Attribute.generatedCommandList.tag -> generatedCommandList
        GroupKeyManagement.Attribute.acceptedCommandList.tag -> acceptedCommandList
        GroupKeyManagement.Attribute.attributeList.tag -> attributeList
        GroupKeyManagement.Attribute.featureMap.tag -> featureMap
        GroupKeyManagement.Attribute.clusterRevision.tag -> clusterRevision
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
          writer.struct(GroupKeyMapStruct.Adapter).writeList(0u, value.groupKeyMap)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.struct(GroupInfoMapStruct.Adapter).writeList(1u, value.groupTable)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.ushort.write(2u, value.maxGroupsPerFabric)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.ushort.write(3u, value.maxGroupKeysPerFabric)
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
            mapOf(0u to GroupKeyMapStruct.Adapter, 1u to GroupInfoMapStruct.Adapter)
          )
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data
            .struct { GroupKeyMapStruct() }
            .getOptionalNullableList(0u, "GroupKeyMap")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data
            .struct { GroupInfoMapStruct() }
            .getOptionalNullableList(1u, "GroupTable")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(2u, "MaxGroupsPerFabric")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(3u, "MaxGroupKeysPerFabric")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
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
    override val groupKeyMap: List<GroupKeyMapStruct>? = null,
    override val groupTable: List<GroupInfoMapStruct>? = null,
    override val maxGroupsPerFabric: UShort? = null,
    override val maxGroupKeysPerFabric: UShort? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 3u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: Feature = Feature(),
    override val clusterRevision: UShort = 0u,
  ) : Attributes, CanMutate<Attributes, MutableAttributes> {

    constructor(
      other: Attributes
    ) : this(
      groupKeyMap = other.groupKeyMap,
      groupTable = other.groupTable,
      maxGroupsPerFabric = other.maxGroupsPerFabric,
      maxGroupKeysPerFabric = other.maxGroupKeysPerFabric,
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
      if (groupKeyMap != other.groupKeyMap) {
        return false
      }
      if (groupTable != other.groupTable) {
        return false
      }
      if (maxGroupsPerFabric != other.maxGroupsPerFabric) {
        return false
      }
      if (maxGroupKeysPerFabric != other.maxGroupKeysPerFabric) {
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
      result = 31 * result + (groupKeyMap?.hashCode() ?: 0)
      result = 31 * result + (groupTable?.hashCode() ?: 0)
      result = 31 * result + (maxGroupsPerFabric?.hashCode() ?: 0)
      result = 31 * result + (maxGroupKeysPerFabric?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "GroupKeyManagement(groupKeyMap=$groupKeyMap, groupTable=$groupTable, maxGroupsPerFabric=$maxGroupsPerFabric, maxGroupKeysPerFabric=$maxGroupKeysPerFabric, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      groupKeyMap: List<GroupKeyMapStruct>? = this.groupKeyMap,
      groupTable: List<GroupInfoMapStruct>? = this.groupTable,
      maxGroupsPerFabric: UShort? = this.maxGroupsPerFabric,
      maxGroupKeysPerFabric: UShort? = this.maxGroupKeysPerFabric,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: Feature = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        groupKeyMap = groupKeyMap,
        groupTable = groupTable,
        maxGroupsPerFabric = maxGroupsPerFabric,
        maxGroupKeysPerFabric = maxGroupKeysPerFabric,
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
      groupKeyMap = attributes.groupKeyMap,
      groupTable = attributes.groupTable,
      maxGroupsPerFabric = attributes.maxGroupsPerFabric,
      maxGroupKeysPerFabric = attributes.maxGroupKeysPerFabric,
      generatedCommandList = attributes.generatedCommandList,
      acceptedCommandList = attributes.acceptedCommandList,
      attributeList = attributes.attributeList,
      featureMap = attributes.featureMap,
      clusterRevision = attributes.clusterRevision,
    ) {
    internal var _groupKeyMap: List<GroupKeyMapStruct>? = null
    override val groupKeyMap: List<GroupKeyMapStruct>?
      get() {
        return _groupKeyMap ?: super.groupKeyMap
      }

    fun setGroupKeyMap(value: List<GroupKeyMapStruct>) {
      _groupKeyMap = value
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is MutableAttributes) return false
      return super.equals(other)
    }

    override fun toString(): String {
      return "GroupKeyManagement.MutableAttributes(${super.toString()})"
    }

    companion object Adapter : StructAdapter<MutableAttributes> {
      override fun write(writer: ClusterPayloadWriter, value: MutableAttributes) {
        writer.wrapPayload(id = Id)
        if (value._groupKeyMap != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
            writer.struct(GroupKeyMapStruct.Adapter).writeList(0u, value._groupKeyMap)
          } else {
            throw HomeException.invalidArgument("groupKeyMap")
          }
        }
      }

      override fun read(reader: ClusterPayloadReader): MutableAttributes =
        MutableAttributes(Attributes.Adapter.read(reader))
    }
  }

  // Commands

  object KeySetWriteCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GroupKeyManagementTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "KeySetWriteCommand"

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

    class Request(val groupKeySet: GroupKeySetStruct = GroupKeySetStruct()) : ClusterStruct {

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
        /** The [groupKeySet] command request field. */
        groupKeySet(
          "groupKeySet",
          0u,
          "GroupKeySetStruct",
          FieldType.Struct,
          false,
          GroupKeySetStruct.Adapter,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(groupKeySet = fields[CommandFields.groupKeySet] as GroupKeySetStruct)
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.groupKeySet.tag -> groupKeySet
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.struct(GroupKeySetStruct.Adapter).write(0u, value.groupKeySet)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload(mapOf(0u to GroupKeySetStruct.Adapter))
          return Request(data.struct { GroupKeySetStruct() }.get(0u, "GroupKeySet"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (groupKeySet != other.groupKeySet) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + groupKeySet.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "KeySetWriteCommand.Request(groupKeySet=$groupKeySet)"
      }
    }
  }

  object KeySetReadCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GroupKeyManagementTrait.Id, 1u)
    override val commandId = requestId.toString()
    override val commandName = "KeySetReadCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GroupKeyManagementTrait.Id, 2u)

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val groupKeySetId: UShort = 0u) : ClusterStruct {

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
        /** The [groupKeySetId] command request field. */
        groupKeySetId(
          "groupKeySetId",
          0u,
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

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(groupKeySetId = fields[CommandFields.groupKeySetId] as UShort)
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.groupKeySetId.tag -> groupKeySetId
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.groupKeySetId)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.ushort.get(0u, "GroupKeySetId"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (groupKeySetId != other.groupKeySetId) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + groupKeySetId.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "KeySetReadCommand.Request(groupKeySetId=$groupKeySetId)"
      }
    }

    class Response(val groupKeySet: GroupKeySetStruct = GroupKeySetStruct()) : ClusterStruct {

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
        /** The [groupKeySet] command request field. */
        groupKeySet(
          "groupKeySet",
          0u,
          "GroupKeySetStruct",
          FieldType.Struct,
          false,
          GroupKeySetStruct.Adapter,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  groupKeySet = fields[CommandFields.groupKeySet] as GroupKeySetStruct
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
          CommandFields.groupKeySet.tag -> groupKeySet
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.struct(GroupKeySetStruct.Adapter).write(0u, value.groupKeySet)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload(mapOf(0u to GroupKeySetStruct.Adapter))
          return Response(data.struct { GroupKeySetStruct() }.get(0u, "GroupKeySet"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (groupKeySet != other.groupKeySet) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + groupKeySet.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "KeySetReadCommand.Response(groupKeySet=$groupKeySet)"
      }
    }
  }

  object KeySetRemoveCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GroupKeyManagementTrait.Id, 3u)
    override val commandId = requestId.toString()
    override val commandName = "KeySetRemoveCommand"

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

    class Request(val groupKeySetId: UShort = 0u) : ClusterStruct {

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
        /** The [groupKeySetId] command request field. */
        groupKeySetId(
          "groupKeySetId",
          0u,
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

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(groupKeySetId = fields[CommandFields.groupKeySetId] as UShort)
              }
            }
        }
      }

      @HomeExperimentalGenericApi
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @HomeExperimentalGenericApi
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.groupKeySetId.tag -> groupKeySetId
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.groupKeySetId)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.ushort.get(0u, "GroupKeySetId"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (groupKeySetId != other.groupKeySetId) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + groupKeySetId.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "KeySetRemoveCommand.Request(groupKeySetId=$groupKeySetId)"
      }
    }
  }

  object KeySetReadAllIndicesCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(GroupKeyManagementTrait.Id, 4u)
    override val commandId = requestId.toString()
    override val commandName = "KeySetReadAllIndicesCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(GroupKeyManagementTrait.Id, 5u)

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
        return "KeySetReadAllIndicesCommand.Request()"
      }
    }

    class Response(val groupKeySetIds: List<UShort> = emptyList()) : ClusterStruct {

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
        /** The [groupKeySetIds] command request field. */
        groupKeySetIds(
          "groupKeySetIds",
          0u,
          "UShort",
          FieldType.UShort,
          true,
          NoOpDescriptor,
          false,
        );

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  groupKeySetIds = fields[CommandFields.groupKeySetIds] as List<UShort>
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
          CommandFields.groupKeySetIds.tag -> groupKeySetIds
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.ushort.writeList(0u, value.groupKeySetIds)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(data.ushort.getList(0u, "GroupKeySetIds"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (groupKeySetIds != other.groupKeySetIds) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + groupKeySetIds.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "KeySetReadAllIndicesCommand.Response(groupKeySetIds=$groupKeySetIds)"
      }
    }
  }
}
