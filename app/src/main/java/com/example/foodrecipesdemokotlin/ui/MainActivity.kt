package com.example.foodrecipesdemokotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.foodrecipesdemokotlin.BaseActivity
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.category.CategoryFragment
import com.example.foodrecipesdemokotlin.ui.recipe_detail.RecipeDetailsFragment
import com.example.foodrecipesdemokotlin.ui.recipe_list.RecipeListFragment

class MainActivity : BaseActivity() {

    private lateinit var mSearchView: androidx.appcompat.widget.SearchView
    private lateinit var fragment: Fragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        subscribeUi()


//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

    }

    private fun subscribeUi() {
        viewModel.status.observe(this, Observer { status ->
            Log.i("MainActivity", "subscribeUi: status: $status")
            onStatusChange(status)
        })
        viewModel.searchView.observe(this, Observer { searchQuery ->
            search(searchQuery, mSearchView)
        })

        viewModel.title.observe(this, Observer {
            title = it.capitalize()
            loadingText(it.capitalize())
        })
    }


    private fun initSearchView(searchView: androidx.appcompat.widget.SearchView) {
        mSearchView.isSubmitButtonEnabled = true
        mSearchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchQuery(it.trim()) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun search(query: String, view: View) {
        supportActionBar?.collapseActionView()
        closeKeyboard(view)
        return setFragmentSearch(query)
    }

    private fun setFragmentSearch(query: String) {
        val fragmentId = navController.currentDestination!!.id
        val fragment =
            supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.primaryNavigationFragment

        return when (fragmentId) {
            R.id.recipeListFragment -> (fragment as RecipeListFragment).searchQuery(query)
            R.id.categoryListFragment -> {
                (fragment as CategoryFragment).searchQuery(query)
                navController.navigate(R.id.action_categoryListFragment_to_recipeListFragment)
            }
            R.id.recipeDetailsFragment -> {
                (fragment as RecipeDetailsFragment).searchQuery(query)
                navController.navigate(R.id.action_recipeDetailsFragment_to_recipeListFragment)
            }
            else -> {
                println("no fragment attached")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        mSearchView =
            menu?.findItem(R.id.app_bar_search)?.actionView as androidx.appcompat.widget.SearchView
        initSearchView(mSearchView);
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (navController.currentDestination!!.id == R.id.recipeListFragment) {
            viewModel.clearList()
            navController.navigate(R.id.action_recipeListFragment_to_categoryListFragment)
        } else {
            super.onBackPressed()
        }

    }
}
