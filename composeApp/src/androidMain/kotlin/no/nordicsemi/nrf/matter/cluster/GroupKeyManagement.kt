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
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.Attributes
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.AttributesImpl
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.Feature
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.GroupInfoMapStruct
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.GroupKeyMapStruct
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.GroupKeySecurityPolicyEnum
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.GroupKeySetStruct
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.KeySetReadAllIndicesCommand
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.KeySetReadCommand
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.KeySetRemoveCommand
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.KeySetWriteCommand
import no.nordicsemi.nrf.matter.GroupKeyManagementTrait.MutableAttributes

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the GroupKeyManagement trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface GroupKeyManagementCommands {
  suspend fun keySetWrite(groupKeySet: GroupKeySetStruct)

  suspend fun keySetRead(groupKeySetId: UShort): GroupKeyManagementTrait.KeySetReadCommand.Response

  suspend fun keySetRemove(groupKeySetId: UShort)

  suspend fun keySetReadAllIndices(): GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response

  fun keySetWriteBatchable(groupKeySet: GroupKeySetStruct): BatchableCommand<Unit>

  fun keySetReadBatchable(
    groupKeySetId: UShort
  ): BatchableCommand<GroupKeyManagementTrait.KeySetReadCommand.Response>

  fun keySetRemoveBatchable(groupKeySetId: UShort): BatchableCommand<Unit>

  fun keySetReadAllIndicesBatchable():
    BatchableCommand<GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface GroupKeyManagementCommandsDefaultImpl : GroupKeyManagementCommands {
  override suspend fun keySetWrite(groupKeySet: GroupKeySetStruct) {
    TODO("Not Implemented")
  }

  override suspend fun keySetRead(
    groupKeySetId: UShort
  ): GroupKeyManagementTrait.KeySetReadCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun keySetRemove(groupKeySetId: UShort) {
    TODO("Not Implemented")
  }

  override suspend fun keySetReadAllIndices():
    GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response {
    TODO("Not Implemented")
  }

  override fun keySetWriteBatchable(groupKeySet: GroupKeySetStruct): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }

  override fun keySetReadBatchable(
    groupKeySetId: UShort
  ): BatchableCommand<GroupKeyManagementTrait.KeySetReadCommand.Response> {
    TODO("Not Implemented")
  }

  override fun keySetRemoveBatchable(groupKeySetId: UShort): BatchableCommand<Unit> {
    TODO("Not Implemented")
  }

  override fun keySetReadAllIndicesBatchable():
    BatchableCommand<GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response> {
    TODO("Not Implemented")
  }
}

/** API for the GroupKeyManagement trait. */
@Generated("GoogleHomePlatformCodegen")
interface GroupKeyManagement :
  Attributes,
  MatterTrait,
  Updatable<GroupKeyManagement, MutableAttributes>,
  GroupKeyManagementCommands {
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
    /** The [groupKeyMap][GroupKeyManagementTrait.Attributes.groupKeyMap] trait attribute. */
    groupKeyMap(
      "groupKeyMap",
      0u,
      "GroupKeyMapStruct",
      FieldType.Struct,
      false,
      GroupKeyMapStruct.Adapter,
      false,
    ),
    /** The [groupTable][GroupKeyManagementTrait.Attributes.groupTable] trait attribute. */
    groupTable(
      "groupTable",
      1u,
      "GroupInfoMapStruct",
      FieldType.Struct,
      false,
      GroupInfoMapStruct.Adapter,
      false,
    ),
    /**
     * The [maxGroupsPerFabric][GroupKeyManagementTrait.Attributes.maxGroupsPerFabric] trait
     * attribute.
     */
    maxGroupsPerFabric(
      "maxGroupsPerFabric",
      2u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [maxGroupKeysPerFabric][GroupKeyManagementTrait.Attributes.maxGroupKeysPerFabric] trait
     * attribute.
     */
    maxGroupKeysPerFabric(
      "maxGroupKeysPerFabric",
      3u,
      "UShort",
      FieldType.UShort,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [generatedCommandList][GroupKeyManagementTrait.Attributes.generatedCommandList] trait
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
     * The [acceptedCommandList][GroupKeyManagementTrait.Attributes.acceptedCommandList] trait
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
    /** The [attributeList][GroupKeyManagementTrait.Attributes.attributeList] trait attribute. */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][GroupKeyManagementTrait.Attributes.featureMap] trait attribute. */
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
     * The [clusterRevision][GroupKeyManagementTrait.Attributes.clusterRevision] trait attribute.
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
              groupKeyMap = fields[groupKeyMap] as List<GroupKeyMapStruct>?,
              groupTable = fields[groupTable] as List<GroupInfoMapStruct>?,
              maxGroupsPerFabric = fields[maxGroupsPerFabric] as UShort?,
              maxGroupKeysPerFabric = fields[maxGroupKeysPerFabric] as UShort?,
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
    /** The [keySetWrite][GroupKeyManagementCommands.keySetWrite] trait command. */
    KeySetWrite(0u),
    /** The [keySetRead][GroupKeyManagementCommands.keySetRead] trait command. */
    KeySetRead(1u),
    /** The [keySetRemove][GroupKeyManagementCommands.keySetRemove] trait command. */
    KeySetRemove(3u),
    /**
     * The [keySetReadAllIndices][GroupKeyManagementCommands.keySetReadAllIndices] trait command.
     */
    KeySetReadAllIndices(4u),
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<GroupKeyManagement>(
      MatterTraitFactory(
        clusterId = GroupKeyManagementTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "GroupKeySecurityPolicyEnum" to
              GroupKeyManagementTrait.GroupKeySecurityPolicyEnum.Adapter
          ),
        bitmapAdapters =
          mapOf<String, BitmapAdapter<*>>("Feature" to GroupKeyManagementTrait.Feature.Adapter),
        creator = ::GroupKeyManagementImpl,
        supportedEvents = mapOf(),
        // All Trait Commands
        commands =
          mapOf(
            GroupKeyManagementTrait.KeySetWriteCommand.requestId.toString() to KeySetWriteCommand,
            GroupKeyManagementTrait.KeySetReadCommand.requestId.toString() to KeySetReadCommand,
            GroupKeyManagementTrait.KeySetRemoveCommand.requestId.toString() to KeySetRemoveCommand,
            GroupKeyManagementTrait.KeySetReadAllIndicesCommand.requestId.toString() to
              KeySetReadAllIndicesCommand,
          ),
      )
    ) {
    val groupKeyMap: AutomationAttribute<List<GroupKeyMapStruct>?>
      get() =
        AutomationAttribute<List<GroupKeyMapStruct>?>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.groupKeyMap.tag,
        )

    val groupTable: AutomationAttribute<List<GroupInfoMapStruct>?>
      get() =
        AutomationAttribute<List<GroupInfoMapStruct>?>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.groupTable.tag,
        )

    val maxGroupsPerFabric: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.maxGroupsPerFabric.tag,
        )

    val maxGroupKeysPerFabric: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.maxGroupKeysPerFabric.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<Feature>
      get() =
        AutomationAttribute<Feature>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          GroupKeyManagementTrait.Id.traitId,
          GroupKeyManagement.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out GroupKeyManagement?>.groupKeyMap:
      TypedExpression<List<GroupKeyMapStruct>?>
      get() =
        fieldSelect<GroupKeyManagement, List<GroupKeyMapStruct>?>(
          this,
          GroupKeyManagement.Attribute.groupKeyMap,
        )

    val TypedExpression<out GroupKeyManagement?>.groupTable:
      TypedExpression<List<GroupInfoMapStruct>?>
      get() =
        fieldSelect<GroupKeyManagement, List<GroupInfoMapStruct>?>(
          this,
          GroupKeyManagement.Attribute.groupTable,
        )

    val TypedExpression<out GroupKeyManagement?>.maxGroupsPerFabric: TypedExpression<UShort?>
      get() =
        fieldSelect<GroupKeyManagement, UShort?>(
          this,
          GroupKeyManagement.Attribute.maxGroupsPerFabric,
        )

    val TypedExpression<out GroupKeyManagement?>.maxGroupKeysPerFabric: TypedExpression<UShort?>
      get() =
        fieldSelect<GroupKeyManagement, UShort?>(
          this,
          GroupKeyManagement.Attribute.maxGroupKeysPerFabric,
        )

    val TypedExpression<out GroupKeyManagement?>.generatedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GroupKeyManagement, List<UInt>>(
          this,
          GroupKeyManagement.Attribute.generatedCommandList,
        )

    val TypedExpression<out GroupKeyManagement?>.acceptedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GroupKeyManagement, List<UInt>>(
          this,
          GroupKeyManagement.Attribute.acceptedCommandList,
        )

    val TypedExpression<out GroupKeyManagement?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<GroupKeyManagement, List<UInt>>(
          this,
          GroupKeyManagement.Attribute.attributeList,
        )

    val TypedExpression<out GroupKeyManagement?>.featureMap: TypedExpression<Feature>
      get() =
        fieldSelect<GroupKeyManagement, Feature>(this, GroupKeyManagement.Attribute.featureMap)

    val TypedExpression<out GroupKeyManagement?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<GroupKeyManagement, UShort>(this, GroupKeyManagement.Attribute.clusterRevision)

    fun Updater<GroupKeyManagement>.setGroupKeyMap(value: List<GroupKeyMapStruct>) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.groupKeyMap, value))
    }

    fun keySetWrite(groupKeySet: GroupKeySetStruct): AutomationCommand {
      val commandId = GroupKeyManagementTrait.KeySetWriteCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(KeySetWriteCommand.Request.CommandFields.groupKeySet to groupKeySet)

      return AutomationCommand(GroupKeyManagement, commandId, paramsMap)
    }

    fun keySetRead(groupKeySetId: UShort): AutomationCommand {
      val commandId = GroupKeyManagementTrait.KeySetReadCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(KeySetReadCommand.Request.CommandFields.groupKeySetId to groupKeySetId)

      return AutomationCommand(GroupKeyManagement, commandId, paramsMap)
    }

    fun keySetRemove(groupKeySetId: UShort): AutomationCommand {
      val commandId = GroupKeyManagementTrait.KeySetRemoveCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(KeySetRemoveCommand.Request.CommandFields.groupKeySetId to groupKeySetId)

      return AutomationCommand(GroupKeyManagement, commandId, paramsMap)
    }

    fun keySetReadAllIndices(): AutomationCommand {
      val commandId = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.requestId.toString()
      return AutomationCommand(GroupKeyManagement, commandId)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "GroupKeyManagement"
  }

  override val factory: TraitFactory<GroupKeyManagement>
    get() = Companion
}

/** @suppress */
class GroupKeyManagementImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) :
  GroupKeyManagement,
  MatterTraitImpl(metadata, client),
  Attributes by attributes,
  Updatable<GroupKeyManagement, MutableAttributes> {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is GroupKeyManagementImpl) return false

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
  override fun supports(attribute: GroupKeyManagement.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: GroupKeyManagement.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun keySetWrite(groupKeySet: GroupKeySetStruct) {
    sendCommand(
      commandId = GroupKeyManagementTrait.KeySetWriteCommand.requestId,
      request = GroupKeyManagementTrait.KeySetWriteCommand.Request(groupKeySet),
      requestAdapter = GroupKeyManagementTrait.KeySetWriteCommand.Request,
      useTimedCommand = false,
    )
  }

  override suspend fun keySetRead(
    groupKeySetId: UShort
  ): GroupKeyManagementTrait.KeySetReadCommand.Response {
    return sendCommand(
      commandId = GroupKeyManagementTrait.KeySetReadCommand.requestId,
      request = GroupKeyManagementTrait.KeySetReadCommand.Request(groupKeySetId),
      requestAdapter = GroupKeyManagementTrait.KeySetReadCommand.Request,
      responseAdapter = GroupKeyManagementTrait.KeySetReadCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun keySetRemove(groupKeySetId: UShort) {
    sendCommand(
      commandId = GroupKeyManagementTrait.KeySetRemoveCommand.requestId,
      request = GroupKeyManagementTrait.KeySetRemoveCommand.Request(groupKeySetId),
      requestAdapter = GroupKeyManagementTrait.KeySetRemoveCommand.Request,
      useTimedCommand = false,
    )
  }

  override suspend fun keySetReadAllIndices():
    GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response {
    return sendCommand(
      commandId = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.requestId,
      request = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Request(),
      requestAdapter = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Request,
      responseAdapter = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response,
      useTimedCommand = false,
    )
  }

  /** @suppress */
  override suspend fun update(
    optimisticReturn: (GroupKeyManagement) -> Unit,
    init: MutableAttributes.() -> Unit,
  ): GroupKeyManagement {
    val newVal = MutableAttributes(attributes).apply(init)
    val returnVal = GroupKeyManagementImpl(metadata, client, newVal)
    optimisticReturn(returnVal)
    write(MutableAttributes, newVal, useTimedInteraction = false)
    return returnVal
  }

  // Commands

  override fun keySetWriteBatchable(groupKeySet: GroupKeySetStruct): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = GroupKeyManagementTrait.KeySetWriteCommand.requestId,
          requestAdapter = GroupKeyManagementTrait.KeySetWriteCommand.Request,
          request = GroupKeyManagementTrait.KeySetWriteCommand.Request(groupKeySet),
          useTimedCommand = false,
        )
    )
  }

  override fun keySetReadBatchable(
    groupKeySetId: UShort
  ): BatchableCommand<GroupKeyManagementTrait.KeySetReadCommand.Response> {
    return BatchableCommand<GroupKeyManagementTrait.KeySetReadCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GroupKeyManagementTrait.KeySetReadCommand.requestId,
          requestAdapter = GroupKeyManagementTrait.KeySetReadCommand.Request,
          request = GroupKeyManagementTrait.KeySetReadCommand.Request(groupKeySetId),
          useTimedCommand = false,
        ),
      responseAdapter = GroupKeyManagementTrait.KeySetReadCommand.Response,
    )
  }

  override fun keySetRemoveBatchable(groupKeySetId: UShort): BatchableCommand<Unit> {
    return BatchableCommand<Unit>(
      objectCommand =
        createObjectCommand(
          commandId = GroupKeyManagementTrait.KeySetRemoveCommand.requestId,
          requestAdapter = GroupKeyManagementTrait.KeySetRemoveCommand.Request,
          request = GroupKeyManagementTrait.KeySetRemoveCommand.Request(groupKeySetId),
          useTimedCommand = false,
        )
    )
  }

  override fun keySetReadAllIndicesBatchable():
    BatchableCommand<GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response> {
    return BatchableCommand<GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.requestId,
          requestAdapter = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Request,
          request = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Request(),
          useTimedCommand = false,
        ),
      responseAdapter = GroupKeyManagementTrait.KeySetReadAllIndicesCommand.Response,
    )
  }

  override fun toString() = attributes.toString()
}
