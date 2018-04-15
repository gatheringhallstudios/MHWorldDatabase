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
import com.gatheringhallstudios.mhworlddatabase.data.views.Reward;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Fragment for a list of monsters
 */

public class MonsterRewardFragment extends Fragment {

    private static final String ARG_MONSTER_ID = "MONSTER_ID";

    MonsterRewardViewModel viewModel;
    RecyclerView recyclerView;

    BasicListDelegationAdapter<Reward> adapter;

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

        // Setup Adapter
        RewardAdapterDelegate delegate = new RewardAdapterDelegate(this::handleRewardSelection);
        adapter = new BasicListDelegationAdapter<>(delegate);

        // Setup RecyclerView
        recyclerView = (RecyclerView) inflater.inflate(R.layout.list_generic, parent, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MonsterRewardViewModel.class);
        viewModel.setMonsterId(monsterId);
        viewModel.getData().observe(this, this::setItems);

        return recyclerView;
    }

    public void setItems(List<Reward> rewards) {
        if (adapter != null) {
            adapter.setItems(rewards);
            adapter.notifyDataSetChanged();
        }
    }

    private void handleRewardSelection(Reward reward) {
        Toast.makeText(getContext(), "Clicked " + reward.name, Toast.LENGTH_SHORT).show();
        // TODO implement reward clicking to item details
//        Navigator nav = (Navigator)getActivity();
//        nav.navigateTo(MonsterDetailPagerFragment.getInstance(monster));
    }
}
