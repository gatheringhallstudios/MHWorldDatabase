package com.gatheringhallstudios.mhworlddatabase.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * Abstract Base Fragment for implementing Hubs with multiple detail tabs.
 * Hub Fragments can subclass this and call addTab() to set up hub pages.
 */

public abstract class BasePagerFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View root = inflater.inflate(R.layout.fragment_generic_pager, container, false);

        // Bind Views
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        ViewPager viewPager = root.findViewById(R.id.pager_list);

        // Setup tabs
        ArrayList<PagerTab> tabs = new ArrayList<>();
        onAddTabs((title, builder) -> tabs.add(new PagerTab(title, builder)));

        // Initialize ViewPager (tab behavior)
        viewPager.setAdapter(new GenericPagerAdapter(this, tabs));
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    /**
     * Called when the fragment wants the tabs
     * @param tabs
     */
    public abstract void onAddTabs(TabAdder tabs);

    public interface TabAdder {
        /**
         * Adds a tab to the fragment
         *
         * @param title   The title to display for the tab
         * @param builder A TabFactory or lambda that builds the tab fragment
         */
        void addTab(String title, PagerTab.Factory builder);
    }
}
