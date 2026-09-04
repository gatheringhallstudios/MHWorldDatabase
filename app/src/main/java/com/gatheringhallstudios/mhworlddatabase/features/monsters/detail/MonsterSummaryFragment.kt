package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.embeds.MonsterAilments
import com.gatheringhallstudios.mhworlddatabase.data.models.Monster
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterHabitat
import com.gatheringhallstudios.mhworlddatabase.data.types.AilmentStrength
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemAilmentBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentMonsterSummaryBinding

/**
 * Fragment for displaying Monster Summary
 */
class MonsterSummaryFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentMonsterSummaryBinding? = null
    private val binding get() = _binding!!


    private val viewModel by lazy {
        // this fragment is a "child", so get the parent fragment's
        ViewModelProvider(parentFragment!!).get(MonsterDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMonsterSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.monster.observe(viewLifecycleOwner, Observer(::populateMonster))
        viewModel.habitats.observe(viewLifecycleOwner, Observer(::populateHabitats))
    }
    /**
     * Populate views with the monster data
     */
    private fun populateMonster(monster: Monster?) {
        if (monster == null) return

        val icon = AssetLoader.loadIconFor(monster)

        binding.monsterHeader.setIconDrawable(icon)
        binding.monsterHeader.setTitleText(monster.name)
        if (monster.ecology != null) binding.monsterHeader.setSubtitleText(monster.ecology)
        binding.monsterHeader.setDescriptionText(monster.description)

        if (monster.has_weakness) {
            populateWeaknessSection(monster)
        }

        populateAilments(monster.ailments)
    }

    /**
     * Populates the weakness data given a monster. Called by populateMonster().
     */
    private fun populateWeaknessSection(monster: Monster) {
        binding.weaknessSection.visibility = View.VISIBLE
        
        val elemWeakness = monster.weaknesses
        val altWeakness = monster.alt_weaknesses
        val statusWeakness = monster.status_weaknesses
        
        if (elemWeakness != null) {
            binding.fireStarCell.setStars(elemWeakness.fire)
            binding.waterStarCell.setStars(elemWeakness.water)
            binding.lightningStarCell.setStars(elemWeakness.thunder)
            binding.iceStarCell.setStars(elemWeakness.ice)
            binding.dragonStarCell.setStars(elemWeakness.dragon)
        }
        
        if (altWeakness != null) {
            binding.fireStarCell.setAltStars(altWeakness.fire)
            binding.waterStarCell.setAltStars(altWeakness.water)
            binding.lightningStarCell.setAltStars(altWeakness.thunder)
            binding.iceStarCell.setAltStars(altWeakness.ice)
            binding.dragonStarCell.setAltStars(altWeakness.dragon)
        }

        if (statusWeakness != null) {
            binding.poisonStarCell.setStars(statusWeakness.poison)
            binding.sleepStarCell.setStars(statusWeakness.sleep)
            binding.paralysisStarCell.setStars(statusWeakness.paralysis)
            binding.blastStarCell.setStars(statusWeakness.blast)
            binding.stunStarCell.setStars(statusWeakness.stun)
        }

        if (!monster.alt_state_description.isNullOrEmpty()) {
            binding.altWeaknessCaption.text = "( ) = ${monster.alt_state_description}"
        }
    }

    private fun populateAilments(ailments: MonsterAilments?) {

        val ailmentList = mutableListOf<String>()
        fun addAilment(@StringRes resId: Int) {
            ailmentList.add(getString(resId))
        }

        // Empty handling
        if (ailments == null) {
            val ailmentBinding = ListitemAilmentBinding.inflate(layoutInflater, binding.ailmentsLayout, false)
            ailmentBinding.ailmentText.text = getString(R.string.general_none)
            binding.ailmentsLayout.addView(ailmentBinding.root)
            return
        }

        // Speicific
        when (ailments.roar) {
            AilmentStrength.NONE -> {}
            AilmentStrength.SMALL -> addAilment(R.string.ailment_roar_small)
            AilmentStrength.LARGE -> addAilment(R.string.ailment_roar_large)
            AilmentStrength.EXTREME -> addAilment(R.string.ailment_roar_extreme)
        }

        when (ailments.wind) {
            AilmentStrength.NONE -> {}
            AilmentStrength.SMALL -> addAilment(R.string.ailment_wind_small)
            AilmentStrength.LARGE -> addAilment(R.string.ailment_wind_large)
            AilmentStrength.EXTREME -> addAilment(R.string.ailment_wind_extreme)
        }

        when (ailments.tremor) {
            AilmentStrength.NONE -> {}
            AilmentStrength.SMALL -> addAilment(R.string.ailment_tremor_small)
            AilmentStrength.LARGE -> addAilment(R.string.ailment_tremor_large)
            AilmentStrength.EXTREME -> addAilment(R.string.ailment_tremor_extreme)
        }

        if (ailments.fireblight) addAilment(R.string.ailment_fireblight)
        if (ailments.waterblight) addAilment(R.string.ailment_waterblight)
        if (ailments.thunderblight) addAilment(R.string.ailment_thunderblight)
        if (ailments.iceblight) addAilment(R.string.ailment_iceblight)
        if (ailments.dragonblight) addAilment(R.string.ailment_dragonblight)
        if (ailments.blastblight) addAilment(R.string.ailment_blastblight)
        if (ailments.poison) addAilment(R.string.ailment_poison)
        if (ailments.sleep) addAilment(R.string.ailment_sleep)
        if (ailments.paralysis) addAilment(R.string.ailment_paralysis)
        if (ailments.bleed) addAilment(R.string.ailment_bleed)
        if (ailments.stun) addAilment(R.string.ailment_stun)
        if (ailments.mud) addAilment(R.string.ailment_mud)
        if (ailments.effluvia) addAilment(R.string.ailment_effluvia)

        if (ailmentList.isEmpty()) addAilment(R.string.general_none)

        // Populate ailments to views
        for (ailment in ailmentList) {
            val ailmentBinding = ListitemAilmentBinding.inflate(layoutInflater, binding.ailmentsLayout, false)
            ailmentBinding.ailmentText.text = ailment
            binding.ailmentsLayout.addView(ailmentBinding.root)
        }

        // Request a layout pass because our height has changed
        binding.ailmentsLayout.requestLayout()

    }

    private fun populateHabitats(habitats: List<MonsterHabitat>?) {
        if (habitats == null) return

        if (habitats.isEmpty()) {
            binding.habitatHeader.visibility = View.GONE
            return
        }

        if (binding.habitatsLayout.childCount > 0)
            binding.habitatsLayout.removeAllViews()

        for (habitat in habitats) {
            val view = IconLabelTextCell(context)

            val areas = StringBuilder()
            habitat.start_area?.let { areas.append("$it \u203A ")}
            habitat.moveAreas?.let {
                areas.append(it.joinToString(", "))
                areas.append(" \u203A ")
            }
            habitat.rest_area?.let { areas.append(it) }

            val icon = AssetLoader.loadIconFor(habitat.location)
            view.setLeftIconType(IconType.PAPER)
            view.setLeftIconDrawable(icon)
            view.setLabelText(habitat.location.name)
            view.setValueText(areas.toString())

            view.setOnClickListener { getRouter().navigateLocationDetail(habitat.location.id) }

            binding.habitatsLayout.addView(view)
        }
    }
}
