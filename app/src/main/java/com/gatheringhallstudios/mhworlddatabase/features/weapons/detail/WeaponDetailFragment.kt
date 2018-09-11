package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.*
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_armor_summary.*

class WeaponDetailFragment : Fragment() {
    companion object {
        const val ARG_WEAPON_ID = "WEAPON"
    }

    private val viewModel: WeaponDetailViewModel by lazy {
        ViewModelProviders.of(this).get(WeaponDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val armorId = arguments?.getInt(ARG_WEAPON_ID) ?: -1

        return inflater.inflate(R.layout.fragment_weapon_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        viewModel.armor.observe(this, Observer(::populateArmor))
    }

//    private fun populateArmor(armorData: ArmorFull?) {
//        if (armorData == null) return
//
//        setActivityTitle(armorData.armor.name)
//        populateArmorBasic(armorData.armor)
//        populateSkills(armorData.skills)
//        populateSetBonuses(armorData.setBonuses)
//        populateComponents(armorData.recipe)
//    }

//    private fun populateArmorBasic(armor: Armor) {
//        // Set header info
//        armor_header.setIconType(IconType.ZEMBELLISHED)
//        armor_header.setIconDrawable(AssetLoader.loadIconFor(armor))
//        armor_header.setTitleText(armor.name)
//        armor_header.setSubtitleText(getString(R.string.format_rarity, armor.rarity))
//        armor_header.setSubtitleColor(AssetLoader.loadRarityColor(armor.rarity))
//
//        // set defense label
//        defense_value.text = getString(
//                R.string.armor_defense_value,
//                armor.defense_base,
//                armor.defense_max,
//                armor.defense_augment_max)
//
//        // set elemental defense values
//        fire_value.text = "${armor.fire}"
//        water_value.text = "${armor.water}"
//        thunder_value.text = "${armor.thunder}"
//        ice_value.text = "${armor.ice}"
//        dragon_value.text = "${armor.dragon}"
//
//        val slotImages = armor.slots.map {
//            context?.getDrawableCompat(SlotEmptyRegistry(it))
//        }
//
//        slot1.setImageDrawable(slotImages[0])
//        slot2.setImageDrawable(slotImages[1])
//        slot3.setImageDrawable(slotImages[2])
//    }

//    private fun populateSkills(skills: List<SkillLevel>) {
//        if (skills.isEmpty()) {
//            armor_skill_section.visibility = View.GONE
//            return
//        }
//
//        armor_skill_section.visibility = View.VISIBLE
//        armor_skill_list.removeAllViews()
//
//        val inflater = LayoutInflater.from(context)
//
//        for (skill in skills) {
//            //Set the label for the Set name
//            val view = inflater.inflate(R.layout.listitem_skill_level, armor_skill_list, false)
//
//            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
//            view.label_text.text = skill.skillTree.name
//            view.level_text.text = getString(R.string.skill_level_qty, skill.level)
//            with(view.skill_level) {
//                maxLevel = skill.skillTree.max_level
//                level = skill.level
//            }
//
//            view.setOnClickListener {
//                getRouter().navigateSkillDetail(skill.skillTree.id)
//            }
//
//            armor_skill_list.addView(view)
//        }
//    }


//    private fun populateComponents(components: List<ItemQuantity>) {
//        if (components.isEmpty()) {
//            armor_components_section.visibility = View.GONE
//            return
//        }
//
//        armor_components_section.visibility = View.VISIBLE
//        armor_components_list.removeAllViews()
//
//        for (itemQuantity in components) {
//            val view = IconLabelTextCell(context)
//            val icon = AssetLoader.loadIconFor(itemQuantity.item)
//
//            view.setLeftIconDrawable(icon)
//            view.setLabelText(itemQuantity.item.name)
//            view.setValueText(getString(R.string.format_quantity_none, itemQuantity.quantity))
//            view.setOnClickListener {
//                getRouter().navigateItemDetail(itemQuantity.item.id)
//            }
//
//            armor_components_list.addView(view)
//        }
//    }
}