package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.content.Intent
import android.os.Bundle
import android.sax.Element
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.fragment.app.DialogFragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.fragment_weapon_filter.*

class WeaponFilterFragment : DialogFragment() {
    companion object {
        const val FILTER_STATE = "FILTER_STATE"

        @JvmStatic fun newInstance(state: FilterState)
                = WeaponFilterFragment().applyArguments {
                    putSerializable(FILTER_STATE, state)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // makes the dialog into a full screen one
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weapon_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // fake radio group for sort objects
        val group = listOf(sort_attack_toggle, sort_affinity_toggle)

        fun handleSortChange(buttonView: Checkable, isChecked: Boolean) {
            if (!isChecked) return

            for (toggle in group) {
                if (toggle != buttonView) {
                    toggle.isChecked = false
                }
            }
        }

        // Bind sort toggle behaviors
        sort_attack_toggle.setOnCheckedChangeListener(::handleSortChange)
        sort_affinity_toggle.setOnCheckedChangeListener(::handleSortChange)

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

        // Apply state from bundle
        val state = arguments?.getSerializable(FILTER_STATE) as? FilterState
        if (state != null) {
            applyState(state)
        }
    }

    /**
     * Returns the current state, received by
     */
    fun calculateState(): FilterState {
        val elements = mutableSetOf<ElementStatus>()
        fun addIfChecked(item: Checkable, element: ElementStatus) {
            if (item.isChecked) elements.add(element)
        }
        addIfChecked(toggle_fire, ElementStatus.FIRE)
        addIfChecked(toggle_water, ElementStatus.WATER)
        addIfChecked(toggle_thunder, ElementStatus.THUNDER)
        addIfChecked(toggle_ice, ElementStatus.ICE)
        addIfChecked(toggle_dragon, ElementStatus.DRAGON)
        addIfChecked(toggle_poison, ElementStatus.POISON)
        addIfChecked(toggle_sleep, ElementStatus.SLEEP)
        addIfChecked(toggle_paralysis, ElementStatus.PARALYSIS)
        addIfChecked(toggle_blast, ElementStatus.BLAST)

        return FilterState(
                isFinalOnly = final_toggle.isChecked,
                sortBy = when {
                    sort_attack_toggle.isChecked -> FilterSortCondition.ATTACK
                    sort_affinity_toggle.isChecked -> FilterSortCondition.AFFINITY
                    else -> FilterSortCondition.NONE
                },
                elements = elements
        )
    }

    /**
     * Applies a FilterState to the current UI.
     */
    fun applyState(state: FilterState) {
        // handle final
        final_toggle.isChecked = state.isFinalOnly

        // handle elements
        val elementMapping = mapOf(
                ElementStatus.FIRE to toggle_fire,
                ElementStatus.WATER to toggle_water,
                ElementStatus.THUNDER to toggle_thunder,
                ElementStatus.ICE to toggle_ice,
                ElementStatus.DRAGON to toggle_dragon,
                ElementStatus.POISON to toggle_poison,
                ElementStatus.SLEEP to toggle_sleep,
                ElementStatus.PARALYSIS to toggle_paralysis,
                ElementStatus.BLAST to toggle_blast
        )
        for ((element, checkable) in elementMapping) {
            checkable.isChecked = element in state.elements
        }

        // handle sorts
        sort_attack_toggle.isChecked = (state.sortBy == FilterSortCondition.ATTACK)
        sort_affinity_toggle.isChecked = (state.sortBy == FilterSortCondition.AFFINITY)
    }
}