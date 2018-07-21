package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Item

class ItemAdapterDelegate(private val onSelect: (Item) -> Unit)
    : SimpleListDelegate<Item, View>() {

    override fun isForViewType(obj: Any) = obj is Item

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: View, data: Item) {
        with (view as IconLabelTextCell) {
            val icon = view.assetLoader.loadIconFor(data)
            setLeftIconDrawable(icon)
            setLabelText(data.name)
        }

        view.setOnClickListener { onSelect(data) }
    }
}
