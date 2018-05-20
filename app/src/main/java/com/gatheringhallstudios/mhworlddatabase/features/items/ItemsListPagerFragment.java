package com.gatheringhallstudios.mhworlddatabase.features.items;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment;
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.list.MonsterListFragment;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.list.MonsterListViewModel;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Monster Hub
 */

public class ItemsListPagerFragment extends BasePagerFragment {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager_list)
    ViewPager viewPager;

    @BindString(R.string.items_tab_item)
    String tabTitleItem;
    @BindString(R.string.items_tab_materials)
    String tabTitleMaterials;
    @BindString(R.string.items_tab_ammo)
    String tabTitleAmmo;
    @BindString(R.string.items_tab_account)
    String tabTitleAccount;

    @Override
    public void onAddTabs(TabAdder tabs) {
        tabs.addTab(tabTitleItem, () ->
                ItemListFragment.newInstance(ItemCategory.ITEM)
        );

        tabs.addTab(tabTitleMaterials, () ->
                ItemListFragment.newInstance(ItemCategory.MATERIAL)
        );
        tabs.addTab(tabTitleAmmo, () ->
                ItemListFragment.newInstance(ItemCategory.AMMO)
        );

        tabs.addTab(tabTitleAccount, () ->
                ItemListFragment.newInstance(ItemCategory.ACCOUNT)
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getString(R.string.items_title));
    }
}
