package no.nordicsemi.nrf.matter.api

import android.content.Context
import java.lang.ref.WeakReference

object ContextHolder {
    var context: WeakReference<Context>? = null

    fun initialise(context: Context) {
        this.context = WeakReference(context)
    }

    fun getContext(): Context {
        return context!!.get()!!
    }
}
