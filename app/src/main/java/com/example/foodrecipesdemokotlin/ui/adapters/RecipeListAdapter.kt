package com.example.foodrecipesdemokotlin.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.domain.RecipeList
import kotlinx.android.synthetic.main.layout_recipe_list_item.view.*
import kotlinx.android.synthetic.main.layout_search_exhausted.view.*
import kotlin.math.roundToInt

private const val NO_RESULTS_TYPE = 0
private const val RESULTS_TYPE = 1

class RecipeListAdapter(val recipeClick: OnRecipeClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<RecipeList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RESULTS_TYPE -> RecipeViewHolder.from(parent)
            NO_RESULTS_TYPE -> NoResultsViewHolder.from(parent)
            else -> NoResultsViewHolder.from(parent)
        }

    }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        if (itemViewType == RESULTS_TYPE) {
            val recipe = data[position]
            (holder as RecipeViewHolder).bind(recipeClick, recipe)
        } else {
            (holder as NoResultsViewHolder).bind()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].title.isNotEmpty()) {
            RESULTS_TYPE
        } else {
            NO_RESULTS_TYPE
        }
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.recipe_list_title
        private val image: ImageView = itemView.recipe_list_image
        private val publisher: TextView = itemView.recipe_list_publisher
        private val socialScore: TextView = itemView.recipe_list_social_score

        fun bind(recipeClick: OnRecipeClick, recipe: RecipeList) {
            itemView.setOnClickListener { recipeClick.onClick(recipe) }
            title.text = recipe.title
            publisher.text = recipe.publisher
            socialScore.text = recipe.socialRank.roundToInt().toString()

            Glide.with(image.context)
                .load(recipe.imageUrl)
                .into(image)
        }

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =
                    layoutInflater.inflate(R.layout.layout_recipe_list_item, parent, false)
                return RecipeViewHolder(view)
            }
        }
    }


    class NoResultsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.no_results_text
        fun bind() {
            textView.text = itemView.resources.getString(R.string.no_results)
        }

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =
                    layoutInflater.inflate(R.layout.layout_search_exhausted, parent, false)
                return RecipeViewHolder(view)
            }
        }
    }
}

class OnRecipeClick(val clickListener: (id: String) -> Unit) {
    fun onClick(recipe: RecipeList) = clickListener(recipe.id)
}

