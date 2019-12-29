package com.example.foodrecipesdemokotlin.repository

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */

enum class ResourceStatus {
    SUCCESS, ERROR, LOADING
}

data class Resource<out T>(val status: ResourceStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(ResourceStatus.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ResourceStatus.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(ResourceStatus.LOADING, data, null)
        }
    }
}