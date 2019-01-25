package ga.nk2ishere.dev.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpDelegate
import com.bluelinelabs.conductor.Controller

abstract class BaseController(args: Bundle? = null) : Controller(args) {

    private val mvpDelegate: MvpDelegate<Controller> by lazy { MvpDelegate<Controller>(this) }

    val requireView: View
        get() = view!!

    init {
        addLifecycleListener(getMvpLifecycleListener())
        BaseApplication.globalEventRelay.subscribe { applyGlobalEvent(it) }
    }

    abstract fun getLayoutId(): Int

    abstract fun initView(view: View)

    open fun applyGlobalEvent(event: GlobalEvent) {}

    protected fun handleGlobalEvent(event: GlobalEvent) {
        BaseApplication.globalEventRelay.acceptGlobalEvent(event)
    }

    protected fun getMvpLifecycleListener(): ControllerLifecycleListener {
        return ControllerLifecycleListener(mvpDelegate)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(getLayoutId(), container, false)
                .also { initView(it) }
    }

}