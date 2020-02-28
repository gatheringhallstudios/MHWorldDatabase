package com.gatheringhallstudios.mhworlddatabase.features.kinsects.detail

import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.pager.BasePagerFragment

class KinsectDetailPagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_KINSECT_ID = "KINSECT"
    }

    /**
     * Returns the viewmodel owned by this parent fragment
     */
    private val viewModel: KinsectDetailViewModel by lazy {
        ViewModelProviders.of(this).get(KinsectDetailViewModel::class.java)
    }

    override fun onAddTabs(tabs: TabAdder) {
        val kinsectId = arguments?.getInt(ARG_KINSECT_ID) ?: -1
        viewModel.loadKinsect(kinsectId)

        tabs.addTab(R.string.tab_kinsect_detail) { KinsectDetailFragment() }
        tabs.addTab(R.string.tab_kinsect_tree) { KinsectDetailFamilyFragment() }
    }
}