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
import com.google.home.matter.serialization.OptionalValue
import com.google.home.toDescriptorMap
import javax.annotation.processing.Generated
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.Attributes
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.AttributesImpl
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.ConnectNetworkCommand
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.Feature
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.MutableAttributes
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.NetworkCommissioningStatusEnum
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.NetworkInfoStruct
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.QueryIdentityCommand
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.RemoveNetworkCommand
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.ReorderNetworkCommand
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.ScanNetworksCommand
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.ThreadCapabilitiesBitmap
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.WiFiBandEnum
import no.nordicsemi.nrf.matter.NetworkCommissioningTrait.WiFiSecurityBitmap

/*
 * This file was machine generated via the code generator
 * in `codegen.clusters.kotlin.CustomGenerator`
 *
 */

/**
 * @suppress
 *
 * Commands for the NetworkCommissioning trait.
 */
@Generated("GoogleHomePlatformCodegen")
interface NetworkCommissioningCommands {
  suspend fun scanNetworks(
    optionalArgs: NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs.() -> Unit = {}
  ): NetworkCommissioningTrait.ScanNetworksCommand.Response

  suspend fun addOrUpdateWiFiNetwork(
    ssid: ByteArray,
    credentials: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs.() -> Unit =
      {},
  ): NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response

  suspend fun addOrUpdateThreadNetwork(
    operationalDataset: ByteArray,
    optionalArgs:
      NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs.() -> Unit =
      {},
  ): NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response

  suspend fun removeNetwork(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs.() -> Unit = {},
  ): NetworkCommissioningTrait.RemoveNetworkCommand.Response

  suspend fun connectNetwork(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs.() -> Unit = {},
  ): NetworkCommissioningTrait.ConnectNetworkCommand.Response

  suspend fun reorderNetwork(
    networkId: ByteArray,
    networkIndex: UByte,
    optionalArgs: NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs.() -> Unit = {},
  ): NetworkCommissioningTrait.ReorderNetworkCommand.Response

  suspend fun queryIdentity(
    keyIdentifier: ByteArray,
    optionalArgs: NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs.() -> Unit = {},
  ): NetworkCommissioningTrait.QueryIdentityCommand.Response

  fun scanNetworksBatchable(
    optionalArgs: NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs.() -> Unit = {}
  ): BatchableCommand<NetworkCommissioningTrait.ScanNetworksCommand.Response>

  fun addOrUpdateWiFiNetworkBatchable(
    ssid: ByteArray,
    credentials: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs.() -> Unit =
      {},
  ): BatchableCommand<NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response>

  fun addOrUpdateThreadNetworkBatchable(
    operationalDataset: ByteArray,
    optionalArgs:
      NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs.() -> Unit =
      {},
  ): BatchableCommand<NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response>

  fun removeNetworkBatchable(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<NetworkCommissioningTrait.RemoveNetworkCommand.Response>

  fun connectNetworkBatchable(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<NetworkCommissioningTrait.ConnectNetworkCommand.Response>

  fun reorderNetworkBatchable(
    networkId: ByteArray,
    networkIndex: UByte,
    optionalArgs: NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<NetworkCommissioningTrait.ReorderNetworkCommand.Response>

  fun queryIdentityBatchable(
    keyIdentifier: ByteArray,
    optionalArgs: NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs.() -> Unit = {},
  ): BatchableCommand<NetworkCommissioningTrait.QueryIdentityCommand.Response>
}

/** @suppress */
@Generated("GoogleHomePlatformCodegen")
interface NetworkCommissioningCommandsDefaultImpl : NetworkCommissioningCommands {
  override suspend fun scanNetworks(
    optionalArgs: NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs.() -> Unit
  ): NetworkCommissioningTrait.ScanNetworksCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun addOrUpdateWiFiNetwork(
    ssid: ByteArray,
    credentials: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun addOrUpdateThreadNetwork(
    operationalDataset: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun removeNetwork(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.RemoveNetworkCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun connectNetwork(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.ConnectNetworkCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun reorderNetwork(
    networkId: ByteArray,
    networkIndex: UByte,
    optionalArgs: NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.ReorderNetworkCommand.Response {
    TODO("Not Implemented")
  }

  override suspend fun queryIdentity(
    keyIdentifier: ByteArray,
    optionalArgs: NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.QueryIdentityCommand.Response {
    TODO("Not Implemented")
  }

  override fun scanNetworksBatchable(
    optionalArgs: NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs.() -> Unit
  ): BatchableCommand<NetworkCommissioningTrait.ScanNetworksCommand.Response> {
    TODO("Not Implemented")
  }

  override fun addOrUpdateWiFiNetworkBatchable(
    ssid: ByteArray,
    credentials: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response> {
    TODO("Not Implemented")
  }

  override fun addOrUpdateThreadNetworkBatchable(
    operationalDataset: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response> {
    TODO("Not Implemented")
  }

  override fun removeNetworkBatchable(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.RemoveNetworkCommand.Response> {
    TODO("Not Implemented")
  }

  override fun connectNetworkBatchable(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.ConnectNetworkCommand.Response> {
    TODO("Not Implemented")
  }

  override fun reorderNetworkBatchable(
    networkId: ByteArray,
    networkIndex: UByte,
    optionalArgs: NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.ReorderNetworkCommand.Response> {
    TODO("Not Implemented")
  }

  override fun queryIdentityBatchable(
    keyIdentifier: ByteArray,
    optionalArgs: NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.QueryIdentityCommand.Response> {
    TODO("Not Implemented")
  }
}

/** API for the NetworkCommissioning trait. */
@Generated("GoogleHomePlatformCodegen")
interface NetworkCommissioning :
  Attributes,
  MatterTrait,
  Updatable<NetworkCommissioning, MutableAttributes>,
  NetworkCommissioningCommands {
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
    /** The [maxNetworks][NetworkCommissioningTrait.Attributes.maxNetworks] trait attribute. */
    maxNetworks("maxNetworks", 0u, "UByte", FieldType.UByte, false, NoOpDescriptor, false),
    /** The [networks][NetworkCommissioningTrait.Attributes.networks] trait attribute. */
    networks(
      "networks",
      1u,
      "NetworkInfoStruct",
      FieldType.Struct,
      false,
      NetworkInfoStruct.Adapter,
      false,
    ),
    /**
     * The [scanMaxTimeSeconds][NetworkCommissioningTrait.Attributes.scanMaxTimeSeconds] trait
     * attribute.
     */
    scanMaxTimeSeconds(
      "scanMaxTimeSeconds",
      2u,
      "UByte",
      FieldType.UByte,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [connectMaxTimeSeconds][NetworkCommissioningTrait.Attributes.connectMaxTimeSeconds] trait
     * attribute.
     */
    connectMaxTimeSeconds(
      "connectMaxTimeSeconds",
      3u,
      "UByte",
      FieldType.UByte,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [interfaceEnabled][NetworkCommissioningTrait.Attributes.interfaceEnabled] trait
     * attribute.
     */
    interfaceEnabled(
      "interfaceEnabled",
      4u,
      "Boolean",
      FieldType.Boolean,
      false,
      NoOpDescriptor,
      false,
    ),
    /**
     * The [lastNetworkingStatus][NetworkCommissioningTrait.Attributes.lastNetworkingStatus] trait
     * attribute.
     */
    lastNetworkingStatus(
      "lastNetworkingStatus",
      5u,
      "NetworkCommissioningStatusEnum",
      FieldType.Enum,
      false,
      NetworkCommissioningStatusEnum.EnumDescriptor,
      true,
    ),
    /** The [lastNetworkId][NetworkCommissioningTrait.Attributes.lastNetworkId] trait attribute. */
    lastNetworkId(
      "lastNetworkId",
      6u,
      "ByteArray",
      FieldType.ByteArray,
      false,
      NoOpDescriptor,
      true,
    ),
    /**
     * The [lastConnectErrorValue][NetworkCommissioningTrait.Attributes.lastConnectErrorValue] trait
     * attribute.
     */
    lastConnectErrorValue(
      "lastConnectErrorValue",
      7u,
      "Int",
      FieldType.Int,
      false,
      NoOpDescriptor,
      true,
    ),
    /**
     * The [supportedWiFiBands][NetworkCommissioningTrait.Attributes.supportedWiFiBands] trait
     * attribute.
     */
    supportedWiFiBands(
      "supportedWiFiBands",
      8u,
      "WiFiBandEnum",
      FieldType.Enum,
      false,
      WiFiBandEnum.EnumDescriptor,
      false,
    ),
    /**
     * The [supportedThreadFeatures][NetworkCommissioningTrait.Attributes.supportedThreadFeatures]
     * trait attribute.
     */
    supportedThreadFeatures(
      "supportedThreadFeatures",
      9u,
      "ThreadCapabilitiesBitmap",
      FieldType.Bitmap,
      false,
      ThreadCapabilitiesBitmap.BitmapDescriptor,
      false,
    ),
    /** The [threadVersion][NetworkCommissioningTrait.Attributes.threadVersion] trait attribute. */
    threadVersion("threadVersion", 10u, "UShort", FieldType.UShort, false, NoOpDescriptor, false),
    /**
     * The [generatedCommandList][NetworkCommissioningTrait.Attributes.generatedCommandList] trait
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
     * The [acceptedCommandList][NetworkCommissioningTrait.Attributes.acceptedCommandList] trait
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
    /** The [attributeList][NetworkCommissioningTrait.Attributes.attributeList] trait attribute. */
    attributeList("attributeList", 65531u, "UInt", FieldType.UInt, false, NoOpDescriptor, false),
    /** The [featureMap][NetworkCommissioningTrait.Attributes.featureMap] trait attribute. */
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
     * The [clusterRevision][NetworkCommissioningTrait.Attributes.clusterRevision] trait attribute.
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
              maxNetworks = fields[maxNetworks] as UByte?,
              networks = fields[networks] as List<NetworkInfoStruct>?,
              scanMaxTimeSeconds = fields[scanMaxTimeSeconds] as UByte?,
              connectMaxTimeSeconds = fields[connectMaxTimeSeconds] as UByte?,
              interfaceEnabled = fields[interfaceEnabled] as Boolean?,
              lastNetworkingStatus =
                fields[lastNetworkingStatus] as NetworkCommissioningStatusEnum?,
              lastNetworkId = fields[lastNetworkId] as ByteArray?,
              lastConnectErrorValue = fields[lastConnectErrorValue] as Int?,
              supportedWiFiBands = fields[supportedWiFiBands] as List<WiFiBandEnum>?,
              supportedThreadFeatures =
                fields[supportedThreadFeatures] as ThreadCapabilitiesBitmap?,
              threadVersion = fields[threadVersion] as UShort?,
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
    /** The [scanNetworks][NetworkCommissioningCommands.scanNetworks] trait command. */
    ScanNetworks(0u),
    /**
     * The [addOrUpdateWiFiNetwork][NetworkCommissioningCommands.addOrUpdateWiFiNetwork] trait
     * command.
     */
    AddOrUpdateWiFiNetwork(2u),
    /**
     * The [addOrUpdateThreadNetwork][NetworkCommissioningCommands.addOrUpdateThreadNetwork] trait
     * command.
     */
    AddOrUpdateThreadNetwork(3u),
    /** The [removeNetwork][NetworkCommissioningCommands.removeNetwork] trait command. */
    RemoveNetwork(4u),
    /** The [connectNetwork][NetworkCommissioningCommands.connectNetwork] trait command. */
    ConnectNetwork(6u),
    /** The [reorderNetwork][NetworkCommissioningCommands.reorderNetwork] trait command. */
    ReorderNetwork(8u),
    /** The [queryIdentity][NetworkCommissioningCommands.queryIdentity] trait command. */
    QueryIdentity(9u),
  }

  fun supports(command: Command): Boolean

  /** @suppress */
  companion object :
    TraitFactory<NetworkCommissioning>(
      MatterTraitFactory(
        clusterId = NetworkCommissioningTrait.Id,
        adapter = Attributes.Adapter,
        traitDescriptor = Attribute.StructDescriptor,
        // Map of enum type name string -> EnumAdapter
        enumAdapters =
          mapOf<String, EnumAdapter<*>>(
            "NetworkCommissioningStatusEnum" to
              NetworkCommissioningTrait.NetworkCommissioningStatusEnum.Adapter,
            "WiFiBandEnum" to NetworkCommissioningTrait.WiFiBandEnum.Adapter,
          ),
        bitmapAdapters =
          mapOf<String, BitmapAdapter<*>>(
            "Feature" to NetworkCommissioningTrait.Feature.Adapter,
            "ThreadCapabilitiesBitmap" to
              NetworkCommissioningTrait.ThreadCapabilitiesBitmap.Adapter,
            "WiFiSecurityBitmap" to NetworkCommissioningTrait.WiFiSecurityBitmap.Adapter,
          ),
        creator = ::NetworkCommissioningImpl,
        supportedEvents = mapOf(),
        // All Trait Commands
        commands =
          mapOf(
            NetworkCommissioningTrait.ScanNetworksCommand.requestId.toString() to
              ScanNetworksCommand,
            NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.requestId.toString() to
              AddOrUpdateWiFiNetworkCommand,
            NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.requestId.toString() to
              AddOrUpdateThreadNetworkCommand,
            NetworkCommissioningTrait.RemoveNetworkCommand.requestId.toString() to
              RemoveNetworkCommand,
            NetworkCommissioningTrait.ConnectNetworkCommand.requestId.toString() to
              ConnectNetworkCommand,
            NetworkCommissioningTrait.ReorderNetworkCommand.requestId.toString() to
              ReorderNetworkCommand,
            NetworkCommissioningTrait.QueryIdentityCommand.requestId.toString() to
              QueryIdentityCommand,
          ),
      )
    ) {
    val maxNetworks: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.maxNetworks.tag,
        )

    val networks: AutomationAttribute<List<NetworkInfoStruct>?>
      get() =
        AutomationAttribute<List<NetworkInfoStruct>?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.networks.tag,
        )

    val scanMaxTimeSeconds: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.scanMaxTimeSeconds.tag,
        )

    val connectMaxTimeSeconds: AutomationAttribute<UByte?>
      get() =
        AutomationAttribute<UByte?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.connectMaxTimeSeconds.tag,
        )

    val interfaceEnabled: AutomationAttribute<Boolean?>
      get() =
        AutomationAttribute<Boolean?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.interfaceEnabled.tag,
        )

    val lastNetworkingStatus: AutomationAttribute<NetworkCommissioningStatusEnum?>
      get() =
        AutomationAttribute<NetworkCommissioningStatusEnum?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.lastNetworkingStatus.tag,
        )

    val lastNetworkId: AutomationAttribute<ByteArray?>
      get() =
        AutomationAttribute<ByteArray?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.lastNetworkId.tag,
        )

    val lastConnectErrorValue: AutomationAttribute<Int?>
      get() =
        AutomationAttribute<Int?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.lastConnectErrorValue.tag,
        )

    val supportedWiFiBands: AutomationAttribute<List<WiFiBandEnum>?>
      get() =
        AutomationAttribute<List<WiFiBandEnum>?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.supportedWiFiBands.tag,
        )

    val supportedThreadFeatures: AutomationAttribute<ThreadCapabilitiesBitmap?>
      get() =
        AutomationAttribute<ThreadCapabilitiesBitmap?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.supportedThreadFeatures.tag,
        )

    val threadVersion: AutomationAttribute<UShort?>
      get() =
        AutomationAttribute<UShort?>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.threadVersion.tag,
        )

    val generatedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.generatedCommandList.tag,
        )

    val acceptedCommandList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.acceptedCommandList.tag,
        )

    val attributeList: AutomationAttribute<List<UInt>>
      get() =
        AutomationAttribute<List<UInt>>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.attributeList.tag,
        )

    val featureMap: AutomationAttribute<Feature>
      get() =
        AutomationAttribute<Feature>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.featureMap.tag,
        )

    val clusterRevision: AutomationAttribute<UShort>
      get() =
        AutomationAttribute<UShort>(
          NetworkCommissioningTrait.Id.traitId,
          NetworkCommissioning.Attribute.clusterRevision.tag,
        )

    val TypedExpression<out NetworkCommissioning?>.maxNetworks: TypedExpression<UByte?>
      get() =
        fieldSelect<NetworkCommissioning, UByte?>(this, NetworkCommissioning.Attribute.maxNetworks)

    val TypedExpression<out NetworkCommissioning?>.networks:
      TypedExpression<List<NetworkInfoStruct>?>
      get() =
        fieldSelect<NetworkCommissioning, List<NetworkInfoStruct>?>(
          this,
          NetworkCommissioning.Attribute.networks,
        )

    val TypedExpression<out NetworkCommissioning?>.scanMaxTimeSeconds: TypedExpression<UByte?>
      get() =
        fieldSelect<NetworkCommissioning, UByte?>(
          this,
          NetworkCommissioning.Attribute.scanMaxTimeSeconds,
        )

    val TypedExpression<out NetworkCommissioning?>.connectMaxTimeSeconds: TypedExpression<UByte?>
      get() =
        fieldSelect<NetworkCommissioning, UByte?>(
          this,
          NetworkCommissioning.Attribute.connectMaxTimeSeconds,
        )

    val TypedExpression<out NetworkCommissioning?>.interfaceEnabled: TypedExpression<Boolean?>
      get() =
        fieldSelect<NetworkCommissioning, Boolean?>(
          this,
          NetworkCommissioning.Attribute.interfaceEnabled,
        )

    val TypedExpression<out NetworkCommissioning?>.lastNetworkingStatus:
      TypedExpression<NetworkCommissioningStatusEnum?>
      get() =
        fieldSelect<NetworkCommissioning, NetworkCommissioningStatusEnum?>(
          this,
          NetworkCommissioning.Attribute.lastNetworkingStatus,
        )

    val TypedExpression<out NetworkCommissioning?>.lastNetworkId: TypedExpression<ByteArray?>
      get() =
        fieldSelect<NetworkCommissioning, ByteArray?>(
          this,
          NetworkCommissioning.Attribute.lastNetworkId,
        )

    val TypedExpression<out NetworkCommissioning?>.lastConnectErrorValue: TypedExpression<Int?>
      get() =
        fieldSelect<NetworkCommissioning, Int?>(
          this,
          NetworkCommissioning.Attribute.lastConnectErrorValue,
        )

    val TypedExpression<out NetworkCommissioning?>.supportedWiFiBands:
      TypedExpression<List<WiFiBandEnum>?>
      get() =
        fieldSelect<NetworkCommissioning, List<WiFiBandEnum>?>(
          this,
          NetworkCommissioning.Attribute.supportedWiFiBands,
        )

    val TypedExpression<out NetworkCommissioning?>.supportedThreadFeatures:
      TypedExpression<ThreadCapabilitiesBitmap?>
      get() =
        fieldSelect<NetworkCommissioning, ThreadCapabilitiesBitmap?>(
          this,
          NetworkCommissioning.Attribute.supportedThreadFeatures,
        )

    val TypedExpression<out NetworkCommissioning?>.threadVersion: TypedExpression<UShort?>
      get() =
        fieldSelect<NetworkCommissioning, UShort?>(
          this,
          NetworkCommissioning.Attribute.threadVersion,
        )

    val TypedExpression<out NetworkCommissioning?>.generatedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<NetworkCommissioning, List<UInt>>(
          this,
          NetworkCommissioning.Attribute.generatedCommandList,
        )

    val TypedExpression<out NetworkCommissioning?>.acceptedCommandList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<NetworkCommissioning, List<UInt>>(
          this,
          NetworkCommissioning.Attribute.acceptedCommandList,
        )

    val TypedExpression<out NetworkCommissioning?>.attributeList: TypedExpression<List<UInt>>
      get() =
        fieldSelect<NetworkCommissioning, List<UInt>>(
          this,
          NetworkCommissioning.Attribute.attributeList,
        )

    val TypedExpression<out NetworkCommissioning?>.featureMap: TypedExpression<Feature>
      get() =
        fieldSelect<NetworkCommissioning, Feature>(this, NetworkCommissioning.Attribute.featureMap)

    val TypedExpression<out NetworkCommissioning?>.clusterRevision: TypedExpression<UShort>
      get() =
        fieldSelect<NetworkCommissioning, UShort>(
          this,
          NetworkCommissioning.Attribute.clusterRevision,
        )

    fun Updater<NetworkCommissioning>.setInterfaceEnabled(value: Boolean) {
      attributesToUpdate.add(AttributeToUpdate(Attribute.interfaceEnabled, value))
    }

    fun scanNetworks(
      optionalArgs: NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs.() -> Unit = {}
    ): AutomationCommand {
      val commandId = NetworkCommissioningTrait.ScanNetworksCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> = mutableMapOf()

      val optionalValues =
        object : NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs {
          private val presence = BooleanArray(2)

          override var ssid: ByteArray? = null
            set(value) {
              presence[0] = true
              field = value
            }

          fun ssidAsOptional(): OptionalValue<ByteArray?> =
            if (presence[0]) {
              OptionalValue.present(ssid)
            } else {
              OptionalValue.absent()
            }

          override var breadcrumb: ULong = 0u
            set(value) {
              presence[1] = true
              field = value
            }

          fun breadcrumbAsOptional(): OptionalValue<ULong> =
            if (presence[1]) {
              OptionalValue.present(breadcrumb)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.ssidAsOptional().doWhenPresent {
        paramsMap.put(ScanNetworksCommand.Request.CommandFields.ssid, it)
      }
      optionalValues.breadcrumbAsOptional().doWhenPresent {
        paramsMap.put(ScanNetworksCommand.Request.CommandFields.breadcrumb, it)
      }

      return AutomationCommand(NetworkCommissioning, commandId, paramsMap)
    }

    fun addOrUpdateWiFiNetwork(
      ssid: ByteArray,
      credentials: ByteArray,
      optionalArgs:
        NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs.() -> Unit =
        {},
    ): AutomationCommand {
      val commandId = NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          AddOrUpdateWiFiNetworkCommand.Request.CommandFields.ssid to ssid,
          AddOrUpdateWiFiNetworkCommand.Request.CommandFields.credentials to credentials,
        )

      val optionalValues =
        object : NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs {
          private val presence = BooleanArray(4)

          override var breadcrumb: ULong = 0u
            set(value) {
              presence[0] = true
              field = value
            }

          fun breadcrumbAsOptional(): OptionalValue<ULong> =
            if (presence[0]) {
              OptionalValue.present(breadcrumb)
            } else {
              OptionalValue.absent()
            }

          override var networkIdentity: ByteArray = ByteArray(0)
            set(value) {
              presence[1] = true
              field = value
            }

          fun networkIdentityAsOptional(): OptionalValue<ByteArray> =
            if (presence[1]) {
              OptionalValue.present(networkIdentity)
            } else {
              OptionalValue.absent()
            }

          override var clientIdentifier: ByteArray = ByteArray(0)
            set(value) {
              presence[2] = true
              field = value
            }

          fun clientIdentifierAsOptional(): OptionalValue<ByteArray> =
            if (presence[2]) {
              OptionalValue.present(clientIdentifier)
            } else {
              OptionalValue.absent()
            }

          override var possessionNonce: ByteArray = ByteArray(0)
            set(value) {
              presence[3] = true
              field = value
            }

          fun possessionNonceAsOptional(): OptionalValue<ByteArray> =
            if (presence[3]) {
              OptionalValue.present(possessionNonce)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.breadcrumbAsOptional().doWhenPresent {
        paramsMap.put(AddOrUpdateWiFiNetworkCommand.Request.CommandFields.breadcrumb, it)
      }
      optionalValues.networkIdentityAsOptional().doWhenPresent {
        paramsMap.put(AddOrUpdateWiFiNetworkCommand.Request.CommandFields.networkIdentity, it)
      }
      optionalValues.clientIdentifierAsOptional().doWhenPresent {
        paramsMap.put(AddOrUpdateWiFiNetworkCommand.Request.CommandFields.clientIdentifier, it)
      }
      optionalValues.possessionNonceAsOptional().doWhenPresent {
        paramsMap.put(AddOrUpdateWiFiNetworkCommand.Request.CommandFields.possessionNonce, it)
      }

      return AutomationCommand(NetworkCommissioning, commandId, paramsMap)
    }

    fun addOrUpdateThreadNetwork(
      operationalDataset: ByteArray,
      optionalArgs:
        NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs.() -> Unit =
        {},
    ): AutomationCommand {
      val commandId = NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          AddOrUpdateThreadNetworkCommand.Request.CommandFields.operationalDataset to
            operationalDataset
        )

      val optionalValues =
        object : NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var breadcrumb: ULong = 0u
            set(value) {
              presence[0] = true
              field = value
            }

          fun breadcrumbAsOptional(): OptionalValue<ULong> =
            if (presence[0]) {
              OptionalValue.present(breadcrumb)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.breadcrumbAsOptional().doWhenPresent {
        paramsMap.put(AddOrUpdateThreadNetworkCommand.Request.CommandFields.breadcrumb, it)
      }

      return AutomationCommand(NetworkCommissioning, commandId, paramsMap)
    }

    fun removeNetwork(
      networkId: ByteArray,
      optionalArgs: NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = NetworkCommissioningTrait.RemoveNetworkCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(RemoveNetworkCommand.Request.CommandFields.networkId to networkId)

      val optionalValues =
        object : NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var breadcrumb: ULong = 0u
            set(value) {
              presence[0] = true
              field = value
            }

          fun breadcrumbAsOptional(): OptionalValue<ULong> =
            if (presence[0]) {
              OptionalValue.present(breadcrumb)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.breadcrumbAsOptional().doWhenPresent {
        paramsMap.put(RemoveNetworkCommand.Request.CommandFields.breadcrumb, it)
      }

      return AutomationCommand(NetworkCommissioning, commandId, paramsMap)
    }

    fun connectNetwork(
      networkId: ByteArray,
      optionalArgs: NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = NetworkCommissioningTrait.ConnectNetworkCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(ConnectNetworkCommand.Request.CommandFields.networkId to networkId)

      val optionalValues =
        object : NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var breadcrumb: ULong = 0u
            set(value) {
              presence[0] = true
              field = value
            }

          fun breadcrumbAsOptional(): OptionalValue<ULong> =
            if (presence[0]) {
              OptionalValue.present(breadcrumb)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.breadcrumbAsOptional().doWhenPresent {
        paramsMap.put(ConnectNetworkCommand.Request.CommandFields.breadcrumb, it)
      }

      return AutomationCommand(NetworkCommissioning, commandId, paramsMap)
    }

    fun reorderNetwork(
      networkId: ByteArray,
      networkIndex: UByte,
      optionalArgs: NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = NetworkCommissioningTrait.ReorderNetworkCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(
          ReorderNetworkCommand.Request.CommandFields.networkId to networkId,
          ReorderNetworkCommand.Request.CommandFields.networkIndex to networkIndex,
        )

      val optionalValues =
        object : NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var breadcrumb: ULong = 0u
            set(value) {
              presence[0] = true
              field = value
            }

          fun breadcrumbAsOptional(): OptionalValue<ULong> =
            if (presence[0]) {
              OptionalValue.present(breadcrumb)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.breadcrumbAsOptional().doWhenPresent {
        paramsMap.put(ReorderNetworkCommand.Request.CommandFields.breadcrumb, it)
      }

      return AutomationCommand(NetworkCommissioning, commandId, paramsMap)
    }

    fun queryIdentity(
      keyIdentifier: ByteArray,
      optionalArgs: NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs.() -> Unit = {},
    ): AutomationCommand {
      val commandId = NetworkCommissioningTrait.QueryIdentityCommand.requestId.toString()
      val paramsMap: MutableMap<Field, Any?> =
        mutableMapOf(QueryIdentityCommand.Request.CommandFields.keyIdentifier to keyIdentifier)

      val optionalValues =
        object : NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs {
          private val presence = BooleanArray(1)

          override var possessionNonce: ByteArray = ByteArray(0)
            set(value) {
              presence[0] = true
              field = value
            }

          fun possessionNonceAsOptional(): OptionalValue<ByteArray> =
            if (presence[0]) {
              OptionalValue.present(possessionNonce)
            } else {
              OptionalValue.absent()
            }
        }
      optionalValues.optionalArgs()

      optionalValues.possessionNonceAsOptional().doWhenPresent {
        paramsMap.put(QueryIdentityCommand.Request.CommandFields.possessionNonce, it)
      }

      return AutomationCommand(NetworkCommissioning, commandId, paramsMap)
    }

    @HomeExperimentalApi
    override fun getAttributeById(tagId: UInt): Field? {
      return Attribute.values().firstOrNull { it.tag == tagId }
    }

    @HomeExperimentalApi
    override fun getAttributeByName(name: String): Field? {
      return Attribute.values().firstOrNull { it.name == name }
    }

    override fun toString() = "NetworkCommissioning"
  }

  override val factory: TraitFactory<NetworkCommissioning>
    get() = Companion
}

/** @suppress */
class NetworkCommissioningImpl
constructor(
  override val metadata: Trait.TraitMetadata,
  client: MatterTraitClient,
  internal val attributes: Attributes,
) :
  NetworkCommissioning,
  MatterTraitImpl(metadata, client),
  Attributes by attributes,
  Updatable<NetworkCommissioning, MutableAttributes> {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is NetworkCommissioningImpl) return false

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
  override fun supports(attribute: NetworkCommissioning.Attribute) =
    attributes.attributeList.contains(attribute.tag)

  /**
   * Checks if the trait supports a command. Some devices might not implement all the commands in a
   * Trait definition.
   *
   * @param command The command to check for.
   * @return True if the command is supported by the trait, false if it is not.
   */
  override fun supports(command: NetworkCommissioning.Command) =
    attributes.acceptedCommandList.contains(command.tag)

  // Commands
  override suspend fun scanNetworks(
    optionalArgs: NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs.() -> Unit
  ): NetworkCommissioningTrait.ScanNetworksCommand.Response {
    val optionalValues =
      object : NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs {
        private val presence = BooleanArray(2)
        override var ssid: ByteArray? = null
          set(value) {
            presence[0] = true
            field = value
          }

        fun ssidAsOptional(): OptionalValue<ByteArray?> =
          if (presence[0]) {
            OptionalValue.present(ssid)
          } else {
            OptionalValue.absent()
          }

        override var breadcrumb: ULong = 0u
          set(value) {
            presence[1] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[1]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = NetworkCommissioningTrait.ScanNetworksCommand.requestId,
      request =
        NetworkCommissioningTrait.ScanNetworksCommand.Request(
          optionalValues.ssidAsOptional(),
          optionalValues.breadcrumbAsOptional(),
        ),
      requestAdapter = NetworkCommissioningTrait.ScanNetworksCommand.Request,
      responseAdapter = NetworkCommissioningTrait.ScanNetworksCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun addOrUpdateWiFiNetwork(
    ssid: ByteArray,
    credentials: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response {
    val optionalValues =
      object : NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(4)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }

        override var networkIdentity: ByteArray = ByteArray(0)
          set(value) {
            presence[1] = true
            field = value
          }

        fun networkIdentityAsOptional(): OptionalValue<ByteArray> =
          if (presence[1]) {
            OptionalValue.present(networkIdentity)
          } else {
            OptionalValue.absent()
          }

        override var clientIdentifier: ByteArray = ByteArray(0)
          set(value) {
            presence[2] = true
            field = value
          }

        fun clientIdentifierAsOptional(): OptionalValue<ByteArray> =
          if (presence[2]) {
            OptionalValue.present(clientIdentifier)
          } else {
            OptionalValue.absent()
          }

        override var possessionNonce: ByteArray = ByteArray(0)
          set(value) {
            presence[3] = true
            field = value
          }

        fun possessionNonceAsOptional(): OptionalValue<ByteArray> =
          if (presence[3]) {
            OptionalValue.present(possessionNonce)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.requestId,
      request =
        NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Request(
          ssid,
          credentials,
          optionalValues.breadcrumbAsOptional(),
          optionalValues.networkIdentityAsOptional(),
          optionalValues.clientIdentifierAsOptional(),
          optionalValues.possessionNonceAsOptional(),
        ),
      requestAdapter = NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Request,
      responseAdapter = NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun addOrUpdateThreadNetwork(
    operationalDataset: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response {
    val optionalValues =
      object : NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.requestId,
      request =
        NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Request(
          operationalDataset,
          optionalValues.breadcrumbAsOptional(),
        ),
      requestAdapter = NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Request,
      responseAdapter = NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun removeNetwork(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.RemoveNetworkCommand.Response {
    val optionalValues =
      object : NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = NetworkCommissioningTrait.RemoveNetworkCommand.requestId,
      request =
        NetworkCommissioningTrait.RemoveNetworkCommand.Request(
          networkId,
          optionalValues.breadcrumbAsOptional(),
        ),
      requestAdapter = NetworkCommissioningTrait.RemoveNetworkCommand.Request,
      responseAdapter = NetworkCommissioningTrait.RemoveNetworkCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun connectNetwork(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.ConnectNetworkCommand.Response {
    val optionalValues =
      object : NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = NetworkCommissioningTrait.ConnectNetworkCommand.requestId,
      request =
        NetworkCommissioningTrait.ConnectNetworkCommand.Request(
          networkId,
          optionalValues.breadcrumbAsOptional(),
        ),
      requestAdapter = NetworkCommissioningTrait.ConnectNetworkCommand.Request,
      responseAdapter = NetworkCommissioningTrait.ConnectNetworkCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun reorderNetwork(
    networkId: ByteArray,
    networkIndex: UByte,
    optionalArgs: NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.ReorderNetworkCommand.Response {
    val optionalValues =
      object : NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = NetworkCommissioningTrait.ReorderNetworkCommand.requestId,
      request =
        NetworkCommissioningTrait.ReorderNetworkCommand.Request(
          networkId,
          networkIndex,
          optionalValues.breadcrumbAsOptional(),
        ),
      requestAdapter = NetworkCommissioningTrait.ReorderNetworkCommand.Request,
      responseAdapter = NetworkCommissioningTrait.ReorderNetworkCommand.Response,
      useTimedCommand = false,
    )
  }

  override suspend fun queryIdentity(
    keyIdentifier: ByteArray,
    optionalArgs: NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs.() -> Unit,
  ): NetworkCommissioningTrait.QueryIdentityCommand.Response {
    val optionalValues =
      object : NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var possessionNonce: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun possessionNonceAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(possessionNonce)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return sendCommand(
      commandId = NetworkCommissioningTrait.QueryIdentityCommand.requestId,
      request =
        NetworkCommissioningTrait.QueryIdentityCommand.Request(
          keyIdentifier,
          optionalValues.possessionNonceAsOptional(),
        ),
      requestAdapter = NetworkCommissioningTrait.QueryIdentityCommand.Request,
      responseAdapter = NetworkCommissioningTrait.QueryIdentityCommand.Response,
      useTimedCommand = false,
    )
  }

  /** @suppress */
  override suspend fun update(
    optimisticReturn: (NetworkCommissioning) -> Unit,
    init: MutableAttributes.() -> Unit,
  ): NetworkCommissioning {
    val newVal = MutableAttributes(attributes).apply(init)
    val returnVal = NetworkCommissioningImpl(metadata, client, newVal)
    optimisticReturn(returnVal)
    write(MutableAttributes, newVal, useTimedInteraction = false)
    return returnVal
  }

  // Commands

  override fun scanNetworksBatchable(
    optionalArgs: NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs.() -> Unit
  ): BatchableCommand<NetworkCommissioningTrait.ScanNetworksCommand.Response> {
    val optionalValues =
      object : NetworkCommissioningTrait.ScanNetworksCommand.OptionalArgs {
        private val presence = BooleanArray(2)
        override var ssid: ByteArray? = null
          set(value) {
            presence[0] = true
            field = value
          }

        fun ssidAsOptional(): OptionalValue<ByteArray?> =
          if (presence[0]) {
            OptionalValue.present(ssid)
          } else {
            OptionalValue.absent()
          }

        override var breadcrumb: ULong = 0u
          set(value) {
            presence[1] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[1]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<NetworkCommissioningTrait.ScanNetworksCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = NetworkCommissioningTrait.ScanNetworksCommand.requestId,
          requestAdapter = NetworkCommissioningTrait.ScanNetworksCommand.Request,
          request =
            NetworkCommissioningTrait.ScanNetworksCommand.Request(
              optionalValues.ssidAsOptional(),
              optionalValues.breadcrumbAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = NetworkCommissioningTrait.ScanNetworksCommand.Response,
    )
  }

  override fun addOrUpdateWiFiNetworkBatchable(
    ssid: ByteArray,
    credentials: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response> {
    val optionalValues =
      object : NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(4)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }

        override var networkIdentity: ByteArray = ByteArray(0)
          set(value) {
            presence[1] = true
            field = value
          }

        fun networkIdentityAsOptional(): OptionalValue<ByteArray> =
          if (presence[1]) {
            OptionalValue.present(networkIdentity)
          } else {
            OptionalValue.absent()
          }

        override var clientIdentifier: ByteArray = ByteArray(0)
          set(value) {
            presence[2] = true
            field = value
          }

        fun clientIdentifierAsOptional(): OptionalValue<ByteArray> =
          if (presence[2]) {
            OptionalValue.present(clientIdentifier)
          } else {
            OptionalValue.absent()
          }

        override var possessionNonce: ByteArray = ByteArray(0)
          set(value) {
            presence[3] = true
            field = value
          }

        fun possessionNonceAsOptional(): OptionalValue<ByteArray> =
          if (presence[3]) {
            OptionalValue.present(possessionNonce)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.requestId,
          requestAdapter = NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Request,
          request =
            NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Request(
              ssid,
              credentials,
              optionalValues.breadcrumbAsOptional(),
              optionalValues.networkIdentityAsOptional(),
              optionalValues.clientIdentifierAsOptional(),
              optionalValues.possessionNonceAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = NetworkCommissioningTrait.AddOrUpdateWiFiNetworkCommand.Response,
    )
  }

  override fun addOrUpdateThreadNetworkBatchable(
    operationalDataset: ByteArray,
    optionalArgs: NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response> {
    val optionalValues =
      object : NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.requestId,
          requestAdapter = NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Request,
          request =
            NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Request(
              operationalDataset,
              optionalValues.breadcrumbAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = NetworkCommissioningTrait.AddOrUpdateThreadNetworkCommand.Response,
    )
  }

  override fun removeNetworkBatchable(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.RemoveNetworkCommand.Response> {
    val optionalValues =
      object : NetworkCommissioningTrait.RemoveNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<NetworkCommissioningTrait.RemoveNetworkCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = NetworkCommissioningTrait.RemoveNetworkCommand.requestId,
          requestAdapter = NetworkCommissioningTrait.RemoveNetworkCommand.Request,
          request =
            NetworkCommissioningTrait.RemoveNetworkCommand.Request(
              networkId,
              optionalValues.breadcrumbAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = NetworkCommissioningTrait.RemoveNetworkCommand.Response,
    )
  }

  override fun connectNetworkBatchable(
    networkId: ByteArray,
    optionalArgs: NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.ConnectNetworkCommand.Response> {
    val optionalValues =
      object : NetworkCommissioningTrait.ConnectNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<NetworkCommissioningTrait.ConnectNetworkCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = NetworkCommissioningTrait.ConnectNetworkCommand.requestId,
          requestAdapter = NetworkCommissioningTrait.ConnectNetworkCommand.Request,
          request =
            NetworkCommissioningTrait.ConnectNetworkCommand.Request(
              networkId,
              optionalValues.breadcrumbAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = NetworkCommissioningTrait.ConnectNetworkCommand.Response,
    )
  }

  override fun reorderNetworkBatchable(
    networkId: ByteArray,
    networkIndex: UByte,
    optionalArgs: NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.ReorderNetworkCommand.Response> {
    val optionalValues =
      object : NetworkCommissioningTrait.ReorderNetworkCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var breadcrumb: ULong = 0u
          set(value) {
            presence[0] = true
            field = value
          }

        fun breadcrumbAsOptional(): OptionalValue<ULong> =
          if (presence[0]) {
            OptionalValue.present(breadcrumb)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<NetworkCommissioningTrait.ReorderNetworkCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = NetworkCommissioningTrait.ReorderNetworkCommand.requestId,
          requestAdapter = NetworkCommissioningTrait.ReorderNetworkCommand.Request,
          request =
            NetworkCommissioningTrait.ReorderNetworkCommand.Request(
              networkId,
              networkIndex,
              optionalValues.breadcrumbAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = NetworkCommissioningTrait.ReorderNetworkCommand.Response,
    )
  }

  override fun queryIdentityBatchable(
    keyIdentifier: ByteArray,
    optionalArgs: NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs.() -> Unit,
  ): BatchableCommand<NetworkCommissioningTrait.QueryIdentityCommand.Response> {
    val optionalValues =
      object : NetworkCommissioningTrait.QueryIdentityCommand.OptionalArgs {
        private val presence = BooleanArray(1)
        override var possessionNonce: ByteArray = ByteArray(0)
          set(value) {
            presence[0] = true
            field = value
          }

        fun possessionNonceAsOptional(): OptionalValue<ByteArray> =
          if (presence[0]) {
            OptionalValue.present(possessionNonce)
          } else {
            OptionalValue.absent()
          }
      }
    optionalValues.optionalArgs()
    return BatchableCommand<NetworkCommissioningTrait.QueryIdentityCommand.Response>(
      objectCommand =
        createObjectCommand(
          commandId = NetworkCommissioningTrait.QueryIdentityCommand.requestId,
          requestAdapter = NetworkCommissioningTrait.QueryIdentityCommand.Request,
          request =
            NetworkCommissioningTrait.QueryIdentityCommand.Request(
              keyIdentifier,
              optionalValues.possessionNonceAsOptional(),
            ),
          useTimedCommand = false,
        ),
      responseAdapter = NetworkCommissioningTrait.QueryIdentityCommand.Response,
    )
  }

  override fun toString() = attributes.toString()
}
