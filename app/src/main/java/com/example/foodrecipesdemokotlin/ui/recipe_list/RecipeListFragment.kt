package com.example.foodrecipesdemokotlin.ui.recipe_list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.BaseFragment
import com.example.foodrecipesdemokotlin.ui.adapters.OnRecipeClick
import com.example.foodrecipesdemokotlin.ui.adapters.RecipeListAdapter
import kotlinx.android.synthetic.main.fragment_recipe_list.*

/**
 * A simple [Fragment] subclass.
 */
class RecipeListFragment : BaseFragment() {

    private lateinit var adapter: RecipeListAdapter

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
        adapter = RecipeListAdapter(OnRecipeClick {
            Log.i("RecipeListFragment", "initRecyclerList: $it")
            setRecipeId(it)
        })
        recipe_list.adapter = adapter
    }

    private fun setRecipeId(recipeId: String) {
        viewModel.setRecipeId(recipeId)
        findNavController().navigate(R.id.action_recipeListFragment_to_recipeDetailsFragment)
    }

    private fun subscribeUi() {
        viewModel.query.observe(viewLifecycleOwner, Observer {
            viewModel.setTitle(it)
        })
        viewModel.recipeList.observe(viewLifecycleOwner, Observer { recipeList ->
            adapter.data = recipeList
            viewModel.loadFromCache("chicken")
        })
    }
}
