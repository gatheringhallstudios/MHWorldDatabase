package com.gatheringhallstudios.mhworlddatabase.features.itemcrafting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemCraftingAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
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

        // Add dividers between items
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        viewModel.combinationListData.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    class ItemCombinationsViewModel(app: Application) : AndroidViewModel(app) {
        private val dao = MHWDatabase.getDatabase(app).itemDao()
        val combinationListData = dao.loadItemCombinations(AppSettings.dataLocale)
    }
}