package com.example.foodrecipesdemokotlin.ui.category


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.domain.Category
import com.example.foodrecipesdemokotlin.ui.BaseFragment
import com.example.foodrecipesdemokotlin.ui.adapters.CategoryAdapter
import com.example.foodrecipesdemokotlin.ui.adapters.OnCategoryClick
import com.example.foodrecipesdemokotlin.util.Konstant
import kotlinx.android.synthetic.main.fragment_category_list.*

/**
 * A simple [Fragment] subclass.
 */
class CategoryFragment : BaseFragment() {

    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUi()
        initRecyclerView()
        displayHomeUp(false)
        activity?.run {
            (this as AppCompatActivity).title = getString(R.string.app_name)
        }
    }

    private fun initRecyclerView() {
        adapter = CategoryAdapter(OnCategoryClick { setCategory(it) })
        category_list.adapter = adapter
        val categoryList: List<Category> = List(Konstant.DEFAULT_CATEGORIES_NAMES.size) { index ->
            Category(
                getString(Konstant.DEFAULT_CATEGORIES_NAMES[index]),
                Konstant.DEFAULT_CATEGORY_IMAGES[index]
            )
        }
        adapter.data = categoryList
    }

    private fun subscribeUi() {
        viewModel.category.observe(viewLifecycleOwner, Observer {
            Log.i("CategoryListFragment", "subscribeUi: $it")
            it?.let {
                navigateToRecipeList(it)
            }
        })
    }

    private fun navigateToRecipeList(query: String) {
        this.findNavController().navigate(R.id.action_categoryListFragment_to_recipeListFragment)
        viewModel.searchRecipes(query)
        viewModel.completedNavigationToRecipeList()
    }

    private fun setCategory(string: String) {
        viewModel.setCategory(string)
    }
}
