package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterBase
import kotlinx.android.synthetic.main.listitem_large.*

class MonsterAdapterDelegate(private val onSelected: (MonsterBase) -> Unit)
    : SimpleListDelegate<MonsterBase>() {

    override fun isForViewType(obj: Any) = obj is MonsterBase

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_large, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: MonsterBase) {
        val icon = viewHolder.assetLoader.loadIconFor(data)

        viewHolder.item_icon.setImageDrawable(icon)
        viewHolder.item_name.text = data.name

        viewHolder.itemView.setOnClickListener { onSelected(data) }
    }
}
