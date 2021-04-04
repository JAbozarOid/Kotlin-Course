package com.example.foody.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * when the app running for the first time,
 * the database is empty, so the requestApiData called in RecipesFragment
 * so after requestApiData is called the readRecipes observer for database is called after that
 * for triggering the observer just once use this extension function
 * */
// this extension function will basically observe this live data object only once and not every time
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}