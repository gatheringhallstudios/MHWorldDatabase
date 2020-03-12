package com.gatheringhallstudios.mhworlddatabase.features.armor.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_armor_set_summary.*
import kotlinx.android.synthetic.main.fragment_armor_summary.*
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*

class ArmorSetDetailFragment : androidx.fragment.app.Fragment() {
    private val viewModel: ArmorDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_armor_set_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.armorSet.observe(this, Observer(::populateArmorSet))
    }

    //Populate the stats from the other pieces of the armor set
    private fun populateArmorSet(armorSet: ArmorSetFull) {
        armor_set_header.setIconType(IconType.ZEMBELLISHED)
        armor_set_header.setTitleText(armorSet.armorset_name)
        armor_set_header.setSubtitleText(getString(R.string.format_rarity, armorSet.rarity))
        armor_set_header.setSubtitleColor(AssetLoader.loadRarityColor(armorSet.rarity))
        armor_set_header.setIconDrawable(AssetLoader.loadIconFor(armorSet))

        //Populate armor pieces section
        armor_set_piece_list.removeAllViews()
        armorSet.armor.forEach { armorFull -> populateArmorSetPieces(armorFull) }

        //Populate set bonuses
        armor_set_set_bonus_list.removeAllViews()
        populateSetBonuses(armorSet.armor.first().setBonuses)

        //Calculate total skills
        populateArmorSetSkills(armorSet.armor)

        //Calculate total stats
        armor_set_fire_value.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.fire }.toString()
        armor_set_water_value.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.water }.toString()
        armor_set_thunder_value.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.thunder }.toString()
        armor_set_ice_value.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.ice }.toString()
        armor_set_dragon_value.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.dragon }.toString()
        armor_set_defense_value.text = getString(R.string.armor_defense_value,
                armorSet.armor.sumBy { armorFull -> armorFull.armor.defense_base },
                armorSet.armor.sumBy { armorFull -> armorFull.armor.defense_max },
                armorSet.armor.sumBy { armorFull -> armorFull.armor.defense_augment_max })

        //Calculate total materials
        populateSetComponents(armorSet.armor)
    }

    private fun populateArmorSetPieces(armorFull: ArmorFull) {
        val view = layoutInflater.inflate(R.layout.listitem_armorset_armor, armor_set_piece_list, false)

        view.armor_icon.setImageDrawable(AssetLoader.loadIconFor(armorFull.armor))
        view.armor_name.text = armorFull.armor.name
        view.rarity_string.text = getString(R.string.format_rarity, armorFull.armor.rarity)
        view.defense_value.text = view.resources.getString(
                R.string.armor_defense_value,
                armorFull.armor.defense_base,
                armorFull.armor.defense_max,
                armorFull.armor.defense_augment_max)
        val slotImages = armorFull.armor.slots.map {
            view.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        view.slot1.setImageDrawable(slotImages[0])
        view.slot2.setImageDrawable(slotImages[1])
        view.slot3.setImageDrawable(slotImages[2])

        view.setOnClickListener {
            getRouter().navigateArmorDetail(armorFull.armor.id)
        }

        armor_set_piece_list.addView(view)
    }

    private fun populateSetBonuses(armorSetBonuses: List<ArmorSetBonus>) {
        if (armorSetBonuses.isEmpty()) {
            armor_set_set_bonus_section.visibility = View.GONE
            return
        }

        // show set bonus section
        armor_set_set_bonus_section.visibility = View.VISIBLE
        armor_set_set_bonus_list.removeAllViews()

        //Set the label for the Set name
        armor_set_set_bonus_name.text = armorSetBonuses.first().name

        //Now to set the actual skills
        for (setBonus in armorSetBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = layoutInflater.inflate(R.layout.listitem_armorset_bonus, null)

            listItem.bonus_skill_icon.setImageDrawable(skillIcon)
            listItem.bonus_skill_name.text = setBonus.skillTree.name
            listItem.bonus_requirement.setImageResource(reqIcon)

            listItem.setOnClickListener {
                getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            armor_set_set_bonus_list.addView(listItem)
        }
    }

    private fun populateSetComponents(armorSet: List<ArmorFull>) {
        //The armor set components need to be "added up" to create a cumulative list of components
        //Incoming item quantities are added to a map. If there is a repeated key, the existing ItemQuantity
        //object is replaced with an updated one because the models are immutable.
        val components = mutableMapOf<Int, ItemQuantity>()
        armor_set_components_list.removeAllViews()

        armorSet.forEach { armorFull ->
            armorFull.recipe.forEach { itemQuantity ->
                if (components.containsKey(itemQuantity.item.id)) {
                    val quantity = components[itemQuantity.item.id]!!.quantity + itemQuantity.quantity
                    components[itemQuantity.item.id] = ItemQuantity(itemQuantity.item, quantity, null)
                } else {
                    components[itemQuantity.item.id] = itemQuantity
                }
            }
        }

        //Iterate over the cumulative map instead of the individual ArmorFull objects
        components.forEach {
            val component = it.value
            val view = IconLabelTextCell(context)
            val icon = AssetLoader.loadIconFor(component.item)

            view.setLeftIconDrawable(icon)
            view.setLabelText(component.item.name)
            view.setValueText(getString(R.string.format_quantity_none, component.quantity))
            view.setOnClickListener {
                getRouter().navigateItemDetail(component.item.id)
            }

            armor_set_components_list.addView(view)
        }
    }

    private fun populateArmorSetSkills(armorSet: List<ArmorFull>) {
        //The armor skill levels need to be "added up" to create a cumulative list of skills
        //Incoming skill levels are added to a map. If there is a repeated key, the existing SkillLevel
        //object is replaced with an updated one because the models are immutable.
        val skills = mutableMapOf<Int, SkillLevel>()
        armor_set_skill_list.removeAllViews()

        armorSet.forEach { armorFull ->
            armorFull.skills.forEach { skill ->
                if (skills.containsKey(skill.skillTree.id)) {
                    val level = skills[skill.skillTree.id]!!.level + skill.level
                    val skillLevel = SkillLevel(level)
                    skillLevel.skillTree = skill.skillTree
                    skills[skill.skillTree.id] = skillLevel
                } else {
                    skills[skill.skillTree.id] = skill
                }
            }
        }

        //Iterate over the cumulative map instead of the individual ArmorFull objects
        skills.forEach {
            val skill = it.value
            //Set the label for the Set name
            val view = layoutInflater.inflate(R.layout.listitem_skill_level, armor_skill_list, false)

            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            view.label_text.text = skill.skillTree.name
            view.level_text.text = getString(R.string.level_qty, skill.level)
            with(view.skill_level) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            view.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            armor_set_skill_list.addView(view)
        }
    }
}