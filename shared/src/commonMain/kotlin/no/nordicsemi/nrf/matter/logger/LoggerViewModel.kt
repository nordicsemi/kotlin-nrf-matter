package no.nordicsemi.nrf.matter.logger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LoggerViewModel : ViewModel() {

    private val logs = NordicLogger.getLogs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val filter = MutableStateFlow("")
    val selectedLogLevels = MutableStateFlow(listOf(SelectableLogLevel.ALL))

    val filteredLogs = combine(logs, filter, selectedLogLevels) { logs, filter, logLevel ->
        logs.filter { it.message.lowercase().contains(filter.lowercase()) }
            .filter { isLogLevelIncluded(it.level) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, logs.value)

    fun setSearch(value: String) {
        filter.value = value
    }

    fun onLogLevelClick(value: SelectableLogLevel) {
        selectedLogLevels.update {
            when (value) {
                SelectableLogLevel.ALL -> listOf(SelectableLogLevel.ALL)
                in it -> (it - value - SelectableLogLevel.ALL).ifEmpty { listOf(SelectableLogLevel.ALL) }
                else -> it + value - SelectableLogLevel.ALL
            }
        }
    }

    private fun isLogLevelIncluded(level: LogLevel): Boolean {
        return selectedLogLevels.value.any {
            when (it) {
                SelectableLogLevel.ALL -> true
                SelectableLogLevel.INFO -> level == LogLevel.INFO
                SelectableLogLevel.DEBUG -> level == LogLevel.DEBUG
                SelectableLogLevel.ERROR -> level == LogLevel.ERROR
            }
        }
    }
}
