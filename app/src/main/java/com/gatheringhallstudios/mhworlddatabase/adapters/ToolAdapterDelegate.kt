package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.ToolBase
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import kotlinx.android.synthetic.main.listitem_large.*

/**
 * Defines an adapter delegate for weapon types
 */
class ToolAdapterDelegate(private val onSelected: (ToolBase) -> Unit) : SimpleListDelegate<ToolBase>() {
    override fun isForViewType(obj: Any) = obj is ToolBase

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ToolBase) {
        with (viewHolder.itemView as IconLabelTextCell) {
            val icon = AssetLoader.loadIconFor(data)
            setLeftIconDrawable(icon)
            setLabelText(data.name)
        }

        viewHolder.itemView.setOnClickListener { onSelected(data) }
    }
}