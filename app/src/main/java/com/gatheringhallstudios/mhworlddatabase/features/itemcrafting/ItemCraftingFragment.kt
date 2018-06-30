package com.gatheringhallstudios.mhworlddatabase.features.itemcrafting

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemCraftingAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase

/**
 * Defines a fragment that lists item combinations.
 * This fragment acts as the main screen to list all item combinations
 */
class ItemCraftingFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ItemCombinationsViewModel::class.java)
    }

    private val adapter = BasicListDelegationAdapter(ItemCraftingAdapterDelegate())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.combinationListData.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        // Add dividers between items
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    class ItemCombinationsViewModel(app: Application) : AndroidViewModel(app) {
        private val dao = MHWDatabase.getDatabase(app).itemDao()
        val combinationListData = dao.loadItemCombinations(AppSettings.dataLocale)
    }
}