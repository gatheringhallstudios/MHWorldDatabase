package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.MonsterListAdapter;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Fragment for a list of monsters
 */

public class MonsterListFragment extends Fragment {
    private static final String ARG_TAB = "MONSTER_TAB";

    MonsterHubViewModel viewModel;

    @BindString(R.string.monsters_small) String tabTitleSmall;
    @BindString(R.string.monsters_large) String tabTitleLarge;
    @BindString(R.string.monsters_all) String tabTitleAll;

    RecyclerView recyclerView;

    public static MonsterListFragment newInstance(String tab) {
        Bundle args = new Bundle();
        args.putString(ARG_TAB, tab);
        MonsterListFragment f = new MonsterListFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, parent);

        if (getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(MonsterHubViewModel.class);
        }

        // Setup RecyclerView
        recyclerView = (RecyclerView) inflater.inflate(R.layout.list_generic, parent, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));

        if (getArguments() != null) {
            String tabName = getArguments().getString(ARG_TAB);
            if (tabTitleLarge.equals(tabName)) {
                loadLargeMonsterList();
            } else if (tabTitleSmall.equals(tabName)) {
                loadSmallMonstersList();
            } else {
                loadAllMonsterList();
            }
        }

        return recyclerView;
    }

    private void loadLargeMonsterList() {
        viewModel.getLargeMonsters().observe(this,
                list -> {
                    recyclerView.setAdapter(new MonsterListAdapter(list));
                });
    }

    private void loadSmallMonstersList() {
        viewModel.getSmallMonsters().observe(this,
                list -> {
                    recyclerView.setAdapter(new MonsterListAdapter(list));
                });
    }

    private void loadAllMonsterList() {
        viewModel.getMonsters().observe(this,
                list -> {
                    recyclerView.setAdapter(new MonsterListAdapter(list));
                });
    }
}
