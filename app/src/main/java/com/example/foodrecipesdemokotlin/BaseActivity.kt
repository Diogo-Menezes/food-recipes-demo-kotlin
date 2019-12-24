package com.example.foodrecipesdemokotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodrecipesdemokotlin.ui.StatusListener
import com.example.foodrecipesdemokotlin.viewmodels.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val FADE_DURATION = 1000L

abstract class BaseActivity : AppCompatActivity(),
    StatusListener {


    private fun animateToInvisible(view: View) {
        view.visibility = View.VISIBLE
        view.bringToFront()
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)
        animator.duration = FADE_DURATION
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                view.visibility = View.INVISIBLE
                if (frame_layout.visibility != View.VISIBLE) animateToVisible(frame_layout)
            }
        })
        animator.start()
    }

    private fun animateToVisible(view: View) {
        view.visibility = View.INVISIBLE
        view.bringToFront()
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
        animator.duration = FADE_DURATION
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.visibility = View.VISIBLE
            }
        })
        animator.start()
    }

    override fun setStatus(status: Status) {
        when (status) {
            Status.NONE -> return
            Status.LOADING -> showProgress(true)
            Status.ERROR -> showMessage() // TODO("24/12/2019 - add message") //turns the progress off automatically
            Status.DONE -> showProgress(false)
            Status.NO_RESULTS -> showMessage(getString(R.string.no_results)) //turns the progress off automatically
        }
    }

    fun Context.makeSnack(message: String) {
        Snackbar.make(main_layout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showMessage(message: String = "Error") {
        applicationContext.makeSnack(message)
        showProgress(false)
        title = getString(R.string.app_name)
    }

    private fun showProgress(show: Boolean) {
        when (show) {
            true -> {
                animateToVisible(load_layout)
                animateToInvisible(frame_layout)
            }
            false -> {
                animateToInvisible(load_layout)
            }
        }
    }
}
