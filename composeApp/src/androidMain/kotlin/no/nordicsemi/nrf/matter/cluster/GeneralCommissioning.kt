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
import com.google.home.Updatable
import com.google.home.annotation.HomeExperimentalApi
import com.google.home.automation.Attribute as AutomationAttribute
import com.google.home.automation.AttributeToUpdate
import com.google.home.automation.Command as AutomationCommand
import com.google.home.automation.TypedExpression
import com.google.home.automation.Updater
import com.google.home.automation.fieldSelect
import com.google.home.matter.MatterTrait
import com.google.home.matter.MatterTraitClient
import com.google.home.matter.MatterTraitFactory
import com.google.home.matter.MatterTraitImpl
import com.google.home.matter.serialization.BitmapAdapter
import com.google.home.matter.serialization.EnumAdapter
import com.google.home.toDescriptorMap
import javax.annotation.processing.Generated
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.ArmFailSafeCommand
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.AttributesImpl
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.BasicCommissioningInfo
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.CommissioningCompleteCommand
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.CommissioningErrorEnum
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Feature
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.MutableAttributes
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.NetworkRecoveryReasonEnum
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.RegulatoryLocationTypeEnum
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetRegulatoryConfigCommand
import no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetTcAcknowledgementsCommand

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the GeneralCommissioning trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface GeneralCommissioningCommands {
  suspend fun armFailSafe(
    expiryLengthSeconds: UShort,
    breadcrumb: ULong,
  ): no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.ArmFailSafeCommand.Response

  suspend fun setRegulatoryConfig(
    newRegulatoryConfig: RegulatoryLocationTypeEnum,
    countryCode: String,
    breadcrumb: ULong,
  ): no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response

  suspend fun commissioningComplete():
    no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.CommissioningCompleteCommand.Response

  suspend fun setTcAcknowledgements(
    tcVersion: UShort,
    tcUserResponse: UShort,
  ): no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response

  fun armFailSafeBatchable(
    expiryLengthSeconds: UShort,
    breadcrumb: ULong,
  ): BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.ArmFailSafeCommand.Response>

  fun setRegulatoryConfigBatchable(
    newRegulatoryConfig: RegulatoryLocationTypeEnum,
    countryCode: String,
    breadcrumb: ULong,
  ): BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response>

  fun commissioningCompleteBatchable():
    BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.CommissioningCompleteCommand.Response>

  fun setTcAcknowledgementsBatchable(
    tcVersion: UShort,
    tcUserResponse: UShort,
  ): BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface GeneralCommissioningCommandsDefaultImpl : GeneralCommissioningCommands {
  override suspend fun armFailSafe(
    expiryLengthSeconds: UShort,
    breadcrumb: ULong,
  ): no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.ArmFailSafeCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun setRegulatoryConfig(
    newRegulatoryConfig: RegulatoryLocationTypeEnum,
    countryCode: String,
    breadcrumb: ULong,
  ): no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun commissioningComplete():
    no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.CommissioningCompleteCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun setTcAcknowledgements(
    tcVersion: UShort,
    tcUserResponse: UShort,
  ): no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response {
    TODO("Not Implemented")
  }

  override fun armFailSafeBatchable(
    expiryLengthSeconds: UShort,
    breadcrumb: ULong,
  ): BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.ArmFailSafeCommand.Response> {
    TODO("Not Implemented")
  }

  override fun setRegulatoryConfigBatchable(
    newRegulatoryConfig: RegulatoryLocationTypeEnum,
    countryCode: String,
    breadcrumb: ULong,
  ): BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response> {
    TODO("Not Implemented")
  }

  override fun commissioningCompleteBatchable():
    BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.CommissioningCompleteCommand.Response> {
    TODO("Not Implemented")
  }

  override fun setTcAcknowledgementsBatchable(
    tcVersion: UShort,
    tcUserResponse: UShort,
  ): BatchableCommand<no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response> {
    TODO("Not Implemented")
  }
}

/** API for the GeneralCommissioning trait. */
@Generated("GoogleHomePlatformCodegen")
interface GeneralCommissioning :
  Attributes,
  MatterTrait,
  Updatable<GeneralCommissioning, MutableAttributes>,
  GeneralCommissioningCommands {
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
    /** The [breadcrumb][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.breadcrumb] trait attribute. */
    breadcrumb("breadcrumb", 0u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
    /**
     * The [basicCommissioningInfo][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.basicCommissioningInfo]
     * trait attribute.
     */
    basicCommissioningInfo(
      "basicCommissioningInfo",
      1u,
      "BasicCommissioningInfo",
      FieldType.Struct,
      false,
      BasicCommissioningInfo.Adapter,
      false,
    ),
    /**
     * The [regulatoryConfig][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.regulatoryConfig] trait
     * attribute.
     */
    regulatoryConfig(
      "regulatoryConfig",
      2u,
      "RegulatoryLocationTypeEnum",
      FieldType.Enum,
      false,
      RegulatoryLocationTypeEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [locationCapability][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.locationCapability] trait
     * attribute.
     */
    locationCapability(
      "locationCapability",
      3u,
      "RegulatoryLocationTypeEnum",
      FieldType.Enum,
      false,
      RegulatoryLocationTypeEnum.EnumDescriptor,
      false,
    ),
    /**
     * The
     * [supportsConcurrentConnection][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.supportsConcurrentConnection]
     * trait attribute.
     */
    supportsConcurrentConnection(
      "supportsConcurrentConnection",
      4u,
      "Boolean",
      FieldType.Boolean,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [tcAcceptedVersion][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.tcAcceptedVersion] trait
     * attribute.
     */
    tcAcceptedVersion(
      "tcAcceptedVersion",
      5u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [tcMinRequiredVersion][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.tcMinRequiredVersion] trait
     * attribute.
     */
    tcMinRequiredVersion(
      "tcMinRequiredVersion",
      6u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [tcAcknowledgements][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.tcAcknowledgements] trait
     * attribute.
     */
    tcAcknowledgements(
      "tcAcknowledgements",
      7u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The
     * [tcAcknowledgementsRequired][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.tcAcknowledgementsRequired]
     * trait attribute.
     */
    tcAcknowledgementsRequired(
      "tcAcknowledgementsRequired",
      8u,
      "Boolean",
      FieldType.Boolean,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [tcUpdateDeadline][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.tcUpdateDeadline] trait
     * attribute.
     */
    tcUpdateDeadline("tcUpdateDeadline", 9u, "UInt", FieldType.UInt, false, NoOpDescriptor, true),
    /**
     * The [recoveryIdentifier][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.recoveryIdentifier] trait
     * attribute.
     */
    recoveryIdentifier(
      "recoveryIdentifier",
      10u,
      "ByteArray",
      FieldType.ByteArray,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [networkRecoveryReason][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.networkRecoveryReason] trait
     * attribute.
     */
    networkRecoveryReason(
      "networkRecoveryReason",
      11u,
      "NetworkRecoveryReasonEnum",
      FieldType.Enum,
      false,
      NetworkRecoveryReasonEnum.EnumDescriptor,
      true,
    ),
    /**
     * The
     * [isCommissioningWithoutPower][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.isCommissioningWithoutPower]
     * trait attribute.
     */
    isCommissioningWithoutPower(
      "isCommissioningWithoutPower",
      12u,
      "Boolean",
      FieldType.Boolean,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [generatedCommandList][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.generatedCommandList] trait
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
     * The [acceptedCommandList][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.acceptedCommandList] trait
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
    /** The [attributeList][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.attributeList] trait attribute. */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.featureMap] trait attribute. */
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
     * The [clusterRevision][no.nordicsemi.nrf.matter.cluster.GeneralCommissioningTrait.Attributes.clusterRevision] trait attribute.
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
              breadcrumb = fields[breadcrumb] as ULong?,
              basicCommissioningInfo = fields[basicCommissioningInfo] as BasicCommissioningInfo?,
              regulatoryConfig = fields[regulatoryConfig] as RegulatoryLocationTypeEnum?,
              locationCapability = fields[locationCapability] as RegulatoryLocationTypeEnum?,
              supportsConcurrentConnection = fields[supportsConcurrentConnection] as Boolean?,
              tcAcceptedVersion = fields[tcAcceptedVersion] as UShort?,
              tcMinRequiredVersion = fields[tcMinRequiredVersion] as UShort?,
              tcAcknowledgements = fields[tcAcknowledgements] as UShort?,
              tcAcknowledgementsRequired = fields[tcAcknowledgementsRequired] as Boolean?,
              tcUpdateDeadline = fields[tcUpdateDeadline] as UInt?,
              recoveryIdentifier = fields[recoveryIdentifier] as ByteArray?,
              networkRecoveryReason = fields[networkRecoveryReason] as NetworkRecoveryReasonEnum?,
              isCommissioningWithoutPower = fields[isCommissioningWithoutPower] as Boolean?,
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
    /** The [armFailSafe][GeneralCommissioningCommands.armFailSafe] trait command. */
    ArmFailSafe(0u),
    /**
     * The [setRegulatoryConfig][GeneralCommissioningCommands.setRegulatoryConfig] trait command.
     */
    SetRegulatoryConfig(2u),
    /**
     * The [commissioningComplete][GeneralCommissioningCommands.commissioningComplete] trait
     * command.
     */
    CommissioningComplete(4u),
    /**
     * The [setTCAcknowledgements][GeneralCommissioningCommands.setTCAcknowledgements] trait
     * command.
     */
    SetTcAcknowledgements(6u),
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<GeneralCommissioning>(
      MatterTraitFactory(
        clusterId = GeneralCommissioningTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "CommissioningErrorEnum" to GeneralCommissioningTrait.CommissioningErrorEnum.Adapter,
            "NetworkRecoveryReasonEnum" to
              GeneralCommissioningTrait.NetworkRecoveryReasonEnum.Adapter,
            "RegulatoryLocationTypeEnum" to
              GeneralCommissioningTrait.RegulatoryLocationTypeEnum.Adapter,
          ),
        bitmapAdapters =
          mapOf<String, BitmapAdapter<*>>("Feature" to GeneralCommissioningTrait.Feature.Adapter),
        creator = ::GeneralCommissioningImpl,
        supportedEvents = mapOf(),
        // All Trait Commands
        commands =
          mapOf(
            GeneralCommissioningTrait.ArmFailSafeCommand.requestId.toString() to ArmFailSafeCommand,
            GeneralCommissioningTrait.SetRegulatoryConfigCommand.requestId.toString() to
              SetRegulatoryConfigCommand,
            GeneralCommissioningTrait.CommissioningCompleteCommand.requestId.toString() to
              CommissioningCompleteCommand,
            GeneralCommissioningTrait.SetTcAcknowledgementsCommand.requestId.toString() to
              SetTcAcknowledgementsCommand,
          ),
      )
    ) {
    val breadcrumb: AutomationAttribute<ULong?>
      get() =
        AutomationAttribute<ULong?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.breadcrumb.tag,
        )

    val basicCommissioningInfo: AutomationAttribute<BasicCommissioningInfo?>
      get() =
        AutomationAttribute<BasicCommissioningInfo?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.basicCommissioningInfo.tag,
        )

    val regulatoryConfig: AutomationAttribute<RegulatoryLocationTypeEnum?>
      get() =
        AutomationAttribute<RegulatoryLocationTypeEnum?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.regulatoryConfig.tag,
        )

    val locationCapability: AutomationAttribute<RegulatoryLocationTypeEnum?>
      get() =
        AutomationAttribute<RegulatoryLocationTypeEnum?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.locationCapability.tag,
        )

    val supportsConcurrentConnection: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.supportsConcurrentConnection.tag,
        )

    val tcAcceptedVersion: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.tcAcceptedVersion.tag,
        )

    val tcMinRequiredVersion: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.tcMinRequiredVersion.tag,
        )

    val tcAcknowledgements: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.tcAcknowledgements.tag,
        )

    val tcAcknowledgementsRequired: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.tcAcknowledgementsRequired.tag,
        )

    val tcUpdateDeadline: AutomationAttribute<UInt?>
      get() =
        AutomationAttribute<UInt?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.tcUpdateDeadline.tag,
        )

    val recoveryIdentifier: AutomationAttribute<ByteArray?>
      get() =
        AutomationAttribute<ByteArray?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.recoveryIdentifier.tag,
        )

    val networkRecoveryReason: AutomationAttribute<NetworkRecoveryReasonEnum?>
      get() =
        AutomationAttribute<NetworkRecoveryReasonEnum?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.networkRecoveryReason.tag,
        )

    val isCommissioningWithoutPower: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.isCommissioningWithoutPower.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<Feature>
      get() =
        AutomationAttribute<Feature>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          GeneralCommissioningTrait.Id.traitId,
          GeneralCommissioning.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out GeneralCommissioning?>.breadcrumb: TypedExpression<ULong?>
      get() =
        fieldSelect<GeneralCommissioning, ULong?>(this, GeneralCommissioning.Attribute.breadcrumb)

    val TypedExpression<out GeneralCommissioning?>.basicCommissioningInfo:
      TypedExpression<BasicCommissioningInfo?>
      get() =
        fieldSelect<GeneralCommissioning, BasicCommissioningInfo?>(
          this,
          GeneralCommissioning.Attribute.basicCommissioningInfo,
        )

    val TypedExpression<out GeneralCommissioning?>.regulatoryConfig:
      TypedExpression<RegulatoryLocationTypeEnum?>
      get() =
        fieldSelect<GeneralCommissioning, RegulatoryLocationTypeEnum?>(
          this,
          GeneralCommissioning.Attribute.regulatoryConfig,
        )

    val TypedExpression<out GeneralCommissioning?>.locationCapability:
      TypedExpression<RegulatoryLocationTypeEnum?>
      get() =
        fieldSelect<GeneralCommissioning, RegulatoryLocationTypeEnum?>(
          this,
          GeneralCommissioning.Attribute.locationCapability,
        )

    val TypedExpression<out GeneralCommissioning?>.supportsConcurrentConnection:
      TypedExpression<Boolean?>
      get() =
        fieldSelect<GeneralCommissioning, Boolean?>(
          this,
          GeneralCommissioning.Attribute.supportsConcurrentConnection,
        )

    val TypedExpression<out GeneralCommissioning?>.tcAcceptedVersion: TypedExpression<UShort?>
      get() =
        fieldSelect<GeneralCommissioning, UShort?>(
          this,
          GeneralCommissioning.Attribute.tcAcceptedVersion,
        )

    val TypedExpression<out GeneralCommissioning?>.tcMinRequiredVersion: TypedExpression<UShort?>
      get() =
        fieldSelect<GeneralCommissioning, UShort?>(
          this,
          GeneralCommissioning.Attribute.tcMinRequiredVersion,
        )

    val TypedExpression<out GeneralCommissioning?>.tcAcknowledgements: TypedExpression<UShort?>
      get() =
        fieldSelect<GeneralCommissioning, UShort?>(
          this,
          GeneralCommissioning.Attribute.tcAcknowledgements,
        )

    val TypedExpression<out GeneralCommissioning?>.tcAcknowledgementsRequired:
      TypedExpression<Boolean?>
      get() =
        fieldSelect<GeneralCommissioning, Boolean?>(
          this,
          GeneralCommissioning.Attribute.tcAcknowledgementsRequired,
        )

    val TypedExpression<out GeneralCommissioning?>.tcUpdateDeadline: TypedExpression<UInt?>
      get() =
        fieldSelect<GeneralCommissioning, UInt?>(
          this,
          GeneralCommissioning.Attribute.tcUpdateDeadline,
        )

    val TypedExpression<out GeneralCommissioning?>.recoveryIdentifier: TypedExpression<ByteArray?>
      get() =
        fieldSelect<GeneralCommissioning, ByteArray?>(
          this,
          GeneralCommissioning.Attribute.recoveryIdentifier,
        )

    val TypedExpression<out GeneralCommissioning?>.networkRecoveryReason:
      TypedExpression<NetworkRecoveryReasonEnum?>
      get() =
        fieldSelect<GeneralCommissioning, NetworkRecoveryReasonEnum?>(
          this,
          GeneralCommissioning.Attribute.networkRecoveryReason,
        )

    val TypedExpression<out GeneralCommissioning?>.isCommissioningWithoutPower:
      TypedExpression<Boolean?>
      get() =
        fieldSelect<GeneralCommissioning, Boolean?>(
          this,
          GeneralCommissioning.Attribute.isCommissioningWithoutPower,
        )

    val TypedExpression<out GeneralCommissioning?>.generatedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GeneralCommissioning, List<UInt>>(
          this,
          GeneralCommissioning.Attribute.generatedCommandList,
        )

    val TypedExpression<out GeneralCommissioning?>.acceptedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GeneralCommissioning, List<UInt>>(
          this,
          GeneralCommissioning.Attribute.acceptedCommandList,
        )

    val TypedExpression<out GeneralCommissioning?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GeneralCommissioning, List<UInt>>(
          this,
          GeneralCommissioning.Attribute.attributeList,
        )

    val TypedExpression<out GeneralCommissioning?>.featureMap: TypedExpression<Feature>
      get() =
        fieldSelect<GeneralCommissioning, Feature>(this, GeneralCommissioning.Attribute.featureMap)

    val TypedExpression<out GeneralCommissioning?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<GeneralCommissioning, UShort>(
          this,
          GeneralCommissioning.Attribute.clusterRevision,
        )

    fun Updater<GeneralCommissioning>.setBreadcrumb(value: ULong) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.breadcrumb, value))
    }

    fun armFailSafe(expiryLengthSeconds: UShort, breadcrumb: ULong): AutomationCommand {
      val commandId = GeneralCommissioningTrait.ArmFailSafeCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          ArmFailSafeCommand.Request.CommandFields.expiryLengthSeconds to expiryLengthSeconds,
          ArmFailSafeCommand.Request.CommandFields.breadcrumb to breadcrumb,
        )

      return AutomationCommand(GeneralCommissioning, commandId, paramsMap)
    }

    fun setRegulatoryConfig(
      newRegulatoryConfig: RegulatoryLocationTypeEnum,
      countryCode: String,
      breadcrumb: ULong,
    ): AutomationCommand {
      val commandId = GeneralCommissioningTrait.SetRegulatoryConfigCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          SetRegulatoryConfigCommand.Request.CommandFields.newRegulatoryConfig to
            newRegulatoryConfig,
          SetRegulatoryConfigCommand.Request.CommandFields.countryCode to countryCode,
          SetRegulatoryConfigCommand.Request.CommandFields.breadcrumb to breadcrumb,
        )

      return AutomationCommand(GeneralCommissioning, commandId, paramsMap)
    }

    fun commissioningComplete(): AutomationCommand {
      val commandId = GeneralCommissioningTrait.CommissioningCompleteCommand.requestId.toString()
      return AutomationCommand(GeneralCommissioning, commandId)
    }

    fun setTcAcknowledgements(tcVersion: UShort, tcUserResponse: UShort): AutomationCommand {
      val commandId = GeneralCommissioningTrait.SetTcAcknowledgementsCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          SetTcAcknowledgementsCommand.Request.CommandFields.tcVersion to tcVersion,
          SetTcAcknowledgementsCommand.Request.CommandFields.tcUserResponse to tcUserResponse,
        )

      return AutomationCommand(GeneralCommissioning, commandId, paramsMap)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "GeneralCommissioning"
  }

  override val factory: TraitFactory<GeneralCommissioning>
    get() = Companion
}

/** @suppress */
class GeneralCommissioningImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) :
  GeneralCommissioning,
  MatterTraitImpl(metadata, client),
  Attributes by attributes,
  Updatable<GeneralCommissioning, MutableAttributes> {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GeneralCommissioningImpl) return false

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
  override fun supports(attribute: GeneralCommissioning.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: GeneralCommissioning.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun armFailSafe(
    expiryLengthSeconds: UShort,
    breadcrumb: ULong,
  ): GeneralCommissioningTrait.ArmFailSafeCommand.Response {
    return sendCommand(
      commandId = GeneralCommissioningTrait.ArmFailSafeCommand.requestId,
      request =
        GeneralCommissioningTrait.ArmFailSafeCommand.Request(expiryLengthSeconds, breadcrumb),
      requestAdapter = GeneralCommissioningTrait.ArmFailSafeCommand.Request,
      responseAdapter = GeneralCommissioningTrait.ArmFailSafeCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun setRegulatoryConfig(
    newRegulatoryConfig: RegulatoryLocationTypeEnum,
    countryCode: String,
    breadcrumb: ULong,
  ): GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response {
    return sendCommand(
      commandId = GeneralCommissioningTrait.SetRegulatoryConfigCommand.requestId,
      request =
        GeneralCommissioningTrait.SetRegulatoryConfigCommand.Request(
          newRegulatoryConfig,
          countryCode,
          breadcrumb,
        ),
      requestAdapter = GeneralCommissioningTrait.SetRegulatoryConfigCommand.Request,
      responseAdapter = GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun commissioningComplete():
    GeneralCommissioningTrait.CommissioningCompleteCommand.Response {
    return sendCommand(
      commandId = GeneralCommissioningTrait.CommissioningCompleteCommand.requestId,
      request = GeneralCommissioningTrait.CommissioningCompleteCommand.Request(),
      requestAdapter = GeneralCommissioningTrait.CommissioningCompleteCommand.Request,
      responseAdapter = GeneralCommissioningTrait.CommissioningCompleteCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun setTcAcknowledgements(
    tcVersion: UShort,
    tcUserResponse: UShort,
  ): GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response {
    return sendCommand(
      commandId = GeneralCommissioningTrait.SetTcAcknowledgementsCommand.requestId,
      request =
        GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Request(tcVersion, tcUserResponse),
      requestAdapter = GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Request,
      responseAdapter = GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response,
      useTimedCommand = false,
    )
  }

  /** @suppress */
  override suspend fun update(
    optimisticReturn: (GeneralCommissioning) -> Unit,
    init: MutableAttributes.() -> Unit,
  ): GeneralCommissioning {
    val newVal = MutableAttributes(attributes).apply(init)
    val returnVal = GeneralCommissioningImpl(metadata, client, newVal)
    optimisticReturn(returnVal)
    write(MutableAttributes, newVal, useTimedInteraction = false)
    return returnVal
  }

  // Commands

  override fun armFailSafeBatchable(
    expiryLengthSeconds: UShort,
    breadcrumb: ULong,
  ): BatchableCommand<GeneralCommissioningTrait.ArmFailSafeCommand.Response> {
    return BatchableCommand<GeneralCommissioningTrait.ArmFailSafeCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GeneralCommissioningTrait.ArmFailSafeCommand.requestId,
          requestAdapter = GeneralCommissioningTrait.ArmFailSafeCommand.Request,
          request =
            GeneralCommissioningTrait.ArmFailSafeCommand.Request(expiryLengthSeconds, breadcrumb),
          useTimedCommand = false,
        ),
      responseAdapter = GeneralCommissioningTrait.ArmFailSafeCommand.Response,
    )
  }

  override fun setRegulatoryConfigBatchable(
    newRegulatoryConfig: RegulatoryLocationTypeEnum,
    countryCode: String,
    breadcrumb: ULong,
  ): BatchableCommand<GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response> {
    return BatchableCommand<GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GeneralCommissioningTrait.SetRegulatoryConfigCommand.requestId,
          requestAdapter = GeneralCommissioningTrait.SetRegulatoryConfigCommand.Request,
          request =
            GeneralCommissioningTrait.SetRegulatoryConfigCommand.Request(
              newRegulatoryConfig,
              countryCode,
              breadcrumb,
            ),
          useTimedCommand = false,
        ),
      responseAdapter = GeneralCommissioningTrait.SetRegulatoryConfigCommand.Response,
    )
  }

  override fun commissioningCompleteBatchable():
    BatchableCommand<GeneralCommissioningTrait.CommissioningCompleteCommand.Response> {
    return BatchableCommand<GeneralCommissioningTrait.CommissioningCompleteCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GeneralCommissioningTrait.CommissioningCompleteCommand.requestId,
          requestAdapter = GeneralCommissioningTrait.CommissioningCompleteCommand.Request,
          request = GeneralCommissioningTrait.CommissioningCompleteCommand.Request(),
          useTimedCommand = false,
        ),
      responseAdapter = GeneralCommissioningTrait.CommissioningCompleteCommand.Response,
    )
  }

  override fun setTcAcknowledgementsBatchable(
    tcVersion: UShort,
    tcUserResponse: UShort,
  ): BatchableCommand<GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response> {
    return BatchableCommand<GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GeneralCommissioningTrait.SetTcAcknowledgementsCommand.requestId,
          requestAdapter = GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Request,
          request =
            GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Request(
              tcVersion,
              tcUserResponse,
            ),
          useTimedCommand = false,
        ),
      responseAdapter = GeneralCommissioningTrait.SetTcAcknowledgementsCommand.Response,
    )
  }

  override fun toString() = attributes.toString()
}
