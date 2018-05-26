package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView
import kotlinx.android.synthetic.main.listitem_monster.view.*

class ItemAdapterDelegate(private val onSelect: (ItemView) -> Unit)
    : SimpleListDelegate<ItemView, View>() {

    override fun getDataClass() = ItemView::class

    override fun onCreateView(parent: ViewGroup): View {
        // todo: create item listitem layout
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_monster, parent, false)
    }

    override fun bindView(view: View, data: ItemView) {
        view.monster_name.text = data.name
        view.setOnClickListener { onSelect(data) }
    }
}
