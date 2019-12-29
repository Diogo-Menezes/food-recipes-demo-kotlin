package com.example.foodrecipesdemokotlin.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://recipesapi.herokuapp.com/api/"

interface RecipeApiService {
    @GET("search")
    suspend fun searchRecipes(
        @Query("key") key: String = "",
        @Query("q") query: String,
        @Query("page") page: String
    ): ApiResponse<NetworkRecipesContainer>


    @GET("get")
    suspend fun getRecipe(
        @Query("key") key: String = "",
        @Query("rId") recipeId: String
    ): ApiResponse<NetworkRecipeContainer>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object RecipeApi {
    val retrofitService: RecipeApiService by lazy { retrofit.create(RecipeApiService::class.java) }
}