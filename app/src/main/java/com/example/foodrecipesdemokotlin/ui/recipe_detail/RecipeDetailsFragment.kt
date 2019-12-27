package com.example.foodrecipesdemokotlin.ui.recipe_detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.domain.Recipe
import com.example.foodrecipesdemokotlin.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_recipe_detail.*

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailsFragment : BaseFragment() {

    lateinit var ingredientsContainer: LinearLayout

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
        viewModel.recipe.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                setDetails(recipe)
            }
        })
    }

    private fun setDetails(recipe: Recipe) {
        recipe_detail_title.text = recipe.title
        recipe_detail_publisher.text = recipe.publisher
        recipe_detail_social_score.text = recipe.socialRank.toString()
        ingredientsContainer = ingredients_container

    }

    private fun createIngredientsView() {

    }
}
