package ga.nk2ishere.dev.fluffypatrol.common.dependencies

import ga.nk2ishere.dev.base.GlobalEventRelay
import org.koin.dsl.module.module

val relayModule = module {
    single { GlobalEventRelay() }
}