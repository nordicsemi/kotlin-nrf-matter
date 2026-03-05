// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

import com.google.home.BatchableCommand
import com.google.home.ClusterStruct
import com.google.home.Descriptor as HomeDescriptor
import com.google.home.DescriptorMap
import com.google.home.Event
import com.google.home.EventFactory
import com.google.home.EventImportance
import com.google.home.Field
import com.google.home.Id
import com.google.home.NoOpDescriptor
import com.google.home.StructDescriptor
import com.google.home.Trait
import com.google.home.TraitFactory
import com.google.home.Type as FieldType
import com.google.home.Updatable
import com.google.home.annotation.HomeExperimentalApi
import com.google.home.automation.Attribute as AutomationAttribute
import com.google.home.automation.AttributeToUpdate
import com.google.home.automation.Command as AutomationCommand
import com.google.home.automation.EventField
import com.google.home.automation.TypedExpression
import com.google.home.automation.Updater
import com.google.home.automation.fieldSelect
import com.google.home.matter.MatterEventFactory
import com.google.home.matter.MatterTrait
import com.google.home.matter.MatterTraitClient
import com.google.home.matter.MatterTraitFactory
import com.google.home.matter.MatterTraitImpl
import com.google.home.matter.serialization.BitmapAdapter
import com.google.home.matter.serialization.EnumAdapter
import com.google.home.toDescriptorMap
import java.time.Instant
import javax.annotation.processing.Generated
import no.nordicsemi.nrf.matter.BasicInformationTrait.Attributes
import no.nordicsemi.nrf.matter.BasicInformationTrait.AttributesImpl
import no.nordicsemi.nrf.matter.BasicInformationTrait.CapabilityMinimaStruct
import no.nordicsemi.nrf.matter.BasicInformationTrait.ColorEnum
import no.nordicsemi.nrf.matter.BasicInformationTrait.Leave
import no.nordicsemi.nrf.matter.BasicInformationTrait.MfgSpecificPingCommand
import no.nordicsemi.nrf.matter.BasicInformationTrait.MutableAttributes
import no.nordicsemi.nrf.matter.BasicInformationTrait.ProductAppearanceStruct
import no.nordicsemi.nrf.matter.BasicInformationTrait.ProductFinishEnum
import no.nordicsemi.nrf.matter.BasicInformationTrait.ReachableChanged
import no.nordicsemi.nrf.matter.BasicInformationTrait.ShutDown
import no.nordicsemi.nrf.matter.BasicInformationTrait.StartUp

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Commands for the BasicInformation trait. */
@Generated("GoogleHomePlatformCodegen")
interface BasicInformationCommands {

  /** Send a manufacturer specific ping. */
  suspend fun mfgSpecificPing()

  /**
   * The batchable version of [mfgSpecificPing] command.
   *
   * Send a manufacturer specific ping.
   *
   * @return BatchableCommand<Unit>
   */
  fun mfgSpecificPingBatchable(): BatchableCommand<Unit>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface BasicInformationCommandsDefaultImpl : BasicInformationCommands {
  override suspend fun mfgSpecificPing() {
    TODO("Not Implemented")
  }

  override fun mfgSpecificPingBatchable(): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }
}

/**
 * API for the BasicInformation trait. This trait provides attributes and events for determining
 * basic information about nodes.
 */
@Generated("GoogleHomePlatformCodegen")
interface BasicInformation :
  Attributes,
  MatterTrait,
  Updatable<BasicInformation, MutableAttributes>,
  BasicInformationCommands {
  /** Descriptor enum for this trait's attributes. */
  enum class Attribute(
    override val fieldName: String,
    override val tag: UInt,
    override val typeName: String,
    override val typeEnum: FieldType,
    override val isList: Boolean,
    override val descriptor: HomeDescriptor,
    val isNullable: Boolean,
  ) : Field {
    /**
     * The [dataModelRevision][BasicInformationTrait.Attributes.dataModelRevision] trait attribute.
     */
    dataModelRevision(
      "dataModelRevision",
      0u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /** The [vendorName][BasicInformationTrait.Attributes.vendorName] trait attribute. */
    vendorName("vendorName", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
    /** The [vendorId][BasicInformationTrait.Attributes.vendorId] trait attribute. */
    vendorId("vendorId", 2u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
    /** The [productName][BasicInformationTrait.Attributes.productName] trait attribute. */
    productName("productName", 3u, "String", FieldType.String, false, NoOpDescriptor, false),
    /** The [productId][BasicInformationTrait.Attributes.productId] trait attribute. */
    productId("productId", 4u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
    /** The [nodeLabel][BasicInformationTrait.Attributes.nodeLabel] trait attribute. */
    nodeLabel("nodeLabel", 5u, "String", FieldType.String, false, NoOpDescriptor, false),
    /** The [location][BasicInformationTrait.Attributes.location] trait attribute. */
    location("location", 6u, "String", FieldType.String, false, NoOpDescriptor, false),
    /** The [hardwareVersion][BasicInformationTrait.Attributes.hardwareVersion] trait attribute. */
    hardwareVersion(
      "hardwareVersion",
      7u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [hardwareVersionString][BasicInformationTrait.Attributes.hardwareVersionString] trait
     * attribute.
     */
    hardwareVersionString(
      "hardwareVersionString",
      8u,
      "String",
      FieldType.String,
      false,
      NoOpDescriptor,
      false,
    ),
    /** The [softwareVersion][BasicInformationTrait.Attributes.softwareVersion] trait attribute. */
    softwareVersion("softwareVersion", 9u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /**
     * The [softwareVersionString][BasicInformationTrait.Attributes.softwareVersionString] trait
     * attribute.
     */
    softwareVersionString(
      "softwareVersionString",
      10u,
      "String",
      FieldType.String,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [manufacturingDate][BasicInformationTrait.Attributes.manufacturingDate] trait attribute.
     */
    manufacturingDate(
      "manufacturingDate",
      11u,
      "String",
      FieldType.String,
      false,
      NoOpDescriptor,
      false,
    ),
    /** The [partNumber][BasicInformationTrait.Attributes.partNumber] trait attribute. */
    partNumber("partNumber", 12u, "String", FieldType.String, false, NoOpDescriptor, false),
    /** The [productUrl][BasicInformationTrait.Attributes.productUrl] trait attribute. */
    productUrl("productUrl", 13u, "String", FieldType.String, false, NoOpDescriptor, false),
    /** The [productLabel][BasicInformationTrait.Attributes.productLabel] trait attribute. */
    productLabel("productLabel", 14u, "String", FieldType.String, false, NoOpDescriptor, false),
    /** The [serialNumber][BasicInformationTrait.Attributes.serialNumber] trait attribute. */
    serialNumber("serialNumber", 15u, "String", FieldType.String, false, NoOpDescriptor, false),
    /**
     * The [localConfigDisabled][BasicInformationTrait.Attributes.localConfigDisabled] trait
     * attribute.
     */
    localConfigDisabled(
      "localConfigDisabled",
      16u,
      "Boolean",
      FieldType.Boolean,
      false,
      NoOpDescriptor,
      false,
    ),
    /** The [reachable][BasicInformationTrait.Attributes.reachable] trait attribute. */
    reachable("reachable", 17u, "Boolean", FieldType.Boolean, false, NoOpDescriptor, false),
    /** The [uniqueId][BasicInformationTrait.Attributes.uniqueId] trait attribute. */
    uniqueId("uniqueId", 18u, "String", FieldType.String, false, NoOpDescriptor, false),
    /**
     * The [capabilityMinima][BasicInformationTrait.Attributes.capabilityMinima] trait attribute.
     */
    capabilityMinima(
      "capabilityMinima",
      19u,
      "CapabilityMinimaStruct",
      FieldType.Struct,
      false,
      CapabilityMinimaStruct.Adapter,
      false,
    ),
    /**
     * The [productAppearance][BasicInformationTrait.Attributes.productAppearance] trait attribute.
     */
    productAppearance(
      "productAppearance",
      20u,
      "ProductAppearanceStruct",
      FieldType.Struct,
      false,
      ProductAppearanceStruct.Adapter,
      false,
    ),
    /**
     * The [specificationVersion][BasicInformationTrait.Attributes.specificationVersion] trait
     * attribute.
     */
    specificationVersion(
      "specificationVersion",
      21u,
      "UInt",
      FieldType.UInt,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [maxPathsPerInvoke][BasicInformationTrait.Attributes.maxPathsPerInvoke] trait attribute.
     */
    maxPathsPerInvoke(
      "maxPathsPerInvoke",
      22u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [configurationVersion][BasicInformationTrait.Attributes.configurationVersion] trait
     * attribute.
     */
    configurationVersion(
      "configurationVersion",
      24u,
      "UInt",
      FieldType.UInt,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [generatedCommandList][BasicInformationTrait.Attributes.generatedCommandList] trait
     * attribute.
     */
    generatedCommandList(
      "generatedCommandList",
      65528u,
      "UInt",
      FieldType.UInt,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [acceptedCommandList][BasicInformationTrait.Attributes.acceptedCommandList] trait
     * attribute.
     */
    acceptedCommandList(
      "acceptedCommandList",
      65529u,
      "UInt",
      FieldType.UInt,
      false,
      NoOpDescriptor,
      false,
    ),
    /** The [attributeList][BasicInformationTrait.Attributes.attributeList] trait attribute. */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][BasicInformationTrait.Attributes.featureMap] trait attribute. */
    featureMap("featureMap", 65532u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [clusterRevision][BasicInformationTrait.Attributes.clusterRevision] trait attribute. */
    clusterRevision(
      "clusterRevision",
      65533u,
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

          @HomeExperimentalApi
          override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
            return AttributesImpl(
              dataModelRevision = fields[dataModelRevision] as UShort?,
              vendorName = fields[vendorName] as String?,
              vendorId = fields[vendorId] as UShort?,
              productName = fields[productName] as String?,
              productId = fields[productId] as UShort?,
              nodeLabel = fields[nodeLabel] as String?,
              location = fields[location] as String?,
              hardwareVersion = fields[hardwareVersion] as UShort?,
              hardwareVersionString = fields[hardwareVersionString] as String?,
              softwareVersion = fields[softwareVersion] as UInt?,
              softwareVersionString = fields[softwareVersionString] as String?,
              manufacturingDate = fields[manufacturingDate] as String?,
              partNumber = fields[partNumber] as String?,
              productUrl = fields[productUrl] as String?,
              productLabel = fields[productLabel] as String?,
              serialNumber = fields[serialNumber] as String?,
              localConfigDisabled = fields[localConfigDisabled] as Boolean?,
              reachable = fields[reachable] as Boolean?,
              uniqueId = fields[uniqueId] as String?,
              capabilityMinima = fields[capabilityMinima] as CapabilityMinimaStruct?,
              productAppearance = fields[productAppearance] as ProductAppearanceStruct?,
              specificationVersion = fields[specificationVersion] as UInt?,
              maxPathsPerInvoke = fields[maxPathsPerInvoke] as UShort?,
              configurationVersion = fields[configurationVersion] as UInt?,
              generatedCommandList = fields[generatedCommandList] as List<UInt>,
              acceptedCommandList = fields[acceptedCommandList] as List<UInt>,
              attributeList = fields[attributeList] as List<UInt>,
              featureMap = fields[featureMap] as UInt,
              clusterRevision = fields[clusterRevision] as UShort,
            )
          }
        }
    }
  }

  fun supports(attribute: Attribute): Boolean

  /** Descriptor enum for this trait's commands. */
  enum class Command(val tag: UInt) {
    /** The [mfgSpecificPing][BasicInformationCommands.mfgSpecificPing] trait command. */
    MfgSpecificPing(0u)
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<BasicInformation>(
      MatterTraitFactory(
        clusterId = BasicInformationTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "ColorEnum" to BasicInformationTrait.ColorEnum.Adapter,
            "ProductFinishEnum" to BasicInformationTrait.ProductFinishEnum.Adapter,
          ),
        bitmapAdapters = mapOf<String, BitmapAdapter<*>>(),
        creator = ::BasicInformationImpl,
        supportedEvents =
          mapOf(
            BasicInformationTrait.StartUpImpl.Id.toString() to BasicInformation.StartUpEvent,
            BasicInformationTrait.ShutDownImpl.Id.toString() to BasicInformation.ShutDownEvent,
            BasicInformationTrait.LeaveImpl.Id.toString() to BasicInformation.LeaveEvent,
            BasicInformationTrait.ReachableChangedImpl.Id.toString() to
              BasicInformation.ReachableChangedEvent,
          ),
        // All Trait Commands
        commands =
          mapOf(
            BasicInformationTrait.MfgSpecificPingCommand.requestId.toString() to
              MfgSpecificPingCommand
          ),
      )
    ) {
    val dataModelRevision: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.dataModelRevision.tag,
        )

    val vendorName: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.vendorName.tag,
        )

    val vendorId: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.vendorId.tag,
        )

    val productName: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.productName.tag,
        )

    val productId: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.productId.tag,
        )

    val nodeLabel: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.nodeLabel.tag,
        )

    val location: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.location.tag,
        )

    val hardwareVersion: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.hardwareVersion.tag,
        )

    val hardwareVersionString: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.hardwareVersionString.tag,
        )

    val softwareVersion: AutomationAttribute<UInt?>
      get() =
        AutomationAttribute<UInt?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.softwareVersion.tag,
        )

    val softwareVersionString: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.softwareVersionString.tag,
        )

    val manufacturingDate: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.manufacturingDate.tag,
        )

    val partNumber: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.partNumber.tag,
        )

    val productUrl: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.productUrl.tag,
        )

    val productLabel: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.productLabel.tag,
        )

    val serialNumber: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.serialNumber.tag,
        )

    val localConfigDisabled: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.localConfigDisabled.tag,
        )

    val reachable: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.reachable.tag,
        )

    val uniqueId: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.uniqueId.tag,
        )

    val capabilityMinima: AutomationAttribute<CapabilityMinimaStruct?>
      get() =
        AutomationAttribute<CapabilityMinimaStruct?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.capabilityMinima.tag,
        )

    val productAppearance: AutomationAttribute<ProductAppearanceStruct?>
      get() =
        AutomationAttribute<ProductAppearanceStruct?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.productAppearance.tag,
        )

    val specificationVersion: AutomationAttribute<UInt?>
      get() =
        AutomationAttribute<UInt?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.specificationVersion.tag,
        )

    val maxPathsPerInvoke: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.maxPathsPerInvoke.tag,
        )

    val configurationVersion: AutomationAttribute<UInt?>
      get() =
        AutomationAttribute<UInt?>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.configurationVersion.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<UInt>
      get() =
        AutomationAttribute<UInt>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          BasicInformationTrait.Id.traitId,
          BasicInformation.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out BasicInformation?>.dataModelRevision: TypedExpression<UShort?>
      get() =
        fieldSelect<BasicInformation, UShort?>(this, BasicInformation.Attribute.dataModelRevision)

    val TypedExpression<out BasicInformation?>.vendorName: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.vendorName)

    val TypedExpression<out BasicInformation?>.vendorId: TypedExpression<UShort?>
      get() = fieldSelect<BasicInformation, UShort?>(this, BasicInformation.Attribute.vendorId)

    val TypedExpression<out BasicInformation?>.productName: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.productName)

    val TypedExpression<out BasicInformation?>.productId: TypedExpression<UShort?>
      get() = fieldSelect<BasicInformation, UShort?>(this, BasicInformation.Attribute.productId)

    val TypedExpression<out BasicInformation?>.nodeLabel: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.nodeLabel)

    val TypedExpression<out BasicInformation?>.location: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.location)

    val TypedExpression<out BasicInformation?>.hardwareVersion: TypedExpression<UShort?>
      get() =
        fieldSelect<BasicInformation, UShort?>(this, BasicInformation.Attribute.hardwareVersion)

    val TypedExpression<out BasicInformation?>.hardwareVersionString: TypedExpression<String?>
      get() =
        fieldSelect<BasicInformation, String?>(
          this,
          BasicInformation.Attribute.hardwareVersionString,
        )

    val TypedExpression<out BasicInformation?>.softwareVersion: TypedExpression<UInt?>
      get() = fieldSelect<BasicInformation, UInt?>(this, BasicInformation.Attribute.softwareVersion)

    val TypedExpression<out BasicInformation?>.softwareVersionString: TypedExpression<String?>
      get() =
        fieldSelect<BasicInformation, String?>(
          this,
          BasicInformation.Attribute.softwareVersionString,
        )

    val TypedExpression<out BasicInformation?>.manufacturingDate: TypedExpression<String?>
      get() =
        fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.manufacturingDate)

    val TypedExpression<out BasicInformation?>.partNumber: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.partNumber)

    val TypedExpression<out BasicInformation?>.productUrl: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.productUrl)

    val TypedExpression<out BasicInformation?>.productLabel: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.productLabel)

    val TypedExpression<out BasicInformation?>.serialNumber: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.serialNumber)

    val TypedExpression<out BasicInformation?>.localConfigDisabled: TypedExpression<Boolean?>
      get() =
        fieldSelect<BasicInformation, Boolean?>(
          this,
          BasicInformation.Attribute.localConfigDisabled,
        )

    val TypedExpression<out BasicInformation?>.reachable: TypedExpression<Boolean?>
      get() = fieldSelect<BasicInformation, Boolean?>(this, BasicInformation.Attribute.reachable)

    val TypedExpression<out BasicInformation?>.uniqueId: TypedExpression<String?>
      get() = fieldSelect<BasicInformation, String?>(this, BasicInformation.Attribute.uniqueId)

    val TypedExpression<out BasicInformation?>.capabilityMinima:
      TypedExpression<CapabilityMinimaStruct?>
      get() =
        fieldSelect<BasicInformation, CapabilityMinimaStruct?>(
          this,
          BasicInformation.Attribute.capabilityMinima,
        )

    val TypedExpression<out BasicInformation?>.productAppearance:
      TypedExpression<ProductAppearanceStruct?>
      get() =
        fieldSelect<BasicInformation, ProductAppearanceStruct?>(
          this,
          BasicInformation.Attribute.productAppearance,
        )

    val TypedExpression<out BasicInformation?>.specificationVersion: TypedExpression<UInt?>
      get() =
        fieldSelect<BasicInformation, UInt?>(this, BasicInformation.Attribute.specificationVersion)

    val TypedExpression<out BasicInformation?>.maxPathsPerInvoke: TypedExpression<UShort?>
      get() =
        fieldSelect<BasicInformation, UShort?>(this, BasicInformation.Attribute.maxPathsPerInvoke)

    val TypedExpression<out BasicInformation?>.configurationVersion: TypedExpression<UInt?>
      get() =
        fieldSelect<BasicInformation, UInt?>(this, BasicInformation.Attribute.configurationVersion)

    val TypedExpression<out BasicInformation?>.generatedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<BasicInformation, List<UInt>>(
          this,
          BasicInformation.Attribute.generatedCommandList,
        )

    val TypedExpression<out BasicInformation?>.acceptedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<BasicInformation, List<UInt>>(
          this,
          BasicInformation.Attribute.acceptedCommandList,
        )

    val TypedExpression<out BasicInformation?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<BasicInformation, List<UInt>>(this, BasicInformation.Attribute.attributeList)

    val TypedExpression<out BasicInformation?>.featureMap: TypedExpression<UInt>
      get() = fieldSelect<BasicInformation, UInt>(this, BasicInformation.Attribute.featureMap)

    val TypedExpression<out BasicInformation?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<BasicInformation, UShort>(this, BasicInformation.Attribute.clusterRevision)

    fun Updater<BasicInformation>.setNodeLabel(value: String) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.nodeLabel, value))
    }

    fun Updater<BasicInformation>.setLocation(value: String) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.location, value))
    }

    fun Updater<BasicInformation>.setLocalConfigDisabled(value: Boolean) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.localConfigDisabled, value))
    }

    /** Send a manufacturer specific ping. */
    fun mfgSpecificPing(): AutomationCommand {
      val commandId = BasicInformationTrait.MfgSpecificPingCommand.requestId.toString()
      return AutomationCommand(BasicInformation, commandId)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "BasicInformation"
  }

  override val factory: TraitFactory<BasicInformation>
    get() = Companion

  // Events
  class StartUpEvent
  private constructor(
    override val eventName: String = "StartUp",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: BasicInformationTrait.StartUp,
  ) : Event, BasicInformationTrait.StartUp by eventPayload {

    override val eventId: Id = Id(BasicInformationTrait.StartUpImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is StartUpEvent) return false
      if (eventId != other.eventId) return false
      if (eventName != other.eventName) return false
      if (timestamp != other.timestamp) return false
      if (eventImportance != other.eventImportance) return false
      if (eventNumber != other.eventNumber) return false
      if (!eventPayload.equals(other.eventPayload)) return false
      return true
    }

    override fun hashCode(): Int {
      var result = 1
      result += 31 * eventId.hashCode()
      result += 31 * eventName.hashCode()
      result += 31 * timestamp.hashCode()
      result += 31 * eventImportance.hashCode()
      result += 31 * eventNumber.hashCode()
      result += 31 * eventPayload.hashCode()
      return result
    }

    /** Descriptor enum for this event's fields. */
    enum class EventFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : Field {
      /** The [softwareVersion][StartUpEvent.Attributes.softwareVersion] event field. */
      softwareVersion("softwareVersion", 0u, "UInt", FieldType.UInt, false, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return BasicInformationTrait.StartUpImpl(
                softwareVersion = fields[EventFields.softwareVersion] as UInt?
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<StartUpEvent>(
        MatterEventFactory(
          BasicInformationTrait.StartUpImpl.Id,
          "StartUp",
          BasicInformationTrait.StartUpImpl.Adapter,
          ::StartUpEvent,
        )
      ) {
      val softwareVersion: EventField<UInt?>
        get() = EventField<UInt?>(BasicInformationTrait.StartUpImpl.Id.traitId, 0u)

      val TypedExpression<out StartUpEvent?>.softwareVersion: TypedExpression<UInt?>
        get() = fieldSelect<StartUpEvent, UInt?>(this, EventFields.softwareVersion)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class ShutDownEvent
  private constructor(
    override val eventName: String = "ShutDown",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: BasicInformationTrait.ShutDown,
  ) : Event, BasicInformationTrait.ShutDown by eventPayload {

    override val eventId: Id = Id(BasicInformationTrait.ShutDownImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ShutDownEvent) return false
      if (eventId != other.eventId) return false
      if (eventName != other.eventName) return false
      if (timestamp != other.timestamp) return false
      if (eventImportance != other.eventImportance) return false
      if (eventNumber != other.eventNumber) return false
      if (!eventPayload.equals(other.eventPayload)) return false
      return true
    }

    override fun hashCode(): Int {
      var result = 1
      result += 31 * eventId.hashCode()
      result += 31 * eventName.hashCode()
      result += 31 * timestamp.hashCode()
      result += 31 * eventImportance.hashCode()
      result += 31 * eventNumber.hashCode()
      result += 31 * eventPayload.hashCode()
      return result
    }

    /** Descriptor enum for this event's fields. */
    enum class EventFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : Field {
      ;

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return BasicInformationTrait.ShutDownImpl()
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<ShutDownEvent>(
        MatterEventFactory(
          BasicInformationTrait.ShutDownImpl.Id,
          "ShutDown",
          BasicInformationTrait.ShutDownImpl.Adapter,
          ::ShutDownEvent,
        )
      ) {

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class LeaveEvent
  private constructor(
    override val eventName: String = "Leave",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: BasicInformationTrait.Leave,
  ) : Event, BasicInformationTrait.Leave by eventPayload {

    override val eventId: Id = Id(BasicInformationTrait.LeaveImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is LeaveEvent) return false
      if (eventId != other.eventId) return false
      if (eventName != other.eventName) return false
      if (timestamp != other.timestamp) return false
      if (eventImportance != other.eventImportance) return false
      if (eventNumber != other.eventNumber) return false
      if (!eventPayload.equals(other.eventPayload)) return false
      return true
    }

    override fun hashCode(): Int {
      var result = 1
      result += 31 * eventId.hashCode()
      result += 31 * eventName.hashCode()
      result += 31 * timestamp.hashCode()
      result += 31 * eventImportance.hashCode()
      result += 31 * eventNumber.hashCode()
      result += 31 * eventPayload.hashCode()
      return result
    }

    /** Descriptor enum for this event's fields. */
    enum class EventFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : Field {
      /** The [fabricIndex][LeaveEvent.Attributes.fabricIndex] event field. */
      fabricIndex("fabricIndex", 0u, "UByte", FieldType.UByte, false, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return BasicInformationTrait.LeaveImpl(
                fabricIndex = fields[EventFields.fabricIndex] as UByte?
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<LeaveEvent>(
        MatterEventFactory(
          BasicInformationTrait.LeaveImpl.Id,
          "Leave",
          BasicInformationTrait.LeaveImpl.Adapter,
          ::LeaveEvent,
        )
      ) {
      val fabricIndex: EventField<UByte?>
        get() = EventField<UByte?>(BasicInformationTrait.LeaveImpl.Id.traitId, 0u)

      val TypedExpression<out LeaveEvent?>.fabricIndex: TypedExpression<UByte?>
        get() = fieldSelect<LeaveEvent, UByte?>(this, EventFields.fabricIndex)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class ReachableChangedEvent
  private constructor(
    override val eventName: String = "ReachableChanged",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: BasicInformationTrait.ReachableChanged,
  ) : Event, BasicInformationTrait.ReachableChanged by eventPayload {

    override val eventId: Id = Id(BasicInformationTrait.ReachableChangedImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ReachableChangedEvent) return false
      if (eventId != other.eventId) return false
      if (eventName != other.eventName) return false
      if (timestamp != other.timestamp) return false
      if (eventImportance != other.eventImportance) return false
      if (eventNumber != other.eventNumber) return false
      if (!eventPayload.equals(other.eventPayload)) return false
      return true
    }

    override fun hashCode(): Int {
      var result = 1
      result += 31 * eventId.hashCode()
      result += 31 * eventName.hashCode()
      result += 31 * timestamp.hashCode()
      result += 31 * eventImportance.hashCode()
      result += 31 * eventNumber.hashCode()
      result += 31 * eventPayload.hashCode()
      return result
    }

    /** Descriptor enum for this event's fields. */
    enum class EventFields(
      override val fieldName: String,
      override val tag: UInt,
      override val typeName: String,
      override val typeEnum: FieldType,
      override val isList: Boolean,
      override val descriptor: HomeDescriptor,
      val isNullable: Boolean,
    ) : Field {
      /**
       * The [reachableNewValue][ReachableChangedEvent.Attributes.reachableNewValue] event field.
       */
      reachableNewValue(
        "reachableNewValue",
        0u,
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

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return BasicInformationTrait.ReachableChangedImpl(
                reachableNewValue = fields[EventFields.reachableNewValue] as Boolean?
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<ReachableChangedEvent>(
        MatterEventFactory(
          BasicInformationTrait.ReachableChangedImpl.Id,
          "ReachableChanged",
          BasicInformationTrait.ReachableChangedImpl.Adapter,
          ::ReachableChangedEvent,
        )
      ) {
      val reachableNewValue: EventField<Boolean?>
        get() = EventField<Boolean?>(BasicInformationTrait.ReachableChangedImpl.Id.traitId, 0u)

      val TypedExpression<out ReachableChangedEvent?>.reachableNewValue: TypedExpression<Boolean?>
        get() = fieldSelect<ReachableChangedEvent, Boolean?>(this, EventFields.reachableNewValue)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }
}

/** @suppress */
class BasicInformationImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) :
  BasicInformation,
  MatterTraitImpl(metadata, client),
  Attributes by attributes,
  Updatable<BasicInformation, MutableAttributes> {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is BasicInformationImpl) return false

    if (metadata != other.metadata) return false
    if (attributes != other.attributes) return false

    return true
  }

  /**
   * Checks if the trait supports an attribute. Some devices might not implement all attributes in a
   * Trait definition.
   *
   * @param attribute The attribute to check for.
   * @return True if the attribute is supported by the trait, false if it is not.
   */
  override fun supports(attribute: BasicInformation.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: BasicInformation.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands

  /** Send a manufacturer specific ping. */
  override suspend fun mfgSpecificPing() {
    sendCommand(
      commandId = BasicInformationTrait.MfgSpecificPingCommand.requestId,
      request = BasicInformationTrait.MfgSpecificPingCommand.Request(),
      requestAdapter = BasicInformationTrait.MfgSpecificPingCommand.Request,
      useTimedCommand = false,
    )
  }

  /** @suppress */
  override suspend fun update(
    optimisticReturn: (BasicInformation) -> Unit,
    init: MutableAttributes.() -> Unit,
  ): BasicInformation {
    val newVal = MutableAttributes(attributes).apply(init)
    val returnVal = BasicInformationImpl(metadata, client, newVal)
    optimisticReturn(returnVal)
    write(MutableAttributes, newVal, useTimedInteraction = false)
    return returnVal
  }

  // Commands

  /**
   * The batchable version of [mfgSpecificPing] command.
   *
   * Send a manufacturer specific ping.
   *
   * @return BatchableCommand<Unit>
   */
  override fun mfgSpecificPingBatchable(): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = BasicInformationTrait.MfgSpecificPingCommand.requestId,
          requestAdapter = BasicInformationTrait.MfgSpecificPingCommand.Request,
          request = BasicInformationTrait.MfgSpecificPingCommand.Request(),
          useTimedCommand = false,
        )
    )
  }

  override fun toString() = attributes.toString()
}
