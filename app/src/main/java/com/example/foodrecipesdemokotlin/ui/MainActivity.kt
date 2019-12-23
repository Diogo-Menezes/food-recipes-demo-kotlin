package com.example.foodrecipesdemokotlin.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.BaseActivity
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.recipe_list.RecipeListFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment()
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host, RecipeListFragment.newInstance())
            .commitNow()
    }
}
