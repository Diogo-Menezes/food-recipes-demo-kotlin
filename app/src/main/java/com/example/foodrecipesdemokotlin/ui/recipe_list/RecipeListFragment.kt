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
import kotlinx.android.synthetic.main.fragment_recipe_list.*

/**
 * A simple [Fragment] subclass.
 */
class RecipeListFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun subscribeUi() {
        viewModel.recipeList.observe(viewLifecycleOwner, Observer { recipeList ->


            text_view.text = recipeList.toString()
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            Log.i("RecipeListFragment", "subscribeUi: $it")
        })
    }


}
