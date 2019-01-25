package ga.nk2ishere.dev.utils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeD(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
    val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
    compositeDisposable.add(disposable)
    return disposable
}

fun <T> Single<T>.subscribeD(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
    val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
    compositeDisposable.add(disposable)
    return disposable
}

fun <T> Maybe<T>.subscribeD(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
    val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
    compositeDisposable.add(disposable)
    return disposable
}

fun Completable.subscribeD(onSuccess: () -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
    val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
    compositeDisposable.add(disposable)
    return disposable
}

fun <T> Single<T>.applySchedulers() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())!!
fun <T> Observable<T>.applySchedulers() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())!!
fun Completable.applySchedulers() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())!!
fun <T> Flowable<T>.applySchedulers() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())!!