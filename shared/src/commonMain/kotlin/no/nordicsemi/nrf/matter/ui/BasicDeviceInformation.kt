package no.nordicsemi.nrf.matter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.model.DeviceUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BasicInformationBottomSheet(
    device: DeviceUiModel,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        BasicDeviceInformation(device = device, onDismiss = onDismiss)
    }
}

@Composable
internal fun BasicDeviceInformation(
    device: DeviceUiModel,
    onDismiss: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Device Details",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Matter Device Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Matter Cluster Index 0x0028 Reader",

                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = "These properties are fetched directly during CASE establishment from local cluster declarations:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            device.device.productName?.let {
                InfoRow(
                    label = "Product Name",
                    value = it,
                    attrId = "0x0003"
                )
            }
            device.device.vendorId?.let {
                InfoRow(
                    label = "Vendor ID",
                    value = it.uppercase(),
                    attrId = "0x0002"
                )
            }
            device.device.productId?.let {
                InfoRow(
                    label = "Product ID",
                    value = it.uppercase(),
                    attrId = "0x0004"
                )
            }
            device.device.vendorName?.let {
                InfoRow(
                    label = "Vendor Name",
                    value = it,
                    attrId = "0x0001"
                )
            }
            device.device.softwareVersion?.let {
                InfoRow(
                    label = "Software Version",
                    value = it,
                    attrId = "0x0009"
                )
            }
            device.device.serialNumer?.let {
                InfoRow(
                    label = "Serial Number",
                    value = it,
                    attrId = "0x000F"
                )
            }
            device.device.uniqueId?.let {
                InfoRow(
                    label = "Unique ID",
                    value = it,
                    attrId = "0x0012"
                )
            }
            device.device.specificationVersion?.let {
                InfoRow(
                    label = "Specification Version",
                    value = it.toString(),
                    attrId = "0x0013"
                )
            }
        }
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Close")
        }
    }

}

@Composable
internal fun InfoRow(label: String, value: String, attrId: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = attrId,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoRowPreview() {
    InfoRow(label = "Label", value = "Value", attrId = "AttrId")
}
