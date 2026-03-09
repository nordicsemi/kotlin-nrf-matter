// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

import com.google.errorprone.annotations.Immutable
import com.google.home.BitmapDescriptor
import com.google.home.ClusterStruct
import com.google.home.Descriptor as HomeDescriptor
import com.google.home.DescriptorMap
import com.google.home.EnumEntry
import com.google.home.Field
import com.google.home.NoOpDescriptor
import com.google.home.StructDescriptor
import com.google.home.Tag
import com.google.home.TagId
import com.google.home.Type as FieldType
import com.google.home.annotation.HomeExperimentalGenericApi
import com.google.home.automation.TypedExpression
import com.google.home.automation.fieldSelect
import com.google.home.matter.serialization.Bitmap
import com.google.home.matter.serialization.BitmapAdapter
import com.google.home.matter.serialization.ClusterBitmap
import com.google.home.matter.serialization.ClusterBitmapFlag
import com.google.home.matter.serialization.ClusterId
import com.google.home.matter.serialization.ClusterPayloadReader
import com.google.home.matter.serialization.ClusterPayloadWriter
import com.google.home.matter.serialization.MutableBitmap
import com.google.home.matter.serialization.OptionalValue
import com.google.home.matter.serialization.StructAdapter
import com.google.home.matter.serialization.unwrapPayload
import com.google.home.matter.serialization.wrapPayload
import com.google.home.toBitmapDescriptor
import com.google.home.toDescriptorMap
import javax.annotation.processing.Generated

/*
 * Serialization object for DescriptorTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for DescriptorTrait. */
@Generated("GoogleHomePlatformCodegen")
object DescriptorTrait {
  val Id = ClusterId(29u, "Descriptor")

  // Enums

  // Bitmaps
  /**
   * Descriptor cluster FeatureMap.
   *
   * @constructor Creates the Feature data class.
   */
  data class Feature(
    /** Supports semantic tags used for providing information about endpoints. */
    val tagList: Boolean = false
  ) : ClusterBitmap(traitId = ClusterId(29u).traitId, bitmapName = "Feature") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      TagList(0x1u)
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<Feature> {
          override fun toRaw(value: Feature): Bitmap =
            MutableBitmap().also { it[MaskFlags.TagList.value] = value.tagList }

          override fun toRuntime(value: Bitmap): Feature = Feature(value[MaskFlags.TagList.value])
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
  /**
   * An object describing an endpoint label specific to a manufacturer.
   *
   * @constructor Creates the SemanticTagStruct class.
   */
  @OptIn(HomeExperimentalGenericApi::class)
  class SemanticTagStruct(
    /** The manufacturer ID. */
    override val mfgCode: UShort? = null,
    override val namespaceId: UByte = 0u,
    /** The semantic tag ID located within the namespace. */
    override val tag: UByte = 0u,
    /** The human-readable text suitable for display on a client. */
    private val _label: OptionalValue<String?> = OptionalValue.absent(),
  ) : ClusterStruct, Tag {
    override val label: String? = _label.getOrNull()

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
      /** The [mfgCode] command request field. */
      mfgCode("mfgCode", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, true),
      /** The [namespaceId] command request field. */
      namespaceId("namespaceId", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
      /** The [Tag] command request field. */
      Tag("Tag", 2u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
      /** The [label] command request field. */
      label("label", 3u, "String", FieldType.String, false, NoOpDescriptor, true),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.mfgCode.tag -> mfgCode
        StructFields.namespaceId.tag -> namespaceId
        StructFields.Tag.tag -> tag
        StructFields.label.tag -> _label
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<SemanticTagStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: SemanticTagStruct) {
        writer.ushort.write(0u, value.mfgCode)
        writer.ubyte.write(1u, value.namespaceId)
        writer.ubyte.write(2u, value.tag)
        writer.string.write(3u, value.label)
      }

      override fun read(reader: ClusterPayloadReader): SemanticTagStruct {
        val data = reader.readPayload()
        return SemanticTagStruct(
          data.ushort.getNullable(0u, "MfgCode"),
          data.ubyte.get(1u, "NamespaceId"),
          data.ubyte.get(2u, "Tag"),
          data.string.getOptionalNullable(3u, "Label"),
        )
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return SemanticTagStruct(
          mfgCode = fields[StructFields.mfgCode] as UShort?,
          namespaceId = fields[StructFields.namespaceId] as UByte,
          tag = fields[StructFields.Tag] as UByte,
          _label = fields[StructFields.label] as OptionalValue<String?>,
        )
      }

      val TypedExpression<out SemanticTagStruct?>.mfgCode: TypedExpression<UShort?>
        get() = fieldSelect<SemanticTagStruct, UShort?>(this, StructFields.mfgCode)

      val TypedExpression<out SemanticTagStruct?>.namespaceId: TypedExpression<UByte>
        get() = fieldSelect<SemanticTagStruct, UByte>(this, StructFields.namespaceId)

      val TypedExpression<out SemanticTagStruct?>.tag: TypedExpression<UByte>
        get() = fieldSelect<SemanticTagStruct, UByte>(this, StructFields.Tag)

      val TypedExpression<out SemanticTagStruct?>.label: TypedExpression<OptionalValue<String?>>
        get() = fieldSelect<SemanticTagStruct, OptionalValue<String?>>(this, StructFields.label)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is SemanticTagStruct) return false
      if (mfgCode != other.mfgCode) {
        return false
      }
      if (namespaceId != other.namespaceId) {
        return false
      }
      if (tag != other.tag) {
        return false
      }
      if (label != other.label) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (mfgCode?.hashCode() ?: 0)
      result = 31 * result + namespaceId.hashCode()
      result = 31 * result + tag.hashCode()
      result = 31 * result + label.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "SemanticTagStruct(mfgCode=$mfgCode, namespaceId=$namespaceId, tag=$tag, label=$label)"
    }
  }

  /**
   * An object describing endpoint conformance to a release of a device type definition.
   *
   * @constructor Creates the DeviceTypeStruct class.
   */
  @OptIn(HomeExperimentalGenericApi::class)
  class DeviceTypeStruct(
    /** The device type definition. */
    val deviceType: UInt = 0u,
    /** The implemented revision of the device type definition. */
    val revision: UShort = 0u,
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
      /** The [deviceType] command request field. */
      deviceType("deviceType", 0u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
      /** The [revision] command request field. */
      revision("revision", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.deviceType.tag -> deviceType
        StructFields.revision.tag -> revision
        else -> null
      }
    }

    /** @suppress */
    @OptIn(HomeExperimentalGenericApi::class)
    @Immutable
    companion object Adapter : StructAdapter<DeviceTypeStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: DeviceTypeStruct) {
        writer.uint.write(0u, value.deviceType)
        writer.ushort.write(1u, value.revision)
      }

      override fun read(reader: ClusterPayloadReader): DeviceTypeStruct {
        val data = reader.readPayload()
        return DeviceTypeStruct(data.uint.get(0u, "DeviceType"), data.ushort.get(1u, "Revision"))
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return DeviceTypeStruct(
          deviceType = fields[StructFields.deviceType] as UInt,
          revision = fields[StructFields.revision] as UShort,
        )
      }

      val TypedExpression<out DeviceTypeStruct?>.deviceType: TypedExpression<UInt>
        get() = fieldSelect<DeviceTypeStruct, UInt>(this, StructFields.deviceType)

      val TypedExpression<out DeviceTypeStruct?>.revision: TypedExpression<UShort>
        get() = fieldSelect<DeviceTypeStruct, UShort>(this, StructFields.revision)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is DeviceTypeStruct) return false
      if (deviceType != other.deviceType) {
        return false
      }
      if (revision != other.revision) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + deviceType.hashCode()
      result = 31 * result + revision.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "DeviceTypeStruct(deviceType=$deviceType, revision=$revision)"
    }
  }

  /** Attributes for the Descriptor cluster. */
  @OptIn(HomeExperimentalGenericApi::class)
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {

    /**
     * A list of device types and revisions to declare endpoint conformance.
     *
     * __Access type:__ Read
     */
    val deviceTypeList: List<DeviceTypeStruct>?

    /**
     * A list of cluster IDs for the server clusters present on the endpoint.
     *
     * __Access type:__ Read
     */
    val serverList: List<UInt>?

    /**
     * A list of cluster IDs for the client clusters present on the endpoint.
     *
     * __Access type:__ Read
     */
    val clientList: List<UInt>?

    /**
     * The composition of the device type instance, including the endpoints in this list.
     *
     * __Access type:__ Read
     */
    val partsList: List<UShort>?

    /**
     * A list of tags used for disambiguating and providing information about endpoints.
     *
     * __Access type:__ Read
     */
    val tagList: List<SemanticTagStruct>?
    val endpointUniqueId: String?

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
    override fun getDescriptor(): StructDescriptor = Descriptor.Attribute.StructDescriptor
    
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        Descriptor.Attribute.deviceTypeList.tag -> deviceTypeList
        Descriptor.Attribute.serverList.tag -> serverList
        Descriptor.Attribute.clientList.tag -> clientList
        Descriptor.Attribute.partsList.tag -> partsList
        Descriptor.Attribute.tagList.tag -> tagList
        Descriptor.Attribute.endpointUniqueId.tag -> endpointUniqueId
        Descriptor.Attribute.generatedCommandList.tag -> generatedCommandList
        Descriptor.Attribute.acceptedCommandList.tag -> acceptedCommandList
        Descriptor.Attribute.attributeList.tag -> attributeList
        Descriptor.Attribute.featureMap.tag -> featureMap
        Descriptor.Attribute.clusterRevision.tag -> clusterRevision
        else -> null
      }
    }

    /** @suppress */
    companion object Adapter : StructAdapter<Attributes> {

      override fun write(writer: ClusterPayloadWriter, value: Attributes) {
        writer.wrapPayload(id = Id)
        if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
          writer.struct(DeviceTypeStruct.Adapter).writeList(0u, value.deviceTypeList)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.uint.writeList(1u, value.serverList)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.uint.writeList(2u, value.clientList)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.ushort.writeList(3u, value.partsList)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
          writer.struct(SemanticTagStruct.Adapter).writeList(4u, value.tagList)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
          writer.string.write(5u, value.endpointUniqueId)
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
          reader.readPayload(mapOf(0u to DeviceTypeStruct.Adapter, 4u to SemanticTagStruct.Adapter))
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data
            .struct { DeviceTypeStruct() }
            .getOptionalNullableList(0u, "DeviceTypeList")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data.uint
            .getOptionalNullableList(1u, "ServerList")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data.uint
            .getOptionalNullableList(2u, "ClientList")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullableList(3u, "PartsList")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
            .getOrNull(),
          data
            .struct { SemanticTagStruct() }
            .getOptionalNullableList(4u, "TagList")
            .also { if (it.isPresent && it.value != null) attributeList.add(4u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(5u, "EndpointUniqueId")
            .also { if (it.isPresent && it.value != null) attributeList.add(5u) }
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
    override val deviceTypeList: List<DeviceTypeStruct>? = null,
    override val serverList: List<UInt>? = null,
    override val clientList: List<UInt>? = null,
    override val partsList: List<UShort>? = null,
    override val tagList: List<SemanticTagStruct>? = null,
    override val endpointUniqueId: String? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 3u, 4u, 5u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: Feature = Feature(),
    override val clusterRevision: UShort = 0u,
  ) : Attributes {

    constructor(
      other: Attributes
    ) : this(
      deviceTypeList = other.deviceTypeList,
      serverList = other.serverList,
      clientList = other.clientList,
      partsList = other.partsList,
      tagList = other.tagList,
      endpointUniqueId = other.endpointUniqueId,
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
      if (deviceTypeList != other.deviceTypeList) {
        return false
      }
      if (serverList != other.serverList) {
        return false
      }
      if (clientList != other.clientList) {
        return false
      }
      if (partsList != other.partsList) {
        return false
      }
      if (tagList != other.tagList) {
        return false
      }
      if (endpointUniqueId != other.endpointUniqueId) {
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
      result = 31 * result + (deviceTypeList?.hashCode() ?: 0)
      result = 31 * result + (serverList?.hashCode() ?: 0)
      result = 31 * result + (clientList?.hashCode() ?: 0)
      result = 31 * result + (partsList?.hashCode() ?: 0)
      result = 31 * result + (tagList?.hashCode() ?: 0)
      result = 31 * result + (endpointUniqueId?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "Descriptor(deviceTypeList=$deviceTypeList, serverList=$serverList, clientList=$clientList, partsList=$partsList, tagList=$tagList, endpointUniqueId=$endpointUniqueId, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      deviceTypeList: List<DeviceTypeStruct>? = this.deviceTypeList,
      serverList: List<UInt>? = this.serverList,
      clientList: List<UInt>? = this.clientList,
      partsList: List<UShort>? = this.partsList,
      tagList: List<SemanticTagStruct>? = this.tagList,
      endpointUniqueId: String? = this.endpointUniqueId,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: Feature = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        deviceTypeList = deviceTypeList,
        serverList = serverList,
        clientList = clientList,
        partsList = partsList,
        tagList = tagList,
        endpointUniqueId = endpointUniqueId,
        generatedCommandList = generatedCommandList,
        acceptedCommandList = acceptedCommandList,
        attributeList = attributeList,
        featureMap = featureMap,
        clusterRevision = clusterRevision,
      )
  }

  // Commands

}
