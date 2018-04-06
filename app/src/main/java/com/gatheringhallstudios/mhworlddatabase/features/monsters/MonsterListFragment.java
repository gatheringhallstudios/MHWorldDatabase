package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.MonsterListAdapter;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import butterknife.ButterKnife;

/**
 * Fragment for a list of monsters
 */

public class MonsterListFragment extends Fragment {
    private static final String ARG_TAB = "MONSTER_TAB";

    MonsterListViewModel viewModel;
    RecyclerView recyclerView;

    public static MonsterListFragment newInstance(MonsterListViewModel.Tab tab) {
        MonsterListFragment f = new MonsterListFragment();
        f.setArguments(new BundleBuilder().putSerializable(ARG_TAB, tab).build());
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, parent);

        // Setup RecyclerView
        recyclerView = (RecyclerView) inflater.inflate(R.layout.list_generic, parent, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));


        MonsterListViewModel.Tab tab = MonsterListViewModel.Tab.ALL;
        Bundle args = getArguments();
        if (args != null) {
            tab = (MonsterListViewModel.Tab) args.getSerializable(ARG_TAB);
        }

        viewModel = ViewModelProviders.of(this).get(MonsterListViewModel.class);
        viewModel.setTab(tab);

        viewModel.getData().observe(this, (list) -> {
            recyclerView.setAdapter(new MonsterListAdapter(list));
        });

        return recyclerView;
    }
}
