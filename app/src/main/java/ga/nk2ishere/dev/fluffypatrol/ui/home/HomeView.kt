package ga.nk2ishere.dev.fluffypatrol.ui.home

import com.github.dimsuz.diffdispatcher.annotations.DiffElement
import ga.nk2ishere.dev.base.MviView
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog

interface HomeView: MviView<HomeViewAction>

@DiffElement(diffReceiver = HomeViewPMRenderer::class)
data class HomeViewPM(
        val isTutorialPassed: Boolean,
        val pets: Collection<Dog> = listOf(),
        val clickedPet: Dog? = null
)

interface HomeViewPMRenderer {
    fun renderPets(pets: Collection<Dog>)
}

sealed class HomeViewEvent

class HomeViewInitializeEvent: HomeViewEvent()
class HomeViewShowTutorialEvent: HomeViewEvent()
class HomeViewAddPetButtonClickedEvent: HomeViewEvent()
class HomeViewPetAddedEvent: HomeViewEvent()
data class HomeViewPetClickedEvent(val pet: Dog): HomeViewEvent()

sealed class HomeViewAction

data class HomeViewUpdatePMAction(val pm: HomeViewPM): HomeViewAction()

sealed class HomeViewSkipAction : HomeViewAction()

class HomeViewShowTutorialAction(): HomeViewSkipAction()
data class HomeViewAddPetAction(val pm: HomeViewPM): HomeViewSkipAction()
data class HomeViewShowPetAction(val pm: HomeViewPM): HomeViewSkipAction()

