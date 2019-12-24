package com.example.foodrecipesdemokotlin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


enum class Status() {
    LOADING,
    ERROR,
    DONE,
    NONE
}

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected var viewModelJob = Job()
    protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    protected val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _status.value = Status.NONE
    }


    protected fun setStatus(status: Status) {
        _status.value = status
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}