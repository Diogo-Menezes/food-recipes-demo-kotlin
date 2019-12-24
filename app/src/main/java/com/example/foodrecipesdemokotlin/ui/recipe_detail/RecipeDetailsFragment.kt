package com.example.foodrecipesdemokotlin.ui.recipe_detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailsFragment : BaseFragment() {

    companion object {
        fun newInstance() = RecipeDetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        subscribeUi()
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    private fun subscribeUi() {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //get recipedetails
        super.onViewCreated(view, savedInstanceState)
    }

    override fun searchQuery(query: String) {

    }
}
