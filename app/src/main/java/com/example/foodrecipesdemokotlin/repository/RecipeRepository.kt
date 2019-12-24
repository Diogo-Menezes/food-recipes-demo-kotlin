package com.example.foodrecipesdemokotlin.repository

import android.content.Context
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.RecipeApi
import kotlinx.coroutines.Deferred

class RecipeRepository(context: Context) {

    fun getRecipeList(query: String, page: String): Deferred<NetworkRecipesContainer> {
        return RecipeApi.retrofitService.searchRecipesAsync(query = query, page = page)
    }


    companion object {
        @Volatile
        private var instance: RecipeRepository? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: RecipeRepository(context).also { instance = it }
            }
    }

}