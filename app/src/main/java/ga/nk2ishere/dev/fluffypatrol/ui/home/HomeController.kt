package ga.nk2ishere.dev.fluffypatrol.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import ga.nk2ishere.dev.base.BaseController
import ga.nk2ishere.dev.base.GlobalEvent
import ga.nk2ishere.dev.fluffypatrol.R
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.ui.addpet.AddPetController
import ga.nk2ishere.dev.fluffypatrol.ui.addpet.AddPetViewPetSavedGlobalEvent
import ga.nk2ishere.dev.fluffypatrol.ui.showpet.ShowPetController
import ga.nk2ishere.dev.fluffypatrol.ui.misc.TutorialActivity
import kotlinx.android.synthetic.main.home.view.*


class HomeController(args: Bundle): BaseController(args), HomeView, HomeViewPMRenderer {

    companion object {
        val KEY_IS_TUTORIAL_PASSED = "is_tutorial_passed"

        fun create(): HomeController =
                HomeController(Bundle())
    }

    private val renderDispatcher = HomeViewPMDiffDispatcher.Builder()
            .target(this)
            .build()
    private var previousHomeViewPM: HomeViewPM? = null
    private val dogListAdapter = GroupAdapter<ViewHolder>()


    override fun renderPets(pets: Collection<Dog>) {
        applicationContext?.let { context -> dogListAdapter.update(pets.map { PetItem(context, it) }) }
    }

    private fun updatePMAction(action: HomeViewUpdatePMAction) {
        renderDispatcher.dispatch(action.pm, previousHomeViewPM)
        previousHomeViewPM = action.pm
    }

    private fun showTutorialAction(action: HomeViewShowTutorialAction) {
        activity?.let { startActivity(Intent(it, TutorialActivity::class.java)) }
    }

    private fun addPetAction(action: HomeViewAddPetAction) {
        router.pushController(RouterTransaction.with(AddPetController.create())
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler()))
    }

    private fun showPetAction(action: HomeViewShowPetAction) {
        router.pushController(RouterTransaction.with(ShowPetController.create(action.pm.clickedPet))
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler()))
    }

    @InjectPresenter lateinit var presenter: HomePresenter
    @ProvidePresenter fun providePresenter(): HomePresenter = HomePresenter()

    override fun getLayoutId(): Int = R.layout.home

    override fun initView(view: View) {
        view.addPet.setOnClickListener { presenter.handleViewEvent(HomeViewAddPetButtonClickedEvent()) }
        view.petsList.adapter = dogListAdapter
        view.petsList.layoutManager = GridLayoutManager(applicationContext, 1, GridLayoutManager.VERTICAL, false)
        dogListAdapter.setOnItemClickListener { item, view -> if(item is PetItem) presenter.handleViewEvent(HomeViewPetClickedEvent(item.dog)) }
    }

    override fun applyGlobalEvent(event: GlobalEvent) { when(event) {
        is AddPetViewPetSavedGlobalEvent -> presenter.handleViewEvent(HomeViewPetAddedEvent())
    } }

    override fun applyAction(action: HomeViewAction) { when(action) {
        is HomeViewUpdatePMAction -> updatePMAction(action)
    } }

    override fun applyActionWithSkip(action: HomeViewAction) { when(action) {
        is HomeViewShowTutorialAction -> showTutorialAction(action)
        is HomeViewAddPetAction -> addPetAction(action)
        is HomeViewShowPetAction -> showPetAction(action)
    } }
}