package com.example.foodrecipesdemokotlin.network

import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.domain.RecipeList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRecipesContainer(val count: Int, val recipes: List<NetworkRecipeList>)

@JsonClass(generateAdapter = true)
data class NetworkRecipeList(
    @Json(name = "_id")
    val id: String,
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "publisher_url")
    val publisherUrl: String,
    @Json(name = "recipe_id")
    val recipeId: String,
    @Json(name = "social_rank")
    val socialRank: Float,
    @Json(name = "source_url")
    val sourceUrl: String,
    @Json(name = "title")
    val title: String
)

fun NetworkRecipesContainer.asDomainModel(): List<RecipeList> {
    return recipes.map {
        RecipeList(
            imageUrl = it.imageUrl,
            socialRank = it.socialRank,
            publisher = it.publisher,
            title = it.title
        )
    }
}

@JsonClass(generateAdapter = true)
data class NetworkRecipe(
    @Json(name = "_id")
    val id: String,
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "publisher_url")
    val publisherUrl: String,
    @Json(name = "recipe_id")
    val recipeId: String,
    @Json(name = "social_rank")
    val socialRank: Float,
    @Json(name = "source_url")
    val sourceUrl: String,
    @Json(name = "title")
    val title: String
)

fun NetworkRecipesContainer.asDatabaseModel(): Array<DataBaseRecipe> {
    return recipes.map {
        DataBaseRecipe(
            recipe_id = it.recipeId,
            title = it.title,
            publisher = it.publisher,
            image_url = it.imageUrl,
            social_rank = it.socialRank,
            timestamp = System.currentTimeMillis().toInt()
        )
    }.toTypedArray()
}