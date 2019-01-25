package ga.nk2ishere.dev.base

import com.jakewharton.rxrelay2.PublishRelay
import ga.nk2ishere.dev.utils.subscribeD
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class GlobalEventRelay(private val relay: PublishRelay<GlobalEvent> = PublishRelay.create<GlobalEvent>()) {
    val disposable = CompositeDisposable()

    internal fun subscribe(onNext: (event: GlobalEvent) -> Unit) {
        relay.subscribeD(onNext, {}, disposable)
    }

    fun acceptGlobalEvent(event: GlobalEvent) {
        Timber.tag("APPLICATION -> GE").i(event.toString())
        relay.accept(event)
    }
}

open class GlobalEvent