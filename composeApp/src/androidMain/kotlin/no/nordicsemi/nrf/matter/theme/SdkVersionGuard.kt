package no.nordicsemi.nrf.matter.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Guards [content] behind a minimum SDK check (API 24 / Android 7.0).
 * If the running device is below the minimum, a full-screen error screen is
 * shown with a Close button that calls [onClose] to terminate the app.
 */
@SuppressLint("ObsoleteSdkInt")
@Composable
fun SdkVersionGuard(
    onClose: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        UnsupportedSdkScreen(onClose = onClose)
    } else {
        content()
    }
}

@Composable
private fun UnsupportedSdkScreen(onClose: () -> Unit) {
    NordicTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Device Not Supported",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This app requires Android 7.0 (API level 24) or higher.\n\n" +
                            "Your device is running API level ${Build.VERSION.SDK_INT}, " +
                            "which is below the minimum required version. " +
                            "We are unable to provide this service on your device.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}
