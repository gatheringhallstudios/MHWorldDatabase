package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment.Companion.SelectorMode
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.CheckedGroup
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

class EquipmentFilterState(
        var selectorMode: SelectorMode,
        var nameFilter: String?,
        var rank: Set<Rank>?,
        var elementalDefense: Set<ElementStatus>?,
        var slotLevels: Set<Int>?,
        var weaponTypes: Set<WeaponType>?,
        var elements: Set<ElementStatus>?
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
                elements = emptySet()
        )
    }

    fun isEmpty(): Boolean {
        return this.nameFilter.isNullOrEmpty() && this.rank.isNullOrEmpty() &&
                this.elementalDefense.isNullOrEmpty() && this.slotLevels.isNullOrEmpty() &&
                this.weaponTypes.isNullOrEmpty() && this.elements.isNullOrEmpty()
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

    //Armor specific
    private lateinit var rankGroup: CheckedGroup<Rank>
    private lateinit var elementalDefGroup: CheckedGroup<ElementStatus>
    //TODO: skill filter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        when (selectorMode) {
            SelectorMode.ARMOR -> {
                nameFilter = ""
                scroll_body.layoutResource = R.layout.fragment_armor_filter_body
                scroll_body.inflate()
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
            }
            SelectorMode.CHARM -> {
                scroll_body.layoutResource = R.layout.fragment_charm_filter_body
                scroll_body.inflate()
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
                        weaponTypes = null
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
                        weaponTypes = null
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
                        weaponTypes = null)
            }
            SelectorMode.WEAPON -> {
                return EquipmentFilterState(
                        selectorMode = selectorMode,
                        nameFilter = name_filter_edittext.text.toString(),
                        elementalDefense = null,
                        rank = rankGroup.getValues().toSet(),
                        slotLevels = slotLevelToggles.getValues().toSet(),
                        elements = elementGroup.getValues().toSet(),
                        weaponTypes = weaponTypeGroup.getValues().toSet()
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
            }
            SelectorMode.DECORATION -> {
                name_filter_edittext.setText(state.nameFilter)
                slotLevelToggles.setValues(state.slotLevels!!)
            }
            SelectorMode.CHARM -> {
                name_filter_edittext.setText(state.nameFilter)
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