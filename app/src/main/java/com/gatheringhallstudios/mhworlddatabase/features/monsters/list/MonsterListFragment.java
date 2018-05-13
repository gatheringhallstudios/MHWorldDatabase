package com.gatheringhallstudios.mhworlddatabase.features.monsters.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.BasicListDelegationAdapter;
import com.gatheringhallstudios.mhworlddatabase.common.Navigator;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;
import com.gatheringhallstudios.mhworlddatabase.common.adapters.MonsterAdapterDelegate;
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterDetailPagerFragment;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Fragment for a list of monsters
 */

public class MonsterListFragment extends Fragment {
    private static final String ARG_TAB = "MONSTER_TAB";

    MonsterListViewModel viewModel;
    RecyclerView recyclerView;

    BasicListDelegationAdapter<MonsterView> adapter;

    public static MonsterListFragment newInstance(MonsterListViewModel.Tab tab) {
        MonsterListFragment f = new MonsterListFragment();
        f.setArguments(new BundleBuilder().putSerializable(ARG_TAB, tab).build());
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, parent);

        // Setup Adapter
        MonsterAdapterDelegate delegate = new MonsterAdapterDelegate(this::handleMonsterSelection);
        adapter = new BasicListDelegationAdapter<>(delegate);

        // Setup RecyclerView
        recyclerView = (RecyclerView) inflater.inflate(R.layout.list_generic, parent, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        recyclerView.setAdapter(adapter);

        MonsterListViewModel.Tab tab = MonsterListViewModel.Tab.LARGE; // default
        Bundle args = getArguments();
        if (args != null) {
            tab = (MonsterListViewModel.Tab) args.getSerializable(ARG_TAB);
        }

        viewModel = ViewModelProviders.of(this).get(MonsterListViewModel.class);
        viewModel.setTab(tab);

        viewModel.getMonsters().observe(this, this::setItems);

        return recyclerView;
    }

    public void setItems(List<MonsterView> monsters) {
        if (adapter != null) {
            adapter.setItems(monsters);
            adapter.notifyDataSetChanged();
        }
    }

    private void handleMonsterSelection(MonsterView monster) {
        Navigator nav = (Navigator)getActivity();
        nav.navigateTo(MonsterDetailPagerFragment.newInstance(monster));
    }
}
