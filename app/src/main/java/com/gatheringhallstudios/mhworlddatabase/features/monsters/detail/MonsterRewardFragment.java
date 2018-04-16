package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.BasicListDelegationAdapter;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.RewardAdapterDelegate;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.SectionHeaderAdapterDelegate;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.SubHeaderAdapterDelegate;
import com.gatheringhallstudios.mhworlddatabase.common.models.SectionHeader;
import com.gatheringhallstudios.mhworlddatabase.common.models.SubHeader;
import com.gatheringhallstudios.mhworlddatabase.data.views.Reward;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Fragment for a list of monsters
 */

public class MonsterRewardFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private static final String ARG_MONSTER_ID = "MONSTER_ID";

    MonsterRewardViewModel viewModel;
    RecyclerView recyclerView;

    BasicListDelegationAdapter<Object> adapter;

    public static MonsterRewardFragment newInstance(int monsterId) {
        MonsterRewardFragment f = new MonsterRewardFragment();
        f.setArguments(new BundleBuilder().putSerializable(ARG_MONSTER_ID, monsterId).build());
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, parent);

        // Retrieve MonsterID from args
        Bundle args = getArguments();
        int monsterId = 0;
        if (args != null) {
            monsterId = args.getInt(ARG_MONSTER_ID, 0);
        }

        // Setup Adapter to display rewards and headers
        RewardAdapterDelegate rewardDelegate = new RewardAdapterDelegate(this::handleRewardSelection);
        SectionHeaderAdapterDelegate sectionHeaderDelegate = new SectionHeaderAdapterDelegate(this::handleSectionHeaderSelection);
        SubHeaderAdapterDelegate subHeaderDelegate = new SubHeaderAdapterDelegate(this::handleSubHeaderSelection);
        adapter = new BasicListDelegationAdapter<>(rewardDelegate, sectionHeaderDelegate, subHeaderDelegate);

        // Setup RecyclerView
        recyclerView = (RecyclerView) inflater.inflate(R.layout.list_generic, parent, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MonsterRewardViewModel.class);
        viewModel.setMonsterId(monsterId);
        viewModel.getData().observe(this, this::setItems);

        return recyclerView;
    }

    /**
     * Set the rewards to be displayed in the fragment
     * @param rewards items be of type Reward.
     */
    public void setItems(List<Reward> rewards) {
        List<Object> rewardsWithHeaders = populateHeaders(rewards);

        if (adapter != null) {
            adapter.setItems(rewardsWithHeaders);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Takes a list of items and populates them with Headers based on section changes.
     * This only works properly if items are sorted by the appropriate header.
     * @param items List of Rewards without any Headers inserted.
     * @return List of Objects (Rewards and Headers) in appropriate positions.
     */
    public List<Object> populateHeaders(List<Reward> items) {
        ArrayList<Object> itemsWithHeaders = new ArrayList<>();

        // Add initial section header
        String headText = items.get(0).rank;
        // TODO Replace with enum translation once ready
        itemsWithHeaders.add(new SectionHeader("Low Rank"));

        // Add initial condition header
        String subHeadText = items.get(0).condition;
        itemsWithHeaders.add(new SubHeader(subHeadText));

        for (int i = 1; i < items.size(); i++) {
            Reward reward = items.get(i);

            // Insert a new rank header if we've reached a new section
            if (!reward.rank.equals(headText)) {
                headText = reward.rank;
                // TODO Replace with enum translation once ready
                if ("lr".equals(headText)) {
                    itemsWithHeaders.add(new SectionHeader("Low Rank"));
                } else if ("hr".equals(headText)) {
                    itemsWithHeaders.add(new SectionHeader("High Rank"));
                }
            }

            // Insert a new condition header if we've reached a new section
            if (!reward.condition.equals(subHeadText)) {
                subHeadText = reward.condition;
                itemsWithHeaders.add(new SubHeader(subHeadText));
            }

            itemsWithHeaders.add(reward);
        }

        return itemsWithHeaders;
    }

    /**
     * Handle onClick of rewards.
     * @param object Object is guaranteed to be a Reward that was clicked
     */
    private void handleRewardSelection(Object object) {
        Reward reward = (Reward) object;
        Toast.makeText(getContext(), "Clicked " + reward.name, Toast.LENGTH_SHORT).show();
        // TODO implement reward clicking to item details
//        Navigator nav = (Navigator)getActivity();
//        nav.navigateTo(MonsterDetailPagerFragment.getInstance(monster));
    }

    /**
     * Handle onClick of SectionHeaders.
     * @param object Object is guaranteed to be a SectionHeader that was clicked
     */
    private void handleSectionHeaderSelection(Object object) {
        // Do nothing for now. Stub for future support of expand/collapse upon header clicks
//        SectionHeader header = (SectionHeader) object;
//        Toast.makeText(getContext(), "Clicked " + header.text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle onClick of SubHeaders.
     * @param object Object is guaranteed to be a SubHeader that was clicked
     */
    private void handleSubHeaderSelection(Object object) {
        // Do nothing for now. Stub for future support of expand/collapse upon header clicks
//        SubHeader header = (SectionHeader) object;
//        Toast.makeText(getContext(), "Clicked " + header.text, Toast.LENGTH_SHORT).show();
    }
}
