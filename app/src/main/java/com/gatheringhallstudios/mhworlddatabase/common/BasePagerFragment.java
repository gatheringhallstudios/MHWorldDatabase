package com.gatheringhallstudios.mhworlddatabase.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.GenericPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Abstract Base Fragment for implementing Hubs with multiple detail tabs.
 * Hub Fragments can subclass this and call addTab() to set up hub pages.
 * Butterknife is already called, so onCreateView cannot be inherited.
 */

public abstract class BasePagerFragment extends Fragment {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.pager_list) ViewPager viewPager;

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View root = inflater.inflate(R.layout.fragment_generic_pager, container, false);

        // Bind ButterKnife
        ButterKnife.bind(this, root);

        // Setup tabs
        ArrayList<PagerTab> tabs = new ArrayList<>();
        onAddTabs((title, builder) -> tabs.add(new PagerTab(title, builder)));

        // Initialize ViewPager (tab behavior)
        viewPager.setAdapter(new GenericPagerAdapter(this, tabs));
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    /**
     * Called when the fragment wants the tabs, but after Butterknife
     * has binded the view
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
