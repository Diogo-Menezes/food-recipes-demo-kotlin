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

private const val FADE_DURATION = 1000L

abstract class BaseActivity : AppCompatActivity(),
    StatusListener {


    private fun animateToGone(view: View) {
        view.bringToFront()
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)
        animator.duration = FADE_DURATION
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                view.visibility = View.INVISIBLE
            }
        })
        animator.start()
    }

    private fun animateToVisible(view: View) {
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
            Status.ERROR -> showError() // TODO("24/12/2019 - add message")
            Status.DONE -> showProgress(false)
        }
    }

    fun Context.makeSnack(message: String) {
        Snackbar.make(main_layout, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showError(message: String = "Error") {
        applicationContext.makeSnack(message)
    }

    private fun showProgress(show: Boolean) {
        when (show) {
            true -> {
                animateToVisible(load_layout)
                animateToGone(nav_host)
            }
            false -> {
                animateToGone(load_layout)
                animateToVisible(nav_host)
            }
        }
    }
}
