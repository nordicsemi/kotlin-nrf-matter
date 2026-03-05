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
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.ApplyUpdateActionEnum
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.Attributes
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.AttributesImpl
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.DownloadProtocolEnum
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.QueryImageCommand
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateProviderTrait.StatusEnum

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the OtaSoftwareUpdateProvider trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface OtaSoftwareUpdateProviderCommands {
  suspend fun queryImage(
    vendorId: UShort,
    productId: UShort,
    softwareVersion: UInt,
    protocolsSupported: List<DownloadProtocolEnum>,
    optionalArgs: OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs.() -> Unit = {},
  ): OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response

  suspend fun applyUpdateRequest(
    updateToken: ByteArray,
    newVersion: UInt,
  ): OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response

  suspend fun notifyUpdateApplied(updateToken: ByteArray, softwareVersion: UInt)

  fun queryImageBatchable(
    vendorId: UShort,
    productId: UShort,
    softwareVersion: UInt,
    protocolsSupported: List<DownloadProtocolEnum>,
    optionalArgs: OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response>

  fun applyUpdateRequestBatchable(
    updateToken: ByteArray,
    newVersion: UInt,
  ): BatchableCommand<OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response>

  fun notifyUpdateAppliedBatchable(
    updateToken: ByteArray,
    softwareVersion: UInt,
  ): BatchableCommand<Unit>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface OtaSoftwareUpdateProviderCommandsDefaultImpl : OtaSoftwareUpdateProviderCommands {
  override suspend fun queryImage(
    vendorId: UShort,
    productId: UShort,
    softwareVersion: UInt,
    protocolsSupported: List<DownloadProtocolEnum>,
    optionalArgs: OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs.() -> Unit,
  ): OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun applyUpdateRequest(
    updateToken: ByteArray,
    newVersion: UInt,
  ): OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun notifyUpdateApplied(updateToken: ByteArray, softwareVersion: UInt) {
    TODO("Not Implemented")
  }

  override fun queryImageBatchable(
    vendorId: UShort,
    productId: UShort,
    softwareVersion: UInt,
    protocolsSupported: List<DownloadProtocolEnum>,
    optionalArgs: OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response> {
    TODO("Not Implemented")
  }

  override fun applyUpdateRequestBatchable(
    updateToken: ByteArray,
    newVersion: UInt,
  ): BatchableCommand<OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response> {
    TODO("Not Implemented")
  }

  override fun notifyUpdateAppliedBatchable(
    updateToken: ByteArray,
    softwareVersion: UInt,
  ): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }
}

/** API for the OtaSoftwareUpdateProvider trait. */
@Generated("GoogleHomePlatformCodegen")
interface OtaSoftwareUpdateProvider : Attributes, MatterTrait, OtaSoftwareUpdateProviderCommands {
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
     * The [generatedCommandList][OtaSoftwareUpdateProviderTrait.Attributes.generatedCommandList]
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
     * The [acceptedCommandList][OtaSoftwareUpdateProviderTrait.Attributes.acceptedCommandList]
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
     * The [attributeList][OtaSoftwareUpdateProviderTrait.Attributes.attributeList] trait attribute.
     */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][OtaSoftwareUpdateProviderTrait.Attributes.featureMap] trait attribute. */
    featureMap("featureMap", 65532u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /**
     * The [clusterRevision][OtaSoftwareUpdateProviderTrait.Attributes.clusterRevision] trait
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
    /** The [queryImage][OtaSoftwareUpdateProviderCommands.queryImage] trait command. */
    QueryImage(0u),
    /**
     * The [applyUpdateRequest][OtaSoftwareUpdateProviderCommands.applyUpdateRequest] trait command.
     */
    ApplyUpdateRequest(2u),
    /**
     * The [notifyUpdateApplied][OtaSoftwareUpdateProviderCommands.notifyUpdateApplied] trait
     * command.
     */
    NotifyUpdateApplied(4u),
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<OtaSoftwareUpdateProvider>(
      MatterTraitFactory(
        clusterId = OtaSoftwareUpdateProviderTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "ApplyUpdateActionEnum" to OtaSoftwareUpdateProviderTrait.ApplyUpdateActionEnum.Adapter,
            "DownloadProtocolEnum" to OtaSoftwareUpdateProviderTrait.DownloadProtocolEnum.Adapter,
            "StatusEnum" to OtaSoftwareUpdateProviderTrait.StatusEnum.Adapter,
          ),
        bitmapAdapters = mapOf<String, BitmapAdapter<*>>(),
        creator = ::OtaSoftwareUpdateProviderImpl,
        supportedEvents = mapOf(),
        // All Trait Commands
        commands =
          mapOf(
            OtaSoftwareUpdateProviderTrait.QueryImageCommand.requestId.toString() to
              QueryImageCommand,
            OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.requestId.toString() to
              ApplyUpdateRequestCommand,
            OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.requestId.toString() to
              NotifyUpdateAppliedCommand,
          ),
      )
    ) {
    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OtaSoftwareUpdateProviderTrait.Id.traitId,
          OtaSoftwareUpdateProvider.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OtaSoftwareUpdateProviderTrait.Id.traitId,
          OtaSoftwareUpdateProvider.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OtaSoftwareUpdateProviderTrait.Id.traitId,
          OtaSoftwareUpdateProvider.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<UInt>
      get() =
        AutomationAttribute<UInt>(
          OtaSoftwareUpdateProviderTrait.Id.traitId,
          OtaSoftwareUpdateProvider.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          OtaSoftwareUpdateProviderTrait.Id.traitId,
          OtaSoftwareUpdateProvider.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out OtaSoftwareUpdateProvider?>.generatedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<OtaSoftwareUpdateProvider, List<UInt>>(
          this,
          OtaSoftwareUpdateProvider.Attribute.generatedCommandList,
        )

    val TypedExpression<out OtaSoftwareUpdateProvider?>.acceptedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<OtaSoftwareUpdateProvider, List<UInt>>(
          this,
          OtaSoftwareUpdateProvider.Attribute.acceptedCommandList,
        )

    val TypedExpression<out OtaSoftwareUpdateProvider?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<OtaSoftwareUpdateProvider, List<UInt>>(
          this,
          OtaSoftwareUpdateProvider.Attribute.attributeList,
        )

    val TypedExpression<out OtaSoftwareUpdateProvider?>.featureMap: TypedExpression<UInt>
      get() =
        fieldSelect<OtaSoftwareUpdateProvider, UInt>(
          this,
          OtaSoftwareUpdateProvider.Attribute.featureMap,
        )

    val TypedExpression<out OtaSoftwareUpdateProvider?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<OtaSoftwareUpdateProvider, UShort>(
          this,
          OtaSoftwareUpdateProvider.Attribute.clusterRevision,
        )

    fun queryImage(
      vendorId: UShort,
      productId: UShort,
      softwareVersion: UInt,
      protocolsSupported: List<DownloadProtocolEnum>,
      optionalArgs: OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = OtaSoftwareUpdateProviderTrait.QueryImageCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          QueryImageCommand.Request.CommandFields.vendorId to vendorId,
          QueryImageCommand.Request.CommandFields.productId to productId,
          QueryImageCommand.Request.CommandFields.softwareVersion to softwareVersion,
          QueryImageCommand.Request.CommandFields.protocolsSupported to protocolsSupported,
        )

      val optionalValues =
        object : OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs {
          private val presence = BooleanArray(4)

          override var hardwareVersion: UShort = 0u
            set(value) {
              presence[0] = true
              field = value
            }

          fun hardwareVersionAsOptional(): OptionalValue<UShort> =
            if (presence[0]) {
              OptionalValue.present(hardwareVersion)
            } else {
              OptionalValue.absent()
            }

          override var location: String = ""
            set(value) {
              presence[1] = true
              field = value
            }

          fun locationAsOptional(): OptionalValue<String> =
            if (presence[1]) {
              OptionalValue.present(location)
            } else {
              OptionalValue.absent()
            }

          override var requestorCanConsent: Boolean = false
            set(value) {
              presence[2] = true
              field = value
            }

          fun requestorCanConsentAsOptional(): OptionalValue<Boolean> =
            if (presence[2]) {
              OptionalValue.present(requestorCanConsent)
            } else {
              OptionalValue.absent()
            }

          override var metadataForProvider: ByteArray = ByteArray(0)
            set(value) {
              presence[3] = true
              field = value
            }

          fun metadataForProviderAsOptional(): OptionalValue<ByteArray> =
            if (presence[3]) {
              OptionalValue.present(metadataForProvider)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.hardwareVersionAsOptional().doWhenPresent {
        paramsMap.put(QueryImageCommand.Request.CommandFields.hardwareVersion, it)
      }
      optionalValues.locationAsOptional().doWhenPresent {
        paramsMap.put(QueryImageCommand.Request.CommandFields.location, it)
      }
      optionalValues.requestorCanConsentAsOptional().doWhenPresent {
        paramsMap.put(QueryImageCommand.Request.CommandFields.requestorCanConsent, it)
      }
      optionalValues.metadataForProviderAsOptional().doWhenPresent {
        paramsMap.put(QueryImageCommand.Request.CommandFields.metadataForProvider, it)
      }

      return AutomationCommand(OtaSoftwareUpdateProvider, commandId, paramsMap)
    }

    fun applyUpdateRequest(updateToken: ByteArray, newVersion: UInt): AutomationCommand {
      val commandId = OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          ApplyUpdateRequestCommand.Request.CommandFields.updateToken to updateToken,
          ApplyUpdateRequestCommand.Request.CommandFields.newVersion to newVersion,
        )

      return AutomationCommand(OtaSoftwareUpdateProvider, commandId, paramsMap)
    }

    fun notifyUpdateApplied(updateToken: ByteArray, softwareVersion: UInt): AutomationCommand {
      val commandId = OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          NotifyUpdateAppliedCommand.Request.CommandFields.updateToken to updateToken,
          NotifyUpdateAppliedCommand.Request.CommandFields.softwareVersion to softwareVersion,
        )

      return AutomationCommand(OtaSoftwareUpdateProvider, commandId, paramsMap)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "OtaSoftwareUpdateProvider"
  }

  override val factory: TraitFactory<OtaSoftwareUpdateProvider>
    get() = Companion
}

/** @suppress */
class OtaSoftwareUpdateProviderImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) : OtaSoftwareUpdateProvider, MatterTraitImpl(metadata, client), Attributes by attributes {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OtaSoftwareUpdateProviderImpl) return false

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
  override fun supports(attribute: OtaSoftwareUpdateProvider.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: OtaSoftwareUpdateProvider.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun queryImage(
    vendorId: UShort,
    productId: UShort,
    softwareVersion: UInt,
    protocolsSupported: List<DownloadProtocolEnum>,
    optionalArgs: OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs.() -> Unit,
  ): OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response {
    val optionalValues =
      object : OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs {
        private val presence = BooleanArray(4)
        override var hardwareVersion: UShort = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun hardwareVersionAsOptional(): OptionalValue<UShort> =
          if (presence[0]) {
            OptionalValue.present(hardwareVersion)
          } else {
            OptionalValue.absent()
          }

        override var location: String = ""
          set(value) {
            presence[1] = true
            field = value
          }

        fun locationAsOptional(): OptionalValue<String> =
          if (presence[1]) {
            OptionalValue.present(location)
          } else {
            OptionalValue.absent()
          }

        override var requestorCanConsent: Boolean = false
          set(value) {
            presence[2] = true
            field = value
          }

        fun requestorCanConsentAsOptional(): OptionalValue<Boolean> =
          if (presence[2]) {
            OptionalValue.present(requestorCanConsent)
          } else {
            OptionalValue.absent()
          }

        override var metadataForProvider: ByteArray = ByteArray(0)
          set(value) {
            presence[3] = true
            field = value
          }

        fun metadataForProviderAsOptional(): OptionalValue<ByteArray> =
          if (presence[3]) {
            OptionalValue.present(metadataForProvider)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = OtaSoftwareUpdateProviderTrait.QueryImageCommand.requestId,
      request =
        OtaSoftwareUpdateProviderTrait.QueryImageCommand.Request(
          vendorId,
          productId,
          softwareVersion,
          protocolsSupported,
          optionalValues.hardwareVersionAsOptional(),
          optionalValues.locationAsOptional(),
          optionalValues.requestorCanConsentAsOptional(),
          optionalValues.metadataForProviderAsOptional(),
        ),
      requestAdapter = OtaSoftwareUpdateProviderTrait.QueryImageCommand.Request,
      responseAdapter = OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun applyUpdateRequest(
    updateToken: ByteArray,
    newVersion: UInt,
  ): OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response {
    return sendCommand(
      commandId = OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.requestId,
      request =
        OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Request(updateToken, newVersion),
      requestAdapter = OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Request,
      responseAdapter = OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun notifyUpdateApplied(updateToken: ByteArray, softwareVersion: UInt) {
    sendCommand(
      commandId = OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.requestId,
      request =
        OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.Request(
          updateToken,
          softwareVersion,
        ),
      requestAdapter = OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.Request,
      useTimedCommand = false,
    )
  }

  // Commands

  override fun queryImageBatchable(
    vendorId: UShort,
    productId: UShort,
    softwareVersion: UInt,
    protocolsSupported: List<DownloadProtocolEnum>,
    optionalArgs: OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response> {
    val optionalValues =
      object : OtaSoftwareUpdateProviderTrait.QueryImageCommand.OptionalArgs {
        private val presence = BooleanArray(4)
        override var hardwareVersion: UShort = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun hardwareVersionAsOptional(): OptionalValue<UShort> =
          if (presence[0]) {
            OptionalValue.present(hardwareVersion)
          } else {
            OptionalValue.absent()
          }

        override var location: String = ""
          set(value) {
            presence[1] = true
            field = value
          }

        fun locationAsOptional(): OptionalValue<String> =
          if (presence[1]) {
            OptionalValue.present(location)
          } else {
            OptionalValue.absent()
          }

        override var requestorCanConsent: Boolean = false
          set(value) {
            presence[2] = true
            field = value
          }

        fun requestorCanConsentAsOptional(): OptionalValue<Boolean> =
          if (presence[2]) {
            OptionalValue.present(requestorCanConsent)
          } else {
            OptionalValue.absent()
          }

        override var metadataForProvider: ByteArray = ByteArray(0)
          set(value) {
            presence[3] = true
            field = value
          }

        fun metadataForProviderAsOptional(): OptionalValue<ByteArray> =
          if (presence[3]) {
            OptionalValue.present(metadataForProvider)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OtaSoftwareUpdateProviderTrait.QueryImageCommand.requestId,
          requestAdapter = OtaSoftwareUpdateProviderTrait.QueryImageCommand.Request,
          request =
            OtaSoftwareUpdateProviderTrait.QueryImageCommand.Request(
              vendorId,
              productId,
              softwareVersion,
              protocolsSupported,
              optionalValues.hardwareVersionAsOptional(),
              optionalValues.locationAsOptional(),
              optionalValues.requestorCanConsentAsOptional(),
              optionalValues.metadataForProviderAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = OtaSoftwareUpdateProviderTrait.QueryImageCommand.Response,
    )
  }

  override fun applyUpdateRequestBatchable(
    updateToken: ByteArray,
    newVersion: UInt,
  ): BatchableCommand<OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response> {
    return BatchableCommand<OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.requestId,
          requestAdapter = OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Request,
          request =
            OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Request(
              updateToken,
              newVersion,
            ),
          useTimedCommand = false,
        ),
      responseAdapter = OtaSoftwareUpdateProviderTrait.ApplyUpdateRequestCommand.Response,
    )
  }

  override fun notifyUpdateAppliedBatchable(
    updateToken: ByteArray,
    softwareVersion: UInt,
  ): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.requestId,
          requestAdapter = OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.Request,
          request =
            OtaSoftwareUpdateProviderTrait.NotifyUpdateAppliedCommand.Request(
              updateToken,
              softwareVersion,
            ),
          useTimedCommand = false,
        )
    )
  }

  override fun toString() = attributes.toString()
}
