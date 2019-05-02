package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle

/**
 * Monster detail Hub. Displays information for a single monster.
 * All data is displayed in separate tabs.
 */

class UserEquipmentSetDetailPagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_USER_EQUIPMENT_SET = "USER_EQUIPMENT_SET"
    }

    private lateinit var viewModel : UserEquipmentSetDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val equipmentSet = args!!.getSerializable(ARG_USER_EQUIPMENT_SET) as UserEquipmentSet

        viewModel = ViewModelProviders.of(this).get(UserEquipmentSetDetailViewModel::class.java)
        viewModel.activeUserEquipmentSet.value = equipmentSet

        this.setActivityTitle(equipmentSet.name)

        // Now add our tabs
        tabs.addTab(getString(R.string.tab_armor_set_builder_equipment)) {
            UserEquipmentSetEditFragment()
        }
        tabs.addTab(getString(R.string.tab_armor_set_builder_summary)) {
            UserEquipmentSetSummaryFragment()
        }
    }
}
