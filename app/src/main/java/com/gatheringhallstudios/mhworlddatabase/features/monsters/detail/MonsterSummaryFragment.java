package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.components.IconLabelTextCell;
import com.gatheringhallstudios.mhworlddatabase.common.components.IconStarCell;
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryElemental;
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryStatus;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHabitatView;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView;

import java.util.List;

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

    @BindView(R.id.habitats_layout)
    LinearLayout habitatsLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monster_summary, parent, false);
        ButterKnife.bind(this, view);

        // Retrieve the viewmodel from the parent fragment
        viewModel = ViewModelProviders.of(getParentFragment()).get(MonsterDetailViewModel.class);
        viewModel.getData().observe(this, this::populateMonster);
        viewModel.getHabitats().observe(this, this::populateHabitats);

        return view;
    }

    /**
     * Populate views with the monster data
     */
    private void populateMonster(MonsterView monster) {
        WeaknessSummaryElemental elemWeakness = monster.weaknesses;
        WeaknessSummaryStatus statusWeakness = monster.status_weaknesses;

        // note: newer data versions have an 'has_weakness' field. Use that.
        if (elemWeakness == null) {
            return;
            // todo: remove entire weaknesses section instead
        }

        // monsterIcon.setIcon(someIcon)
        monsterName.setText(monster.name);
        monsterDescription.setText(monster.description);

        fireStarCell.setStars(elemWeakness.fire);
        waterStarCell.setStars(elemWeakness.water);
        lightningStarCell.setStars(elemWeakness.thunder);
        iceStarCell.setStars(elemWeakness.ice);
        dragonStarCell.setStars(elemWeakness.dragon);

        poisonStarCell.setStars(statusWeakness.poison);
        sleepStarCell.setStars(statusWeakness.sleep);
        paralysisStarCell.setTag(statusWeakness.paralysis);
        blastStarCell.setStars(statusWeakness.blast);
        stunStarCell.setStars(statusWeakness.stun);

        // todo: support alt weaknessess states
    }

    private void populateHabitats(List<MonsterHabitatView> habitats) {
        // todo: if habitats is empty, remove entire habitats section

        if (habitatsLayout.getChildCount() > 0)
            habitatsLayout.removeAllViews();

        for (MonsterHabitatView habitat : habitats) {
            IconLabelTextCell view = new IconLabelTextCell(getContext());

            StringBuilder areas = new StringBuilder();
            if (habitat.start_area != null) {
                areas.append(habitat.start_area);
                areas.append(", ");
            }
            if (habitat.move_area != null) {
                areas.append(habitat.move_area);
                areas.append(", ");
            }
            if (habitat.rest_area != null) {
                areas.append(habitat.rest_area);
            }

            Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.question_mark_grey);

            view.setLeftIconDrawable(icon);
            view.setLabelText(habitat.location_name);
            view.setValueText(areas.toString());

            habitatsLayout.addView(view);
        }
    }
}
