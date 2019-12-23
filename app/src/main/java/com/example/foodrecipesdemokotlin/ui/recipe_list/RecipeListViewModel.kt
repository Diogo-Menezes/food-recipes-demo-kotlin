package com.example.foodrecipesdemokotlin.ui.recipe_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesdemokotlin.models.Recipe
import com.example.foodrecipesdemokotlin.network.RecipeApi
import com.example.foodrecipesdemokotlin.ui.BaseViewModel
import com.example.foodrecipesdemokotlin.ui.Status
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class RecipeListViewModel() : BaseViewModel() {

    private val _recipeList = MutableLiveData<List<Recipe>>()
    val recipeList: LiveData<List<Recipe>>
        get() = _recipeList

    init {
        //test
        getRecipeList("chicken", "1")
    }

    private fun getRecipeList(query: String, page: String) {
        coroutineScope.launch {
            var getDeferredRecipes: Deferred<List<Recipe>> =
                RecipeApi.retrofitService.searchRecipes(query = query, page = page)
            try {
                setStatus(Status.LOADING)
                val listResult = getDeferredRecipes.await()
                setStatus(Status.DONE)
                _recipeList.value = listResult
            } catch (e: Exception) {
                setStatus(Status.ERROR)
                //return cached data if possible
                _recipeList.value = ArrayList()
            }
        }
    }
}