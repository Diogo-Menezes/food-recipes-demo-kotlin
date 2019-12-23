package com.example.foodrecipesdemokotlin.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "recipes")
@Parcelize
data class Recipe(
    @PrimaryKey
    var recipe_id: String,
    private val title: String,
    private val publisher: String,
    private val image_url: String,
    private val social_rank: Float,
    private val ingredients: Array<String>,
    private val timestamp: Int
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (recipe_id != other.recipe_id) return false
        if (title != other.title) return false
        if (publisher != other.publisher) return false
        if (image_url != other.image_url) return false
        if (social_rank != other.social_rank) return false
        if (!ingredients.contentEquals(other.ingredients)) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recipe_id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + publisher.hashCode()
        result = 31 * result + image_url.hashCode()
        result = 31 * result + social_rank.hashCode()
        result = 31 * result + ingredients.contentHashCode()
        result = 31 * result + timestamp
        return result
    }
}