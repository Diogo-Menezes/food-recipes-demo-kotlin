package com.example.foodrecipesdemokotlin.repository

import NetworkBoundResource
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.database.RecipesDatabase
import com.example.foodrecipesdemokotlin.network.NetworkRecipeContainer
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.RecipeApi
import com.example.foodrecipesdemokotlin.network.asDatabaseModel
import com.example.foodrecipesdemokotlin.util.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.concurrent.TimeUnit


class RecipeRepository(database: RecipesDatabase) {

    private val recipeDao = database.recipeDao


    suspend fun saveRecipeFavorite(recipeId: String, favorite: Boolean) {
        withContext(IO) {
            recipeDao.setRecipeFavorite(recipeId, favorite)
            Log.i("RecipeRepository", "saveRecipeFavorite: $favorite")
        }

    }


    fun getRecipe(recipeId: String, hasInternet: Boolean): LiveData<Resource<DataBaseRecipe>> {
        return object : NetworkBoundResource<DataBaseRecipe, NetworkRecipeContainer>(hasInternet) {
            override suspend fun saveCallResult(item: NetworkRecipeContainer) {
                val recipe = item.networkRecipe.asDatabaseModel()
                withContext(IO) {
                    var isFavorite = false
                    if (recipeDao.insertRecipes(recipe) == -1L) {
                        isFavorite = recipeDao.checkRecipe(recipe.recipe_id).favorite
                    }
                    recipeDao.insertRecipe(recipe)
                    recipeDao.setRecipeFavorite(recipe.recipe_id, isFavorite)
                }
            }

            override fun shouldFetch(data: DataBaseRecipe?): Boolean {
                var shouldFetch = false
                data?.let { recipe ->
                    Log.i(
                        "RecipeRepository",
                        "shouldFetch: ${recipe.timestamp} size: ${recipe.ingredients?.size}"
                    )
                    shouldFetch = when {
                        recipe.timestamp == 0L -> {
                            true
                        }
                        System.currentTimeMillis() - recipe.timestamp >= TimeUnit.DAYS.toMillis(30) -> {
                            true
                        }
                        recipe.ingredients?.size == 0 -> {
                            true
                        }
                        else -> false
                    }
                }
                Log.i("NetworkBoundResource", "shouldFetch: $shouldFetch")
                return shouldFetch
            }


            override fun loadFromDb(): LiveData<DataBaseRecipe> = recipeDao.getRecipe(recipeId)

            override suspend fun createCall(): LiveData<Response<NetworkRecipeContainer>> {

                return liveData {
                    try {
                        val response = RecipeApi.retrofitService.getRecipe(recipeId = recipeId)
                        Log.i("RecipeRepository", "createCall:${response.body()} ")
                        emit(response)
                    } catch (e: Exception) {
                        cancelJob(e.message.toString())
                        Log.i("RecipeRepository", "createCall: ${e.message}")
                    }
                }
            }

        }.asLiveData()

    }

    fun getRecipes(
        query: String,
        page: String,
        loadFromInternet: Boolean
    ): LiveData<Resource<List<DataBaseRecipe>>> {
        return object :
            NetworkBoundResource<List<DataBaseRecipe>, NetworkRecipesContainer>(loadFromInternet) {
            override suspend fun saveCallResult(item: NetworkRecipesContainer) {
                Log.i("RecipeRepository", "saveCallResult: called ${item.count}")
                withContext(IO) {
                    val recipeList = item.asDatabaseModel()
                    recipeList.forEach { recipe ->
                        if (recipeDao.insertRecipes(recipe) == -1L) {
                            //THE RECIPE ALREADY EXISTS UPDATE ONLY
                            Log.i("RecipeRepository", "saveCallResult: update ${recipe.title}")
                            recipeDao.updateRecipes(
                                recipe.recipe_id,
                                recipe.title,
                                recipe.publisher,
                                recipe.image_url,
                                recipe.social_rank
                            )
                        } else {
                            Log.i(
                                "RecipeRepository",
                                "saveCallResult: insertRecipe ${recipe.title}"
                            )
                            recipeDao.insertRecipe(recipe)
                        }
                    }
                }
            }

            override fun shouldFetch(data: List<DataBaseRecipe>?): Boolean {
                return true
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