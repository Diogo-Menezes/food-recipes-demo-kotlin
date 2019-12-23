package com.example.foodrecipesdemokotlin.ui.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodrecipesdemokotlin.R

/**
 * A simple [Fragment] subclass.
 */
class CategoryListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    companion object {
        fun newInstance() = CategoryListFragment()
    }
}
