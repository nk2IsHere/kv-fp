package ga.nk2ishere.dev.fluffypatrol.common.dependencies

import ga.nk2ishere.dev.fluffypatrol.common.interactors.DogInteractor
import ga.nk2ishere.dev.fluffypatrol.common.interactors.HealthInteractor
import org.koin.dsl.module.module

val interactorsModule = module {
    single { DogInteractor(get(), get("dog")) }
    single { HealthInteractor(get(), get("health"), get()) }
}