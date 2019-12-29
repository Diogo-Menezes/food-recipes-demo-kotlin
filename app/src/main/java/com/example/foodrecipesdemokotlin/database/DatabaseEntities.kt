package com.example.foodrecipesdemokotlin.database

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipesdemokotlin.domain.Recipe
import com.example.foodrecipesdemokotlin.domain.RecipeList


@Entity(tableName = "recipes")
data class DataBaseRecipe constructor(
    @PrimaryKey
    @ColumnInfo(name = "recipe_id")
    var recipe_id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "publisher")
    val publisher: String,

    @ColumnInfo(name = "image_url")
    val image_url: String,

    @ColumnInfo(name = "social_rank")
    val social_rank: Float,

    @ColumnInfo(name = "ingredients")
    val ingredients: Array<String>?= arrayOf(),

    @ColumnInfo(name = "timestamp")
    val timestamp: Int
)

fun List<DataBaseRecipe>.asDomainModel(): List<RecipeList> {
    return map {
        RecipeList(
            imageUrl = it.image_url,
            socialRank = it.social_rank,
            publisher = it.publisher,
            recipeId = it.recipe_id,
            title = it.title
        )
    }
}

fun DataBaseRecipe.asDomainModel(): Recipe {
    return Recipe(
        ingredients = ingredients,
        imageUrl = image_url,
        socialRank = social_rank,
        publisher = publisher,
        recipeId = recipe_id,
        title = title
    )
}