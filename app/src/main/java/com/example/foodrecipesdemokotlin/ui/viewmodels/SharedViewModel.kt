package com.example.foodrecipesdemokotlin.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.database.asDomainModel
import com.example.foodrecipesdemokotlin.database.getDatabase
import com.example.foodrecipesdemokotlin.domain.Recipe
import com.example.foodrecipesdemokotlin.domain.RecipeList
import com.example.foodrecipesdemokotlin.repository.RecipeRepository
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : BaseViewModel(application) {

    private val repository = RecipeRepository(getDatabase(application))


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
        setStatus(Status.LOADING)
    }

    fun completedNavigationToRecipeList() {
        _category.value = null
    }


    //RECIPE LIST FRAGMENT
    private val _recipeList = MutableLiveData<List<RecipeList>>()
    val recipeList: LiveData<List<RecipeList>>
        get() = _recipeList

    init {
        _recipeList.value = ArrayList()
    }

    val list: LiveData<List<DataBaseRecipe>> = Transformations.switchMap(_recipeList) {
        repository.getRecipes(_query.value!!,_pageNumber.value!!)
    }
    fun getRecipeList(query: String, page: String = "1") =
        repository.getRecipes(query, page).map {
            setStatus(Status.DONE)
            it
        }

//    private fun getRecipeList(query: String, page: String = "1") {
//        Log.i("SharedViewModel", "getRecipeList: called \nQuery:$query\nPage: $page")
//        viewModelScope.launch {
//            if (status.value != Status.LOADING) setStatus(Status.LOADING)
//            val search =
//                Log.i("SharedViewModel", "getRecipeList: ${search.value}")
//
//            liveData() {
//                repository.getRecipes(query, page)
//                emit(search)
//                search.map {
//                    _recipeList.value = it.asDomainModel()
//                }
//                Log.i("SharedViewModel", "getRecipeList: ${search.value}")
//            }
//
//
///*                }
//            } else {
//                _recipeList.value = ArrayList()
//            }
//            search.map { resource ->
//                when (resource.status) {
//                    ResourceStatus.SUCCESS -> {
//                        setStatus(Status.DONE)
//                        _recipeList.value = resource.data?.asDomainModel()
//                    }
//                    ResourceStatus.ERROR -> {
//                        setStatus(Status.ERROR)
//                        _recipeList.value = resource.data?.asDomainModel() ?: ArrayList()
//                    }
//                    ResourceStatus.LOADING -> {
//                        setStatus(Status.LOADING)
//                        _recipeList.value = resource.data?.asDomainModel()
//                    }
//                }*/
//
//        }
//    }


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
