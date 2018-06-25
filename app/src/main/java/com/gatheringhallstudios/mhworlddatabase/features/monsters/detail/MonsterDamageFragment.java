package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterHitzoneEntity;
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHitzoneView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * Fragment for a list of monsters
 */

public class MonsterDamageFragment extends Fragment {
    MonsterDetailViewModel viewModel;

    private final String TAG = getClass().getSimpleName();

    LinearLayout physicalDamageLayout;
    LinearLayout elementDamageLayout;

    // Thresholds for BOLD numbers
    final int EFFECTIVE_PHYSICAL = 45;
    final int EFFECTIVE_ELEMENTAL = 20;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate and bind our layout
        View view = inflater.inflate(R.layout.fragment_monster_damage, parent, false);

        // Bind views
        physicalDamageLayout = view.findViewById(R.id.physical_damage_layout);
        elementDamageLayout = view.findViewById(R.id.element_damage_layout);

        viewModel = ViewModelProviders.of(getParentFragment()).get(MonsterDetailViewModel.class);
        viewModel.getHitzones().observe(this, this::setHitzones);

        return view;
    }

    /**
     * Set the hitzones to be displayed in the fragment
     *
     * @param hitzones items be of type Reward.
     */
    public void setHitzones(List<MonsterHitzoneView> hitzones) {
        // Clear layouts
        if (physicalDamageLayout.getChildCount() != 0)
            physicalDamageLayout.removeAllViews();

        if (elementDamageLayout.getChildCount() != 0)
            elementDamageLayout.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Populate Physical Damage
        for (MonsterHitzoneView hitzone : hitzones) {
            View physical = inflater.inflate(R.layout.listitem_monster_hitzone, physicalDamageLayout, false);

            TextView bodyPart = physical.findViewById(R.id.body_part);
            TextView cut = physical.findViewById(R.id.dmg2);
            TextView impact = physical.findViewById(R.id.dmg3);
            TextView shot = physical.findViewById(R.id.dmg4);
            TextView ko = physical.findViewById(R.id.dmg5);

            // Bind views
            bodyPart.setText(hitzone.getBody_part());
            // TODO Altered status names should be a different font. Ex: (Enraged)

            MonsterHitzoneEntity data = hitzone.getData();
            bindHitzone(cut, data.getCut(), EFFECTIVE_PHYSICAL);
            bindHitzone(impact, data.getImpact(), EFFECTIVE_PHYSICAL);
            bindHitzone(shot, data.getShot(), EFFECTIVE_PHYSICAL);
            bindHitzone(ko, data.getKo(), EFFECTIVE_PHYSICAL);

            physicalDamageLayout.addView(physical);
        }

        // Populate Elemental Damage
        for (MonsterHitzoneView hitzone : hitzones) {
            View elemental = inflater.inflate(R.layout.listitem_monster_hitzone, elementDamageLayout, false);

            TextView bodyPart = elemental.findViewById(R.id.body_part);
            TextView fire = elemental.findViewById(R.id.dmg1);
            TextView water = elemental.findViewById(R.id.dmg2);
            TextView ice = elemental.findViewById(R.id.dmg3);
            TextView thunder = elemental.findViewById(R.id.dmg4);
            TextView dragon = elemental.findViewById(R.id.dmg5);

            // Bind views
            bodyPart.setText(hitzone.getBody_part());
            // TODO Altered status names should be a different font. Ex: (Enraged)

            MonsterHitzoneEntity data = hitzone.getData();
            bindHitzone(fire, data.getFire(), EFFECTIVE_ELEMENTAL);
            bindHitzone(water, data.getWater(), EFFECTIVE_ELEMENTAL);
            bindHitzone(ice, data.getIce(), EFFECTIVE_ELEMENTAL);
            bindHitzone(thunder, data.getThunder(), EFFECTIVE_ELEMENTAL);
            bindHitzone(dragon, data.getDragon(), EFFECTIVE_ELEMENTAL);

            elementDamageLayout.addView(elemental);
        }
    }

    private void bindHitzone(TextView view, int value, int threshold) {
        if (value >= threshold) {
            view.setTypeface(null, Typeface.BOLD);
            view.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryTextColor));
        }
        view.setText(Integer.toString(value));
    }

}
