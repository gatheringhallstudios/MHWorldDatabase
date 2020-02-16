package com.gatheringhallstudios.mhworlddatabase.features.workshop.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.features.workshop.UserEquipmentSetViewModel
import com.gatheringhallstudios.mhworlddatabase.util.pager.BasePagerFragment
import kotlinx.android.synthetic.main.fragment_generic_pager.*

/**
 * Monster detail Hub. Displays information for a single monster.
 * All data is displayed in separate tabs.
 */

class WorkshopDetailPagerFragment : BasePagerFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(UserEquipmentSetViewModel::class.java)
    }

    companion object {
        const val ARG_USER_EQUIPMENT_SET_ID = "USER_EQUIPMENT_SET_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager_list.disableScroll(true)
    }

    override fun onAddTabs(tabs: TabAdder) {
        val args = arguments
        val equipmentSetId = args!!.getInt(ARG_USER_EQUIPMENT_SET_ID)
        viewModel.setActiveUserEquipmentSet(equipmentSetId)

        // Now add our tabs
        tabs.addTab(getString(R.string.tab_workshop_builder_equipment)) {
            WorkshopEditFragment()
        }
        tabs.addTab(getString(R.string.tab_workshop_builder_summary)) {
            WorkshopSummaryFragment()
        }
    }
}
