package com.example.foodrecipesdemokotlin.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipesdemokotlin.domain.Recipe


@Entity(tableName = "recipes")
data class DataBaseRecipe constructor(
    @PrimaryKey
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
    val ingredients: Array<String>? = null,

    @ColumnInfo(name = "timestamp")
    val timestamp: Int
)

fun List<DataBaseRecipe>.asDomainModel(): List<Recipe> {
    return map {
        Recipe(
            ingredients = it.ingredients,
            imageUrl = it.image_url,
            socialRank = it.social_rank,
            publisher = it.publisher,
            recipeId = it.recipe_id,
            title = it.title
        )
    }
}