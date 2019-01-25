package ga.nk2ishere.dev.fluffypatrol

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs
import ga.nk2ishere.dev.base.BaseApplication
import ga.nk2ishere.dev.base.GlobalEventRelay
import ga.nk2ishere.dev.fluffypatrol.common.dependencies.interactorsModule
import ga.nk2ishere.dev.fluffypatrol.common.dependencies.relayModule
import ga.nk2ishere.dev.fluffypatrol.common.dependencies.storageModule
import org.koin.android.ext.android.startKoin
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

class Application : BaseApplication(), KoinComponent {

    private val globalEventRelay: GlobalEventRelay by inject()
    override fun provideGlobalEventRelay(): GlobalEventRelay = globalEventRelay

    override fun onApplicationInit() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin(this, listOf(storageModule, interactorsModule, relayModule))

        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()
    }
}