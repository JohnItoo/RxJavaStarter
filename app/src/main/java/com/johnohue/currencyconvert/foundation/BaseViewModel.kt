package com.johnohue.currencyconvert.foundation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel() : ViewModel() {
    protected val _hasError = MutableLiveData<Boolean>(false)


    val hasError: LiveData<Boolean>
        get() = _hasError

    protected val disposable: CompositeDisposable = CompositeDisposable()


}