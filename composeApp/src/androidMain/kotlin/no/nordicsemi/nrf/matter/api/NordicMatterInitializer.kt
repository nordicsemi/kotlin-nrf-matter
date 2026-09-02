package no.nordicsemi.nrf.matter.api

import android.content.Context
import androidx.startup.Initializer
import no.nordicsemi.nrf.matter.logger.db.LogDatabase

/**
 * Hands the library the application context before any of the app's own components run.
 *
 * Declared as an App Startup initializer in the library's manifest, which the manifest merger
 * folds into the consuming app - so an app has nothing to call, and the library is ready even
 * when the process was started for something other than the UI: Google Home binding
 * `AppCommissioningService` on a cold start is the case that matters here.
 *
 * Only cheap object construction happens on this path. The CHIP native library, the DataStore
 * files and the Room database are all behind `by lazy`, so an app that never commissions a device
 * pays close to nothing for it.
 *
 * An app that would rather do this itself removes the meta-data node from its manifest
 *
 *     <meta-data android:name="no.nordicsemi.nrf.matter.api.NordicMatterInitializer"
 *         tools:node="remove" />
 *
 * and calls [NordicMatters.initialize] instead. That is also the path unit tests take, since no
 * provider runs there.
 */
class NordicMatterInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        // The logger first: the rest of the library logs, and on Android that needs the database.
        LogDatabase.initialize(context)
        NordicMatters.initialize(context)
    }

    /** Nothing has to be initialized before this. */
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
