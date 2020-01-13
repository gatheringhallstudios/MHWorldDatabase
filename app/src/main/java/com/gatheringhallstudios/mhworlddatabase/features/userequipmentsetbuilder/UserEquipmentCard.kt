package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.level_text
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.view_base_body_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_empty_equipment_header_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.equipment_icon
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.equipment_name
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.rarity_string
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.icon_slots
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot1
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot2
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot3

/**
 * Wrapper over the ExpandableCardView used to display equipment data.
 * Used to
 */
class UserEquipmentCard(private val card: ExpandableCardView) {
    private fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return card.resources.getString(resId, *formatArgs)
    }

    fun bindWeapon(userWeapon: UserWeapon?) {
        if (userWeapon != null) {
            val weapon = userWeapon.weapon.weapon
            with(card) {
                setHeader(R.layout.view_weapon_header_expandable_cardview)
                setBody(R.layout.view_base_body_expandable_cardview)

                equipment_name.text = weapon.name
                equipment_icon.setImageDrawable(AssetLoader.loadIconFor(weapon))
                rarity_string.setTextColor(AssetLoader.loadRarityColor(weapon.rarity))
                rarity_string.text = getString(R.string.format_rarity, weapon.rarity)
                rarity_string.visibility = View.VISIBLE
                attack_value.text = weapon.attack.toString()

                decorations_section.visibility = View.GONE
            }

            val skillsList = combineEquipmentSkillsWithDecorationSkills(userWeapon.weapon.skills, userWeapon.decorations.map {
                val skillLevel = SkillLevel(level = 1)
                skillLevel.skillTree = it.decoration.skillTree
                skillLevel
            })

            populateSkills(skillsList)
            populateSetBonuses(emptyList())
        } else {
            bindEmptyWeapon()
        }
    }

    fun bindWeapon(userWeapon: UserWeapon?, setId: Int) {
        if (userWeapon != null) {
            val weapon = userWeapon.weapon.weapon
            with(card) {
                setHeader(R.layout.view_weapon_header_expandable_cardview)
                setBody(R.layout.view_base_body_expandable_cardview)

                equipment_name.text = weapon.name
                equipment_icon.setImageDrawable(AssetLoader.loadIconFor(weapon))
                rarity_string.setTextColor(AssetLoader.loadRarityColor(weapon.rarity))
                rarity_string.text = getString(R.string.format_rarity, weapon.rarity)
                rarity_string.visibility = View.VISIBLE
                attack_value.text = weapon.attack.toString()

                decorations_section.visibility = View.GONE
            }

            val skillsList = combineEquipmentSkillsWithDecorationSkills(userWeapon.weapon.skills, userWeapon.decorations.map {
                val skillLevel = SkillLevel(level = 1)
                skillLevel.skillTree = it.decoration.skillTree
                skillLevel
            })

            populateSkills(skillsList)
            populateSetBonuses(emptyList())
        } else {
            with(card) {
                bindEmptyWeapon()
                setOnClick {
                    getRouter().navigateUserEquipmentPieceSelector(UserEquipmentSetSelectorListFragment.Companion.SelectorMode.WEAPON,
                            null, setId, null, null)
                }
            }
        }
    }

    fun bindHeadArmor(userArmor: UserArmorPiece?) {
        bindArmor(userArmor, ArmorType.HEAD)
    }

    fun bindHeadArmor(userArmor: UserArmorPiece?, setId: Int) {
        bindArmor(userArmor, ArmorType.HEAD, setId)
    }

    fun bindArmArmor(userArmor: UserArmorPiece?) {
        bindArmor(userArmor, ArmorType.ARMS)
    }

    fun bindArmArmor(userArmor: UserArmorPiece?, setId: Int) {
        bindArmor(userArmor, ArmorType.ARMS, setId)
    }

    fun bindChestArmor(userArmor: UserArmorPiece?) {
        bindArmor(userArmor, ArmorType.CHEST)
    }

    fun bindChestArmor(userArmor: UserArmorPiece?, setId: Int) {
        bindArmor(userArmor, ArmorType.CHEST, setId)
    }

    fun bindLegArmor(userArmor: UserArmorPiece?) {
        bindArmor(userArmor, ArmorType.LEGS)
    }

    fun bindLegArmor(userArmor: UserArmorPiece?, setId: Int) {
        bindArmor(userArmor, ArmorType.LEGS, setId)
    }

    fun bindWaistArmor(userArmor: UserArmorPiece?) {
        bindArmor(userArmor, ArmorType.WAIST)
    }

    fun bindWaistArmor(userArmor: UserArmorPiece?, setId: Int) {
        bindArmor(userArmor, ArmorType.WAIST, setId)
    }

    fun bindCharm(userCharm: UserCharm?) {
        if (userCharm != null) {
            with(card) {
                setHeader(R.layout.view_base_header_expandable_cardview)
                setBody(R.layout.view_base_body_expandable_cardview)

                equipment_name.text = userCharm.charm.charm.name
                equipment_icon.setImageDrawable(AssetLoader.loadIconFor(userCharm.charm.charm))
                rarity_string.text = getString(R.string.format_rarity, userCharm.charm.charm.rarity)
                rarity_string.setTextColor(AssetLoader.loadRarityColor(userCharm.charm.charm.rarity))
                rarity_string.visibility = View.VISIBLE

                defense_value.visibility = View.GONE
                icon_defense.visibility = View.GONE
                hideSlots()
                decorations_section.visibility = View.GONE
            }

            populateSkills(userCharm.charm.skills)
            populateSetBonuses(emptyList())
        } else {
            bindEmptyCharm()
        }
    }

    fun bindCharm(userCharm: UserCharm?, setId: Int) {
        if (userCharm != null) {
            card.run {
                setHeader(R.layout.view_base_header_expandable_cardview)
                setBody(R.layout.view_base_body_expandable_cardview)

                equipment_name.text = userCharm.charm.charm.name
                equipment_icon.setImageDrawable(AssetLoader.loadIconFor(userCharm.charm.charm))
                rarity_string.text = getString(R.string.format_rarity, userCharm.charm.charm.rarity)
                rarity_string.setTextColor(AssetLoader.loadRarityColor(userCharm.charm.charm.rarity))
                rarity_string.visibility = View.VISIBLE

                defense_value.visibility = View.GONE
                icon_defense.visibility = View.GONE
                hideSlots()
                decorations_section.visibility = View.GONE
            }
            populateSkills(userCharm.charm.skills)
            populateSetBonuses(emptyList())
        } else {
            bindEmptyCharm()
            with(card) {
                setOnClick {
                    getRouter().navigateUserEquipmentPieceSelector(UserEquipmentSetSelectorListFragment.Companion.SelectorMode.CHARM,
                            null, setId, null, null)
                }
            }
        }
    }

    fun bindDecoration(userDecoration: UserDecoration) {
        val decoration = userDecoration.decoration

        with(card) {
            setHeader(R.layout.view_base_header_expandable_cardview)
            setBody(R.layout.view_base_body_expandable_cardview)
            equipment_name.text = decoration.name
            equipment_icon.setImageDrawable(AssetLoader.loadIconFor(decoration))
            rarity_string.text = getString(R.string.format_rarity, decoration.rarity)
            rarity_string.setTextColor(AssetLoader.loadRarityColor(decoration.rarity))
            rarity_string.visibility = View.VISIBLE

            defense_value.visibility = View.GONE
            icon_defense.visibility = View.GONE
        }
    }

    private fun bindArmor(userArmor: UserArmorPiece?, armorType: ArmorType, setId: Int? = null) {
        if (userArmor != null) {
            val armor = userArmor.armor
            with(card) {
                setHeader(R.layout.view_base_header_expandable_cardview)
                setBody(R.layout.view_base_body_expandable_cardview)
                equipment_name.text = armor.armor.name
                rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
                rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.armor.rarity))
                rarity_string.visibility = View.VISIBLE
                equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
                defense_value.text = getString(
                        R.string.armor_defense_value,
                        armor.armor.defense_base,
                        armor.armor.defense_max,
                        armor.armor.defense_augment_max)

                decorations_section.visibility = View.GONE
                populateSetBonuses(armor.setBonuses)

                val skillsList = combineEquipmentSkillsWithDecorationSkills(armor.skills, userArmor.decorations.map {
                    val skillLevel = SkillLevel(level = 1)
                    skillLevel.skillTree = it.decoration.skillTree
                    skillLevel
                })

                populateSkills(skillsList)
            }
        } else {
            with(card) {
                bindEmptyArmor(armorType)
                setOnClick {
                    getRouter().navigateUserEquipmentPieceSelector(UserEquipmentSetSelectorListFragment.Companion.SelectorMode.ARMOR,
                            null, setId, armorType, null)
                }
            }
        }
    }

    private fun setEmptyView(@StringRes title: Int, @DrawableRes icon: Int) {
        card.run {
            setHeader(R.layout.view_empty_equipment_header_expandable_cardview)
            setBody(R.layout.view_empty_equipment_body_expandable_cardview)
        }
        card.run {
            new_equipment_set_label.text = getString(title)
            equipment_set_icon2.setImageResource(icon)
        }
    }

    private fun hideSlots() {
        card.run {
            icon_slots.visibility = View.GONE
            slot1.visibility = View.GONE
            slot2.visibility = View.GONE
            slot3.visibility = View.GONE
        }
    }

    fun bindEmptyWeapon() {
        setEmptyView(R.string.user_equipment_set_no_weapon, R.drawable.ic_equipment_weapon_empty)
    }

    fun bindEmptyArmor(type: ArmorType?) {
        setEmptyView(R.string.user_equipment_set_no_armor, when (type) {
            ArmorType.HEAD -> R.drawable.ic_equipment_head_empty
            ArmorType.CHEST -> R.drawable.ic_equipment_chest_empty
            ArmorType.ARMS -> R.drawable.ic_equipment_arm_empty
            ArmorType.WAIST -> R.drawable.ic_equipment_waist_empty
            ArmorType.LEGS -> R.drawable.ic_equipment_leg_empty
            else -> R.drawable.ic_equipment_armor_set_empty
        })
    }

    fun bindEmptyCharm() {
        setEmptyView(R.string.user_equipment_set_no_charm, R.drawable.ic_equipment_armor_set_empty)
    }

    fun bindEmptyDecoration(slotSize: Int) {
        setEmptyView(R.string.user_equipment_set_no_decoration, when (slotSize) {
            1 -> R.drawable.ic_ui_slot_1_empty
            2 -> R.drawable.ic_ui_slot_2_empty
            3 -> R.drawable.ic_ui_slot_3_empty
            4 -> R.drawable.ic_ui_slot_4_empty
            else -> R.drawable.ic_ui_slot_none
        })
    }

    fun setOnClick(onClick: () -> Unit) {
        card.setOnClick(onClick)
    }

    fun setOnSwipeRight(onSwipeRight: () -> Unit) {
        card.setOnSwipeRight(onSwipeRight)
    }

    fun setOnSwipeLeft(onSwipeLeft: () -> Unit) {
        card.setOnSwipeLeft(onSwipeLeft)
    }

    fun populateSkills(skills: List<SkillLevel>) {
        with(card.skill_section) {
            skill_list.removeAllViews()
            visibility = if (skills.isEmpty()) View.GONE else View.VISIBLE
        }

        val inflater = LayoutInflater.from(card.context)
        for (skill in skills) {
            //Set the label for the Set name
            val view = inflater.inflate(R.layout.listitem_skill_level, card.skill_list, false)

            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            view.label_text.text = skill.skillTree.name
            view.level_text.text = getString(R.string.skill_level_qty, skill.level)
            with(view.skill_level) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            view.setOnClickListener {
                card.getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            card.skill_list.addView(view)
        }
    }

    fun populateSetBonuses(setBonuses: List<ArmorSetBonus>) {
        with(card.set_bonus_section) {
            set_bonus_list.removeAllViews()
            visibility = if (setBonuses.isEmpty()) View.GONE else View.VISIBLE
        }

        val inflater = LayoutInflater.from(card.context)

        //Now to set the actual skills
        for (setBonus in setBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = inflater.inflate(R.layout.listitem_armorset_bonus, card.set_bonus_list, false)

            listItem.bonus_skill_icon.setImageDrawable(skillIcon)
            listItem.bonus_skill_name.text = setBonus.skillTree.name
            listItem.bonus_requirement.setImageResource(reqIcon)

            listItem.setOnClickListener {
                card.getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            card.set_bonus_list.addView(listItem)
        }
    }

    /**
     * Populates the slot icons, but hides the decorations section.
     * If you want decorations to be selectable, use populateDecorations instead.
     */
    fun populateSlots(slots: EquipmentSlots) {
        this.populateDecorations(slots, emptyList())
        card.decorations_section.visibility = View.GONE
    }

    /**
     * Populates the decoration section for most equipment pieces.
     * For each callback, it will either return the UserDecoration that was clicked, or
     * the slot number (1-indexed) of the clicked slot.
     */
    fun populateDecorations(slots: EquipmentSlots, decorations: List<UserDecoration>,
                            onEmptyClick: ((Int) -> Unit)? = null,
                            onClick: ((Int, UserDecoration) -> Unit)? = null,
                            onDelete: ((UserDecoration) -> Unit)? = null) {
        with(card.decorations_section) {
            visibility = if (slots.isEmpty()) View.GONE else View.VISIBLE
            slot1_detail.visibility = View.GONE
            slot2_detail.visibility = View.GONE
            slot3_detail.visibility = View.GONE
        }

        // Bind decorations that exist first
        slots.active.forEachIndexed { idx, slotSize ->
            val slotNumber = idx + 1
            val userDecoration = decorations.find { it.slotNumber == slotNumber }
            val decoration = userDecoration?.decoration

            val imageView = when (slotNumber) {
                1 -> card.slot1
                2 -> card.slot2
                3 -> card.slot3
                else -> throw IndexOutOfBoundsException("SlotIdx is out of range 1-3: $slotNumber")
            }

            val detailView = when (slotNumber) {
                1 -> card.slot1_detail
                2 -> card.slot2_detail
                3 -> card.slot3_detail
                else -> throw IndexOutOfBoundsException("SlotIdx is out of range 1-3: $slotNumber")
            }

            detailView.visibility = View.VISIBLE
            detailView.removeDecorator()

            if (decoration != null) {
                imageView.setImageDrawable(AssetLoader.loadFilledSlotIcon(decoration, slotSize))
                detailView.setLabelText(decoration.name)
                detailView.setLeftIconDrawable(AssetLoader.loadFilledSlotIcon(decoration, slotSize))

                detailView.setOnClickListener {
                    onClick?.invoke(slotNumber, userDecoration)
                }

                detailView.setButtonClickFunction {
                    onDelete?.invoke(userDecoration)
                }
            } else {
                imageView.setImageDrawable(card.context!!.getDrawableCompat(SlotEmptyRegistry(slotSize)))
                detailView.setLeftIconDrawable(card.context!!.getDrawableCompat(SlotEmptyRegistry(slotSize)))
                detailView.setLabelText(getString(R.string.user_equipment_set_no_decoration))

                detailView.setOnClickListener {
                    onEmptyClick?.invoke(slotNumber)
                }
            }
        }
    }

    private fun combineEquipmentSkillsWithDecorationSkills(equipmentSkills: List<SkillLevel>, decorationSkills: List<SkillLevel>): List<SkillLevel> {
        val skills = equipmentSkills.associateBy({ it.skillTree.id }, { it }).toMutableMap()
        for (skill in decorationSkills) {
            if (skills.containsKey(skill.skillTree.id)) {
                val level = skills.getValue(skill.skillTree.id).level + skill.level
                val skillLevel = SkillLevel(level)
                skillLevel.skillTree = skill.skillTree
                skills[skill.skillTree.id] = skillLevel
            } else {
                skills[skill.skillTree.id] = skill
            }
        }
        val result = skills.values.toMutableList()
        result.sortWith(compareByDescending<SkillLevel> { it.level }.thenBy { it.skillTree.id })
        return result
    }
}