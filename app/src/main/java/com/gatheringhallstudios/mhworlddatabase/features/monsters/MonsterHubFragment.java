package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.features.common.BaseHubFragment;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Monster Hub
 */

public class MonsterHubFragment extends BaseHubFragment {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager_list)
    ViewPager viewPager;

    @BindString(R.string.monsters_large)
    String tabTitleLarge;
    @BindString(R.string.monsters_small)
    String tabTitleSmall;

    private MonsterListViewModel.Tab[] tabs = {
            MonsterListViewModel.Tab.LARGE,
            MonsterListViewModel.Tab.SMALL
    };

    @Override
    public CharSequence getTabTitles(int index) {
        MonsterListViewModel.Tab tab = tabs[index];
        switch (tab) {
            case LARGE:
                return tabTitleLarge;
            case SMALL:
                return tabTitleSmall;
            default:
                Log.d(TAG, "getPageTitle: Unknown tab!");
                return tabTitleLarge;
        }
    }

    @Override
    public String getHubTitle() {
        return getString(R.string.monsters_title);
    }

    @Override
    public int getTabCount() {
        // get item count - equal to number of tabs
        return tabs.length;
    }

    @Override
    public Fragment getTab(int index) {
        try {
            return MonsterListFragment.newInstance(tabs[index]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
}
