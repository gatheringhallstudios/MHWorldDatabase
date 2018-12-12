package com.gatheringhallstudios.mhworlddatabase.adapters

import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemCombination
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.listitem_item_crafting.*
import kotlinx.android.synthetic.main.listitem_item_crafting.view.*

/**
 * Defines an adapter delegate for a list of item combinations
 */
class ItemCraftingAdapterDelegate : SimpleListDelegate<ItemCombination>() {
    override fun isForViewType(obj: Any) = obj is ItemCombination

    override fun onCreateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.listitem_item_crafting, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ItemCombination) {
        viewHolder.result_icon.setImageDrawable(AssetLoader.loadIconFor(data.result))
        viewHolder.result_name.text = data.result.name

        viewHolder.item1_icon.setImageDrawable(AssetLoader.loadIconFor(data.first))
        viewHolder.item1_name.text = data.first.name

        viewHolder.item2_view.visibility = View.GONE
        if (data.second != null) {
            viewHolder.item2_view.visibility = View.VISIBLE
            viewHolder.item2_icon.setImageDrawable(AssetLoader.loadIconFor(data.second))
            viewHolder.item2_name.text = data.second.name
        }

        viewHolder.yield_label.text = viewHolder.resources.getString(R.string.item_crafting_yield, data.quantity)

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateItemDetail(data.result.id)
        }

        viewHolder.item1_view.setOnClickListener {
            it.getRouter().navigateItemDetail(data.first.id)
        }

        if (data.second != null) {
            viewHolder.item2_view.setOnClickListener {
                it.getRouter().navigateItemDetail(data.second.id)
            }
        } else {
            viewHolder.item2_view.setOnClickListener(null)
        }
    }
}