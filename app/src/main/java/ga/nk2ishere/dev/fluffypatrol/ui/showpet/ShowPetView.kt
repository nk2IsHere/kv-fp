package ga.nk2ishere.dev.fluffypatrol.ui.showpet

import com.github.dimsuz.diffdispatcher.annotations.DiffElement
import ga.nk2ishere.dev.base.MviView
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.data.Health


interface ShowPetView: MviView<ShowPetViewAction>

@DiffElement(ShowPetViewPMRenderer::class)
data class ShowPetViewPM(
        val currentPet: PetState? = null,
        val currentHealth: Health? = null,
        val healths: List<Health> = listOf()
) {
    data class PetState(
            val name: String? = null,
            val age: String? = null,
            val type: Dog.Type? = null,
            val gender: Dog.Gender? = null
    )
}

interface ShowPetViewPMRenderer {
    fun renderCurrentPet(currentPet: ShowPetViewPM.PetState?)
    fun renderCurrentHealth(currentHealth: Health?)
    fun renderHealths(healths: List<Health>)
}

sealed class ShowPetViewEvent

class ShowPetViewInitializeEvent: ShowPetViewEvent()
class ShowPetViewAddHealthButtonClickedEvent: ShowPetViewEvent()
class ShowPetViewHealthSavedEvent: ShowPetViewEvent()

sealed class ShowPetViewAction

data class ShowPetViewUpdatePMAction(val pm: ShowPetViewPM): ShowPetViewAction()

sealed class ShowPetViewSkipAction: ShowPetViewAction()

class ShowPetViewShowAddHealthAction: ShowPetViewSkipAction()