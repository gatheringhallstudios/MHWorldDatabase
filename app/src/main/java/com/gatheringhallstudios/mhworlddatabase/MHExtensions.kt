package com.gatheringhallstudios.mhworlddatabase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.v4.app.Fragment
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
