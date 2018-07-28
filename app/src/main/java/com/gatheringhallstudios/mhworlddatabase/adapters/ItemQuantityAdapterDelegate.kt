package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity
import com.gatheringhallstudios.mhworlddatabase.getRouter

/**
 * Adapter delegate used to render ItemQuantity models.
 * Non-configurable, always navigates to item
 */
class ItemQuantityAdapterDelegate : SimpleListDelegate<ItemQuantity>() {

    override fun isForViewType(obj: Any) = obj is ItemQuantity

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ItemQuantity) {
        with (viewHolder.itemView as IconLabelTextCell) {
            val icon = AssetLoader.loadIconFor(data.item)
            setLeftIconDrawable(icon)
            setLabelText(data.item.name)
            setValueText("x${data.quantity}")
        }

        viewHolder.itemView.setOnClickListener { it.getRouter().navigateItemDetail(data.item.id) }
    }
}
