package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.DecorationBase

class DecorationAdapterDelegate(private val onSelected: (DecorationBase) -> Unit)
    : SimpleListDelegate<DecorationBase, IconLabelTextCell>() {

    val TAG = this.javaClass.simpleName

    override fun isForViewType(obj: Any) = obj is DecorationBase

    override fun onCreateView(parent: ViewGroup): IconLabelTextCell {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: IconLabelTextCell, data: DecorationBase) {
        val icon = AssetLoader(view.context).loadIconFor(data)

        view.setLeftIconDrawable(icon)
        view.setLabelText(data.name)

        view.setOnClickListener { onSelected(data) }
    }
}
