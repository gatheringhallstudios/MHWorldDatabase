package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemLocationView
import kotlinx.android.synthetic.main.listitem_reward.view.*

/**
 * Renders list items for item location information
 */
private class ItemLocationAdapterDelegate : SimpleListDelegate<ItemLocationView, View>() {
    override fun getDataClass() = ItemLocationView::class

    override fun onCreateView(parent: ViewGroup): View {
        // todo: refactor listitem_reward into a general view
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_reward, parent, false)
    }

    override fun bindView(view: View, data: ItemLocationView) {
        view.reward_name.text = view.resources.getString(R.string.location_area, data.data.area)
        view.reward_stack.text = "x ${data.data.stack}"
        view.reward_percent.text = "${data.data.percentage}%"
    }
}

class ItemLocationsFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ItemDetailViewModel::class.java)
    }

    val adapter = CategoryAdapter(ItemLocationAdapterDelegate())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.itemLocations.observe(this, Observer(::populateLocations))
    }

    private fun populateLocations(locations : List<ItemLocationView>?) {
        adapter.clear()

        if (locations == null) {
            adapter.notifyDataSetChanged()
            return
        }

        val groups = locations.groupBy {
            "${it.data.rank} ${it.location_name}"
        }

        for (group in groups) {
            adapter.addSubSection(group.key, group.value)
        }

        adapter.notifyDataSetChanged()
    }
}