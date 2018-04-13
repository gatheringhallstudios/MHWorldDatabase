package com.gatheringhallstudios.mhworlddatabase.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Abstract Base Fragment for implementing Hubs with multiple detail tabs.
 * Hub Fragments can subclass this and call addTab() to set up hub pages.
 */

public abstract class BaseHubFragment extends Fragment {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.pager_list) ViewPager viewPager;

    private ArrayList<HubTab> tabs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View root = inflater.inflate(R.layout.fragment_generic_hub, container, false);

        // Bind ButterKnife
        ButterKnife.bind(this, root);

        // Initialize ViewPager (tab behavior)
        viewPager.setAdapter(new GenericPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }


    public abstract String getHubTitle();

    /**
     * Adds a tab to the fragment
     * @param title The title to display for the tab
     * @param builder A TabBuilder or functional interface to build tab fragment
     */
    public void addTab(String title, TabBuilder builder) {
        tabs.add(new HubTab(title, builder));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getHubTitle());
    }

    /**
     * Defines an interface for a class that builds a fragment for a tab.
     */
    public interface TabBuilder {
        Fragment build();
    }

    /**
     * Internal class to encapsulate a tab's metadata
     */
    private class HubTab {
        public String title;
        public TabBuilder builder;
        public HubTab(String name, TabBuilder builder) {
            this.title = name;
            this.builder = builder;
        }
        public Fragment build() {
            return builder.build();
        }
    }

    /**
     * Internal class that handles the actual logic of rendering tabs
     */
    private class GenericPagerAdapter extends FragmentPagerAdapter {

        GenericPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            try {
                return tabs.get(index).build();
            } catch (ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int index) {
            try {
                return tabs.get(index).title;
            } catch (ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }
}
