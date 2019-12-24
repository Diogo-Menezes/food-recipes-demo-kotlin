package com.example.foodrecipesdemokotlin.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.domain.Recipe
import kotlinx.android.synthetic.main.fragment_recipe_detail.view.*
import kotlin.math.roundToInt

class RecipeListAdapter : RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    val data = listOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.recipe_list_title
        private val image: ImageView = itemView.recipe_list_image
        private val publisher: TextView = itemView.recipe_list_publisher
        private val socialScore: TextView = itemView.recipe_list_social_score

        fun bind(item: Recipe) {

            title.text = item.title
            publisher.text = item.publisher
            socialScore.text = item.socialRank.roundToInt().toString()
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
}