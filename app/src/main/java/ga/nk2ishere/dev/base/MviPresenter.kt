package ga.nk2ishere.dev.base

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import timber.log.Timber

abstract class MviPresenter<VA, VE, V : MviView<VA>, S> : BasePresenter<V>() {

    protected abstract val TAG: String

    private val eventsSource = PublishRelay.create<VE>()
    private val actionsSource = PublishRelay.create<VA>()
    private val stateSource = BehaviorRelay.create<S>()

    protected val state: Observable<S> = stateSource.firstOrError().toObservable()

    abstract fun isSkipViewAction(viewAction: VA): Boolean

    abstract fun createSharedList(shared: Observable<VE>): List<Observable<VA>>


    fun handleViewEvent(viewEvent: VE) { eventsSource.accept(viewEvent) }
    protected fun handleViewAction(viewAction: VA) = actionsSource.accept(viewAction)
    protected fun handleState(state: S) = stateSource.accept(state)

    private fun subscribeToViewEvents() {
        eventsSource
                .doOnNext { Timber.tag("$TAG -> VA").i(it.toString()) }
                .compose(createEventHandlers())
                .subscribeP({ viewAction ->
                    handleViewAction(viewAction)
                }, {
                    throw it
                })
    }

    private fun subscribeToViewActions() {
        actionsSource
                .doOnNext { Timber.tag("$TAG -> VE").i(it.toString()) }
                .subscribeP({ viewAction ->
                    if (isSkipViewAction(viewAction)) {
                        viewState.applyActionWithSkip(viewAction)
                    } else {
                        viewState.applyAction(viewAction)
                    }
                }, {
                    throw it
                })
    }

    private fun createEventHandlers(): ObservableTransformer<VE, VA> {
        return ObservableTransformer { observable ->
            observable.publish { shared ->
                Observable.merge(createSharedList(shared))
            }
        }
    }

    override fun onFirstViewAttach() {
        subscribeToViewActions()
        subscribeToViewEvents()
        super.onFirstViewAttach()
    }
}
