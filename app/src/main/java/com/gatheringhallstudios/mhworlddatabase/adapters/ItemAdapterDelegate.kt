package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Item

class ItemAdapterDelegate(private val onSelect: (Item) -> Unit)
    : SimpleListDelegate<Item, View>() {

    override fun getDataClass() = Item::class

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: View, data: Item) {
        with (view as IconLabelTextCell) {
            val icon = AssetLoader(view.context).loadItemIcon(data)
            setLeftIconDrawable(icon)
            setLabelText(data.name)
        }

        view.setOnClickListener { onSelect(data) }
    }
}
