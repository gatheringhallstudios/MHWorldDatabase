package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.CompactStatCell
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_user_equipment_set_summary.*
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.*
import kotlinx.android.synthetic.main.listitem_armorset_armor.view.defense_value
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.listitem_weapon.view.*
import kotlinx.android.synthetic.main.listitem_weapon.view.slot1
import kotlinx.android.synthetic.main.listitem_weapon.view.slot2
import kotlinx.android.synthetic.main.listitem_weapon.view.slot3

class UserEquipmentSetSummaryFragment : androidx.fragment.app.Fragment() {

    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: UserEquipmentSetDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(UserEquipmentSetDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_equipment_set_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.activeUserEquipmentSet.observe(this, Observer<UserEquipmentSet> {
            populateUserEquipmentSummary(it, AppSettings.showTrueAttackValues)
        })
    }

    private fun populateUserEquipmentSummary(userEquipmentSet: UserEquipmentSet, showTrueAttackValues: Boolean) {
        armor_set_piece_list.removeAllViews()
        armor_set_skill_list.removeAllViews()
        armor_set_set_bonus_list.removeAllViews()
        weapon_list.removeAllViews()

        //Load the icon of the first piece of equipment or no icon if the set is empty
        if (userEquipmentSet.equipment.isNotEmpty()) {
            armor_set_header.setIconDrawable(getIconObject(userEquipmentSet.equipment))
        }

        armor_set_header.setTitleText(userEquipmentSet.name)
        armor_set_defense_value.text = getString(
                R.string.armor_defense_value,
                userEquipmentSet.defense_base,
                userEquipmentSet.defense_max,
                userEquipmentSet.defense_augment_max)

        armor_set_fire_value.text = userEquipmentSet.fireDefense.toString()
        armor_set_water_value.text = userEquipmentSet.waterDefense.toString()
        armor_set_thunder_value.text = userEquipmentSet.thunderDefense.toString()
        armor_set_ice_value.text = userEquipmentSet.iceDefense.toString()
        armor_set_dragon_value.text = userEquipmentSet.dragonDefense.toString()

        userEquipmentSet.equipment.filter { it.type() == DataType.WEAPON }.forEach { populateWeapon(it as UserWeapon, showTrueAttackValues) }
        userEquipmentSet.equipment.filter { it.type() == DataType.ARMOR }.forEach { populateArmorSetPieces(it as UserArmorPiece) }
        userEquipmentSet.skills.forEach { populateArmorSkills(it.value) }
        userEquipmentSet.setBonuses.forEach {
            populateArmorSetBonusName(it.key)
            populateArmorSetBonuses(it.value)
        }
    }

    private fun populateWeapon(userWeapon: UserWeapon, showTrueAttackValues: Boolean) {
        val weapon = userWeapon.weapon
        val view = layoutInflater.inflate(R.layout.listitem_weapon, weapon_list, false)

        view.weapon_name.text = weapon.weapon.name
        view.weapon_image.setImageDrawable(AssetLoader.loadIconFor(weapon.weapon))
        view.weapon_craftable_image.visibility = when {
            weapon.weapon.craftable -> View.VISIBLE
            else -> View.GONE
        }

        // Populate static stats like attack, affinity...
        populateStaticStats(weapon.weapon, view, showTrueAttackValues)
        // Populate decorationIds
        populateDecorations(weapon.weapon, view)
        // Populate stats like element, defense...
        populateComplexStats(weapon.weapon, view)

        weapon_list.addView(view)
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

    private fun populateDecorations(weapon: Weapon, weaponView: View) {
        val slotImages = weapon.slots.map {
            weaponView.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        weaponView.slot1.setImageDrawable(slotImages[0])
        weaponView.slot2.setImageDrawable(slotImages[1])
        weaponView.slot3.setImageDrawable(slotImages[2])

        // Hide views if no slots
        weaponView.slot1.visibility = when (weapon.slots[0]) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
        weaponView.slot2.visibility = when (weapon.slots[1]) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
        weaponView.slot3.visibility = when (weapon.slots[2]) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun populateArmorSetPieces(armorPiece: UserArmorPiece) {
        val armorFull = armorPiece.armor
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

    private fun populateArmorSkills(skill: SkillLevel) {
        //Set the label for the Set name
        val view = layoutInflater.inflate(R.layout.listitem_skill_level, armor_skill_section, false)
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

        armor_set_skill_list.addView(view)
    }

    private fun populateArmorSetBonusName(setBonusName: String) {
        val textView = TextView(context)
        textView.text = setBonusName
        textView.setTextAppearance(context, R.style.TextHeadlineHigh)
        armor_set_set_bonus_list.addView(textView)
    }

    private fun populateArmorSetBonuses(setBonuses: List<ArmorSetBonus>) {
        for (setBonus in setBonuses) {
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

    private fun createElementString(element1_attack: Int?, element_hidden: Boolean): String {
        val workString = element1_attack ?: "-----"

        return when (element_hidden) {
            true -> "($workString)"
            false -> workString.toString()
        }
    }

    private fun getIconObject(equipment: MutableList<UserEquipment>): Drawable? {
        val item = equipment.first()
        return when (item.type()) {
            DataType.WEAPON -> AssetLoader.loadIconFor((item as UserWeapon).weapon.weapon)
            DataType.ARMOR -> AssetLoader.loadIconFor((item as UserArmorPiece).armor.armor)
            DataType.CHARM -> AssetLoader.loadIconFor((item as UserCharm).charm.charm)
            else -> null
        }
    }
}