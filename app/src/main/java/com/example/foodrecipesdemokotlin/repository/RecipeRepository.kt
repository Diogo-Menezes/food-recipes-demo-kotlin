package com.example.foodrecipesdemokotlin.repository

import NetworkBoundResource
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.database.RecipesDatabase
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.RecipeApi
import com.example.foodrecipesdemokotlin.network.asDatabaseModel
import com.example.foodrecipesdemokotlin.util.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response


class RecipeRepository(database: RecipesDatabase) {

    private val recipeDao = database.recipeDao

    fun getRecipes(
        query: String,
        page: String,
        loadFromInternet: Boolean
    ): LiveData<Resource<List<DataBaseRecipe>>> {
        return object : NetworkBoundResource<List<DataBaseRecipe>, NetworkRecipesContainer>(loadFromInternet) {
            override suspend fun saveCallResult(item: NetworkRecipesContainer) {
                Log.i("RecipeRepository", "saveCallResult: called ${item.count}")
                withContext(IO) {
                    if (item.count > 0) recipeDao.insertRecipes(*item.asDatabaseModel())
                }
            }

            override fun shouldFetch(data: List<DataBaseRecipe>?): Boolean {
                val shouldFetch = true
                /*GlobalScope.launch(IO) {
                    data?.forEach { recipe ->
                        val timePassed = (System.currentTimeMillis() - recipe.timestamp)
                        shouldFetch = if (timePassed > TimeUnit.DAYS.toMillis(30)) {
                            recipeDao.insertRecipe(recipe)
                            true
                        } else {
                            false
                        }
                    }
                }*/
                Log.i("NetworkBoundResource", "shouldFetch: $shouldFetch : ${timeElapsed()}")
                return shouldFetch
            }

            override fun loadFromDb(): LiveData<List<DataBaseRecipe>> =
                recipeDao.getRecipes(query = query, page = page.toInt())


            override suspend fun createCall(): LiveData<Response<NetworkRecipesContainer>> {
                return liveData {
                    try {
                        val search =
                            RecipeApi.retrofitService.searchRecipes(query = query, page = page)
                        emit(search)
                    } catch (e: Exception) {
                        cancelJob(e.message.toString())
                        Log.i("RecipeRepository", "createCall: ${e.message}")

                    }
                }
            }
        }.asLiveData()
    }
}