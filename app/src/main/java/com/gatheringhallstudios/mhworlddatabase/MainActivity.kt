package com.gatheringhallstudios.mhworlddatabase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.michaelflisar.changelog.ChangelogBuilder
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

    private var searchView: SearchView? = null
    private lateinit var appBarConfiguration : AppBarConfiguration

    val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(this.toolbar)
        setupNavigation()
        showChangelog()

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

    private fun showChangelog() {
        ChangelogBuilder()
                // library will take care to show activity/dialog only if the changelog has new
                // info and will only show this new info
                .withManagedShowOnStart(true)
                // provide a custom title if desired, default one is "Changelog <VERSION>"
                .withTitle(getString(R.string.label_whats_new))
                // provide a custom ok button text if desired, default one is "OK"
                .withOkButtonLabel(getString(R.string.action_ok))
                .withRateButton(true)
                .withRateButtonLabel(getString(R.string.label_rate_app))
                // second parameter defines, if the dialog has a dark or light theme
                .buildAndShowDialog(this, true)
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
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.monsterListDestination,
                R.id.itemListDestination,
                R.id.itemCraftingListDestination,
                R.id.armorListDestination,
                R.id.skillListDestination,
                R.id.decorationListDestination,
                R.id.locationListDestination,
                R.id.charmListDestination,
                R.id.weaponListDestination,
                R.id.bookmarksListDestination,
                R.id.kinsectTreeDestination,
                R.id.workshopDestination),
                drawer_layout
        )

        val navController = findNavController(R.id.content_main_frame)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Hook up navigation drawer items to NavController
        nav_view.setupWithNavController(navController)

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
        return findNavController(R.id.content_main_frame).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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

        return when (id) {
            R.id.action_search -> true
            R.id.action_toggle_bookmark -> false
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun resetSearchListeners(callback: () -> Unit) {
        searchView?.setOnSearchClickListener(null)
        searchView?.setOnQueryTextListener(null)
        searchView?.setOnCloseListener(null)

        callback()

        setSearchListeners()
    }

    private fun setSearchListeners() {
        // Navigate to search fragment once search starts
        searchView?.setOnSearchClickListener {
            viewModel.startNewSearch()

            findNavController(R.id.content_main_frame)
                    .navigate(R.id.openSearchAction)
        }

        // disables closing
        searchView?.setOnCloseListener {
            true
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
