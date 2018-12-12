package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.features.weapons.WeaponTreeListAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.features.weapons.RenderedTreeNode
import com.gatheringhallstudios.mhworlddatabase.getRouter

/**
 * Fragment used to display the weapon family tab.
 * Weapon families are used to figure out the crafting path.
 */
class WeaponDetailFamilyFragment : RecyclerViewFragment() {
    val adapter = BasicListDelegationAdapter(
            WeaponTreeListAdapterDelegate { getRouter().navigateWeaponDetail(it.id) }
    )

    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: WeaponDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(WeaponDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter(adapter)

        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        viewModel.weaponTrees.observe(this, Observer { tree ->
            if (tree == null) return@Observer

            val familyPath = tree.getWeapon(viewModel.weaponId)?.path
            adapter.items = familyPath?.map { RenderedTreeNode(it.value) } ?: emptyList()
            adapter.notifyDataSetChanged()
        })
    }
}