package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.CheckableNotifier
import com.gatheringhallstudios.mhworlddatabase.components.CheckedImageButton
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentWeaponFilterBodyBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentEquipmentFilterBinding

/**
 * Helper class to manage a collection of checkables, including updating and receiving
 * the selected value.
 */
class CheckedGroup<T>(val singleOnly: Boolean = false) {
    private val map = mutableMapOf<Checkable, T>()

    /**
     * Returns a read only map containing all binded views.
     */
    val views: Map<Checkable, T> get() = map

    fun uncheckAll() {
        for ((view, _) in map) {
            view.isChecked = false
        }
    }

    /**
     * Adds a binding to the list.
     * This version also updates the change listener.
     */
    fun addBinding(item: CheckableNotifier, value: T) {
        addBinding(item as Checkable, value)
        item.onCheckedChangeListener = ::notifyChanged
    }

    /**
     * Adds a binding to the list.
     * This version also updates the change listener.
     */
    fun addBinding(item: CompoundButton, value: T) {
        addBinding(item as Checkable, value)
        item.setOnCheckedChangeListener(::notifyChanged)
    }

    /**
     * Adds a binding to the list.
     * It is necessary to register the change event to notify the group with this version.
     */
    private fun addBinding(item: Checkable, value: T) {
        map[item] = value
    }

    /**
     * Notify that an item has changed. Required as the checkable interface
     * does not have an event register function.
     */
    fun notifyChanged(item: Checkable, isChecked: Boolean) {
        if (!isChecked || !singleOnly) {
            return
        }

        for (registered in map.keys) {
            if (registered != item && registered.isChecked) {
                registered.isChecked = false
            }
        }
    }

    /**
     * Returns the value of the checked item, or null if none are selected
     */
    fun getValue(): T? {
        for ((registered, value) in map) {
            if (registered.isChecked) {
                return value
            }
        }

        return null
    }

    /**
     * Returns the values of all checked items.
     */
    fun getValues(): List<T> {
        val results = mutableListOf<T>()
        for ((registered, value) in map) {
            if (registered.isChecked) {
                results.add(value)
            }
        }
        return results
    }

    /**
     * Updates all registered items to reflect the value (and only the value)
     */
    fun setValue(value: T?) {
        if (value == null) {
            uncheckAll()
        } else {
            setValues(listOf(value))
        }
    }

    /**
     * Updates the registered items to reflect the list of values
     */
    fun setValues(values: Iterable<T>) {
        val valuesTemp = mutableSetOf<T>()
        valuesTemp.addAll(values)

        for ((registered, registeredValue) in map) {
            registered.isChecked = (registeredValue in values)
        }
    }
}

/**
 * Main fragment that manages the weapon filter dialog.
 * Create a new object with setInstance, set the target fragment, and on an apply
 * it'll call back with a result.
 */
class WeaponFilterFragment : DialogFragment() {
    private var _binding: FragmentEquipmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var bodyBinding: FragmentWeaponFilterBodyBinding

    companion object {
        const val FILTER_WEAPON_TYPE = "FILTER_WEAPON_TYPE"
        const val FILTER_STATE = "FILTER_STATE"

        @JvmStatic fun newInstance(wtype: WeaponType, state: FilterState)
                = WeaponFilterFragment().applyArguments {
                    putSerializable(FILTER_WEAPON_TYPE, wtype)
                    putSerializable(FILTER_STATE, state)
                }
    }

    private lateinit var weaponType: WeaponType

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEquipmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.scrollBody.layoutResource = R.layout.fragment_weapon_filter_body
        bodyBinding = FragmentWeaponFilterBodyBinding.bind(binding.scrollBody.inflate())

        // NOTE FOR GROUPS: Only singleOnly groups need to be notified (to enable unselections)
        this.weaponType = arguments?.getSerializable(FILTER_WEAPON_TYPE) as WeaponType


        // define sort group
        sortGroup = CheckedGroup(singleOnly = true)
        sortGroup.addBinding(bodyBinding.sortAttackToggle, FilterSortCondition.ATTACK)
        sortGroup.addBinding(bodyBinding.sortAffinityToggle, FilterSortCondition.AFFINITY)
        sortGroup.addBinding(bodyBinding.sortElementToggle, FilterSortCondition.ELEMENT_STATUS)

        // define element group
        elementGroup = CheckedGroup()
        elementGroup.apply {
            addBinding(bodyBinding.toggleFire, ElementStatus.FIRE)
            addBinding(bodyBinding.toggleWater, ElementStatus.WATER)
            addBinding(bodyBinding.toggleThunder, ElementStatus.THUNDER)
            addBinding(bodyBinding.toggleIce, ElementStatus.ICE)
            addBinding(bodyBinding.toggleDragon, ElementStatus.DRAGON)
            addBinding(bodyBinding.togglePoison, ElementStatus.POISON)
            addBinding(bodyBinding.toggleSleep, ElementStatus.SLEEP)
            addBinding(bodyBinding.toggleParalysis, ElementStatus.PARALYSIS)
            addBinding(bodyBinding.toggleBlast, ElementStatus.BLAST)
            addBinding(bodyBinding.toggleNonElemental, ElementStatus.NON_ELEMENTAL)
        }

        // define phial group
        phialGroupCB = CheckedGroup()
        phialGroupCB.apply {
            addBinding(bodyBinding.phialToggleImpact, PhialType.IMPACT)
            addBinding(bodyBinding.phialTogglePowerElementCb, PhialType.POWER_ELEMENT)
        }
        
        phialGroupSWAXE = CheckedGroup()
        phialGroupSWAXE.apply {
            addBinding(bodyBinding.phialTogglePower, PhialType.POWER)
            addBinding(bodyBinding.phialTogglePowerElementSwaxe, PhialType.POWER_ELEMENT)
            addBinding(bodyBinding.phialTogglePoison, PhialType.POISON)
            addBinding(bodyBinding.phialToggleParalysis, PhialType.PARALYSIS)
            addBinding(bodyBinding.phialToggleExhaust, PhialType.EXHAUST)
            addBinding(bodyBinding.phialToggleDragon, PhialType.DRAGON)
        }

        kinsectGroup = CheckedGroup()
        kinsectGroup.apply {
            addBinding(bodyBinding.kinsectToggleSpeed, KinsectBonus.SPEED)
            addBinding(bodyBinding.kinsectToggleStamina, KinsectBonus.STAMINA)
            addBinding(bodyBinding.kinsectToggleHealth, KinsectBonus.HEALTH)
            addBinding(bodyBinding.kinsectToggleElement, KinsectBonus.ELEMENT)
            addBinding(bodyBinding.kinsectToggleSever, KinsectBonus.SEVER)
            addBinding(bodyBinding.kinsectToggleBlunt, KinsectBonus.BLUNT)
            addBinding(bodyBinding.kinsectToggleSpiritStrength, KinsectBonus.SPIRIT_STRENGTH)
            addBinding(bodyBinding.kinsectToggleStaminaHealth, KinsectBonus.STAMINA_HEALTH)
        }

        shellingGroup = CheckedGroup()
        shellingGroup.apply {
            addBinding(bodyBinding.shellingToggleNormal, ShellingType.NORMAL)
            addBinding(bodyBinding.shellingToggleLong, ShellingType.LONG)
            addBinding(bodyBinding.shellingToggleWide, ShellingType.WIDE)
        }

        shellingLevelGroup = CheckedGroup()
        shellingLevelGroup.apply {
            addBinding(bodyBinding.shellingToggleLevel1, 1)
            addBinding(bodyBinding.shellingToggleLevel2, 2)
            addBinding(bodyBinding.shellingToggleLevel3, 3)
            addBinding(bodyBinding.shellingToggleLevel4, 4)
            addBinding(bodyBinding.shellingToggleLevel5, 5)
            addBinding(bodyBinding.shellingToggleLevel6, 6)
            addBinding(bodyBinding.shellingToggleLevel7, 7)
        }

        coatingGroup = CheckedGroup()
        coatingGroup.apply {
            addBinding(bodyBinding.coatingPower, CoatingType.POWER)
            addBinding(bodyBinding.coatingPara, CoatingType.PARALYSIS)
            addBinding(bodyBinding.coatingPoison, CoatingType.POISON)
            addBinding(bodyBinding.coatingSleep, CoatingType.SLEEP)
            addBinding(bodyBinding.coatingBlast, CoatingType.BLAST)
        }

        specialAmmoGroup = CheckedGroup(singleOnly = true)
        specialAmmoGroup.apply {
            addBinding(bodyBinding.sammoWyvernheartToggle, SpecialAmmoType.WYVERNHEART)
            addBinding(bodyBinding.sammoWyvernsnipeToggle, SpecialAmmoType.WYVERNSNIPE)
        }

        // Implement actions
        binding.actionClear.setOnClickListener {
            applyState(FilterState.default)
        }
        binding.actionCancel.setOnClickListener {
            dismiss()
        }
        binding.actionApply.setOnClickListener {
            val data = Intent()
            data.putExtra(FILTER_STATE, calculateState())
            targetFragment?.onActivityResult(targetRequestCode, 0, data)
            dismiss()
        }

        // Enable visibility of elements based on weapon type
        bodyBinding.elementToggles.isVisible = when (weaponType) {
            WeaponType.LIGHT_BOWGUN, WeaponType.HEAVY_BOWGUN -> false
            else -> true
        }

        bodyBinding.phialTypesCb.isVisible = (weaponType == WeaponType.CHARGE_BLADE)
        bodyBinding.phialTypesSwaxe.isVisible = (weaponType == WeaponType.SWITCH_AXE)
        bodyBinding.titlePhials.isVisible = bodyBinding.phialTypesCb.isVisible || bodyBinding.phialTypesSwaxe.isVisible

        bodyBinding.titleKinsect.isVisible = (weaponType == WeaponType.INSECT_GLAIVE)
        bodyBinding.kinsectToggles.isVisible = (weaponType == WeaponType.INSECT_GLAIVE)

        bodyBinding.titleShelling.isVisible = (weaponType == WeaponType.GUNLANCE)
        bodyBinding.shellingToggles.isVisible = (weaponType == WeaponType.GUNLANCE)

        bodyBinding.titleCoatings.isVisible = (weaponType == WeaponType.BOW)
        bodyBinding.coatingToggles.isVisible = (weaponType == WeaponType.BOW)
        if (bodyBinding.coatingToggles.isVisible) {
            for ((button, value) in coatingGroup.views) {
                val icon = AssetLoader.loadIconFor(value)
                (button as? CheckedImageButton)?.setImageDrawable(icon)
            }
        }

        bodyBinding.titleAmmo.isVisible = (weaponType == WeaponType.HEAVY_BOWGUN)
        bodyBinding.specialAmmoToggles.isVisible = (weaponType == WeaponType.HEAVY_BOWGUN)

        // Apply and config state from bundle
        val state = arguments?.getSerializable(FILTER_STATE) as? FilterState
        if (state != null) {
            applyState(state)
        }
    }

    /**
     * Returns the current state, received by analyzing the current view state.
     */
    fun calculateState(): FilterState {
        val phials = when (weaponType) {
            WeaponType.CHARGE_BLADE -> phialGroupCB.getValues().toSet()
            WeaponType.SWITCH_AXE -> phialGroupSWAXE.getValues().toSet()
            else -> emptySet()
        }

        return FilterState(
                isFinalOnly = bodyBinding.finalToggle.isChecked,
                sortBy = sortGroup.getValue() ?: FilterSortCondition.NONE,
                elements = elementGroup.getValues().toSet(),
                phials = phials,
                kinsectBonuses = kinsectGroup.getValues().toSet(),
                shellingTypes = shellingGroup.getValues().toSet(),
                shellingLevels = shellingLevelGroup.getValues().toSet(),
                coatingTypes = coatingGroup.getValues().toSet(),
                specialAmmo = specialAmmoGroup.getValue()
        )
    }

    /**
     * Applies a FilterState to the current UI.
     */
    fun applyState(state: FilterState) {
        // handle final
        bodyBinding.finalToggle.isChecked = state.isFinalOnly

        // Set the basic group values
        sortGroup.setValue(state.sortBy)
        elementGroup.setValues(state.elements)
        phialGroupCB.setValues(state.phials)
        phialGroupSWAXE.setValues(state.phials)
        kinsectGroup.setValues(state.kinsectBonuses)
        shellingGroup.setValues(state.shellingTypes)
        shellingLevelGroup.setValues(state.shellingLevels)
        coatingGroup.setValues(state.coatingTypes)
        specialAmmoGroup.setValue(state.specialAmmo)
    }
}