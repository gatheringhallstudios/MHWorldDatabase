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
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_armor_summary.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_large.view.*

import kotlinx.android.synthetic.main.cell_icon_label_text.view.*

class ArmorDetailFragment : Fragment() {
    companion object {
        const val ARG_ARMOR_ID = "ARMOR"
    }

    private val viewModel: ArmorDetailViewModel by lazy {
        ViewModelProviders.of(this).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val armorId = arguments?.getInt(ArmorDetailFragment.ARG_ARMOR_ID) ?: -1
        viewModel.loadArmor(armorId)

        return inflater.inflate(R.layout.fragment_armor_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // todo: Icon Label Text cell probably shouldn't be used for things like defense values

        defense_cell.removeDecorator()
        fire_star_cell.removeDecorator()
        water_star_cell.removeDecorator()
        ice_star_cell.removeDecorator()
        lightning_star_cell.removeDecorator()
        dragon_star_cell.removeDecorator()

        // todo: this kind of change should be part of the cell itself
        fire_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        water_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        ice_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        lightning_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)
        dragon_star_cell.value_text.setTypeface(Typeface.DEFAULT_BOLD)

        viewModel.armor.observe(this, Observer(::populateArmor))
    }

    private fun populateArmor(armorData: ArmorFull?) {
        if (armorData == null) return

        populateArmorBasic(armorData.armor)
        populateSkills(armorData.skills)
        populateSetBonuses(armorData.setBonuses)
        populateComponents(armorData.recipe)
    }

    private fun populateArmorBasic(armor: Armor) {
        armor_detail_name.text = armor.name
        armor_detail_rarity.setTextColor(assetLoader.loadRarityColor(armor.rarity))
        armor_detail_rarity.text = getString(
                R.string.rarity_string,
                armor.rarity)
        armor_icon.setImageDrawable(assetLoader.loadIconFor(armor))

        // set defense label
        defense_cell.setLabelText(getString(
                R.string.armor_defense_value,
                armor.defense_base,
                armor.defense_max,
                armor.defense_augment_max))

        // set elemental defense values
        fire_star_cell.setLabelText(getString(R.string.armor_detail_vs_fire))
        fire_star_cell.setValueText("${armor.fire}")
        water_star_cell.setLabelText(getString(R.string.armor_detail_vs_water))
        water_star_cell.setValueText("${armor.water}")
        ice_star_cell.setLabelText(getString(R.string.armor_detail_vs_ice))
        ice_star_cell.setValueText("${armor.ice}")
        lightning_star_cell.setLabelText(getString(R.string.armor_detail_vs_thunder))
        lightning_star_cell.setValueText("${armor.thunder}")
        dragon_star_cell.setLabelText(getString(R.string.armor_detail_vs_dragon))
        dragon_star_cell.setValueText("${armor.dragon}")

        val slotImages = armor.slots.map {
            context?.getDrawableCompat(SlotEmptyRegistry(it))
        }

        slot1.setImageDrawable(slotImages[0])
        slot2.setImageDrawable(slotImages[1])
        slot3.setImageDrawable(slotImages[2])
    }

    private fun populateSetBonuses(armorSetBonuses: List<ArmorSetBonus>) {
        if (armorSetBonuses.isEmpty()) {
            armor_set_bonus_section.visibility = View.GONE
            return
        }

        // show set bonus section
        armor_set_bonus_section.visibility = View.VISIBLE
        armor_set_bonus_list.removeAllViews()

        //Set the label for the Set name
        val view = layoutInflater.inflate(R.layout.listitem_large, armor_set_bonus_list, false)
        view.item_name.text = armorSetBonuses.first().name
        view.item_icon.visibility = View.GONE
        armor_set_bonus_list.addView(view)

        //Now to set the actual skills
        for (setBonus in armorSetBonuses) {
            val listItem = layoutInflater.inflate(R.layout.listitem_armorset_bonus, null)
            val icon = assetLoader.loadIconFor(setBonus.skillTree)

            listItem.skill_icon.setImageDrawable(icon)
            listItem.piece_bonus_text.text = getString(R.string.armor_detail_piece_bonus, setBonus.required)
            listItem.skill_name.text = setBonus.skillTree.name
            listItem.setOnClickListener {
                getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            armor_set_bonus_list.addView(listItem)
        }
    }

    private fun populateSkills(skills: List<SkillLevel>) {
        if (skills.isEmpty()) {
            armor_skill_section.visibility = View.GONE
            return
        }

        armor_skill_section.visibility = View.VISIBLE
        armor_skill_list.removeAllViews()

        for (skill in skills) {
            //Set the label for the Set name
            val view = IconLabelTextCell(context)
            val icon = assetLoader.loadIconFor(skill.skillTree)
            val levels = "+${skill.level} ${resources.getQuantityString(R.plurals.skills_level, skill.level)}"

            view.removeDecorator()
            view.setLeftIconDrawable(icon)
            view.setLabelText(skill.skillTree.name)
            view.setValueText(levels)
            view.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            armor_skill_list.addView(view)
        }
    }

    private fun populateComponents(components: List<ItemQuantity>) {
        if (components.isEmpty()) {
            armor_components_section.visibility = View.GONE
            return
        }

        armor_components_section.visibility = View.VISIBLE
        armor_components_list.removeAllViews()

        for (itemQuantity in components) {
            val view = IconLabelTextCell(context)
            val icon = assetLoader.loadIconFor(itemQuantity.item)

            view.setLeftIconDrawable(icon)
            view.setLabelText(itemQuantity.item.name)
            view.setValueText(getString(R.string.quantity, itemQuantity.quantity))
            view.setOnClickListener {
                getRouter().navigateItemDetail(itemQuantity.item.id)
            }

            armor_components_list.addView(view)
        }
    }
}