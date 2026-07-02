package no.nordicsemi.nrf.matter

import android.os.Bundle
import androidx.activity.compose.setContent
import no.nordicsemi.nrf.matter.theme.NordicActivity
import no.nordicsemi.nrf.matter.theme.SdkVersionGuard
import org.koin.androidx.compose.koinViewModel

class MainActivity : NordicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize CMP Toast.
        multiplatform.network.cmptoast.AppContext.apply { set(applicationContext) }

        setContent {
            SdkVersionGuard(onClose = { finishAndRemoveTask() }) {
                App(homeViewModel = koinViewModel())
            }
        }
    }
}
