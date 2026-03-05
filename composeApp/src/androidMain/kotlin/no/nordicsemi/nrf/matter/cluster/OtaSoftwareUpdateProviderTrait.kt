// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

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
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode

/*
 * Serialization object for OtaSoftwareUpdateProviderTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for OtaSoftwareUpdateProviderTrait. */
@Generated("GoogleHomePlatformCodegen")
object OtaSoftwareUpdateProviderTrait {
  val Id = ClusterId(41u, "OtaSoftwareUpdateProvider")

  // Enums
  enum class ApplyUpdateActionEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(41u).traitId,
    override val typeName: String = "ApplyUpdateActionEnum",
  ) : ClusterEnum {
    Proceed(0u),
    AwaitNextAction(1u),
    Discontinue(2u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "ApplyUpdateActionEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "ApplyUpdateActionEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class DownloadProtocolEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(41u).traitId,
    override val typeName: String = "DownloadProtocolEnum",
  ) : ClusterEnum {
    BdxSynchronous(0u),
    BdxAsynchronous(1u),
    HTTPS(2u),
    VendorSpecific(3u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "DownloadProtocolEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "DownloadProtocolEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  enum class StatusEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(41u).traitId,
    override val typeName: String = "StatusEnum",
  ) : ClusterEnum {
    UpdateAvailable(0u),
    Busy(1u),
    NotAvailable(2u),
    DownloadProtocolNotSupported(3u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "StatusEnum".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "StatusEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps

  // Events

  // Structs

  /** Attributes for the OtaSoftwareUpdateProvider cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {

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
      OtaSoftwareUpdateProvider.Attribute.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        OtaSoftwareUpdateProvider.Attribute.generatedCommandList.tag -> generatedCommandList
        OtaSoftwareUpdateProvider.Attribute.acceptedCommandList.tag -> acceptedCommandList
        OtaSoftwareUpdateProvider.Attribute.attributeList.tag -> attributeList
        OtaSoftwareUpdateProvider.Attribute.featureMap.tag -> featureMap
        OtaSoftwareUpdateProvider.Attribute.clusterRevision.tag -> clusterRevision
        else -> null
      }
    }

    /** @suppress */
    companion object Adapter : StructAdapter<Attributes> {

      override fun write(writer: ClusterPayloadWriter, value: Attributes) {
        writer.wrapPayload(id = Id)
        writer.uint.writeList(65528u, value.generatedCommandList)
        writer.uint.writeList(65529u, value.acceptedCommandList)
        writer.uint.writeList(65531u, value.attributeList)
        writer.uint.write(65532u, value.featureMap)
        writer.ushort.write(65533u, value.clusterRevision)
      }

      override fun read(reader: ClusterPayloadReader): Attributes {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
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
    override val generatedCommandList: List<UInt> = emptyList(),
    override val acceptedCommandList: List<UInt> = emptyList(),
    override val attributeList: List<UInt> = listOf(65528u, 65529u, 65531u, 65532u, 65533u),
    override val featureMap: UInt = 0u,
    override val clusterRevision: UShort = 0u,
  ) : Attributes {

    constructor(
      other: Attributes
    ) : this(
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
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "OtaSoftwareUpdateProvider(generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: UInt = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        generatedCommandList = generatedCommandList,
        acceptedCommandList = acceptedCommandList,
        attributeList = attributeList,
        featureMap = featureMap,
        clusterRevision = clusterRevision,
      )
  }

  // Commands

  object QueryImageCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OtaSoftwareUpdateProviderTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "QueryImageCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OtaSoftwareUpdateProviderTrait.Id, 1u)

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(
      val vendorId: UShort = 0u,
      val productId: UShort = 0u,
      val softwareVersion: UInt = 0u,
      val protocolsSupported: List<DownloadProtocolEnum> = emptyList(),
      val hardwareVersion: OptionalValue<UShort> = OptionalValue.absent(),
      val location: OptionalValue<String> = OptionalValue.absent(),
      val requestorCanConsent: OptionalValue<Boolean> = OptionalValue.absent(),
      val metadataForProvider: OptionalValue<ByteArray> = OptionalValue.absent(),
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
        /** The [vendorId] command request field. */
        vendorId("vendorId", 0u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
        /** The [productId] command request field. */
        productId("productId", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
        /** The [softwareVersion] command request field. */
        softwareVersion(
          "softwareVersion",
          2u,
          "UInt",
          FieldType.UInt,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [protocolsSupported] command request field. */
        protocolsSupported(
          "protocolsSupported",
          3u,
          "DownloadProtocolEnum",
          FieldType.Enum,
          true,
          NoOpDescriptor,
          false,
        ),
        /** The [hardwareVersion] command request field. */
        hardwareVersion(
          "hardwareVersion",
          4u,
          "UShort",
          FieldType.UShort,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [location] command request field. */
        location("location", 5u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [requestorCanConsent] command request field. */
        requestorCanConsent(
          "requestorCanConsent",
          6u,
          "Boolean",
          FieldType.Boolean,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [metadataForProvider] command request field. */
        metadataForProvider(
          "metadataForProvider",
          7u,
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

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  vendorId = fields[CommandFields.vendorId] as UShort,
                  productId = fields[CommandFields.productId] as UShort,
                  softwareVersion = fields[CommandFields.softwareVersion] as UInt,
                  protocolsSupported =
                    fields[CommandFields.protocolsSupported] as List<DownloadProtocolEnum>,
                  hardwareVersion = fields[CommandFields.hardwareVersion] as OptionalValue<UShort>,
                  location = fields[CommandFields.location] as OptionalValue<String>,
                  requestorCanConsent =
                    fields[CommandFields.requestorCanConsent] as OptionalValue<Boolean>,
                  metadataForProvider =
                    fields[CommandFields.metadataForProvider] as OptionalValue<ByteArray>,
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
          CommandFields.vendorId.tag -> vendorId
          CommandFields.productId.tag -> productId
          CommandFields.softwareVersion.tag -> softwareVersion
          CommandFields.protocolsSupported.tag -> protocolsSupported
          CommandFields.hardwareVersion.tag -> hardwareVersion
          CommandFields.location.tag -> location
          CommandFields.requestorCanConsent.tag -> requestorCanConsent
          CommandFields.metadataForProvider.tag -> metadataForProvider
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.ushort.write(0u, value.vendorId)
          writer.ushort.write(1u, value.productId)
          writer.uint.write(2u, value.softwareVersion)
          writer.enum(DownloadProtocolEnum.Adapter).writeList(3u, value.protocolsSupported)
          writer.ushort.write(4u, value.hardwareVersion)
          writer.string.write(5u, value.location)
          writer.boolean.write(6u, value.requestorCanConsent)
          writer.bytearray.write(7u, value.metadataForProvider)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.ushort.get(0u, "VendorId"),
            data.ushort.get(1u, "ProductId"),
            data.uint.get(2u, "SoftwareVersion"),
            data.enum(DownloadProtocolEnum.Adapter).getList(3u, "ProtocolsSupported"),
            data.ushort.getOptional(4u, "HardwareVersion"),
            data.string.getOptional(5u, "Location"),
            data.boolean.getOptional(6u, "RequestorCanConsent"),
            data.bytearray.getOptional(7u, "MetadataForProvider"),
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
        if (productId != other.productId) {
          return false
        }
        if (softwareVersion != other.softwareVersion) {
          return false
        }
        if (protocolsSupported != other.protocolsSupported) {
          return false
        }
        if (hardwareVersion != other.hardwareVersion) {
          return false
        }
        if (location != other.location) {
          return false
        }
        if (requestorCanConsent != other.requestorCanConsent) {
          return false
        }
        if (metadataForProvider != other.metadataForProvider) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + vendorId.hashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + softwareVersion.hashCode()
        result = 31 * result + protocolsSupported.hashCode()
        result = 31 * result + hardwareVersion.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + requestorCanConsent.hashCode()
        result = 31 * result + metadataForProvider.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "QueryImageCommand.Request(vendorId=$vendorId, productId=$productId, softwareVersion=$softwareVersion, protocolsSupported=$protocolsSupported, hardwareVersion=$hardwareVersion, location=$location, requestorCanConsent=$requestorCanConsent, metadataForProvider=$metadataForProvider)"
      }
    }

    /** Optional arguments for the command QueryImageCommand Request */
    interface OptionalArgs {
      var hardwareVersion: UShort
      var location: String
      var requestorCanConsent: Boolean
      var metadataForProvider: ByteArray
    }

    class Response(
      val status: StatusEnum = StatusEnum.UpdateAvailable,
      val delayedActionTime: UInt? = null,
      val imageUri: String? = null,
      val softwareVersion: UInt? = null,
      val softwareVersionString: String? = null,
      val updateToken: ByteArray? = null,
      val userConsentNeeded: Boolean? = null,
      val metadataForRequestor: ByteArray? = null,
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
        /** The [status] command request field. */
        status("status", 0u, "StatusEnum", FieldType.Enum, false, NoOpDescriptor, false),
        /** The [delayedActionTime] command request field. */
        delayedActionTime(
          "delayedActionTime",
          1u,
          "UInt",
          FieldType.UInt,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [imageUri] command request field. */
        imageUri("imageUri", 2u, "String", FieldType.String, false, NoOpDescriptor, false),
        /** The [softwareVersion] command request field. */
        softwareVersion(
          "softwareVersion",
          3u,
          "UInt",
          FieldType.UInt,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [softwareVersionString] command request field. */
        softwareVersionString(
          "softwareVersionString",
          4u,
          "String",
          FieldType.String,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [updateToken] command request field. */
        updateToken(
          "updateToken",
          5u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [userConsentNeeded] command request field. */
        userConsentNeeded(
          "userConsentNeeded",
          6u,
          "Boolean",
          FieldType.Boolean,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [metadataForRequestor] command request field. */
        metadataForRequestor(
          "metadataForRequestor",
          7u,
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

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Response(
                  status = fields[CommandFields.status] as StatusEnum,
                  delayedActionTime = fields[CommandFields.delayedActionTime] as UInt?,
                  imageUri = fields[CommandFields.imageUri] as String?,
                  softwareVersion = fields[CommandFields.softwareVersion] as UInt?,
                  softwareVersionString = fields[CommandFields.softwareVersionString] as String?,
                  updateToken = fields[CommandFields.updateToken] as ByteArray?,
                  userConsentNeeded = fields[CommandFields.userConsentNeeded] as Boolean?,
                  metadataForRequestor = fields[CommandFields.metadataForRequestor] as ByteArray?,
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
          CommandFields.status.tag -> status
          CommandFields.delayedActionTime.tag -> delayedActionTime
          CommandFields.imageUri.tag -> imageUri
          CommandFields.softwareVersion.tag -> softwareVersion
          CommandFields.softwareVersionString.tag -> softwareVersionString
          CommandFields.updateToken.tag -> updateToken
          CommandFields.userConsentNeeded.tag -> userConsentNeeded
          CommandFields.metadataForRequestor.tag -> metadataForRequestor
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(StatusEnum.Adapter).write(0u, value.status)
          writer.uint.write(1u, value.delayedActionTime)
          writer.string.write(2u, value.imageUri)
          writer.uint.write(3u, value.softwareVersion)
          writer.string.write(4u, value.softwareVersionString)
          writer.bytearray.write(5u, value.updateToken)
          writer.boolean.write(6u, value.userConsentNeeded)
          writer.bytearray.write(7u, value.metadataForRequestor)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(StatusEnum.Adapter).get(0u, "Status"),
            data.uint.getOptionalNullable(1u, "DelayedActionTime").getOrNull(),
            data.string.getOptionalNullable(2u, "ImageUri").getOrNull(),
            data.uint.getOptionalNullable(3u, "SoftwareVersion").getOrNull(),
            data.string.getOptionalNullable(4u, "SoftwareVersionString").getOrNull(),
            data.bytearray.getOptionalNullable(5u, "UpdateToken").getOrNull(),
            data.boolean.getOptionalNullable(6u, "UserConsentNeeded").getOrNull(),
            data.bytearray.getOptionalNullable(7u, "MetadataForRequestor").getOrNull(),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (status != other.status) {
          return false
        }
        if (delayedActionTime != other.delayedActionTime) {
          return false
        }
        if (imageUri != other.imageUri) {
          return false
        }
        if (softwareVersion != other.softwareVersion) {
          return false
        }
        if (softwareVersionString != other.softwareVersionString) {
          return false
        }
        if (!(updateToken contentEquals other.updateToken)) {
          return false
        }
        if (userConsentNeeded != other.userConsentNeeded) {
          return false
        }
        if (!(metadataForRequestor contentEquals other.metadataForRequestor)) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + status.hashCode()
        result = 31 * result + (delayedActionTime?.hashCode() ?: 0)
        result = 31 * result + (imageUri?.hashCode() ?: 0)
        result = 31 * result + (softwareVersion?.hashCode() ?: 0)
        result = 31 * result + (softwareVersionString?.hashCode() ?: 0)
        result = 31 * result + (updateToken?.contentHashCode() ?: 0)
        result = 31 * result + (userConsentNeeded?.hashCode() ?: 0)
        result = 31 * result + (metadataForRequestor?.contentHashCode() ?: 0)

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "QueryImageCommand.Response(status=$status, delayedActionTime=$delayedActionTime, imageUri=$imageUri, softwareVersion=$softwareVersion, softwareVersionString=$softwareVersionString, updateToken=$updateToken, userConsentNeeded=$userConsentNeeded, metadataForRequestor=$metadataForRequestor)"
      }
    }
  }

  object ApplyUpdateRequestCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OtaSoftwareUpdateProviderTrait.Id, 2u)
    override val commandId = requestId.toString()
    override val commandName = "ApplyUpdateRequestCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })
    /** @suppress */
    val responseId = ScopedCommandId(OtaSoftwareUpdateProviderTrait.Id, 3u)

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldById(tagId: UInt): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalGenericApi
    override fun getCommandRequestFieldByName(name: String): com.google.home.Field? {
      return Request.CommandFields.values().firstOrNull { it.name == name }
    }

    class Request(val updateToken: ByteArray = ByteArray(0), val newVersion: UInt = 0u) :
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
        /** The [updateToken] command request field. */
        updateToken(
          "updateToken",
          0u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [newVersion] command request field. */
        newVersion("newVersion", 1u, "UInt", FieldType.UInt, false, NoOpDescriptor, false);

        companion object {
          val StructDescriptor =
            object : StructDescriptor {
              @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

              @HomeExperimentalGenericApi
              override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
                return Request(
                  updateToken = fields[CommandFields.updateToken] as ByteArray,
                  newVersion = fields[CommandFields.newVersion] as UInt,
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
          CommandFields.updateToken.tag -> updateToken
          CommandFields.newVersion.tag -> newVersion
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.updateToken)
          writer.uint.write(1u, value.newVersion)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(data.bytearray.get(0u, "UpdateToken"), data.uint.get(1u, "NewVersion"))
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(updateToken contentEquals other.updateToken)) {
          return false
        }
        if (newVersion != other.newVersion) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + updateToken.contentHashCode()
        result = 31 * result + newVersion.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ApplyUpdateRequestCommand.Request(updateToken=$updateToken, newVersion=$newVersion)"
      }
    }

    class Response(
      val action: ApplyUpdateActionEnum = ApplyUpdateActionEnum.Proceed,
      val delayedActionTime: UInt = 0u,
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
        /** The [action] command request field. */
        action("action", 0u, "ApplyUpdateActionEnum", FieldType.Enum, false, NoOpDescriptor, false),
        /** The [delayedActionTime] command request field. */
        delayedActionTime(
          "delayedActionTime",
          1u,
          "UInt",
          FieldType.UInt,
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
                return Response(
                  action = fields[CommandFields.action] as ApplyUpdateActionEnum,
                  delayedActionTime = fields[CommandFields.delayedActionTime] as UInt,
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
          CommandFields.action.tag -> action
          CommandFields.delayedActionTime.tag -> delayedActionTime
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Response> {
        override fun write(writer: ClusterPayloadWriter, value: Response) {
          writer.wrapPayload(id = responseId)
          writer.enum(ApplyUpdateActionEnum.Adapter).write(0u, value.action)
          writer.uint.write(1u, value.delayedActionTime)
        }

        override fun read(reader: ClusterPayloadReader): Response {
          reader.unwrapPayload(id = responseId)
          val data = reader.readPayload()
          return Response(
            data.enum(ApplyUpdateActionEnum.Adapter).get(0u, "Action"),
            data.uint.get(1u, "DelayedActionTime"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Response) return false
        if (action != other.action) {
          return false
        }
        if (delayedActionTime != other.delayedActionTime) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + action.hashCode()
        result = 31 * result + delayedActionTime.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "ApplyUpdateRequestCommand.Response(action=$action, delayedActionTime=$delayedActionTime)"
      }
    }
  }

  object NotifyUpdateAppliedCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(OtaSoftwareUpdateProviderTrait.Id, 4u)
    override val commandId = requestId.toString()
    override val commandName = "NotifyUpdateAppliedCommand"

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

    class Request(val updateToken: ByteArray = ByteArray(0), val softwareVersion: UInt = 0u) :
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
        /** The [updateToken] command request field. */
        updateToken(
          "updateToken",
          0u,
          "ByteArray",
          FieldType.ByteArray,
          false,
          NoOpDescriptor,
          false,
        ),
        /** The [softwareVersion] command request field. */
        softwareVersion(
          "softwareVersion",
          1u,
          "UInt",
          FieldType.UInt,
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
                  updateToken = fields[CommandFields.updateToken] as ByteArray,
                  softwareVersion = fields[CommandFields.softwareVersion] as UInt,
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
          CommandFields.updateToken.tag -> updateToken
          CommandFields.softwareVersion.tag -> softwareVersion
          else -> null
        }
      }

      /** @suppress */
      companion object Adapter : StructAdapter<Request> {
        override fun write(writer: ClusterPayloadWriter, value: Request) {
          writer.wrapPayload(id = requestId)
          writer.bytearray.write(0u, value.updateToken)
          writer.uint.write(1u, value.softwareVersion)
        }

        override fun read(reader: ClusterPayloadReader): Request {
          reader.unwrapPayload(id = requestId)
          val data = reader.readPayload()
          return Request(
            data.bytearray.get(0u, "UpdateToken"),
            data.uint.get(1u, "SoftwareVersion"),
          )
        }
      }

      /** @suppress */
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Request) return false
        if (!(updateToken contentEquals other.updateToken)) {
          return false
        }
        if (softwareVersion != other.softwareVersion) {
          return false
        }

        return true
      }

      /** @suppress */
      override fun hashCode(): Int {
        var result = 1
        result = 31 * result + updateToken.contentHashCode()
        result = 31 * result + softwareVersion.hashCode()

        return result
      }

      /** @suppress */
      override fun toString(): String {
        return "NotifyUpdateAppliedCommand.Request(updateToken=$updateToken, softwareVersion=$softwareVersion)"
      }
    }
  }
}
