package ga.nk2ishere.dev.fluffypatrol.common.dependencies

import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.data.Health
import ga.nk2ishere.dev.fluffypatrol.common.data.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val storageModule = module {
    single<BoxStore>("store") { MyObjectBox.builder()
                .androidContext(androidContext())
                .build() }

    single<Box<Dog>>("dog") { get<BoxStore>("store").boxFor(Dog::class.java) }
    single<Box<Health>>("health") { get<BoxStore>("store").boxFor(Health::class.java) }
}