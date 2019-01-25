package ga.nk2ishere.dev.fluffypatrol.ui.addpet

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.arellomobile.mvp.InjectViewState
import ga.nk2ishere.dev.base.MviPresenter
import ga.nk2ishere.dev.fluffypatrol.BuildConfig
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.interactors.DogInteractor
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.File
import java.util.*

@InjectViewState
class AddPetPresenter(val context: Context?): MviPresenter<AddPetViewAction, AddPetViewEvent, AddPetView, AddPetViewPM>(), KoinComponent {
    override val TAG: String = "ADD_PET_PRESENTER"
    private val dogInteractor: DogInteractor by inject()

    private fun createInitializeHandler(): ObservableTransformer<AddPetViewInitializeEvent, AddPetViewAction> =
            ObservableTransformer {
                it.map { AddPetViewPM() }
                        .doOnNext { handleState(it) }
                        .map { AddPetViewUpdatePMAction(it) }
            }

    private fun createEditPhotoHandler(): ObservableTransformer<AddPetViewPhotoEditedEvent, AddPetViewAction> =
            ObservableTransformer {
                it.flatMap { state }
                        .map { it.copy(photoName = null, photoPath = null) }
                        .doOnNext { handleState(it) }
                        .map { AddPetViewUpdatePMAction(it) }
            }

    private fun createNameEditedHandler(): ObservableTransformer<AddPetViewNameEditedEvent, AddPetViewAction> =
            ObservableTransformer {
                Observable.zip(it, it.flatMap { state }, BiFunction { event: AddPetViewNameEditedEvent, pm: AddPetViewPM ->event to pm })
                        .map { it.second.copy(
                            name = it.first.name,
                            nameError = if(it.first.name.isBlank() or it.first.name.isEmpty()) "Не дожно быть пустым!" else null
                        ) }
                        .doOnNext { handleState(it) }
                        .map { AddPetViewUpdatePMAction(it) }
            }

    private fun createAgeEditedHandler(): ObservableTransformer<AddPetViewAgeEditedEvent, AddPetViewAction> =
            ObservableTransformer {
                Observable.zip(it, it.flatMap { state }, BiFunction { event: AddPetViewAgeEditedEvent, pm: AddPetViewPM -> event to pm })
                        .map { it.second.copy(
                            age = it.first.age,
                            ageError = if((it.first.age < 0) or (it.first.age > 20)) "Не дожно быть пустым, большим 20 или отрицательным!" else null
                        ) }
                        .doOnNext { handleState(it) }
                        .map { AddPetViewUpdatePMAction(it) }
            }

    private fun createGenderEditedHandler(): ObservableTransformer<AddPetViewGenderEditedEvent, AddPetViewAction> =
            ObservableTransformer {
                Observable.zip(it, it.flatMap { state }, BiFunction { event: AddPetViewGenderEditedEvent, pm: AddPetViewPM -> event to pm })
                        .map { it.second.copy(gender = if(it.first.gender) Dog.Gender.F else Dog.Gender.M) }
                        .doOnNext { handleState(it) }
                        .map { AddPetViewUpdatePMAction(it) }
            }

    private fun createTypeEditedHandler(): ObservableTransformer<AddPetViewTypeEditedEvent, AddPetViewAction> =
            ObservableTransformer {
                Observable.zip(it, it.flatMap { state }, BiFunction { event: AddPetViewTypeEditedEvent, pm: AddPetViewPM -> event to pm })
                        .map { it.second.copy(type = Dog.Type.values().first { value -> value.prettyName == it.first.type }) }
                        .doOnNext { handleState(it) }
                        .map { AddPetViewUpdatePMAction(it) }
            }

    private fun createPhotoButtonClickedHandler(): ObservableTransformer<AddPetViewPhotoButtonClickedEvent, AddPetViewAction> =
            ObservableTransformer {
                it.flatMap { state }
                        .filter { context != null }
                        .map {
                            val photoName = UUID.randomUUID()
                            it.copy(
                                    photo = photoName,
                                    photoPath = File(context!!.filesDir, "$photoName.jpg").absolutePath
                            )
                        }
                        .doOnNext { handleState(it) }
                        .map { AddPetViewOpenCameraAction(it) }
            }

    private fun createSavePetButtonClicked(): ObservableTransformer<AddPetViewSavePetButtonClickedEvent, AddPetViewAction> =
            ObservableTransformer {
                it.flatMap { state }
                        .map { it.copy(
                                gender = it.gender ?: Dog.Gender.M,
                                type = it.type ?: Dog.Type.GOLDEN_RETRIVER,
                                photoError = if(it.photo == null) "Фотография не выбрана" else null,
                                hasErrors = (it.nameError != null) or (it.ageError != null) or (it.photo == null)
                        ) }
                        .doOnNext { handleState(it) }
                        .doOnNext { handleViewAction(AddPetViewUpdatePMAction(it)) }
                        .filter { !it.hasErrors }
                        .doOnNext { dogInteractor.put(Dog(
                                name = it.name!!,
                                age = it.age!!,
                                gender = it.gender!!,
                                type = it.type!!,
                                photo = it.photo!!
                        )).subscribe { handleViewAction(AddPetViewNotifyPetSavedAction()) } }
                        .map { AddPetViewSavePetAction() }
            }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        handleViewEvent(AddPetViewInitializeEvent())
    }

    override fun isSkipViewAction(viewAction: AddPetViewAction): Boolean = viewAction is AddPetViewSkipAction

    override fun createSharedList(shared: Observable<AddPetViewEvent>): List<Observable<AddPetViewAction>> =
            listOf(
                    shared.ofType(AddPetViewInitializeEvent::class.java).compose(createInitializeHandler()),
                    shared.ofType(AddPetViewPhotoEditedEvent::class.java).compose(createEditPhotoHandler()),
                    shared.ofType(AddPetViewNameEditedEvent::class.java).compose(createNameEditedHandler()),
                    shared.ofType(AddPetViewAgeEditedEvent::class.java).compose(createAgeEditedHandler()),
                    shared.ofType(AddPetViewGenderEditedEvent::class.java).compose(createGenderEditedHandler()),
                    shared.ofType(AddPetViewTypeEditedEvent::class.java).compose(createTypeEditedHandler()),
                    shared.ofType(AddPetViewPhotoButtonClickedEvent::class.java).compose(createPhotoButtonClickedHandler()),
                    shared.ofType(AddPetViewSavePetButtonClickedEvent::class.java).compose(createSavePetButtonClicked())
            )
}