package com.gatheringhallstudios.mhworlddatabase.features.armor.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.*
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_armor_summary.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*

class ArmorDetailFragment : androidx.fragment.app.Fragment() {
    companion object {
        const val ARG_ARMOR_ID = "ARMOR"
    }

    private val viewModel: ArmorDetailViewModel by lazy {
        ViewModelProviders.of(this).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val armorId = arguments?.getInt(ARG_ARMOR_ID) ?: -1
        viewModel.loadArmor(armorId)

        return inflater.inflate(R.layout.fragment_armor_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.armor.observe(this, Observer(::populateArmor))
    }

    private fun populateArmor(armorData: ArmorFull?) {
        if (armorData == null) return

        setActivityTitle(armorData.armor.name)
        populateArmorBasic(armorData.armor)
        populateSkills(armorData.skills)
        populateSetBonuses(armorData.setBonuses)
        populateComponents(armorData.recipe)
    }

    private fun populateArmorBasic(armor: Armor) {
        // Set header info
        armor_header.setIconType(IconType.ZEMBELLISHED)
        armor_header.setIconDrawable(AssetLoader.loadIconFor(armor))
        armor_header.setTitleText(armor.name)
        armor_header.setSubtitleText(getString(R.string.format_rarity, armor.rarity))
        armor_header.setSubtitleColor(AssetLoader.loadRarityColor(armor.rarity))

        // set defense label
        defense_value.text = getString(
                R.string.armor_defense_value,
                armor.defense_base,
                armor.defense_max,
                armor.defense_augment_max)

        // set elemental defense values
        fire_value.text = "${armor.fire}"
        water_value.text = "${armor.water}"
        thunder_value.text = "${armor.thunder}"
        ice_value.text = "${armor.ice}"
        dragon_value.text = "${armor.dragon}"

        val slotImages = armor.slots.map {
            context?.getDrawableCompat(SlotEmptyRegistry(it))
        }

        slot1.setImageDrawable(slotImages[0])
        slot2.setImageDrawable(slotImages[1])
        slot3.setImageDrawable(slotImages[2])
    }

    private fun populateSkills(skills: List<SkillLevel>) {
        if (skills.isEmpty()) {
            armor_skill_section.visibility = View.GONE
            return
        }

        armor_skill_section.visibility = View.VISIBLE
        armor_skill_list.removeAllViews()

        val inflater = LayoutInflater.from(context)

        for (skill in skills) {
            //Set the label for the Set name
            val view = inflater.inflate(R.layout.listitem_skill_level, armor_skill_list, false)

            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            view.label_text.text = skill.skillTree.name
            view.level_text.text = getString(R.string.skill_level_qty, skill.level)
            with(view.skill_level) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            view.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            armor_skill_list.addView(view)
        }
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
        set_bonus_name.text = armorSetBonuses.first().name

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

            armor_set_bonus_list.addView(listItem)
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
            val icon = AssetLoader.loadIconFor(itemQuantity.item)

            view.setLeftIconDrawable(icon)
            view.setLabelText(itemQuantity.item.name)
            view.setValueText(getString(R.string.format_quantity_none, itemQuantity.quantity))
            view.setOnClickListener {
                getRouter().navigateItemDetail(itemQuantity.item.id)
            }

            armor_components_list.addView(view)
        }
    }
}