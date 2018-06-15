package com.gatheringhallstudios.mhworlddatabase.features.items

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.ItemAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ItemDao
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView
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
        findNavController().navigate(
                R.id.itemDetailDestination,
                BundleBuilder().putInt(ItemDetailPagerFragment.ARG_ITEM_ID, it.id).build())
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        var category: ItemCategory? = ItemCategory.MATERIAL
        val args = arguments;
        if (args != null) {
            category = args.getSerializable(ARG_CATEGORY) as ItemCategory?
        }

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