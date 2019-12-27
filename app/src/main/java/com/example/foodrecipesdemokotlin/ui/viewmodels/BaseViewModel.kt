package com.example.foodrecipesdemokotlin.ui.viewmodels

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

    protected val _query = MutableLiveData<String>()
    val query: LiveData<String>
        get() = _query

    protected val _searchView = MutableLiveData<String>()
    val searchView: LiveData<String>
        get() = _searchView

    protected val _pageNumber = MutableLiveData<String>()


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