package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelButtonCell
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTree
import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment.Companion.SelectorMode
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.CheckedGroup
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.WeaponTreePagerFragment.Companion.FILTER_RESULT_CODE
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.fragment_armor_filter_body.name_filter_edittext
import kotlinx.android.synthetic.main.fragment_armor_filter_body.rank_toggle_high_rank
import kotlinx.android.synthetic.main.fragment_armor_filter_body.rank_toggle_low_rank
import kotlinx.android.synthetic.main.fragment_armor_filter_body.toggle_dragon
import kotlinx.android.synthetic.main.fragment_armor_filter_body.toggle_fire
import kotlinx.android.synthetic.main.fragment_armor_filter_body.toggle_ice
import kotlinx.android.synthetic.main.fragment_armor_filter_body.toggle_thunder
import kotlinx.android.synthetic.main.fragment_armor_filter_body.toggle_water
import kotlinx.android.synthetic.main.fragment_decoration_filter_body.slot_level_toggle_level_1
import kotlinx.android.synthetic.main.fragment_decoration_filter_body.slot_level_toggle_level_2
import kotlinx.android.synthetic.main.fragment_decoration_filter_body.slot_level_toggle_level_3
import kotlinx.android.synthetic.main.fragment_decoration_filter_body.slot_level_toggle_level_4
import kotlinx.android.synthetic.main.fragment_equipment_filter.*
import kotlinx.android.synthetic.main.fragment_weapon_filter2_body.*
import java.io.Serializable
import kotlinx.android.synthetic.main.fragment_armor_filter_body.skill_1 as armor_skill_1
import kotlinx.android.synthetic.main.fragment_armor_filter_body.skill_2 as armor_skill_2
import kotlinx.android.synthetic.main.fragment_charm_filter_body.skill_1 as charm_skill_1

class EquipmentFilterState(
        var selectorMode: SelectorMode,
        var nameFilter: String?,
        var rank: Set<Rank>?,
        var elementalDefense: Set<ElementStatus>?,
        var slotLevels: Set<Int>?,
        var weaponTypes: Set<WeaponType>?,
        var elements: Set<ElementStatus>?,
        var skills: Set<SkillTree>?
) : Serializable {
    companion object {
        @JvmStatic
        val default = EquipmentFilterState(
                selectorMode = SelectorMode.NONE,
                nameFilter = "",
                rank = emptySet(),
                elementalDefense = emptySet(),
                slotLevels = emptySet(),
                weaponTypes = emptySet(),
                elements = emptySet(),
                skills = emptySet()
        )
    }

    fun isEmpty(): Boolean {
        return this.nameFilter.isNullOrEmpty() && this.rank.isNullOrEmpty() &&
                this.elementalDefense.isNullOrEmpty() && this.slotLevels.isNullOrEmpty() &&
                this.weaponTypes.isNullOrEmpty() && this.elements.isNullOrEmpty()
                && this.skills.isNullOrEmpty()
    }
}

/**
 * Main fragment that manages the selector filter dialog
 * Create a new object with setInstance, set the target fragment, and on an apply
 * it'll call back with a result.
 */
class EquipmentFilterFragment : DialogFragment() {
    companion object {
        const val SELECTOR_MODE = "FILTER_MODE"
        const val FILTER_STATE = "FILTER_STATE"

        @JvmStatic
        fun newInstance(selectorMode: SelectorMode, state: EquipmentFilterState?) = EquipmentFilterFragment().applyArguments {
            putSerializable(SELECTOR_MODE, selectorMode)
            putSerializable(FILTER_STATE, state)
        }
    }

    //Decides what mode to put the selector fragment
    private lateinit var selectorMode: SelectorMode

    //Shared
    private lateinit var nameFilter: String
    private lateinit var skillGroup: SkillGroup

    //Armor specific
    private lateinit var rankGroup: CheckedGroup<Rank>
    private lateinit var elementalDefGroup: CheckedGroup<ElementStatus>

    //Weapon specific
    private lateinit var weaponTypeGroup: CheckedGroup<WeaponType>
    private lateinit var elementGroup: CheckedGroup<ElementStatus>

    //Decoration specific
    private lateinit var slotLevelToggles: CheckedGroup<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // makes the dialog into a full screen one
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme)

        this.selectorMode = arguments?.getSerializable(SELECTOR_MODE) as SelectorMode
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_equipment_filter, container, false)
    }

    /**
     * Receives a dialog result. Currently the only supported dialog is the filter fragment.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != FILTER_RESULT_CODE) {
            return
        }

        val skillTree = data?.getSerializableExtra(SkillSelectorFragment.SELECTED_SKILL) as? SkillTree
        val skillNumber = data?.getSerializableExtra(SkillSelectorFragment.SKILL_NUMBER) as? Int
        if (skillTree != null && skillNumber != null) {
            skillGroup.setValue(skillTree, skillNumber)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        when (selectorMode) {
            SelectorMode.ARMOR -> {
                nameFilter = ""
                scroll_body.layoutResource = R.layout.fragment_armor_filter_body
                scroll_body.inflate()
                armor_skill_1.setLabelText(getString(R.string.user_equipment_set_no_skill))
                armor_skill_2.setLabelText(getString(R.string.user_equipment_set_no_skill))

                rankGroup = CheckedGroup()
                rankGroup.apply {
                    rankGroup.addBinding(rank_toggle_low_rank, Rank.LOW)
                    rankGroup.addBinding(rank_toggle_high_rank, Rank.HIGH)
                }

                elementalDefGroup = CheckedGroup()
                elementalDefGroup.apply {
                    elementalDefGroup.addBinding(toggle_fire, ElementStatus.FIRE)
                    elementalDefGroup.addBinding(toggle_water, ElementStatus.WATER)
                    elementalDefGroup.addBinding(toggle_thunder, ElementStatus.THUNDER)
                    elementalDefGroup.addBinding(toggle_ice, ElementStatus.ICE)
                    elementalDefGroup.addBinding(toggle_dragon, ElementStatus.DRAGON)
                }

                armor_skill_1.setOnClickListener {
                    val skillFragment = SkillSelectorFragment.newInstance(0)
                    skillFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                    skillFragment.show(fragmentManager!!, "Filter")
                }
                armor_skill_1.setButtonClickFunction {
                    skillGroup.removeValue(0)
                }

                armor_skill_2.setOnClickListener {
                    val skillFragment = SkillSelectorFragment.newInstance(1)
                    skillFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                    skillFragment.show(fragmentManager!!, "Filter")
                }
                armor_skill_2.setButtonClickFunction {
                    skillGroup.removeValue(1)
                }

                skillGroup = SkillGroup()
                skillGroup.apply {
                    skillGroup.addBinding(armor_skill_1)
                    skillGroup.addBinding(armor_skill_2)
                }
            }
            SelectorMode.CHARM -> {
                scroll_body.layoutResource = R.layout.fragment_charm_filter_body
                scroll_body.inflate()
                charm_skill_1.setLabelText(getString(R.string.user_equipment_set_no_skill))

                charm_skill_1.setOnClickListener {
                    val skillFragment = SkillSelectorFragment.newInstance(0)
                    skillFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                    skillFragment.show(fragmentManager!!, "Filter")
                }
                charm_skill_1.setButtonClickFunction {
                    skillGroup.removeValue(0)
                skillGroup = SkillGroup()
                skillGroup.addBinding(charm_skill_1)

                }
            }
            SelectorMode.WEAPON -> {
                scroll_body.layoutResource = R.layout.fragment_weapon_filter2_body
                scroll_body.inflate()

                weaponTypeGroup = CheckedGroup()
                weaponTypeGroup.apply {
                    weaponTypeGroup.addBinding(toggle_great_sword, WeaponType.GREAT_SWORD)
                    weaponTypeGroup.addBinding(toggle_long_sword, WeaponType.LONG_SWORD)
                    weaponTypeGroup.addBinding(toggle_sword_and_shield, WeaponType.SWORD_AND_SHIELD)
                    weaponTypeGroup.addBinding(toggle_dual_blades, WeaponType.DUAL_BLADES)
                    weaponTypeGroup.addBinding(toggle_hammer, WeaponType.HAMMER)
                    weaponTypeGroup.addBinding(toggle_hunting_horn, WeaponType.HUNTING_HORN)
                    weaponTypeGroup.addBinding(toggle_lance, WeaponType.LANCE)
                    weaponTypeGroup.addBinding(toggle_gunlance, WeaponType.GUNLANCE)
                    weaponTypeGroup.addBinding(toggle_switch_axe, WeaponType.SWITCH_AXE)
                    weaponTypeGroup.addBinding(toggle_charge_blade, WeaponType.CHARGE_BLADE)
                    weaponTypeGroup.addBinding(toggle_insect_glaive, WeaponType.INSECT_GLAIVE)
                    weaponTypeGroup.addBinding(toggle_light_bowgun, WeaponType.LIGHT_BOWGUN)
                    weaponTypeGroup.addBinding(toggle_heavy_bowgun, WeaponType.HEAVY_BOWGUN)
                    weaponTypeGroup.addBinding(toggle_bow, WeaponType.BOW)
                }

                elementGroup = CheckedGroup()
                elementGroup.apply {
                    addBinding(toggle_fire, ElementStatus.FIRE)
                    addBinding(toggle_water, ElementStatus.WATER)
                    addBinding(toggle_thunder, ElementStatus.THUNDER)
                    addBinding(toggle_ice, ElementStatus.ICE)
                    addBinding(toggle_dragon, ElementStatus.DRAGON)
                    addBinding(toggle_poison, ElementStatus.POISON)
                    addBinding(toggle_sleep, ElementStatus.SLEEP)
                    addBinding(toggle_paralysis, ElementStatus.PARALYSIS)
                    addBinding(toggle_blast, ElementStatus.BLAST)
                }

                slotLevelToggles = CheckedGroup()
                slotLevelToggles.apply {
                    slotLevelToggles.addBinding(slot_level_toggle_level_1, 1)
                    slotLevelToggles.addBinding(slot_level_toggle_level_2, 2)
                    slotLevelToggles.addBinding(slot_level_toggle_level_3, 3)
                    slotLevelToggles.addBinding(slot_level_toggle_level_4, 4)
                }

                rankGroup = CheckedGroup()
                rankGroup.apply {
                    rankGroup.addBinding(rank_toggle_low_rank, Rank.LOW)
                    rankGroup.addBinding(rank_toggle_high_rank, Rank.HIGH)
                }
            }
            SelectorMode.DECORATION -> {
                scroll_body.layoutResource = R.layout.fragment_decoration_filter_body
                scroll_body.inflate()
                nameFilter = ""
                slotLevelToggles = CheckedGroup()
                slotLevelToggles.apply {
                    slotLevelToggles.addBinding(slot_level_toggle_level_1, 1)
                    slotLevelToggles.addBinding(slot_level_toggle_level_2, 2)
                    slotLevelToggles.addBinding(slot_level_toggle_level_3, 3)
                    slotLevelToggles.addBinding(slot_level_toggle_level_4, 4)
                }
            }
        }

        // Implement actions
        action_clear.setOnClickListener {
            applyState(EquipmentFilterState.default)
        }
        action_cancel.setOnClickListener {
            dismiss()
        }
        action_apply.setOnClickListener {
            val data = Intent()
            data.putExtra(FILTER_STATE, calculateState())
            targetFragment?.onActivityResult(targetRequestCode, 0, data)
            dismiss()
        }

        // Apply and config state from bundle
        val state = arguments?.getSerializable(FILTER_STATE) as? EquipmentFilterState
        if (state != null) {
            applyState(state)
        }
    }

    /**
     * Returns the current state, received by analyzing the current view state.
     */
    fun calculateState(): EquipmentFilterState? {
        when (selectorMode) {
            SelectorMode.ARMOR -> {
                return EquipmentFilterState(
                        selectorMode = selectorMode,
                        nameFilter = name_filter_edittext.text.toString(),
                        elementalDefense = elementalDefGroup.getValues().toSet(),
                        rank = rankGroup.getValues().toSet(),
                        slotLevels = null,
                        elements = null,
                        weaponTypes = null,
                        skills = skillGroup.getValues().toSet()
                )
            }
            SelectorMode.DECORATION -> {
                return EquipmentFilterState(
                        selectorMode = selectorMode,
                        nameFilter = name_filter_edittext.text.toString(),
                        elementalDefense = null,
                        rank = null,
                        slotLevels = slotLevelToggles.getValues().toSet(),
                        elements = null,
                        weaponTypes = null,
                        skills = null
                )
            }
            SelectorMode.CHARM -> {
                return EquipmentFilterState(
                        selectorMode = selectorMode,
                        nameFilter = name_filter_edittext.text.toString(),
                        elementalDefense = null,
                        rank = null,
                        slotLevels = null,
                        elements = null,
                        weaponTypes = null,
                        skills = skillGroup.getValues().toSet())
            }
            SelectorMode.WEAPON -> {
                return EquipmentFilterState(
                        selectorMode = selectorMode,
                        nameFilter = name_filter_edittext.text.toString(),
                        elementalDefense = null,
                        rank = rankGroup.getValues().toSet(),
                        slotLevels = slotLevelToggles.getValues().toSet(),
                        elements = elementGroup.getValues().toSet(),
                        weaponTypes = weaponTypeGroup.getValues().toSet(),
                        skills = null
                )
            }
        }
        return null
    }

    /**
     * Applies a FilterState to the current UI.
     */
    fun applyState(state: EquipmentFilterState) {
        when (selectorMode) {
            SelectorMode.ARMOR -> {
                name_filter_edittext.setText(state.nameFilter)
                elementalDefGroup.setValues(state.elementalDefense!!)
                rankGroup.setValues(state.rank!!)
                skillGroup.setValues(state.skills!!)
            }
            SelectorMode.DECORATION -> {
                name_filter_edittext.setText(state.nameFilter)
                slotLevelToggles.setValues(state.slotLevels!!)
            }
            SelectorMode.CHARM -> {
                name_filter_edittext.setText(state.nameFilter)
                skillGroup.setValues(state.skills!!)
            }
            SelectorMode.WEAPON -> {
                name_filter_edittext.setText(state.nameFilter)
                rankGroup.setValues(state.rank!!)
                weaponTypeGroup.setValues(state.weaponTypes!!)
                elementGroup.setValues(state.elements!!)
                slotLevelToggles.setValues(state.slotLevels!!)
            }
        }
    }
}

/**
 * Helper class to manage a list of skills, including updating and receiving
 * the selected value.
 */
class SkillGroup {
    private var list = mutableListOf<Pair<IconLabelButtonCell, SkillTree?>>()

    /**
     * Returns a read only map containing all binded views.
     */
    val views: List<Pair<IconLabelButtonCell, SkillTree?>> get() = list

    fun emptyList() {
        this.list = list.map {
            it.copy(second = null)
        }.toMutableList()
    }

    /**
     * Adds a binding to the list.
     * It is necessary to register the change event to notify the group with this version.
     */
    fun addBinding(item: IconLabelButtonCell) {
        list.add(Pair(item, null))
    }

    /**
     * Returns the value of the checked item, or null if none are selected
     */
    fun getValue(skillNumber: Int): SkillTree? {
        return if (skillNumber < list.size) {
            list[skillNumber].second
        } else {
            null
        }
    }

    /**
     * Returns the values of all checked items.
     */
    fun getValues(): List<SkillTree> {
        val buf = mutableListOf<SkillTree>()
        list.forEach { if (it.second != null) buf.add(it.second!!) }
        return buf
    }

    fun setValue(value: SkillTree, skillNumber: Int) {
        if (skillNumber < list.size) {
            val pair = list[skillNumber]
            val buf = pair.copy(second = value)
            list[skillNumber] = buf

            val registered = buf.first
            val skill = buf.second
            val icon = if (skill != null) AssetLoader.loadIconFor(skill) else null
            registered.setLeftIconDrawable(icon)
            registered.setLabelText(skill?.name)
        }
    }

    /**
     * Updates the registered items to reflect the list of values
     */
    fun setValues(values: Iterable<SkillTree>) {
        emptyList()
        values.forEachIndexed { idx, value ->
            if (idx < list.size) {
                val pair = list[idx]
                val buf = pair.copy(second = value)
                list[idx] = buf
            }
        }

        for (pair in list) {
            val registered = pair.first
            val skill = pair.second
            val icon = if (skill != null) AssetLoader.loadIconFor(skill) else null
            registered.setLeftIconDrawable(icon)
            registered.setLabelText(skill?.name)
        }
    }

    fun removeValue(skillNumber: Int) {
        if (skillNumber < list.size) {
            val pair = list[skillNumber]
            val buf = pair.copy(second = null)
            list[skillNumber] = buf

            buf.first.setLeftIconDrawable(null)
            buf.first.setLabelText(null)
        }
    }
}