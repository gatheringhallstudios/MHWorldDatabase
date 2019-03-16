package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.features.weapons.WeaponTreeAdapter
import com.gatheringhallstudios.mhworlddatabase.getRouter


/**
 * Fragment that displays the MHModelTree object.
 * This displays the weapons of a particular weapon type as a tree.
 */
class WeaponTreeListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(WeaponTreeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        val adapter = WeaponTreeAdapter(AppSettings.showTrueAttackValues) {
            getRouter().navigateWeaponDetail(it.id)
        }
        setAdapter(adapter)

        viewModel.nodeListData.observe(this, Observer {
            adapter.setItems(it ?: emptyList())
        })

    }
}