package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle

class WeaponTreeListFragment : RecyclerViewFragment() {
    companion object {
        const val ARG_WEAPON_TREE_NAME = "WEAPON_TREE_ID"
        const val ARG_WEAPON_TREE_TYPE = "WEAPON_TREE_TYPE"
    }

    // Setup adapter and navigation
    private val adapter = BasicListDelegationAdapter(WeaponTreeListAdapterDelegate {
        getRouter().navigateWeaponDetail(it.id)
    })

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WeaponTreeListViewModel::class.java)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setActivityTitle(arguments?.getString(ARG_WEAPON_TREE_NAME))
        setAdapter(adapter)

        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        // Load data
        viewModel.setWeaponType( arguments?.getSerializable(ARG_WEAPON_TREE_TYPE) as WeaponType)
        adapter.items = viewModel.weaponPaths
        adapter.notifyDataSetChanged()
    }
}