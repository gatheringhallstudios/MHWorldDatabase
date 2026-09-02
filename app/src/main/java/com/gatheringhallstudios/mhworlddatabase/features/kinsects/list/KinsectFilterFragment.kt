package com.gatheringhallstudios.mhworlddatabase.features.kinsects.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectAttackType
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectDustEffect
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.CheckedGroup
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentKinsectFilterBodyBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentEquipmentFilterBinding

class KinsectFilterFragment : DialogFragment() {
    private var _binding: FragmentEquipmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var bodyBinding: FragmentKinsectFilterBodyBinding

    companion object {
        const val FILTER_STATE = "FILTER_STATE"

        @JvmStatic fun newInstance(state: FilterState)
                = KinsectFilterFragment().applyArguments {
            putSerializable(FILTER_STATE, state)
        }
    }

    lateinit var attackTypeGroup: CheckedGroup<KinsectAttackType>
    lateinit var dustEffectGroup: CheckedGroup<KinsectDustEffect>
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
        binding.scrollBody.layoutResource = R.layout.fragment_kinsect_filter_body
        bodyBinding = FragmentKinsectFilterBodyBinding.bind(binding.scrollBody.inflate())

        // define sort group
        sortGroup = CheckedGroup(singleOnly = true)
        sortGroup.addBinding(bodyBinding.sortPowerToggle, FilterSortCondition.POWER)
        sortGroup.addBinding(bodyBinding.sortSpeedToggle, FilterSortCondition.SPEED)
        sortGroup.addBinding(bodyBinding.sortHealToggle, FilterSortCondition.HEAL)


        attackTypeGroup = CheckedGroup()
        attackTypeGroup.apply {
            addBinding(bodyBinding.attackTypeToggleSever, KinsectAttackType.SEVER)
            addBinding(bodyBinding.attackTypeToggleBlunt, KinsectAttackType.BLUNT)
        }

        dustEffectGroup = CheckedGroup()
        dustEffectGroup.apply {
            addBinding(bodyBinding.togglePoison, KinsectDustEffect.POISON)
            addBinding(bodyBinding.toggleParalysis, KinsectDustEffect.PARALYSIS)
            addBinding(bodyBinding.toggleHeal, KinsectDustEffect.HEAL)
            addBinding(bodyBinding.toggleBlast, KinsectDustEffect.BLAST)
        }

        // Implement actions
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

        val state = arguments?.getSerializable(FILTER_STATE) as? FilterState
        if (state != null) {
            applyState(state)
        }
    }

    /**
     * Returns the current state, received by analyzing the current view state.
     */
    fun calculateState(): FilterState {

        return FilterState(
                isFinalOnly = bodyBinding.finalToggle.isChecked,
                sortBy = sortGroup.getValue() ?: FilterSortCondition.NONE,
                attackTypes = attackTypeGroup.getValues().toSet(),
                dustEffects = dustEffectGroup.getValues().toSet()
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
        attackTypeGroup.setValues(state.attackTypes)
        dustEffectGroup.setValues(state.dustEffects)
    }
}