package ga.nk2ishere.dev.fluffypatrol.ui.addpet

import android.net.Uri
import com.github.dimsuz.diffdispatcher.annotations.DiffElement
import ga.nk2ishere.dev.base.GlobalEvent
import ga.nk2ishere.dev.base.MviView
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import java.util.*

interface AddPetView: MviView<AddPetViewAction>

@DiffElement(diffReceiver = AddPetViewPMRenderer::class)
data class AddPetViewPM(
        val name: String? = null,
        val photo: UUID? = null,
        val age: Int? = null,
        val type: Dog.Type? = null,
        val gender: Dog.Gender? = null,
        //----//
        val nameError: String? = "Не может быть пустым",
        val ageError: String? = "Не может быть пустым",
        val photoError: String? = null,

        val hasErrors: Boolean = true,
        //----//
        val photoName: UUID? = null,
        val photoPath: String? = null
)

interface AddPetViewPMRenderer {
    fun renderNameError(nameError: String?)
    fun renderAgeError(ageError: String?)
    fun renderPhotoError(photoError: String?)
    fun renderPhoto(photo: UUID?)
}

sealed class AddPetViewEvent

class AddPetViewInitializeEvent: AddPetViewEvent()
class AddPetViewPhotoButtonClickedEvent: AddPetViewEvent()
class AddPetViewPhotoEditedEvent: AddPetViewEvent()
data class AddPetViewNameEditedEvent(val name: String): AddPetViewEvent()
data class AddPetViewAgeEditedEvent(val age: Int): AddPetViewEvent()
data class AddPetViewGenderEditedEvent(val gender: Boolean): AddPetViewEvent()
data class AddPetViewTypeEditedEvent(val type: String): AddPetViewEvent()
class AddPetViewSavePetButtonClickedEvent: AddPetViewEvent()

sealed class AddPetViewAction

data class AddPetViewUpdatePMAction(val pm: AddPetViewPM): AddPetViewAction()

sealed class AddPetViewSkipAction: AddPetViewAction()

class AddPetViewSavePetAction: AddPetViewSkipAction()
data class AddPetViewOpenCameraAction(val pm: AddPetViewPM): AddPetViewSkipAction()
class AddPetViewNotifyPetSavedAction: AddPetViewSkipAction()

class AddPetViewPetSavedGlobalEvent: GlobalEvent()