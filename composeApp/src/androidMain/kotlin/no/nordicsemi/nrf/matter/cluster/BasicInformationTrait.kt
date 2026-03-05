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
 * Serialization object for BasicInformationTrait.
 *
 * This file was machine generate via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Attributes for BasicInformationTrait. */
@Generated("GoogleHomePlatformCodegen")
object BasicInformationTrait {
  val Id = ClusterId(40u, "BasicInformation")

  // Enums
  /** Color values. */
  enum class ColorEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(40u).traitId,
    override val typeName: String = "ColorEnum",
  ) : ClusterEnum {
    /** Black. */
    Black(0u),
    /** Navy. */
    Navy(1u),
    /** Green. */
    Green(2u),
    /** Teal. */
    Teal(3u),
    /** Maroon. */
    Maroon(4u),
    /** Purple. */
    Purple(5u),
    /** Olive. */
    Olive(6u),
    /** Gray. */
    Gray(7u),
    /** Blue. */
    Blue(8u),
    /** Lime. */
    Lime(9u),
    /** Aqua. */
    Aqua(10u),
    /** Red. */
    Red(11u),
    /** Fuchsia. */
    Fuchsia(12u),
    /** Yellow. */
    Yellow(13u),
    /** White. */
    White(14u),
    /** Nickel. */
    Nickel(15u),
    /** Chrome. */
    Chrome(16u),
    /** Brass. */
    Brass(17u),
    /** Copper. */
    Copper(18u),
    /** Silver. */
    Silver(19u),
    /** Gold. */
    Gold(20u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "ColorEnum".upperCamelToSnakeUpper() + "_" + super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "ColorEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  /** Product finish values. */
  enum class ProductFinishEnum(
    override val value: ULong,
    override val traitId: String = ClusterId(40u).traitId,
    override val typeName: String = "ProductFinishEnum",
  ) : ClusterEnum {
    /** Other. */
    Other(0u),
    /** Matte. */
    Matte(1u),
    /** Satin. */
    Satin(2u),
    /** Polished. */
    Polished(3u),
    /** Rugged. */
    Rugged(4u),
    /** Fabric. */
    Fabric(5u),
    // When encountering fields that out of range (e.g due to newer schema), this value is emitted
    /**
     * The enum value is out of range. For example, a newer Matter cluster definition may support
     * enum values not yet supported by the Home APIs.
     */
    UnknownValue(ClusterEnum.UNKNOWN_ENUM_VALUE_CODE);

    @HomeExperimentalGenericApi
    fun toDescription(): String {
      return "ProductFinishEnum".upperCamelToSnakeUpper() +
        "_" +
        super.toString().camelToSnakeUpper()
    }

    /** @suppress */
    companion object {
      val Adapter = EnumAdapter(values())

      @HomeExperimentalGenericApi
      val EnumDescriptor =
        object : EnumDescriptor {
          override val name: String = "ProductFinishEnum"

          @Suppress("Immutable")
          override val values: Map<ULong, EnumEntry> = entries.toEnumDescriptor()
        }
    }
  }

  // Bitmaps

  // Events
  /** Indicates that the node has completed a boot or reboot process. */
  interface StartUp : ClusterStruct {
    /**
     * The software version of the node. Set to the same value as available in the
     * [softwareVersion][BasicInformationTrait.Attributes.softwareVersion] attribute.
     */
    val softwareVersion: UInt?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      BasicInformation.StartUpEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        BasicInformation.StartUpEvent.EventFields.softwareVersion.tag -> softwareVersion
        else -> null
      }
    }
  }

  /** @suppress */
  class StartUpImpl(override val softwareVersion: UInt? = null) : StartUp {
    /** @suppress */
    companion object Adapter : StructAdapter<StartUp> {
      val Id = ScopedEventId(BasicInformationTrait.Id, 0u)

      override fun write(writer: ClusterPayloadWriter, value: StartUp) {
        writer.wrapPayload(id = Id)
        writer.uint.write(0u, value.softwareVersion)
      }

      override fun read(reader: ClusterPayloadReader): StartUp {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return StartUpImpl(data.uint.getOptionalNullable(0u, "SoftwareVersion").getOrNull())
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is StartUp) return false
      if (softwareVersion != other.softwareVersion) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (softwareVersion?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "StartUp(softwareVersion=$softwareVersion)"
    }
  }

  @Suppress("ClassShouldBeObject")
  /** Indicates that the node has begun an orderly shutdown sequence on a best effort basis. */
  interface ShutDown : ClusterStruct {

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      BasicInformation.ShutDownEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        else -> null
      }
    }
  }

  /** @suppress */
  class ShutDownImpl() : ShutDown {
    /** @suppress */
    companion object Adapter : StructAdapter<ShutDown> {
      val Id = ScopedEventId(BasicInformationTrait.Id, 1u)

      override fun read(reader: ClusterPayloadReader) = ShutDownImpl()
    }
  }

  /** Indicates that the node has left a given fabric. */
  interface Leave : ClusterStruct {
    /** The identifier for the fabric where the target endpoint is located. */
    val fabricIndex: UByte?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      BasicInformation.LeaveEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        BasicInformation.LeaveEvent.EventFields.fabricIndex.tag -> fabricIndex
        else -> null
      }
    }
  }

  /** @suppress */
  class LeaveImpl(override val fabricIndex: UByte? = null) : Leave {
    /** @suppress */
    companion object Adapter : StructAdapter<Leave> {
      val Id = ScopedEventId(BasicInformationTrait.Id, 2u)

      override fun write(writer: ClusterPayloadWriter, value: Leave) {
        writer.wrapPayload(id = Id)
        writer.ubyte.write(0u, value.fabricIndex)
      }

      override fun read(reader: ClusterPayloadReader): Leave {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return LeaveImpl(data.ubyte.getOptionalNullable(0u, "FabricIndex").getOrNull())
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Leave) return false
      if (fabricIndex != other.fabricIndex) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (fabricIndex?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "Leave(fabricIndex=$fabricIndex)"
    }
  }

  /** Indicates that the state of the node's reachability has changed. */
  interface ReachableChanged : ClusterStruct {
    /** The new [reachable][BasicInformationTrait.Attributes.reachable] attribute value. */
    val reachableNewValue: Boolean?

    @HomeExperimentalGenericApi
    override fun getDescriptor(): StructDescriptor =
      BasicInformation.ReachableChangedEvent.EventFields.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        BasicInformation.ReachableChangedEvent.EventFields.reachableNewValue.tag ->
          reachableNewValue
        else -> null
      }
    }
  }

  /** @suppress */
  class ReachableChangedImpl(override val reachableNewValue: Boolean? = null) : ReachableChanged {
    /** @suppress */
    companion object Adapter : StructAdapter<ReachableChanged> {
      val Id = ScopedEventId(BasicInformationTrait.Id, 3u)

      override fun write(writer: ClusterPayloadWriter, value: ReachableChanged) {
        writer.wrapPayload(id = Id)
        writer.boolean.write(0u, value.reachableNewValue)
      }

      override fun read(reader: ClusterPayloadReader): ReachableChanged {
        reader.unwrapPayload(id = Id)
        val data = reader.readPayload()
        return ReachableChangedImpl(
          data.boolean.getOptionalNullable(0u, "ReachableNewValue").getOrNull()
        )
      }
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ReachableChanged) return false
      if (reachableNewValue != other.reachableNewValue) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + (reachableNewValue?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "ReachableChanged(reachableNewValue=$reachableNewValue)"
    }
  }

  // Structs
  /**
   * A set of constant values related to the overall capabilities of the node.
   *
   * @constructor Creates the CapabilityMinimaStruct class.
   */
  class CapabilityMinimaStruct(
    /**
     * The actual minimum number of concurrent CASE (certificate-authenticated session
     * establishment) sessions that are supported per fabric.
     */
    val caseSessionsPerFabric: UShort = 0u,
    /** The actual minimum number of concurrent subscriptions supported per fabric. */
    val subscriptionsPerFabric: UShort = 0u,
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
      /** The [caseSessionsPerFabric] command request field. */
      caseSessionsPerFabric(
        "caseSessionsPerFabric",
        0u,
        "UShort",
        FieldType.UShort,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [subscriptionsPerFabric] command request field. */
      subscriptionsPerFabric(
        "subscriptionsPerFabric",
        1u,
        "UShort",
        FieldType.UShort,
        false,
        NoOpDescriptor,
        false,
      ),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.caseSessionsPerFabric.tag -> caseSessionsPerFabric
        StructFields.subscriptionsPerFabric.tag -> subscriptionsPerFabric
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<CapabilityMinimaStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: CapabilityMinimaStruct) {
        writer.ushort.write(0u, value.caseSessionsPerFabric)
        writer.ushort.write(1u, value.subscriptionsPerFabric)
      }

      override fun read(reader: ClusterPayloadReader): CapabilityMinimaStruct {
        val data = reader.readPayload()
        return CapabilityMinimaStruct(
          data.ushort.get(0u, "CaseSessionsPerFabric"),
          data.ushort.get(1u, "SubscriptionsPerFabric"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return CapabilityMinimaStruct(
          caseSessionsPerFabric = fields[StructFields.caseSessionsPerFabric] as UShort,
          subscriptionsPerFabric = fields[StructFields.subscriptionsPerFabric] as UShort,
        )
      }

      val TypedExpression<out CapabilityMinimaStruct?>.caseSessionsPerFabric:
        TypedExpression<UShort>
        get() =
          fieldSelect<CapabilityMinimaStruct, UShort>(this, StructFields.caseSessionsPerFabric)

      val TypedExpression<out CapabilityMinimaStruct?>.subscriptionsPerFabric:
        TypedExpression<UShort>
        get() =
          fieldSelect<CapabilityMinimaStruct, UShort>(this, StructFields.subscriptionsPerFabric)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is CapabilityMinimaStruct) return false
      if (caseSessionsPerFabric != other.caseSessionsPerFabric) {
        return false
      }
      if (subscriptionsPerFabric != other.subscriptionsPerFabric) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + caseSessionsPerFabric.hashCode()
      result = 31 * result + subscriptionsPerFabric.hashCode()

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "CapabilityMinimaStruct(caseSessionsPerFabric=$caseSessionsPerFabric, subscriptionsPerFabric=$subscriptionsPerFabric)"
    }
  }

  /**
   * The product's appearance.
   *
   * @constructor Creates the ProductAppearanceStruct class.
   */
  class ProductAppearanceStruct(
    /**
     * The product's appearance, as enumerated by
     * [ProductFinishEnum][BasicInformationTrait.ProductFinishEnum].
     */
    val finish: ProductFinishEnum = ProductFinishEnum.Other,
    /**
     * The product's primary color, as enumerated by [ColorEnum][BasicInformationTrait.ColorEnum].
     */
    val primaryColor: ColorEnum? = null,
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
      /** The [finish] command request field. */
      finish(
        "finish",
        0u,
        "ProductFinishEnum",
        FieldType.Enum,
        false,
        ProductFinishEnum.EnumDescriptor,
        false,
      ),
      /** The [primaryColor] command request field. */
      primaryColor(
        "primaryColor",
        1u,
        "ColorEnum",
        FieldType.Enum,
        false,
        ColorEnum.EnumDescriptor,
        true,
      ),
    }

    @HomeExperimentalGenericApi override fun getDescriptor(): StructDescriptor = Adapter

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        StructFields.finish.tag -> finish
        StructFields.primaryColor.tag -> primaryColor
        else -> null
      }
    }

    /** @suppress */
    @Immutable
    companion object Adapter : StructAdapter<ProductAppearanceStruct>, StructDescriptor {
      override fun write(writer: ClusterPayloadWriter, value: ProductAppearanceStruct) {
        writer.enum(ProductFinishEnum.Adapter).write(0u, value.finish)
        writer.enum(ColorEnum.Adapter).write(1u, value.primaryColor)
      }

      override fun read(reader: ClusterPayloadReader): ProductAppearanceStruct {
        val data = reader.readPayload()
        return ProductAppearanceStruct(
          data.enum(ProductFinishEnum.Adapter).get(0u, "Finish"),
          data.enum(ColorEnum.Adapter).getNullable(1u, "PrimaryColor"),
        )
      }

      @HomeExperimentalGenericApi
      @Suppress("Immutable")
      override val fields: DescriptorMap = StructFields.entries.toDescriptorMap()

      @HomeExperimentalGenericApi
      override fun toStruct(fields: Map<com.google.home.Field, Any?>): ClusterStruct {
        return ProductAppearanceStruct(
          finish = fields[StructFields.finish] as ProductFinishEnum,
          primaryColor = fields[StructFields.primaryColor] as ColorEnum?,
        )
      }

      val TypedExpression<out ProductAppearanceStruct?>.finish: TypedExpression<ProductFinishEnum>
        get() = fieldSelect<ProductAppearanceStruct, ProductFinishEnum>(this, StructFields.finish)

      val TypedExpression<out ProductAppearanceStruct?>.primaryColor: TypedExpression<ColorEnum?>
        get() = fieldSelect<ProductAppearanceStruct, ColorEnum?>(this, StructFields.primaryColor)
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ProductAppearanceStruct) return false
      if (finish != other.finish) {
        return false
      }
      if (primaryColor != other.primaryColor) {
        return false
      }

      return true
    }

    /** @suppress */
    override fun hashCode(): Int {
      var result = 1
      result = 31 * result + finish.hashCode()
      result = 31 * result + (primaryColor?.hashCode() ?: 0)

      return result
    }

    /** @suppress */
    override fun toString(): String {
      return "ProductAppearanceStruct(finish=$finish, primaryColor=$primaryColor)"
    }
  }

  /** Attributes for the BasicInformation cluster. */
  @Generated("GoogleHomePlatformCodegen")
  interface Attributes : ClusterStruct {

    /**
     * The revision number of the data model against which the node is certified.
     *
     * __Access type:__ Read
     */
    val dataModelRevision: UShort?

    /**
     * A human-readable (displayable) name of the vendor for the node.
     *
     * __Access type:__ Read
     */
    val vendorName: String?

    /**
     * The Vendor ID.
     *
     * __Access type:__ Read
     */
    val vendorId: UShort?

    /**
     * A human-readable (displayable) name of the model for the node such as the model number (or
     * other identifier) assigned by the vendor.
     *
     * __Access type:__ Read
     */
    val productName: String?

    /**
     * The Product ID assigned by the vendor that is unique to the specific product of the Node.
     *
     * __Access type:__ Read
     */
    val productId: UShort?

    /**
     * The user defined name for the node.
     *
     * __Access type:__ Write
     */
    val nodeLabel: String?

    /**
     * The ISO 3166-1 alpha-2 code representing the country, dependent territory, or special area of
     * geographic interest in which the node is located at the time of the attribute being set.
     *
     * __Access type:__ Write
     */
    val location: String?

    /**
     * The version number of the hardware of the node.
     *
     * __Access type:__ Read
     */
    val hardwareVersion: UShort?

    /**
     * The human-readable version number of the hardware of the node.
     *
     * __Access type:__ Read
     */
    val hardwareVersionString: String?

    /**
     * The current version number for the software running on the node.
     *
     * __Access type:__ Read
     */
    val softwareVersion: UInt?

    /**
     * The current human-readable representation for the software running on the node.
     *
     * __Access type:__ Read
     */
    val softwareVersionString: String?

    /**
     * The date that the node was manufactured.
     *
     * __Access type:__ Read
     */
    val manufacturingDate: String?

    /**
     * The human-readable (displayable) vendor assigned part number for the node whose meaning and
     * numbering scheme is vendor defined.
     *
     * __Access type:__ Read
     */
    val partNumber: String?

    /**
     * The link to a product specific web page.
     *
     * __Access type:__ Read
     */
    val productUrl: String?

    /**
     * The vendor specific human-readable (displayable) product label.
     *
     * __Access type:__ Read
     */
    val productLabel: String?

    /**
     * The human-readable (displayable) serial number.
     *
     * __Access type:__ Read
     */
    val serialNumber: String?

    /**
     * If set to `true`, allow a local node configuration to be disabled.
     *
     * __Access type:__ Write
     */
    val localConfigDisabled: Boolean?

    /**
     * Indicates whether the node can be reached.
     *
     * __Access type:__ Read
     */
    val reachable: Boolean?

    /**
     * The unique identifier for the device, which is constructed in a manufacturer specific manner.
     *
     * __Access type:__ Read
     */
    val uniqueId: String?

    /**
     * The minimum guaranteed value for some system-wide resource capabilities that are not
     * otherwise cluster-specific and do not appear elsewhere.
     *
     * __Access type:__ Read
     */
    val capabilityMinima: CapabilityMinimaStruct?

    /**
     * The information about the appearance of the product.
     *
     * __Access type:__ Read
     */
    val productAppearance: ProductAppearanceStruct?

    /**
     * The specification version.
     *
     * __Access type:__ Read
     */
    val specificationVersion: UInt?

    /**
     * The maximum paths per invoke.
     *
     * __Access type:__ Read
     */
    val maxPathsPerInvoke: UShort?
    val configurationVersion: UInt?

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
    override fun getDescriptor(): StructDescriptor = BasicInformation.Attribute.StructDescriptor

    @HomeExperimentalGenericApi
    override fun getFieldValueById(tagId: TagId): Any? {
      return when (tagId) {
        BasicInformation.Attribute.dataModelRevision.tag -> dataModelRevision
        BasicInformation.Attribute.vendorName.tag -> vendorName
        BasicInformation.Attribute.vendorId.tag -> vendorId
        BasicInformation.Attribute.productName.tag -> productName
        BasicInformation.Attribute.productId.tag -> productId
        BasicInformation.Attribute.nodeLabel.tag -> nodeLabel
        BasicInformation.Attribute.location.tag -> location
        BasicInformation.Attribute.hardwareVersion.tag -> hardwareVersion
        BasicInformation.Attribute.hardwareVersionString.tag -> hardwareVersionString
        BasicInformation.Attribute.softwareVersion.tag -> softwareVersion
        BasicInformation.Attribute.softwareVersionString.tag -> softwareVersionString
        BasicInformation.Attribute.manufacturingDate.tag -> manufacturingDate
        BasicInformation.Attribute.partNumber.tag -> partNumber
        BasicInformation.Attribute.productUrl.tag -> productUrl
        BasicInformation.Attribute.productLabel.tag -> productLabel
        BasicInformation.Attribute.serialNumber.tag -> serialNumber
        BasicInformation.Attribute.localConfigDisabled.tag -> localConfigDisabled
        BasicInformation.Attribute.reachable.tag -> reachable
        BasicInformation.Attribute.uniqueId.tag -> uniqueId
        BasicInformation.Attribute.capabilityMinima.tag -> capabilityMinima
        BasicInformation.Attribute.productAppearance.tag -> productAppearance
        BasicInformation.Attribute.specificationVersion.tag -> specificationVersion
        BasicInformation.Attribute.maxPathsPerInvoke.tag -> maxPathsPerInvoke
        BasicInformation.Attribute.configurationVersion.tag -> configurationVersion
        BasicInformation.Attribute.generatedCommandList.tag -> generatedCommandList
        BasicInformation.Attribute.acceptedCommandList.tag -> acceptedCommandList
        BasicInformation.Attribute.attributeList.tag -> attributeList
        BasicInformation.Attribute.featureMap.tag -> featureMap
        BasicInformation.Attribute.clusterRevision.tag -> clusterRevision
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
          writer.ushort.write(0u, value.dataModelRevision)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(1u)) {
          writer.string.write(1u, value.vendorName)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(2u)) {
          writer.ushort.write(2u, value.vendorId)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(3u)) {
          writer.string.write(3u, value.productName)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(4u)) {
          writer.ushort.write(4u, value.productId)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
          writer.string.write(5u, value.nodeLabel)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(6u)) {
          writer.string.write(6u, value.location)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(7u)) {
          writer.ushort.write(7u, value.hardwareVersion)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(8u)) {
          writer.string.write(8u, value.hardwareVersionString)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(9u)) {
          writer.uint.write(9u, value.softwareVersion)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(10u)) {
          writer.string.write(10u, value.softwareVersionString)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(11u)) {
          writer.string.write(11u, value.manufacturingDate)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(12u)) {
          writer.string.write(12u, value.partNumber)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(13u)) {
          writer.string.write(13u, value.productUrl)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(14u)) {
          writer.string.write(14u, value.productLabel)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(15u)) {
          writer.string.write(15u, value.serialNumber)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(16u)) {
          writer.boolean.write(16u, value.localConfigDisabled)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(17u)) {
          writer.boolean.write(17u, value.reachable)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(18u)) {
          writer.string.write(18u, value.uniqueId)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(19u)) {
          writer.struct(CapabilityMinimaStruct.Adapter).write(19u, value.capabilityMinima)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(20u)) {
          writer.struct(ProductAppearanceStruct.Adapter).write(20u, value.productAppearance)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(21u)) {
          writer.uint.write(21u, value.specificationVersion)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(22u)) {
          writer.ushort.write(22u, value.maxPathsPerInvoke)
        }
        if (!writer.strictOperationValidation || value.attributeList.contains(24u)) {
          writer.uint.write(24u, value.configurationVersion)
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
          reader.readPayload(
            mapOf(19u to CapabilityMinimaStruct.Adapter, 20u to ProductAppearanceStruct.Adapter)
          )
        val attributeList = mutableListOf<UInt>()
        return AttributesImpl(
          data.ushort
            .getOptionalNullable(0u, "DataModelRevision")
            .also { if (it.isPresent && it.value != null) attributeList.add(0u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(1u, "VendorName")
            .also { if (it.isPresent && it.value != null) attributeList.add(1u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(2u, "VendorId")
            .also { if (it.isPresent && it.value != null) attributeList.add(2u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(3u, "ProductName")
            .also { if (it.isPresent && it.value != null) attributeList.add(3u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(4u, "ProductId")
            .also { if (it.isPresent && it.value != null) attributeList.add(4u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(5u, "NodeLabel")
            .also { if (it.isPresent && it.value != null) attributeList.add(5u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(6u, "Location")
            .also { if (it.isPresent && it.value != null) attributeList.add(6u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(7u, "HardwareVersion")
            .also { if (it.isPresent && it.value != null) attributeList.add(7u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(8u, "HardwareVersionString")
            .also { if (it.isPresent && it.value != null) attributeList.add(8u) }
            .getOrNull(),
          data.uint
            .getOptionalNullable(9u, "SoftwareVersion")
            .also { if (it.isPresent && it.value != null) attributeList.add(9u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(10u, "SoftwareVersionString")
            .also { if (it.isPresent && it.value != null) attributeList.add(10u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(11u, "ManufacturingDate")
            .also { if (it.isPresent && it.value != null) attributeList.add(11u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(12u, "PartNumber")
            .also { if (it.isPresent && it.value != null) attributeList.add(12u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(13u, "ProductUrl")
            .also { if (it.isPresent && it.value != null) attributeList.add(13u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(14u, "ProductLabel")
            .also { if (it.isPresent && it.value != null) attributeList.add(14u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(15u, "SerialNumber")
            .also { if (it.isPresent && it.value != null) attributeList.add(15u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(16u, "LocalConfigDisabled")
            .also { if (it.isPresent && it.value != null) attributeList.add(16u) }
            .getOrNull(),
          data.boolean
            .getOptionalNullable(17u, "Reachable")
            .also { if (it.isPresent && it.value != null) attributeList.add(17u) }
            .getOrNull(),
          data.string
            .getOptionalNullable(18u, "UniqueId")
            .also { if (it.isPresent && it.value != null) attributeList.add(18u) }
            .getOrNull(),
          data
            .struct { CapabilityMinimaStruct() }
            .getOptionalNullable(19u, "CapabilityMinima")
            .also { if (it.isPresent && it.value != null) attributeList.add(19u) }
            .getOrNull(),
          data
            .struct { ProductAppearanceStruct() }
            .getOptionalNullable(20u, "ProductAppearance")
            .also { if (it.isPresent && it.value != null) attributeList.add(20u) }
            .getOrNull(),
          data.uint
            .getOptionalNullable(21u, "SpecificationVersion")
            .also { if (it.isPresent && it.value != null) attributeList.add(21u) }
            .getOrNull(),
          data.ushort
            .getOptionalNullable(22u, "MaxPathsPerInvoke")
            .also { if (it.isPresent && it.value != null) attributeList.add(22u) }
            .getOrNull(),
          data.uint
            .getOptionalNullable(24u, "ConfigurationVersion")
            .also { if (it.isPresent && it.value != null) attributeList.add(24u) }
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
    override val dataModelRevision: UShort? = null,
    override val vendorName: String? = null,
    override val vendorId: UShort? = null,
    override val productName: String? = null,
    override val productId: UShort? = null,
    override val nodeLabel: String? = null,
    override val location: String? = null,
    override val hardwareVersion: UShort? = null,
    override val hardwareVersionString: String? = null,
    override val softwareVersion: UInt? = null,
    override val softwareVersionString: String? = null,
    override val manufacturingDate: String? = null,
    override val partNumber: String? = null,
    override val productUrl: String? = null,
    override val productLabel: String? = null,
    override val serialNumber: String? = null,
    override val localConfigDisabled: Boolean? = null,
    override val reachable: Boolean? = null,
    override val uniqueId: String? = null,
    override val capabilityMinima: CapabilityMinimaStruct? = null,
    override val productAppearance: ProductAppearanceStruct? = null,
    override val specificationVersion: UInt? = null,
    override val maxPathsPerInvoke: UShort? = null,
    override val configurationVersion: UInt? = null,
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
        13u,
        14u,
        15u,
        16u,
        17u,
        18u,
        19u,
        20u,
        21u,
        22u,
        24u,
        65528u,
        65529u,
        65531u,
        65532u,
        65533u,
      ),
    override val featureMap: UInt = 0u,
    override val clusterRevision: UShort = 0u,
  ) : Attributes, CanMutate<Attributes, MutableAttributes> {

    constructor(
      other: Attributes
    ) : this(
      dataModelRevision = other.dataModelRevision,
      vendorName = other.vendorName,
      vendorId = other.vendorId,
      productName = other.productName,
      productId = other.productId,
      nodeLabel = other.nodeLabel,
      location = other.location,
      hardwareVersion = other.hardwareVersion,
      hardwareVersionString = other.hardwareVersionString,
      softwareVersion = other.softwareVersion,
      softwareVersionString = other.softwareVersionString,
      manufacturingDate = other.manufacturingDate,
      partNumber = other.partNumber,
      productUrl = other.productUrl,
      productLabel = other.productLabel,
      serialNumber = other.serialNumber,
      localConfigDisabled = other.localConfigDisabled,
      reachable = other.reachable,
      uniqueId = other.uniqueId,
      capabilityMinima = other.capabilityMinima,
      productAppearance = other.productAppearance,
      specificationVersion = other.specificationVersion,
      maxPathsPerInvoke = other.maxPathsPerInvoke,
      configurationVersion = other.configurationVersion,
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
      if (dataModelRevision != other.dataModelRevision) {
        return false
      }
      if (vendorName != other.vendorName) {
        return false
      }
      if (vendorId != other.vendorId) {
        return false
      }
      if (productName != other.productName) {
        return false
      }
      if (productId != other.productId) {
        return false
      }
      if (nodeLabel != other.nodeLabel) {
        return false
      }
      if (location != other.location) {
        return false
      }
      if (hardwareVersion != other.hardwareVersion) {
        return false
      }
      if (hardwareVersionString != other.hardwareVersionString) {
        return false
      }
      if (softwareVersion != other.softwareVersion) {
        return false
      }
      if (softwareVersionString != other.softwareVersionString) {
        return false
      }
      if (manufacturingDate != other.manufacturingDate) {
        return false
      }
      if (partNumber != other.partNumber) {
        return false
      }
      if (productUrl != other.productUrl) {
        return false
      }
      if (productLabel != other.productLabel) {
        return false
      }
      if (serialNumber != other.serialNumber) {
        return false
      }
      if (localConfigDisabled != other.localConfigDisabled) {
        return false
      }
      if (reachable != other.reachable) {
        return false
      }
      if (uniqueId != other.uniqueId) {
        return false
      }
      if (capabilityMinima != other.capabilityMinima) {
        return false
      }
      if (productAppearance != other.productAppearance) {
        return false
      }
      if (specificationVersion != other.specificationVersion) {
        return false
      }
      if (maxPathsPerInvoke != other.maxPathsPerInvoke) {
        return false
      }
      if (configurationVersion != other.configurationVersion) {
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
      result = 31 * result + (dataModelRevision?.hashCode() ?: 0)
      result = 31 * result + (vendorName?.hashCode() ?: 0)
      result = 31 * result + (vendorId?.hashCode() ?: 0)
      result = 31 * result + (productName?.hashCode() ?: 0)
      result = 31 * result + (productId?.hashCode() ?: 0)
      result = 31 * result + (nodeLabel?.hashCode() ?: 0)
      result = 31 * result + (location?.hashCode() ?: 0)
      result = 31 * result + (hardwareVersion?.hashCode() ?: 0)
      result = 31 * result + (hardwareVersionString?.hashCode() ?: 0)
      result = 31 * result + (softwareVersion?.hashCode() ?: 0)
      result = 31 * result + (softwareVersionString?.hashCode() ?: 0)
      result = 31 * result + (manufacturingDate?.hashCode() ?: 0)
      result = 31 * result + (partNumber?.hashCode() ?: 0)
      result = 31 * result + (productUrl?.hashCode() ?: 0)
      result = 31 * result + (productLabel?.hashCode() ?: 0)
      result = 31 * result + (serialNumber?.hashCode() ?: 0)
      result = 31 * result + (localConfigDisabled?.hashCode() ?: 0)
      result = 31 * result + (reachable?.hashCode() ?: 0)
      result = 31 * result + (uniqueId?.hashCode() ?: 0)
      result = 31 * result + (capabilityMinima?.hashCode() ?: 0)
      result = 31 * result + (productAppearance?.hashCode() ?: 0)
      result = 31 * result + (specificationVersion?.hashCode() ?: 0)
      result = 31 * result + (maxPathsPerInvoke?.hashCode() ?: 0)
      result = 31 * result + (configurationVersion?.hashCode() ?: 0)
      result = 31 * result + generatedCommandList.hashCode()
      result = 31 * result + acceptedCommandList.hashCode()
      result = 31 * result + attributeList.hashCode()
      result = 31 * result + featureMap.hashCode()
      result = 31 * result + clusterRevision.hashCode()

      return result
    }

    override fun toString(): String {
      return "BasicInformation(dataModelRevision=$dataModelRevision, vendorName=$vendorName, vendorId=$vendorId, productName=$productName, productId=$productId, nodeLabel=$nodeLabel, location=$location, hardwareVersion=$hardwareVersion, hardwareVersionString=$hardwareVersionString, softwareVersion=$softwareVersion, softwareVersionString=$softwareVersionString, manufacturingDate=$manufacturingDate, partNumber=$partNumber, productUrl=$productUrl, productLabel=$productLabel, serialNumber=$serialNumber, localConfigDisabled=$localConfigDisabled, reachable=$reachable, uniqueId=$uniqueId, capabilityMinima=$capabilityMinima, productAppearance=$productAppearance, specificationVersion=$specificationVersion, maxPathsPerInvoke=$maxPathsPerInvoke, configurationVersion=$configurationVersion, generatedCommandList=$generatedCommandList, acceptedCommandList=$acceptedCommandList, attributeList=$attributeList, featureMap=$featureMap, clusterRevision=$clusterRevision)"
    }

    fun copy(
      dataModelRevision: UShort? = this.dataModelRevision,
      vendorName: String? = this.vendorName,
      vendorId: UShort? = this.vendorId,
      productName: String? = this.productName,
      productId: UShort? = this.productId,
      nodeLabel: String? = this.nodeLabel,
      location: String? = this.location,
      hardwareVersion: UShort? = this.hardwareVersion,
      hardwareVersionString: String? = this.hardwareVersionString,
      softwareVersion: UInt? = this.softwareVersion,
      softwareVersionString: String? = this.softwareVersionString,
      manufacturingDate: String? = this.manufacturingDate,
      partNumber: String? = this.partNumber,
      productUrl: String? = this.productUrl,
      productLabel: String? = this.productLabel,
      serialNumber: String? = this.serialNumber,
      localConfigDisabled: Boolean? = this.localConfigDisabled,
      reachable: Boolean? = this.reachable,
      uniqueId: String? = this.uniqueId,
      capabilityMinima: CapabilityMinimaStruct? = this.capabilityMinima,
      productAppearance: ProductAppearanceStruct? = this.productAppearance,
      specificationVersion: UInt? = this.specificationVersion,
      maxPathsPerInvoke: UShort? = this.maxPathsPerInvoke,
      configurationVersion: UInt? = this.configurationVersion,
      generatedCommandList: List<UInt> = this.generatedCommandList,
      acceptedCommandList: List<UInt> = this.acceptedCommandList,
      attributeList: List<UInt> = this.attributeList,
      featureMap: UInt = this.featureMap,
      clusterRevision: UShort = this.clusterRevision,
    ) =
      AttributesImpl(
        dataModelRevision = dataModelRevision,
        vendorName = vendorName,
        vendorId = vendorId,
        productName = productName,
        productId = productId,
        nodeLabel = nodeLabel,
        location = location,
        hardwareVersion = hardwareVersion,
        hardwareVersionString = hardwareVersionString,
        softwareVersion = softwareVersion,
        softwareVersionString = softwareVersionString,
        manufacturingDate = manufacturingDate,
        partNumber = partNumber,
        productUrl = productUrl,
        productLabel = productLabel,
        serialNumber = serialNumber,
        localConfigDisabled = localConfigDisabled,
        reachable = reachable,
        uniqueId = uniqueId,
        capabilityMinima = capabilityMinima,
        productAppearance = productAppearance,
        specificationVersion = specificationVersion,
        maxPathsPerInvoke = maxPathsPerInvoke,
        configurationVersion = configurationVersion,
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
      dataModelRevision = attributes.dataModelRevision,
      vendorName = attributes.vendorName,
      vendorId = attributes.vendorId,
      productName = attributes.productName,
      productId = attributes.productId,
      nodeLabel = attributes.nodeLabel,
      location = attributes.location,
      hardwareVersion = attributes.hardwareVersion,
      hardwareVersionString = attributes.hardwareVersionString,
      softwareVersion = attributes.softwareVersion,
      softwareVersionString = attributes.softwareVersionString,
      manufacturingDate = attributes.manufacturingDate,
      partNumber = attributes.partNumber,
      productUrl = attributes.productUrl,
      productLabel = attributes.productLabel,
      serialNumber = attributes.serialNumber,
      localConfigDisabled = attributes.localConfigDisabled,
      reachable = attributes.reachable,
      uniqueId = attributes.uniqueId,
      capabilityMinima = attributes.capabilityMinima,
      productAppearance = attributes.productAppearance,
      specificationVersion = attributes.specificationVersion,
      maxPathsPerInvoke = attributes.maxPathsPerInvoke,
      configurationVersion = attributes.configurationVersion,
      generatedCommandList = attributes.generatedCommandList,
      acceptedCommandList = attributes.acceptedCommandList,
      attributeList = attributes.attributeList,
      featureMap = attributes.featureMap,
      clusterRevision = attributes.clusterRevision,
    ) {
    internal var _nodeLabel: String? = null
    override val nodeLabel: String?
      get() {
        return _nodeLabel ?: super.nodeLabel
      }

    fun setNodeLabel(value: String) {
      _nodeLabel = value
    }

    internal var _location: String? = null
    override val location: String?
      get() {
        return _location ?: super.location
      }

    fun setLocation(value: String) {
      _location = value
    }

    internal var _localConfigDisabled: Boolean? = null
    override val localConfigDisabled: Boolean?
      get() {
        return _localConfigDisabled ?: super.localConfigDisabled
      }

    fun setLocalConfigDisabled(value: Boolean) {
      _localConfigDisabled = value
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is MutableAttributes) return false
      return super.equals(other)
    }

    override fun toString(): String {
      return "BasicInformation.MutableAttributes(${super.toString()})"
    }

    companion object Adapter : StructAdapter<MutableAttributes> {
      override fun write(writer: ClusterPayloadWriter, value: MutableAttributes) {
        writer.wrapPayload(id = Id)
        if (value._nodeLabel != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(5u)) {
            writer.string.write(5u, value._nodeLabel)
          } else {
            throw HomeException.invalidArgument("nodeLabel")
          }
        }
        if (value._location != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(6u)) {
            writer.string.write(6u, value._location)
          } else {
            throw HomeException.invalidArgument("location")
          }
        }
        if (value._localConfigDisabled != null) {
          if (!writer.strictOperationValidation || value.attributeList.contains(16u)) {
            writer.boolean.write(16u, value._localConfigDisabled)
          } else {
            throw HomeException.invalidArgument("localConfigDisabled")
          }
        }
      }

      override fun read(reader: ClusterPayloadReader): MutableAttributes =
        MutableAttributes(Attributes.Adapter.read(reader))
    }
  }

  // Commands

  /**
   * Send a manufacturer specific ping.
   *
   * @see BasicInformationCommands.mfgSpecificPing Use this command from the
   *   `trait(BasicInformation)` method.
   */
  object MfgSpecificPingCommand : CommandDescriptor {
    /** @suppress */
    override val requestId = ScopedCommandId(BasicInformationTrait.Id, 0u)
    override val commandId = requestId.toString()
    override val commandName = "MfgSpecificPingCommand"

    override val fields: DescriptorMap =
      Request.CommandFields.values().associateBy({ it.tag }, { it })

    /**
     * The request payload for the MfgSpecificPing command.
     *
     * @constructor Creates a request payload for the MfgSpecificPing command.
     */
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
        return "MfgSpecificPingCommand.Request()"
      }
    }
  }
}
