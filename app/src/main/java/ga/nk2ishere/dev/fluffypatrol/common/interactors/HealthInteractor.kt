package ga.nk2ishere.dev.fluffypatrol.common.interactors

import android.content.Context
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.data.Health
import ga.nk2ishere.dev.fluffypatrol.common.data.Health_
import ga.nk2ishere.dev.utils.applySchedulers
import io.objectbox.Box
import io.reactivex.Completable
import io.reactivex.Single

class HealthInteractor(private val context: Context, private val box: Box<Health>, private val dogInteractor: DogInteractor) {

    fun put(health: Health, dog: Dog) =
            Completable.fromCallable {
                health.dog.target = dog
                dog.health.add(health)
            }.andThen(dogInteractor.put(dog))
                    .applySchedulers()

    fun put(health: Health) =
            Completable.fromCallable { box.put(health) }
                    .applySchedulers()

    fun read(id: Long) =
            Single.just(box.query()
                    .equal(Health_.id, id)
                    .build()
                    .findFirst())
                    .applySchedulers()

    fun read(dog: Dog) =
            dogInteractor.read(dog.id)
                    .map { it.health.toList() }

    fun remove(id: Long, dog: Dog) =
            read(id).flatMapCompletable { Completable.fromCallable { dog.health.remove(it) } }
                    .applySchedulers()

    fun remove(health: Health, dog: Dog) =
            Completable.fromCallable { dog.health.remove(health) }
                    .applySchedulers()

    fun remove(healths: Collection<Health>, dog: Dog) =
            Completable.fromCallable { healths.forEach { dog.health.remove(it) } }
                    .applySchedulers()

    fun getHealthDelta(weight: Double, growth: Double, pet: Dog): Double? = pet.let {
        val normalGrowth = it.type.health[it.gender]?.growth?.get(0) ?: return null
        val normalWeight = it.type.health[it.gender]?.weight?.get(0) ?: return null

        val weightCoefficients = when {
            weight < normalWeight.start -> weight / normalWeight.start
            weight > normalWeight.endInclusive -> normalWeight.endInclusive / weight
            weight in normalWeight -> 1.0
            else -> null
        }
        val growthCoefficients = when {
            growth < normalGrowth.start -> growth / normalGrowth.start
            growth > normalGrowth.endInclusive -> normalGrowth.endInclusive / growth
            growth in normalGrowth -> 1.0
            else -> null
        }

        return@let ((weightCoefficients ?: return@let null) + (growthCoefficients ?: return@let null)) / 2
    }
}