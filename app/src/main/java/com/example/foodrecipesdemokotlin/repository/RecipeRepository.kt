package com.example.foodrecipesdemokotlin.repository

import androidx.lifecycle.LiveData
import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.database.RecipesDatabase
import com.example.foodrecipesdemokotlin.network.NetworkRecipeContainer
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.RecipeApi
import com.example.foodrecipesdemokotlin.network.asDatabaseModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeRepository(private val database:RecipesDatabase) {

    fun getRecipeList(query: String, page: String): Deferred<NetworkRecipesContainer> {
        val deferred = RecipeApi.retrofitService.searchRecipesAsync(query = query, page = page)

        GlobalScope.launch {
            val list = deferred.await().asDatabaseModel()
            insertRecipes(list)
        }

        return deferred
    }

    private fun insertRecipes(list: Array<DataBaseRecipe>) {
        database.recipeDao.insertRecipes(*list)
    }

    fun loadFromCache(query: String, page: String = "1"): LiveData<List<DataBaseRecipe>> {
        return database.recipeDao.getRecipes(query, page.toInt())
    }

    fun getRecipe(recipeId: String): Deferred<NetworkRecipeContainer> {
        return RecipeApi.retrofitService.getRecipeAsync(recipeId = recipeId)
    }


//    companion object {
//        @Volatile
//        private var instance: RecipeRepository? = null
//
//        fun getInstance(context: Context) =
//            instance ?: synchronized(this) {
//                instance ?: RecipeRepository(context).also { instance = it }
//            }
//    }

}