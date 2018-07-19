package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.*
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSetBonus
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSkill
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_armor_summary.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_large.view.*

import kotlinx.android.synthetic.main.cell_icon_label_text.view.*

class ArmorSummaryFragment : Fragment() {

    private val viewModel: ArmorDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_armor_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.armor.observe(this, Observer(::populateArmor))
        viewModel.armorSetSkill.observe(this, Observer(::populateSetBonuses))
        viewModel.armorSkill.observe(this, Observer(::populateSkills))
    }

    private fun populateArmor(armor: Armor?) {
        if (armor == null) return
        val loader = AssetLoader(context!!)

        armor_detail_name.text = armor.name
        armor_detail_rarity.setTextColor(loader.loadRarityColor(armor.rarity))
        armor_detail_rarity.text = getString(
                R.string.rarity_string,
                armor.rarity)
        armor_icon.setImageDrawable(loader.loadArmorIcon(armor.armor_type, armor.rarity))

        populateDefense(armor)
        populateSlots(armor)
    }

    private fun populateSlots(armor: Armor) {
        slots_icon.background = null
        slots_icon.setPadding(0, 0, 0, 0)

        val slotImages = armor.slots.map {
            context?.getDrawableCompat(SlotEmptyRegistry(it))
        }

        slot1.setImageDrawable(slotImages[0])
        slot2.setImageDrawable(slotImages[1])
        slot3.setImageDrawable(slotImages[2])

    }

    private fun populateSetBonuses(armorSetBonusViews: List<ArmorSetBonus>?) {
        if (armorSetBonusViews.orEmpty().isEmpty()) {
            return
        }

        // show set bonus section
        armor_set_bonus_header.visibility = View.VISIBLE
        armor_set_bonus_layout.visibility = View.VISIBLE

        if (armor_set_bonus_layout.childCount > 0) {
            armor_set_bonus_layout.removeAllViews()
        }

        //Set the label for the Set name
        val view = layoutInflater.inflate(R.layout.listitem_large, null)
        view.item_name.text = armorSetBonusViews!!.first().name
        view.item_icon.visibility = View.GONE
        armor_set_bonus_layout.addView(view)

        //Now to set the actual skills
        for (armorSetBonusView in armorSetBonusViews) {
            val listItem = layoutInflater.inflate(R.layout.listitem_armorset_bonus, null)

            // todo: replace with correct icon
            val icon = assetLoader.loadSkillIcon(armorSetBonusView.icon_color)

            listItem.skill_icon.setImageDrawable(icon)
            listItem.piece_bonus_text.text = getString(R.string.armor_detail_piece_bonus,
                    armorSetBonusView.required)
            listItem.skill_name.text = armorSetBonusView.skillName
            listItem.setOnClickListener {
                getRouter().navigateSkillDetail(armorSetBonusView.skilltree_id)
            }

            armor_set_bonus_layout.addView(listItem)
        }
    }

    private fun populateSkills(skills: List<ArmorSkill>?) {
        if (skills.orEmpty().isEmpty()) {
            return
        }

        if (armor_skill_layout.childCount > 0) {
            armor_skill_layout.removeAllViews()
        }

        for (skill in skills!!) {
            //Set the label for the Set name
            val view = IconLabelTextCell(context)
            val icon = assetLoader.loadSkillIcon(skill.icon_color)
            val levels = "+${skill.skillLevel} ${resources.getQuantityString(R.plurals.skills_level, skill.skillLevel)}"

            view.setLeftIconDrawable(icon)
            view.setLabelText(skill.armor.name)
            view.setValueText(levels)
            view.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skilltree_id)
            }
            view.removeDecorator()

            armor_skill_layout.addView(view)
        }
    }

    private fun populateDefense(armor: Armor) {
        defense_cell.setLabelText(getString(
                R.string.armor_defense_value,
                armor.defense_base,
                armor.defense_max,
                armor.defense_augment_max))
        defense_cell.removeDecorator()

        fire_star_cell.setLabelText(getString(R.string.armor_detail_vs_fire))
        fire_star_cell.setValueText("${armor.fire}")
        fire_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        fire_star_cell.removeDecorator()

        water_star_cell.setLabelText(getString(R.string.armor_detail_vs_water))
        water_star_cell.setValueText("${armor.water}")
        water_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        water_star_cell.removeDecorator()

        ice_star_cell.setLabelText(getString(R.string.armor_detail_vs_ice))
        ice_star_cell.setValueText("${armor.ice}")
        ice_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        ice_star_cell.removeDecorator()

        lightning_star_cell.setLabelText(getString(R.string.armor_detail_vs_thunder))
        lightning_star_cell.setValueText("${armor.thunder}")
        lightning_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        lightning_star_cell.removeDecorator()

        dragon_star_cell.setLabelText(getString(R.string.armor_detail_vs_dragon))
        dragon_star_cell.setValueText("${armor.dragon}")
        dragon_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        dragon_star_cell.removeDecorator()
    }
}