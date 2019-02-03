package com.gatheringhallstudios.mhworlddatabase

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination

class MultiStartNavigationUI(private val startDestinations: List<Int>) {
    fun setupActionBarWithNavController(activity: AppCompatActivity, navController: NavController,
                                        drawerLayout: androidx.drawerlayout.widget.DrawerLayout?) {

        navController.addOnDestinationChangedListener(ActionBarOnNavigatedListener(
                activity, startDestinations, drawerLayout))
    }

    fun navigateUp(drawerLayout: androidx.drawerlayout.widget.DrawerLayout?, navController: NavController): Boolean {
        if (drawerLayout != null && startDestinations.contains(navController.currentDestination?.id)) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        } else {
            return navController.navigateUp()
        }
    }

    fun onBackPressed(activity: AppCompatActivity,
                      navController: NavController): Boolean {
        if (startDestinations.contains(navController.currentDestination?.id)) {
            ActivityCompat.finishAfterTransition(activity)
            return true
        }

        return false
    }

    private class ActionBarOnNavigatedListener(
            private val mActivity: AppCompatActivity,
            private val startDestinations: List<Int>,
            private val mDrawerLayout: androidx.drawerlayout.widget.DrawerLayout?
    ) : NavController.OnDestinationChangedListener {
        private var mArrowDrawable: DrawerArrowDrawable? = null
        private var mAnimator: ValueAnimator? = null

        override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
            val actionBar = mActivity.supportActionBar

            val title = destination.label
            if (!title.isNullOrEmpty()) {
                actionBar?.title = title
            }

            val isStartDestination = startDestinations.contains(destination.id)
            actionBar?.setDisplayHomeAsUpEnabled(this.mDrawerLayout != null || !isStartDestination)
            setActionBarUpIndicator(mDrawerLayout != null && isStartDestination)
        }

        private fun setActionBarUpIndicator(showAsDrawerIndicator: Boolean) {
            val delegate = mActivity.drawerToggleDelegate
            var animate = true
            if (mArrowDrawable == null) {
                mArrowDrawable = DrawerArrowDrawable(delegate!!.actionBarThemedContext)
                delegate.setActionBarUpIndicator(mArrowDrawable, 0)
                animate = false
            }

            mArrowDrawable?.let {
                val endValue = if (showAsDrawerIndicator) 0.0f else 1.0f

                if (animate) {
                    val startValue = it.progress
                    mAnimator?.cancel()

                    @SuppressLint("ObjectAnimatorBinding")
                    mAnimator = ObjectAnimator.ofFloat(it, "progress", startValue, endValue)
                    mAnimator?.start()
                } else {
                    it.progress = endValue
                }
            }

        }
    }
}