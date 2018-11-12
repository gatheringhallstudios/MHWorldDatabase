package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle

/**
 * Fragment that displays the WeaponTreeCollection object.
 * This displays the weapons of a particular weapon type as a tree.
 */
class WeaponTreeListFragment : RecyclerViewFragment() {
    companion object {
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
        val type = arguments?.getSerializable(ARG_WEAPON_TREE_TYPE) as WeaponType

        setActivityTitle(AssetLoader.getNameFor(type))
        setAdapter(adapter)

        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        // Load data
        viewModel.setWeaponType(type)
        adapter.items = viewModel.weaponTree.weaponPaths
        adapter.notifyDataSetChanged()
    }
}