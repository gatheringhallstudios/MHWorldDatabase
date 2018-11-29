package com.gatheringhallstudios.mhworlddatabase

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*

/**
 * The main activity used in MHWorld Database.
 * MHWorldDatabase is a single activity app, so all navigation replaces the active fragment.
 * Searching is implemented by globally maintaining the state of the searchview.
 * TODO: Consider making the search option a simple button, that navigates to an actionbarless fragment that handles all searching.
 */
class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private var searchView : SearchView? = null

    /*
     * List of Start Destination IDs. These destinations
     * will show the menu button instead of back button in the
     * toolbar.
     */
    private val multiStartNavigationUi = MultiStartNavigationUI(listOf(
            R.id.monsterListDestination,
            R.id.itemListDestination,
            R.id.itemCraftingListDestination,
            R.id.armorListDestination,
            R.id.skillListDestination,
            R.id.decorationListDestination,
            R.id.locationListDestination,
            R.id.charmListDestination,
            R.id.weaponListDestination
    ))

    val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(this.toolbar)
        setupNavigation()

        viewModel.searchActive.observe(this, Observer {
            val active = it ?: false
            searchView?.let { searchView ->
                val shouldIconify = !active

                // if nothing changed, exit the "let"
                if (searchView.isIconified == shouldIconify) {
                    return@let
                }

                // Update the search while listeners are disabled
                // not doing this would create a cycle and create weird bugs
                resetSearchListeners {
                    // If we don't do this, iconify will fail
                    if (!active) {
                        searchView.setQuery("", false)
                    }

                    searchView.isIconified = shouldIconify

                    if (active) {
                        searchView.setQuery(viewModel.filter.value, false)
                        searchView.clearFocus()
                    }
                }
            }
        })
    }

    /**
     * Requests the search view to contain a new value directly.
     * This will indirectly update the activity viewmodel's filter property.
     * This does not activiate any query events.
     */
    fun updateSearchView(filterValue: String) {
        searchView?.let { searchView ->
            // update the searchbox query if open and the value differs
            searchView.setQuery(filterValue, false)
        }
    }

    private fun setupNavigation() {
        // Replace NavigationUI.setupActionBarWithNavController with the
        // multiple start destination one
        multiStartNavigationUi.setupActionBarWithNavController(this,
                Navigation.findNavController(this, R.id.content_main_frame),
                this.drawer_layout)

        // Hook up navigation drawer items to NavController
        NavigationUI.setupWithNavController(
                this.nav_view,
                Navigation.findNavController(this, R.id.content_main_frame))

        // Remove icon tint from navigation drawer
        this.nav_view.itemIconTintList = null
    }

    override fun onBackPressed() {
        val drawer = this.drawer_layout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = Navigation.findNavController(this, R.id.content_main_frame)
        return multiStartNavigationUi.navigateUp(this.drawer_layout, controller)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        // checks if the searchView was closed last time. Necessary if the options menu is being recreated.
        val wasIconified = searchView?.isIconified ?: true

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView?.setQuery(viewModel.filter.value, false)
        if (!wasIconified) {
            searchView?.isIconified = false
        }

        setSearchListeners()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun resetSearchListeners(callback : () -> Unit) {
        searchView?.setOnSearchClickListener(null)
        searchView?.setOnQueryTextListener(null)
        searchView?.setOnCloseListener(null)

        callback()

        setSearchListeners()
    }

    private fun setSearchListeners() {
        searchView?.setOnSearchClickListener {
            viewModel.startNewSearch()

            Navigation.findNavController(this, R.id.content_main_frame)
                    .navigate(R.id.openSearchAction)
        }

        // disables closing
        searchView?.setOnCloseListener {
            true
        }

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchUpdate(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchUpdate(newText ?: "")
                return true
            }
        })
    }

    /**
     * Restarts the app. Launches a new app and close the current one
     */
    fun restartApp() {
        val restartIntent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        finish()
        startActivity(restartIntent)
    }
}
