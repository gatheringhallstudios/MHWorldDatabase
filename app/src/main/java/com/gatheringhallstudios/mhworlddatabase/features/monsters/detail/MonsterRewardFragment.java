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
import com.gatheringhallstudios.mhworlddatabase.common.adapters.HeaderAdapterDelegate;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.RewardAdapterDelegate;
import com.gatheringhallstudios.mhworlddatabase.common.models.Header;
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
        HeaderAdapterDelegate headerDelegate = new HeaderAdapterDelegate(this::handleHeaderSelection);
        adapter = new BasicListDelegationAdapter<>(rewardDelegate, headerDelegate);

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

        String headerText = items.get(0).condition;
        itemsWithHeaders.add(new Header(headerText));

        for (int i = 1; i < items.size(); i++) {
            Reward reward = items.get(i);

            // Insert a new header if we've reached a new section
            if (!reward.condition.equals(headerText)) {
                headerText = reward.condition;
                itemsWithHeaders.add(new Header(headerText));
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
     * Handle onClick of Headers.
     * @param object Object is guaranteed to be a Header that was clicked
     */
    private void handleHeaderSelection(Object object) {
        // Do nothing for now. Stub for future support of expand/collapse upon header clicks
//        Header header = (Header) object;
//        Toast.makeText(getContext(), "Clicked " + header.text, Toast.LENGTH_SHORT).show();
    }
}
