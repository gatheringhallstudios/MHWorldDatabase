package com.gatheringhallstudios.mhworlddatabase;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gatheringhallstudios.mhworlddatabase.common.Navigator;
import com.gatheringhallstudios.mhworlddatabase.features.armor.ArmorListFragment;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterListPagerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Navigator {

    public MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigationDrawer(toolbar);

        // Instantiate our MainViewModel
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Load the initial fragment
        Fragment initial = getFragmentForNavId(R.id.nav_monsters);
        navigateTo(initial, Behavior.RESET);
    }

    private void setupNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void navigateTo(Fragment f, Navigator.Behavior behavior) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main_frame, f);

        switch (behavior) {
            case ADD:
                ft.addToBackStack(null);
                break;
        }

        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment destination = getFragmentForNavId(id);
        if (destination != null) {
            navigateTo(destination, Behavior.RESET);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    /**
     * Internal method that determines the fragment that should be loaded
     * when the given nav item is selected.
     * @param navId A layout id such as R.id.nav_monsters
     * @return the new fragment
     */
    protected Fragment getFragmentForNavId(int navId) {
        switch (navId) {
            case R.id.nav_monsters:
                return new MonsterListPagerFragment();
            case R.id.nav_armor:
                return new ArmorListFragment();
            default:
                // throw an exception in the future, but for now return null
                return null;
        }
    }
}
