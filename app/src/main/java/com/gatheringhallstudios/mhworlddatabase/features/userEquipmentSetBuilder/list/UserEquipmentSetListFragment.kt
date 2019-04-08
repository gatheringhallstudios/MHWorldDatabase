package com.gatheringhallstudios.mhworlddatabase.features.userEquipmentSetBuilder.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.HeaderItemDivider
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet

/**
 * Created by Carlos on 3/22/2018.
 */

class UserEquipmentSetListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(UserEquipmentSetListViewModel::class.java)
    }

    // Setup recycler list adapter and the on-selected
    private val adapter = BasicListDelegationAdapter(UserEquipmentSetAdapterDelegate {
//        getRouter().navigateItemDetail(it.id)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        viewModel.getEquipmentSetList()

        // Add dividers between items
        recyclerView.addItemDecoration(HeaderItemDivider(DashedDividerDrawable(context!!)))

        if (adapter.itemCount == 0) {

            viewModel.userEquipmentSetData.observe(this, Observer<MutableList<UserEquipmentSet>> {
                adapter.items = it.toList()
                if (it.size == 0) {
                    showEmptyView()
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.title_armor_set_builder)
    }
}
