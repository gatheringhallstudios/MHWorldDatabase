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
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment.Companion.SelectorMode
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.CheckedGroup
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.fragment_armor_filter_body.*
import kotlinx.android.synthetic.main.fragment_armor_filter_body.name_filter_edittext
import kotlinx.android.synthetic.main.fragment_decoration_filter_body.*
import kotlinx.android.synthetic.main.fragment_equipment_filter.*
import java.io.Serializable

class EquipmentFilterState(
        var selectorMode: SelectorMode,
//    var isFinalOnly: Boolean,
//    var sortBy: FilterSortCondition,
        var nameFilter: String?,
        var rank: Set<Rank>?,
        var elementalDefense: Set<ElementStatus>?,
        var slotLevels: Set<Int>?
//    var phials: Set<PhialType>,
//    var kinsectBonuses: Set<KinsectBonus>,
//    var shellingTypes: Set<ShellingType>,
//    var shellingLevels: Set<Int>,
//    var coatingTypes: Set<CoatingType>,
//    var specialAmmo: SpecialAmmoType?
) : Serializable {
    companion object {
        @JvmStatic
        val default = EquipmentFilterState(
                selectorMode = SelectorMode.NONE,
//                isFinalOnly = false,
//                sortBy = FilterSortCondition.NONE,
                nameFilter = "",
                rank = emptySet(),
                elementalDefense = emptySet(),
                slotLevels = emptySet()
//                phials = emptySet(),
//                kinsectBonuses = emptySet(),
//                shellingTypes = emptySet(),
//                shellingLevels = emptySet(),
//                coatingTypes = emptySet(),
//                specialAmmo = null
        )
    }

    fun isEmpty(): Boolean {
        return this.nameFilter.isNullOrEmpty() && this.rank.isNullOrEmpty() &&
                this.elementalDefense.isNullOrEmpty() && this.slotLevels.isNullOrEmpty()
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
                        slotLevels = null
                )
            }
            SelectorMode.DECORATION -> {
                return EquipmentFilterState(
                        selectorMode = selectorMode,
                        nameFilter = name_filter_edittext.text.toString(),
                        elementalDefense = null,
                        rank = null,
                        slotLevels = slotLevelToggles.getValues().toSet()
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
        }
    }
}