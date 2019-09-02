package com.seoul.ttarawa.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    /**
     * 프로그레스바
     */
    protected val _isVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean>
        get() = _isVisible

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}