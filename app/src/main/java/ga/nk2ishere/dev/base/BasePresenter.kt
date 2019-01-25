package ga.nk2ishere.dev.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ga.nk2ishere.dev.utils.subscribeD
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BasePresenter<View : MvpView> : MvpPresenter<View>() {
    protected val presenterDisposables = CompositeDisposable()
    protected val viewDisposables = CompositeDisposable()

    override fun onDestroy() {
        presenterDisposables.clear()
        super.onDestroy()
    }

    override fun detachView(view: View) {
        viewDisposables.clear()
        super.detachView(view)
    }

    fun <T> Observable<T>.subscribeV(onNext: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onNext, onError, viewDisposables)
    }

    fun <T> Single<T>.subscribeV(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onSuccess, onError, viewDisposables)
    }

    fun <T> Maybe<T>.subscribeV(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onSuccess, onError, viewDisposables)
    }

    fun Completable.subscribeV(onSuccess: () -> Unit, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onSuccess, onError, viewDisposables)
    }

    fun <T> Observable<T>.subscribeP(onNext: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onNext, onError, presenterDisposables)
    }

    fun <T> Single<T>.subscribeP(onSuccess: (T) -> Unit = {}, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onSuccess, onError, presenterDisposables)
    }

    fun <T> Maybe<T>.subscribeP(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onSuccess, onError, presenterDisposables)
    }

    fun Completable.subscribeP(onSuccess: () -> Unit = {}, onError: ((Throwable) -> Unit) = Timber::e): Disposable {
        return this.subscribeD(onSuccess, onError, presenterDisposables)
    }
}