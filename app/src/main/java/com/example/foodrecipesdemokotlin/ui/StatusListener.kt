package com.example.foodrecipesdemokotlin.ui

import com.example.foodrecipesdemokotlin.viewmodels.Status


interface StatusListener {
    fun setStatus(status: Status)
}
