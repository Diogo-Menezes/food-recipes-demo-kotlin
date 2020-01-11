package com.example.foodrecipesdemokotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.foodrecipesdemokotlin.ui.StatusListener
import com.example.foodrecipesdemokotlin.ui.viewmodels.SharedViewModel
import com.example.foodrecipesdemokotlin.ui.viewmodels.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.load_layout
import kotlinx.android.synthetic.main.loading_layout.*

private const val FADE_DURATION = 500L
private const val ALPHA_VISIBLE = 1f
private const val ALPHA_INVISIBLE = 0f

abstract class BaseActivity : AppCompatActivity(),
    StatusListener {

    protected val viewModel: SharedViewModel by lazy {
        ViewModelProviders.of(this)[SharedViewModel::class.java]
    }

    private lateinit var animator: ObjectAnimator

    fun animateToInvisible(view: View, duration: Long = FADE_DURATION) {
        view.bringToFront()
        view.visibility = VISIBLE
        ObjectAnimator
            .ofFloat(view, View.ALPHA, ALPHA_INVISIBLE)
            .apply {
                this.duration = duration
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationCancel(animation: Animator?) {
                        view.alpha = ALPHA_INVISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        view.alpha = ALPHA_INVISIBLE
                    }
                })
                start()
            }
    }

    fun animateToVisible(view: View, duration: Long = FADE_DURATION) {
        view.bringToFront()
        view.visibility = VISIBLE
        ObjectAnimator
            .ofFloat(view, View.ALPHA, ALPHA_VISIBLE).apply {
                this.duration = FADE_DURATION
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        view.alpha = ALPHA_VISIBLE
                    }
                })
                start()
            }
    }

    protected fun onStatusChange(status: Status) {
        when (status) {
            Status.NONE -> return
            Status.LOADING -> showProgress(true)
            Status.LOADING_WITH_DATA -> animateToVisible(frame_layout)
            Status.ERROR -> showMessage() // TODO("24/12/2019 - add message") //turns the progress off automatically
            Status.DONE -> showProgress(false)
            Status.NO_RESULTS -> showMessage(
                getString(
                    R.string.no_results,
                    viewModel.query.value
                )
            ) //turns the progress off automatically
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
                animateToVisible(frame_layout)
            }
        }
    }

    protected fun showNavHostLayout(show: Boolean) =
        if (show) animateToVisible(frame_layout) else animateToInvisible(frame_layout)

    override fun closeKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun loadingText(string: String) {
        loading_text.text = string
    }


}
