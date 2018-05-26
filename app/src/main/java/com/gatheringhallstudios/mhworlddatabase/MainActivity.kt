package com.gatheringhallstudios.mhworlddatabase

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(this.toolbar)

        setupNavigation()
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
