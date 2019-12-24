package com.example.foodrecipesdemokotlin.ui.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.adapters.RecipeAdapter
import com.example.foodrecipesdemokotlin.util.Konstant
import kotlinx.android.synthetic.main.fragment_category_list.*

/**
 * A simple [Fragment] subclass.
 */
class CategoryListFragment : Fragment() {

    private val adapter = RecipeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        category_list.adapter = adapter
        adapter.data = Konstant.DEFAULT_SEARCH_CATEGORIES.asList()
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = CategoryListFragment()
    }
}
