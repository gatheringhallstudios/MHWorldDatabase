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
import kotlinx.android.synthetic.main.fragment_equipment_filter.*
import kotlinx.android.synthetic.main.fragment_kinsect_filter_body.*

class KinsectFilterFragment : DialogFragment() {
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
        return inflater.inflate(R.layout.fragment_equipment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scroll_body.layoutResource = R.layout.fragment_kinsect_filter_body
        scroll_body.inflate()

        // define sort group
        sortGroup = CheckedGroup(singleOnly = true)
        sortGroup.addBinding(sort_power_toggle, FilterSortCondition.POWER)
        sortGroup.addBinding(sort_speed_toggle, FilterSortCondition.SPEED)
        sortGroup.addBinding(sort_heal_toggle, FilterSortCondition.HEAL)


        attackTypeGroup = CheckedGroup()
        attackTypeGroup.apply {
            addBinding(attack_type_toggle_sever, KinsectAttackType.SEVER)
            addBinding(attack_type_toggle_blunt, KinsectAttackType.BLUNT)
        }

        dustEffectGroup = CheckedGroup()
        dustEffectGroup.apply {
            addBinding(toggle_poison, KinsectDustEffect.POISON)
            addBinding(toggle_paralysis, KinsectDustEffect.PARALYSIS)
            addBinding(toggle_heal, KinsectDustEffect.HEAL)
            addBinding(toggle_blast, KinsectDustEffect.BLAST)
        }

        // Implement actions
        // Implement actions
        action_clear.setOnClickListener {
            applyState(FilterState.default)
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
                isFinalOnly = final_toggle.isChecked,
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
        final_toggle.isChecked = state.isFinalOnly

        // Set the basic group values
        sortGroup.setValue(state.sortBy)
        attackTypeGroup.setValues(state.attackTypes)
        dustEffectGroup.setValues(state.dustEffects)
    }
}