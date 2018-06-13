package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

class ItemAdapterDelegate(private val onSelect: (ItemView) -> Unit)
    : SimpleListDelegate<ItemView, View>() {

    override fun getDataClass() = ItemView::class

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: View, data: ItemView) {
        with (view as IconLabelTextCell) {
            setLabelText(data.name)
        }

        view.setOnClickListener { onSelect(data) }
    }
}
