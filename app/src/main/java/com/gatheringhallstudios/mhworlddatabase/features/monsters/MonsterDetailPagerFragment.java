package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterDetailViewModel;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterDamageFragment;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterRewardFragment;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterSummaryFragment;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Monster detail Hub. Displays information for a single monster.
 * All data is displayed in separate tabs.
 */

public class MonsterDetailPagerFragment extends BasePagerFragment {

    private final String TAG = getClass().getSimpleName();
    private static final String ARG_MONSTER_ID = "MONSTER_ID";

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager_list)
    ViewPager viewPager;

    @BindString(R.string.monsters_detail_tab_summary)
    String tabTitleSummary;
    @BindString(R.string.monsters_detail_tab_damage)
    String tabTitleDamage;
    @BindString(R.string.monsters_detail_tab_rewards)
    String tabTitleRewards;

    public static MonsterDetailPagerFragment newInstance(int monsterId) {
        MonsterDetailPagerFragment fragment = new MonsterDetailPagerFragment();
        fragment.setArguments(new BundleBuilder()
                .putSerializable(ARG_MONSTER_ID, monsterId)
                .build());
        return fragment;
    }

    public static MonsterDetailPagerFragment newInstance(MonsterView monster){
        return newInstance(monster.id);
    }

    @Override
    public void onAddTabs(TabAdder tabs) {
        // Retrieve MonsterID from args (required!)
        Bundle args = getArguments();
        int monsterId = args.getInt(ARG_MONSTER_ID);

        // Retrieve and set up our ViewModel
        MonsterDetailViewModel viewModel = ViewModelProviders.of(this).get(MonsterDetailViewModel.class);
        viewModel.setMonster(monsterId);

        viewModel.getData().observe(this, this::setTitle);

        // Now add our tabs
        tabs.addTab(tabTitleSummary, () -> new MonsterSummaryFragment());
        tabs.addTab(tabTitleDamage, () ->
            MonsterDamageFragment.newInstance(monsterId)
        );
        tabs.addTab(tabTitleRewards, () -> new MonsterRewardFragment());
    }

    private void setTitle(MonsterView monster) {
        getActivity().setTitle(monster.name);
    }
}
