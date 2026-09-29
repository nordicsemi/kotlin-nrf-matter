package no.nordicsemi.nrf.matter

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.compose.viewmodel.koinViewModel

class NordicActivity : ComponentActivity() {

    companion object {
        private var coldStart = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && coldStart) {
            coldStart = false
            val then = System.currentTimeMillis()
            splashScreen.setKeepOnScreenCondition {
                val now = System.currentTimeMillis()
                now < then + 900
            }
        }

        // Initialize CMP Toast.
        multiplatform.network.cmptoast.AppContext.apply { set(applicationContext) }
        setContent {
            App(homeViewModel = koinViewModel())
        }
    }
}