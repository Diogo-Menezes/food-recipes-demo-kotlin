package com.example.foodrecipesdemokotlin.ui.recipe_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesdemokotlin.domain.RecipeList
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.RecipeApi
import com.example.foodrecipesdemokotlin.network.asDomainModel
import com.example.foodrecipesdemokotlin.ui.BaseViewModel
import com.example.foodrecipesdemokotlin.ui.Status
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class RecipeListViewModel() : BaseViewModel() {

    private val _recipeList = MutableLiveData<List<RecipeList>>()
    val recipeList: LiveData<List<RecipeList>>
        get() = _recipeList

    init {
        //test
        getRecipeList("chicken", "1")
    }

    private fun getRecipeList(query: String, page: String) {
        coroutineScope.launch {
            val getDeferredRecipes: Deferred<NetworkRecipesContainer> =
                RecipeApi.retrofitService.searchRecipesAsync(query = query, page = page)
            try {
                setStatus(Status.LOADING)
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
}