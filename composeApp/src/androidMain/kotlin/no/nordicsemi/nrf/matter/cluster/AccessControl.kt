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
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessControlEntryAuthModeEnum
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessControlEntryChanged
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessControlEntryPrivilegeEnum
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessControlEntryStruct
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessControlExtensionChanged
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessControlExtensionStruct
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessRestrictionEntryStruct
import no.nordicsemi.nrf.matter.AccessControlTrait.AccessRestrictionTypeEnum
import no.nordicsemi.nrf.matter.AccessControlTrait.Attributes
import no.nordicsemi.nrf.matter.AccessControlTrait.AttributesImpl
import no.nordicsemi.nrf.matter.AccessControlTrait.ChangeTypeEnum
import no.nordicsemi.nrf.matter.AccessControlTrait.CommissioningAccessRestrictionEntryStruct
import no.nordicsemi.nrf.matter.AccessControlTrait.FabricRestrictionReviewUpdate
import no.nordicsemi.nrf.matter.AccessControlTrait.Feature
import no.nordicsemi.nrf.matter.AccessControlTrait.MutableAttributes
import no.nordicsemi.nrf.matter.AccessControlTrait.ReviewFabricRestrictionsCommand

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the AccessControl trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface AccessControlCommands {
  suspend fun reviewFabricRestrictions(
    arl: List<CommissioningAccessRestrictionEntryStruct>
  ): AccessControlTrait.ReviewFabricRestrictionsCommand.Response

  fun reviewFabricRestrictionsBatchable(
    arl: List<CommissioningAccessRestrictionEntryStruct>
  ): BatchableCommand<AccessControlTrait.ReviewFabricRestrictionsCommand.Response>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface AccessControlCommandsDefaultImpl : AccessControlCommands {
  override suspend fun reviewFabricRestrictions(
    arl: List<CommissioningAccessRestrictionEntryStruct>
  ): AccessControlTrait.ReviewFabricRestrictionsCommand.Response {
    TODO("Not Implemented")
  }

  override fun reviewFabricRestrictionsBatchable(
    arl: List<CommissioningAccessRestrictionEntryStruct>
  ): BatchableCommand<AccessControlTrait.ReviewFabricRestrictionsCommand.Response> {
    TODO("Not Implemented")
  }
}

/** API for the AccessControl trait. */
@Generated("GoogleHomePlatformCodegen")
interface AccessControl :
  Attributes, MatterTrait, Updatable<AccessControl, MutableAttributes>, AccessControlCommands {
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
    /** The [acl][AccessControlTrait.Attributes.acl] trait attribute. */
    acl(
      "acl",
      0u,
      "AccessControlEntryStruct",
      FieldType.Struct,
      false,
      AccessControlEntryStruct.Adapter,
      false,
    ),
    /** The [extension][AccessControlTrait.Attributes.extension] trait attribute. */
    extension(
      "extension",
      1u,
      "AccessControlExtensionStruct",
      FieldType.Struct,
      false,
      AccessControlExtensionStruct.Adapter,
      false,
    ),
    /**
     * The
     * [subjectsPerAccessControlEntry][AccessControlTrait.Attributes.subjectsPerAccessControlEntry]
     * trait attribute.
     */
    subjectsPerAccessControlEntry(
      "subjectsPerAccessControlEntry",
      2u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The
     * [targetsPerAccessControlEntry][AccessControlTrait.Attributes.targetsPerAccessControlEntry]
     * trait attribute.
     */
    targetsPerAccessControlEntry(
      "targetsPerAccessControlEntry",
      3u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The
     * [accessControlEntriesPerFabric][AccessControlTrait.Attributes.accessControlEntriesPerFabric]
     * trait attribute.
     */
    accessControlEntriesPerFabric(
      "accessControlEntriesPerFabric",
      4u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /** The [commissioningArl][AccessControlTrait.Attributes.commissioningArl] trait attribute. */
    commissioningArl(
      "commissioningArl",
      5u,
      "CommissioningAccessRestrictionEntryStruct",
      FieldType.Struct,
      false,
      CommissioningAccessRestrictionEntryStruct.Adapter,
      false,
    ),
    /** The [arl][AccessControlTrait.Attributes.arl] trait attribute. */
    arl(
      "arl",
      6u,
      "AccessRestrictionEntryStruct",
      FieldType.Struct,
      false,
      AccessRestrictionEntryStruct.Adapter,
      false,
    ),
    /**
     * The [generatedCommandList][AccessControlTrait.Attributes.generatedCommandList] trait
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
     * The [acceptedCommandList][AccessControlTrait.Attributes.acceptedCommandList] trait attribute.
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
    /** The [attributeList][AccessControlTrait.Attributes.attributeList] trait attribute. */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][AccessControlTrait.Attributes.featureMap] trait attribute. */
    featureMap(
      "featureMap",
      65532u,
      "Feature",
      FieldType.Bitmap,
      false,
      Feature.BitmapDescriptor,
      false,
    ),
    /** The [clusterRevision][AccessControlTrait.Attributes.clusterRevision] trait attribute. */
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
              acl = fields[acl] as List<AccessControlEntryStruct>?,
              extension = fields[extension] as List<AccessControlExtensionStruct>?,
              subjectsPerAccessControlEntry = fields[subjectsPerAccessControlEntry] as UShort?,
              targetsPerAccessControlEntry = fields[targetsPerAccessControlEntry] as UShort?,
              accessControlEntriesPerFabric = fields[accessControlEntriesPerFabric] as UShort?,
              commissioningArl =
                fields[commissioningArl] as List<CommissioningAccessRestrictionEntryStruct>?,
              arl = fields[arl] as List<AccessRestrictionEntryStruct>?,
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
     * The [reviewFabricRestrictions][AccessControlCommands.reviewFabricRestrictions] trait command.
     */
    ReviewFabricRestrictions(0u)
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<AccessControl>(
      MatterTraitFactory(
        clusterId = AccessControlTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "AccessControlEntryAuthModeEnum" to
              AccessControlTrait.AccessControlEntryAuthModeEnum.Adapter,
            "AccessControlEntryPrivilegeEnum" to
              AccessControlTrait.AccessControlEntryPrivilegeEnum.Adapter,
            "AccessRestrictionTypeEnum" to AccessControlTrait.AccessRestrictionTypeEnum.Adapter,
            "ChangeTypeEnum" to AccessControlTrait.ChangeTypeEnum.Adapter,
          ),
        bitmapAdapters =
          mapOf<String, BitmapAdapter<*>>("Feature" to AccessControlTrait.Feature.Adapter),
        creator = ::AccessControlImpl,
        supportedEvents =
          mapOf(
            AccessControlTrait.AccessControlEntryChangedImpl.Id.toString() to
              AccessControl.AccessControlEntryChangedEvent,
            AccessControlTrait.AccessControlExtensionChangedImpl.Id.toString() to
              AccessControl.AccessControlExtensionChangedEvent,
            AccessControlTrait.FabricRestrictionReviewUpdateImpl.Id.toString() to
              AccessControl.FabricRestrictionReviewUpdateEvent,
          ),
        // All Trait Commands
        commands =
          mapOf(
            AccessControlTrait.ReviewFabricRestrictionsCommand.requestId.toString() to
              ReviewFabricRestrictionsCommand
          ),
      )
    ) {
    val acl: AutomationAttribute<List<AccessControlEntryStruct>?>
      get() =
        AutomationAttribute<List<AccessControlEntryStruct>?>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.acl.tag,
        )

    val extension: AutomationAttribute<List<AccessControlExtensionStruct>?>
      get() =
        AutomationAttribute<List<AccessControlExtensionStruct>?>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.extension.tag,
        )

    val subjectsPerAccessControlEntry: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.subjectsPerAccessControlEntry.tag,
        )

    val targetsPerAccessControlEntry: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.targetsPerAccessControlEntry.tag,
        )

    val accessControlEntriesPerFabric: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.accessControlEntriesPerFabric.tag,
        )

    val commissioningArl: AutomationAttribute<List<CommissioningAccessRestrictionEntryStruct>?>
      get() =
        AutomationAttribute<List<CommissioningAccessRestrictionEntryStruct>?>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.commissioningArl.tag,
        )

    val arl: AutomationAttribute<List<AccessRestrictionEntryStruct>?>
      get() =
        AutomationAttribute<List<AccessRestrictionEntryStruct>?>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.arl.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<Feature>
      get() =
        AutomationAttribute<Feature>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          AccessControlTrait.Id.traitId,
          AccessControl.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out AccessControl?>.acl: TypedExpression<List<AccessControlEntryStruct>?>
      get() =
        fieldSelect<AccessControl, List<AccessControlEntryStruct>?>(
          this,
          AccessControl.Attribute.acl,
        )

    val TypedExpression<out AccessControl?>.extension:
      TypedExpression<List<AccessControlExtensionStruct>?>
      get() =
        fieldSelect<AccessControl, List<AccessControlExtensionStruct>?>(
          this,
          AccessControl.Attribute.extension,
        )

    val TypedExpression<out AccessControl?>.subjectsPerAccessControlEntry: TypedExpression<UShort?>
      get() =
        fieldSelect<AccessControl, UShort?>(
          this,
          AccessControl.Attribute.subjectsPerAccessControlEntry,
        )

    val TypedExpression<out AccessControl?>.targetsPerAccessControlEntry: TypedExpression<UShort?>
      get() =
        fieldSelect<AccessControl, UShort?>(
          this,
          AccessControl.Attribute.targetsPerAccessControlEntry,
        )

    val TypedExpression<out AccessControl?>.accessControlEntriesPerFabric: TypedExpression<UShort?>
      get() =
        fieldSelect<AccessControl, UShort?>(
          this,
          AccessControl.Attribute.accessControlEntriesPerFabric,
        )

    val TypedExpression<out AccessControl?>.commissioningArl:
      TypedExpression<List<CommissioningAccessRestrictionEntryStruct>?>
      get() =
        fieldSelect<AccessControl, List<CommissioningAccessRestrictionEntryStruct>?>(
          this,
          AccessControl.Attribute.commissioningArl,
        )

    val TypedExpression<out AccessControl?>.arl:
      TypedExpression<List<AccessRestrictionEntryStruct>?>
      get() =
        fieldSelect<AccessControl, List<AccessRestrictionEntryStruct>?>(
          this,
          AccessControl.Attribute.arl,
        )

    val TypedExpression<out AccessControl?>.generatedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<AccessControl, List<UInt>>(this, AccessControl.Attribute.generatedCommandList)

    val TypedExpression<out AccessControl?>.acceptedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<AccessControl, List<UInt>>(this, AccessControl.Attribute.acceptedCommandList)

    val TypedExpression<out AccessControl?>.attributeList: TypedExpression<List<UInt>>
      get() = fieldSelect<AccessControl, List<UInt>>(this, AccessControl.Attribute.attributeList)

    val TypedExpression<out AccessControl?>.featureMap: TypedExpression<Feature>
      get() = fieldSelect<AccessControl, Feature>(this, AccessControl.Attribute.featureMap)

    val TypedExpression<out AccessControl?>.clusterRevision: TypedExpression<UShort>
      get() = fieldSelect<AccessControl, UShort>(this, AccessControl.Attribute.clusterRevision)

    fun Updater<AccessControl>.setAcl(value: List<AccessControlEntryStruct>) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.acl, value))
    }

    fun Updater<AccessControl>.setExtension(value: List<AccessControlExtensionStruct>) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.extension, value))
    }

    fun reviewFabricRestrictions(
      arl: List<CommissioningAccessRestrictionEntryStruct>
    ): AutomationCommand {
      val commandId = AccessControlTrait.ReviewFabricRestrictionsCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(ReviewFabricRestrictionsCommand.Request.CommandFields.arl to arl)

      return AutomationCommand(AccessControl, commandId, paramsMap)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "AccessControl"
  }

  override val factory: TraitFactory<AccessControl>
    get() = Companion

  // Events
  class AccessControlEntryChangedEvent
  private constructor(
    override val eventName: String = "AccessControlEntryChanged",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: AccessControlTrait.AccessControlEntryChanged,
  ) : Event, AccessControlTrait.AccessControlEntryChanged by eventPayload {

    override val eventId: Id = Id(AccessControlTrait.AccessControlEntryChangedImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessControlEntryChangedEvent) return false
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
      /** The [adminNodeId][AccessControlEntryChangedEvent.Attributes.adminNodeId] event field. */
      adminNodeId("adminNodeId", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, true),
      /**
       * The [adminPasscodeId][AccessControlEntryChangedEvent.Attributes.adminPasscodeId] event
       * field.
       */
      adminPasscodeId(
        "adminPasscodeId",
        2u,
        "UShort",
        FieldType.UShort,
        false,
        NoOpDescriptor,
        true,
      ),
      /** The [changeType][AccessControlEntryChangedEvent.Attributes.changeType] event field. */
      changeType("changeType", 3u, "ChangeTypeEnum", FieldType.Enum, false, NoOpDescriptor, false),
      /** The [latestValue][AccessControlEntryChangedEvent.Attributes.latestValue] event field. */
      latestValue(
        "latestValue",
        4u,
        "AccessControlEntryStruct",
        FieldType.Struct,
        false,
        AccessControlEntryStruct.Adapter,
        true,
      ),
      /** The [fabricIndex][AccessControlEntryChangedEvent.Attributes.fabricIndex] event field. */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return AccessControlTrait.AccessControlEntryChangedImpl(
                adminNodeId = fields[EventFields.adminNodeId] as ULong?,
                adminPasscodeId = fields[EventFields.adminPasscodeId] as UShort?,
                changeType = fields[EventFields.changeType] as ChangeTypeEnum?,
                latestValue = fields[EventFields.latestValue] as AccessControlEntryStruct?,
                fabricIndex = fields[EventFields.fabricIndex] as UByte?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<AccessControlEntryChangedEvent>(
        MatterEventFactory(
          AccessControlTrait.AccessControlEntryChangedImpl.Id,
          "AccessControlEntryChanged",
          AccessControlTrait.AccessControlEntryChangedImpl.Adapter,
          ::AccessControlEntryChangedEvent,
        )
      ) {
      val adminNodeId: EventField<ULong?>
        get() = EventField<ULong?>(AccessControlTrait.AccessControlEntryChangedImpl.Id.traitId, 1u)

      val adminPasscodeId: EventField<UShort?>
        get() = EventField<UShort?>(AccessControlTrait.AccessControlEntryChangedImpl.Id.traitId, 2u)

      val changeType: EventField<ChangeTypeEnum?>
        get() =
          EventField<ChangeTypeEnum?>(
            AccessControlTrait.AccessControlEntryChangedImpl.Id.traitId,
            3u,
          )

      val latestValue: EventField<AccessControlEntryStruct?>
        get() =
          EventField<AccessControlEntryStruct?>(
            AccessControlTrait.AccessControlEntryChangedImpl.Id.traitId,
            4u,
          )

      val fabricIndex: EventField<UByte?>
        get() =
          EventField<UByte?>(AccessControlTrait.AccessControlEntryChangedImpl.Id.traitId, 254u)

      val TypedExpression<out AccessControlEntryChangedEvent?>.adminNodeId: TypedExpression<ULong?>
        get() = fieldSelect<AccessControlEntryChangedEvent, ULong?>(this, EventFields.adminNodeId)

      val TypedExpression<out AccessControlEntryChangedEvent?>.adminPasscodeId:
        TypedExpression<UShort?>
        get() =
          fieldSelect<AccessControlEntryChangedEvent, UShort?>(this, EventFields.adminPasscodeId)

      val TypedExpression<out AccessControlEntryChangedEvent?>.changeType:
        TypedExpression<ChangeTypeEnum?>
        get() =
          fieldSelect<AccessControlEntryChangedEvent, ChangeTypeEnum?>(this, EventFields.changeType)

      val TypedExpression<out AccessControlEntryChangedEvent?>.latestValue:
        TypedExpression<AccessControlEntryStruct?>
        get() =
          fieldSelect<AccessControlEntryChangedEvent, AccessControlEntryStruct?>(
            this,
            EventFields.latestValue,
          )

      val TypedExpression<out AccessControlEntryChangedEvent?>.fabricIndex: TypedExpression<UByte?>
        get() = fieldSelect<AccessControlEntryChangedEvent, UByte?>(this, EventFields.fabricIndex)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class AccessControlExtensionChangedEvent
  private constructor(
    override val eventName: String = "AccessControlExtensionChanged",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: AccessControlTrait.AccessControlExtensionChanged,
  ) : Event, AccessControlTrait.AccessControlExtensionChanged by eventPayload {

    override val eventId: Id =
      Id(AccessControlTrait.AccessControlExtensionChangedImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is AccessControlExtensionChangedEvent) return false
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
       * The [adminNodeId][AccessControlExtensionChangedEvent.Attributes.adminNodeId] event field.
       */
      adminNodeId("adminNodeId", 1u, "ULong", FieldType.ULong, false, NoOpDescriptor, true),
      /**
       * The [adminPasscodeId][AccessControlExtensionChangedEvent.Attributes.adminPasscodeId] event
       * field.
       */
      adminPasscodeId(
        "adminPasscodeId",
        2u,
        "UShort",
        FieldType.UShort,
        false,
        NoOpDescriptor,
        true,
      ),
      /** The [changeType][AccessControlExtensionChangedEvent.Attributes.changeType] event field. */
      changeType("changeType", 3u, "ChangeTypeEnum", FieldType.Enum, false, NoOpDescriptor, false),
      /**
       * The [latestValue][AccessControlExtensionChangedEvent.Attributes.latestValue] event field.
       */
      latestValue(
        "latestValue",
        4u,
        "AccessControlExtensionStruct",
        FieldType.Struct,
        false,
        AccessControlExtensionStruct.Adapter,
        true,
      ),
      /**
       * The [fabricIndex][AccessControlExtensionChangedEvent.Attributes.fabricIndex] event field.
       */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return AccessControlTrait.AccessControlExtensionChangedImpl(
                adminNodeId = fields[EventFields.adminNodeId] as ULong?,
                adminPasscodeId = fields[EventFields.adminPasscodeId] as UShort?,
                changeType = fields[EventFields.changeType] as ChangeTypeEnum?,
                latestValue = fields[EventFields.latestValue] as AccessControlExtensionStruct?,
                fabricIndex = fields[EventFields.fabricIndex] as UByte?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<AccessControlExtensionChangedEvent>(
        MatterEventFactory(
          AccessControlTrait.AccessControlExtensionChangedImpl.Id,
          "AccessControlExtensionChanged",
          AccessControlTrait.AccessControlExtensionChangedImpl.Adapter,
          ::AccessControlExtensionChangedEvent,
        )
      ) {
      val adminNodeId: EventField<ULong?>
        get() =
          EventField<ULong?>(AccessControlTrait.AccessControlExtensionChangedImpl.Id.traitId, 1u)

      val adminPasscodeId: EventField<UShort?>
        get() =
          EventField<UShort?>(AccessControlTrait.AccessControlExtensionChangedImpl.Id.traitId, 2u)

      val changeType: EventField<ChangeTypeEnum?>
        get() =
          EventField<ChangeTypeEnum?>(
            AccessControlTrait.AccessControlExtensionChangedImpl.Id.traitId,
            3u,
          )

      val latestValue: EventField<AccessControlExtensionStruct?>
        get() =
          EventField<AccessControlExtensionStruct?>(
            AccessControlTrait.AccessControlExtensionChangedImpl.Id.traitId,
            4u,
          )

      val fabricIndex: EventField<UByte?>
        get() =
          EventField<UByte?>(AccessControlTrait.AccessControlExtensionChangedImpl.Id.traitId, 254u)

      val TypedExpression<out AccessControlExtensionChangedEvent?>.adminNodeId:
        TypedExpression<ULong?>
        get() =
          fieldSelect<AccessControlExtensionChangedEvent, ULong?>(this, EventFields.adminNodeId)

      val TypedExpression<out AccessControlExtensionChangedEvent?>.adminPasscodeId:
        TypedExpression<UShort?>
        get() =
          fieldSelect<AccessControlExtensionChangedEvent, UShort?>(
            this,
            EventFields.adminPasscodeId,
          )

      val TypedExpression<out AccessControlExtensionChangedEvent?>.changeType:
        TypedExpression<ChangeTypeEnum?>
        get() =
          fieldSelect<AccessControlExtensionChangedEvent, ChangeTypeEnum?>(
            this,
            EventFields.changeType,
          )

      val TypedExpression<out AccessControlExtensionChangedEvent?>.latestValue:
        TypedExpression<AccessControlExtensionStruct?>
        get() =
          fieldSelect<AccessControlExtensionChangedEvent, AccessControlExtensionStruct?>(
            this,
            EventFields.latestValue,
          )

      val TypedExpression<out AccessControlExtensionChangedEvent?>.fabricIndex:
        TypedExpression<UByte?>
        get() =
          fieldSelect<AccessControlExtensionChangedEvent, UByte?>(this, EventFields.fabricIndex)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class FabricRestrictionReviewUpdateEvent
  private constructor(
    override val eventName: String = "FabricRestrictionReviewUpdate",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: AccessControlTrait.FabricRestrictionReviewUpdate,
  ) : Event, AccessControlTrait.FabricRestrictionReviewUpdate by eventPayload {

    override val eventId: Id =
      Id(AccessControlTrait.FabricRestrictionReviewUpdateImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is FabricRestrictionReviewUpdateEvent) return false
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
      /** The [token][FabricRestrictionReviewUpdateEvent.Attributes.token] event field. */
      token("token", 0u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
      /**
       * The [instruction][FabricRestrictionReviewUpdateEvent.Attributes.instruction] event field.
       */
      instruction("instruction", 1u, "String", FieldType.String, false, NoOpDescriptor, false),
      /**
       * The [arlRequestFlowUrl][FabricRestrictionReviewUpdateEvent.Attributes.arlRequestFlowUrl]
       * event field.
       */
      arlRequestFlowUrl(
        "arlRequestFlowUrl",
        2u,
        "String",
        FieldType.String,
        false,
        NoOpDescriptor,
        false,
      ),
      /**
       * The [fabricIndex][FabricRestrictionReviewUpdateEvent.Attributes.fabricIndex] event field.
       */
      fabricIndex("fabricIndex", 254u, "UByte", FieldType.UByte, false, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return AccessControlTrait.FabricRestrictionReviewUpdateImpl(
                token = fields[EventFields.token] as ULong?,
                instruction = fields[EventFields.instruction] as String?,
                arlRequestFlowUrl = fields[EventFields.arlRequestFlowUrl] as String?,
                fabricIndex = fields[EventFields.fabricIndex] as UByte?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<FabricRestrictionReviewUpdateEvent>(
        MatterEventFactory(
          AccessControlTrait.FabricRestrictionReviewUpdateImpl.Id,
          "FabricRestrictionReviewUpdate",
          AccessControlTrait.FabricRestrictionReviewUpdateImpl.Adapter,
          ::FabricRestrictionReviewUpdateEvent,
        )
      ) {
      val token: EventField<ULong?>
        get() =
          EventField<ULong?>(AccessControlTrait.FabricRestrictionReviewUpdateImpl.Id.traitId, 0u)

      val instruction: EventField<String?>
        get() =
          EventField<String?>(AccessControlTrait.FabricRestrictionReviewUpdateImpl.Id.traitId, 1u)

      val arlRequestFlowUrl: EventField<String?>
        get() =
          EventField<String?>(AccessControlTrait.FabricRestrictionReviewUpdateImpl.Id.traitId, 2u)

      val fabricIndex: EventField<UByte?>
        get() =
          EventField<UByte?>(AccessControlTrait.FabricRestrictionReviewUpdateImpl.Id.traitId, 254u)

      val TypedExpression<out FabricRestrictionReviewUpdateEvent?>.token: TypedExpression<ULong?>
        get() = fieldSelect<FabricRestrictionReviewUpdateEvent, ULong?>(this, EventFields.token)

      val TypedExpression<out FabricRestrictionReviewUpdateEvent?>.instruction:
        TypedExpression<String?>
        get() =
          fieldSelect<FabricRestrictionReviewUpdateEvent, String?>(this, EventFields.instruction)

      val TypedExpression<out FabricRestrictionReviewUpdateEvent?>.arlRequestFlowUrl:
        TypedExpression<String?>
        get() =
          fieldSelect<FabricRestrictionReviewUpdateEvent, String?>(
            this,
            EventFields.arlRequestFlowUrl,
          )

      val TypedExpression<out FabricRestrictionReviewUpdateEvent?>.fabricIndex:
        TypedExpression<UByte?>
        get() =
          fieldSelect<FabricRestrictionReviewUpdateEvent, UByte?>(this, EventFields.fabricIndex)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }
}

/** @suppress */
class AccessControlImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) :
  AccessControl,
  MatterTraitImpl(metadata, client),
  Attributes by attributes,
  Updatable<AccessControl, MutableAttributes> {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AccessControlImpl) return false

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
  override fun supports(attribute: AccessControl.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: AccessControl.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun reviewFabricRestrictions(
    arl: List<CommissioningAccessRestrictionEntryStruct>
  ): AccessControlTrait.ReviewFabricRestrictionsCommand.Response {
    return sendCommand(
      commandId = AccessControlTrait.ReviewFabricRestrictionsCommand.requestId,
      request = AccessControlTrait.ReviewFabricRestrictionsCommand.Request(arl),
      requestAdapter = AccessControlTrait.ReviewFabricRestrictionsCommand.Request,
      responseAdapter = AccessControlTrait.ReviewFabricRestrictionsCommand.Response,
      useTimedCommand = false,
    )
  }

  /** @suppress */
  override suspend fun update(
    optimisticReturn: (AccessControl) -> Unit,
    init: MutableAttributes.() -> Unit,
  ): AccessControl {
    val newVal = MutableAttributes(attributes).apply(init)
    val returnVal = AccessControlImpl(metadata, client, newVal)
    optimisticReturn(returnVal)
    write(MutableAttributes, newVal, useTimedInteraction = false)
    return returnVal
  }

  // Commands

  override fun reviewFabricRestrictionsBatchable(
    arl: List<CommissioningAccessRestrictionEntryStruct>
  ): BatchableCommand<AccessControlTrait.ReviewFabricRestrictionsCommand.Response> {
    return BatchableCommand<AccessControlTrait.ReviewFabricRestrictionsCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = AccessControlTrait.ReviewFabricRestrictionsCommand.requestId,
          requestAdapter = AccessControlTrait.ReviewFabricRestrictionsCommand.Request,
          request = AccessControlTrait.ReviewFabricRestrictionsCommand.Request(arl),
          useTimedCommand = false,
        ),
      responseAdapter = AccessControlTrait.ReviewFabricRestrictionsCommand.Response,
    )
  }

  override fun toString() = attributes.toString()
}
