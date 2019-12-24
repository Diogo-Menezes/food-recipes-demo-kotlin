package com.example.foodrecipesdemokotlin.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.domain.RecipeList
import kotlinx.android.synthetic.main.fragment_recipe_detail.view.*
import kotlin.math.roundToInt

class RecipeListAdapter : RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    var data = listOf<RecipeList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = data[position]
        holder.bind(recipe)
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.recipe_list_title
        private val image: ImageView = itemView.recipe_list_image
        private val publisher: TextView = itemView.recipe_list_publisher
        private val socialScore: TextView = itemView.recipe_list_social_score

        fun bind(recipe: RecipeList) {
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
}