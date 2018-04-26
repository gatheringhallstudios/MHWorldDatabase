package com.gatheringhallstudios.mhworlddatabase.features.items.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView;

import butterknife.ButterKnife;

public class ItemSummaryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_summary, parent, false);
        ButterKnife.bind(this, view);

        ItemDetailViewModel viewmodel = ViewModelProviders.of(getParentFragment()).get(ItemDetailViewModel.class);
        viewmodel.getItem().observe(this, this::populateItem);

        return view;
    }

    private void populateItem(ItemView item) {
        // todo: bind
    }
}
