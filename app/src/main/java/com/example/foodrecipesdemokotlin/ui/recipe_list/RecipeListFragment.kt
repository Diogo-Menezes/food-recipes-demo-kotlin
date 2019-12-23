package com.example.foodrecipesdemokotlin.ui.recipe_list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.R

/**
 * A simple [Fragment] subclass.
 */
class RecipeListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by lazy {
        ViewModelProviders.of(this).get(RecipeListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        subscribeUi()
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)

    }

    private fun subscribeUi() {
        viewModel.recipeList.observe(viewLifecycleOwner, Observer { recipeList ->
            for (recipe in recipeList) {
                Log.i("RecipeListFragment", "subscribeUi: ${recipe.toString()}")
            }
        })
    }


}
