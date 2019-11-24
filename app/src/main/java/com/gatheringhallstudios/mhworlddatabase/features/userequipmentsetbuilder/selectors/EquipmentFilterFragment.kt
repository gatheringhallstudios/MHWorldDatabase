package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment.Companion.SelectorMode
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.CheckedGroup
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.FilterSortCondition
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.FilterState
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.fragment_equipment_filter.*

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
        fun newInstance(selectorMode: SelectorMode, state: FilterState?) = EquipmentFilterFragment().applyArguments {
            putSerializable(SELECTOR_MODE, selectorMode)
            putSerializable(FILTER_STATE, state)
        }
    }

    //Decides what mode to put the selector fragment
    private lateinit var selectorMode: SelectorMode

    lateinit var elementGroup: CheckedGroup<ElementStatus>
    lateinit var phialGroupCB: CheckedGroup<PhialType>
    lateinit var phialGroupSWAXE: CheckedGroup<PhialType>
    lateinit var kinsectGroup: CheckedGroup<KinsectBonus>
    lateinit var shellingGroup: CheckedGroup<ShellingType>
    lateinit var shellingLevelGroup: CheckedGroup<Int>
    lateinit var coatingGroup: CheckedGroup<CoatingType>
    lateinit var specialAmmoGroup: CheckedGroup<SpecialAmmoType>
    lateinit var sortGroup: CheckedGroup<FilterSortCondition>

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
                scroll_body.layoutResource = R.layout.fragment_armor_filter_body
                scroll_body.inflate()
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
            }
        }

        // Implement actions
        action_clear.setOnClickListener {
            //            applyState(FilterState.default)
        }
        action_cancel.setOnClickListener {
            dismiss()
        }
        action_apply.setOnClickListener {
            //            val data = Intent()
//            data.putExtra(FILTER_STATE, calculateState())
//            targetFragment?.onActivityResult(targetRequestCode, 0, data)
//            dismiss()
        }
//
//        // Enable visibility of elements based on weapon type
//        element_toggles.isVisible = when (weaponType) {
//            WeaponType.LIGHT_BOWGUN, WeaponType.HEAVY_BOWGUN -> false
//            else -> true
//        }
//
//        phial_types_cb.isVisible = (weaponType == WeaponType.CHARGE_BLADE)
//        phial_types_swaxe.isVisible = (weaponType == WeaponType.SWITCH_AXE)
//        title_phials.isVisible = phial_types_cb.isVisible || phial_types_swaxe.isVisible
//
//        title_kinsect.isVisible = (weaponType == WeaponType.INSECT_GLAIVE)
//        kinsect_toggles.isVisible = (weaponType == WeaponType.INSECT_GLAIVE)
//
//        title_shelling.isVisible = (weaponType == WeaponType.GUNLANCE)
//        shelling_toggles.isVisible = (weaponType == WeaponType.GUNLANCE)
//
//        title_coatings.isVisible = (weaponType == WeaponType.BOW)
//        coating_toggles.isVisible = (weaponType == WeaponType.BOW)
//        if (coating_toggles.isVisible) {
//            for ((button, value) in coatingGroup.views) {
//                val icon = AssetLoader.loadIconFor(value)
//                (button as? CheckedImageButton)?.setImageDrawable(icon)
//            }
//        }
//
//        title_ammo.isVisible = (weaponType == WeaponType.HEAVY_BOWGUN)
//        special_ammo_toggles.isVisible = (weaponType == WeaponType.HEAVY_BOWGUN)
//
//        // Apply and config state from bundle
//        val state = arguments?.getSerializable(FILTER_STATE) as? FilterState
//        if (state != null) {
//            applyState(state)
//        }
    }

    /**
     * Returns the current state, received by analyzing the current view state.
     */
//    fun calculateState(): FilterState {
//        val phials = when (weaponType) {
//            WeaponType.CHARGE_BLADE -> phialGroupCB.getValues().toSet()
//            WeaponType.SWITCH_AXE -> phialGroupSWAXE.getValues().toSet()
//            else -> emptySet()
//        }
//
//        return FilterState(
//                isFinalOnly = final_toggle.isChecked,
//                sortBy = sortGroup.getValue() ?: FilterSortCondition.NONE,
//                elements = elementGroup.getValues().toSet(),
//                phials = phials,
//                kinsectBonuses = kinsectGroup.getValues().toSet(),
//                shellingTypes = shellingGroup.getValues().toSet(),
//                shellingLevels = shellingLevelGroup.getValues().toSet(),
//                coatingTypes = coatingGroup.getValues().toSet(),
//                specialAmmo = specialAmmoGroup.getValue()
//        )
//    }
//
//    /**
//     * Applies a FilterState to the current UI.
//     */
//    fun applyState(state: FilterState) {
//        // handle final
//        final_toggle.isChecked = state.isFinalOnly
//
//        // Set the basic group values
//        sortGroup.setValue(state.sortBy)
//        elementGroup.setValues(state.elements)
//        phialGroupCB.setValues(state.phials)
//        phialGroupSWAXE.setValues(state.phials)
//        kinsectGroup.setValues(state.kinsectBonuses)
//        shellingGroup.setValues(state.shellingTypes)
//        shellingLevelGroup.setValues(state.shellingLevels)
//        coatingGroup.setValues(state.coatingTypes)
//        specialAmmoGroup.setValue(state.specialAmmo)
//    }
}