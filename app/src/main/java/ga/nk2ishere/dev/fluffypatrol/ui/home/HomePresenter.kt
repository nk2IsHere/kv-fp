package ga.nk2ishere.dev.fluffypatrol.ui.home

import com.arellomobile.mvp.InjectViewState
import com.pixplicity.easyprefs.library.Prefs
import ga.nk2ishere.dev.base.MviPresenter
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.interactors.DogInteractor
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

@InjectViewState
class HomePresenter(): MviPresenter<HomeViewAction, HomeViewEvent, HomeView, HomeViewPM>(), KoinComponent {
    override val TAG: String = "HOME_PRESENTER"
    private val dogInteractor: DogInteractor by inject()

    private fun createInitializeHandler(): ObservableTransformer<HomeViewInitializeEvent, HomeViewAction> =
            ObservableTransformer {
                it.switchMapSingle { dogInteractor.read() }
                        .map { HomeViewPM(
                            isTutorialPassed = Prefs.getBoolean(HomeController.KEY_IS_TUTORIAL_PASSED, false),
                            pets = it
                        ) }
                        .doOnNext { handleState(it) }
                        .map { HomeViewUpdatePMAction(it) }
            }

    private fun createShowTutorialHandler(): ObservableTransformer<HomeViewShowTutorialEvent, HomeViewAction> =
            ObservableTransformer {
                it.flatMap { state }
                        .filter { !it.isTutorialPassed }
                        .map { it.copy(isTutorialPassed = true) }
                        .doOnNext { Prefs.putBoolean(HomeController.KEY_IS_TUTORIAL_PASSED, it.isTutorialPassed) }
                        .map { HomeViewShowTutorialAction() }
            }

    private fun createAddPetButtonClickHandler(): ObservableTransformer<HomeViewAddPetButtonClickedEvent, HomeViewAction> =
            ObservableTransformer {
                it.flatMap { state }
                        .map { HomeViewAddPetAction(it) }
            }

    private fun createPetAddedHandler(): ObservableTransformer<HomeViewPetAddedEvent, HomeViewAction> =
            ObservableTransformer {
                it.switchMapSingle { dogInteractor.read() }
                        .zipWith(it.flatMap { state }, BiFunction { data: List<Dog>, state: HomeViewPM  -> state.copy(
                                pets = data
                        ) })
                        .map { HomeViewUpdatePMAction(it) }
            }

    private fun createPetClickedHandler(): ObservableTransformer<HomeViewPetClickedEvent, HomeViewAction> =
            ObservableTransformer {
                it.zipWith(it.flatMap { state }, BiFunction { event: HomeViewPetClickedEvent, state: HomeViewPM -> state.copy(
                                clickedPet = event.pet
                        ) })
                        .doOnNext { handleState(it) }
                        .map { HomeViewShowPetAction(it) }
            }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        handleViewEvent(HomeViewInitializeEvent())
        handleViewEvent(HomeViewShowTutorialEvent())
    }

    override fun isSkipViewAction(viewAction: HomeViewAction): Boolean = viewAction is HomeViewSkipAction

    override fun createSharedList(shared: Observable<HomeViewEvent>): List<Observable<HomeViewAction>> =
            listOf(
                    shared.ofType(HomeViewInitializeEvent::class.java).compose(createInitializeHandler()),
                    shared.ofType(HomeViewShowTutorialEvent::class.java).compose(createShowTutorialHandler()),
                    shared.ofType(HomeViewAddPetButtonClickedEvent::class.java).compose(createAddPetButtonClickHandler()),
                    shared.ofType(HomeViewPetAddedEvent::class.java).compose(createPetAddedHandler()),
                    shared.ofType(HomeViewPetClickedEvent::class.java).compose(createPetClickedHandler())
            )

}
