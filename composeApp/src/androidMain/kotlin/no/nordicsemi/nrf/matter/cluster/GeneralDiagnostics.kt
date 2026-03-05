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
import com.google.home.annotation.HomeExperimentalApi
import com.google.home.automation.Attribute as AutomationAttribute
import com.google.home.automation.Command as AutomationCommand
import com.google.home.automation.EventField
import com.google.home.automation.TypedExpression
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
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.Attributes
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.AttributesImpl
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.BootReason
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.BootReasonEnum
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.Feature
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.HardwareFaultChange
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.HardwareFaultEnum
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.InterfaceTypeEnum
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.NetworkFaultChange
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.NetworkFaultEnum
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.NetworkInterface
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.PayloadTestRequestCommand
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.RadioFaultChange
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.RadioFaultEnum
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.TestEventTriggerCommand
import no.nordicsemi.nrf.matter.GeneralDiagnosticsTrait.TimeSnapshotCommand

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the GeneralDiagnostics trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface GeneralDiagnosticsCommands {
  suspend fun testEventTrigger(enableKey: ByteArray, eventTrigger: ULong)

  suspend fun timeSnapshot(): GeneralDiagnosticsTrait.TimeSnapshotCommand.Response

  suspend fun payloadTestRequest(
    enableKey: ByteArray,
    value: UByte,
    count: UShort,
  ): GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response

  fun testEventTriggerBatchable(enableKey: ByteArray, eventTrigger: ULong): BatchableCommand<Unit>

  fun timeSnapshotBatchable():
    BatchableCommand<GeneralDiagnosticsTrait.TimeSnapshotCommand.Response>

  fun payloadTestRequestBatchable(
    enableKey: ByteArray,
    value: UByte,
    count: UShort,
  ): BatchableCommand<GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface GeneralDiagnosticsCommandsDefaultImpl : GeneralDiagnosticsCommands {
  override suspend fun testEventTrigger(enableKey: ByteArray, eventTrigger: ULong) {
    TODO("Not Implemented")
  }

  override suspend fun timeSnapshot(): GeneralDiagnosticsTrait.TimeSnapshotCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun payloadTestRequest(
    enableKey: ByteArray,
    value: UByte,
    count: UShort,
  ): GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response {
    TODO("Not Implemented")
  }

  override fun testEventTriggerBatchable(
    enableKey: ByteArray,
    eventTrigger: ULong,
  ): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }

  override fun timeSnapshotBatchable():
    BatchableCommand<GeneralDiagnosticsTrait.TimeSnapshotCommand.Response> {
    TODO("Not Implemented")
  }

  override fun payloadTestRequestBatchable(
    enableKey: ByteArray,
    value: UByte,
    count: UShort,
  ): BatchableCommand<GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response> {
    TODO("Not Implemented")
  }
}

/** API for the GeneralDiagnostics trait. */
@Generated("GoogleHomePlatformCodegen")
interface GeneralDiagnostics : Attributes, MatterTrait, GeneralDiagnosticsCommands {
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
     * The [networkInterfaces][GeneralDiagnosticsTrait.Attributes.networkInterfaces] trait
     * attribute.
     */
    networkInterfaces(
      "networkInterfaces",
      0u,
      "NetworkInterface",
      FieldType.Struct,
      false,
      NetworkInterface.Adapter,
      false,
    ),
    /** The [rebootCount][GeneralDiagnosticsTrait.Attributes.rebootCount] trait attribute. */
    rebootCount("rebootCount", 1u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
    /** The [upTime][GeneralDiagnosticsTrait.Attributes.upTime] trait attribute. */
    upTime("upTime", 2u, "ULong", FieldType.ULong, false, NoOpDescriptor, false),
    /**
     * The [totalOperationalHours][GeneralDiagnosticsTrait.Attributes.totalOperationalHours] trait
     * attribute.
     */
    totalOperationalHours(
      "totalOperationalHours",
      3u,
      "UInt",
      FieldType.UInt,
      false,
      NoOpDescriptor,
      false,
    ),
    /** The [bootReason][GeneralDiagnosticsTrait.Attributes.bootReason] trait attribute. */
    bootReason(
      "bootReason",
      4u,
      "BootReasonEnum",
      FieldType.Enum,
      false,
      BootReasonEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [activeHardwareFaults][GeneralDiagnosticsTrait.Attributes.activeHardwareFaults] trait
     * attribute.
     */
    activeHardwareFaults(
      "activeHardwareFaults",
      5u,
      "HardwareFaultEnum",
      FieldType.Enum,
      false,
      HardwareFaultEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [activeRadioFaults][GeneralDiagnosticsTrait.Attributes.activeRadioFaults] trait
     * attribute.
     */
    activeRadioFaults(
      "activeRadioFaults",
      6u,
      "RadioFaultEnum",
      FieldType.Enum,
      false,
      RadioFaultEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [activeNetworkFaults][GeneralDiagnosticsTrait.Attributes.activeNetworkFaults] trait
     * attribute.
     */
    activeNetworkFaults(
      "activeNetworkFaults",
      7u,
      "NetworkFaultEnum",
      FieldType.Enum,
      false,
      NetworkFaultEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [testEventTriggersEnabled][GeneralDiagnosticsTrait.Attributes.testEventTriggersEnabled]
     * trait attribute.
     */
    testEventTriggersEnabled(
      "testEventTriggersEnabled",
      8u,
      "Boolean",
      FieldType.Boolean,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [generatedCommandList][GeneralDiagnosticsTrait.Attributes.generatedCommandList] trait
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
     * The [acceptedCommandList][GeneralDiagnosticsTrait.Attributes.acceptedCommandList] trait
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
    /** The [attributeList][GeneralDiagnosticsTrait.Attributes.attributeList] trait attribute. */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][GeneralDiagnosticsTrait.Attributes.featureMap] trait attribute. */
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
     * The [clusterRevision][GeneralDiagnosticsTrait.Attributes.clusterRevision] trait attribute.
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
              networkInterfaces = fields[networkInterfaces] as List<NetworkInterface>?,
              rebootCount = fields[rebootCount] as UShort?,
              upTime = fields[upTime] as ULong?,
              totalOperationalHours = fields[totalOperationalHours] as UInt?,
              bootReason = fields[bootReason] as BootReasonEnum?,
              activeHardwareFaults = fields[activeHardwareFaults] as List<HardwareFaultEnum>?,
              activeRadioFaults = fields[activeRadioFaults] as List<RadioFaultEnum>?,
              activeNetworkFaults = fields[activeNetworkFaults] as List<NetworkFaultEnum>?,
              testEventTriggersEnabled = fields[testEventTriggersEnabled] as Boolean?,
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
    /** The [testEventTrigger][GeneralDiagnosticsCommands.testEventTrigger] trait command. */
    TestEventTrigger(0u),
    /** The [timeSnapshot][GeneralDiagnosticsCommands.timeSnapshot] trait command. */
    TimeSnapshot(1u),
    /** The [payloadTestRequest][GeneralDiagnosticsCommands.payloadTestRequest] trait command. */
    PayloadTestRequest(3u),
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<GeneralDiagnostics>(
      MatterTraitFactory(
        clusterId = GeneralDiagnosticsTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "BootReasonEnum" to GeneralDiagnosticsTrait.BootReasonEnum.Adapter,
            "HardwareFaultEnum" to GeneralDiagnosticsTrait.HardwareFaultEnum.Adapter,
            "InterfaceTypeEnum" to GeneralDiagnosticsTrait.InterfaceTypeEnum.Adapter,
            "NetworkFaultEnum" to GeneralDiagnosticsTrait.NetworkFaultEnum.Adapter,
            "RadioFaultEnum" to GeneralDiagnosticsTrait.RadioFaultEnum.Adapter,
          ),
        bitmapAdapters =
          mapOf<String, BitmapAdapter<*>>("Feature" to GeneralDiagnosticsTrait.Feature.Adapter),
        creator = ::GeneralDiagnosticsImpl,
        supportedEvents =
          mapOf(
            GeneralDiagnosticsTrait.HardwareFaultChangeImpl.Id.toString() to
              GeneralDiagnostics.HardwareFaultChangeEvent,
            GeneralDiagnosticsTrait.RadioFaultChangeImpl.Id.toString() to
              GeneralDiagnostics.RadioFaultChangeEvent,
            GeneralDiagnosticsTrait.NetworkFaultChangeImpl.Id.toString() to
              GeneralDiagnostics.NetworkFaultChangeEvent,
            GeneralDiagnosticsTrait.BootReasonImpl.Id.toString() to
              GeneralDiagnostics.BootReasonEvent,
          ),
        // All Trait Commands
        commands =
          mapOf(
            GeneralDiagnosticsTrait.TestEventTriggerCommand.requestId.toString() to
              TestEventTriggerCommand,
            GeneralDiagnosticsTrait.TimeSnapshotCommand.requestId.toString() to TimeSnapshotCommand,
            GeneralDiagnosticsTrait.PayloadTestRequestCommand.requestId.toString() to
              PayloadTestRequestCommand,
          ),
      )
    ) {
    val networkInterfaces: AutomationAttribute<List<NetworkInterface>?>
      get() =
        AutomationAttribute<List<NetworkInterface>?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.networkInterfaces.tag,
        )

    val rebootCount: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.rebootCount.tag,
        )

    val upTime: AutomationAttribute<ULong?>
      get() =
        AutomationAttribute<ULong?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.upTime.tag,
        )

    val totalOperationalHours: AutomationAttribute<UInt?>
      get() =
        AutomationAttribute<UInt?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.totalOperationalHours.tag,
        )

    val bootReason: AutomationAttribute<BootReasonEnum?>
      get() =
        AutomationAttribute<BootReasonEnum?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.bootReason.tag,
        )

    val activeHardwareFaults: AutomationAttribute<List<HardwareFaultEnum>?>
      get() =
        AutomationAttribute<List<HardwareFaultEnum>?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.activeHardwareFaults.tag,
        )

    val activeRadioFaults: AutomationAttribute<List<RadioFaultEnum>?>
      get() =
        AutomationAttribute<List<RadioFaultEnum>?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.activeRadioFaults.tag,
        )

    val activeNetworkFaults: AutomationAttribute<List<NetworkFaultEnum>?>
      get() =
        AutomationAttribute<List<NetworkFaultEnum>?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.activeNetworkFaults.tag,
        )

    val testEventTriggersEnabled: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.testEventTriggersEnabled.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<Feature>
      get() =
        AutomationAttribute<Feature>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          GeneralDiagnosticsTrait.Id.traitId,
          GeneralDiagnostics.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out GeneralDiagnostics?>.networkInterfaces:
      TypedExpression<List<NetworkInterface>?>
      get() =
        fieldSelect<GeneralDiagnostics, List<NetworkInterface>?>(
          this,
          GeneralDiagnostics.Attribute.networkInterfaces,
        )

    val TypedExpression<out GeneralDiagnostics?>.rebootCount: TypedExpression<UShort?>
      get() =
        fieldSelect<GeneralDiagnostics, UShort?>(this, GeneralDiagnostics.Attribute.rebootCount)

    val TypedExpression<out GeneralDiagnostics?>.upTime: TypedExpression<ULong?>
      get() = fieldSelect<GeneralDiagnostics, ULong?>(this, GeneralDiagnostics.Attribute.upTime)

    val TypedExpression<out GeneralDiagnostics?>.totalOperationalHours: TypedExpression<UInt?>
      get() =
        fieldSelect<GeneralDiagnostics, UInt?>(
          this,
          GeneralDiagnostics.Attribute.totalOperationalHours,
        )

    val TypedExpression<out GeneralDiagnostics?>.bootReason: TypedExpression<BootReasonEnum?>
      get() =
        fieldSelect<GeneralDiagnostics, BootReasonEnum?>(
          this,
          GeneralDiagnostics.Attribute.bootReason,
        )

    val TypedExpression<out GeneralDiagnostics?>.activeHardwareFaults:
      TypedExpression<List<HardwareFaultEnum>?>
      get() =
        fieldSelect<GeneralDiagnostics, List<HardwareFaultEnum>?>(
          this,
          GeneralDiagnostics.Attribute.activeHardwareFaults,
        )

    val TypedExpression<out GeneralDiagnostics?>.activeRadioFaults:
      TypedExpression<List<RadioFaultEnum>?>
      get() =
        fieldSelect<GeneralDiagnostics, List<RadioFaultEnum>?>(
          this,
          GeneralDiagnostics.Attribute.activeRadioFaults,
        )

    val TypedExpression<out GeneralDiagnostics?>.activeNetworkFaults:
      TypedExpression<List<NetworkFaultEnum>?>
      get() =
        fieldSelect<GeneralDiagnostics, List<NetworkFaultEnum>?>(
          this,
          GeneralDiagnostics.Attribute.activeNetworkFaults,
        )

    val TypedExpression<out GeneralDiagnostics?>.testEventTriggersEnabled: TypedExpression<Boolean?>
      get() =
        fieldSelect<GeneralDiagnostics, Boolean?>(
          this,
          GeneralDiagnostics.Attribute.testEventTriggersEnabled,
        )

    val TypedExpression<out GeneralDiagnostics?>.generatedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GeneralDiagnostics, List<UInt>>(
          this,
          GeneralDiagnostics.Attribute.generatedCommandList,
        )

    val TypedExpression<out GeneralDiagnostics?>.acceptedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GeneralDiagnostics, List<UInt>>(
          this,
          GeneralDiagnostics.Attribute.acceptedCommandList,
        )

    val TypedExpression<out GeneralDiagnostics?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GeneralDiagnostics, List<UInt>>(
          this,
          GeneralDiagnostics.Attribute.attributeList,
        )

    val TypedExpression<out GeneralDiagnostics?>.featureMap: TypedExpression<Feature>
      get() =
        fieldSelect<GeneralDiagnostics, Feature>(this, GeneralDiagnostics.Attribute.featureMap)

    val TypedExpression<out GeneralDiagnostics?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<GeneralDiagnostics, UShort>(this, GeneralDiagnostics.Attribute.clusterRevision)

    fun testEventTrigger(enableKey: ByteArray, eventTrigger: ULong): AutomationCommand {
      val commandId = GeneralDiagnosticsTrait.TestEventTriggerCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          TestEventTriggerCommand.Request.CommandFields.enableKey to enableKey,
          TestEventTriggerCommand.Request.CommandFields.eventTrigger to eventTrigger,
        )

      return AutomationCommand(GeneralDiagnostics, commandId, paramsMap)
    }

    fun timeSnapshot(): AutomationCommand {
      val commandId = GeneralDiagnosticsTrait.TimeSnapshotCommand.requestId.toString()
      return AutomationCommand(GeneralDiagnostics, commandId)
    }

    fun payloadTestRequest(enableKey: ByteArray, value: UByte, count: UShort): AutomationCommand {
      val commandId = GeneralDiagnosticsTrait.PayloadTestRequestCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          PayloadTestRequestCommand.Request.CommandFields.enableKey to enableKey,
          PayloadTestRequestCommand.Request.CommandFields.`value` to value,
          PayloadTestRequestCommand.Request.CommandFields.count to count,
        )

      return AutomationCommand(GeneralDiagnostics, commandId, paramsMap)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "GeneralDiagnostics"
  }

  override val factory: TraitFactory<GeneralDiagnostics>
    get() = Companion

  // Events
  class HardwareFaultChangeEvent
  private constructor(
    override val eventName: String = "HardwareFaultChange",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: GeneralDiagnosticsTrait.HardwareFaultChange,
  ) : Event, GeneralDiagnosticsTrait.HardwareFaultChange by eventPayload {

    override val eventId: Id = Id(GeneralDiagnosticsTrait.HardwareFaultChangeImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is HardwareFaultChangeEvent) return false
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
      /** The [current][HardwareFaultChangeEvent.Attributes.current] event field. */
      current("current", 0u, "HardwareFaultEnum", FieldType.Enum, true, NoOpDescriptor, false),
      /** The [previous][HardwareFaultChangeEvent.Attributes.previous] event field. */
      previous("previous", 1u, "HardwareFaultEnum", FieldType.Enum, true, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return GeneralDiagnosticsTrait.HardwareFaultChangeImpl(
                current = fields[EventFields.current] as List<HardwareFaultEnum>?,
                previous = fields[EventFields.previous] as List<HardwareFaultEnum>?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<HardwareFaultChangeEvent>(
        MatterEventFactory(
          GeneralDiagnosticsTrait.HardwareFaultChangeImpl.Id,
          "HardwareFaultChange",
          GeneralDiagnosticsTrait.HardwareFaultChangeImpl.Adapter,
          ::HardwareFaultChangeEvent,
        )
      ) {
      val current: EventField<List<HardwareFaultEnum>?>
        get() =
          EventField<List<HardwareFaultEnum>?>(
            GeneralDiagnosticsTrait.HardwareFaultChangeImpl.Id.traitId,
            0u,
          )

      val previous: EventField<List<HardwareFaultEnum>?>
        get() =
          EventField<List<HardwareFaultEnum>?>(
            GeneralDiagnosticsTrait.HardwareFaultChangeImpl.Id.traitId,
            1u,
          )

      val TypedExpression<out HardwareFaultChangeEvent?>.current:
        TypedExpression<List<HardwareFaultEnum>?>
        get() =
          fieldSelect<HardwareFaultChangeEvent, List<HardwareFaultEnum>?>(this, EventFields.current)

      val TypedExpression<out HardwareFaultChangeEvent?>.previous:
        TypedExpression<List<HardwareFaultEnum>?>
        get() =
          fieldSelect<HardwareFaultChangeEvent, List<HardwareFaultEnum>?>(
            this,
            EventFields.previous,
          )

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class RadioFaultChangeEvent
  private constructor(
    override val eventName: String = "RadioFaultChange",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: GeneralDiagnosticsTrait.RadioFaultChange,
  ) : Event, GeneralDiagnosticsTrait.RadioFaultChange by eventPayload {

    override val eventId: Id = Id(GeneralDiagnosticsTrait.RadioFaultChangeImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is RadioFaultChangeEvent) return false
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
      /** The [current][RadioFaultChangeEvent.Attributes.current] event field. */
      current("current", 0u, "RadioFaultEnum", FieldType.Enum, true, NoOpDescriptor, false),
      /** The [previous][RadioFaultChangeEvent.Attributes.previous] event field. */
      previous("previous", 1u, "RadioFaultEnum", FieldType.Enum, true, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return GeneralDiagnosticsTrait.RadioFaultChangeImpl(
                current = fields[EventFields.current] as List<RadioFaultEnum>?,
                previous = fields[EventFields.previous] as List<RadioFaultEnum>?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<RadioFaultChangeEvent>(
        MatterEventFactory(
          GeneralDiagnosticsTrait.RadioFaultChangeImpl.Id,
          "RadioFaultChange",
          GeneralDiagnosticsTrait.RadioFaultChangeImpl.Adapter,
          ::RadioFaultChangeEvent,
        )
      ) {
      val current: EventField<List<RadioFaultEnum>?>
        get() =
          EventField<List<RadioFaultEnum>?>(
            GeneralDiagnosticsTrait.RadioFaultChangeImpl.Id.traitId,
            0u,
          )

      val previous: EventField<List<RadioFaultEnum>?>
        get() =
          EventField<List<RadioFaultEnum>?>(
            GeneralDiagnosticsTrait.RadioFaultChangeImpl.Id.traitId,
            1u,
          )

      val TypedExpression<out RadioFaultChangeEvent?>.current:
        TypedExpression<List<RadioFaultEnum>?>
        get() = fieldSelect<RadioFaultChangeEvent, List<RadioFaultEnum>?>(this, EventFields.current)

      val TypedExpression<out RadioFaultChangeEvent?>.previous:
        TypedExpression<List<RadioFaultEnum>?>
        get() =
          fieldSelect<RadioFaultChangeEvent, List<RadioFaultEnum>?>(this, EventFields.previous)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class NetworkFaultChangeEvent
  private constructor(
    override val eventName: String = "NetworkFaultChange",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: GeneralDiagnosticsTrait.NetworkFaultChange,
  ) : Event, GeneralDiagnosticsTrait.NetworkFaultChange by eventPayload {

    override val eventId: Id = Id(GeneralDiagnosticsTrait.NetworkFaultChangeImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is NetworkFaultChangeEvent) return false
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
      /** The [current][NetworkFaultChangeEvent.Attributes.current] event field. */
      current("current", 0u, "NetworkFaultEnum", FieldType.Enum, true, NoOpDescriptor, false),
      /** The [previous][NetworkFaultChangeEvent.Attributes.previous] event field. */
      previous("previous", 1u, "NetworkFaultEnum", FieldType.Enum, true, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return GeneralDiagnosticsTrait.NetworkFaultChangeImpl(
                current = fields[EventFields.current] as List<NetworkFaultEnum>?,
                previous = fields[EventFields.previous] as List<NetworkFaultEnum>?,
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<NetworkFaultChangeEvent>(
        MatterEventFactory(
          GeneralDiagnosticsTrait.NetworkFaultChangeImpl.Id,
          "NetworkFaultChange",
          GeneralDiagnosticsTrait.NetworkFaultChangeImpl.Adapter,
          ::NetworkFaultChangeEvent,
        )
      ) {
      val current: EventField<List<NetworkFaultEnum>?>
        get() =
          EventField<List<NetworkFaultEnum>?>(
            GeneralDiagnosticsTrait.NetworkFaultChangeImpl.Id.traitId,
            0u,
          )

      val previous: EventField<List<NetworkFaultEnum>?>
        get() =
          EventField<List<NetworkFaultEnum>?>(
            GeneralDiagnosticsTrait.NetworkFaultChangeImpl.Id.traitId,
            1u,
          )

      val TypedExpression<out NetworkFaultChangeEvent?>.current:
        TypedExpression<List<NetworkFaultEnum>?>
        get() =
          fieldSelect<NetworkFaultChangeEvent, List<NetworkFaultEnum>?>(this, EventFields.current)

      val TypedExpression<out NetworkFaultChangeEvent?>.previous:
        TypedExpression<List<NetworkFaultEnum>?>
        get() =
          fieldSelect<NetworkFaultChangeEvent, List<NetworkFaultEnum>?>(this, EventFields.previous)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }

  class BootReasonEvent
  private constructor(
    override val eventName: String = "BootReason",
    private val timestampInMs: Long,
    override val eventImportance: EventImportance,
    override val eventNumber: ULong,
    private val eventPayload: GeneralDiagnosticsTrait.BootReason,
  ) : Event, GeneralDiagnosticsTrait.BootReason by eventPayload {

    override val eventId: Id = Id(GeneralDiagnosticsTrait.BootReasonImpl.Id.toString())
    override val timestamp: Instant = Instant.ofEpochMilli(timestampInMs)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is BootReasonEvent) return false
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
      /** The [bootReason][BootReasonEvent.Attributes.bootReason] event field. */
      bootReason("bootReason", 0u, "BootReasonEnum", FieldType.Enum, false, NoOpDescriptor, false);

      companion object {
        val StructDescriptor =
          object : StructDescriptor {
            @Suppress("Immutable") override val fields: DescriptorMap = entries.toDescriptorMap()

            @HomeExperimentalApi
            override fun toStruct(fields: Map<Field, Any?>): ClusterStruct {
              return GeneralDiagnosticsTrait.BootReasonImpl(
                bootReason = fields[EventFields.bootReason] as BootReasonEnum?
              )
            }
          }
      }
    }

    /** @suppress */
    companion object :
      EventFactory<BootReasonEvent>(
        MatterEventFactory(
          GeneralDiagnosticsTrait.BootReasonImpl.Id,
          "BootReason",
          GeneralDiagnosticsTrait.BootReasonImpl.Adapter,
          ::BootReasonEvent,
        )
      ) {
      val bootReason: EventField<BootReasonEnum?>
        get() = EventField<BootReasonEnum?>(GeneralDiagnosticsTrait.BootReasonImpl.Id.traitId, 0u)

      val TypedExpression<out BootReasonEvent?>.bootReason: TypedExpression<BootReasonEnum?>
        get() = fieldSelect<BootReasonEvent, BootReasonEnum?>(this, EventFields.bootReason)

      override fun getEventFieldById(tagId: UInt): Field? {
        return EventFields.values().firstOrNull { it.tag == tagId }
      }
    }
  }
}

/** @suppress */
class GeneralDiagnosticsImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) : GeneralDiagnostics, MatterTraitImpl(metadata, client), Attributes by attributes {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GeneralDiagnosticsImpl) return false

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
  override fun supports(attribute: GeneralDiagnostics.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: GeneralDiagnostics.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun testEventTrigger(enableKey: ByteArray, eventTrigger: ULong) {
    sendCommand(
      commandId = GeneralDiagnosticsTrait.TestEventTriggerCommand.requestId,
      request = GeneralDiagnosticsTrait.TestEventTriggerCommand.Request(enableKey, eventTrigger),
      requestAdapter = GeneralDiagnosticsTrait.TestEventTriggerCommand.Request,
      useTimedCommand = false,
    )
  }

  override suspend fun timeSnapshot(): GeneralDiagnosticsTrait.TimeSnapshotCommand.Response {
    return sendCommand(
      commandId = GeneralDiagnosticsTrait.TimeSnapshotCommand.requestId,
      request = GeneralDiagnosticsTrait.TimeSnapshotCommand.Request(),
      requestAdapter = GeneralDiagnosticsTrait.TimeSnapshotCommand.Request,
      responseAdapter = GeneralDiagnosticsTrait.TimeSnapshotCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun payloadTestRequest(
    enableKey: ByteArray,
    value: UByte,
    count: UShort,
  ): GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response {
    return sendCommand(
      commandId = GeneralDiagnosticsTrait.PayloadTestRequestCommand.requestId,
      request = GeneralDiagnosticsTrait.PayloadTestRequestCommand.Request(enableKey, value, count),
      requestAdapter = GeneralDiagnosticsTrait.PayloadTestRequestCommand.Request,
      responseAdapter = GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response,
      useTimedCommand = false,
    )
  }

  // Commands

  override fun testEventTriggerBatchable(
    enableKey: ByteArray,
    eventTrigger: ULong,
  ): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = GeneralDiagnosticsTrait.TestEventTriggerCommand.requestId,
          requestAdapter = GeneralDiagnosticsTrait.TestEventTriggerCommand.Request,
          request =
            GeneralDiagnosticsTrait.TestEventTriggerCommand.Request(enableKey, eventTrigger),
          useTimedCommand = false,
        )
    )
  }

  override fun timeSnapshotBatchable():
    BatchableCommand<GeneralDiagnosticsTrait.TimeSnapshotCommand.Response> {
    return BatchableCommand<GeneralDiagnosticsTrait.TimeSnapshotCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GeneralDiagnosticsTrait.TimeSnapshotCommand.requestId,
          requestAdapter = GeneralDiagnosticsTrait.TimeSnapshotCommand.Request,
          request = GeneralDiagnosticsTrait.TimeSnapshotCommand.Request(),
          useTimedCommand = false,
        ),
      responseAdapter = GeneralDiagnosticsTrait.TimeSnapshotCommand.Response,
    )
  }

  override fun payloadTestRequestBatchable(
    enableKey: ByteArray,
    value: UByte,
    count: UShort,
  ): BatchableCommand<GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response> {
    return BatchableCommand<GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GeneralDiagnosticsTrait.PayloadTestRequestCommand.requestId,
          requestAdapter = GeneralDiagnosticsTrait.PayloadTestRequestCommand.Request,
          request =
            GeneralDiagnosticsTrait.PayloadTestRequestCommand.Request(enableKey, value, count),
          useTimedCommand = false,
        ),
      responseAdapter = GeneralDiagnosticsTrait.PayloadTestRequestCommand.Response,
    )
  }

  override fun toString() = attributes.toString()
}
