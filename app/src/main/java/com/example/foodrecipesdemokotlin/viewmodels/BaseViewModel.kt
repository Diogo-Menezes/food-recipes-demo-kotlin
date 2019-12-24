package com.example.foodrecipesdemokotlin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


enum class Status() {
    NO_RESULTS,
    LOADING,
    ERROR,
    DONE,
    NONE
}

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    protected fun setStatus(status: Status) {
        _status.value = status
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}