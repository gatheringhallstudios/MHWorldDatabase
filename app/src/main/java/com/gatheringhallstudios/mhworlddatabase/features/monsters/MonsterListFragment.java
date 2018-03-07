package com.gatheringhallstudios.mhworlddatabase.features.monsters;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.Monster;

/**
 * Monster list
 */

public class MonsterListFragment extends Fragment {
    MonsterViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MonsterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_monsters, container, false);
        viewModel.getMonsters().observe(this, (item) -> {
            for (Monster monster : item) {
                // todo: do something with this
                //Log.w("MHWTest", monster.getName());
            }
        });
        return root;
    }
}
