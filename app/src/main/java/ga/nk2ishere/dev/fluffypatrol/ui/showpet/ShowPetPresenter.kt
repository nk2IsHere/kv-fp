package ga.nk2ishere.dev.fluffypatrol.ui.showpet

import com.arellomobile.mvp.InjectViewState
import ga.nk2ishere.dev.base.MviPresenter
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.data.Health
import ga.nk2ishere.dev.fluffypatrol.common.interactors.HealthInteractor
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

@InjectViewState
class ShowPetPresenter(val currentPet: Dog?): MviPresenter<ShowPetViewAction, ShowPetViewEvent, ShowPetView, ShowPetViewPM>(), KoinComponent {
    override val TAG: String = "SHOW_PET_PRESENTER"

    private val healthInteractor: HealthInteractor by inject()

    private fun createInitializeHandler(): ObservableTransformer<ShowPetViewInitializeEvent, ShowPetViewAction> =
            ObservableTransformer {
                it.map { ShowPetViewPM(
                        currentPet = ShowPetViewPM.PetState(
                                name = currentPet?.name.orEmpty(),
                                age = currentPet?.age?.toString() ?: "",
                                type = currentPet?.type,
                                gender = currentPet?.gender
                        ),
                        currentHealth = currentPet?.health?.lastOrNull(),
                        healths = currentPet?.health?.toList() ?: listOf()
                ) }.doOnNext { handleState(it) }
                        .map { ShowPetViewUpdatePMAction(it) }
            }

    private fun createAddHealthButtonClickedHandler(): ObservableTransformer<ShowPetViewAddHealthButtonClickedEvent, ShowPetViewAction> =
            ObservableTransformer {
                it.flatMap { state }
                        .map { ShowPetViewShowAddHealthAction() }
            }

    private fun createHealthSavedHandler(): ObservableTransformer<ShowPetViewHealthSavedEvent, ShowPetViewAction> =
            ObservableTransformer {
                it.switchMapSingle { healthInteractor.read(currentPet!!) }
                        .zipWith(it.flatMap { state }, BiFunction { data: List<Health>, state: ShowPetViewPM -> state.copy(
                                healths = data,
                                currentHealth = data.lastOrNull()
                        ) })
                        .doOnNext { handleState(it) }
                        .map { ShowPetViewUpdatePMAction(it) }
            }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        handleViewEvent(ShowPetViewInitializeEvent())
    }

    override fun isSkipViewAction(viewAction: ShowPetViewAction): Boolean = viewAction is ShowPetViewSkipAction
    override fun createSharedList(shared: Observable<ShowPetViewEvent>): List<Observable<ShowPetViewAction>> =
            listOf(
                    shared.ofType(ShowPetViewInitializeEvent::class.java).compose(createInitializeHandler()),
                    shared.ofType(ShowPetViewAddHealthButtonClickedEvent::class.java).compose(createAddHealthButtonClickedHandler()),
                    shared.ofType(ShowPetViewHealthSavedEvent::class.java).compose(createHealthSavedHandler())
            )
}