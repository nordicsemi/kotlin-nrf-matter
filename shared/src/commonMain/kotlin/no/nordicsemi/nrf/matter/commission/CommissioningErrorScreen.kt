package no.nordicsemi.nrf.matter.commission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SlatePrimary = Color(0xFF556791)
private val CardLight = Color.White
private val CardDark = Color(0xFF1E293B)
private val TextTitleLight = Color(0xFF1E293B)
private val TextTitleDark = Color.White
private val TextBodyLight = Color(0xFF64748B)
private val TextBodyDark = Color(0xFF94A3B8)
private val BorderLight = Color(0xFFE2E8F0)
private val BorderDark = Color(0xFF334155)
private val PillBgLight = Color(0xFFF1F5F9)
private val PillBgDark = Color(0xFF334155)

@Composable
fun CommissioningErrorScreen(
    error: CommissioningException,
    onBack: () -> Unit,
    navigateToLogs: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Connection Failed",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSystemInDarkTheme()) TextTitleDark else TextTitleLight
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildAnnotatedString {
                    append("We were unable to pair the ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Matter device")
                    }
                    append(" to your network.")
                },
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) TextBodyDark else TextBodyLight,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            DetailsCard(error, isSystemInDarkTheme())

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TROUBLESHOOTING",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = if (isSystemInDarkTheme()) TextBodyDark else TextBodyLight,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TroubleshootItem(
                    text = "Ensure the device is in commissioning mode (fast blinking light).",
                    isDark = isSystemInDarkTheme()
                )
                Spacer(modifier = Modifier.height(12.dp))
                TroubleshootItem(
                    text = "Verify that the Fabric ID ${error.displayFabricId} is correctly configured.",
                    isDark = isSystemInDarkTheme()
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { navigateToLogs() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Rounded.Terminal, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Go to Logs Panel", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onBack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) PillBgDark else PillBgLight,
                    contentColor = if (isSystemInDarkTheme()) TextTitleDark else TextTitleLight
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Rounded.Close, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Finish", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DetailsCard(
    error: CommissioningException,
    isDark: Boolean
) {
    Surface(
        color = if (isDark) CardDark else CardLight,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isDark) BorderDark else BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
            DetailRow(
                label = "Commissioning id",
                value = {
                    Surface(
                        color = if (isDark) PillBgDark else PillBgLight,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = error.displayDeviceId,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) TextTitleDark else TextTitleLight,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                .basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    repeatDelayMillis = 1000
                                )
                        )
                    }
                },
                isDark = isDark
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailRow(
                label = "Error Code",
                value = {
                    Surface(
                        color = if (isDark) PillBgDark else PillBgLight,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = error.displayErrorCode,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) TextTitleDark else TextTitleLight,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                .basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    repeatDelayMillis = 1000
                                )
                        )
                    }
                },
                isDark = isDark
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailRow(
                label = "Stage",
                value = {
                    Text(
                        text = error.stage.toString(),
                        fontSize = 14.sp,
                        color = if (isDark) TextTitleDark else TextTitleLight,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            .basicMarquee(
                                iterations = Int.MAX_VALUE,
                                repeatDelayMillis = 1000
                            )
                    )
                },
                isDark = isDark
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailRow(
                label = "Message",
                value = {
                    Text(
                        text = error.displayMessage,
                        fontSize = 14.sp,
                        color = if (isDark) TextTitleDark else TextTitleLight,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            .basicMarquee(
                                iterations = Int.MAX_VALUE,
                                repeatDelayMillis = 1000
                            )
                    )
                },
                isDark = isDark
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: @Composable () -> Unit, isDark: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isDark) TextBodyDark else TextBodyLight
        )
        value()
    }
}

@Composable
fun TroubleshootItem(text: String, isDark: Boolean) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            Icons.Rounded.CheckCircleOutline,
            contentDescription = null,
            tint = SlatePrimary,
            modifier = Modifier.size(20.dp).offset(y = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = if (isDark) TextBodyDark else TextBodyLight
        )
    }
}
