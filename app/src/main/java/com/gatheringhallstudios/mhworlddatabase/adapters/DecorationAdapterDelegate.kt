package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.DecorationBase

class DecorationAdapterDelegate(private val onSelected: (DecorationBase) -> Unit)
    : SimpleListDelegate<DecorationBase>() {

    val TAG = this.javaClass.simpleName

    override fun isForViewType(obj: Any) = obj is DecorationBase

    override fun onCreateView(parent: ViewGroup): IconLabelTextCell {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: DecorationBase) {
        with(viewHolder.itemView as IconLabelTextCell) {
            val icon = AssetLoader.loadIconFor(data)
            setLeftIconDrawable(icon)
            setLabelText(data.name)
        }

        viewHolder.itemView.setOnClickListener { onSelected(data) }
    }
}
