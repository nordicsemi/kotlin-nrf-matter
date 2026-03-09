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
import com.google.home.NoOpDescriptor
import com.google.home.StructDescriptor
import com.google.home.TagId
import com.google.home.Type as FieldType
import com.google.home.annotation.HomeExperimentalGenericApi
import com.google.home.automation.TypedExpression
import com.google.home.automation.fieldSelect
import com.google.home.matter.serialization.ClusterEnum
import com.google.home.matter.serialization.ClusterId
import com.google.home.matter.serialization.ClusterPayloadReader
import com.google.home.matter.serialization.ClusterPayloadWriter
import com.google.home.matter.serialization.EnumAdapter
import com.google.home.matter.serialization.OptionalValue
import com.google.home.matter.serialization.ScopedCommandId
import com.google.home.matter.serialization.StructAdapter
import com.google.home.matter.serialization.camelToSnakeUpper
import com.google.home.matter.serialization.unwrapPayload
import com.google.home.matter.serialization.upperCamelToSnakeUpper
import com.google.home.matter.serialization.wrapPayload
import com.google.home.toDescriptorMap
import com.google.home.toEnumDescriptor
import javax.annotation.processing.Generated
import kotlin.collections.contentDeepEquals
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode

/*
 * Serialization object for OperationalCredentialsTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for OperationalCredentialsTrait. */
@Generated("GoogleHomePlatformCodegen")
object OperationalCredentialsTrait {
  val Id = ClusterId(62u, "OperationalCredentials")

  // Enums
  enum class CertificateChainTypeEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(62u).traitId,
    override val typeName: String = "CertificateChainTypeEnum",
  ) : ClusterEnum {
    DacCertificate(1u),
    PaiCertificate(2u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    ; //UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @OptIn(HomeExperimentalGenericApi::class)
    fun toDescription(): String {
      return "CertificateChainTypeEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @OptIn(HomeExperimentalGenericApi::class)
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "CertificateChainTypeEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class NodeOperationalCertStatusEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(62u).traitId,
    override val typeName: String = "NodeOperationalCertStatusEnum",
  ) : ClusterEnum {
    OK(0u),
    InvalidPublicKey(1u),
    InvalidNodeOpId(2u),
    InvalidNoc(3u),
    MissingCsr(4u),
    TableFull(5u),
    InvalidAdminSubject(6u),
    FabricConflict(9u),
    LabelConflict(10u),
    InvalidFabricIndex(11u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    ; //UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @OptIn(HomeExperimentalGenericApi::class)
    fun toDescription(): String {
      return "NodeOperationalCertStatusEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @OptIn(HomeExperimentalGenericApi::class)
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "NodeOperationalCertStatusEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps

  // Events

  // Structs
  class FabricDescriptorStruct(
    val rootPublicKey: ByteArray = ByteArray(0),
    val vendorId: UShort = 0u,
    val fabricId: ULong = 0u,
    val nodeId: ULong = 0u,
    val label: String = "",
    val vidVerificationStatement: OptionalValue<ByteArray> = OptionalValue.absent(),
    val fabricIndex: UByte = 0u,
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
      /** The [rootPublicKey] command request field. */
      rootPublicKey(
        "rootPublicKey",
        1u,
        "ByteArray",
        FieldType.ByteArray,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [vendorId] command request field. */
      vendorId("vendorId", 2u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
      /** The [fabricId] command request field. */
      fabricId("fabricId", 3u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
      /** The [nodeId] command request field. */
      nodeId("nodeId", 4u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
      /** The [label] command request field. */
      label("label", 5u, "String", FieldType.String, false, NoOpDescriptor, false),
      /** The [vidVerificationStatement] command request field. */
      vidVerificationStatement(
        "vidVerificationStatement",
        6u,
        "ByteArray",
        FieldType.ByteArray,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.rootPublicKey.tag -> rootPublicKey
        StructFields.vendorId.tag -> vendorId
        StructFields.fabricId.tag -> fabricId
        StructFields.nodeId.tag -> nodeId
        StructFields.label.tag -> label
        StructFields.vidVerificationStatement.tag -> vidVerificationStatement
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<FabricDescriptorStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: FabricDescriptorStruct) {
        writer.bytearray.write(1u, value.rootPublicKey)
        writer.ushort.write(2u, value.vendorId)
        writer.ulong.write(3u, value.fabricId)
        writer.ulong.write(4u, value.nodeId)
        writer.string.write(5u, value.label)
        writer.bytearray.write(6u, value.vidVerificationStatement)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): FabricDescriptorStruct {
        val data = reader.readPayload()
        return FabricDescriptorStruct(
          data.bytearray.get(1u, "RootPublicKey"),
          data.ushort.get(2u, "VendorId"),
          data.ulong.get(3u, "FabricId"),
          data.ulong.get(4u, "NodeId"),
          data.string.get(5u, "Label"),
          data.bytearray.getOptional(6u, "VidVerificationStatement"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return FabricDescriptorStruct(
          rootPublicKey = fields[StructFields.rootPublicKey] as ByteArray,
          vendorId = fields[StructFields.vendorId] as UShort,
          fabricId = fields[StructFields.fabricId] as ULong,
          nodeId = fields[StructFields.nodeId] as ULong,
          label = fields[StructFields.label] as String,
          vidVerificationStatement =
            fields[StructFields.vidVerificationStatement] as OptionalValue<ByteArray>,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out FabricDescriptorStruct?>.rootPublicKey: TypedExpression<ByteArray>
        get() = fieldSelect<FabricDescriptorStruct, ByteArray>(this, StructFields.rootPublicKey)

      val TypedExpression<out FabricDescriptorStruct?>.vendorId: TypedExpression<UShort>
        get() = fieldSelect<FabricDescriptorStruct, UShort>(this, StructFields.vendorId)

      val TypedExpression<out FabricDescriptorStruct?>.fabricId: TypedExpression<ULong>
        get() = fieldSelect<FabricDescriptorStruct, ULong>(this, StructFields.fabricId)

      val TypedExpression<out FabricDescriptorStruct?>.nodeId: TypedExpression<ULong>
        get() = fieldSelect<FabricDescriptorStruct, ULong>(this, StructFields.nodeId)

      val TypedExpression<out FabricDescriptorStruct?>.label: TypedExpression<String>
        get() = fieldSelect<FabricDescriptorStruct, String>(this, StructFields.label)

      val TypedExpression<out FabricDescriptorStruct?>.vidVerificationStatement:
        TypedExpression<OptionalValue<ByteArray>>
        get() =
          fieldSelect<FabricDescriptorStruct, OptionalValue<ByteArray>>(
            this,
            StructFields.vidVerificationStatement,
          )

      val TypedExpression<out FabricDescriptorStruct?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<FabricDescriptorStruct, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is FabricDescriptorStruct) return false
      if (!(rootPublicKey contentEquals other.rootPublicKey)) {
        return false
      }
      if (vendorId != other.vendorId) {
        return false
      }
      if (fabricId != other.fabricId) {
        return false
      }
      if (nodeId != other.nodeId) {
        return false
      }
      if (label != other.label) {
        return false
      }
      if (vidVerificationStatement != other.vidVerificationStatement) {
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
      result = 31 * result + rootPublicKey.contentHashCode()
      result = 31 * result + vendorId.hashCode()
      result = 31 * result + fabricId.hashCode()
      result = 31 * result + nodeId.hashCode()
      result = 31 * result + label.hashCode()
      result = 31 * result + vidVerificationStatement.hashCode()
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "FabricDescriptorStruct(rootPublicKey=$rootPublicKey, vendorId=$vendorId, fabricId=$fabricId, nodeId=$nodeId, label=$label, vidVerificationStatement=$vidVerificationStatement, fabricIndex=$fabricIndex)"
    }
  }

  class NocStruct(
    val noc: ByteArray = ByteArray(0),
    val icac: ByteArray? = null,
    val vvsc: OptionalValue<ByteArray> = OptionalValue.absent(),
    val fabricIndex: UByte = 0u,
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
      /** The [noc] command request field. */
      noc("noc", 1u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
      /** The [icac] command request field. */
      icac("icac", 2u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, true),
      /** The [vvsc] command request field. */
      vvsc("vvsc", 3u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
      /** The [fabricIndex] command request field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    }

    @OptIn(HomeExperimentalGenericApi::class) override fun getDescriptor(): StructDescriptor = Adapter

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.noc.tag -> noc
        StructFields.icac.tag -> icac
        StructFields.vvsc.tag -> vvsc
        StructFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<NocStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: NocStruct) {
        writer.bytearray.write(1u, value.noc)
        writer.bytearray.write(2u, value.icac)
        writer.bytearray.write(3u, value.vvsc)
        writer.ubyte.write(254u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): NocStruct {
        val data = reader.readPayload()
        return NocStruct(
          data.bytearray.get(1u, "Noc"),
          data.bytearray.getNullable(2u, "Icac"),
          data.bytearray.getOptional(3u, "Vvsc"),
          data.ubyte.get(254u, "FabricIndex"),
        )
      }

      @OptIn(HomeExperimentalGenericApi::class)
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @OptIn(HomeExperimentalGenericApi::class)
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return NocStruct(
          noc = fields[StructFields.noc] as ByteArray,
          icac = fields[StructFields.icac] as ByteArray?,
          vvsc = fields[StructFields.vvsc] as OptionalValue<ByteArray>,
          fabricIndex = fields[StructFields.fabricIndex] as UByte,
        )
      }

      val TypedExpression<out NocStruct?>.noc: TypedExpression<ByteArray>
        get() = fieldSelect<NocStruct, ByteArray>(this, StructFields.noc)

      val TypedExpression<out NocStruct?>.icac: TypedExpression<ByteArray?>
        get() = fieldSelect<NocStruct, ByteArray?>(this, StructFields.icac)

      val TypedExpression<out NocStruct?>.vvsc: TypedExpression<OptionalValue<ByteArray>>
        get() = fieldSelect<NocStruct, OptionalValue<ByteArray>>(this, StructFields.vvsc)

      val TypedExpression<out NocStruct?>.fabricIndex: TypedExpression<UByte>
        get() = fieldSelect<NocStruct, UByte>(this, StructFields.fabricIndex)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is NocStruct) return false
      if (!(noc contentEquals other.noc)) {
        return false
      }
      if (!(icac contentEquals other.icac)) {
        return false
      }
      if (vvsc != other.vvsc) {
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
      result = 31 * result + noc.contentHashCode()
      result = 31 * result + (icac?.contentHashCode() ?: 0)
      result = 31 * result + vvsc.hashCode()
      result = 31 * result + fabricIndex.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "NocStruct(noc=$noc, icac=$icac, vvsc=$vvsc, fabricIndex=$fabricIndex)"
    }
  }

  /** Attributes for the OperationalCredentials cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {
    val nocs: List<NocStruct>?
    val fabrics: List<FabricDescriptorStruct>?
    val supportedFabrics: UByte?
    val commissionedFabrics: UByte?
    val trustedRootCertificates: List<ByteArray>?
    val currentFabricIndex: UByte?

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

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getDescriptor(): StructDescriptor =
      OperationalCredentials.Attribute.StructDescriptor

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        OperationalCredentials.Attribute.nocs.tag -> nocs
        OperationalCredentials.Attribute.fabrics.tag -> fabrics
        OperationalCredentials.Attribute.supportedFabrics.tag -> supportedFabrics
        OperationalCredentials.Attribute.commissionedFabrics.tag -> commissionedFabrics
        OperationalCredentials.Attribute.trustedRootCertificates.tag -> trustedRootCertificates
        OperationalCredentials.Attribute.currentFabricIndex.tag -> currentFabricIndex
        OperationalCredentials.Attribute.generatedCommandList.tag -> generatedCommandList
        OperationalCredentials.Attribute.acceptedCommandList.tag -> acceptedCommandList
        OperationalCredentials.Attribute.attributeList.tag -> attributeList
        OperationalCredentials.Attribute.featureMap.tag -> featureMap
        OperationalCredentials.Attribute.clusterRevision.tag -> clusterRevision
        else -> null
      }
    }

    /** @suppress */
    companion object Adapter : StructAdapter<Attributes> {

      override fun write(writer: ClusterPayloadWriter, value: Attributes) {
        writer.wrapPayload(id = Id)
        if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
          writer.struct(NocStruct.Adapter).writeList(0u, value.nocs)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.struct(FabricDescriptorStruct.Adapter).writeList(1u, value.fabrics)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.ubyte.write(2u, value.supportedFabrics)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.ubyte.write(3u, value.commissionedFabrics)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
          writer.bytearray.writeList(4u, value.trustedRootCertificates)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
          writer.ubyte.write(5u, value.currentFabricIndex)
        }
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.uint.write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data =
          reader.readPayload(mapOf(0u to NocStruct.Adapter, 1u to FabricDescriptorStruct.Adapter))
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data
            .struct { NocStruct() }
            .getOptionalNullableList(0u, "Nocs")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data
            .struct { FabricDescriptorStruct() }
            .getOptionalNullableList(1u, "Fabrics")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data.ubyte
            .getOptionalNullable(2u, "SupportedFabrics")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.ubyte
            .getOptionalNullable(3u, "CommissionedFabrics")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
            .getOrNull(),
          data.bytearray
            .getOptionalNullableList(4u, "TrustedRootCertificates")
            .also { if (it.isPresent && it.value != null) attributeList.add(4u) }
            .getOrNull(),
          data.ubyte
            .getOptionalNullable(5u, "CurrentFabricIndex")
            .also { if (it.isPresent && it.value != null) attributeList.add(5u) }
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
    override val nocs: List<NocStruct>? = null,
    override val fabrics: List<FabricDescriptorStruct>? = null,
    override val supportedFabrics: UByte? = null,
    override val commissionedFabrics: UByte? = null,
    override val trustedRootCertificates: List<ByteArray>? = null,
    override val currentFabricIndex: UByte? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 3u, 4u, 5u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: UInt = 0u,
    override val clusterRevision: UShort = 0u,
  ) : Attributes {

    constructor(
      other: Attributes
    ) : this(
      nocs = other.nocs,
      fabrics = other.fabrics,
      supportedFabrics = other.supportedFabrics,
      commissionedFabrics = other.commissionedFabrics,
      trustedRootCertificates = other.trustedRootCertificates,
      currentFabricIndex = other.currentFabricIndex,
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
      if (nocs != other.nocs) {
        return false
      }
      if (fabrics != other.fabrics) {
        return false
      }
      if (supportedFabrics != other.supportedFabrics) {
        return false
      }
      if (commissionedFabrics != other.commissionedFabrics) {
        return false
      }
      if (
        !(trustedRootCertificates?.toTypedArray() contentDeepEquals
          other.trustedRootCertificates?.toTypedArray())
      ) {
        return false
      }
      if (currentFabricIndex != other.currentFabricIndex) {
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
      result = 31 * result + (nocs?.hashCode() ?: 0)
      result = 31 * result + (fabrics?.hashCode() ?: 0)
      result = 31 * result + (supportedFabrics?.hashCode() ?: 0)
      result = 31 * result + (commissionedFabrics?.hashCode() ?: 0)
      result = 31 * result + (trustedRootCertificates?.toTypedArray().contentDeepHashCode() ?: 0)
      result = 31 * result + (currentFabricIndex?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "OperationalCredentials(nocs=$nocs, fabrics=$fabrics, supportedFabrics=$supportedFabrics, commissionedFabrics=$commissionedFabrics, trustedRootCertificates=$trustedRootCertificates, currentFabricIndex=$currentFabricIndex, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      nocs: List<NocStruct>? = this.nocs,
      fabrics: List<FabricDescriptorStruct>? = this.fabrics,
      supportedFabrics: UByte? = this.supportedFabrics,
      commissionedFabrics: UByte? = this.commissionedFabrics,
      trustedRootCertificates: List<ByteArray>? = this.trustedRootCertificates,
      currentFabricIndex: UByte? = this.currentFabricIndex,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: UInt = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        nocs = nocs,
        fabrics = fabrics,
        supportedFabrics = supportedFabrics,
        commissionedFabrics = commissionedFabrics,
        trustedRootCertificates = trustedRootCertificates,
        currentFabricIndex = currentFabricIndex,
        generatedCommandList = generatedCommandList,
        acceptedCommandList = acceptedCommandList,
        attributeList = attributeList,
        featureMap = featureMap,
        clusterRevision = clusterRevision,
      )
  }

  // Commands

  object AttestationRequestCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "AttestationRequestCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 1u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val attestationNonce: ByteArray = ByteArray(0)) : ClusterStruct {

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
        /** The [attestationNonce] command request field. */
        attestationNonce(
          "attestationNonce",
          0u,
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
                  attestationNonce = fields[CommandFields.attestationNonce] as ByteArray
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
          CommandFields.attestationNonce.tag -> attestationNonce
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.attestationNonce)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.bytearray.get(0u, "AttestationNonce"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(attestationNonce contentEquals other.attestationNonce)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + attestationNonce.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AttestationRequestCommand.Request(attestationNonce=$attestationNonce)"
      }
    }

    class Response(
      val attestationElements: ByteArray = ByteArray(0),
      val attestationSignature: ByteArray = ByteArray(0),
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
        /** The [attestationElements] command request field. */
        attestationElements(
          "attestationElements",
          0u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [attestationSignature] command request field. */
        attestationSignature(
          "attestationSignature",
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
                  attestationElements = fields[CommandFields.attestationElements] as ByteArray,
                  attestationSignature = fields[CommandFields.attestationSignature] as ByteArray,
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
          CommandFields.attestationElements.tag -> attestationElements
          CommandFields.attestationSignature.tag -> attestationSignature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.bytearray.write(0u, value.attestationElements)
          writer.bytearray.write(1u, value.attestationSignature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.bytearray.get(0u, "AttestationElements"),
            data.bytearray.get(1u, "AttestationSignature"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (!(attestationElements contentEquals other.attestationElements)) {
          return false
        }
        if (!(attestationSignature contentEquals other.attestationSignature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + attestationElements.contentHashCode()
        result = 31 * result + attestationSignature.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AttestationRequestCommand.Response(attestationElements=$attestationElements, attestationSignature=$attestationSignature)"
      }
    }
  }

  object CertificateChainRequestCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 2u)
    override val commandId = requestId.toString()
    override val commandName = "CertificateChainRequestCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 3u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val certificateType: CertificateChainTypeEnum = CertificateChainTypeEnum.DacCertificate
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
        /** The [certificateType] command request field. */
        certificateType(
          "certificateType",
          0u,
          "CertificateChainTypeEnum",
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
                return Request(
                  certificateType =
                    fields[CommandFields.certificateType] as CertificateChainTypeEnum
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
          CommandFields.certificateType.tag -> certificateType
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.enum(CertificateChainTypeEnum.Adapter).write(0u, value.certificateType)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.enum(CertificateChainTypeEnum.Adapter).get(0u, "CertificateType"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (certificateType != other.certificateType) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + certificateType.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "CertificateChainRequestCommand.Request(certificateType=$certificateType)"
      }
    }

    class Response(val certificate: ByteArray = ByteArray(0)) : ClusterStruct {

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
        /** The [certificate] command request field. */
        certificate(
          "certificate",
          0u,
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
                return Response(certificate = fields[CommandFields.certificate] as ByteArray)
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.certificate.tag -> certificate
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.bytearray.write(0u, value.certificate)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(data.bytearray.get(0u, "Certificate"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (!(certificate contentEquals other.certificate)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + certificate.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "CertificateChainRequestCommand.Response(certificate=$certificate)"
      }
    }
  }

  object CsrRequestCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 4u)
    override val commandId = requestId.toString()
    override val commandName = "CsrRequestCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 5u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val csrNonce: ByteArray = ByteArray(0),
      val isForUpdateNoc: OptionalValue<Boolean> = OptionalValue.absent(),
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
        /** The [csrNonce] command request field. */
        csrNonce("csrNonce", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [isForUpdateNoc] command request field. */
        isForUpdateNoc(
          "isForUpdateNoc",
          1u,
          "Boolean",
          FieldType.Boolean,
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
                  csrNonce = fields[CommandFields.csrNonce] as ByteArray,
                  isForUpdateNoc = fields[CommandFields.isForUpdateNoc] as OptionalValue<Boolean>,
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
          CommandFields.csrNonce.tag -> csrNonce
          CommandFields.isForUpdateNoc.tag -> isForUpdateNoc
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.csrNonce)
          writer.boolean.write(1u, value.isForUpdateNoc)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "CsrNonce"),
            data.boolean.getOptional(1u, "IsForUpdateNoc"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(csrNonce contentEquals other.csrNonce)) {
          return false
        }
        if (isForUpdateNoc != other.isForUpdateNoc) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + csrNonce.contentHashCode()
        result = 31 * result + isForUpdateNoc.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "CsrRequestCommand.Request(csrNonce=$csrNonce, isForUpdateNoc=$isForUpdateNoc)"
      }
    }

    /** Optional arguments for the command CsrRequestCommand Request */
    interface OptionalArgs {
      var isForUpdateNoc: Boolean
    }

    class Response(
      val nocsrElements: ByteArray = ByteArray(0),
      val attestationSignature: ByteArray = ByteArray(0),
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
        /** The [nocsrElements] command request field. */
        nocsrElements(
          "nocsrElements",
          0u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [attestationSignature] command request field. */
        attestationSignature(
          "attestationSignature",
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
                  nocsrElements = fields[CommandFields.nocsrElements] as ByteArray,
                  attestationSignature = fields[CommandFields.attestationSignature] as ByteArray,
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
          CommandFields.nocsrElements.tag -> nocsrElements
          CommandFields.attestationSignature.tag -> attestationSignature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.bytearray.write(0u, value.nocsrElements)
          writer.bytearray.write(1u, value.attestationSignature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.bytearray.get(0u, "NocsrElements"),
            data.bytearray.get(1u, "AttestationSignature"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (!(nocsrElements contentEquals other.nocsrElements)) {
          return false
        }
        if (!(attestationSignature contentEquals other.attestationSignature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + nocsrElements.contentHashCode()
        result = 31 * result + attestationSignature.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "CsrRequestCommand.Response(nocsrElements=$nocsrElements, attestationSignature=$attestationSignature)"
      }
    }
  }

  object AddNocCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 6u)
    override val commandId = requestId.toString()
    override val commandName = "AddNocCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 8u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val nocValue: ByteArray = ByteArray(0),
      val icacValue: OptionalValue<ByteArray> = OptionalValue.absent(),
      val ipkValue: ByteArray = ByteArray(0),
      val caseAdminSubject: ULong = 0u,
      val adminVendorId: UShort = 0u,
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
        /** The [nocValue] command request field. */
        nocValue("nocValue", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [icacValue] command request field. */
        icacValue("icacValue", 1u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [ipkValue] command request field. */
        ipkValue("ipkValue", 2u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [caseAdminSubject] command request field. */
        caseAdminSubject(
          "caseAdminSubject",
          3u,
          "ULong",
          FieldType.ULong,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [adminVendorId] command request field. */
        adminVendorId(
          "adminVendorId",
          4u,
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
                  nocValue = fields[CommandFields.nocValue] as ByteArray,
                  icacValue = fields[CommandFields.icacValue] as OptionalValue<ByteArray>,
                  ipkValue = fields[CommandFields.ipkValue] as ByteArray,
                  caseAdminSubject = fields[CommandFields.caseAdminSubject] as ULong,
                  adminVendorId = fields[CommandFields.adminVendorId] as UShort,
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
          CommandFields.nocValue.tag -> nocValue
          CommandFields.icacValue.tag -> icacValue
          CommandFields.ipkValue.tag -> ipkValue
          CommandFields.caseAdminSubject.tag -> caseAdminSubject
          CommandFields.adminVendorId.tag -> adminVendorId
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.nocValue)
          writer.bytearray.write(1u, value.icacValue)
          writer.bytearray.write(2u, value.ipkValue)
          writer.ulong.write(3u, value.caseAdminSubject)
          writer.ushort.write(4u, value.adminVendorId)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "NocValue"),
            data.bytearray.getOptional(1u, "IcacValue"),
            data.bytearray.get(2u, "IpkValue"),
            data.ulong.get(3u, "CaseAdminSubject"),
            data.ushort.get(4u, "AdminVendorId"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(nocValue contentEquals other.nocValue)) {
          return false
        }
        if (icacValue != other.icacValue) {
          return false
        }
        if (!(ipkValue contentEquals other.ipkValue)) {
          return false
        }
        if (caseAdminSubject != other.caseAdminSubject) {
          return false
        }
        if (adminVendorId != other.adminVendorId) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + nocValue.contentHashCode()
        result = 31 * result + icacValue.hashCode()
        result = 31 * result + ipkValue.contentHashCode()
        result = 31 * result + caseAdminSubject.hashCode()
        result = 31 * result + adminVendorId.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AddNocCommand.Request(nocValue=$nocValue, icacValue=$icacValue, ipkValue=$ipkValue, caseAdminSubject=$caseAdminSubject, adminVendorId=$adminVendorId)"
      }
    }

    /** Optional arguments for the command AddNocCommand Request */
    interface OptionalArgs {
      var icacValue: ByteArray
    }

    class Response(
      val statusCode: NodeOperationalCertStatusEnum = NodeOperationalCertStatusEnum.OK,
      val fabricIndex: UByte? = null,
      val debugText: String? = null,
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
        /** The [statusCode] command request field. */
        statusCode(
          "statusCode",
          0u,
          "NodeOperationalCertStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [fabricIndex] command request field. */
        fabricIndex("fabricIndex", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [debugText] command request field. */
        debugText("debugText", 2u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  statusCode = fields[CommandFields.statusCode] as NodeOperationalCertStatusEnum,
                  fabricIndex = fields[CommandFields.fabricIndex] as UByte?,
                  debugText = fields[CommandFields.debugText] as String?,
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
          CommandFields.statusCode.tag -> statusCode
          CommandFields.fabricIndex.tag -> fabricIndex
          CommandFields.debugText.tag -> debugText
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NodeOperationalCertStatusEnum.Adapter).write(0u, value.statusCode)
          writer.ubyte.write(1u, value.fabricIndex)
          writer.string.write(2u, value.debugText)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NodeOperationalCertStatusEnum.Adapter).get(0u, "StatusCode"),
            data.ubyte.getOptionalNullable(1u, "FabricIndex").getOrNull(),
            data.string.getOptionalNullable(2u, "DebugText").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (statusCode != other.statusCode) {
          return false
        }
        if (fabricIndex != other.fabricIndex) {
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
        result = 31 * result + statusCode.hashCode()
        result = 31 * result + (fabricIndex?.hashCode() ?: 0)
        result = 31 * result + (debugText?.hashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AddNocCommand.Response(statusCode=$statusCode, fabricIndex=$fabricIndex, debugText=$debugText)"
      }
    }
  }

  object UpdateNocCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 7u)
    override val commandId = requestId.toString()
    override val commandName = "UpdateNocCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 8u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val nocValue: ByteArray = ByteArray(0),
      val icacValue: OptionalValue<ByteArray> = OptionalValue.absent(),
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
        /** The [nocValue] command request field. */
        nocValue("nocValue", 0u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false),
        /** The [icacValue] command request field. */
        icacValue("icacValue", 1u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  nocValue = fields[CommandFields.nocValue] as ByteArray,
                  icacValue = fields[CommandFields.icacValue] as OptionalValue<ByteArray>,
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
          CommandFields.nocValue.tag -> nocValue
          CommandFields.icacValue.tag -> icacValue
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.nocValue)
          writer.bytearray.write(1u, value.icacValue)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "NocValue"),
            data.bytearray.getOptional(1u, "IcacValue"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(nocValue contentEquals other.nocValue)) {
          return false
        }
        if (icacValue != other.icacValue) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + nocValue.contentHashCode()
        result = 31 * result + icacValue.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "UpdateNocCommand.Request(nocValue=$nocValue, icacValue=$icacValue)"
      }
    }

    /** Optional arguments for the command UpdateNocCommand Request */
    interface OptionalArgs {
      var icacValue: ByteArray
    }

    class Response(
      val statusCode: NodeOperationalCertStatusEnum = NodeOperationalCertStatusEnum.OK,
      val fabricIndex: UByte? = null,
      val debugText: String? = null,
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
        /** The [statusCode] command request field. */
        statusCode(
          "statusCode",
          0u,
          "NodeOperationalCertStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [fabricIndex] command request field. */
        fabricIndex("fabricIndex", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [debugText] command request field. */
        debugText("debugText", 2u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  statusCode = fields[CommandFields.statusCode] as NodeOperationalCertStatusEnum,
                  fabricIndex = fields[CommandFields.fabricIndex] as UByte?,
                  debugText = fields[CommandFields.debugText] as String?,
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
          CommandFields.statusCode.tag -> statusCode
          CommandFields.fabricIndex.tag -> fabricIndex
          CommandFields.debugText.tag -> debugText
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NodeOperationalCertStatusEnum.Adapter).write(0u, value.statusCode)
          writer.ubyte.write(1u, value.fabricIndex)
          writer.string.write(2u, value.debugText)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NodeOperationalCertStatusEnum.Adapter).get(0u, "StatusCode"),
            data.ubyte.getOptionalNullable(1u, "FabricIndex").getOrNull(),
            data.string.getOptionalNullable(2u, "DebugText").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (statusCode != other.statusCode) {
          return false
        }
        if (fabricIndex != other.fabricIndex) {
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
        result = 31 * result + statusCode.hashCode()
        result = 31 * result + (fabricIndex?.hashCode() ?: 0)
        result = 31 * result + (debugText?.hashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "UpdateNocCommand.Response(statusCode=$statusCode, fabricIndex=$fabricIndex, debugText=$debugText)"
      }
    }
  }

  object UpdateFabricLabelCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 9u)
    override val commandId = requestId.toString()
    override val commandName = "UpdateFabricLabelCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 8u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val label: String = "") : ClusterStruct {

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
        /** The [label] command request field. */
        label("label", 0u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(label = fields[CommandFields.label] as String)
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.label.tag -> label
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.string.write(0u, value.label)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.string.get(0u, "Label"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (label != other.label) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + label.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "UpdateFabricLabelCommand.Request(label=$label)"
      }
    }

    class Response(
      val statusCode: NodeOperationalCertStatusEnum = NodeOperationalCertStatusEnum.OK,
      val fabricIndex: UByte? = null,
      val debugText: String? = null,
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
        /** The [statusCode] command request field. */
        statusCode(
          "statusCode",
          0u,
          "NodeOperationalCertStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [fabricIndex] command request field. */
        fabricIndex("fabricIndex", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [debugText] command request field. */
        debugText("debugText", 2u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  statusCode = fields[CommandFields.statusCode] as NodeOperationalCertStatusEnum,
                  fabricIndex = fields[CommandFields.fabricIndex] as UByte?,
                  debugText = fields[CommandFields.debugText] as String?,
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
          CommandFields.statusCode.tag -> statusCode
          CommandFields.fabricIndex.tag -> fabricIndex
          CommandFields.debugText.tag -> debugText
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NodeOperationalCertStatusEnum.Adapter).write(0u, value.statusCode)
          writer.ubyte.write(1u, value.fabricIndex)
          writer.string.write(2u, value.debugText)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NodeOperationalCertStatusEnum.Adapter).get(0u, "StatusCode"),
            data.ubyte.getOptionalNullable(1u, "FabricIndex").getOrNull(),
            data.string.getOptionalNullable(2u, "DebugText").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (statusCode != other.statusCode) {
          return false
        }
        if (fabricIndex != other.fabricIndex) {
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
        result = 31 * result + statusCode.hashCode()
        result = 31 * result + (fabricIndex?.hashCode() ?: 0)
        result = 31 * result + (debugText?.hashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "UpdateFabricLabelCommand.Response(statusCode=$statusCode, fabricIndex=$fabricIndex, debugText=$debugText)"
      }
    }
  }

  object RemoveFabricCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 10u)
    override val commandId = requestId.toString()
    override val commandName = "RemoveFabricCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 8u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val fabricIndex: UByte = 0u) : ClusterStruct {

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
        /** The [fabricIndex] command request field. */
        fabricIndex("fabricIndex", 0u, "UByte", FieldType.UByte, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(fabricIndex = fields[CommandFields.fabricIndex] as UByte)
              }
            }
        }
      }

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getDescriptor(): StructDescriptor = CommandFields.Companion.StructDescriptor

      @OptIn(HomeExperimentalGenericApi::class)
      override fun getFieldValueById(tagId: TagId): Any? {
        return when (tagId) {
          CommandFields.fabricIndex.tag -> fabricIndex
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ubyte.write(0u, value.fabricIndex)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.ubyte.get(0u, "FabricIndex"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (fabricIndex != other.fabricIndex) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + fabricIndex.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "RemoveFabricCommand.Request(fabricIndex=$fabricIndex)"
      }
    }

    class Response(
      val statusCode: NodeOperationalCertStatusEnum = NodeOperationalCertStatusEnum.OK,
      val fabricIndex: UByte? = null,
      val debugText: String? = null,
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
        /** The [statusCode] command request field. */
        statusCode(
          "statusCode",
          0u,
          "NodeOperationalCertStatusEnum",
          FieldType.Enum,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [fabricIndex] command request field. */
        fabricIndex("fabricIndex", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [debugText] command request field. */
        debugText("debugText", 2u, "String", FieldType.String, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  statusCode = fields[CommandFields.statusCode] as NodeOperationalCertStatusEnum,
                  fabricIndex = fields[CommandFields.fabricIndex] as UByte?,
                  debugText = fields[CommandFields.debugText] as String?,
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
          CommandFields.statusCode.tag -> statusCode
          CommandFields.fabricIndex.tag -> fabricIndex
          CommandFields.debugText.tag -> debugText
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(NodeOperationalCertStatusEnum.Adapter).write(0u, value.statusCode)
          writer.ubyte.write(1u, value.fabricIndex)
          writer.string.write(2u, value.debugText)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(NodeOperationalCertStatusEnum.Adapter).get(0u, "StatusCode"),
            data.ubyte.getOptionalNullable(1u, "FabricIndex").getOrNull(),
            data.string.getOptionalNullable(2u, "DebugText").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (statusCode != other.statusCode) {
          return false
        }
        if (fabricIndex != other.fabricIndex) {
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
        result = 31 * result + statusCode.hashCode()
        result = 31 * result + (fabricIndex?.hashCode() ?: 0)
        result = 31 * result + (debugText?.hashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "RemoveFabricCommand.Response(statusCode=$statusCode, fabricIndex=$fabricIndex, debugText=$debugText)"
      }
    }
  }

  object AddTrustedRootCertificateCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 11u)
    override val commandId = requestId.toString()
    override val commandName = "AddTrustedRootCertificateCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val rootCaCertificate: ByteArray = ByteArray(0)) : ClusterStruct {

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
        /** The [rootCaCertificate] command request field. */
        rootCaCertificate(
          "rootCaCertificate",
          0u,
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
                  rootCaCertificate = fields[CommandFields.rootCaCertificate] as ByteArray
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
          CommandFields.rootCaCertificate.tag -> rootCaCertificate
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.rootCaCertificate)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.bytearray.get(0u, "RootCaCertificate"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(rootCaCertificate contentEquals other.rootCaCertificate)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + rootCaCertificate.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "AddTrustedRootCertificateCommand.Request(rootCaCertificate=$rootCaCertificate)"
      }
    }
  }

  object SetVidVerificationStatementCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 12u)
    override val commandId = requestId.toString()
    override val commandName = "SetVIDVerificationStatementCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val vendorId: OptionalValue<UShort> = OptionalValue.absent(),
      val vidVerificationStatement: OptionalValue<ByteArray> = OptionalValue.absent(),
      val vvsc: OptionalValue<ByteArray> = OptionalValue.absent(),
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
        /** The [vendorId] command request field. */
        vendorId("vendorId", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
        /** The [vidVerificationStatement] command request field. */
        vidVerificationStatement(
          "vidVerificationStatement",
          1u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [vvsc] command request field. */
        vvsc("vvsc", 2u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  vendorId = fields[CommandFields.vendorId] as OptionalValue<UShort>,
                  vidVerificationStatement =
                    fields[CommandFields.vidVerificationStatement] as OptionalValue<ByteArray>,
                  vvsc = fields[CommandFields.vvsc] as OptionalValue<ByteArray>,
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
          CommandFields.vendorId.tag -> vendorId
          CommandFields.vidVerificationStatement.tag -> vidVerificationStatement
          CommandFields.vvsc.tag -> vvsc
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.vendorId)
          writer.bytearray.write(1u, value.vidVerificationStatement)
          writer.bytearray.write(2u, value.vvsc)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.ushort.getOptional(0u, "VendorId"),
            data.bytearray.getOptional(1u, "VidVerificationStatement"),
            data.bytearray.getOptional(2u, "Vvsc"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (vendorId != other.vendorId) {
          return false
        }
        if (vidVerificationStatement != other.vidVerificationStatement) {
          return false
        }
        if (vvsc != other.vvsc) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + vendorId.hashCode()
        result = 31 * result + vidVerificationStatement.hashCode()
        result = 31 * result + vvsc.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "SetVIDVerificationStatementCommand.Request(vendorId=$vendorId, vidVerificationStatement=$vidVerificationStatement, vvsc=$vvsc)"
      }
    }

    /** Optional arguments for the command SetVIDVerificationStatementCommand Request */
    interface OptionalArgs {
      var vendorId: UShort
      var vidVerificationStatement: ByteArray
      var vvsc: ByteArray
    }
  }

  object SignVidVerificationRequestCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OperationalCredentialsTrait.Id, 13u)
    override val commandId = requestId.toString()
    override val commandName = "SignVIDVerificationRequestCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OperationalCredentialsTrait.Id, 14u)

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @OptIn(HomeExperimentalGenericApi::class)
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val fabricIndex: UByte = 0u, val clientChallenge: ByteArray = ByteArray(0)) :
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
        /** The [fabricIndex] command request field. */
        fabricIndex("fabricIndex", 0u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [clientChallenge] command request field. */
        clientChallenge(
          "clientChallenge",
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
                  fabricIndex = fields[CommandFields.fabricIndex] as UByte,
                  clientChallenge = fields[CommandFields.clientChallenge] as ByteArray,
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
          CommandFields.fabricIndex.tag -> fabricIndex
          CommandFields.clientChallenge.tag -> clientChallenge
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ubyte.write(0u, value.fabricIndex)
          writer.bytearray.write(1u, value.clientChallenge)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.ubyte.get(0u, "FabricIndex"),
            data.bytearray.get(1u, "ClientChallenge"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (fabricIndex != other.fabricIndex) {
          return false
        }
        if (!(clientChallenge contentEquals other.clientChallenge)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + fabricIndex.hashCode()
        result = 31 * result + clientChallenge.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "SignVIDVerificationRequestCommand.Request(fabricIndex=$fabricIndex, clientChallenge=$clientChallenge)"
      }
    }

    class Response(
      val fabricIndex: UByte = 0u,
      val fabricBindingVersion: UByte = 0u,
      val signature: ByteArray = ByteArray(0),
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
        /** The [fabricIndex] command request field. */
        fabricIndex("fabricIndex", 0u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
        /** The [fabricBindingVersion] command request field. */
        fabricBindingVersion(
          "fabricBindingVersion",
          1u,
          "UByte",
          FieldType.UByte,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [signature] command request field. */
        signature("signature", 2u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @OptIn(HomeExperimentalGenericApi::class)
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  fabricIndex = fields[CommandFields.fabricIndex] as UByte,
                  fabricBindingVersion = fields[CommandFields.fabricBindingVersion] as UByte,
                  signature = fields[CommandFields.signature] as ByteArray,
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
          CommandFields.fabricIndex.tag -> fabricIndex
          CommandFields.fabricBindingVersion.tag -> fabricBindingVersion
          CommandFields.signature.tag -> signature
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.ubyte.write(0u, value.fabricIndex)
          writer.ubyte.write(1u, value.fabricBindingVersion)
          writer.bytearray.write(2u, value.signature)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.ubyte.get(0u, "FabricIndex"),
            data.ubyte.get(1u, "FabricBindingVersion"),
            data.bytearray.get(2u, "Signature"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (fabricIndex != other.fabricIndex) {
          return false
        }
        if (fabricBindingVersion != other.fabricBindingVersion) {
          return false
        }
        if (!(signature contentEquals other.signature)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + fabricIndex.hashCode()
        result = 31 * result + fabricBindingVersion.hashCode()
        result = 31 * result + signature.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "SignVIDVerificationRequestCommand.Response(fabricIndex=$fabricIndex, fabricBindingVersion=$fabricBindingVersion, signature=$signature)"
      }
    }
  }
}
