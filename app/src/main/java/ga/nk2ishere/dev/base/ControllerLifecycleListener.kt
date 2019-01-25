package ga.nk2ishere.dev.base

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpDelegate
import com.bluelinelabs.conductor.Controller


class ControllerLifecycleListener(private val mvpDelegate: MvpDelegate<Controller>) : Controller.LifecycleListener() {
    private var bundle: Bundle? = null

    private var wasCreated: Boolean = false

    override fun postCreateView(controller: Controller, view: View) {
        wasCreated = true
        mvpDelegate.onCreate(bundle)
    }

    override fun postAttach(controller: Controller, view: View) {
        mvpDelegate.onAttach()
    }

    override fun onRestoreInstanceState(controller: Controller, savedInstanceState: Bundle) {
        bundle = savedInstanceState
    }

    override fun preDetach(controller: Controller, view: View) {
        mvpDelegate.onDetach()
    }

    override fun preDestroyView(controller: Controller, view: View) {
        mvpDelegate.onDestroyView()
    }

    override fun preDestroy(controller: Controller) {
        if (wasCreated) mvpDelegate.onDestroy()
    }

    override fun onSaveInstanceState(controller: Controller, outState: Bundle) {
        mvpDelegate.onSaveInstanceState(outState)
    }
}