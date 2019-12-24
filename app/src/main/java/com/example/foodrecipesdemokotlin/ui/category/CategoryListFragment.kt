package com.example.foodrecipesdemokotlin.ui.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.foodrecipesdemokotlin.R
import com.example.foodrecipesdemokotlin.ui.BaseFragment
import com.example.foodrecipesdemokotlin.ui.adapters.CategoryAdapter
import com.example.foodrecipesdemokotlin.ui.adapters.OnCategoryClick
import com.example.foodrecipesdemokotlin.util.Konstant
import kotlinx.android.synthetic.main.fragment_category_list.*

/**
 * A simple [Fragment] subclass.
 */
class CategoryListFragment : BaseFragment() {

    private lateinit var adapter: CategoryAdapter

    private val viewModel: CategoryViewModel by lazy {
        ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = CategoryAdapter(OnCategoryClick { onClick(it) })
        category_list.adapter = adapter
        adapter.data = Konstant.DEFAULT_SEARCH_CATEGORIES.asList()

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.category.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    CategoryListFragmentDirections.actionCategoryListFragmentToRecipeListFragment(it)
                )
                statusListener.loadingText(it)
                viewModel.finishedNavigation()
            }

        })
    }

    companion object {
        fun newInstance() = CategoryListFragment()
    }

    private fun onClick(string: String) {
        viewModel.setCategory(string)
    }

}
