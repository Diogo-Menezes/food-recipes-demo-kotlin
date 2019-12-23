package com.example.foodrecipesdemokotlin.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.foodrecipesdemokotlin.BaseActivity
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.recipe_list.RecipeListViewModel

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showProgress(true)
    }
}
