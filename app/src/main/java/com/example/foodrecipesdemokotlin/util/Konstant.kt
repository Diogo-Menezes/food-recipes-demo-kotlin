package com.example.foodrecipesdemokotlin.util

import androidx.core.content.res.TypedArrayUtils.getString
import com.example.foodrecipesdemokotlin.R


object Konstant {
    //PREFERENCES
    const val PREFERENCES = "com.diogomenezes.foodrecipesdemo.preferences"

    //CATEGORIES
    val DEFAULT_CATEGORIES_NAMES = arrayOf(
        R.string.steak,
        R.string.breakfast,
        R.string.chicken,
        R.string.beef,
        R.string.brunch,
        R.string.dinner,
        R.string.wine,
        R.string.italian,
        R.string.vegan
    )
    val DEFAULT_CATEGORY_IMAGES = arrayOf(
        R.drawable.steak,
        R.drawable.breakfast,
        R.drawable.chicken,
        R.drawable.beef,
        R.drawable.brunch,
        R.drawable.dinner,
        R.drawable.wine,
        R.drawable.italian,
        R.drawable.vegan
    )
}