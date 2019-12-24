package com.example.foodrecipesdemokotlin.ui.recipe_list


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.StatusListener
import kotlinx.android.synthetic.main.fragment_recipe_list.*

/**
 * A simple [Fragment] subclass.
 */
class RecipeListFragment : Fragment() {

    private lateinit var statusListener: StatusListener

    companion object {
        fun newInstance() = RecipeListFragment()
    }

    private val viewModel: RecipeListViewModel by lazy {
        ViewModelProviders.of(this).get(RecipeListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subscribeUi()
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)

    }


    private fun subscribeUi() {
        viewModel.recipeList.observe(viewLifecycleOwner, Observer { recipeList ->
            text_view.text = recipeList.toString()
        })

        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
            Log.i("RecipeListFragment", "subscribeUi: ${status.name}")
            statusListener.setStatus(status)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            statusListener = context as StatusListener
        } catch (e: Exception) {
            Log.i("RecipeListFragment", "onAttach: ${e.message}")
        }
    }

}
