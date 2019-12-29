package com.example.foodrecipesdemokotlin.repository

import NetworkBoundResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.foodrecipesdemokotlin.database.RecipesDatabase
import com.example.foodrecipesdemokotlin.database.asDomainModel
import com.example.foodrecipesdemokotlin.network.ApiResponse
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.network.RecipeApi

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

    fun loadFromCache(query: String, page: String = "1"): LiveData<List<DataBaseRecipe>> {
        return recipeDao.getRecipes(query, page.toInt())
    }

    fun getAllRecipes(): LiveData<List<DataBaseRecipe>> {
        return recipeDao.getAllRecipes()
    }

    fun loadRecipeFromCache(recipeId: String): LiveData<DataBaseRecipe> {
        return recipeDao.getRecipe(recipeId)
    }*/


    fun getRecipes(query: String, page: String): LiveData<Resource<NetworkRecipesContainer>> {
        return object :
            NetworkBoundResource<NetworkRecipesContainer, NetworkRecipesContainer>() {
            override fun saveCallResult(item: NetworkRecipesContainer) {

            }

            override fun shouldFetch(data: NetworkRecipesContainer?): Boolean {

            }

            override fun loadFromDb(): LiveData<NetworkRecipesContainer> {
                val cache = recipeDao.getRecipes(query, page.toInt()).map {it}
                return cache
            }

            override suspend fun createCall(): ApiResponse<NetworkRecipesContainer> {
                return RecipeApi.retrofitService.searchRecipes(query = query, page = page)
            }

        }.asLiveData()

    }
}