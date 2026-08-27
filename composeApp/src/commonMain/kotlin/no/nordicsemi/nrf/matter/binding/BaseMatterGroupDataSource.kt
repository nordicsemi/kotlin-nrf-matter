package no.nordicsemi.nrf.matter.binding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import no.nordicsemi.nrf.matter.model.MatterGroup

class BaseMatterGroupDataSource(
    private val dataStore: DataStore<Preferences>,
) : MatterGroupDataSource {

    private val MATTER_GROUPS_KEY = stringPreferencesKey("matter_groups_json")

    override suspend fun save(group: MatterGroup) {
        dataStore.edit { prefs ->
            val current = prefs[MATTER_GROUPS_KEY]?.let { decode(it) } ?: emptyList()
            val updated = current.filterNot { it.groupId == group.groupId } + group
            prefs[MATTER_GROUPS_KEY] = encode(updated)
        }
    }

    override fun getAll(): Flow<List<MatterGroup>> =
        dataStore.data.map { prefs ->
            prefs[MATTER_GROUPS_KEY]?.let { decode(it) } ?: emptyList()
        }

    override suspend fun delete(groupId: Int) {
        dataStore.edit { prefs ->
            val current = prefs[MATTER_GROUPS_KEY]?.let { decode(it) } ?: emptyList()
            val updated = current.filterNot { it.groupId == groupId }
            prefs[MATTER_GROUPS_KEY] = encode(updated)
        }
    }

    private fun encode(list: List<MatterGroup>): String = Json.encodeToString(list)
    private fun decode(json: String): List<MatterGroup> = Json.decodeFromString(json)
}
