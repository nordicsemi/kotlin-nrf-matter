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
import com.google.home.matter.serialization.OptionalValue
import com.google.home.toDescriptorMap
import java.time.Instant
import javax.annotation.processing.Generated
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.AnnouncementReasonEnum
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.Attributes
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.AttributesImpl
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.ChangeReasonEnum
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.DownloadError
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.MutableAttributes
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.ProviderLocation
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.StateTransition
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.UpdateStateEnum
import no.nordicsemi.nrf.matter.OtaSoftwareUpdateRequestorTrait.VersionApplied

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/** Commands for the OtaSoftwareUpdateRequestor trait. */
@Generated("GoogleHomePlatformCodegen")
interface OtaSoftwareUpdateRequestorCommands {

  /**
   * Announce the presence of a particular OTA provider.
   *
   * @param announcementReason The reason for the announcement.
   * @param endpoint The endpoint ID of the node that implements the OTA Provider cluster.
   * @param optionalArgs Receiver for the optional arguments of this command
   */
  suspend fun announceOtaProvider(
    providerNodeId: ULong,
    vendorId: UShort,
    announcementReason: AnnouncementReasonEnum,
    endpoint: UShort,
    optionalArgs:
      OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs.() -> Unit =
      {},
  )

  /**
   * The batchable version of [announceOtaProvider] command.
   *
   * Announce the presence of a particular OTA provider.
   *
   * @param announcementReason The reason for the announcement.
   * @param endpoint The endpoint ID of the node that implements the OTA Provider cluster.
   * @param optionalArgs Receiver for the optional arguments of this command
   * @return BatchableCommand<Unit>
   */
  fun announceOtaProviderBatchable(
    providerNodeId: ULong,
    vendorId: UShort,
    announcementReason: AnnouncementReasonEnum,
    endpoint: UShort,
    optionalArgs:
      OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs.() -> Unit =
      {},
  ): BatchableCommand<Unit>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface OtaSoftwareUpdateRequestorCommandsDefaultImpl : OtaSoftwareUpdateRequestorCommands {
  override suspend fun announceOtaProvider(
    providerNodeId: ULong,
    vendorId: UShort,
    announcementReason: AnnouncementReasonEnum,
    endpoint: UShort,
    optionalArgs: OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs.() -> Unit,
  ) {
    TODO("Not Implemented")
  }

  override fun announceOtaProviderBatchable(
    providerNodeId: ULong,
    vendorId: UShort,
    announcementReason: AnnouncementReasonEnum,
    endpoint: UShort,
    optionalArgs: OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }
}

/**
 * API for the OtaSoftwareUpdateRequestor trait. This trait provides an interface to a device that
 * is capable of receiving an OTA software update.
 */
@Generated("GoogleHomePlatformCodegen")
interface OtaSoftwareUpdateRequestor :
  Attributes,
  MatterTrait,
  Updatable<OtaSoftwareUpdateRequestor, MutableAttributes>,
  OtaSoftwareUpdateRequestorCommands {
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
     * The [defaultOtaProviders][OtaSoftwareUpdateRequestorTrait.Attributes.defaultOtaProviders]
     * trait attribute.
     */
    defaultOtaProviders(
      "defaultOtaProviders",
      0u,
      "ProviderLocation",
      FieldType.Struct,
      false,
      ProviderLocation.Adapter,
      false,
    ),
    /**
     * The [updatePossible][OtaSoftwareUpdateRequestorTrait.Attributes.updatePossible] trait
     * attribute.
     */
    updatePossible(
      "updatePossible",
      1u,
      "Boolean",
      FieldType.Boolean,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [updateState][OtaSoftwareUpdateRequestorTrait.Attributes.updateState] trait attribute.
     */
    updateState(
      "updateState",
      2u,
      "UpdateStateEnum",
      FieldType.Enum,
      false,
      UpdateStateEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [updateStateProgress][OtaSoftwareUpdateRequestorTrait.Attributes.updateStateProgress]
     * trait attribute.
     */
    updateStateProgress(
      "updateStateProgress",
      3u,
      "UByte",
      FieldType.UByte,
      false,
      NoOpDescriptor,
      true,
    ),
    /**
     * The [generatedCommandList][OtaSoftwareUpdateRequestorTrait.Attributes.generatedCommandList]
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
     * The [acceptedCommandList][OtaSoftwareUpdateRequestorTrait.Attributes.acceptedCommandList]
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
     * The [attributeList][OtaSoftwareUpdateRequestorTrait.Attributes.attributeList] trait
     * attribute.
     */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][OtaSoftwareUpdateRequestorTrait.Attributes.featureMap] trait attribute. */
    featureMap("featureMap", 65532u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /**
     * The [clusterRevision][OtaSoftwareUpdateRequestorTrait.Attributes.clusterRevision] trait
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
              defaultOtaProviders = fields[defaultOtaProviders] as List<ProviderLocation>?,
              updatePossible = fields[updatePossible] as Boolean?,
              updateState = fields[updateState] as UpdateStateEnum?,
              updateStateProgress = fields[updateStateProgress] as UByte?,
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
     * The [announceOtaProvider][OtaSoftwareUpdateRequestorCommands.announceOtaProvider] trait
     * command.
     */
    AnnounceOtaProvider(0u)
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<OtaSoftwareUpdateRequestor>(
      MatterTraitFactory(
        clusterId = OtaSoftwareUpdateRequestorTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "AnnouncementReasonEnum" to
              OtaSoftwareUpdateRequestorTrait.AnnouncementReasonEnum.Adapter,
            "ChangeReasonEnum" to OtaSoftwareUpdateRequestorTrait.ChangeReasonEnum.Adapter,
            "UpdateStateEnum" to OtaSoftwareUpdateRequestorTrait.UpdateStateEnum.Adapter,
          ),
        bitmapAdapters = mapOf<String, BitmapAdapter<*>>(),
        creator = ::OtaSoftwareUpdateRequestorImpl,
        supportedEvents =
          mapOf(
            OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Id.toString() to
              OtaSoftwareUpdateRequestor.StateTransitionEvent,
            OtaSoftwareUpdateRequestorTrait.VersionAppliedImpl.Id.toString() to
              OtaSoftwareUpdateRequestor.VersionAppliedEvent,
            OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Id.toString() to
              OtaSoftwareUpdateRequestor.DownloadErrorEvent,
          ),
        // All Trait Commands
        commands =
          mapOf(
            OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.requestId.toString() to
              AnnounceOtaProviderCommand
          ),
      )
    ) {
    val defaultOtaProviders: AutomationAttribute<List<ProviderLocation>?>
      get() =
        AutomationAttribute<List<ProviderLocation>?>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.defaultOtaProviders.tag,
        )

    val updatePossible: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.updatePossible.tag,
        )

    val updateState: AutomationAttribute<UpdateStateEnum?>
      get() =
        AutomationAttribute<UpdateStateEnum?>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.updateState.tag,
        )

    val updateStateProgress: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.updateStateProgress.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<UInt>
      get() =
        AutomationAttribute<UInt>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          OtaSoftwareUpdateRequestorTrait.Id.traitId,
          OtaSoftwareUpdateRequestor.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.defaultOtaProviders:
      TypedExpression<List<ProviderLocation>?>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, List<ProviderLocation>?>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.defaultOtaProviders,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.updatePossible: TypedExpression<Boolean?>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, Boolean?>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.updatePossible,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.updateState:
      TypedExpression<UpdateStateEnum?>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, UpdateStateEnum?>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.updateState,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.updateStateProgress:
      TypedExpression<UByte?>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, UByte?>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.updateStateProgress,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.generatedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, List<UInt>>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.generatedCommandList,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.acceptedCommandList:
      TypedExpression<List<UInt>>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, List<UInt>>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.acceptedCommandList,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, List<UInt>>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.attributeList,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.featureMap: TypedExpression<UInt>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, UInt>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.featureMap,
        )

    val TypedExpression<out OtaSoftwareUpdateRequestor?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<OtaSoftwareUpdateRequestor, UShort>(
          this,
          OtaSoftwareUpdateRequestor.Attribute.clusterRevision,
        )

    fun Updater<OtaSoftwareUpdateRequestor>.setDefaultOtaProviders(value: List<ProviderLocation>) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.defaultOtaProviders, value))
    }

    /**
     * Announce the presence of a particular OTA provider.
     *
     * @param announcementReason The reason for the announcement.
     * @param endpoint The endpoint ID of the node that implements the OTA Provider cluster.
     * @param optionalArgs Receiver for the optional arguments of this command
     */
    fun announceOtaProvider(
      providerNodeId: ULong,
      vendorId: UShort,
      announcementReason: AnnouncementReasonEnum,
      endpoint: UShort,
      optionalArgs:
        OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs.() -> Unit =
        {},
    ): AutomationCommand {
      val commandId =
        OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          AnnounceOtaProviderCommand.Request.CommandFields.providerNodeId to providerNodeId,
          AnnounceOtaProviderCommand.Request.CommandFields.vendorId to vendorId,
          AnnounceOtaProviderCommand.Request.CommandFields.announcementReason to announcementReason,
          AnnounceOtaProviderCommand.Request.CommandFields.endpoint to endpoint,
        )

      val optionalValues =
        object : OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var metadataForNode: ByteArray = ByteArray(0)
            set(value) {
              presence[0] = true
              field = value
            }

          fun metadataForNodeAsOptional(): OptionalValue<ByteArray> =
            if (presence[0]) {
              OptionalValue.present(metadataForNode)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.metadataForNodeAsOptional().doWhenPresent {
        paramsMap.put(AnnounceOtaProviderCommand.Request.CommandFields.metadataForNode, it)
      }

      return AutomationCommand(OtaSoftwareUpdateRequestor, commandId, paramsMap)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "OtaSoftwareUpdateRequestor"
  }

  override val factory: TraitFactory<OtaSoftwareUpdateRequestor>
    get() = Companion

  // Events
  class StateTransitionEvent
  private constructor(
    override val eventName: String = "StateTransition",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: OtaSoftwareUpdateRequestorTrait.StateTransition,
  ) : Event, OtaSoftwareUpdateRequestorTrait.StateTransition by eventPayload {

    override val eventId: Id = Id(OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is StateTransitionEvent) return false
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
      /** The [previousState][StateTransitionEvent.Attributes.previousState] event field. */
      previousState(
        "previousState",
        0u,
        "UpdateStateEnum",
        FieldType.Enum,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [newState][StateTransitionEvent.Attributes.newState] event field. */
      newState("newState", 1u, "UpdateStateEnum", FieldType.Enum, false, NoOpDescriptor, false),
      /** The [reason][StateTransitionEvent.Attributes.reason] event field. */
      reason("reason", 2u, "ChangeReasonEnum", FieldType.Enum, false, NoOpDescriptor, false),
      /**
       * The [targetSoftwareVersion][StateTransitionEvent.Attributes.targetSoftwareVersion] event
       * field.
       */
      targetSoftwareVersion(
        "targetSoftwareVersion",
        3u,
        "UInt",
        FieldType.UInt,
        false,
        NoOpDescriptor,
        true,
      );

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return OtaSoftwareUpdateRequestorTrait.StateTransitionImpl(
                previousState = fields[EventFields.previousState] as UpdateStateEnum?,
                newState = fields[EventFields.newState] as UpdateStateEnum?,
                reason = fields[EventFields.reason] as ChangeReasonEnum?,
                targetSoftwareVersion = fields[EventFields.targetSoftwareVersion] as UInt?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<StateTransitionEvent>(
        MatterEventFactory(
          OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Id,
          "StateTransition",
          OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Adapter,
          ::StateTransitionEvent,
        )
      ) {
      val previousState: EventField<UpdateStateEnum?>
        get() =
          EventField<UpdateStateEnum?>(
            OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Id.traitId,
            0u,
          )

      val newState: EventField<UpdateStateEnum?>
        get() =
          EventField<UpdateStateEnum?>(
            OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Id.traitId,
            1u,
          )

      val reason: EventField<ChangeReasonEnum?>
        get() =
          EventField<ChangeReasonEnum?>(
            OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Id.traitId,
            2u,
          )

      val targetSoftwareVersion: EventField<UInt?>
        get() =
          EventField<UInt?>(OtaSoftwareUpdateRequestorTrait.StateTransitionImpl.Id.traitId, 3u)

      val TypedExpression<out StateTransitionEvent?>.previousState:
        TypedExpression<UpdateStateEnum?>
        get() = fieldSelect<StateTransitionEvent, UpdateStateEnum?>(this, EventFields.previousState)

      val TypedExpression<out StateTransitionEvent?>.newState: TypedExpression<UpdateStateEnum?>
        get() = fieldSelect<StateTransitionEvent, UpdateStateEnum?>(this, EventFields.newState)

      val TypedExpression<out StateTransitionEvent?>.reason: TypedExpression<ChangeReasonEnum?>
        get() = fieldSelect<StateTransitionEvent, ChangeReasonEnum?>(this, EventFields.reason)

      val TypedExpression<out StateTransitionEvent?>.targetSoftwareVersion: TypedExpression<UInt?>
        get() = fieldSelect<StateTransitionEvent, UInt?>(this, EventFields.targetSoftwareVersion)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class VersionAppliedEvent
  private constructor(
    override val eventName: String = "VersionApplied",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: OtaSoftwareUpdateRequestorTrait.VersionApplied,
  ) : Event, OtaSoftwareUpdateRequestorTrait.VersionApplied by eventPayload {

    override val eventId: Id = Id(OtaSoftwareUpdateRequestorTrait.VersionAppliedImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is VersionAppliedEvent) return false
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
      /** The [softwareVersion][VersionAppliedEvent.Attributes.softwareVersion] event field. */
      softwareVersion("softwareVersion", 0u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
      /** The [productId][VersionAppliedEvent.Attributes.productId] event field. */
      productId("productId", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return OtaSoftwareUpdateRequestorTrait.VersionAppliedImpl(
                softwareVersion = fields[EventFields.softwareVersion] as UInt?,
                productId = fields[EventFields.productId] as UShort?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<VersionAppliedEvent>(
        MatterEventFactory(
          OtaSoftwareUpdateRequestorTrait.VersionAppliedImpl.Id,
          "VersionApplied",
          OtaSoftwareUpdateRequestorTrait.VersionAppliedImpl.Adapter,
          ::VersionAppliedEvent,
        )
      ) {
      val softwareVersion: EventField<UInt?>
        get() = EventField<UInt?>(OtaSoftwareUpdateRequestorTrait.VersionAppliedImpl.Id.traitId, 0u)

      val productId: EventField<UShort?>
        get() =
          EventField<UShort?>(OtaSoftwareUpdateRequestorTrait.VersionAppliedImpl.Id.traitId, 1u)

      val TypedExpression<out VersionAppliedEvent?>.softwareVersion: TypedExpression<UInt?>
        get() = fieldSelect<VersionAppliedEvent, UInt?>(this, EventFields.softwareVersion)

      val TypedExpression<out VersionAppliedEvent?>.productId: TypedExpression<UShort?>
        get() = fieldSelect<VersionAppliedEvent, UShort?>(this, EventFields.productId)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class DownloadErrorEvent
  private constructor(
    override val eventName: String = "DownloadError",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: OtaSoftwareUpdateRequestorTrait.DownloadError,
  ) : Event, OtaSoftwareUpdateRequestorTrait.DownloadError by eventPayload {

    override val eventId: Id = Id(OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is DownloadErrorEvent) return false
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
      /** The [softwareVersion][DownloadErrorEvent.Attributes.softwareVersion] event field. */
      softwareVersion("softwareVersion", 0u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
      /** The [bytesDownloaded][DownloadErrorEvent.Attributes.bytesDownloaded] event field. */
      bytesDownloaded(
        "bytesDownloaded",
        1u,
        "ULong",
        FieldType.ULong,
        false,
        NoOpDescriptor,
        false,
      ),
      /** The [progressPercent][DownloadErrorEvent.Attributes.progressPercent] event field. */
      progressPercent("progressPercent", 2u, "UByte", FieldType.UByte, false, NoOpDescriptor, true),
      /** The [platformCode][DownloadErrorEvent.Attributes.platformCode] event field. */
      platformCode("platformCode", 3u, "Long", FieldType.Long, false, NoOpDescriptor, true);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl(
                softwareVersion = fields[EventFields.softwareVersion] as UInt?,
                bytesDownloaded = fields[EventFields.bytesDownloaded] as ULong?,
                progressPercent = fields[EventFields.progressPercent] as UByte?,
                platformCode = fields[EventFields.platformCode] as Long?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<DownloadErrorEvent>(
        MatterEventFactory(
          OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Id,
          "DownloadError",
          OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Adapter,
          ::DownloadErrorEvent,
        )
      ) {
      val softwareVersion: EventField<UInt?>
        get() = EventField<UInt?>(OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Id.traitId, 0u)

      val bytesDownloaded: EventField<ULong?>
        get() = EventField<ULong?>(OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Id.traitId, 1u)

      val progressPercent: EventField<UByte?>
        get() = EventField<UByte?>(OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Id.traitId, 2u)

      val platformCode: EventField<Long?>
        get() = EventField<Long?>(OtaSoftwareUpdateRequestorTrait.DownloadErrorImpl.Id.traitId, 3u)

      val TypedExpression<out DownloadErrorEvent?>.softwareVersion: TypedExpression<UInt?>
        get() = fieldSelect<DownloadErrorEvent, UInt?>(this, EventFields.softwareVersion)

      val TypedExpression<out DownloadErrorEvent?>.bytesDownloaded: TypedExpression<ULong?>
        get() = fieldSelect<DownloadErrorEvent, ULong?>(this, EventFields.bytesDownloaded)

      val TypedExpression<out DownloadErrorEvent?>.progressPercent: TypedExpression<UByte?>
        get() = fieldSelect<DownloadErrorEvent, UByte?>(this, EventFields.progressPercent)

      val TypedExpression<out DownloadErrorEvent?>.platformCode: TypedExpression<Long?>
        get() = fieldSelect<DownloadErrorEvent, Long?>(this, EventFields.platformCode)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }
}

/** @suppress */
class OtaSoftwareUpdateRequestorImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) :
  OtaSoftwareUpdateRequestor,
  MatterTraitImpl(metadata, client),
  Attributes by attributes,
  Updatable<OtaSoftwareUpdateRequestor, MutableAttributes> {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OtaSoftwareUpdateRequestorImpl) return false

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
  override fun supports(attribute: OtaSoftwareUpdateRequestor.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: OtaSoftwareUpdateRequestor.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands

  /**
   * Announce the presence of a particular OTA provider.
   *
   * @param announcementReason The reason for the announcement.
   * @param endpoint The endpoint ID of the node that implements the OTA Provider cluster.
   * @param optionalArgs Receiver for the optional arguments of this command
   */
  override suspend fun announceOtaProvider(
    providerNodeId: ULong,
    vendorId: UShort,
    announcementReason: AnnouncementReasonEnum,
    endpoint: UShort,
    optionalArgs: OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs.() -> Unit,
  ) {
    val optionalValues =
      object : OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var metadataForNode: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun metadataForNodeAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(metadataForNode)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    sendCommand(
      commandId = OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.requestId,
      request =
        OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.Request(
          providerNodeId,
          vendorId,
          announcementReason,
          optionalValues.metadataForNodeAsOptional(),
          endpoint,
        ),
      requestAdapter = OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.Request,
      useTimedCommand = false,
    )
  }

  /** @suppress */
  override suspend fun update(
    optimisticReturn: (OtaSoftwareUpdateRequestor) -> Unit,
    init: MutableAttributes.() -> Unit,
  ): OtaSoftwareUpdateRequestor {
    val newVal = MutableAttributes(attributes).apply(init)
    val returnVal = OtaSoftwareUpdateRequestorImpl(metadata, client, newVal)
    optimisticReturn(returnVal)
    write(MutableAttributes, newVal, useTimedInteraction = false)
    return returnVal
  }

  // Commands

  /**
   * The batchable version of [announceOtaProvider] command.
   *
   * Announce the presence of a particular OTA provider.
   *
   * @param announcementReason The reason for the announcement.
   * @param endpoint The endpoint ID of the node that implements the OTA Provider cluster.
   * @param optionalArgs Receiver for the optional arguments of this command
   * @return BatchableCommand<Unit>
   */
  override fun announceOtaProviderBatchable(
    providerNodeId: ULong,
    vendorId: UShort,
    announcementReason: AnnouncementReasonEnum,
    endpoint: UShort,
    optionalArgs: OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<Unit> {
    val optionalValues =
      object : OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var metadataForNode: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun metadataForNodeAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(metadataForNode)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.requestId,
          requestAdapter = OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.Request,
          request =
            OtaSoftwareUpdateRequestorTrait.AnnounceOtaProviderCommand.Request(
              providerNodeId,
              vendorId,
              announcementReason,
              optionalValues.metadataForNodeAsOptional(),
              endpoint,
            ),
          useTimedCommand = false,
        )
    )
  }

  override fun toString() = attributes.toString()
}
