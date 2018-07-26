package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Item

class ItemAdapterDelegate(private val onSelect: (Item) -> Unit): SimpleListDelegate<Item>() {
    override fun isForViewType(obj: Any) = obj is Item

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Item) {
        with (viewHolder.itemView as IconLabelTextCell) {
            val icon = assetLoader.loadIconFor(data)
            setLeftIconDrawable(icon)
            setLabelText(data.name)
        }

        viewHolder.itemView.setOnClickListener { onSelect(data) }
    }
}
