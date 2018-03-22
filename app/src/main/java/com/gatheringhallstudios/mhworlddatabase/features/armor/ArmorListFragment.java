package com.gatheringhallstudios.mhworlddatabase.features.armor;

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
import com.gatheringhallstudios.mhworlddatabase.common.adapters.ArmorListAdapter;

/**
 * Created by Carlos on 3/22/2018.
 */

public class ArmorListFragment extends Fragment {
    ArmorViewModel viewModel;
    RecyclerView view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.list_generic, parent, false);
        view.setLayoutManager(new LinearLayoutManager(parent.getContext()));

        viewModel = ViewModelProviders.of(getActivity()).get(ArmorViewModel.class);

        viewModel.GetArmorList().observe(this, (list) -> {
            view.setAdapter(new ArmorListAdapter(list));
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getString(R.string.armor_title));
    }
}
