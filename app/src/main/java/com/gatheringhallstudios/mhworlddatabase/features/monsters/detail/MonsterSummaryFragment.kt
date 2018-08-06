package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.Monster
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterHabitat
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.fragment_monster_summary.*

/**
 * Fragment for displaying Monster Summary
 */
class MonsterSummaryFragment : Fragment() {

    private val viewModel by lazy {
        // this fragment is a "child", so get the parent fragment's
        ViewModelProviders.of(parentFragment!!).get(MonsterDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_monster_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.monster.observe(this, Observer(::populateMonster))
        viewModel.habitats.observe(this, Observer(::populateHabitats))
    }
    /**
     * Populate views with the monster data
     */
    private fun populateMonster(monster: Monster?) {
        if (monster == null) return

        val elemWeakness = monster.weaknesses
        val statusWeakness = monster.status_weaknesses

        val icon = AssetLoader.loadIconFor(monster)

        monster_header.setIconDrawable(icon)
        monster_header.setTitleText(monster.name)
        if (monster.ecology != null) monster_header.setSubtitleText(monster.ecology)
        monster_header.setDescriptionText(monster.description)

        // todo: remove weakness section if both are null
        // note: newer data versions have an 'has_weakness' field. Use that.

        if (elemWeakness != null) {
            fire_star_cell.setStars(elemWeakness.fire)
            water_star_cell.setStars(elemWeakness.water)
            lightning_star_cell.setStars(elemWeakness.thunder)
            ice_star_cell.setStars(elemWeakness.ice)
            dragon_star_cell.setStars(elemWeakness.dragon)
        }

        if (statusWeakness != null) {
            poison_star_cell.setStars(statusWeakness.poison)
            sleep_star_cell.setStars(statusWeakness.sleep)
            paralysis_star_cell.setStars(statusWeakness.paralysis)
            blast_star_cell.setStars(statusWeakness.blast)
            stun_star_cell.setStars(statusWeakness.stun)
        }

        // todo: support alt weaknessess states
    }

    private fun populateHabitats(habitats: List<MonsterHabitat>?) {
        if (habitats == null) return

        if (habitats.isEmpty()) {
            habitat_header.visibility = View.GONE
            return
        }

        if (habitats_layout.childCount > 0)
            habitats_layout.removeAllViews()

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

            habitats_layout.addView(view)
        }
    }
}
