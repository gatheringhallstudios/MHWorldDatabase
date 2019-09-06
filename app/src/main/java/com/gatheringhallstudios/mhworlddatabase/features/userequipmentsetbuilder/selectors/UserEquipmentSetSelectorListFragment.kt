package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.components.SpacesItemDecoration
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.view.icon
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.view.label_text
import kotlinx.android.synthetic.main.fragment_user_equipment_set_selector.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.level_text
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.view_base_body_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class UserEquipmentSetSelectorListFragment : Fragment() {
    companion object {
        const val ARG_ACTIVE_EQUIPMENT = "ACTIVE_EQUIPMENT"
        const val ARG_SET_ID = "ACTIVE_SET_ID" //The equipment set that is currently being handled when in builder mode
        const val ARG_ARMOR_FILTER = "ACTIVE_ARMOR_FILTER" //What class armor to limit the selector to
        const val ARG_SELECTOR_MODE = "SELECTOR_MODE"
        const val ARG_DECORATION_CONFIG = "DECORATION_CONFIG"

        enum class SelectorMode {
            ARMOR,
            DECORATION,
            CHARM,
            WEAPON
        }

        class DecorationsConfig(val targetEquipmentId: Int, val targetEquipmentSlot: Int,
                                val targetEquipmentType: DataType, val decorationLevelFilter: Int) : Serializable
    }

    private val viewModel: UserEquipmentSetSelectorViewModel by lazy {
        ViewModelProviders.of(this).get(UserEquipmentSetSelectorViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_equipment_set_selector, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mode = arguments?.getSerializable(ARG_SELECTOR_MODE) as? SelectorMode
        val filter = arguments?.getSerializable(ARG_ARMOR_FILTER) as? ArmorType
        val activeEquipment = arguments?.getSerializable(ARG_ACTIVE_EQUIPMENT) as? UserEquipment
        val activeEquipmentSetId = arguments?.getInt(ARG_SET_ID)
        val decorationsConfig = arguments?.getSerializable(ARG_DECORATION_CONFIG) as? DecorationsConfig

        when (mode) {
            SelectorMode.ARMOR -> initArmorSelector(filter, activeEquipment as? UserArmorPiece, activeEquipmentSetId)
            SelectorMode.CHARM -> initCharmSelector(activeEquipment as? UserCharm, activeEquipmentSetId)
            SelectorMode.DECORATION -> initDecorationSelector(activeEquipment as? UserDecoration, activeEquipmentSetId, decorationsConfig!!)
            SelectorMode.WEAPON -> initWeaponSelector(activeEquipment as? UserWeapon, activeEquipmentSetId)
        }
    }

    private fun initArmorSelector(filter: ArmorType?, activeArmorPiece: UserArmorPiece?, activeEquipmentSetId: Int?) {
        if (filter != null) {
            viewModel.loadArmor(AppSettings.dataLocale, filter)
        }

        val adapter = UserEquipmentSetArmorSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateEquipmentForEquipmentSet(it.entityId, it.entityType, activeEquipmentSetId!!, activeArmorPiece?.armor?.entityId)
                }

                getRouter().goBack()
            }
        }

        populateSetBonuses(emptyList(), active_equipment_slot.set_bonus_section)
        populateSkills(emptyList(), active_equipment_slot.skill_section)
        //If this is going to be new piece of armor, do not populate the active armor piece
        if (activeArmorPiece != null) {
            populateActiveArmor(activeArmorPiece)
        }

        equipment_list.adapter = adapter
        equipment_list.addItemDecoration(SpacesItemDecoration(8))

        viewModel.armor.observe(this, Observer {
            adapter.items = it
        })
    }

    private fun initCharmSelector(activeCharm: UserCharm?, activeEquipmentSetId: Int?) {
        viewModel.loadCharms(AppSettings.dataLocale)

        val adapter = UserEquipmentSetCharmSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateEquipmentForEquipmentSet(it.entityId, it.entityType, activeEquipmentSetId!!, activeCharm?.entityId())
                }

                getRouter().goBack()
            }
        }
        //If this is going to be new piece of armor, do not populate the active armor piece
        if (activeCharm != null) {
            populateActiveCharm(activeCharm)
        }
        equipment_list.adapter = adapter
        equipment_list.addItemDecoration(SpacesItemDecoration(8))
        viewModel.charms.observe(this, Observer {
            adapter.items = it
        })
    }

    private fun initDecorationSelector(activeDecoration: UserDecoration?, activeEquipmentSetId: Int?, decorationsConfig: DecorationsConfig) {
        viewModel.loadDecorations(AppSettings.dataLocale)

        val adapter = UserEquipmentSetDecorationSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateDecorationForEquipmentSet(it.id, decorationsConfig.targetEquipmentId,
                            decorationsConfig.targetEquipmentSlot, decorationsConfig.targetEquipmentType, activeEquipmentSetId!!, activeDecoration?.entityId())
                }
                getRouter().goBack()
            }
        }

        equipment_list.adapter = adapter
        equipment_list.addItemDecoration(SpacesItemDecoration(8))

        if (activeDecoration != null) {
            populateActiveDecoration(activeDecoration)
        }

        viewModel.decorations.observe(this, Observer {
            adapter.items = it.filter { decoration ->
                decoration.slot <= decorationsConfig.decorationLevelFilter
            }
        })
    }

    private fun initWeaponSelector(activeWeapon: UserWeapon?, activeEquipmentSetId: Int?) {
        viewModel.loadWeapons(AppSettings.dataLocale)

        val adapter = UserEquipmentSetWeaponSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateEquipmentForEquipmentSet(it.entityId, it.entityType, activeEquipmentSetId!!, activeWeapon?.entityId())
                }
                getRouter().goBack()
            }
        }

        equipment_list.adapter = adapter
        equipment_list.addItemDecoration(SpacesItemDecoration(8))

        if (activeWeapon != null) {
            populateActiveWeapon(activeWeapon)
        }

        viewModel.weapons.observe(this, Observer {
            adapter.items = it
        })
    }

    private fun populateActiveWeapon(userWeapon: UserWeapon) {
        val weapon = userWeapon.weapon.weapon
        val skills = userWeapon.weapon.skills
        val slots = userWeapon.weapon.weapon.slots
        active_equipment_slot.setHeader(R.layout.view_weapon_header_expandable_cardview)
        active_equipment_slot.equipment_name.text = weapon.name
        active_equipment_slot.rarity_string.text = getString(R.string.format_rarity, weapon.rarity)
        active_equipment_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(weapon.rarity))
        active_equipment_slot.rarity_string.visibility = View.VISIBLE
        active_equipment_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(weapon))

        populateSkills(skills, active_equipment_slot.skill_section)
        active_equipment_slot.decorations_section.visibility = View.GONE
        active_equipment_slot.slot1_detail.visibility = View.GONE
        active_equipment_slot.slot2_detail.visibility = View.GONE
        active_equipment_slot.slot3_detail.visibility = View.GONE

        if (!slots.isEmpty()) {
            active_equipment_slot.decorations_section.visibility = View.VISIBLE
            slots.active.forEachIndexed { idx, _ ->
                when (idx + 1) {
                    1 -> active_equipment_slot.slot1_detail.visibility = View.VISIBLE
                    2 -> active_equipment_slot.slot2_detail.visibility = View.VISIBLE
                    3 -> active_equipment_slot.slot3_detail.visibility = View.VISIBLE
                }
            }

            for (userDecoration in userWeapon.decorations) {
                when (userDecoration.slotNumber) {
                    1 -> populateSlot1(active_equipment_slot, userDecoration.decoration)
                    2 -> populateSlot2(active_equipment_slot, userDecoration.decoration)
                    3 -> populateSlot3(active_equipment_slot, userDecoration.decoration)
                }
            }
        }
    }

    private fun populateActiveArmor(userArmor: UserArmorPiece) {
        val armor = userArmor.armor

        active_equipment_slot.equipment_name.text = armor.armor.name
        active_equipment_slot.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
        active_equipment_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.armor.rarity))
        active_equipment_slot.rarity_string.visibility = View.VISIBLE
        active_equipment_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
        active_equipment_slot.defense_value.text = getString(
                R.string.armor_defense_value,
                armor.armor.defense_base,
                armor.armor.defense_max,
                armor.armor.defense_augment_max)

        populateSkills(armor.skills, active_equipment_slot.skill_section)
        populateSetBonuses(armor.setBonuses, active_equipment_slot.set_bonus_section)
        active_equipment_slot.decorations_section.visibility = View.GONE
        active_equipment_slot.slot1_detail.visibility = View.GONE
        active_equipment_slot.slot2_detail.visibility = View.GONE
        active_equipment_slot.slot3_detail.visibility = View.GONE

        if (!armor.armor.slots.isEmpty()) {
            active_equipment_slot.decorations_section.visibility = View.VISIBLE
            userArmor.armor.armor.slots.active.forEachIndexed { idx, _ ->
                when (idx + 1) {
                    1 -> active_equipment_slot.slot1_detail.visibility = View.VISIBLE
                    2 -> active_equipment_slot.slot2_detail.visibility = View.VISIBLE
                    3 -> active_equipment_slot.slot3_detail.visibility = View.VISIBLE
                }
            }

            for (userDecoration in userArmor.decorations) {
                when (userDecoration.slotNumber) {
                    1 -> populateSlot1(active_equipment_slot, userDecoration.decoration)
                    2 -> populateSlot2(active_equipment_slot, userDecoration.decoration)
                    3 -> populateSlot3(active_equipment_slot, userDecoration.decoration)
                }
            }
        }
    }

    private fun populateActiveCharm(userCharm: UserCharm) {
        val charm = userCharm.charm.charm

        active_equipment_slot.equipment_name.text = charm.name
        active_equipment_slot.rarity_string.text = getString(R.string.format_rarity, charm.rarity)
        active_equipment_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(charm.rarity))
        active_equipment_slot.rarity_string.visibility = View.VISIBLE
        active_equipment_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(charm))
        active_equipment_slot.defense_value.visibility = View.GONE
        active_equipment_slot.icon_defense.visibility = View.GONE
        populateSkills(userCharm.charm.skills, active_equipment_slot.skill_section)
        populateSetBonuses(emptyList(), active_equipment_slot.set_bonus_section)
        active_equipment_slot.icon_slots.visibility = View.GONE
        active_equipment_slot.slot1.visibility = View.GONE
        active_equipment_slot.slot2.visibility = View.GONE
        active_equipment_slot.slot3.visibility = View.GONE
        active_equipment_slot.decorations_section.visibility = View.GONE
        active_equipment_slot.slot1_detail.visibility = View.GONE
        active_equipment_slot.slot2_detail.visibility = View.GONE
        active_equipment_slot.slot3_detail.visibility = View.GONE
    }

    private fun populateActiveDecoration(userDecoration: UserDecoration) {
        val decoration = userDecoration.decoration

        active_equipment_slot.equipment_name.text = decoration.name
        active_equipment_slot.rarity_string.text = getString(R.string.format_rarity, decoration.rarity)
        active_equipment_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(decoration.rarity))
        active_equipment_slot.rarity_string.visibility = View.VISIBLE
        active_equipment_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(decoration))
        active_equipment_slot.defense_value.visibility = View.GONE
        active_equipment_slot.icon_defense.visibility = View.GONE
        val skill = SkillLevel(level = 1)
        skill.skillTree = decoration.skillTree
        populateSkills(listOf(skill), active_equipment_slot.skill_section)
        populateSetBonuses(emptyList(), active_equipment_slot.set_bonus_section)
        active_equipment_slot.decorations_section.visibility = View.GONE
        active_equipment_slot.icon_slots.visibility = View.GONE
        active_equipment_slot.slot1_detail.visibility = View.GONE
        active_equipment_slot.slot2_detail.visibility = View.GONE
        active_equipment_slot.slot3_detail.visibility = View.GONE
        active_equipment_slot.slot1.visibility = View.GONE
        active_equipment_slot.slot2.visibility = View.GONE
        active_equipment_slot.slot3.visibility = View.GONE
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