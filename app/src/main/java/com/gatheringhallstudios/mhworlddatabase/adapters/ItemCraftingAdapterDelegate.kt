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
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemItemCraftingBinding

/**
 * Defines an adapter delegate for a list of item combinations
 */
class ItemCraftingAdapterDelegate : SimpleListDelegate<ItemCombination>() {
    override fun isForViewType(obj: Any) = obj is ItemCombination

    override fun onCreateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.listitem_item_crafting, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ItemCombination) {
        val binding = ListitemItemCraftingBinding.bind(viewHolder.itemView)
        binding.resultIcon.setImageDrawable(AssetLoader.loadIconFor(data.result))
        binding.resultName.text = data.result.name

        binding.item1Icon.setImageDrawable(AssetLoader.loadIconFor(data.first))
        binding.item1Name.text = data.first.name

        binding.item2View.visibility = View.GONE
        if (data.second != null) {
            binding.item2View.visibility = View.VISIBLE
            binding.item2Icon.setImageDrawable(AssetLoader.loadIconFor(data.second))
            binding.item2Name.text = data.second.name
        }

        binding.yieldLabel.text = viewHolder.resources.getString(R.string.item_crafting_yield, data.quantity)

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateItemDetail(data.result.id)
        }

        binding.item1View.setOnClickListener {
            it.getRouter().navigateItemDetail(data.first.id)
        }

        if (data.second != null) {
            binding.item2View.setOnClickListener {
                it.getRouter().navigateItemDetail(data.second.id)
            }
        } else {
            binding.item2View.setOnClickListener(null)
        }
    }
}