package no.nordicsemi.nrf.matter.binding

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import no.nordicsemi.nrf.matter.shared.generated.resources.Res
import no.nordicsemi.nrf.matter.shared.generated.resources.binding_links_only
import no.nordicsemi.nrf.matter.theme.NordicBlue
import no.nordicsemi.nrf.matter.theme.NordicTheme
import org.jetbrains.compose.resources.painterResource

/*
 * Copyright (c) 2025, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@Composable
internal fun BindingLoader() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val signalPaths = listOf(
        "M141.56,54.5L141.56,30.5",
        "M124.59,61.53L107.62,44.56",
        "M117.56,78.5L93.56,78.5",
        "M189.56,150.5L189.56,174.5",
        "M206.53,143.47L223.5,160.44",
        "M213.56,126.5L237.56,126.5"
    )

    val signalAlphas = signalPaths.indices.map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    delayMillis = index * 200,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "signal_$index"
        )
    }

    val strokeColor = MaterialTheme.colorScheme.primary
    val strokeWidth = 8f
    val viewportWidth = 330f
    val viewportHeight = 205f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(120.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.binding_links_only),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Canvas(modifier = Modifier.matchParentSize()) {
            val scaleX = size.width / viewportWidth
            val scaleY = size.height / viewportHeight

            signalPaths.forEachIndexed { index, pathData ->
                val path = PathParser().parsePathString(pathData).toPath()
                val matrix = Matrix()
                matrix.scale(scaleX, scaleY)
                path.transform(matrix)

                drawPath(
                    path = path,
                    color = strokeColor.copy(alpha = signalAlphas[index].value),
                    style = Stroke(
                        width = strokeWidth * ((scaleX + scaleY) / 2f),
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoaderPreview() {
    NordicTheme {
        BindingLoader()
    }
}

@Composable
internal fun BindingLoaderDialog(
    logs: List<String>,
    loadingText: @Composable (() -> Unit) = {}
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            lazyListState.animateScrollToItem(logs.lastIndex)
        }
    }

    Dialog(
        onDismissRequest = { /* Do nothing */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BindingLoader()
                Spacer(modifier = Modifier.height(16.dp))
                loadingText()
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "PROCESS LOGS",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    HorizontalDivider()

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = logs
                        ) { log ->
                            Text(
                                text = log,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = NordicBlue,
                            )
                        }
                    }
                }
            }
        }
    }
}
