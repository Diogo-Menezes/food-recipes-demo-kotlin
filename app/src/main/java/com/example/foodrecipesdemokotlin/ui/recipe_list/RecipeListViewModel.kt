package com.example.foodrecipesdemokotlin.ui.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesdemokotlin.domain.RecipeList
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.asDomainModel
import com.example.foodrecipesdemokotlin.repository.RecipeRepository
import com.example.foodrecipesdemokotlin.viewmodels.BaseViewModel
import com.example.foodrecipesdemokotlin.viewmodels.Status
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipeListViewModel(application: Application) : BaseViewModel(application) {

    private val repository = RecipeRepository(application)

    private val _recipeList = MutableLiveData<List<RecipeList>>()
    val recipeList: LiveData<List<RecipeList>>
        get() = _recipeList

    init {
        //test
        getRecipeList("chicken", "1")
    }

    private fun getRecipeList(query: String, page: String) {
        Log.i("RecipeListViewModel", "getRecipeList: called $query")
        coroutineScope.launch {
            val getDeferredRecipes: Deferred<NetworkRecipesContainer> =
                repository.getRecipeList(query = query, page = page)
            try {
                setStatus(Status.LOADING)
                delay(5000L)
                val listResult = getDeferredRecipes.await()
                setStatus(Status.DONE)
                _recipeList.value = listResult.asDomainModel()
            } catch (e: Exception) {
                Log.i("RecipeListViewModel", "getRecipeList: ${e.message}")
                setStatus(Status.ERROR)
                //return cached data if possible
                _recipeList.value = ArrayList()
            }
        }
    }

    fun searchRecipes(query: String, page: String) {
        getRecipeList(query, page)
        Log.i("RecipeListViewModel", "searchRecipes: called")
    }
}