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
import com.google.home.matter.serialization.OptionalValue
import com.google.home.toDescriptorMap
import javax.annotation.processing.Generated
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.AddNocCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.AddTrustedRootCertificateCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.AttestationRequestCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.Attributes
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.AttributesImpl
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.CertificateChainRequestCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.CertificateChainTypeEnum
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.CsrRequestCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.FabricDescriptorStruct
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.NocStruct
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.NodeOperationalCertStatusEnum
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.RemoveFabricCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.SetVidVerificationStatementCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.SignVidVerificationRequestCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.UpdateFabricLabelCommand
import no.nordicsemi.nrf.matter.OperationalCredentialsTrait.UpdateNocCommand

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the OperationalCredentials trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface OperationalCredentialsCommands {
  suspend fun attestationRequest(
    attestationNonce: ByteArray
  ): OperationalCredentialsTrait.AttestationRequestCommand.Response

  suspend fun certificateChainRequest(
    certificateType: CertificateChainTypeEnum
  ): OperationalCredentialsTrait.CertificateChainRequestCommand.Response

  suspend fun csrRequest(
    csrNonce: ByteArray,
    optionalArgs: OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs.() -> Unit = {},
  ): OperationalCredentialsTrait.CsrRequestCommand.Response

  suspend fun addNoc(
    nocValue: ByteArray,
    ipkValue: ByteArray,
    caseAdminSubject: ULong,
    adminVendorId: UShort,
    optionalArgs: OperationalCredentialsTrait.AddNocCommand.OptionalArgs.() -> Unit = {},
  ): OperationalCredentialsTrait.AddNocCommand.Response

  suspend fun updateNoc(
    nocValue: ByteArray,
    optionalArgs: OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs.() -> Unit = {},
  ): OperationalCredentialsTrait.UpdateNocCommand.Response

  suspend fun updateFabricLabel(
    label: String
  ): OperationalCredentialsTrait.UpdateFabricLabelCommand.Response

  suspend fun removeFabric(
    fabricIndex: UByte
  ): OperationalCredentialsTrait.RemoveFabricCommand.Response

  suspend fun addTrustedRootCertificate(rootCaCertificate: ByteArray)

  suspend fun setVidVerificationStatement(
    optionalArgs:
      OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs.() -> Unit =
      {}
  )

  suspend fun signVidVerificationRequest(
    fabricIndex: UByte,
    clientChallenge: ByteArray,
  ): OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response

  fun attestationRequestBatchable(
    attestationNonce: ByteArray
  ): BatchableCommand<OperationalCredentialsTrait.AttestationRequestCommand.Response>

  fun certificateChainRequestBatchable(
    certificateType: CertificateChainTypeEnum
  ): BatchableCommand<OperationalCredentialsTrait.CertificateChainRequestCommand.Response>

  fun csrRequestBatchable(
    csrNonce: ByteArray,
    optionalArgs: OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<OperationalCredentialsTrait.CsrRequestCommand.Response>

  fun addNocBatchable(
    nocValue: ByteArray,
    ipkValue: ByteArray,
    caseAdminSubject: ULong,
    adminVendorId: UShort,
    optionalArgs: OperationalCredentialsTrait.AddNocCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<OperationalCredentialsTrait.AddNocCommand.Response>

  fun updateNocBatchable(
    nocValue: ByteArray,
    optionalArgs: OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<OperationalCredentialsTrait.UpdateNocCommand.Response>

  fun updateFabricLabelBatchable(
    label: String
  ): BatchableCommand<OperationalCredentialsTrait.UpdateFabricLabelCommand.Response>

  fun removeFabricBatchable(
    fabricIndex: UByte
  ): BatchableCommand<OperationalCredentialsTrait.RemoveFabricCommand.Response>

  fun addTrustedRootCertificateBatchable(rootCaCertificate: ByteArray): BatchableCommand<Unit>

  fun setVidVerificationStatementBatchable(
    optionalArgs:
      OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs.() -> Unit =
      {}
  ): BatchableCommand<Unit>

  fun signVidVerificationRequestBatchable(
    fabricIndex: UByte,
    clientChallenge: ByteArray,
  ): BatchableCommand<OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface OperationalCredentialsCommandsDefaultImpl : OperationalCredentialsCommands {
  override suspend fun attestationRequest(
    attestationNonce: ByteArray
  ): OperationalCredentialsTrait.AttestationRequestCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun certificateChainRequest(
    certificateType: CertificateChainTypeEnum
  ): OperationalCredentialsTrait.CertificateChainRequestCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun csrRequest(
    csrNonce: ByteArray,
    optionalArgs: OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs.() -> Unit,
  ): OperationalCredentialsTrait.CsrRequestCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun addNoc(
    nocValue: ByteArray,
    ipkValue: ByteArray,
    caseAdminSubject: ULong,
    adminVendorId: UShort,
    optionalArgs: OperationalCredentialsTrait.AddNocCommand.OptionalArgs.() -> Unit,
  ): OperationalCredentialsTrait.AddNocCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun updateNoc(
    nocValue: ByteArray,
    optionalArgs: OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs.() -> Unit,
  ): OperationalCredentialsTrait.UpdateNocCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun updateFabricLabel(
    label: String
  ): OperationalCredentialsTrait.UpdateFabricLabelCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun removeFabric(
    fabricIndex: UByte
  ): OperationalCredentialsTrait.RemoveFabricCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun addTrustedRootCertificate(rootCaCertificate: ByteArray) {
    TODO("Not Implemented")
  }

  override suspend fun setVidVerificationStatement(
    optionalArgs:
      OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs.() -> Unit
  ) {
    TODO("Not Implemented")
  }

  override suspend fun signVidVerificationRequest(
    fabricIndex: UByte,
    clientChallenge: ByteArray,
  ): OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response {
    TODO("Not Implemented")
  }

  override fun attestationRequestBatchable(
    attestationNonce: ByteArray
  ): BatchableCommand<OperationalCredentialsTrait.AttestationRequestCommand.Response> {
    TODO("Not Implemented")
  }

  override fun certificateChainRequestBatchable(
    certificateType: CertificateChainTypeEnum
  ): BatchableCommand<OperationalCredentialsTrait.CertificateChainRequestCommand.Response> {
    TODO("Not Implemented")
  }

  override fun csrRequestBatchable(
    csrNonce: ByteArray,
    optionalArgs: OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OperationalCredentialsTrait.CsrRequestCommand.Response> {
    TODO("Not Implemented")
  }

  override fun addNocBatchable(
    nocValue: ByteArray,
    ipkValue: ByteArray,
    caseAdminSubject: ULong,
    adminVendorId: UShort,
    optionalArgs: OperationalCredentialsTrait.AddNocCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OperationalCredentialsTrait.AddNocCommand.Response> {
    TODO("Not Implemented")
  }

  override fun updateNocBatchable(
    nocValue: ByteArray,
    optionalArgs: OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OperationalCredentialsTrait.UpdateNocCommand.Response> {
    TODO("Not Implemented")
  }

  override fun updateFabricLabelBatchable(
    label: String
  ): BatchableCommand<OperationalCredentialsTrait.UpdateFabricLabelCommand.Response> {
    TODO("Not Implemented")
  }

  override fun removeFabricBatchable(
    fabricIndex: UByte
  ): BatchableCommand<OperationalCredentialsTrait.RemoveFabricCommand.Response> {
    TODO("Not Implemented")
  }

  override fun addTrustedRootCertificateBatchable(
    rootCaCertificate: ByteArray
  ): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }

  override fun setVidVerificationStatementBatchable(
    optionalArgs:
      OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs.() -> Unit
  ): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }

  override fun signVidVerificationRequestBatchable(
    fabricIndex: UByte,
    clientChallenge: ByteArray,
  ): BatchableCommand<OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response> {
    TODO("Not Implemented")
  }
}

/** API for the OperationalCredentials trait. */
@Generated("GoogleHomePlatformCodegen")
interface OperationalCredentials : Attributes, MatterTrait, OperationalCredentialsCommands {
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
    /** The [nocs][OperationalCredentialsTrait.Attributes.nocs] trait attribute. */
    nocs("nocs", 0u, "NocStruct", FieldType.Struct, false, NocStruct.Adapter, false),
    /** The [fabrics][OperationalCredentialsTrait.Attributes.fabrics] trait attribute. */
    fabrics(
      "fabrics",
      1u,
      "FabricDescriptorStruct",
      FieldType.Struct,
      false,
      FabricDescriptorStruct.Adapter,
      false,
    ),
    /**
     * The [supportedFabrics][OperationalCredentialsTrait.Attributes.supportedFabrics] trait
     * attribute.
     */
    supportedFabrics(
      "supportedFabrics",
      2u,
      "UByte",
      FieldType.UByte,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [commissionedFabrics][OperationalCredentialsTrait.Attributes.commissionedFabrics] trait
     * attribute.
     */
    commissionedFabrics(
      "commissionedFabrics",
      3u,
      "UByte",
      FieldType.UByte,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [trustedRootCertificates][OperationalCredentialsTrait.Attributes.trustedRootCertificates]
     * trait attribute.
     */
    trustedRootCertificates(
      "trustedRootCertificates",
      4u,
      "ByteArray",
      FieldType.ByteArray,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [currentFabricIndex][OperationalCredentialsTrait.Attributes.currentFabricIndex] trait
     * attribute.
     */
    currentFabricIndex(
      "currentFabricIndex",
      5u,
      "UByte",
      FieldType.UByte,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [generatedCommandList][OperationalCredentialsTrait.Attributes.generatedCommandList] trait
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
     * The [acceptedCommandList][OperationalCredentialsTrait.Attributes.acceptedCommandList] trait
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
    /**
     * The [attributeList][OperationalCredentialsTrait.Attributes.attributeList] trait attribute.
     */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][OperationalCredentialsTrait.Attributes.featureMap] trait attribute. */
    featureMap("featureMap", 65532u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /**
     * The [clusterRevision][OperationalCredentialsTrait.Attributes.clusterRevision] trait
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
              nocs = fields[nocs] as List<NocStruct>?,
              fabrics = fields[fabrics] as List<FabricDescriptorStruct>?,
              supportedFabrics = fields[supportedFabrics] as UByte?,
              commissionedFabrics = fields[commissionedFabrics] as UByte?,
              trustedRootCertificates = fields[trustedRootCertificates] as List<ByteArray>?,
              currentFabricIndex = fields[currentFabricIndex] as UByte?,
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
    /**
     * The [attestationRequest][OperationalCredentialsCommands.attestationRequest] trait command.
     */
    AttestationRequest(0u),
    /**
     * The [certificateChainRequest][OperationalCredentialsCommands.certificateChainRequest] trait
     * command.
     */
    CertificateChainRequest(2u),
    /** The [csrRequest][OperationalCredentialsCommands.csrRequest] trait command. */
    CsrRequest(4u),
    /** The [addNoc][OperationalCredentialsCommands.addNoc] trait command. */
    AddNoc(6u),
    /** The [updateNoc][OperationalCredentialsCommands.updateNoc] trait command. */
    UpdateNoc(7u),
    /** The [updateFabricLabel][OperationalCredentialsCommands.updateFabricLabel] trait command. */
    UpdateFabricLabel(9u),
    /** The [removeFabric][OperationalCredentialsCommands.removeFabric] trait command. */
    RemoveFabric(10u),
    /**
     * The [addTrustedRootCertificate][OperationalCredentialsCommands.addTrustedRootCertificate]
     * trait command.
     */
    AddTrustedRootCertificate(11u),
    /**
     * The [setVIDVerificationStatement][OperationalCredentialsCommands.setVIDVerificationStatement]
     * trait command.
     */
    SetVidVerificationStatement(12u),
    /**
     * The [signVIDVerificationRequest][OperationalCredentialsCommands.signVIDVerificationRequest]
     * trait command.
     */
    SignVidVerificationRequest(13u),
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<OperationalCredentials>(
      MatterTraitFactory(
        clusterId = OperationalCredentialsTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "CertificateChainTypeEnum" to
              OperationalCredentialsTrait.CertificateChainTypeEnum.Adapter,
            "NodeOperationalCertStatusEnum" to
              OperationalCredentialsTrait.NodeOperationalCertStatusEnum.Adapter,
          ),
        bitmapAdapters = mapOf<String, BitmapAdapter<*>>(),
        creator = ::OperationalCredentialsImpl,
        supportedEvents = mapOf(),
        // All Trait Commands
        commands =
          mapOf(
            OperationalCredentialsTrait.AttestationRequestCommand.requestId.toString() to
              AttestationRequestCommand,
            OperationalCredentialsTrait.CertificateChainRequestCommand.requestId.toString() to
              CertificateChainRequestCommand,
            OperationalCredentialsTrait.CsrRequestCommand.requestId.toString() to CsrRequestCommand,
            OperationalCredentialsTrait.AddNocCommand.requestId.toString() to AddNocCommand,
            OperationalCredentialsTrait.UpdateNocCommand.requestId.toString() to UpdateNocCommand,
            OperationalCredentialsTrait.UpdateFabricLabelCommand.requestId.toString() to
              UpdateFabricLabelCommand,
            OperationalCredentialsTrait.RemoveFabricCommand.requestId.toString() to
              RemoveFabricCommand,
            OperationalCredentialsTrait.AddTrustedRootCertificateCommand.requestId.toString() to
              AddTrustedRootCertificateCommand,
            OperationalCredentialsTrait.SetVidVerificationStatementCommand.requestId.toString() to
              SetVidVerificationStatementCommand,
            OperationalCredentialsTrait.SignVidVerificationRequestCommand.requestId.toString() to
              SignVidVerificationRequestCommand,
          ),
      )
    ) {
    val nocs: AutomationAttribute<List<NocStruct>?>
      get() =
        AutomationAttribute<List<NocStruct>?>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.nocs.tag,
        )

    val fabrics: AutomationAttribute<List<FabricDescriptorStruct>?>
      get() =
        AutomationAttribute<List<FabricDescriptorStruct>?>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.fabrics.tag,
        )

    val supportedFabrics: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.supportedFabrics.tag,
        )

    val commissionedFabrics: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.commissionedFabrics.tag,
        )

    val trustedRootCertificates: AutomationAttribute<List<ByteArray>?>
      get() =
        AutomationAttribute<List<ByteArray>?>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.trustedRootCertificates.tag,
        )

    val currentFabricIndex: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.currentFabricIndex.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<UInt>
      get() =
        AutomationAttribute<UInt>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          OperationalCredentialsTrait.Id.traitId,
          OperationalCredentials.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out OperationalCredentials?>.nocs: TypedExpression<List<NocStruct>?>
      get() =
        fieldSelect<OperationalCredentials, List<NocStruct>?>(
          this,
          OperationalCredentials.Attribute.nocs,
        )

    val TypedExpression<out OperationalCredentials?>.fabrics:
      TypedExpression<List<FabricDescriptorStruct>?>
      get() =
        fieldSelect<OperationalCredentials, List<FabricDescriptorStruct>?>(
          this,
          OperationalCredentials.Attribute.fabrics,
        )

    val TypedExpression<out OperationalCredentials?>.supportedFabrics: TypedExpression<UByte?>
      get() =
        fieldSelect<OperationalCredentials, UByte?>(
          this,
          OperationalCredentials.Attribute.supportedFabrics,
        )

    val TypedExpression<out OperationalCredentials?>.commissionedFabrics: TypedExpression<UByte?>
      get() =
        fieldSelect<OperationalCredentials, UByte?>(
          this,
          OperationalCredentials.Attribute.commissionedFabrics,
        )

    val TypedExpression<out OperationalCredentials?>.trustedRootCertificates:
      TypedExpression<List<ByteArray>?>
      get() =
        fieldSelect<OperationalCredentials, List<ByteArray>?>(
          this,
          OperationalCredentials.Attribute.trustedRootCertificates,
        )

    val TypedExpression<out OperationalCredentials?>.currentFabricIndex: TypedExpression<UByte?>
      get() =
        fieldSelect<OperationalCredentials, UByte?>(
          this,
          OperationalCredentials.Attribute.currentFabricIndex,
        )

    val TypedExpression<out OperationalCredentials?>.generatedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<OperationalCredentials, List<UInt>>(
          this,
          OperationalCredentials.Attribute.generatedCommandList,
        )

    val TypedExpression<out OperationalCredentials?>.acceptedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<OperationalCredentials, List<UInt>>(
          this,
          OperationalCredentials.Attribute.acceptedCommandList,
        )

    val TypedExpression<out OperationalCredentials?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<OperationalCredentials, List<UInt>>(
          this,
          OperationalCredentials.Attribute.attributeList,
        )

    val TypedExpression<out OperationalCredentials?>.featureMap: TypedExpression<UInt>
      get() =
        fieldSelect<OperationalCredentials, UInt>(this, OperationalCredentials.Attribute.featureMap)

    val TypedExpression<out OperationalCredentials?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<OperationalCredentials, UShort>(
          this,
          OperationalCredentials.Attribute.clusterRevision,
        )

    fun attestationRequest(attestationNonce: ByteArray): AutomationCommand {
      val commandId = OperationalCredentialsTrait.AttestationRequestCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          AttestationRequestCommand.Request.CommandFields.attestationNonce to attestationNonce
        )

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun certificateChainRequest(certificateType: CertificateChainTypeEnum): AutomationCommand {
      val commandId =
        OperationalCredentialsTrait.CertificateChainRequestCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          CertificateChainRequestCommand.Request.CommandFields.certificateType to certificateType
        )

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun csrRequest(
      csrNonce: ByteArray,
      optionalArgs: OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = OperationalCredentialsTrait.CsrRequestCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(CsrRequestCommand.Request.CommandFields.csrNonce to csrNonce)

      val optionalValues =
        object : OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var isForUpdateNoc: Boolean = false
            set(value) {
              presence[0] = true
              field = value
            }

          fun isForUpdateNocAsOptional(): OptionalValue<Boolean> =
            if (presence[0]) {
              OptionalValue.present(isForUpdateNoc)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.isForUpdateNocAsOptional().doWhenPresent {
        paramsMap.put(CsrRequestCommand.Request.CommandFields.isForUpdateNoc, it)
      }

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun addNoc(
      nocValue: ByteArray,
      ipkValue: ByteArray,
      caseAdminSubject: ULong,
      adminVendorId: UShort,
      optionalArgs: OperationalCredentialsTrait.AddNocCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = OperationalCredentialsTrait.AddNocCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          AddNocCommand.Request.CommandFields.nocValue to nocValue,
          AddNocCommand.Request.CommandFields.ipkValue to ipkValue,
          AddNocCommand.Request.CommandFields.caseAdminSubject to caseAdminSubject,
          AddNocCommand.Request.CommandFields.adminVendorId to adminVendorId,
        )

      val optionalValues =
        object : OperationalCredentialsTrait.AddNocCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var icacValue: ByteArray = ByteArray(0)
            set(value) {
              presence[0] = true
              field = value
            }

          fun icacValueAsOptional(): OptionalValue<ByteArray> =
            if (presence[0]) {
              OptionalValue.present(icacValue)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.icacValueAsOptional().doWhenPresent {
        paramsMap.put(AddNocCommand.Request.CommandFields.icacValue, it)
      }

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun updateNoc(
      nocValue: ByteArray,
      optionalArgs: OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = OperationalCredentialsTrait.UpdateNocCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(UpdateNocCommand.Request.CommandFields.nocValue to nocValue)

      val optionalValues =
        object : OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var icacValue: ByteArray = ByteArray(0)
            set(value) {
              presence[0] = true
              field = value
            }

          fun icacValueAsOptional(): OptionalValue<ByteArray> =
            if (presence[0]) {
              OptionalValue.present(icacValue)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.icacValueAsOptional().doWhenPresent {
        paramsMap.put(UpdateNocCommand.Request.CommandFields.icacValue, it)
      }

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun updateFabricLabel(label: String): AutomationCommand {
      val commandId = OperationalCredentialsTrait.UpdateFabricLabelCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(UpdateFabricLabelCommand.Request.CommandFields.label to label)

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun removeFabric(fabricIndex: UByte): AutomationCommand {
      val commandId = OperationalCredentialsTrait.RemoveFabricCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(RemoveFabricCommand.Request.CommandFields.fabricIndex to fabricIndex)

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun addTrustedRootCertificate(rootCaCertificate: ByteArray): AutomationCommand {
      val commandId =
        OperationalCredentialsTrait.AddTrustedRootCertificateCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          AddTrustedRootCertificateCommand.Request.CommandFields.rootCaCertificate to
            rootCaCertificate
        )

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun setVidVerificationStatement(
      optionalArgs:
        OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs.() -> Unit =
        {}
    ): AutomationCommand {
      val commandId =
        OperationalCredentialsTrait.SetVidVerificationStatementCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> = mutableMapOf()

      val optionalValues =
        object : OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs {
          private val presence = BooleanArray(3)

          override var vendorId: UShort = 0u
            set(value) {
              presence[0] = true
              field = value
            }

          fun vendorIdAsOptional(): OptionalValue<UShort> =
            if (presence[0]) {
              OptionalValue.present(vendorId)
            } else {
              OptionalValue.absent()
            }

          override var vidVerificationStatement: ByteArray = ByteArray(0)
            set(value) {
              presence[1] = true
              field = value
            }

          fun vidVerificationStatementAsOptional(): OptionalValue<ByteArray> =
            if (presence[1]) {
              OptionalValue.present(vidVerificationStatement)
            } else {
              OptionalValue.absent()
            }

          override var vvsc: ByteArray = ByteArray(0)
            set(value) {
              presence[2] = true
              field = value
            }

          fun vvscAsOptional(): OptionalValue<ByteArray> =
            if (presence[2]) {
              OptionalValue.present(vvsc)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.vendorIdAsOptional().doWhenPresent {
        paramsMap.put(SetVidVerificationStatementCommand.Request.CommandFields.vendorId, it)
      }
      optionalValues.vidVerificationStatementAsOptional().doWhenPresent {
        paramsMap.put(
          SetVidVerificationStatementCommand.Request.CommandFields.vidVerificationStatement,
          it,
        )
      }
      optionalValues.vvscAsOptional().doWhenPresent {
        paramsMap.put(SetVidVerificationStatementCommand.Request.CommandFields.vvsc, it)
      }

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    fun signVidVerificationRequest(
      fabricIndex: UByte,
      clientChallenge: ByteArray,
    ): AutomationCommand {
      val commandId =
        OperationalCredentialsTrait.SignVidVerificationRequestCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          SignVidVerificationRequestCommand.Request.CommandFields.fabricIndex to fabricIndex,
          SignVidVerificationRequestCommand.Request.CommandFields.clientChallenge to clientChallenge,
        )

      return AutomationCommand(OperationalCredentials, commandId, paramsMap)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "OperationalCredentials"
  }

  override val factory: TraitFactory<OperationalCredentials>
    get() = Companion
}

/** @suppress */
class OperationalCredentialsImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) : OperationalCredentials, MatterTraitImpl(metadata, client), Attributes by attributes {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OperationalCredentialsImpl) return false

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
  override fun supports(attribute: OperationalCredentials.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: OperationalCredentials.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun attestationRequest(
    attestationNonce: ByteArray
  ): OperationalCredentialsTrait.AttestationRequestCommand.Response {
    return sendCommand(
      commandId = OperationalCredentialsTrait.AttestationRequestCommand.requestId,
      request = OperationalCredentialsTrait.AttestationRequestCommand.Request(attestationNonce),
      requestAdapter = OperationalCredentialsTrait.AttestationRequestCommand.Request,
      responseAdapter = OperationalCredentialsTrait.AttestationRequestCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun certificateChainRequest(
    certificateType: CertificateChainTypeEnum
  ): OperationalCredentialsTrait.CertificateChainRequestCommand.Response {
    return sendCommand(
      commandId = OperationalCredentialsTrait.CertificateChainRequestCommand.requestId,
      request = OperationalCredentialsTrait.CertificateChainRequestCommand.Request(certificateType),
      requestAdapter = OperationalCredentialsTrait.CertificateChainRequestCommand.Request,
      responseAdapter = OperationalCredentialsTrait.CertificateChainRequestCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun csrRequest(
    csrNonce: ByteArray,
    optionalArgs: OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs.() -> Unit,
  ): OperationalCredentialsTrait.CsrRequestCommand.Response {
    val optionalValues =
      object : OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var isForUpdateNoc: Boolean = false
          set(value) {
            presence[0] = true
            field = value
          }

        fun isForUpdateNocAsOptional(): OptionalValue<Boolean> =
          if (presence[0]) {
            OptionalValue.present(isForUpdateNoc)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = OperationalCredentialsTrait.CsrRequestCommand.requestId,
      request =
        OperationalCredentialsTrait.CsrRequestCommand.Request(
          csrNonce,
          optionalValues.isForUpdateNocAsOptional(),
        ),
      requestAdapter = OperationalCredentialsTrait.CsrRequestCommand.Request,
      responseAdapter = OperationalCredentialsTrait.CsrRequestCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun addNoc(
    nocValue: ByteArray,
    ipkValue: ByteArray,
    caseAdminSubject: ULong,
    adminVendorId: UShort,
    optionalArgs: OperationalCredentialsTrait.AddNocCommand.OptionalArgs.() -> Unit,
  ): OperationalCredentialsTrait.AddNocCommand.Response {
    val optionalValues =
      object : OperationalCredentialsTrait.AddNocCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var icacValue: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun icacValueAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(icacValue)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = OperationalCredentialsTrait.AddNocCommand.requestId,
      request =
        OperationalCredentialsTrait.AddNocCommand.Request(
          nocValue,
          optionalValues.icacValueAsOptional(),
          ipkValue,
          caseAdminSubject,
          adminVendorId,
        ),
      requestAdapter = OperationalCredentialsTrait.AddNocCommand.Request,
      responseAdapter = OperationalCredentialsTrait.AddNocCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun updateNoc(
    nocValue: ByteArray,
    optionalArgs: OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs.() -> Unit,
  ): OperationalCredentialsTrait.UpdateNocCommand.Response {
    val optionalValues =
      object : OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var icacValue: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun icacValueAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(icacValue)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = OperationalCredentialsTrait.UpdateNocCommand.requestId,
      request =
        OperationalCredentialsTrait.UpdateNocCommand.Request(
          nocValue,
          optionalValues.icacValueAsOptional(),
        ),
      requestAdapter = OperationalCredentialsTrait.UpdateNocCommand.Request,
      responseAdapter = OperationalCredentialsTrait.UpdateNocCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun updateFabricLabel(
    label: String
  ): OperationalCredentialsTrait.UpdateFabricLabelCommand.Response {
    return sendCommand(
      commandId = OperationalCredentialsTrait.UpdateFabricLabelCommand.requestId,
      request = OperationalCredentialsTrait.UpdateFabricLabelCommand.Request(label),
      requestAdapter = OperationalCredentialsTrait.UpdateFabricLabelCommand.Request,
      responseAdapter = OperationalCredentialsTrait.UpdateFabricLabelCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun removeFabric(
    fabricIndex: UByte
  ): OperationalCredentialsTrait.RemoveFabricCommand.Response {
    return sendCommand(
      commandId = OperationalCredentialsTrait.RemoveFabricCommand.requestId,
      request = OperationalCredentialsTrait.RemoveFabricCommand.Request(fabricIndex),
      requestAdapter = OperationalCredentialsTrait.RemoveFabricCommand.Request,
      responseAdapter = OperationalCredentialsTrait.RemoveFabricCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun addTrustedRootCertificate(rootCaCertificate: ByteArray) {
    sendCommand(
      commandId = OperationalCredentialsTrait.AddTrustedRootCertificateCommand.requestId,
      request =
        OperationalCredentialsTrait.AddTrustedRootCertificateCommand.Request(rootCaCertificate),
      requestAdapter = OperationalCredentialsTrait.AddTrustedRootCertificateCommand.Request,
      useTimedCommand = false,
    )
  }

  override suspend fun setVidVerificationStatement(
    optionalArgs:
      OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs.() -> Unit
  ) {
    val optionalValues =
      object : OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs {
        private val presence = BooleanArray(3)
        override var vendorId: UShort = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun vendorIdAsOptional(): OptionalValue<UShort> =
          if (presence[0]) {
            OptionalValue.present(vendorId)
          } else {
            OptionalValue.absent()
          }

        override var vidVerificationStatement: ByteArray = ByteArray(0)
          set(value) {
            presence[1] = true
            field = value
          }

        fun vidVerificationStatementAsOptional(): OptionalValue<ByteArray> =
          if (presence[1]) {
            OptionalValue.present(vidVerificationStatement)
          } else {
            OptionalValue.absent()
          }

        override var vvsc: ByteArray = ByteArray(0)
          set(value) {
            presence[2] = true
            field = value
          }

        fun vvscAsOptional(): OptionalValue<ByteArray> =
          if (presence[2]) {
            OptionalValue.present(vvsc)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    sendCommand(
      commandId = OperationalCredentialsTrait.SetVidVerificationStatementCommand.requestId,
      request =
        OperationalCredentialsTrait.SetVidVerificationStatementCommand.Request(
          optionalValues.vendorIdAsOptional(),
          optionalValues.vidVerificationStatementAsOptional(),
          optionalValues.vvscAsOptional(),
        ),
      requestAdapter = OperationalCredentialsTrait.SetVidVerificationStatementCommand.Request,
      useTimedCommand = false,
    )
  }

  override suspend fun signVidVerificationRequest(
    fabricIndex: UByte,
    clientChallenge: ByteArray,
  ): OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response {
    return sendCommand(
      commandId = OperationalCredentialsTrait.SignVidVerificationRequestCommand.requestId,
      request =
        OperationalCredentialsTrait.SignVidVerificationRequestCommand.Request(
          fabricIndex,
          clientChallenge,
        ),
      requestAdapter = OperationalCredentialsTrait.SignVidVerificationRequestCommand.Request,
      responseAdapter = OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response,
      useTimedCommand = false,
    )
  }

  // Commands

  override fun attestationRequestBatchable(
    attestationNonce: ByteArray
  ): BatchableCommand<OperationalCredentialsTrait.AttestationRequestCommand.Response> {
    return BatchableCommand<OperationalCredentialsTrait.AttestationRequestCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.AttestationRequestCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.AttestationRequestCommand.Request,
          request = OperationalCredentialsTrait.AttestationRequestCommand.Request(attestationNonce),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.AttestationRequestCommand.Response,
    )
  }

  override fun certificateChainRequestBatchable(
    certificateType: CertificateChainTypeEnum
  ): BatchableCommand<OperationalCredentialsTrait.CertificateChainRequestCommand.Response> {
    return BatchableCommand<OperationalCredentialsTrait.CertificateChainRequestCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.CertificateChainRequestCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.CertificateChainRequestCommand.Request,
          request =
            OperationalCredentialsTrait.CertificateChainRequestCommand.Request(certificateType),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.CertificateChainRequestCommand.Response,
    )
  }

  override fun csrRequestBatchable(
    csrNonce: ByteArray,
    optionalArgs: OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OperationalCredentialsTrait.CsrRequestCommand.Response> {
    val optionalValues =
      object : OperationalCredentialsTrait.CsrRequestCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var isForUpdateNoc: Boolean = false
          set(value) {
            presence[0] = true
            field = value
          }

        fun isForUpdateNocAsOptional(): OptionalValue<Boolean> =
          if (presence[0]) {
            OptionalValue.present(isForUpdateNoc)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<OperationalCredentialsTrait.CsrRequestCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.CsrRequestCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.CsrRequestCommand.Request,
          request =
            OperationalCredentialsTrait.CsrRequestCommand.Request(
              csrNonce,
              optionalValues.isForUpdateNocAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.CsrRequestCommand.Response,
    )
  }

  override fun addNocBatchable(
    nocValue: ByteArray,
    ipkValue: ByteArray,
    caseAdminSubject: ULong,
    adminVendorId: UShort,
    optionalArgs: OperationalCredentialsTrait.AddNocCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OperationalCredentialsTrait.AddNocCommand.Response> {
    val optionalValues =
      object : OperationalCredentialsTrait.AddNocCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var icacValue: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun icacValueAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(icacValue)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<OperationalCredentialsTrait.AddNocCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.AddNocCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.AddNocCommand.Request,
          request =
            OperationalCredentialsTrait.AddNocCommand.Request(
              nocValue,
              optionalValues.icacValueAsOptional(),
              ipkValue,
              caseAdminSubject,
              adminVendorId,
            ),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.AddNocCommand.Response,
    )
  }

  override fun updateNocBatchable(
    nocValue: ByteArray,
    optionalArgs: OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OperationalCredentialsTrait.UpdateNocCommand.Response> {
    val optionalValues =
      object : OperationalCredentialsTrait.UpdateNocCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var icacValue: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun icacValueAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(icacValue)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<OperationalCredentialsTrait.UpdateNocCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.UpdateNocCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.UpdateNocCommand.Request,
          request =
            OperationalCredentialsTrait.UpdateNocCommand.Request(
              nocValue,
              optionalValues.icacValueAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.UpdateNocCommand.Response,
    )
  }

  override fun updateFabricLabelBatchable(
    label: String
  ): BatchableCommand<OperationalCredentialsTrait.UpdateFabricLabelCommand.Response> {
    return BatchableCommand<OperationalCredentialsTrait.UpdateFabricLabelCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.UpdateFabricLabelCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.UpdateFabricLabelCommand.Request,
          request = OperationalCredentialsTrait.UpdateFabricLabelCommand.Request(label),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.UpdateFabricLabelCommand.Response,
    )
  }

  override fun removeFabricBatchable(
    fabricIndex: UByte
  ): BatchableCommand<OperationalCredentialsTrait.RemoveFabricCommand.Response> {
    return BatchableCommand<OperationalCredentialsTrait.RemoveFabricCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.RemoveFabricCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.RemoveFabricCommand.Request,
          request = OperationalCredentialsTrait.RemoveFabricCommand.Request(fabricIndex),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.RemoveFabricCommand.Response,
    )
  }

  override fun addTrustedRootCertificateBatchable(
    rootCaCertificate: ByteArray
  ): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.AddTrustedRootCertificateCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.AddTrustedRootCertificateCommand.Request,
          request =
            OperationalCredentialsTrait.AddTrustedRootCertificateCommand.Request(rootCaCertificate),
          useTimedCommand = false,
        )
    )
  }

  override fun setVidVerificationStatementBatchable(
    optionalArgs:
      OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs.() -> Unit
  ): BatchableCommand<Unit> {
    val optionalValues =
      object : OperationalCredentialsTrait.SetVidVerificationStatementCommand.OptionalArgs {
        private val presence = BooleanArray(3)
        override var vendorId: UShort = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun vendorIdAsOptional(): OptionalValue<UShort> =
          if (presence[0]) {
            OptionalValue.present(vendorId)
          } else {
            OptionalValue.absent()
          }

        override var vidVerificationStatement: ByteArray = ByteArray(0)
          set(value) {
            presence[1] = true
            field = value
          }

        fun vidVerificationStatementAsOptional(): OptionalValue<ByteArray> =
          if (presence[1]) {
            OptionalValue.present(vidVerificationStatement)
          } else {
            OptionalValue.absent()
          }

        override var vvsc: ByteArray = ByteArray(0)
          set(value) {
            presence[2] = true
            field = value
          }

        fun vvscAsOptional(): OptionalValue<ByteArray> =
          if (presence[2]) {
            OptionalValue.present(vvsc)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.SetVidVerificationStatementCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.SetVidVerificationStatementCommand.Request,
          request =
            OperationalCredentialsTrait.SetVidVerificationStatementCommand.Request(
              optionalValues.vendorIdAsOptional(),
              optionalValues.vidVerificationStatementAsOptional(),
              optionalValues.vvscAsOptional(),
            ),
          useTimedCommand = false,
        )
    )
  }

  override fun signVidVerificationRequestBatchable(
    fabricIndex: UByte,
    clientChallenge: ByteArray,
  ): BatchableCommand<OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response> {
    return BatchableCommand<OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OperationalCredentialsTrait.SignVidVerificationRequestCommand.requestId,
          requestAdapter = OperationalCredentialsTrait.SignVidVerificationRequestCommand.Request,
          request =
            OperationalCredentialsTrait.SignVidVerificationRequestCommand.Request(
              fabricIndex,
              clientChallenge,
            ),
          useTimedCommand = false,
        ),
      responseAdapter = OperationalCredentialsTrait.SignVidVerificationRequestCommand.Response,
    )
  }

  override fun toString() = attributes.toString()
}
