package no.nordicsemi.nrf.matter.api

import android.content.Context
import androidx.startup.Initializer
import no.nordicsemi.nrf.matter.logger.db.LogDatabase

class NordicMatterInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        LogDatabase.initialize(context)
        NordicMatters.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
