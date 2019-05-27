package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.HeaderItemDivider
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Carlos on 3/22/2018.
 */

class UserEquipmentSetListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(UserEquipmentSetListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getEquipmentSetList()

        // Add dividers between items
        recyclerView.addItemDecoration(HeaderItemDivider(DashedDividerDrawable(context!!)))

        viewModel.userEquipmentSets.observe(this, Observer<MutableList<UserEquipmentSet>> {
            // Setup recycler list adapter and the on-selected
            if (!containsEmptyElement(it)) {
                it.add(UserEquipmentSet.createEmptySet())
            }

            val adapter = UserEquipmentSetAdapterDelegate(it) {
                if (it.id == 0) { //Set has not yet been created
                    GlobalScope.launch(Dispatchers.Main) {
                        val equipmentSet = withContext(Dispatchers.IO) { viewModel.createEquipmentSet() }
                        getRouter().navigateUserEquipmentSetDetail(equipmentSet)
                    }
                } else {
                    getRouter().navigateUserEquipmentSetDetail(it)
                }
            }

            this.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.title_armor_set_builder)
    }

    private fun containsEmptyElement(list: MutableList<UserEquipmentSet>): Boolean {
        return list.last().id == 0
    }
}
