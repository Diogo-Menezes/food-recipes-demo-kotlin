package com.example.foodrecipesdemokotlin.ui

import android.view.View
import com.example.foodrecipesdemokotlin.viewmodels.Status


interface StatusListener {
    fun setStatus(status: Status)

    fun closeKeyboard(view: View)

    fun loadingText(string: String);

}
