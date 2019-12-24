package com.example.foodrecipesdemokotlin.ui.recipe_list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.BaseFragment
import com.example.foodrecipesdemokotlin.ui.adapters.RecipeListAdapter
import kotlinx.android.synthetic.main.fragment_recipe_list.*

/**
 * A simple [Fragment] subclass.
 */
class RecipeListFragment : BaseFragment() {

    private val adapter = RecipeListAdapter()
    private val viewModel: RecipeListViewModel by lazy {
        ViewModelProviders.of(this).get(RecipeListViewModel::class.java)
    }

    companion object {
        fun newInstance() = RecipeListFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subscribeUi()
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerList()
    }

    private fun initRecyclerList() {
        recipe_list.adapter = adapter
    }

    private fun subscribeUi() {
        viewModel.recipeList.observe(viewLifecycleOwner, Observer { recipeList ->
            adapter.data = recipeList
        })

        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
            Log.i("RecipeListFragment", "subscribeUi: ${status.name}")
            statusListener.setStatus(status)
        })
    }

    override fun searchQuery(query: String) {
        viewModel.searchRecipes(query, "1")
    }
}
