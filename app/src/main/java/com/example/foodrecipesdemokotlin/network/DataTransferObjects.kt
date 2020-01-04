package com.example.foodrecipesdemokotlin.network

import com.example.foodrecipesdemokotlin.database.DataBaseRecipe
import com.example.foodrecipesdemokotlin.domain.Recipe
import com.example.foodrecipesdemokotlin.domain.RecipeList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRecipesContainer(val count: Int, val recipes: List<NetworkRecipes>)

@JsonClass(generateAdapter = true)
data class NetworkRecipes(
    @Json(name = "_id")
    private val id: String,
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

@JsonClass(generateAdapter = true)
data class NetworkRecipeContainer(
    @Json(name = "recipe")
    val networkRecipe: NetworkRecipe
)

@JsonClass(generateAdapter = true)
data class NetworkRecipe(
    @Json(name = "ingredients")
    val ingredients: Array<String>,

    @Json(name = "image_url")
    val imageUrl: String,

    @Json(name = "social_rank")
    val socialRank: Float,

    @Json(name = "publisher")
    val publisher: String,

    @Json(name = "publisher_url")
    val publisherUrl: String,

    @Json(name = "recipe_id")
    val recipeId: String,

    @Json(name = "title")
    val title: String
) {

}

fun NetworkRecipesContainer.asDomainModel(): List<RecipeList> {
    return recipes.map {
        RecipeList(
            recipeId = it.recipeId,
            imageUrl = it.imageUrl,
            socialRank = it.socialRank,
            publisher = it.publisher,
            title = it.title
        )
    }
}

fun NetworkRecipesContainer.asDatabaseModel(): Array<DataBaseRecipe> {
    return recipes.map {
        DataBaseRecipe(
            recipe_id = it.recipeId,
            title = it.title,
            publisher = it.publisher,
            image_url = it.imageUrl,
            social_rank = it.socialRank,
            timestamp = 0L
        )
    }.toTypedArray()
}

fun NetworkRecipe.asDomainModel(): Recipe {
    return Recipe(
        ingredients = ingredients,
        imageUrl = imageUrl,
        socialRank = socialRank,
        recipeId = recipeId,
        publisher = publisher,
        title = title
    )
}

fun NetworkRecipe.asDatabaseModel(): DataBaseRecipe {
    return DataBaseRecipe(
        recipe_id = recipeId,
        title = title,
        publisher = publisher,
        image_url = imageUrl,
        social_rank = socialRank,
        ingredients = ingredients,
        timestamp = System.currentTimeMillis()
    )
}

