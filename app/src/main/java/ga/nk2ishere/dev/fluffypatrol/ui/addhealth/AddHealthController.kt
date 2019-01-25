package ga.nk2ishere.dev.fluffypatrol.ui.addhealth

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ga.nk2ishere.dev.base.BaseController
import ga.nk2ishere.dev.fluffypatrol.R
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.utils.ReducedTextWatcher
import kotlinx.android.synthetic.main.add_health.view.*
import timber.log.Timber

class AddHealthController(args: Bundle, private val pet: Dog?): BaseController(), AddHealthView, AddHealthViewPMRenderer {

    constructor(args: Bundle): this(args, null)

    companion object {
        fun create(pet: Dog?) =
                AddHealthController(Bundle(), pet)
    }

    private val renderDispatcher = AddHealthViewPMDiffDispatcher.Builder()
            .target(this)
            .build()
    private var previousAddHealthViewPM: AddHealthViewPM? = null

    override fun renderWeightError(weightError: String?) {
        view?.weight?.error = weightError
    }

    override fun renderGrowthError(growthError: String?) {
        view?.growth?.error = growthError
    }

    private fun updatePMAction(action: AddHealthViewUpdatePMAction) {
        renderDispatcher.dispatch(action.pm, previousAddHealthViewPM)
        previousAddHealthViewPM = action.pm
    }

    private fun saveHealthAction(action: AddHealthViewSaveHealthAction) {
        router.handleBack()
    }

    private fun notifyHealthSavedAction(action: AddHealthViewNotifyHealthSavedAction) {
        handleGlobalEvent(AddHealthViewNotifyHealthSavedGlobalEvent())
    }

    @InjectPresenter lateinit var presenter: AddHealthPresenter
    @ProvidePresenter fun providePresenter(): AddHealthPresenter = AddHealthPresenter(pet)

    override fun getLayoutId(): Int = R.layout.add_health

    override fun initView(view: View) {
        view.weight?.addTextChangedListener(ReducedTextWatcher { presenter.handleViewEvent(AddHealthViewWeightEditedEvent(it.toDoubleOrNull())) })
        view.growth?.addTextChangedListener(ReducedTextWatcher { presenter.handleViewEvent(AddHealthViewGrowthEditedEvent(it.toDoubleOrNull())) })
        view.add?.setOnClickListener { presenter.handleViewEvent(AddHealthViewSaveHealthButtonClickedEvent()) }
    }

    override fun applyAction(action: AddHealthViewAction) { when(action) {
        is AddHealthViewUpdatePMAction -> updatePMAction(action)
    } }

    override fun applyActionWithSkip(action: AddHealthViewAction) { when(action) {
        is AddHealthViewSaveHealthAction -> saveHealthAction(action)
        is AddHealthViewNotifyHealthSavedAction -> notifyHealthSavedAction(action)
    } }
}