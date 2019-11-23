package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import kotlinx.android.synthetic.main.fragment_generic_pager.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager_list.disableScroll(true)
    }

    override fun onAddTabs(tabs: TabAdder) {
        val args = arguments
        val equipmentSet = args!!.getSerializable(ARG_USER_EQUIPMENT_SET) as UserEquipmentSet

        viewModel = ViewModelProviders.of(this).get(UserEquipmentSetDetailViewModel::class.java)

        if (equipmentSet.id != viewModel.activeUserEquipmentSet.value?.id) {
            viewModel.activeUserEquipmentSet.value = equipmentSet
        }

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
