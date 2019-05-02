package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.CompactStatCell
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.android.synthetic.main.cell_icon_label_text.view.*
import kotlinx.android.synthetic.main.fragment_user_equipment_set_editor.*
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.defense_value
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.armor_icon
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.armor_name
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.rarity_string
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.slot_section
import kotlinx.android.synthetic.main.listitem_universal_simple.view.label_text
import kotlinx.android.synthetic.main.listitem_universal_simple.view.icon

import kotlinx.android.synthetic.main.listitem_weapon.view.*

class UserEquipmentSetEditFragment : androidx.fragment.app.Fragment() {
    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: UserEquipmentSetDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(UserEquipmentSetDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_equipment_set_editor, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.activeUserEquipmentSet.observe(this, Observer<UserEquipmentSet> {
            populateUserEquipment(it, AppSettings.showTrueAttackValues)
        })
    }

    private fun populateUserEquipment(userEquipmentSet: UserEquipmentSet, showTrueAttackValues: Boolean) {
        userEquipmentSet.equipment.forEach {
            when (it.getType()) {
                DataType.WEAPON -> {
                    populateWeapon(it as UserWeapon, showTrueAttackValues)
                }
                DataType.ARMOR -> {
                    populateArmor(it as UserArmorPiece)
                }
                DataType.CHARM -> {
                    populateCharm(it as UserCharm)
                }
                else -> {
                } //Skip
            }
        }
    }

    private fun populateArmor(userArmor: UserArmorPiece) {
        val armor = userArmor.armor
        when (armor.armor.armor_type) {
            ArmorType.HEAD -> {
                user_equipment_head_slot.armor_name.text = armor.armor.name
                user_equipment_head_slot.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
                user_equipment_head_slot.armor_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
                user_equipment_head_slot.defense_value.text = getString(
                        R.string.armor_defense_value,
                        armor.armor.defense_base,
                        armor.armor.defense_max,
                        armor.armor.defense_augment_max)
            }
            ArmorType.CHEST -> {
                user_equipment_chest_slot.armor_name.text = armor.armor.name
                user_equipment_chest_slot.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
                user_equipment_chest_slot.armor_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
                user_equipment_chest_slot.defense_value.text = getString(
                        R.string.armor_defense_value,
                        armor.armor.defense_base,
                        armor.armor.defense_max,
                        armor.armor.defense_augment_max)
            }
            ArmorType.ARMS -> {
                user_equipment_arms_slot.armor_name.text = armor.armor.name
                user_equipment_arms_slot.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
                user_equipment_arms_slot.armor_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
                user_equipment_arms_slot.defense_value.text = getString(
                        R.string.armor_defense_value,
                        armor.armor.defense_base,
                        armor.armor.defense_max,
                        armor.armor.defense_augment_max)
            }
            ArmorType.WAIST -> {
                user_equipment_waist_slot.armor_name.text = armor.armor.name
                user_equipment_waist_slot.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
                user_equipment_waist_slot.armor_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
                user_equipment_waist_slot.defense_value.text = getString(
                        R.string.armor_defense_value,
                        armor.armor.defense_base,
                        armor.armor.defense_max,
                        armor.armor.defense_augment_max)
            }
            ArmorType.LEGS -> {
                user_equipment_legs_slot.armor_name.text = armor.armor.name
                user_equipment_legs_slot.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
                user_equipment_legs_slot.armor_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
                user_equipment_legs_slot.defense_value.text = getString(
                        R.string.armor_defense_value,
                        armor.armor.defense_base,
                        armor.armor.defense_max,
                        armor.armor.defense_augment_max)
            }
        }
    }

    private fun populateCharm(userCharm: UserCharm) {
        user_equipment_charm_slot.label_text.text = userCharm.charm.charm.name
        user_equipment_charm_slot.icon.setImageDrawable(AssetLoader.loadIconFor(userCharm.charm.charm))
    }

    private fun populateWeapon(userWeapon: UserWeapon, showTrueAttackValues: Boolean) {
        val weapon = userWeapon.weapon
        user_equipment_weapon_slot.weapon_name.text = weapon.weapon.name
        user_equipment_weapon_slot.weapon_image.setImageDrawable(AssetLoader.loadIconFor(weapon.weapon))
        user_equipment_weapon_slot.weapon_craftable_image.visibility = when {
            weapon.weapon.craftable -> View.VISIBLE
            else -> View.GONE
        }

        // Populate static stats like attack, affinity...
        populateStaticStats(weapon.weapon, user_equipment_weapon_slot, showTrueAttackValues)
        // Populate decorationIds
//        populateDecorations(weapon.weapon, view)
        // Populate stats like element, defense...
        populateComplexStats(weapon.weapon, user_equipment_weapon_slot)
    }

    private fun populateComplexStats(weapon: Weapon, weaponView: View) {
        weaponView.complex_stat_layout.removeAllViews()
        // Elemental Stat (added if there's a value)
        if (weapon.element1 != null) {
            val elementView = CompactStatCell(
                    context,
                    AssetLoader.loadElementIcon(weapon.element1),
                    createElementString(weapon.element1_attack, weapon.element_hidden))

            if (weapon.element_hidden) {
                elementView.labelView.alpha = 0.5.toFloat()
            } else {
                elementView.labelView.alpha = 1.0.toFloat()
            }

            weaponView.complex_stat_layout.addView(elementView)
        }

        // Affinity (added if there's a value)
        if (weapon.affinity != 0) {
            val affinityValue = getString(R.string.format_plus_percentage, weapon.affinity)

            val affinityView = CompactStatCell(
                    context,
                    R.drawable.ic_ui_affinity,
                    affinityValue)

            affinityView.labelView.setTextColor(ContextCompat.getColor(context!!, when {
                weapon.affinity > 0 -> R.color.textColorGreen
                else -> R.color.textColorRed
            }))

            weaponView.complex_stat_layout.addView(affinityView)
        }

        // Defense, added if there's a value
        if (weapon.defense != 0) {
            val defenseValue = getString(R.string.format_plus, weapon.defense)
            val defenseView = CompactStatCell(
                    context,
                    R.drawable.ic_ui_defense,
                    defenseValue
            )

            defenseView.labelView.setTextColor(ContextCompat.getColor(context!!, when {
                weapon.defense > 0 -> R.color.textColorGreen
                else -> R.color.textColorRed
            }))

            weaponView.complex_stat_layout.addView(defenseView)
        }
    }

    private fun populateStaticStats(weapon: Weapon, view: View, showTrueAttackValues: Boolean) {
        view.attack_value.setLabelText(
                if (showTrueAttackValues) weapon.attack_true.toString()
                else weapon.attack.toString())

        //Render sharpness data if it exists, else hide the bars
        val sharpnessData = weapon.sharpnessData
        if (sharpnessData != null) {
            view.sharpness_container.visibility = View.VISIBLE
            view.sharpness_value.drawSharpness(sharpnessData.min)
            view.sharpness_max_value.drawSharpness(sharpnessData.max)
        } else {
            view.sharpness_container.visibility = View.GONE
        }
    }

    private fun createElementString(element1_attack: Int?, element_hidden: Boolean): String {
        val workString = element1_attack ?: "-----"

        return when (element_hidden) {
            true -> "($workString)"
            false -> workString.toString()
        }
    }

}