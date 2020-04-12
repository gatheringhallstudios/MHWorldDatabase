package com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.Tool
import com.gatheringhallstudios.mhworlddatabase.features.workshop.UserEquipmentCard

class WorkshopToolSelectorAdapter(private val onSelected: (Tool) -> Unit) : SimpleRecyclerViewAdapter<Tool>() {

    val TAG = this.javaClass.simpleName

    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Tool) {
        val card = UserEquipmentCard(viewHolder.itemView as ExpandableCardView)
        card.bindTool(data, onClick = { onSelected(data) }, onSwipeRight = null)
        card.populateSlots(data.slots)
    }
}
