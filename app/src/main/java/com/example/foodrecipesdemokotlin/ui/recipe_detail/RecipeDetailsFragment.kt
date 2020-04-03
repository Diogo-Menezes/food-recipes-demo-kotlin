package com.example.foodrecipesdemokotlin.ui.recipe_detail


import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.domain.Recipe
import com.example.foodrecipesdemokotlin.ui.BaseFragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_recipe_detail.*
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailsFragment : BaseFragment() {

    lateinit var ingredientsContainer: LinearLayout
    lateinit var mRecipe: Recipe
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subscribeUi()
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        recipe_detail_favorite_icon.setOnClickListener {
            animateChange(it)
            changeFavorite()
        }
        recipe_detail_share_icon.setOnClickListener { shareRecipe() }
        recipe_detail_close_icon.setOnClickListener { findNavController().navigateUp() }
        setScrollListener()
    }

    private fun setScrollListener() {
        val appBarLayout = recipe_detail_app_bar_layout
        val offsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            Log.i("RecipeDetailsFragment", "setScrollListener: $verticalOffset \nrange:  ${appBarLayout.totalScrollRange}")
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            recipe_detail_motion_layout.progress = seekPosition

            /*if (verticalOffset <= (-appBarLayout.totalScrollRange / 2)) {
                recipe_detail_title.visibility = INVISIBLE
                recipe_detail_title_view_background.visibility = INVISIBLE
                recipe_detail_app_bar_title.visibility = VISIBLE

            } else {
                recipe_detail_title.visibility = VISIBLE
                recipe_detail_title_view_background.visibility = VISIBLE
                recipe_detail_app_bar_title.visibility = INVISIBLE

            }*/


        }
        appBarLayout.addOnOffsetChangedListener(offsetChangedListener)
    }

    private fun changeFavorite() {
        viewModel.changeRecipeFavorite(mRecipe.recipeId, !isFavorite)
    }

    private fun shareRecipe() {

        var shareText = StringBuilder()
            .append(resources.getString(R.string.check_this_recipe) + "\n")
            .append("${mRecipe.title}\n\n")
            .append(resources.getString(R.string.ingredients) + ":\n")
        mRecipe.ingredients.apply {
            this?.forEach { shareText.append("- $it\n") }
        }


        val intent = ShareCompat.IntentBuilder.from(activity)
            .setText(shareText.toString())
            .setType("text/plain")
            .intent

        startActivity(intent)
    }


    private fun subscribeUi() {
        viewModel.getRecipe.observe(viewLifecycleOwner, Observer {
            Log.i("RecipeDetailsFragment", "subscribeUi: ${it.status}")

        })
        viewModel.recipe.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                Log.i("RecipeDetailsFragment", "subscribeUi: $recipe")
                setDetails(recipe)
            }
        })

        viewModel.favorite.observe(viewLifecycleOwner, Observer {
            isFavorite = it
            if (isFavorite) {
                recipe_detail_favorite_icon.setImageResource(R.drawable.ic_favorite)
            } else {
                recipe_detail_favorite_icon.setImageResource(R.drawable.ic_not_favorite)
            }
        })
    }

    private fun setDetails(recipe: Recipe) {
        this.mRecipe = recipe
        Glide.with(this)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .load(recipe.imageUrl)
            .into(recipe_detail_image)

//        recipe_detail_app_bar_title.text = recipe.title
        recipe_detail_title.text = recipe.title
        recipe_detail_publisher.text = recipe.publisher
        recipe_detail_social_score.text = recipe.socialRank.roundToInt().toString()



        ingredientsContainer = ingredients_container
        ingredientsContainer.removeAllViews()

        recipe.ingredients?.forEach {
            val view = TextView(context!!)
            view.text = getString(R.string.ingredient, it)
            view.textSize = 16f
            ingredientsContainer.addView(view)
            animateChange(view)
        }

        if (recipe.ingredients?.isNullOrEmpty()!!) {
            ingredientsContainer.apply {
                val view = TextView(context)
                view.text = resources.getString(R.string.missing_ingredients_list)
                view.textSize = 16f
                this.addView(view)
            }
        }
    }

    private fun animateChange(view: View) {
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 400L
            repeatMode = ObjectAnimator.RESTART
        }.start()
    }
}
