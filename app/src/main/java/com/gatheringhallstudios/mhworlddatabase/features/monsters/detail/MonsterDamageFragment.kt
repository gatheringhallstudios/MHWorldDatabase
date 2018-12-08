package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterBreak
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterHitzone
import com.gatheringhallstudios.mhworlddatabase.data.types.Extract
import com.gatheringhallstudios.mhworlddatabase.util.DataSynchronizer
import com.gatheringhallstudios.mhworlddatabase.util.DataWatcher
import com.gatheringhallstudios.mhworlddatabase.util.getColorCompat

import kotlinx.android.synthetic.main.fragment_monster_damage.*
import kotlinx.android.synthetic.main.fragment_monster_damage_breaks.*
import kotlinx.android.synthetic.main.fragment_monster_damage_hitzones.*
import kotlinx.android.synthetic.main.listitem_monster_break.view.*
import kotlinx.android.synthetic.main.listitem_monster_hitzone.view.*

/** Resolves a hitzone text value to a string */
private fun resolveInt(value: Int?) = when (value) {
    null, 0 -> "-"
    else -> value.toString()
}


/**
 * Special data holder to synchronize damage data to handle all at once.
 */
class MonsterDamageData: DataSynchronizer() {
    var hitzones: List<MonsterHitzone> by DataWatcher(this)
    var breaks: List<MonsterBreak> by DataWatcher(this)
}

/**
 * Fragment for a monster's hitzone and break values
 */
class MonsterDamageFragment : androidx.fragment.app.Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(MonsterDetailViewModel::class.java)
    }

    private val TAG = javaClass.simpleName

    // Thresholds for BOLD values
    internal val EFFECTIVE_PHYSICAL = 45
    internal val EFFECTIVE_ELEMENTAL = 20

    // Types to color each effective values
    internal val ELEMENT_FIRE = "fire"
    internal val ELEMENT_WATER = "water"
    internal val ELEMENT_THUNDER = "thunder"
    internal val ELEMENT_ICE = "ice"
    internal val ELEMENT_DRAGON = "dragon"
    internal val ELEMENT_NONE = "none"

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate and bind our layout
        return inflater.inflate(R.layout.fragment_monster_damage, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // synchronize data loaded
        val damageData = MonsterDamageData()

        viewModel.hitzones.observe(this, Observer  {
            if (it != null) damageData.hitzones = it
        })
        viewModel.breaks.observe(this, Observer {
            if (it != null) damageData.breaks = it
        })

        damageData.observeAllLoaded(this) {
            // make either empty or damage/hitzones visible depending on if loaded
            if (damageData.hitzones.isEmpty() && damageData.breaks.isEmpty()) {
                empty_section.visibility = View.VISIBLE
            } else {
                damage_section.visibility = View.VISIBLE

                if (damageData.hitzones.isNotEmpty()) {
                    hitzone_section.visibility = View.VISIBLE
                    populateHitzones(damageData.hitzones)
                }

                if (damageData.breaks.isNotEmpty()) {
                    break_section.visibility = View.VISIBLE
                    populateBreaks(damageData.breaks)
                }
            }
        }
    }

    /**
     * Set the hitzones to be displayed in the fragment
     *
     * @param hitzones items be of type Reward.
     */
    fun populateHitzones(hitzones: List<MonsterHitzone>?) {
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

            bindHitzone(physical.dmg2, data.cut, EFFECTIVE_PHYSICAL, ELEMENT_NONE)
            bindHitzone(physical.dmg3, data.impact, EFFECTIVE_PHYSICAL, ELEMENT_NONE)
            bindHitzone(physical.dmg4, data.shot, EFFECTIVE_PHYSICAL, ELEMENT_NONE)
            bindHitzone(physical.dmg5, data.ko, EFFECTIVE_PHYSICAL, ELEMENT_NONE)

            physicalDamageLayout!!.addView(physical)
        }

        // Populate Elemental Damage
        for ((data, bodyPartName) in hitzones) {
            val elemental = inflater.inflate(R.layout.listitem_monster_hitzone, elementDamageLayout, false)

            // Bind views
            elemental.body_part.text = bodyPartName
            // TODO Altered status names should be a different font. Ex: (Enraged)

            bindHitzone(elemental.dmg1, data.fire, EFFECTIVE_ELEMENTAL, ELEMENT_FIRE)
            bindHitzone(elemental.dmg2, data.water, EFFECTIVE_ELEMENTAL, ELEMENT_WATER)
            bindHitzone(elemental.dmg3, data.thunder, EFFECTIVE_ELEMENTAL, ELEMENT_THUNDER)
            bindHitzone(elemental.dmg4, data.ice, EFFECTIVE_ELEMENTAL, ELEMENT_ICE)
            bindHitzone(elemental.dmg5, data.dragon, EFFECTIVE_ELEMENTAL, ELEMENT_DRAGON)

            elementDamageLayout.addView(elemental)
        }
    }

    fun populateBreaks(breaks: List<MonsterBreak>?) {
        break_layout.removeAllViews()
        if (breaks == null) return

        val inflater = LayoutInflater.from(context)

        for (breakData in breaks) {
            val breakView = inflater.inflate(R.layout.listitem_monster_break, break_layout, false)

            breakView.break_part_name.text = breakData.part_name

            breakView.flinch.text = resolveInt(breakData.flinch)
            breakView.flinch.setTextColor(ContextCompat.getColor(context!!, R.color.textColorHigh))
            breakView.wound.text = resolveInt(breakData.wound)
            breakView.wound.setTextColor(ContextCompat.getColor(context!!, R.color.textColorHigh))
            breakView.sever.text = resolveInt(breakData.sever)
            breakView.sever.setTextColor(ContextCompat.getColor(context!!, R.color.textColorHigh))

            breakView.extract.text = context?.getString(when (breakData.extract) {
                Extract.ORANGE -> R.string.extract_orange_abbr
                Extract.RED -> R.string.extract_red_abbr
                Extract.WHITE -> R.string.extract_white_abbr
                Extract.GREEN -> R.string.extract_green_abbr
            })

            breakView.extract.setTextColor(context?.getColorCompat(when (breakData.extract) {
                Extract.ORANGE -> R.color.icon_orange
                Extract.RED -> R.color.icon_red
                Extract.WHITE -> R.color.icon_white
                Extract.GREEN -> R.color.icon_green
            }) ?: 0)

            break_layout.addView(breakView)
        }
    }

    /**
     * Apply styles to hitzones
     */
    private fun bindHitzone(view: TextView, value: Int, threshold: Int, element: String) {
        view.text = resolveInt(value)

        // Emphasise text based on effectiveness
        if (value == 0) {
            // do nothing
        } else if (value >= threshold) {
            view.setTypeface(null, Typeface.BOLD)

            // Emphasize elemental damage with color
            // These colors are temporary until we can find a more appropriate, subtler palette
            val colorResource = when (element) {
                ELEMENT_FIRE -> R.color.icon_red
                ELEMENT_WATER -> R.color.icon_blue
                ELEMENT_THUNDER -> R.color.icon_yellow
                ELEMENT_ICE -> R.color.icon_blue
                ELEMENT_DRAGON -> R.color.icon_dark_purple
                else -> R.color.textColorHigh
            }
            view.setTextColor(ContextCompat.getColor(context!!, colorResource))
        } else {
            // Medium effectiveness
            view.setTextColor(ContextCompat.getColor(context!!, R.color.textColorMedium))
        }
    }
}
