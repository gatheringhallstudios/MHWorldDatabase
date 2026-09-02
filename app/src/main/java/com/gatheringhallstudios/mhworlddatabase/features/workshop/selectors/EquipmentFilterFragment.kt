package com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors

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
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.CheckedGroup
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.WeaponTreePagerFragment.Companion.FILTER_RESULT_CODE
import com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors.WorkshopSelectorListFragment.Companion.SelectorMode
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import java.io.Serializable
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentDecorationFilterBodyBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentWorkshopWeaponFilterBodyBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentCharmFilterBodyBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentArmorFilterBodyBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentEquipmentFilterBinding

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
    private var _binding: FragmentEquipmentFilterBinding? = null
    private val binding get() = _binding!!

    // The scroll body is a ViewStub inflated with a different layout per selector mode
    private var armorBody: FragmentArmorFilterBodyBinding? = null
    private var charmBody: FragmentCharmFilterBodyBinding? = null
    private var weaponBody: FragmentWorkshopWeaponFilterBodyBinding? = null
    private var decorationBody: FragmentDecorationFilterBodyBinding? = null

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
        _binding = FragmentEquipmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        armorBody = null
        charmBody = null
        weaponBody = null
        decorationBody = null
        _binding = null
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
                binding.scrollBody.layoutResource = R.layout.fragment_armor_filter_body
                val body = FragmentArmorFilterBodyBinding.bind(binding.scrollBody.inflate())
                armorBody = body
                body.skill1.setLabelText(getString(R.string.user_equipment_set_no_skill))
                body.skill2.setLabelText(getString(R.string.user_equipment_set_no_skill))

                rankGroup = CheckedGroup()
                rankGroup.apply {
                    rankGroup.addBinding(body.rankToggleLowRank, Rank.LOW)
                    rankGroup.addBinding(body.rankToggleHighRank, Rank.HIGH)
                    rankGroup.addBinding(body.rankToggleMasterRank, Rank.MASTER)
                }

                elementalDefGroup = CheckedGroup()
                elementalDefGroup.apply {
                    elementalDefGroup.addBinding(body.toggleFire, ElementStatus.FIRE)
                    elementalDefGroup.addBinding(body.toggleWater, ElementStatus.WATER)
                    elementalDefGroup.addBinding(body.toggleThunder, ElementStatus.THUNDER)
                    elementalDefGroup.addBinding(body.toggleIce, ElementStatus.ICE)
                    elementalDefGroup.addBinding(body.toggleDragon, ElementStatus.DRAGON)
                }

                body.skill1.setOnClickListener {
                    val skillFragment = SkillSelectorFragment.newInstance(0)
                    skillFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                    skillFragment.show(fragmentManager!!, "Filter")
                }
                body.skill1.setButtonClickFunction {
                    skillGroup.removeValue(0)
                }

                body.skill2.setOnClickListener {
                    val skillFragment = SkillSelectorFragment.newInstance(1)
                    skillFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                    skillFragment.show(fragmentManager!!, "Filter")
                }
                body.skill2.setButtonClickFunction {
                    skillGroup.removeValue(1)
                }

                skillGroup = SkillGroup()
                skillGroup.apply {
                    skillGroup.addBinding(body.skill1)
                    skillGroup.addBinding(body.skill2)
                }
            }
            SelectorMode.CHARM -> {
                binding.scrollBody.layoutResource = R.layout.fragment_charm_filter_body
                val body = FragmentCharmFilterBodyBinding.bind(binding.scrollBody.inflate())
                charmBody = body
                body.skill1.setLabelText(getString(R.string.user_equipment_set_no_skill))

                body.skill1.setOnClickListener {
                    val skillFragment = SkillSelectorFragment.newInstance(0)
                    skillFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                    skillFragment.show(fragmentManager!!, "Filter")
                }
                body.skill1.setButtonClickFunction {
                    skillGroup.removeValue(0)
                }

                skillGroup = SkillGroup()
                skillGroup.apply {
                    skillGroup.addBinding(body.skill1)
                }
            }
            SelectorMode.WEAPON -> {
                binding.scrollBody.layoutResource = R.layout.fragment_workshop_weapon_filter_body
                val body = FragmentWorkshopWeaponFilterBodyBinding.bind(binding.scrollBody.inflate())
                weaponBody = body

                weaponTypeGroup = CheckedGroup()
                weaponTypeGroup.apply {
                    weaponTypeGroup.addBinding(body.toggleGreatSword, WeaponType.GREAT_SWORD)
                    weaponTypeGroup.addBinding(body.toggleLongSword, WeaponType.LONG_SWORD)
                    weaponTypeGroup.addBinding(body.toggleSwordAndShield, WeaponType.SWORD_AND_SHIELD)
                    weaponTypeGroup.addBinding(body.toggleDualBlades, WeaponType.DUAL_BLADES)
                    weaponTypeGroup.addBinding(body.toggleHammer, WeaponType.HAMMER)
                    weaponTypeGroup.addBinding(body.toggleHuntingHorn, WeaponType.HUNTING_HORN)
                    weaponTypeGroup.addBinding(body.toggleLance, WeaponType.LANCE)
                    weaponTypeGroup.addBinding(body.toggleGunlance, WeaponType.GUNLANCE)
                    weaponTypeGroup.addBinding(body.toggleSwitchAxe, WeaponType.SWITCH_AXE)
                    weaponTypeGroup.addBinding(body.toggleChargeBlade, WeaponType.CHARGE_BLADE)
                    weaponTypeGroup.addBinding(body.toggleInsectGlaive, WeaponType.INSECT_GLAIVE)
                    weaponTypeGroup.addBinding(body.toggleLightBowgun, WeaponType.LIGHT_BOWGUN)
                    weaponTypeGroup.addBinding(body.toggleHeavyBowgun, WeaponType.HEAVY_BOWGUN)
                    weaponTypeGroup.addBinding(body.toggleBow, WeaponType.BOW)
                }

                elementGroup = CheckedGroup()
                elementGroup.apply {
                    addBinding(body.toggleFire, ElementStatus.FIRE)
                    addBinding(body.toggleWater, ElementStatus.WATER)
                    addBinding(body.toggleThunder, ElementStatus.THUNDER)
                    addBinding(body.toggleIce, ElementStatus.ICE)
                    addBinding(body.toggleDragon, ElementStatus.DRAGON)
                    addBinding(body.togglePoison, ElementStatus.POISON)
                    addBinding(body.toggleSleep, ElementStatus.SLEEP)
                    addBinding(body.toggleParalysis, ElementStatus.PARALYSIS)
                    addBinding(body.toggleBlast, ElementStatus.BLAST)
                    addBinding(body.toggleNonElemental, ElementStatus.NON_ELEMENTAL)
                }

                slotLevelToggles = CheckedGroup()
                slotLevelToggles.apply {
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel1, 1)
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel2, 2)
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel3, 3)
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel4, 4)
                }

                rankGroup = CheckedGroup()
                rankGroup.apply {
                    rankGroup.addBinding(body.rankWeaponToggleLowRank, Rank.LOW)
                    rankGroup.addBinding(body.rankWeaponToggleHighRank, Rank.HIGH)
                    rankGroup.addBinding(body.rankWeaponToggleMasterRank, Rank.MASTER)
                }
            }
            SelectorMode.DECORATION -> {
                binding.scrollBody.layoutResource = R.layout.fragment_decoration_filter_body
                val body = FragmentDecorationFilterBodyBinding.bind(binding.scrollBody.inflate())
                decorationBody = body
                nameFilter = ""
                slotLevelToggles = CheckedGroup()
                slotLevelToggles.apply {
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel1, 1)
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel2, 2)
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel3, 3)
                    slotLevelToggles.addBinding(body.slotLevelToggleLevel4, 4)
                }
            }
            else -> Unit
        }

        // Implement actions
        binding.actionClear.setOnClickListener {
            applyState(EquipmentFilterState.default)
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
                        nameFilter = armorBody!!.nameFilterEdittext.text.toString(),
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
                        nameFilter = decorationBody!!.nameFilterEdittext.text.toString(),
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
                        nameFilter = charmBody!!.nameFilterEdittext.text.toString(),
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
                        nameFilter = weaponBody!!.nameFilterEdittext.text.toString(),
                        elementalDefense = null,
                        rank = rankGroup.getValues().toSet(),
                        slotLevels = slotLevelToggles.getValues().toSet(),
                        elements = elementGroup.getValues().toSet(),
                        weaponTypes = weaponTypeGroup.getValues().toSet(),
                        skills = null
                )
            }
            else -> Unit
        }
        return null
    }

    /**
     * Applies a FilterState to the current UI.
     */
    fun applyState(state: EquipmentFilterState) {
        when (selectorMode) {
            SelectorMode.ARMOR -> {
                armorBody!!.nameFilterEdittext.setText(state.nameFilter)
                elementalDefGroup.setValues(state.elementalDefense!!)
                rankGroup.setValues(state.rank!!)
                skillGroup.setValues(state.skills!!)
            }
            SelectorMode.DECORATION -> {
                decorationBody!!.nameFilterEdittext.setText(state.nameFilter)
                slotLevelToggles.setValues(state.slotLevels!!)
            }
            SelectorMode.CHARM -> {
                charmBody!!.nameFilterEdittext.setText(state.nameFilter)
                skillGroup.setValues(state.skills!!)
            }
            SelectorMode.WEAPON -> {
                weaponBody!!.nameFilterEdittext.setText(state.nameFilter)
                rankGroup.setValues(state.rank!!)
                weaponTypeGroup.setValues(state.weaponTypes!!)
                elementGroup.setValues(state.elements!!)
                slotLevelToggles.setValues(state.slotLevels!!)
            }
            else -> Unit
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
            val res = it.copy(second = null)
            res.first.hideButton()
            res
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
            registered.showButton()
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
            if (skill != null) {
                val icon = AssetLoader.loadIconFor(skill)
                registered.showButton()
                registered.setLeftIconDrawable(icon)
                registered.setLabelText(skill?.name)
            }
        }
    }

    fun removeValue(skillNumber: Int) {
        if (skillNumber < list.size) {
            val pair = list[skillNumber]
            val buf = pair.copy(second = null)
            list[skillNumber] = buf

            buf.first.setLeftIconDrawable(null)
            buf.first.setLabelText(null)
            buf.first.hideButton()
        }
    }
}