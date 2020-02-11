package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.UserEquipmentCard


class UserEquipmentSetDecorationSelectorAdapter(private val onSelected: (Decoration) -> Unit) : SimpleRecyclerViewAdapter<Decoration>() {

    val TAG = this.javaClass.simpleName
    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Decoration) {
        val card = UserEquipmentCard(viewHolder.itemView as ExpandableCardView)
        card.bindDecoration(data, onClick = { onSelected(data) })
    }
}
