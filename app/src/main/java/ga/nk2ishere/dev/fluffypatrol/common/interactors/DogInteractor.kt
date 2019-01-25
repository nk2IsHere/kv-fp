package ga.nk2ishere.dev.fluffypatrol.common.interactors

import android.content.Context
import com.jakewharton.rxrelay2.PublishRelay
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog_
import ga.nk2ishere.dev.utils.applySchedulers
import io.objectbox.Box
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

class DogInteractor(private val context: Context, private val box: Box<Dog>) {

    fun put(dog: Dog) =
            Completable.fromCallable { box.put(dog) }
                    .applySchedulers()

    fun read(id: Long) =
            Single.just(box.query()
                    .equal(Dog_.id, id)
                    .build()
                    .findFirst())
                    .applySchedulers()

    fun read() =
            Single.fromCallable { box.all }
                    .applySchedulers()

    fun remove(id: Long) =
            Completable.fromCallable { box.remove(id) }
                    .applySchedulers()

    fun remove(dog: Dog) =
            Completable.fromCallable { box.remove(dog) }
                    .applySchedulers()

    fun remove(dogs: Collection<Dog>) =
            Completable.fromCallable { box.remove(dogs) }
                    .applySchedulers()
}
