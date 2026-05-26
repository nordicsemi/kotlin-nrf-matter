package no.nordicsemi.nrf.matter.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.theme.NordicSun
import nrfmatterformobile.composeapp.generated.resources.Res
import nrfmatterformobile.composeapp.generated.resources.light_bulb
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ManufacturerSpecItem(
    homeViewModel: HomeViewModel,
    device: DeviceUiModel,
    enabled: Boolean,
    updateDeviceState: (deviceId: DeviceId, Boolean) -> Unit,
    onClick: () -> Unit
) {
    val isButtonOn = homeViewModel.subscribeToButtonChanges(device.device.deviceId)
        .collectAsStateWithLifecycle(initialValue = false)
        .value

    val data = device.device.deviceMatterInfo.first().manufacturerSpecificData!! // Shouldn't be null for this device.

    Column {
        DeviceItemContainer(
            homeViewModel = homeViewModel,
            device = device,
            icon = painterResource(Res.drawable.light_bulb),
            title = data.name,
            subtitle = "Turn light ON or OFF",
            isOnline = enabled,
            onDeviceClick = onClick
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Switch(
                    checked = enabled,
                    onCheckedChange = {
                        updateDeviceState(device.device.deviceId, it)
                    }
                )

                Text("LED", style = MaterialTheme.typography.labelSmall)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Switch(
                    checked = isButtonOn,
                    onCheckedChange = {
                        updateDeviceState(device.device.deviceId, it)
                    },
                    enabled = false,
                )

                Text("Button", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun DeviceItemContainer(
    homeViewModel: HomeViewModel,
    device: DeviceUiModel,
    icon: Painter,
    title: String,
    subtitle: String,
    isOnline: Boolean = true,
    onDeviceClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val randomNumber = homeViewModel.subscribeToRandomNumber(device.device.deviceId)
        .collectAsStateWithLifecycle(initialValue = null)
        .value

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = if (isOnline) BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(0.3f)
        ) else CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDeviceClick() }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val boxColor = if (isOnline)
                    NordicSun
                else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            boxColor,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (isOnline)
                            MaterialTheme.colorScheme.primary else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.5f)
                    )
                }

                content()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { homeViewModel.generateRandomNumber(device.device.deviceId) }) {
                    Text("Generate number")
                }

                Spacer(modifier = Modifier.padding(16.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${randomNumber ?: "<empty>"}")
                    Text("Random number", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
