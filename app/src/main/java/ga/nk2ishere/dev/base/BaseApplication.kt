package ga.nk2ishere.dev.base

import android.app.Application

abstract class BaseApplication: Application() {
    abstract fun provideGlobalEventRelay(): GlobalEventRelay

    override fun onCreate() {
        super.onCreate()
        this.onApplicationInit()
        this.onApplicationInitEnd()
    }

    override fun onTerminate() {
        onApplicationExit()
        globalEventRelay.disposable.clear()
        super.onTerminate()
    }

    abstract fun onApplicationInit()
    open fun onApplicationExit() {}

    private fun onApplicationInitEnd() {
        globalEventRelay = provideGlobalEventRelay()
    }

    companion object {
        internal lateinit var globalEventRelay: GlobalEventRelay
    }
}