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
import com.gatheringhallstudios.mhworlddatabase.data.Monster;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Monster list
 */

public class MonsterHubFragment extends Fragment {
    MonsterHubViewModel viewModel;

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.pager_list_monsters) ViewPager viewPager;

    @BindString(R.string.monsters_small) String tabTitleSmall;
    @BindString(R.string.monsters_large) String tabTitleLarge;
    @BindString(R.string.monsters_all) String tabTitleAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get ViewModel
        if (getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(MonsterHubViewModel.class);
        }

        // Inflate view
        View root = inflater.inflate(R.layout.fragment_monsters, container, false);

        // Bind butterknife
        ButterKnife.bind(this, root);

        // Initialize ViewPager
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

        // Tab titles
        private String[] tabs = {
                tabTitleLarge,
                tabTitleSmall,
                tabTitleAll
        };

        MonsterGridPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            try {
                String tabName = tabs[index];
                return MonsterListFragment.newInstance(tabName);
            } catch (ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int index) {
            return tabs[index];
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return tabs.length;
        }

    }
}
