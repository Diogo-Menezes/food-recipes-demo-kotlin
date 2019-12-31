package com.example.foodrecipesdemokotlin.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job


enum class Status() {
    NO_RESULTS,
    LOADING,
    ERROR,
    DONE,
    NONE,
    LOADING_WITH_DATA
}

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var app: Application = application

    protected var viewModelJob: CompletableJob = Job()

    //USED TO UPDATE THE TITLE
    protected val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    //USED FOR TRACKING THE INPUT IN SEARCH VIEW
    protected val _searchView = MutableLiveData<String>()
    val searchView: LiveData<String>
        get() = _searchView

    //USED TO UPDATE THE LOADING TEXT
    protected val _query = MutableLiveData<String>()
    val query: LiveData<String>
        get() = _query

    protected val _pageNumber = MutableLiveData<String>()

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    protected fun setStatus(status: Status) {
        _status.value = status
    }

    fun isConnectedToTheInternet(): Boolean {

        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {
            Log.e("SessionManager", "isConnectedToTheInternet (line 72): ${e.message}")
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}