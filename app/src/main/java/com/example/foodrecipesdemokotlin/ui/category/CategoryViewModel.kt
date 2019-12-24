package com.example.foodrecipesdemokotlin.ui.category

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesdemokotlin.viewmodels.BaseViewModel
import com.example.foodrecipesdemokotlin.viewmodels.Status

class CategoryViewModel(application: Application) : BaseViewModel(application) {

    private val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category

    fun setCategory(string: String) {
        setStatus(Status.LOADING)
        _category.value = string
    }

    fun finishedNavigation() {
        _category.value = null
    }

    override fun onCleared() {
        super.onCleared()
    }
}