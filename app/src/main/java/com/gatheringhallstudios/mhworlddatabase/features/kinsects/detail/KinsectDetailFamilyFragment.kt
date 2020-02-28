package com.gatheringhallstudios.mhworlddatabase.features.kinsects.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.features.kinsects.KinsectTreeListAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.util.tree.RenderedTreeNode
import com.gatheringhallstudios.mhworlddatabase.util.tree.createTreeRenderList

class KinsectDetailFamilyFragment : RecyclerViewFragment() {
    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: KinsectDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(KinsectDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CategoryAdapter(
                KinsectTreeListAdapterDelegate(
                        onLongSelect = null,
                        onSelected = { getRouter().navigateKinsectDetail(it.id) }
                )
        )
        setAdapter(adapter)

        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))

        viewModel.kinsectFamilyData.observe(this, Observer { data ->
            adapter.clear()
            if (data == null) return@Observer

            val familyNodes = createTreeRenderList(data.familyPath)
            val finalNodes = data.finalKinsects.map { RenderedTreeNode(it) }

            adapter.addSection(familyNodes)
            if (finalNodes.isNotEmpty()) {
                adapter.addSection(getString(R.string.kinsect_final_upgrades), finalNodes)
            }
        })
    }
}