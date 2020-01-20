package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorFull
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.UserEquipmentCard


class UserEquipmentSetArmorSelectorAdapter(private val onSelected: (ArmorFull) -> Unit) : SimpleRecyclerViewAdapter<ArmorFull>() {

    val TAG = this.javaClass.simpleName

    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ArmorFull) {
        val card = UserEquipmentCard(viewHolder.itemView as ExpandableCardView)
        card.bindArmor(data, onClick = { onSelected(data) }, onSwipeRight = null)
        card.populateSlots(data.armor.slots)
    }
}
