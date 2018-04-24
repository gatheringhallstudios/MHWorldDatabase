package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.views.Hitzone;
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for a list of monsters
 */

public class MonsterDamageFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private static final String ARG_MONSTER_ID = "MONSTER_ID";

    @BindView(R.id.physical_damage_layout)
    LinearLayout physicalDamageLayout;
    @BindView(R.id.element_damage_layout)
    LinearLayout elementDamageLayout;

    MonsterDamageViewModel viewModel;

    // Thresholds for BOLD numbers
    final int EFFECTIVE_PHYSICAL = 45;
    final int EFFECTIVE_ELEMENTAL = 20;

    public static MonsterDamageFragment newInstance(int monsterId) {
        MonsterDamageFragment f = new MonsterDamageFragment();
        f.setArguments(new BundleBuilder().putSerializable(ARG_MONSTER_ID, monsterId).build());
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate and bind our layout
        View view = inflater.inflate(R.layout.fragment_monster_damage, parent, false);
        ButterKnife.bind(this, view);

        // Retrieve MonsterID from args
        Bundle args = getArguments();
        int monsterId = 0;
        if (args != null) {
            monsterId = args.getInt(ARG_MONSTER_ID, 0);
        }

        // Get and setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(MonsterDamageViewModel.class);
        viewModel.setMonsterId(monsterId);
        viewModel.getHitzoneData().observe(this, (hitzones) -> setHitzones(hitzones, inflater));

        return view;
    }

    /**
     * Set the hitzones to be displayed in the fragment
     *
     * @param hitzones items be of type Reward.
     */
    public void setHitzones(List<Hitzone> hitzones, LayoutInflater inflater) {
        // Clear layouts
        if (physicalDamageLayout.getChildCount() != 0)
            physicalDamageLayout.removeAllViews();

        if (elementDamageLayout.getChildCount() != 0)
            elementDamageLayout.removeAllViews();

        // Populate Physical Damage
        for (Hitzone hitzone : hitzones) {
            View physical = inflater.inflate(R.layout.listitem_monster_hitzone, physicalDamageLayout, false);

            // Find target views manually since we can't run ButterKnife.bind more than once.
            TextView bodyPart = physical.findViewById(R.id.body_part);
            TextView cut = physical.findViewById(R.id.dmg2);
            TextView impact = physical.findViewById(R.id.dmg3);
            TextView shot = physical.findViewById(R.id.dmg4);
            TextView ko = physical.findViewById(R.id.dmg5);

            // Bind views
            bodyPart.setText(hitzone.bodyPart);
            // TODO Altered status names should be a different font. Ex: (Enraged)
            bindHitzone(cut, hitzone.cut, EFFECTIVE_PHYSICAL);
            bindHitzone(impact, hitzone.impact, EFFECTIVE_PHYSICAL);
            bindHitzone(shot, hitzone.shot, EFFECTIVE_PHYSICAL);
            bindHitzone(ko, hitzone.ko, EFFECTIVE_PHYSICAL);

            physicalDamageLayout.addView(physical);
        }

        // Populate Elemental Damage
        for (Hitzone hitzone : hitzones) {
            View elemental = inflater.inflate(R.layout.listitem_monster_hitzone, elementDamageLayout, false);

            // Find target views manually since we can't run ButterKnife.bind more than once.
            TextView bodyPart = elemental.findViewById(R.id.body_part);
            TextView fire = elemental.findViewById(R.id.dmg1);
            TextView water = elemental.findViewById(R.id.dmg2);
            TextView ice = elemental.findViewById(R.id.dmg3);
            TextView thunder = elemental.findViewById(R.id.dmg4);
            TextView dragon = elemental.findViewById(R.id.dmg5);

            // Bind views
            bodyPart.setText(hitzone.bodyPart);
            // TODO Altered status names should be a different font. Ex: (Enraged)
            bindHitzone(fire, hitzone.fire, EFFECTIVE_ELEMENTAL);
            bindHitzone(water, hitzone.water, EFFECTIVE_ELEMENTAL);
            bindHitzone(ice, hitzone.ice, EFFECTIVE_ELEMENTAL);
            bindHitzone(thunder, hitzone.thunder, EFFECTIVE_ELEMENTAL);
            bindHitzone(dragon, hitzone.dragon, EFFECTIVE_ELEMENTAL);

            elementDamageLayout.addView(elemental);
        }
    }

    private void bindHitzone(TextView view, int value, int threshold) {
        if (value >= threshold) view.setTypeface(null, Typeface.BOLD);
        view.setText(Integer.toString(value));
    }

}
