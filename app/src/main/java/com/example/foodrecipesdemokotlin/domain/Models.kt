package com.example.foodrecipesdemokotlin.domain

import com.example.foodrecipesdemokotlin.util.Konstant

data class Recipe(
    val ingredients: Array<String>?,
    val imageUrl: String,
    val socialRank: Float,
    val publisher: String,
    val recipeId: String,
    val title: String,
    val favorite: Boolean = false
)

data class RecipeList(
    val recipeId: String,
    val title: String,
    val publisher: String,
    val socialRank: Float,
    val imageUrl: String
)

data class Category(
    val categoryName: String,
    val imageUrl: Int
)

fun insertNoResultsRecipe(query: String): List<RecipeList> {
    return listOf(
        RecipeList(
            recipeId = "",
            title = query,
            publisher = Konstant.NO_RESULTS,
            socialRank = 0f,
            imageUrl = ""
        )
    )
}
