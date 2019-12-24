package com.example.foodrecipesdemokotlin.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.ui.recipe_list.RecipeListViewModel

/**
 * A simple [Fragment] subclass.
 */
open class BaseFragment : Fragment() {
    lateinit var statusListener: StatusListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance() = BaseFragment()
    }

    override fun onAttach(context: Context) {
        Log.i("BaseFragment", "onAttach: called")
        super.onAttach(context)
        try {
            statusListener = context as StatusListener
        } catch (e: Exception) {
            Log.i("BaseFragment", "onAttach: Must implement interface ${e.message} ")
        }
    }

    open fun searchQuery(query: String) {
        Log.i("BaseFragment", "searchQuery: $query")
    }
}
