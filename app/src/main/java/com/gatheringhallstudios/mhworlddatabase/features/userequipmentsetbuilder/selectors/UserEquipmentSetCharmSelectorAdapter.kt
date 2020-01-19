package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmFull
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.UserEquipmentCard

class UserEquipmentSetCharmSelectorAdapter(private val onSelected: (CharmFull) -> Unit) : SimpleRecyclerViewAdapter<CharmFull>() {

    val TAG = this.javaClass.simpleName

    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: CharmFull) {
        val charm = data
32
        val card = UserEquipmentCard(viewHolder.itemView as ExpandableCardView)
        card.bindCharm(charm, onClick = { onSelected(data) }, onSwipeRight = null)
    }
}
