package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.getAssetDrawable
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHabitatView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
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
    private fun populateMonster(monster: MonsterView?) {
        if (monster == null) return

        val elemWeakness = monster.data.weaknesses
        val statusWeakness = monster.data.status_weaknesses

        val icon = context?.getAssetDrawable("monsters/${monster.id}.png")
        item_icon.setImageDrawable(icon)

        monster_ecology.text = monster.ecology
        item_name.text = monster.name
        monster_description.text = monster.description

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

    private fun populateHabitats(habitats: List<MonsterHabitatView>?) {
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
            habitat.data.start_area?.let { areas.append("$it \u203A ")}
            habitat.data.move_area?.let { areas.append("$it \u203A ") }
            habitat.data.rest_area?.let { areas.append(it) }

            val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_question_mark)

            view.setLeftIconDrawable(icon)
            view.setLabelText(habitat.location_name)
            view.setValueText(areas.toString())

            habitats_layout.addView(view)
        }
    }
}
