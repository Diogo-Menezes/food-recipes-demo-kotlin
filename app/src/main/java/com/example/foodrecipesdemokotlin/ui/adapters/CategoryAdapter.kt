package com.example.foodrecipesdemokotlin.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.util.Konstant
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_category_list_item.view.*

class CategoryAdapter(val onCategoryClick: OnCategoryClick) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var data = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = data[position]
        holder.bind(onCategoryClick, item, position)
    }

    override fun getItemCount() = data.size


    class CategoryViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val imageView: CircleImageView = itemView.category_image
        private val category: TextView = itemView.category_title

        fun bind(categoryClick: OnCategoryClick, item: String, position: Int) {

            category.text = item
            itemView.setOnClickListener { categoryClick.onClick(item) }
            val image = Konstant.DEFAULT_SEARCH_CATEGORY_IMAGES[position]
            val path =
                Uri.parse("android.resource://com.diogomenezes.recipesdemo/drawable/${image}")
            Glide
                .with(itemView)
                .load(path)
                .into(imageView)
        }

        companion object {
            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.layout_category_list_item, parent, false)
                return CategoryViewHolder(view)
            }
        }

    }
}

class OnCategoryClick(val clickListener: (category: String) -> Unit) {
    fun onClick(string: String) = clickListener(string)
}