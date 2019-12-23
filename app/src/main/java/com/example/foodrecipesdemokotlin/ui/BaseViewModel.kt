package com.example.foodrecipesdemokotlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodrecipesdemokotlin.ui.Status.NONE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


enum class Status {
    LOADING,
    ERROR,
    DONE,
    NONE
}

abstract class BaseViewModel : ViewModel() {

    protected var viewModelJob = Job()
    protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    protected val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _status.value = NONE
    }


    protected fun setStatus(status: Status) {
        _status.value = status
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}