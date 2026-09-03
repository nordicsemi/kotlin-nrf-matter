package no.nordicsemi.nrf.matter.commission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.ExperimentalCompottieApi
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import no.nordicsemi.nrf.matter.shared.generated.resources.Res
import no.nordicsemi.nrf.matter.platform.PlatformType
import no.nordicsemi.nrf.matter.platform.currentType
import no.nordicsemi.nrf.matter.theme.NordicDarkGray

@OptIn(ExperimentalCompottieApi::class)
@Composable
fun CommissioningInProgressScreen() {
    Box(
        Modifier.fillMaxSize().background(Color.White),
        contentAlignment = if (currentType == PlatformType.ANDROID) Alignment.Center else Alignment.TopCenter,
    ) {
        val composition by rememberLottieComposition {
            LottieCompositionSpec.JsonString(
                Res.readBytes("files/cat_animation_themed.json").decodeToString()
            )
        }
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = Compottie.IterateForever
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
                contentDescription = "Lottie animation",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Spacer(Modifier.size(16.dp))

            Text("Please wait while we prepare everything.", color = NordicDarkGray)
        }
    }
}
