package no.nordicsemi.nrf.matter.binding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding

class BaseGroupBindingDataSource(
    private val dataStore: DataStore<Preferences>,
) : GroupBindingDataSource {

    private val GROUP_BINDINGS_KEY = stringPreferencesKey("group_bindings_json")

    override suspend fun save(binding: GroupBinding) {
        dataStore.edit { prefs ->
            val current = prefs[GROUP_BINDINGS_KEY]?.let { decode(it) } ?: emptyList()
            val updated = current.filterNot { it.id == binding.id } + binding
            NordicLogger.info("updated group binding table: $updated", tag = "GroupBindings")
            prefs[GROUP_BINDINGS_KEY] = encode(updated)
        }
    }

    override fun getBindingsForDevice(deviceId: DeviceId): Flow<List<GroupBinding>> {
        return dataStore.data.map { prefs ->
            val bindings = prefs[GROUP_BINDINGS_KEY]?.let { decode(it) } ?: emptyList()
            bindings.filter { it.sourceNodeId == deviceId || it.targetNodeId == deviceId }
        }
    }

    override fun getAll(): Flow<List<GroupBinding>> =
        dataStore.data.map { prefs ->
            prefs[GROUP_BINDINGS_KEY]?.let { decode(it) } ?: emptyList()
        }

    override suspend fun delete(binding: GroupBinding) {
        dataStore.edit { prefs ->
            val current = prefs[GROUP_BINDINGS_KEY]?.let { decode(it) } ?: emptyList()
            val updated = current.filterNot { it.id == binding.id }
            NordicLogger.info("updated group binding table: $updated", tag = "GroupBindings")
            prefs[GROUP_BINDINGS_KEY] = encode(updated)
        }
    }

    private fun encode(list: List<GroupBinding>): String = Json.encodeToString(list)
    private fun decode(json: String): List<GroupBinding> = Json.decodeFromString(json)
}
