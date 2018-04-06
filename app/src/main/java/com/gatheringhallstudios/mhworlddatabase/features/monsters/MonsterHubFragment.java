package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.arch.lifecycle.ViewModelProviders;
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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Monster list
 */

public class MonsterHubFragment extends Fragment {
    MonsterListViewModel viewModel;

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.pager_list_monsters) ViewPager viewPager;

    @BindString(R.string.monsters_large) String tabTitleLarge;
    @BindString(R.string.monsters_small) String tabTitleSmall;
    @BindString(R.string.monsters_all) String tabTitleAll;

    private MonsterListViewModel.Tab[] tabs = {
            MonsterListViewModel.Tab.LARGE,
            MonsterListViewModel.Tab.SMALL,
            MonsterListViewModel.Tab.ALL
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View root = inflater.inflate(R.layout.fragment_monsters, container, false);

        // Bind butterknife
        ButterKnife.bind(this, root);

        // Initialize ViewPager (tab behavior)
        viewPager.setAdapter(new MonsterGridPagerAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getString(R.string.monsters_title));
    }

    public class MonsterGridPagerAdapter extends FragmentPagerAdapter {

        MonsterGridPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            try {
                return MonsterListFragment.newInstance(tabs[index]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int index) {
            MonsterListViewModel.Tab tab = tabs[index];
            switch (tab) {
                case LARGE:
                    return tabTitleLarge;
                case SMALL:
                    return tabTitleSmall;
                default:
                    return tabTitleAll;
            }
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return tabs.length;
        }

    }
}
