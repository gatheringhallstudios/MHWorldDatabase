package com.gatheringhallstudios.mhworlddatabase.features.common;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Abstract Base Fragment for implementing Hubs with multiple detail tabs.
 * Hub Fragments can subclass this and implement the abstract methods to
 * set up hub pages.
 */

public abstract class BaseHubFragment extends Fragment {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.pager_list) ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View root = inflater.inflate(R.layout.fragment_hub_generic, container, false);

        // Bind ButterKnife
        ButterKnife.bind(this, root);

        // Initialize ViewPager (tab behavior)
        viewPager.setAdapter(new GenericPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    public abstract CharSequence getTabTitles(int index);
    public abstract String getHubTitle();
    public abstract int getTabCount();
    public abstract Fragment getTab(int index);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getHubTitle());
    }

    public class GenericPagerAdapter extends FragmentPagerAdapter {

        GenericPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            return getTab(index);
        }

        @Override
        public CharSequence getPageTitle(int index) {
            return getTabTitles(index);
        }

        @Override
        public int getCount() {
            return getTabCount();
        }
    }
}
