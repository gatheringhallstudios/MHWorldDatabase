package com.gatheringhallstudios.mhworlddatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

val TAG = "MHWorldApplicationUtil"

/**
 * Returns the router object that can be used to navigate between pages
 */
fun androidx.fragment.app.Fragment.getRouter() : Router {
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
fun androidx.fragment.app.Fragment.setActivityTitle(title: String?) {
    // note: activity.setTitle() doesn't work with the navigation library we're using
    // this is a workaround
    (activity as AppCompatActivity).supportActionBar?.title = title
}