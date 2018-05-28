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

    val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(this.toolbar)
        setupNavigation()

        // bind the searchbox to the filter
        viewModel.filter.observe(this, Observer { filterValue ->
            searchView?.let { searchView ->
                // update the searchbox query if open and the value differs
                if (!searchView.isIconified && searchView.query != filterValue) {
                    searchView.setQuery(filterValue, false);
                }
            }
        })

        viewModel.searchActive.observe(this, Observer {
            val active = it ?: false
            searchView?.let { searchView ->
                // clear the searchView query.
                // If we don't do this, closing the search view will fail
                if (!active) {
                    searchView.setQuery("", false)
                }

                // update iconify state...if not equal
                val shouldIconify = !active
                if (searchView.isIconified != shouldIconify) {
                    searchView.isIconified = shouldIconify
                }
            }
        })
    }

    private fun setupNavigation() {
        // Hook up action bar and drawer open button to NavController
        NavigationUI.setupActionBarWithNavController(
                this,
                Navigation.findNavController(this, R.id.content_main_frame),
                this.drawer_layout)
        // Hook up navigation drawer items to NavController
        NavigationUI.setupWithNavController(
                this.nav_view,
                Navigation.findNavController(this, R.id.content_main_frame))
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
        return NavigationUI.navigateUp(this.drawer_layout, controller)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView?.setQuery(viewModel.filter.value, false)

        searchView?.setOnSearchClickListener {
            // this check is a hack but I don't see another way
            // We only run the code below if the searchView was opened manually
            // if search is active...it was opened programatically
            val openedManually = viewModel.searchActive.value == false
            if (openedManually) {
                viewModel.startNewSearch()

                Navigation.findNavController(this, R.id.content_main_frame)
                        .navigate(R.id.searchDestination)
            } else {
                searchView?.setQuery(viewModel.filter.value, false)
                searchView?.clearFocus()
            }
        }

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (viewModel.searchActive.value == true) {
                    viewModel.filter.value = query ?: ""
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (viewModel.searchActive.value == true) {
                    viewModel.filter.value = newText ?: ""
                }
                return true
            }
        })

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
}
