package com.example.foodrecipesdemokotlin.ui.viewmodels

import android.app.Application
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
import com.example.foodrecipesdemokotlin.util.Resource
import com.example.foodrecipesdemokotlin.util.ResourceStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : BaseViewModel(application) {

    private val repository = RecipeRepository(getDatabase(application))

    //Main Activity
    fun searchQuery(searchQuery: String) {
        _searchView.value = searchQuery
        _recipeList.value = ArrayList()
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
    }

    fun completedNavigationToRecipeList() {
        _recipeList.value = ArrayList()
        setStatus(Status.LOADING)
        _category.value = null
    }


    //RECIPE LIST FRAGMENT
    val recipes = MediatorLiveData<Resource<List<DataBaseRecipe>>>()
    private val _recipeList = MutableLiveData<List<RecipeList>>()
    val recipeList: LiveData<List<RecipeList>>
        get() = _recipeList

    private lateinit var repo: LiveData<Resource<List<DataBaseRecipe>>>

    private fun getRecipeList(query: String, page: String = "1") {
        Log.i("SharedViewModel", "getRecipeList: called \nQuery:$query\nPage: $page")

        prepareSearch()
        repo = repository.getRecipes(query, page, isConnectedToTheInternet())
        viewModelScope.launch(viewModelJob) {
            if (status.value != Status.LOADING) setStatus(Status.LOADING)
            delay(500L) //To show loading animation
            observeRecipes()
        }
    }

    private fun prepareSearch() {
        if (viewModelJob.isActive) viewModelJob.cancel()
        viewModelJob = Job()
        if (::repo.isInitialized) {
            recipes.removeSource(repo)
        }
    }

    private fun observeRecipes() {
        recipes.addSource(repo) { resource ->
            Log.i("SharedViewModel", "resourceStatus: ${resource.status} ")
            when (resource.status) {
                ResourceStatus.SUCCESS -> {
                    setStatus(Status.DONE)
                    setList(resource)
                    recipes.removeSource(repo)
                }
                ResourceStatus.ERROR -> {
                    setStatus(Status.ERROR)
                }
                ResourceStatus.LOADING -> {
                    setStatus(Status.LOADING)
                    resource.data?.let {
                        _recipeList.value = it.asDomainModel()
                        setStatus(Status.LOADING_WITH_DATA)
                    }
                }
            }
        }
    }

    private fun setList(resource: Resource<List<DataBaseRecipe>>) {
        if (resource.data.isNullOrEmpty()) {
            _recipeList.value = insertNoResultsRecipe(_query.value!!)
            setStatus(Status.NO_RESULTS)
        } else {
            _recipeList.value = resource.data.asDomainModel()
        }
    }


    fun searchRecipes(query: String, page: String = "1") {
        _query.value = query
        _pageNumber.value = page
        getRecipeList(query, page)
        setTitle(query)
    }

    fun searchNextPage() {
        val query = _query.value!!
        val page = (_pageNumber.value!!.toInt().inc()).toString()
        getRecipeList(query, page)
    }

    fun completedNavigationToDetailList() {
        setStatus(Status.DONE)
    }

    fun clearList() {
        if (::repo.isInitialized) {
            recipes.removeSource(repo)
        }
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

