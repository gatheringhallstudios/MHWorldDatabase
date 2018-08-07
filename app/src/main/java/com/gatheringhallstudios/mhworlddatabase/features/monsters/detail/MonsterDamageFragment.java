package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.entities.MonsterHitzoneEntity;
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterHitzone;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for a list of monsters
 */

public class MonsterDamageFragment extends Fragment {
    MonsterDetailViewModel viewModel;

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.physical_damage_layout)
    LinearLayout physicalDamageLayout;
    @BindView(R.id.element_damage_layout)
    LinearLayout elementDamageLayout;

    // Thresholds for BOLD values
    final int EFFECTIVE_PHYSICAL = 45;
    final int EFFECTIVE_ELEMENTAL = 20;

    // Types to color each effective values
    final String ELEMENT_FIRE = "fire";
    final String ELEMENT_WATER = "water";
    final String ELEMENT_THUNDER = "thunder";
    final String ELEMENT_ICE = "ice";
    final String ELEMENT_DRAGON = "dragon";
    final String ELEMENT_NONE = "none";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate and bind our layout
        View view = inflater.inflate(R.layout.fragment_monster_damage, parent, false);
        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(getParentFragment()).get(MonsterDetailViewModel.class);
        viewModel.getHitzones().observe(this, this::setHitzones);

        return view;
    }

    /**
     * Set the hitzones to be displayed in the fragment
     *
     * @param hitzones items be of type Reward.
     */
    public void setHitzones(List<MonsterHitzone> hitzones) {
        // Clear layouts
        if (physicalDamageLayout.getChildCount() != 0)
            physicalDamageLayout.removeAllViews();

        if (elementDamageLayout.getChildCount() != 0)
            elementDamageLayout.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Populate Physical Damage
        for (MonsterHitzone hitzone : hitzones) {
            View physical = inflater.inflate(R.layout.listitem_monster_hitzone, physicalDamageLayout, false);

            // Find target views manually since we can't run ButterKnife.bind more than once.
            TextView bodyPartView = physical.findViewById(R.id.body_part);
            TextView cutView = physical.findViewById(R.id.dmg2);
            TextView impactView = physical.findViewById(R.id.dmg3);
            TextView shotView = physical.findViewById(R.id.dmg4);
            TextView koView = physical.findViewById(R.id.dmg5);

            // Bind views
            bodyPartView.setText(hitzone.getBody_part());
            // TODO Altered status names should be a different font. Ex: (Enraged)

            MonsterHitzoneEntity data = hitzone.getData();
            bindHitzone(cutView, data.getCut(), EFFECTIVE_PHYSICAL, ELEMENT_NONE);
            bindHitzone(impactView, data.getImpact(), EFFECTIVE_PHYSICAL, ELEMENT_NONE);
            bindHitzone(shotView, data.getShot(), EFFECTIVE_PHYSICAL, ELEMENT_NONE);
            bindHitzone(koView, data.getKo(), EFFECTIVE_PHYSICAL, ELEMENT_NONE);

            physicalDamageLayout.addView(physical);
        }

        // Populate Elemental Damage
        for (MonsterHitzone hitzone : hitzones) {
            View elemental = inflater.inflate(R.layout.listitem_monster_hitzone, elementDamageLayout, false);

            // Find target views manually since we can't run ButterKnife.bind more than once.
            TextView bodyPartView = elemental.findViewById(R.id.body_part);
            TextView fireView = elemental.findViewById(R.id.dmg1);
            TextView waterView = elemental.findViewById(R.id.dmg2);
            TextView thunderView = elemental.findViewById(R.id.dmg3);
            TextView iceView = elemental.findViewById(R.id.dmg4);
            TextView dragonView = elemental.findViewById(R.id.dmg5);

            // Bind views
            bodyPartView.setText(hitzone.getBody_part());
            // TODO Altered status names should be a different font. Ex: (Enraged)

            MonsterHitzoneEntity data = hitzone.getData();
            bindHitzone(fireView, data.getFire(), EFFECTIVE_ELEMENTAL, ELEMENT_FIRE);
            bindHitzone(waterView, data.getWater(), EFFECTIVE_ELEMENTAL, ELEMENT_WATER);
            bindHitzone(thunderView, data.getThunder(), EFFECTIVE_ELEMENTAL, ELEMENT_THUNDER);
            bindHitzone(iceView, data.getIce(), EFFECTIVE_ELEMENTAL, ELEMENT_ICE);
            bindHitzone(dragonView, data.getDragon(), EFFECTIVE_ELEMENTAL, ELEMENT_DRAGON);

            elementDamageLayout.addView(elemental);
        }
    }

    /**
     * Apply styles to hitzones
     */
    private void bindHitzone(TextView view, int value, int threshold, String element) {

        view.setText(Integer.toString(value));

        // Emphasise text based on effectiveness
        if (value == 0) {
            // do nothing
        } else if (value > 0 && value < threshold) {
            view.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorMedium));
        } else if (value >= threshold) {
            view.setTypeface(null, Typeface.BOLD);

            // Emphasize elemental damage with color
            // These colors are temporary until we can find a more appropriate, subtler palette
            switch (element) {
                case ELEMENT_FIRE:
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.icon_red));
                    break;
                case ELEMENT_WATER:
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.icon_blue));
                    break;
                case ELEMENT_THUNDER:
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.icon_yellow));
                    break;
                case ELEMENT_ICE:
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.icon_blue));
                    break;
                case ELEMENT_DRAGON:
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.icon_dark_purple));
                    break;
                case ELEMENT_NONE:
                    // intentional fallthrough
                default:
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorHigh));
            }
        }
    }
}
