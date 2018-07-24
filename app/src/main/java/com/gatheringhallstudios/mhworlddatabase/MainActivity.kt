package com.gatheringhallstudios.mhworlddatabase

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
            R.id.itemCraftingDestination,
            R.id.armorListDestination,
            R.id.skillListDestination,
            R.id.decorationListDestination,
            R.id.locationListDestination,
            R.id.charmListDestination
    ))

    val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(this.toolbar)
        setupNavigation()

        // Initialize application settings
        // todo: if there are issues, create an Application subclass and bind there
        AppSettings.bindApplication(this.application)

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
     * This will indirectly update the viewmodel's filter property
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

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView?.setQuery(viewModel.filter.value, false)

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
}
