package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.components.IconStarCell;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for displaying Monster Summary
 */

public class MonsterSummaryFragment extends Fragment {

    MonsterDetailViewModel viewModel;

    @BindView(R.id.monster_icon) ImageView monsterIcon;
    @BindView(R.id.monster_name) TextView monsterName;
    @BindView(R.id.monster_description) TextView monsterDescription;

    @BindView(R.id.fire_star_cell) IconStarCell fireStarCell;
    @BindView(R.id.water_star_cell) IconStarCell waterStarCell;
    @BindView(R.id.lightning_star_cell) IconStarCell lightningStarCell;
    @BindView(R.id.ice_star_cell) IconStarCell iceStarCell;
    @BindView(R.id.dragon_star_cell) IconStarCell dragonStarCell;
    @BindView(R.id.poison_star_cell) IconStarCell poisonStarCell;
    @BindView(R.id.sleep_star_cell) IconStarCell sleepStarCell;
    @BindView(R.id.paralysis_star_cell) IconStarCell paralysisStarCell;
    @BindView(R.id.blast_star_cell) IconStarCell blastStarCell;
    @BindView(R.id.stun_star_cell) IconStarCell stunStarCell;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monster_summary, parent, false);
        ButterKnife.bind(this, view);

        // Retrieve the viewmodel from the parent fragment
        viewModel = ViewModelProviders.of(getParentFragment()).get(MonsterDetailViewModel.class);
        viewModel.getData().observe(this, this::populateMonster);

        return view;
    }

    /**
     * Populate views with the monster data
     */
    private void populateMonster(MonsterView monster) {
        // monsterIcon.setIcon(someIcon)
        monsterName.setText(monster.name);
        monsterDescription.setText(monster.description);

        // TODO Populate with real data when available
        fireStarCell.setStars(getRandomStars());
        waterStarCell.setStars(getRandomStars());
        lightningStarCell.setStars(getRandomStars());
        iceStarCell.setStars(getRandomStars());
        dragonStarCell.setStars(getRandomStars());

        poisonStarCell.setStars(getRandomStars());
        sleepStarCell.setStars(getRandomStars());
        paralysisStarCell.setTag(getRandomStars());
        blastStarCell.setStars(getRandomStars());
        stunStarCell.setStars(getRandomStars());
    }

    private int getRandomStars() {
        double random = Math.floor(Math.random() * 4);
        return (int) random;
    }
}
