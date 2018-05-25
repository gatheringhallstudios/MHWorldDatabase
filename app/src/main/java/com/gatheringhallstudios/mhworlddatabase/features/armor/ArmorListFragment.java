package com.gatheringhallstudios.mhworlddatabase.features.armor;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.adapters.BasicListDelegationAdapter;
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment;
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasic;

import java.util.List;

/**
 * Created by Carlos on 3/22/2018.
 */

public class ArmorListFragment extends RecyclerViewFragment {
    ArmorAdapterDelegate delegate = new ArmorAdapterDelegate();
    BasicListDelegationAdapter<ArmorBasic> adapter
            = new BasicListDelegationAdapter<>(delegate);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.setAdapter(adapter);

        ArmorViewModel viewModel = ViewModelProviders.of(this).get(ArmorViewModel.class);
        viewModel.getArmorList().observe(this, this::setItems);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(getString(R.string.armor_title));
    }

    public void setItems(List<ArmorBasic> armor) {
        adapter.setItems(armor);
        adapter.notifyDataSetChanged();
    }
}
