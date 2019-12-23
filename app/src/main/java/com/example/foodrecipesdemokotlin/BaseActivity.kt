package com.example.foodrecipesdemokotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.ui.BaseViewModel
import com.example.foodrecipesdemokotlin.ui.Status
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.coroutines.*

private const val FADE_DURATION = 1000L

abstract class BaseActivity : AppCompatActivity() {



    private fun animateToGone(view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            view.animate().alpha(0f).duration = FADE_DURATION
            delay(FADE_DURATION)
            view.visibility = View.GONE
        }
    }

    private fun animateToVisible(view: View) {
        view.run {
            visibility = VISIBLE
            alpha = 0f
            animate().alpha(1f).duration = FADE_DURATION
        }
    }

    protected fun showProgress(show: Boolean) {
        when (show) {
            true -> animateToVisible(load_layout)
            false -> animateToGone(load_layout)
        }
    }

    open override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.i("BaseActivity", "onCreate: called")
//        subscribeUi()
    }

//    fun subscribeUi() {
//        Log.i("BaseActivity", "subscribeUi: called")
//        viewModel.status.observe(this, Observer { status ->
//            Log.i("BaseActivity", "subscribeUi: $status")
//            when (status) {
//                Status.LOADING -> showProgress(true)
//                Status.ERROR -> Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
//                Status.DONE -> showProgress(false)
//                Status.NONE -> showProgress(false)
//                else -> {
//                }
//            }
//        })
//    }
}
