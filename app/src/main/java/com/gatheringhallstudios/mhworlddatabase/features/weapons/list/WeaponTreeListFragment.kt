package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle

class WeaponTreeListFragment : RecyclerViewFragment() {
    companion object {
        const val ARG_WEAPON_TREE_NAME = "WEAPON_TREE_ID"
    }

    // Setup adapter and navigation
    private val adapter = BasicListDelegationAdapter(WeaponTreeListAdapterDelegate {
//        getRouter().navigateLocationDetail(it.id)
    })

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WeaponTreeListViewModel::class.java)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setActivityTitle(arguments?.getString(ARG_WEAPON_TREE_NAME))
        setAdapter(adapter)
        // Load data
        viewModel.initializeTree()
        adapter.items = viewModel.weaponPaths.flatten().distinct()
        adapter.notifyDataSetChanged()
    }
}