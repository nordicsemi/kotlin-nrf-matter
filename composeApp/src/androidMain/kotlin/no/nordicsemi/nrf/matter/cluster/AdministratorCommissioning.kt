// This file contains machine-generated code.
@file:Suppress("PackageName")

package no.nordicsemi.nrf.matter

import com.google.home.BatchableCommand
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
import com.google.home.automation.Command as AutomationCommand
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
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.Attributes
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.AttributesImpl
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.CommissioningWindowStatusEnum
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.Feature
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.OpenCommissioningWindowCommand
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.RevokeCommissioningCommand
import no.nordicsemi.nrf.matter.AdministratorCommissioningTrait.StatusCode

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the AdministratorCommissioning trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface AdministratorCommissioningCommands {
  suspend fun openCommissioningWindow(
    commissioningTimeout: UShort,
    pakePasscodeVerifier: ByteArray,
    discriminator: UShort,
    iterations: UInt,
    salt: ByteArray,
  )

  suspend fun openBasicCommissioningWindow(commissioningTimeout: UShort)

  suspend fun revokeCommissioning()

  fun openCommissioningWindowBatchable(
    commissioningTimeout: UShort,
    pakePasscodeVerifier: ByteArray,
    discriminator: UShort,
    iterations: UInt,
    salt: ByteArray,
  ): BatchableCommand<Unit>

  fun openBasicCommissioningWindowBatchable(commissioningTimeout: UShort): BatchableCommand<Unit>

  fun revokeCommissioningBatchable(): BatchableCommand<Unit>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface AdministratorCommissioningCommandsDefaultImpl : AdministratorCommissioningCommands {
  override suspend fun openCommissioningWindow(
    commissioningTimeout: UShort,
    pakePasscodeVerifier: ByteArray,
    discriminator: UShort,
    iterations: UInt,
    salt: ByteArray,
  ) {
    TODO("Not Implemented")
  }

  override suspend fun openBasicCommissioningWindow(commissioningTimeout: UShort) {
    TODO("Not Implemented")
  }

  override suspend fun revokeCommissioning() {
    TODO("Not Implemented")
  }

  override fun openCommissioningWindowBatchable(
    commissioningTimeout: UShort,
    pakePasscodeVerifier: ByteArray,
    discriminator: UShort,
    iterations: UInt,
    salt: ByteArray,
  ): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }

  override fun openBasicCommissioningWindowBatchable(
    commissioningTimeout: UShort
  ): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }

  override fun revokeCommissioningBatchable(): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }
}

/** API for the AdministratorCommissioning trait. */
@Generated("GoogleHomePlatformCodegen")
interface AdministratorCommissioning : Attributes, MatterTrait, AdministratorCommissioningCommands {
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
     * The [windowStatus][AdministratorCommissioningTrait.Attributes.windowStatus] trait attribute.
     */
    windowStatus(
      "windowStatus",
      0u,
      "CommissioningWindowStatusEnum",
      FieldType.Enum,
      false,
      CommissioningWindowStatusEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [adminFabricIndex][AdministratorCommissioningTrait.Attributes.adminFabricIndex] trait
     * attribute.
     */
    adminFabricIndex("adminFabricIndex", 1u, "UByte", FieldType.UByte, false, NoOpDescriptor, true),
    /**
     * The [adminVendorId][AdministratorCommissioningTrait.Attributes.adminVendorId] trait
     * attribute.
     */
    adminVendorId("adminVendorId", 2u, "UShort", FieldType.UShort, false, NoOpDescriptor, true),
    /**
     * The [generatedCommandList][AdministratorCommissioningTrait.Attributes.generatedCommandList]
     * trait attribute.
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
     * The [acceptedCommandList][AdministratorCommissioningTrait.Attributes.acceptedCommandList]
     * trait attribute.
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
    /**
     * The [attributeList][AdministratorCommissioningTrait.Attributes.attributeList] trait
     * attribute.
     */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][AdministratorCommissioningTrait.Attributes.featureMap] trait attribute. */
    featureMap(
      "featureMap",
      65532u,
      "Feature",
      FieldType.Bitmap,
      false,
      Feature.BitmapDescriptor,
      false,
    ),
    /**
     * The [clusterRevision][AdministratorCommissioningTrait.Attributes.clusterRevision] trait
     * attribute.
     */
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
              windowStatus = fields[windowStatus] as CommissioningWindowStatusEnum?,
              adminFabricIndex = fields[adminFabricIndex] as UByte?,
              adminVendorId = fields[adminVendorId] as UShort?,
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

  /** Descriptor enum for this trait's commands. */
  enum class Command(val tag: UInt) {
    /**
     * The [openCommissioningWindow][AdministratorCommissioningCommands.openCommissioningWindow]
     * trait command.
     */
    OpenCommissioningWindow(0u),
    /**
     * The
     * [openBasicCommissioningWindow][AdministratorCommissioningCommands.openBasicCommissioningWindow]
     * trait command.
     */
    OpenBasicCommissioningWindow(1u),
    /**
     * The [revokeCommissioning][AdministratorCommissioningCommands.revokeCommissioning] trait
     * command.
     */
    RevokeCommissioning(2u),
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<AdministratorCommissioning>(
      MatterTraitFactory(
        clusterId = AdministratorCommissioningTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "CommissioningWindowStatusEnum" to
              AdministratorCommissioningTrait.CommissioningWindowStatusEnum.Adapter,
            "StatusCode" to AdministratorCommissioningTrait.StatusCode.Adapter,
          ),
        bitmapAdapters =
          mapOf<String, BitmapAdapter<*>>(
            "Feature" to AdministratorCommissioningTrait.Feature.Adapter
          ),
        creator = ::AdministratorCommissioningImpl,
        supportedEvents = mapOf(),
        // All Trait Commands
        commands =
          mapOf(
            AdministratorCommissioningTrait.OpenCommissioningWindowCommand.requestId.toString() to
              OpenCommissioningWindowCommand,
            AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.requestId
              .toString() to OpenBasicCommissioningWindowCommand,
            AdministratorCommissioningTrait.RevokeCommissioningCommand.requestId.toString() to
              RevokeCommissioningCommand,
          ),
      )
    ) {
    val windowStatus: AutomationAttribute<CommissioningWindowStatusEnum?>
      get() =
        AutomationAttribute<CommissioningWindowStatusEnum?>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.windowStatus.tag,
        )

    val adminFabricIndex: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.adminFabricIndex.tag,
        )

    val adminVendorId: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.adminVendorId.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<Feature>
      get() =
        AutomationAttribute<Feature>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          AdministratorCommissioningTrait.Id.traitId,
          AdministratorCommissioning.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out AdministratorCommissioning?>.windowStatus:
      TypedExpression<CommissioningWindowStatusEnum?>
      get() =
        fieldSelect<AdministratorCommissioning, CommissioningWindowStatusEnum?>(
          this,
          AdministratorCommissioning.Attribute.windowStatus,
        )

    val TypedExpression<out AdministratorCommissioning?>.adminFabricIndex: TypedExpression<UByte?>
      get() =
        fieldSelect<AdministratorCommissioning, UByte?>(
          this,
          AdministratorCommissioning.Attribute.adminFabricIndex,
        )

    val TypedExpression<out AdministratorCommissioning?>.adminVendorId: TypedExpression<UShort?>
      get() =
        fieldSelect<AdministratorCommissioning, UShort?>(
          this,
          AdministratorCommissioning.Attribute.adminVendorId,
        )

    val TypedExpression<out AdministratorCommissioning?>.generatedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<AdministratorCommissioning, List<UInt>>(
          this,
          AdministratorCommissioning.Attribute.generatedCommandList,
        )

    val TypedExpression<out AdministratorCommissioning?>.acceptedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<AdministratorCommissioning, List<UInt>>(
          this,
          AdministratorCommissioning.Attribute.acceptedCommandList,
        )

    val TypedExpression<out AdministratorCommissioning?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<AdministratorCommissioning, List<UInt>>(
          this,
          AdministratorCommissioning.Attribute.attributeList,
        )

    val TypedExpression<out AdministratorCommissioning?>.featureMap: TypedExpression<Feature>
      get() =
        fieldSelect<AdministratorCommissioning, Feature>(
          this,
          AdministratorCommissioning.Attribute.featureMap,
        )

    val TypedExpression<out AdministratorCommissioning?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<AdministratorCommissioning, UShort>(
          this,
          AdministratorCommissioning.Attribute.clusterRevision,
        )

    fun openCommissioningWindow(
      commissioningTimeout: UShort,
      pakePasscodeVerifier: ByteArray,
      discriminator: UShort,
      iterations: UInt,
      salt: ByteArray,
    ): AutomationCommand {
      val commandId =
        AdministratorCommissioningTrait.OpenCommissioningWindowCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          OpenCommissioningWindowCommand.Request.CommandFields.commissioningTimeout to
            commissioningTimeout,
          OpenCommissioningWindowCommand.Request.CommandFields.pakePasscodeVerifier to
            pakePasscodeVerifier,
          OpenCommissioningWindowCommand.Request.CommandFields.discriminator to discriminator,
          OpenCommissioningWindowCommand.Request.CommandFields.iterations to iterations,
          OpenCommissioningWindowCommand.Request.CommandFields.salt to salt,
        )

      return AutomationCommand(AdministratorCommissioning, commandId, paramsMap)
    }

    fun openBasicCommissioningWindow(commissioningTimeout: UShort): AutomationCommand {
      val commandId =
        AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          OpenBasicCommissioningWindowCommand.Request.CommandFields.commissioningTimeout to
            commissioningTimeout
        )

      return AutomationCommand(AdministratorCommissioning, commandId, paramsMap)
    }

    fun revokeCommissioning(): AutomationCommand {
      val commandId =
        AdministratorCommissioningTrait.RevokeCommissioningCommand.requestId.toString()
      return AutomationCommand(AdministratorCommissioning, commandId)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "AdministratorCommissioning"
  }

  override val factory: TraitFactory<AdministratorCommissioning>
    get() = Companion
}

/** @suppress */
class AdministratorCommissioningImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) : AdministratorCommissioning, MatterTraitImpl(metadata, client), Attributes by attributes {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AdministratorCommissioningImpl) return false

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
  override fun supports(attribute: AdministratorCommissioning.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: AdministratorCommissioning.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun openCommissioningWindow(
    commissioningTimeout: UShort,
    pakePasscodeVerifier: ByteArray,
    discriminator: UShort,
    iterations: UInt,
    salt: ByteArray,
  ) {
    sendCommand(
      commandId = AdministratorCommissioningTrait.OpenCommissioningWindowCommand.requestId,
      request =
        AdministratorCommissioningTrait.OpenCommissioningWindowCommand.Request(
          commissioningTimeout,
          pakePasscodeVerifier,
          discriminator,
          iterations,
          salt,
        ),
      requestAdapter = AdministratorCommissioningTrait.OpenCommissioningWindowCommand.Request,
      useTimedCommand = true,
    )
  }

  override suspend fun openBasicCommissioningWindow(commissioningTimeout: UShort) {
    sendCommand(
      commandId = AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.requestId,
      request =
        AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.Request(
          commissioningTimeout
        ),
      requestAdapter = AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.Request,
      useTimedCommand = true,
    )
  }

  override suspend fun revokeCommissioning() {
    sendCommand(
      commandId = AdministratorCommissioningTrait.RevokeCommissioningCommand.requestId,
      request = AdministratorCommissioningTrait.RevokeCommissioningCommand.Request(),
      requestAdapter = AdministratorCommissioningTrait.RevokeCommissioningCommand.Request,
      useTimedCommand = true,
    )
  }

  // Commands

  override fun openCommissioningWindowBatchable(
    commissioningTimeout: UShort,
    pakePasscodeVerifier: ByteArray,
    discriminator: UShort,
    iterations: UInt,
    salt: ByteArray,
  ): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = AdministratorCommissioningTrait.OpenCommissioningWindowCommand.requestId,
          requestAdapter = AdministratorCommissioningTrait.OpenCommissioningWindowCommand.Request,
          request =
            AdministratorCommissioningTrait.OpenCommissioningWindowCommand.Request(
              commissioningTimeout,
              pakePasscodeVerifier,
              discriminator,
              iterations,
              salt,
            ),
          useTimedCommand = true,
        )
    )
  }

  override fun openBasicCommissioningWindowBatchable(
    commissioningTimeout: UShort
  ): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.requestId,
          requestAdapter =
            AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.Request,
          request =
            AdministratorCommissioningTrait.OpenBasicCommissioningWindowCommand.Request(
              commissioningTimeout
            ),
          useTimedCommand = true,
        )
    )
  }

  override fun revokeCommissioningBatchable(): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = AdministratorCommissioningTrait.RevokeCommissioningCommand.requestId,
          requestAdapter = AdministratorCommissioningTrait.RevokeCommissioningCommand.Request,
          request = AdministratorCommissioningTrait.RevokeCommissioningCommand.Request(),
          useTimedCommand = true,
        )
    )
  }

  override fun toString() = attributes.toString()
}
