package com.example.foodrecipesdemokotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.ui.BaseViewModel
import com.example.foodrecipesdemokotlin.ui.Status
import kotlinx.android.synthetic.main.loading_layout.*

abstract class BaseActivity : AppCompatActivity() {

    private val viewModel: BaseViewModel by lazy {
        ViewModelProviders.of(this).get(
            BaseViewModel::class.java
        )
    }

    fun showProgress(show: Boolean) {
        if (show) load_layout.visibility = VISIBLE else INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        viewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING -> showProgress(true)
                Status.ERROR -> Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                Status.DONE -> showProgress(false)
                Status.NONE -> showProgress(false)
                else -> {
                }
            }
        })
    }
}
