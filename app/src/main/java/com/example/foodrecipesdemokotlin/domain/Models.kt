package com.example.foodrecipesdemokotlin.domain

data class Recipe(
    val ingredients: Array<String>?,
    val imageUrl: String,
    val socialRank: Float,
    val publisher: String,
    val recipeId: String,
    val title: String
)

data class RecipeList(
    val title: String,
    val publisher: String,
    val socialRank: Float,
    val imageUrl: String
)
