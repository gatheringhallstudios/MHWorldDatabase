package com.gatheringhallstudios.mhworlddatabase.features.items;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment;
import com.gatheringhallstudios.mhworlddatabase.features.items.detail.ItemDetailViewModel;
import com.gatheringhallstudios.mhworlddatabase.features.items.detail.ItemSummaryFragment;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterDetailPagerFragment;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

public class ItemDetailPagerFragment extends BasePagerFragment {
    private static final String ARG_ITEM_ID = "ITEM_ID";

    public static ItemDetailPagerFragment newInstance(int itemId) {
        ItemDetailPagerFragment fragment = new ItemDetailPagerFragment();
        fragment.setArguments(new BundleBuilder()
                .putSerializable(ARG_ITEM_ID, itemId)
                .build());
        return fragment;
    }

    @Override
    public void onAddTabs(TabAdder tabs) {
        // Retrieve MonsterID from args (required!)
        Bundle args = getArguments();
        int itemId = args.getInt(ARG_ITEM_ID);

        ItemDetailViewModel viewModel = ViewModelProviders.of(this).get(ItemDetailViewModel.class);
        viewModel.setItem(itemId);

        tabs.addTab("Summary", ItemSummaryFragment::new);
    }
}
