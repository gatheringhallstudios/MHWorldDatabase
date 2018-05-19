package com.gatheringhallstudios.mhworlddatabase;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gatheringhallstudios.mhworlddatabase.common.Navigator;
import com.gatheringhallstudios.mhworlddatabase.features.armor.ArmorListFragment;
import com.gatheringhallstudios.mhworlddatabase.features.items.ItemListFragment;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterListPagerFragment;
import com.gatheringhallstudios.mhworlddatabase.features.skills.SkillListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Navigator {

    private final String TAG = getClass().getSimpleName();

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
            case R.id.nav_items:
                return new ItemListFragment();
            case R.id.nav_locations:
                return null;
            case R.id.nav_skills:
                return new SkillListFragment();
            case R.id.nav_armor:
                return new ArmorListFragment();
            default:
                // throw an exception in the future, but for now return null
                return null;
        }
    }

    /**
     * Callback for when the user selects where to navigate to
     * @param item
     * @return
     */
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

    /*
     *
     * NAVIGATOR IMPLEMENTATION
     * Moving everything down here in case we decide to move
     * this logic to a separate class
     *
     */

    // should this move to a new class, this will need to be initialized at runtime
    @IdRes int contentFrame = R.id.content_main_frame;

    /**
     * Navigate to a transaction using a FragmentTransaction.
     * @param f Fragment to load
     * @param behavior a {@link com.gatheringhallstudios.mhworlddatabase.common.Navigator.Behavior}
     *                 specifying the type of transaction to make. See
     *                 {@link com.gatheringhallstudios.mhworlddatabase.common.Navigator.Behavior}
     *                 for more details.
     */
    public void navigateTo(Fragment f, Navigator.Behavior behavior) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (behavior) {
            case ADD:
                // Add previous fragment to backstack
                ft.addToBackStack(null);
                break;
            case RESET:
                // Clear the backstack
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }

        // Display the new fragment and commit
        ft.replace(contentFrame, f);
        ft.commit();
    }
}
