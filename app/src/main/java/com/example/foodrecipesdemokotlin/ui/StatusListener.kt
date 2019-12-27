package com.example.foodrecipesdemokotlin.ui

import android.view.View
import com.example.foodrecipesdemokotlin.ui.viewmodels.Status


interface StatusListener {

//    fun showFragmentLayout(show: Boolean);
//
//    fun setStatus(status: Status)

    fun closeKeyboard(view: View)

    fun loadingText(string: String);

}
