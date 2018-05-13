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
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterRewardEntity;
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;

/**
 * Fragment for a list of monsters
 */

public class MonsterRewardFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private static final String ARG_MONSTER_ID = "MONSTER_ID";
    private static final String ARG_RANK = "RANK";

    MonsterDetailViewModel viewModel;
    RecyclerView recyclerView;

    BasicListDelegationAdapter<Object> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, parent);

        // Setup Adapter to display rewards and headers
        RewardAdapterDelegate rewardDelegate = new RewardAdapterDelegate(this::handleRewardSelection);
        SubHeaderAdapterDelegate subHeaderDelegate = new SubHeaderAdapterDelegate(this::handleSubHeaderSelection);
        adapter = new BasicListDelegationAdapter<>(rewardDelegate, subHeaderDelegate);

        // Setup RecyclerView
        recyclerView = (RecyclerView) inflater.inflate(R.layout.list_generic, parent, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getParentFragment()).get(MonsterDetailViewModel.class);
        viewModel.getRewards().observe(this, this::setItems);

        return recyclerView;
    }

    public static MonsterRewardFragment newInstance(Rank rank) {
        MonsterRewardFragment fragment = new MonsterRewardFragment();
        fragment.setArguments(new BundleBuilder()
                .putSerializable(ARG_RANK, rank)
                .build());
        return fragment;
    }

    /**
     * Set the rewards to be displayed in the fragment
     * @param rewards items be of type Reward.
     */
    public void setItems(List<MonsterRewardView> rewards) {

        List<Object> rewardsWithHeaders = populateHeaders(rewards, (Rank) getArguments().getSerializable(ARG_RANK));

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
    public List<Object> populateHeaders(List<MonsterRewardView> items, Rank rank) {
        ArrayList<Object> itemsWithHeaders = new ArrayList<>();

        // select only the items that are of hte relevant rank
        List<MonsterRewardView> grouped = StreamSupport.stream(items)
                .filter(i -> i.rank.equals(rank))
                .collect(Collectors.toList());

        // todo: more automated way to translate rank to text
        // group again by condition
        Map<String, List<MonsterRewardView>> conditionGroups =
                StreamSupport.stream(grouped)
                    .collect(Collectors.groupingBy((i) -> i.condition,
                            LinkedHashMap::new, // maintain order
                            Collectors.toList()));

        for (Map.Entry<String, List<MonsterRewardView>> entry : conditionGroups.entrySet()) {
            String condition = entry.getKey();
            itemsWithHeaders.add(new SubHeader(condition));

            // Now add the reward items
            itemsWithHeaders.addAll(entry.getValue());
        }

        return itemsWithHeaders;
    }

    /**
     * Handle onClick of rewards.
     * @param object Object is guaranteed to be a Reward that was clicked
     */
    private void handleRewardSelection(Object object) {
        MonsterRewardView reward = (MonsterRewardView) object;
        Toast.makeText(getContext(), "Clicked " + reward.item_name, Toast.LENGTH_SHORT).show();
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
