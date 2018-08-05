package com.gatheringhallstudios.mhworlddatabase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

val TAG = "MHWorldApplicationUtil"

/**
 * Returns the router object that can be used to navigate between pages
 */
fun Fragment.getRouter() : Router {
    return Router(this.findNavController())
}

/**
 * Returns the router object that can be used to navigate between pages
 */
fun View.getRouter() : Router {
    return Router(this.findNavController())
}

/**
 * Sets the action bar's title text of the fragment's parent activity.
 * This function is shorthand for retrieving the parent's internal action bar and then setting that title.
 */
fun Fragment.setActivityTitle(title: String?) {
    // note: activity.setTitle() doesn't work with the navigation library we're using
    // this is a workaround
    (activity as AppCompatActivity).supportActionBar?.title = title
}