package com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponFull
import com.gatheringhallstudios.mhworlddatabase.features.workshop.UserEquipmentCard


class WorkshopWeaponSelectorAdapter(private val onSelected: (WeaponFull) -> Unit) : SimpleRecyclerViewAdapter<WeaponFull>() {

    val TAG = this.javaClass.simpleName

    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: WeaponFull) {
        val card = UserEquipmentCard(viewHolder.itemView as ExpandableCardView)
        card.bindWeapon(data, onClick = { onSelected(data) }, onSwipeRight = null)
        card.populateSlots(data.weapon.slots)
    }
}
