package com.example.foodrecipesdemokotlin.repository

import NetworkBoundResource
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.database.RecipesDatabase
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.RecipeApi
import com.example.foodrecipesdemokotlin.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response


class RecipeRepository(private val database: RecipesDatabase) {

    private val recipeDao = database.recipeDao

    /*suspend fun getRecipeList(query: String, page: String): NetworkRecipesContainer {
        val list = RecipeApi.retrofitService.searchRecipes(query = query, page = page)
        insertRecipeList(list.asDatabaseModel())
        return list
    }

    suspend fun getRecipe(recipeId: String): NetworkRecipeContainer {
        val recipe = RecipeApi.retrofitService.getRecipe(recipeId = recipeId)
        insertRecipe(recipe.networkRecipe.asDatabaseModel())
        return recipe
    }

    private suspend fun insertRecipeList(list: Array<DataBaseRecipe>) {
        withContext(Dispatchers.IO) {
            database.recipeDao.insertRecipes(*list)
        }
    }

    private suspend fun insertRecipe(recipe: DataBaseRecipe) {
        withContext(Dispatchers.IO) {
            database.recipeDao.insertRecipe(recipe)
        }
    }
    */

    fun getRecipes(query: String, page: String): LiveData<List<DataBaseRecipe>> {
        return object : NetworkBoundResource<List<DataBaseRecipe>, NetworkRecipesContainer>() {
            override suspend fun saveCallResult(item: NetworkRecipesContainer) {
                Log.i("RecipeRepository", "saveCallResult: called")
                recipeDao.insertRecipes(*item.asDatabaseModel())
            }

            override fun shouldFetch(data: List<DataBaseRecipe>?): Boolean {
                Log.i("RecipeRepository", "shouldFetch: called")
                return true
            }

            override fun loadFromDb(): LiveData<List<DataBaseRecipe>> {
                var cache: List<DataBaseRecipe> = mutableListOf()
                GlobalScope.launch(IO) {
                    cache = recipeDao.getRecipes(query, page.toInt())
//                    Log.i("RecipeRepository", "loadFromDb: called ${cache}")
                }

                return object : LiveData<List<DataBaseRecipe>>() {
                    override fun onActive() {
                        super.onActive()
                        postValue(cache)
                    }
                }
            }

            override suspend fun createCall(): Response<NetworkRecipesContainer> {
                Log.i("RecipeRepository", "createCall: called")
                return RecipeApi.retrofitService.searchRecipes(query = query, page = page)
            }

        }.asLiveData()
    }
}