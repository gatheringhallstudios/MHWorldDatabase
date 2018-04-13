package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

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

    @Override
    public void onAddTabs(TabAdder tabs) {
        tabs.addTab(tabTitleLarge, () ->
                MonsterListFragment.newInstance(MonsterListViewModel.Tab.LARGE)
        );

        tabs.addTab(tabTitleSmall, () ->
                MonsterListFragment.newInstance(MonsterListViewModel.Tab.SMALL)
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getString(R.string.monsters_title));
    }
}
