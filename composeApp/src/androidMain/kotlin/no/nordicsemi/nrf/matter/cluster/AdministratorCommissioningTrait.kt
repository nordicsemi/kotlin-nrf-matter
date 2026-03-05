// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

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
 * Serialization object for AdministratorCommissioningTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for AdministratorCommissioningTrait. */
@Generated("GoogleHomePlatformCodegen")
object AdministratorCommissioningTrait {
  val Id = ClusterId(60u, "AdministratorCommissioning")

  // Enums
  enum class CommissioningWindowStatusEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(60u).traitId,
    override val typeName: String = "CommissioningWindowStatusEnum",
  ) : ClusterEnum {
    WindowNotOpen(0u),
    EnhancedWindowOpen(1u),
    BasicWindowOpen(2u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "CommissioningWindowStatusEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "CommissioningWindowStatusEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class StatusCode(
    override val value: ULong,
    override val traitId: String = ClusterId(60u).traitId,
    override val typeName: String = "StatusCode",
  ) : ClusterEnum {
    Busy(2u),
    PakeParameterError(3u),
    WindowNotOpen(4u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "StatusCode".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "StatusCode"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps
  data class Feature(val basic: Boolean = false) :
    ClusterBitmap(traitId = ClusterId(60u).traitId, bitmapName = "Feature") {
    override fun toRaw(): ULong {
      return Bitmap.toRaw(Adapter.toRaw(this))
    }

    private enum class MaskFlags(override val value: ULong) : ClusterBitmapFlag {
      Basic(0x1u)
    }

    /** @suppress */
    companion object {
      val Adapter =
        object : BitmapAdapter<Feature> {
          override fun toRaw(value: Feature): Bitmap =
            MutableBitmap().also { it[MaskFlags.Basic.value] = value.basic }

          override fun toRuntime(value: Bitmap): Feature = Feature(value[MaskFlags.Basic.value])
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

  /** Attributes for the AdministratorCommissioning cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {
    val windowStatus: CommissioningWindowStatusEnum?
    val adminFabricIndex: UByte?
    val adminVendorId: UShort?

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
    override fun getDescriptor(): StructDescriptor =
      AdministratorCommissioning.Attribute.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        AdministratorCommissioning.Attribute.windowStatus.tag -> windowStatus
        AdministratorCommissioning.Attribute.adminFabricIndex.tag -> adminFabricIndex
        AdministratorCommissioning.Attribute.adminVendorId.tag -> adminVendorId
        AdministratorCommissioning.Attribute.generatedCommandList.tag -> generatedCommandList
        AdministratorCommissioning.Attribute.acceptedCommandList.tag -> acceptedCommandList
        AdministratorCommissioning.Attribute.attributeList.tag -> attributeList
        AdministratorCommissioning.Attribute.featureMap.tag -> featureMap
        AdministratorCommissioning.Attribute.clusterRevision.tag -> clusterRevision
        else -> null
      }
    }

    /** @suppress */
    companion object Adapter : StructAdapter<Attributes> {

      override fun write(writer: ClusterPayloadWriter, value: Attributes) {
        writer.wrapPayload(id = Id)
        if (!writer.strictOperationValidation || value.attributeList.contains(0u)) {
          writer.enum(CommissioningWindowStatusEnum.Adapter).write(0u, value.windowStatus)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.ubyte.write(1u, value.adminFabricIndex)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.ushort.write(2u, value.adminVendorId)
        }
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.bitmap(Feature.Adapter).write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data
            .enum(CommissioningWindowStatusEnum.Adapter)
            .getOptionalNullable(0u, "WindowStatus")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data.ubyte
            .getOptionalNullable(1u, "AdminFabricIndex")
            .also { if (it.isPresent) attributeList.add(1u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(2u, "AdminVendorId")
            .also { if (it.isPresent) attributeList.add(2u) }
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
    override val windowStatus: CommissioningWindowStatusEnum? = null,
    override val adminFabricIndex: UByte? = null,
    override val adminVendorId: UShort? = null,
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> =
      listOf(0u, 1u, 2u, 65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: Feature = Feature(),
    override val clusterRevision: UShort = 0u,
  ) : Attributes {

    constructor(
      other: Attributes
    ) : this(
      windowStatus = other.windowStatus,
      adminFabricIndex = other.adminFabricIndex,
      adminVendorId = other.adminVendorId,
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
      if (windowStatus != other.windowStatus) {
        return false
      }
      if (adminFabricIndex != other.adminFabricIndex) {
        return false
      }
      if (adminVendorId != other.adminVendorId) {
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
      result = 31 * result + (windowStatus?.hashCode() ?: 0)
      result = 31 * result + (adminFabricIndex?.hashCode() ?: 0)
      result = 31 * result + (adminVendorId?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "AdministratorCommissioning(windowStatus=$windowStatus, adminFabricIndex=$adminFabricIndex, adminVendorId=$adminVendorId, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      windowStatus: CommissioningWindowStatusEnum? = this.windowStatus,
      adminFabricIndex: UByte? = this.adminFabricIndex,
      adminVendorId: UShort? = this.adminVendorId,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: Feature = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        windowStatus = windowStatus,
        adminFabricIndex = adminFabricIndex,
        adminVendorId = adminVendorId,
        generatedCommandList = generatedCommandList,
        acceptedCommandList = acceptedCommandList,
        attributeList = attributeList,
        featureMap = featureMap,
        clusterRevision = clusterRevision,
      )
  }

  // Commands

  object OpenCommissioningWindowCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(AdministratorCommissioningTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "OpenCommissioningWindowCommand"

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

    class Request(
      val commissioningTimeout: UShort = 0u,
      val pakePasscodeVerifier: ByteArray = ByteArray(0),
      val discriminator: UShort = 0u,
      val iterations: UInt = 0u,
      val salt: ByteArray = ByteArray(0),
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
        /** The [commissioningTimeout] command request field. */
        commissioningTimeout(
          "commissioningTimeout",
          0u,
          "UShort",
          FieldType.UShort,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [pakePasscodeVerifier] command request field. */
        pakePasscodeVerifier(
          "pakePasscodeVerifier",
          1u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [discriminator] command request field. */
        discriminator(
          "discriminator",
          2u,
          "UShort",
          FieldType.UShort,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [iterations] command request field. */
        iterations("iterations", 3u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
        /** The [salt] command request field. */
        salt("salt", 4u, "ByteArray", FieldType.ByteArray, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  commissioningTimeout = fields[CommandFields.commissioningTimeout] as UShort,
                  pakePasscodeVerifier = fields[CommandFields.pakePasscodeVerifier] as ByteArray,
                  discriminator = fields[CommandFields.discriminator] as UShort,
                  iterations = fields[CommandFields.iterations] as UInt,
                  salt = fields[CommandFields.salt] as ByteArray,
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
          CommandFields.commissioningTimeout.tag -> commissioningTimeout
          CommandFields.pakePasscodeVerifier.tag -> pakePasscodeVerifier
          CommandFields.discriminator.tag -> discriminator
          CommandFields.iterations.tag -> iterations
          CommandFields.salt.tag -> salt
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.commissioningTimeout)
          writer.bytearray.write(1u, value.pakePasscodeVerifier)
          writer.ushort.write(2u, value.discriminator)
          writer.uint.write(3u, value.iterations)
          writer.bytearray.write(4u, value.salt)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.ushort.get(0u, "CommissioningTimeout"),
            data.bytearray.get(1u, "PakePasscodeVerifier"),
            data.ushort.get(2u, "Discriminator"),
            data.uint.get(3u, "Iterations"),
            data.bytearray.get(4u, "Salt"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (commissioningTimeout != other.commissioningTimeout) {
          return false
        }
        if (!(pakePasscodeVerifier contentEquals other.pakePasscodeVerifier)) {
          return false
        }
        if (discriminator != other.discriminator) {
          return false
        }
        if (iterations != other.iterations) {
          return false
        }
        if (!(salt contentEquals other.salt)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + commissioningTimeout.hashCode()
        result = 31 * result + pakePasscodeVerifier.contentHashCode()
        result = 31 * result + discriminator.hashCode()
        result = 31 * result + iterations.hashCode()
        result = 31 * result + salt.contentHashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "OpenCommissioningWindowCommand.Request(commissioningTimeout=$commissioningTimeout, pakePasscodeVerifier=$pakePasscodeVerifier, discriminator=$discriminator, iterations=$iterations, salt=$salt)"
      }
    }
  }

  object OpenBasicCommissioningWindowCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(AdministratorCommissioningTrait.Id, 1u)
    override val commandId = requestId.toString()
    override val commandName = "OpenBasicCommissioningWindowCommand"

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

    class Request(val commissioningTimeout: UShort = 0u) : ClusterStruct {

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
        /** The [commissioningTimeout] command request field. */
        commissioningTimeout(
          "commissioningTimeout",
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
                return Request(
                  commissioningTimeout = fields[CommandFields.commissioningTimeout] as UShort
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
          CommandFields.commissioningTimeout.tag -> commissioningTimeout
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.commissioningTimeout)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.ushort.get(0u, "CommissioningTimeout"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (commissioningTimeout != other.commissioningTimeout) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + commissioningTimeout.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "OpenBasicCommissioningWindowCommand.Request(commissioningTimeout=$commissioningTimeout)"
      }
    }
  }

  object RevokeCommissioningCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(AdministratorCommissioningTrait.Id, 2u)
    override val commandId = requestId.toString()
    override val commandName = "RevokeCommissioningCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })

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
        return "RevokeCommissioningCommand.Request()"
      }
    }
  }
}
