package com.gatheringhallstudios.mhworlddatabase.features.items

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ItemDao
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

class ItemListFragment : RecyclerViewFragment() {
    companion object {
        private val ARG_CATEGORY = "CATEGORY"

        @JvmStatic
        fun newInstance(category: ItemCategory): ItemListFragment {
            val f = ItemListFragment()
            f.arguments = BundleBuilder().putSerializable(ARG_CATEGORY, category).build()
            return f
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ItemListFragment.ViewModel::class.java)
    }

    // Setup recycler list adapter and the on-selected
    private val adapter = BasicListDelegationAdapter(ItemAdapterDelegate(onSelect = {
        getRouter().navigateItemDetail(it.id)
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        val category = arguments?.getSerializable(ARG_CATEGORY) as ItemCategory?

        viewModel.init(category)

        viewModel.items.observe(this, Observer({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))
    }

    // ViewModel class used by this Fragment
    class ViewModel(application: Application) : AndroidViewModel(application) {
        private val dao: ItemDao = MHWDatabase.getDatabase(application).itemDao()
        lateinit var items: LiveData<List<ItemView>> private set


        fun init(category: ItemCategory?) {
            if (!::items.isInitialized) {
                items = dao.loadItems("en", category)
            }
        }
    }
}