package ga.nk2ishere.dev.fluffypatrol.ui.showpet

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import ga.nk2ishere.dev.base.BaseController
import ga.nk2ishere.dev.base.GlobalEvent
import ga.nk2ishere.dev.base.MviView
import ga.nk2ishere.dev.fluffypatrol.R
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.data.Health
import ga.nk2ishere.dev.fluffypatrol.common.interactors.HealthInteractor
import ga.nk2ishere.dev.fluffypatrol.ui.addhealth.AddHealthController
import ga.nk2ishere.dev.fluffypatrol.ui.addhealth.AddHealthViewNotifyHealthSavedAction
import ga.nk2ishere.dev.fluffypatrol.ui.addhealth.AddHealthViewNotifyHealthSavedGlobalEvent
import ga.nk2ishere.dev.fluffypatrol.ui.addpet.AddPetPresenter
import ga.nk2ishere.dev.utils.round
import kotlinx.android.synthetic.main.show_pet.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

class ShowPetController(args: Bundle, private val pet: Dog? = null): BaseController(args), ShowPetView, ShowPetViewPMRenderer {
    constructor(args: Bundle): this(args, null)

    private val renderDispatcher = ShowPetViewPMDiffDispatcher.Builder()
            .target(this)
            .build()
    private var previousShowPetViewPM: ShowPetViewPM? = null
    private val healthListAdapter = GroupAdapter<ViewHolder>()

    companion object {
        fun create(pet: Dog? = null) =
                ShowPetController(Bundle(), pet)
    }

    override fun renderCurrentPet(currentPet: ShowPetViewPM.PetState?) {
        view?.name?.text = currentPet?.name
        view?.type?.text = currentPet?.type?.prettyName
        view?.age?.text = currentPet?.age.toString()
        view?.gender?.setImageDrawable(resources?.getDrawable(when {
            currentPet?.gender == Dog.Gender.M -> R.drawable.fp_male
            currentPet?.gender == Dog.Gender.F -> R.drawable.fp_female
            else -> R.drawable.fp_no_photo
        }))
    }

    override fun renderCurrentHealth(currentHealth: Health?) {
        view?.currentHealth?.text = currentHealth?.coefficient?.times(100)?.round(2)?.toString() ?: "нет"
    }

    override fun renderHealths(healths: List<Health>) {
        healths.forEach { view?.healthGraph?.addValue(it.coefficient.times(100).toFloat()) }
        applicationContext?.let { context -> healthListAdapter.update(healths.map { HealthItem(context, it) }) }
    }

    private fun updatePMAction(action: ShowPetViewUpdatePMAction) {
        renderDispatcher.dispatch(action.pm, previousShowPetViewPM)
        previousShowPetViewPM = action.pm
    }

    private fun showAddHealthAction(action: ShowPetViewShowAddHealthAction) {
        router.pushController(RouterTransaction.with(AddHealthController.create(pet))
                .pushChangeHandler(FadeChangeHandler(false))
                .popChangeHandler(FadeChangeHandler()))
    }

    @InjectPresenter lateinit var presenter: ShowPetPresenter
    @ProvidePresenter fun providePresenter(): ShowPetPresenter = ShowPetPresenter(pet)

    override fun getLayoutId(): Int = R.layout.show_pet

    override fun initView(view: View) {
        view.healthList.layoutManager = GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false)
        view.healthList.adapter = healthListAdapter
        view.addHealth.setOnClickListener { presenter.handleViewEvent(ShowPetViewAddHealthButtonClickedEvent()) }
    }

    override fun applyAction(action: ShowPetViewAction) { when(action) {
        is ShowPetViewUpdatePMAction -> updatePMAction(action)
    } }

    override fun applyActionWithSkip(action: ShowPetViewAction) { when(action) {
        is ShowPetViewShowAddHealthAction -> showAddHealthAction(action)
    } }

    override fun applyGlobalEvent(event: GlobalEvent) { when(event) {
        is AddHealthViewNotifyHealthSavedGlobalEvent -> presenter.handleViewEvent(ShowPetViewHealthSavedEvent())
    } }
}