package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list.UserEquipmentSetListViewModel
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment.Companion
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.view.icon
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.view.label_text
import kotlinx.android.synthetic.main.fragment_user_equipment_set_editor.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.level_text
import kotlinx.android.synthetic.main.listitem_skill_level.view.*

class UserEquipmentSetEditFragment : androidx.fragment.app.Fragment() {
    private var isNewFragment = true

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
            populateDefaults(it.id)
            populateUserEquipment(it)
        })
    }

    override fun onResume() {
        super.onResume()
        //Try to avoid stale check on first round
//        if (!isNewFragment && viewModel.isActiveUserEquipmentSetStale()) {
        if (!isNewFragment) {
            val buffer = ViewModelProviders.of(activity!!).get(UserEquipmentSetListViewModel::class.java)
            viewModel.activeUserEquipmentSet.value = buffer.getEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
        }

        isNewFragment = false
    }

    private fun populateDefaults(userEquipmentSetId: Int) {
        user_equipment_head_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.HEAD, null)
        }
        populateSkills(emptyList(), user_equipment_head_slot.skill_section)
        populateSetBonuses(emptyList(), user_equipment_head_slot.set_bonus_section)

        user_equipment_chest_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.CHEST, null)
        }
        populateSkills(emptyList(), user_equipment_chest_slot.skill_section)
        populateSetBonuses(emptyList(), user_equipment_chest_slot.set_bonus_section)

        user_equipment_arms_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.ARMS, null)
        }
        populateSkills(emptyList(), user_equipment_arms_slot.skill_section)
        populateSetBonuses(emptyList(), user_equipment_arms_slot.set_bonus_section)

        user_equipment_waist_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.WAIST, null)
        }
        populateSkills(emptyList(), user_equipment_waist_slot.skill_section)
        populateSetBonuses(emptyList(), user_equipment_waist_slot.set_bonus_section)

        user_equipment_legs_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.LEGS, null)
        }
        populateSkills(emptyList(), user_equipment_legs_slot.skill_section)
        populateSetBonuses(emptyList(), user_equipment_legs_slot.set_bonus_section)

        user_equipment_charm_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.CHARM, null, userEquipmentSetId, null, null)
        }
        populateSkills(emptyList(), user_equipment_legs_slot.skill_section)
        populateSetBonuses(emptyList(), user_equipment_legs_slot.set_bonus_section)
    }

    private fun attachOnClickListeners(armorPiece: UserArmorPiece, userEquipmentSetId: Int) {
//        user_equipment_weapon_slot.card_arrow.setOnClick {
//            toggle(user_equipment_weapon_slot)
//        }
        val armor = armorPiece.armor.armor
        when (armor.armor_type) {
            ArmorType.HEAD -> {
                user_equipment_head_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, armorPiece, userEquipmentSetId, ArmorType.HEAD, null)
                }
                attachDecorationOnClickListeners(armorPiece, armorPiece.decorations, user_equipment_head_slot.decorations_section, userEquipmentSetId)
            }
            ArmorType.CHEST -> {
                user_equipment_chest_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, armorPiece, userEquipmentSetId, ArmorType.CHEST, null)
                }
                attachDecorationOnClickListeners(armorPiece, armorPiece.decorations, user_equipment_chest_slot.decorations_section, userEquipmentSetId)
            }
            ArmorType.ARMS -> {
                user_equipment_arms_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, armorPiece, userEquipmentSetId, ArmorType.ARMS, null)
                }
                attachDecorationOnClickListeners(armorPiece, armorPiece.decorations, user_equipment_arms_slot.decorations_section, userEquipmentSetId)
            }
            ArmorType.WAIST -> {
                user_equipment_waist_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, armorPiece, userEquipmentSetId, ArmorType.WAIST, null)
                }
                attachDecorationOnClickListeners(armorPiece, armorPiece.decorations, user_equipment_waist_slot.decorations_section, userEquipmentSetId)
            }
            ArmorType.LEGS -> {
                user_equipment_legs_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, armorPiece, userEquipmentSetId, ArmorType.LEGS, null)
                }
                attachDecorationOnClickListeners(armorPiece, armorPiece.decorations, user_equipment_legs_slot.decorations_section, userEquipmentSetId)
            }
        }
    }

    private fun attachDecorationOnClickListeners(userEquipment: UserEquipment, decorations: List<UserDecoration>?, layout: LinearLayout, userEquipmentSetId: Int) {
        //Set Defaults
        if (userEquipment.type() == DataType.ARMOR) {
            val userArmor = userEquipment as UserArmorPiece
            userArmor.armor.armor.slots.forEachIndexed { slot, idx ->
                when (idx) {
                    1 -> layout.slot1_detail.setOnClickListener {
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, null,
                                userEquipmentSetId, null,
                                Companion.DecorationsConfig(userArmor.entityId(), 1, userArmor.type(), slot))
                    }
                    2 -> layout.slot2_detail.setOnClickListener {
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, null,
                                userEquipmentSetId, null,
                                Companion.DecorationsConfig(userArmor.entityId(), 2, userArmor.type(), slot))
                    }
                    3 -> layout.slot3_detail.setOnClickListener {
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, null,
                                userEquipmentSetId, null,
                                Companion.DecorationsConfig(userArmor.entityId(), 3, userArmor.type(), slot))
                    }
                }
            }
        }

        decorations?.forEach { userDecoration ->
            when (userDecoration.slotNumber) {
                1 -> layout.slot1_detail.setOnClickListener {
                    viewModel.setActiveUserEquipment(userDecoration)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION,
                            userDecoration, userEquipmentSetId, null,
                            Companion.DecorationsConfig(userEquipment.entityId(), 1,
                                    userEquipment.type(), userDecoration.decoration.slot))
                }
                2 -> layout.slot2_detail.setOnClickListener {
                    viewModel.setActiveUserEquipment(userDecoration)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, userDecoration, userEquipmentSetId, null,
                            Companion.DecorationsConfig(userEquipment.entityId(), 2,
                                    userEquipment.type(), userDecoration.decoration.slot))
                }
                3 -> layout.slot3_detail.setOnClickListener {
                    viewModel.setActiveUserEquipment(userDecoration)
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, userDecoration, userEquipmentSetId, null,
                            Companion.DecorationsConfig(userEquipment.entityId(), 3,
                                    userEquipment.type(), userDecoration.decoration.slot))
                }
            }
        }
    }

    private fun populateUserEquipment(userEquipmentSet: UserEquipmentSet) {
        userEquipmentSet.equipment.forEach {
            when (it.type()) {
                DataType.WEAPON -> {
                    populateWeapon(it as UserWeapon)
                }
                DataType.ARMOR -> {
                    populateArmor(it as UserArmorPiece)
                    attachOnClickListeners(it, userEquipmentSet.id)
                }
                DataType.CHARM -> {
                    populateCharm(it as UserCharm, userEquipmentSet.id)
                    user_equipment_charm_slot.setOnClick {
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.CHARM, it, userEquipmentSet.id, null, null)
                    }
                }
                else -> {
                } //Skip
            }
        }
    }

    private fun populateArmor(userArmor: UserArmorPiece) {
        val armor = userArmor.armor
        val layout: View
        when (armor.armor.armor_type) {
            ArmorType.HEAD -> {
                layout = user_equipment_head_slot
            }
            ArmorType.CHEST -> {
                layout = user_equipment_chest_slot
            }
            ArmorType.ARMS -> {
                layout = user_equipment_arms_slot
            }
            ArmorType.WAIST -> {
                layout = user_equipment_waist_slot
            }
            ArmorType.LEGS -> {
                layout = user_equipment_legs_slot
            }
        }

        layout.equipment_name.text = armor.armor.name
        layout.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
        layout.rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.armor.rarity))
        layout.rarity_string.visibility = View.VISIBLE
        layout.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
        layout.defense_value.text = getString(R.string.armor_defense_value, armor.armor.defense_base, armor.armor.defense_max, armor.armor.defense_augment_max)

        //Combine the skills from the armor piece and the decorations
        
        populateSkills(armor.skills, layout.skill_section)
        populateSetBonuses(armor.setBonuses, layout.set_bonus_section)

        resetSlots(layout)

        //Populate defaults
        userArmor.armor.armor.slots.forEachIndexed {_, idx  ->
            when (idx) {
                1 -> layout.slot1_detail.visibility = View.VISIBLE
                2 -> layout.slot2_detail.visibility = View.VISIBLE
                3 -> layout.slot3_detail.visibility = View.VISIBLE
            }
        }

        for (userDecoration in userArmor.decorations) {
            when (userDecoration.slotNumber) {
                1 -> populateSlot1(layout, userDecoration.decoration)
                2 -> populateSlot2(layout, userDecoration.decoration)
                3 -> populateSlot3(layout, userDecoration.decoration)
            }
        }
    }

    private fun populateCharm(userCharm: UserCharm, userEquipmentSetId: Int) {
        user_equipment_charm_slot.equipment_name.text = userCharm.charm.charm.name
        user_equipment_charm_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(userCharm.charm.charm))
        user_equipment_charm_slot.rarity_string.text = getString(R.string.format_rarity, userCharm.charm.charm.rarity)
        user_equipment_charm_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(userCharm.charm.charm.rarity))
        user_equipment_charm_slot.rarity_string.visibility = View.VISIBLE
        user_equipment_charm_slot.setOnClick {
            viewModel.setActiveUserEquipment(userCharm)
            getRouter().navigateUserEquipmentCharmSelector(userEquipmentSetId, userCharm.charm.entityId)
        }
        user_equipment_charm_slot.hideSlots()
        hideDefense(user_equipment_charm_slot)
        user_equipment_charm_slot.card_arrow.visibility = View.INVISIBLE
    }

    private fun populateWeapon(userWeapon: UserWeapon) {
        val weapon = userWeapon.weapon.weapon
        user_equipment_weapon_slot.equipment_name.text = weapon.name
        user_equipment_weapon_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(weapon))
        user_equipment_weapon_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(weapon.rarity))
        user_equipment_weapon_slot.rarity_string.text = getString(R.string.format_rarity, weapon.rarity)
        user_equipment_weapon_slot.rarity_string.visibility = View.VISIBLE
        for (userDecoration in userWeapon.decorations) {
            when (userDecoration.slotNumber) {
                1 -> populateSlot1(user_equipment_weapon_slot, userDecoration.decoration)
                2 -> populateSlot2(user_equipment_weapon_slot, userDecoration.decoration)
                3 -> populateSlot3(user_equipment_weapon_slot, userDecoration.decoration)
            }
        }

        if (weapon.defense != 0) {
            val defenseValue = getString(R.string.format_plus, weapon.defense)
            user_equipment_weapon_slot.defense_value.text = defenseValue
        } else {
            user_equipment_weapon_slot.icon_defense.visibility = View.INVISIBLE
            user_equipment_weapon_slot.defense_value.visibility = View.INVISIBLE
        }
    }

    private fun populateSlot1(view: View, decoration: Decoration) {
        view.slot1.setImageDrawable(AssetLoader.loadIconFor(decoration))
        view.slot1_detail.visibility = View.VISIBLE
        view.slot1_detail.setLabelText(decoration.name)
        view.slot1_detail.setLeftIconDrawable(AssetLoader.loadIconFor(decoration))
    }

    private fun populateSlot2(view: View, decoration: Decoration) {
        view.slot2.setImageDrawable(AssetLoader.loadIconFor(decoration))
        view.slot2_detail.visibility = View.VISIBLE
        view.slot2_detail.setLabelText(decoration.name)
        view.slot2_detail.setLeftIconDrawable(AssetLoader.loadIconFor(decoration))
    }

    private fun populateSlot3(view: View, decoration: Decoration) {
        view.slot3.setImageDrawable(AssetLoader.loadIconFor(decoration))
        view.slot3_detail.visibility = View.VISIBLE
        view.slot3_detail.setLabelText(decoration.name)
        view.slot3_detail.setLeftIconDrawable(AssetLoader.loadIconFor(decoration))
    }

    private fun resetSlots(layout:View) {
        layout.slot1.setImageDrawable(context!!.getDrawable(R.drawable.ic_ui_slot_none))
        layout.slot2.setImageDrawable(context!!.getDrawable(R.drawable.ic_ui_slot_none))
        layout.slot3.setImageDrawable(context!!.getDrawable(R.drawable.ic_ui_slot_none))

        layout.slot1_detail.setLeftIconDrawable(null)
        layout.slot1_detail.setLabelText("")
        layout.slot1_detail.setValueText("")
        layout.slot2_detail.setLeftIconDrawable(null)
        layout.slot2_detail.setLabelText("")
        layout.slot2_detail.setValueText("")
        layout.slot2_detail.setLeftIconDrawable(null)
        layout.slot2_detail.setLabelText("")
        layout.slot2_detail.setValueText("")

        layout.slot1_detail.visibility = View.GONE
        layout.slot2_detail.visibility = View.GONE
        layout.slot3_detail.visibility = View.GONE
    }

    private fun hideDefense(view: View) {
        view.icon_defense.visibility = View.INVISIBLE
        view.defense_value.visibility = View.INVISIBLE
    }

    private fun populateSkills(skills: List<SkillLevel>, skillLayout: LinearLayout) {
        if (skills.isEmpty()) {
            skillLayout.visibility = View.GONE
            return
        }

        skillLayout.visibility = View.VISIBLE
        skillLayout.skill_list.removeAllViews()

        val inflater = LayoutInflater.from(context)

        for (skill in skills) {
            //Set the label for the Set name
            val view = inflater.inflate(R.layout.listitem_skill_level, skillLayout.skill_list, false)

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

            skillLayout.skill_list.addView(view)
        }
    }

    private fun populateSetBonuses(armorSetBonuses: List<ArmorSetBonus>, setBonusSection: LinearLayout) {
        if (armorSetBonuses.isEmpty()) {
            setBonusSection.visibility = View.GONE
            return
        }

        // show set bonus section
        setBonusSection.visibility = View.VISIBLE
        setBonusSection.set_bonus_list.removeAllViews()

        //Now to set the actual skills
        for (setBonus in armorSetBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = layoutInflater.inflate(R.layout.listitem_armorset_bonus, setBonusSection.set_bonus_list, false)

            listItem.bonus_skill_icon.setImageDrawable(skillIcon)
            listItem.bonus_skill_name.text = setBonus.skillTree.name
            listItem.bonus_requirement.setImageResource(reqIcon)

            listItem.setOnClickListener {
                getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            setBonusSection.set_bonus_list.addView(listItem)
        }
    }
}