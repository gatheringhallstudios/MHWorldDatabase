package com.gatheringhallstudios.mhworlddatabase.common;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

/**
 *  Handles navigation to other pages.
 *  Currently only used to define fragment navigation to specific fragments.
 */

public interface Navigator {

    /**
     * Defines the behavior to use when navigating
     */
    enum Behavior {
        /*
         * Add the current fragment to the backstack and load the new fragment.
         */
        ADD,
        /*
         * Remove the current fragment, clear the backstack, and load the new fragment.
         */
        RESET
    }

    /**
     * Begins a navigation to a specific fragment.
     * The fragment is added to the backstack.
     */
    default void navigateTo(Fragment fragment) {
        navigateTo(fragment, Behavior.ADD);
    }

    /**
     * Begins a navigation to a specific fragment with a specified behavior.
     * Use ADD to add to backstack, or RESET to clear the backstack.
     * @param fragment
     * @param behavior
     */
    void navigateTo(Fragment fragment, Behavior behavior);
}
