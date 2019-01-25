package ga.nk2ishere.dev.fluffypatrol.ui.addhealth

import com.github.dimsuz.diffdispatcher.annotations.DiffElement
import ga.nk2ishere.dev.base.GlobalEvent
import ga.nk2ishere.dev.base.MviView

interface AddHealthView: MviView<AddHealthViewAction>

@DiffElement(diffReceiver = AddHealthViewPMRenderer::class)
data class AddHealthViewPM(
        val weight: Double? = null,
        val growth: Double? = null,
        //-----//
        val weightError: String? = "Не может быть пустым",
        val growthError: String? = "Не может быть пустым"
)

interface AddHealthViewPMRenderer {
    fun renderWeightError(weightError: String?)
    fun renderGrowthError(growthError: String?)
}

sealed class AddHealthViewEvent

class AddHealthViewInitializeEvent: AddHealthViewEvent()
data class AddHealthViewWeightEditedEvent(val weight: Double?): AddHealthViewEvent()
data class AddHealthViewGrowthEditedEvent(val growth: Double?): AddHealthViewEvent()
class AddHealthViewSaveHealthButtonClickedEvent: AddHealthViewEvent()


sealed class AddHealthViewAction

data class AddHealthViewUpdatePMAction(val pm: AddHealthViewPM): AddHealthViewAction()

sealed class AddHealthViewSkipAction: AddHealthViewAction()

class AddHealthViewSaveHealthAction: AddHealthViewSkipAction()
class AddHealthViewNotifyHealthSavedAction: AddHealthViewSkipAction()

class AddHealthViewNotifyHealthSavedGlobalEvent: GlobalEvent()