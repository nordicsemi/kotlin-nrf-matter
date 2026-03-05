// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

import com.google.home.ClusterStruct
import com.google.home.Descriptor as HomeDescriptor
import com.google.home.DescriptorMap
import com.google.home.Field
import com.google.home.Id
import com.google.home.NoOpDescriptor
import com.google.home.StructDescriptor
import com.google.home.Trait
import com.google.home.TraitFactory
import com.google.home.Type as FieldType
import com.google.home.annotation.HomeExperimentalApi
import com.google.home.automation.Attribute as AutomationAttribute
import com.google.home.automation.TypedExpression
import com.google.home.automation.fieldSelect
import com.google.home.matter.MatterTrait
import com.google.home.matter.MatterTraitClient
import com.google.home.matter.MatterTraitFactory
import com.google.home.matter.MatterTraitImpl
import com.google.home.matter.serialization.BitmapAdapter
import com.google.home.matter.serialization.EnumAdapter
import com.google.home.toDescriptorMap
import javax.annotation.processing.Generated
import no.nordicsemi.nrf.matter.DescriptorTrait.Attributes
import no.nordicsemi.nrf.matter.DescriptorTrait.AttributesImpl
import no.nordicsemi.nrf.matter.DescriptorTrait.DeviceTypeStruct
import no.nordicsemi.nrf.matter.DescriptorTrait.Feature
import no.nordicsemi.nrf.matter.DescriptorTrait.SemanticTagStruct

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Commands for the Descriptor trait. */

/**
 * API for the Descriptor trait. This trait provides an interface for describing the device's
 * capabilities and configuration.
 */
@Generated("GoogleHomePlatformCodegen")
interface Descriptor : Attributes, MatterTrait {
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
    /** The [deviceTypeList][DescriptorTrait.Attributes.deviceTypeList] trait attribute. */
    deviceTypeList(
      "deviceTypeList",
      0u,
      "DeviceTypeStruct",
      FieldType.Struct,
      false,
      DeviceTypeStruct.Adapter,
      false,
    ),
    /** The [serverList][DescriptorTrait.Attributes.serverList] trait attribute. */
    serverList("serverList", 1u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [clientList][DescriptorTrait.Attributes.clientList] trait attribute. */
    clientList("clientList", 2u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [partsList][DescriptorTrait.Attributes.partsList] trait attribute. */
    partsList("partsList", 3u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
    /** The [tagList][DescriptorTrait.Attributes.tagList] trait attribute. */
    tagList(
      "tagList",
      4u,
      "SemanticTagStruct",
      FieldType.Struct,
      false,
      SemanticTagStruct.Adapter,
      false,
    ),
    /** The [endpointUniqueId][DescriptorTrait.Attributes.endpointUniqueId] trait attribute. */
    endpointUniqueId(
      "endpointUniqueId",
      5u,
      "String",
      FieldType.String,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [generatedCommandList][DescriptorTrait.Attributes.generatedCommandList] trait attribute.
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
     * The [acceptedCommandList][DescriptorTrait.Attributes.acceptedCommandList] trait attribute.
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
    /** The [attributeList][DescriptorTrait.Attributes.attributeList] trait attribute. */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][DescriptorTrait.Attributes.featureMap] trait attribute. */
    featureMap(
      "featureMap",
      65532u,
      "Feature",
      FieldType.Bitmap,
      false,
      Feature.BitmapDescriptor,
      false,
    ),
    /** The [clusterRevision][DescriptorTrait.Attributes.clusterRevision] trait attribute. */
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
              deviceTypeList = fields[deviceTypeList] as List<DeviceTypeStruct>?,
              serverList = fields[serverList] as List<UInt>?,
              clientList = fields[clientList] as List<UInt>?,
              partsList = fields[partsList] as List<UShort>?,
              tagList = fields[tagList] as List<SemanticTagStruct>?,
              endpointUniqueId = fields[endpointUniqueId] as String?,
              generatedCommandList = fields[generatedCommandList] as List<UInt>,
              acceptedCommandList = fields[acceptedCommandList] as List<UInt>,
              attributeList = fields[attributeList] as List<UInt>,
              featureMap = fields[featureMap] as Feature,
              clusterRevision = fields[clusterRevision] as UShort,
            )
          }
        }
    }
  }

  fun supports(attribute: Attribute): Boolean

  /** @suppress */
  companion object :
    TraitFactory<Descriptor>(
      MatterTraitFactory(
        clusterId = DescriptorTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters = mapOf<String, EnumAdapter<*>>(),
        bitmapAdapters =
          mapOf<String, BitmapAdapter<*>>("Feature" to DescriptorTrait.Feature.Adapter),
        creator = ::DescriptorImpl,
        supportedEvents = mapOf(),
        // All Trait Commands
        commands = mapOf(),
      )
    ) {
    val deviceTypeList: AutomationAttribute<List<DeviceTypeStruct>?>
      get() =
        AutomationAttribute<List<DeviceTypeStruct>?>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.deviceTypeList.tag,
        )

    val serverList: AutomationAttribute<List<UInt>?>
      get() =
        AutomationAttribute<List<UInt>?>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.serverList.tag,
        )

    val clientList: AutomationAttribute<List<UInt>?>
      get() =
        AutomationAttribute<List<UInt>?>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.clientList.tag,
        )

    val partsList: AutomationAttribute<List<UShort>?>
      get() =
        AutomationAttribute<List<UShort>?>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.partsList.tag,
        )

    val tagList: AutomationAttribute<List<SemanticTagStruct>?>
      get() =
        AutomationAttribute<List<SemanticTagStruct>?>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.tagList.tag,
        )

    val endpointUniqueId: AutomationAttribute<String?>
      get() =
        AutomationAttribute<String?>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.endpointUniqueId.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<Feature>
      get() =
        AutomationAttribute<Feature>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          DescriptorTrait.Id.traitId,
          Descriptor.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out Descriptor?>.deviceTypeList: TypedExpression<List<DeviceTypeStruct>?>
      get() =
        fieldSelect<Descriptor, List<DeviceTypeStruct>?>(this, Descriptor.Attribute.deviceTypeList)

    val TypedExpression<out Descriptor?>.serverList: TypedExpression<List<UInt>?>
      get() = fieldSelect<Descriptor, List<UInt>?>(this, Descriptor.Attribute.serverList)

    val TypedExpression<out Descriptor?>.clientList: TypedExpression<List<UInt>?>
      get() = fieldSelect<Descriptor, List<UInt>?>(this, Descriptor.Attribute.clientList)

    val TypedExpression<out Descriptor?>.partsList: TypedExpression<List<UShort>?>
      get() = fieldSelect<Descriptor, List<UShort>?>(this, Descriptor.Attribute.partsList)

    val TypedExpression<out Descriptor?>.tagList: TypedExpression<List<SemanticTagStruct>?>
      get() = fieldSelect<Descriptor, List<SemanticTagStruct>?>(this, Descriptor.Attribute.tagList)

    val TypedExpression<out Descriptor?>.endpointUniqueId: TypedExpression<String?>
      get() = fieldSelect<Descriptor, String?>(this, Descriptor.Attribute.endpointUniqueId)

    val TypedExpression<out Descriptor?>.generatedCommandList: TypedExpression<List<UInt>>
      get() = fieldSelect<Descriptor, List<UInt>>(this, Descriptor.Attribute.generatedCommandList)

    val TypedExpression<out Descriptor?>.acceptedCommandList: TypedExpression<List<UInt>>
      get() = fieldSelect<Descriptor, List<UInt>>(this, Descriptor.Attribute.acceptedCommandList)

    val TypedExpression<out Descriptor?>.attributeList: TypedExpression<List<UInt>>
      get() = fieldSelect<Descriptor, List<UInt>>(this, Descriptor.Attribute.attributeList)

    val TypedExpression<out Descriptor?>.featureMap: TypedExpression<Feature>
      get() = fieldSelect<Descriptor, Feature>(this, Descriptor.Attribute.featureMap)

    val TypedExpression<out Descriptor?>.clusterRevision: TypedExpression<UShort>
      get() = fieldSelect<Descriptor, UShort>(this, Descriptor.Attribute.clusterRevision)

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "Descriptor"
  }

  override val factory: TraitFactory<Descriptor>
    get() = Companion
}

/** @suppress */
class DescriptorImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) : Descriptor, MatterTraitImpl(metadata, client), Attributes by attributes {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DescriptorImpl) return false

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
  override fun supports(attribute: Descriptor.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  // Commands

  override fun toString() = attributes.toString()
}
