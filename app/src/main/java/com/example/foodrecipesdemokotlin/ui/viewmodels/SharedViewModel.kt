package com.example.foodrecipesdemokotlin.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.database.asDomainModel
import com.example.foodrecipesdemokotlin.database.getDatabase
import com.example.foodrecipesdemokotlin.domain.Recipe
import com.example.foodrecipesdemokotlin.domain.RecipeList
import com.example.foodrecipesdemokotlin.domain.insertNoResultsRecipe
import com.example.foodrecipesdemokotlin.repository.RecipeRepository
import com.example.foodrecipesdemokotlin.repository.Resource
import com.example.foodrecipesdemokotlin.repository.ResourceStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : BaseViewModel(application) {

    private val repository = RecipeRepository(getDatabase(application))
    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("NewApi")
    private var hasInternet: Boolean = connectivityManager.isDefaultNetworkActive

    //Main Activity
    fun searchQuery(searchQuery: String) {
        _searchView.value = searchQuery
        _recipeList.value = ArrayList()
        searchRecipes(searchQuery)
    }

    fun setTitle(title: String) {
        _title.value = title
    }


    //CATEGORY FRAGMENT
    private
    val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category

    fun setCategory(string: String) {
        _category.value = string
//        setStatus(Status.LOADING)
    }

    fun completedNavigationToRecipeList() {
        _category.value = null
    }


    //RECIPE LIST FRAGMENT
    val recipes = MediatorLiveData<Resource<List<DataBaseRecipe>>>()
    private val _recipeList = MutableLiveData<List<RecipeList>>()
    val recipeList: LiveData<List<RecipeList>>
        get() = _recipeList

    init {
        _recipeList.value = ArrayList()
    }

    private fun getRecipeList(query: String, page: String = "1") {
        Log.i("SharedViewModel", "getRecipeList: called \nQuery:$query\nPage: $page")
        viewModelScope.launch {
            if (status.value != Status.LOADING) setStatus(Status.LOADING)
            val repository = repository.getRecipes(query, page, hasInternet)
            delay(1000L)
            recipes.addSource(repository) { resource ->
                Log.i("SharedViewModel", "resourceStatus: ${resource.status} ")
                when (resource.status) {
                    ResourceStatus.SUCCESS -> {
                        setStatus(Status.DONE)
                        if (resource.data.isNullOrEmpty()) {
                            _recipeList.value = insertNoResultsRecipe(_query.value!!)
                            setStatus(Status.NO_RESULTS)
                        } else {
                            _recipeList.value = resource.data.asDomainModel()
                        }
                        recipes.removeSource(repository)
                    }
                    ResourceStatus.ERROR -> {
                        setStatus(Status.ERROR)
                        resource.data?.let { _recipeList.value = resource.data.asDomainModel() }
                        _recipeList.value = ArrayList()
                        recipes.removeSource(repository)
                    }
                    ResourceStatus.LOADING -> {
                        setStatus(Status.LOADING)
                        resource.data?.let {
                            _recipeList.value = it.asDomainModel()
                            setStatus(Status.DONE)
                        }
                    }
                }
            }
        }
    }


    fun searchRecipes(query: String, page: String = "1") {
        _query.value = query
        _pageNumber.value = page
        getRecipeList(query, page)
        setTitle(query)
    }

    fun searchNextPage() {
        getRecipeList(_query.value!!, _pageNumber.value!!)
    }

    fun completedNavigationToDetailList() {

    }

    fun clearList() {
        _recipeList.value = ArrayList()
        setStatus(Status.DONE)
    }


    //Recipe Detail Fragment
    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe>
        get() = _recipe

    private fun getRecipe(recipeId: String) {
        viewModelScope.launch {}
    }

    fun setRecipeId(recipeId: String) {
        getRecipe(recipeId)
    }
}
