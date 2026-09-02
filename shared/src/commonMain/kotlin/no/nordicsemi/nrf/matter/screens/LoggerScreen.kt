package no.nordicsemi.nrf.matter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.nrf.matter.logger.LogEntity
import no.nordicsemi.nrf.matter.logger.LogLevel
import no.nordicsemi.nrf.matter.logger.LoggerViewModel
import no.nordicsemi.nrf.matter.logger.SelectableLogLevel
import no.nordicsemi.nrf.matter.theme.NordicBlue
import no.nordicsemi.nrf.matter.theme.NordicGreen
import no.nordicsemi.nrf.matter.theme.NordicRed
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoggerScreen() {
    val viewModel: LoggerViewModel = koinViewModel()

    val listState = rememberLazyListState()

    val searchQuery by viewModel.filter.collectAsStateWithLifecycle()
    val selectedLevelFilters = viewModel.selectedLogLevels.collectAsStateWithLifecycle().value

    val filteredLogs = viewModel.filteredLogs.collectAsStateWithLifecycle().value

    Row {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearch(it) },
                placeholder = {
                    Text(
                        "Search messages, nodes or tags...",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearch("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SelectableLogLevel.entries.forEach { level ->
                    FilterChip(
                        selected = selectedLevelFilters.contains(level),
                        onClick = { viewModel.onLogLevelClick(level) },
                        label = { Text(level.toString(), fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .padding(12.dp)
            ) {
                if (filteredLogs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No logs match current search/filter.",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredLogs.count()) { i ->
                            LogItemRow(filteredLogs[i])
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogItemRow(log: LogEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = log.formattedDate.value,
                color = Color(0xFF64748B),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
            )

            Text(
                text = if (log.tag.isEmpty()) "${log.level}" else "${log.level}/${log.tag}",
                color = log.level.toColor(),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = log.message,
            color = Color(0xFFE2E8F0),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
        )
    }
}

private fun LogLevel.toColor() = when (this) {
    LogLevel.INFO -> NordicGreen
    LogLevel.DEBUG -> NordicBlue
    LogLevel.ERROR -> NordicRed
}
