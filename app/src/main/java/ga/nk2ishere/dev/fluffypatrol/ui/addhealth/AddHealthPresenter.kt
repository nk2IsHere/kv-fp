package ga.nk2ishere.dev.fluffypatrol.ui.addhealth

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
class AddHealthPresenter(private val pet: Dog?): MviPresenter<AddHealthViewAction, AddHealthViewEvent, AddHealthView, AddHealthViewPM>(), KoinComponent {
    override val TAG: String = "ADD_HEALTH_PRESENTER"
    private val healthInteractor: HealthInteractor by inject()

    private fun createInitializeHandler(): ObservableTransformer<AddHealthViewInitializeEvent, AddHealthViewAction> =
            ObservableTransformer {
                it.map { AddHealthViewPM() }
                        .doOnNext { handleState(it) }
                        .map { AddHealthViewUpdatePMAction(it) }
            }

    private fun createWeightEditedEvent(): ObservableTransformer<AddHealthViewWeightEditedEvent, AddHealthViewAction> =
            ObservableTransformer {
                it.zipWith(it.flatMap { state }, BiFunction { event: AddHealthViewWeightEditedEvent, state: AddHealthViewPM -> state.copy(
                        weight = event.weight,
                        weightError = if((event.weight == null) or (event.weight ?: -1.0 > 100.0) or (event.weight ?: -1.0 <= 0)) "Ошибка в написании, величина больше 100 или пустое поле" else null
                ) }).doOnNext { handleState(it) }
                        .map { AddHealthViewUpdatePMAction(it) }
            }

    private fun createGrowthEditedEvent(): ObservableTransformer<AddHealthViewGrowthEditedEvent, AddHealthViewAction> =
            ObservableTransformer {
                it.zipWith(it.flatMap { state }, BiFunction { event: AddHealthViewGrowthEditedEvent, state: AddHealthViewPM -> state.copy(
                        growth = event.growth,
                        growthError = if((event.growth == null) or (event.growth ?: -1.0 > 100.0) or (event.growth ?: -1.0 <= 0)) "Ошибка в написании, величина больше 100 или пустое поле" else null
                ) }).doOnNext { handleState(it) }
                        .map { AddHealthViewUpdatePMAction(it) }
            }

    private fun createSaveHealthButtonClickedEvent(): ObservableTransformer<AddHealthViewSaveHealthButtonClickedEvent, AddHealthViewAction> =
            ObservableTransformer {
                it.flatMap { state }
                        .filter { (it.weightError == null) and (it.growthError == null) }
                        .doOnNext { healthInteractor.put(Health(
                                    weight = it.weight!!,
                                    growth = it.growth!!,
                                    coefficient = healthInteractor.getHealthDelta(it.weight, it.growth, pet!!)!!,
                                    date = System.currentTimeMillis()
                                ), pet).subscribe { handleViewAction(AddHealthViewNotifyHealthSavedAction()) } }
                        .map { AddHealthViewSaveHealthAction() }
            }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        handleViewEvent(AddHealthViewInitializeEvent())
    }

    override fun isSkipViewAction(viewAction: AddHealthViewAction): Boolean = viewAction is AddHealthViewSkipAction

    override fun createSharedList(shared: Observable<AddHealthViewEvent>): List<Observable<AddHealthViewAction>> =
            listOf(
                    shared.ofType(AddHealthViewInitializeEvent::class.java).compose(createInitializeHandler()),
                    shared.ofType(AddHealthViewWeightEditedEvent::class.java).compose(createWeightEditedEvent()),
                    shared.ofType(AddHealthViewGrowthEditedEvent::class.java).compose(createGrowthEditedEvent()),
                    shared.ofType(AddHealthViewSaveHealthButtonClickedEvent::class.java).compose(createSaveHealthButtonClickedEvent())
            )
}