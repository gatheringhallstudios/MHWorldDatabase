package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterHitzone
import kotlinx.android.synthetic.main.fragment_monster_damage.*
import kotlinx.android.synthetic.main.listitem_monster_hitzone.view.*

/**
 * Fragment for damage information about a monster
 */

class MonsterDamageFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(MonsterDetailViewModel::class.java)
    }

    private val TAG = javaClass.simpleName

    // Thresholds for BOLD numbers
    internal val EFFECTIVE_PHYSICAL = 45
    internal val EFFECTIVE_ELEMENTAL = 20

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate and bind our layout
        return inflater.inflate(R.layout.fragment_monster_damage, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.hitzones.observe(this, Observer { this.setHitzones(it) })
    }

    /**
     * Set the hitzones to be displayed in the fragment
     *
     * @param hitzones items be of type Reward.
     */
    private fun setHitzones(hitzones: List<MonsterHitzone>?) {
        if (hitzones == null) return

        val physicalDamageLayout = this.physical_damage_layout
        val elementDamageLayout = this.element_damage_layout

        // Clear layouts
        if (physicalDamageLayout.childCount != 0)
            physicalDamageLayout.removeAllViews()

        if (elementDamageLayout.childCount != 0)
            elementDamageLayout.removeAllViews()

        val inflater = LayoutInflater.from(context)

        // Populate Physical Damage
        for ((data, bodyPartName) in hitzones) {
            val physical = inflater.inflate(R.layout.listitem_monster_hitzone, physicalDamageLayout, false)

            // Bind views
            physical.body_part.text = bodyPartName
            // TODO Altered status names should be a different font. Ex: (Enraged)

            bindHitzone(physical.dmg2, data.cut, EFFECTIVE_PHYSICAL)
            bindHitzone(physical.dmg3, data.impact, EFFECTIVE_PHYSICAL)
            bindHitzone(physical.dmg4, data.shot, EFFECTIVE_PHYSICAL)
            bindHitzone(physical.dmg5, data.ko, EFFECTIVE_PHYSICAL)

            physicalDamageLayout!!.addView(physical)
        }

        // Populate Elemental Damage
        for ((data, bodyPartName) in hitzones) {
            val elemental = inflater.inflate(R.layout.listitem_monster_hitzone, elementDamageLayout, false)

            // Bind views
            elemental.body_part.text = bodyPartName
            // TODO Altered status names should be a different font. Ex: (Enraged)

            bindHitzone(elemental.dmg1, data.fire, EFFECTIVE_ELEMENTAL)
            bindHitzone(elemental.dmg2, data.water, EFFECTIVE_ELEMENTAL)
            bindHitzone(elemental.dmg3, data.thunder, EFFECTIVE_ELEMENTAL)
            bindHitzone(elemental.dmg4, data.ice, EFFECTIVE_ELEMENTAL)
            bindHitzone(elemental.dmg5, data.dragon, EFFECTIVE_ELEMENTAL)

            elementDamageLayout!!.addView(elemental)
        }
    }

    /**
     * Apply styles to hitzones
     */
    private fun bindHitzone(view: TextView, value: Int, threshold: Int) {
        if (value == 0) {
            // do nothing
        } else if (value > 0 && value < threshold) {
            view.setTextColor(ContextCompat.getColor(context!!, R.color.textColorMedium))
        } else if (value >= threshold) {
            view.setTypeface(null, Typeface.BOLD)
            view.setTextColor(ContextCompat.getColor(context!!, R.color.textColorHigh))
        }

        view.text = value.toString()
    }

}
