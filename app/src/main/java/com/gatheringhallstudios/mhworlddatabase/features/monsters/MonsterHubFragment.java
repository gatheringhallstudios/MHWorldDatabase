package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.BaseHubFragment;

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

    public MonsterHubFragment() {
        addTab(tabTitleLarge, () ->
                MonsterListFragment.newInstance(MonsterListViewModel.Tab.LARGE)
        );

        addTab(tabTitleSmall, () ->
                MonsterListFragment.newInstance(MonsterListViewModel.Tab.SMALL)
        );
    }

    @Override
    public String getHubTitle() {
        return getString(R.string.monsters_title);
    }
}
