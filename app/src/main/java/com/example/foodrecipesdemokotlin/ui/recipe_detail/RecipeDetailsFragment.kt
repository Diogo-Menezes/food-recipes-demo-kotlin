package com.example.foodrecipesdemokotlin.ui.recipe_detail


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.domain.Recipe
import com.example.foodrecipesdemokotlin.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_recipe_detail.*
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailsFragment : BaseFragment() {

    lateinit var ingredientsContainer: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        displayHomeUp(true)
        subscribeUi()
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    private fun subscribeUi() {
        viewModel.recipe.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                Log.i("RecipeDetailsFragment", "subscribeUi: $recipe")
                setDetails(recipe)
            }
        })
    }

    private fun setDetails(recipe: Recipe) {
        activity?.title = recipe.title
        Glide.with(this)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
            )
            .load(recipe.imageUrl)
            .into(recipe_detail_image)

        recipe_detail_title.text = recipe.title
        recipe_detail_publisher.text = recipe.publisher
        recipe_detail_social_score.text = recipe.socialRank.roundToInt().toString()

        ingredientsContainer = ingredients_container
        recipe.ingredients?.forEach {
            val view = TextView(context!!)
            view.text = getString(R.string.ingredient, it)
            ingredientsContainer.addView(view)
        }


    }

    private fun createIngredientsView() {

    }
}
