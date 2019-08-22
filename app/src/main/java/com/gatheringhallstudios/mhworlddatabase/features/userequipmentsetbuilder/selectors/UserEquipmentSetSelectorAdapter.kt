package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipment
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.slot1
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.slot2
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.slot3
import kotlinx.android.synthetic.main.listitem_armorset_armor.*

class UserEquipmentSetSelectorAdapter(private val onSelected: (Armor) -> Unit): SimpleRecyclerViewAdapter<Armor>() {

    val TAG = this.javaClass.simpleName

    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Armor) {
        viewHolder.itemView.equipment_name.text = data.name
        viewHolder.itemView.rarity_string.text = viewHolder.resources.getString(R.string.format_rarity, data.rarity)
        viewHolder.itemView.rarity_string.setTextColor(AssetLoader.loadRarityColor(data.rarity))
        viewHolder.itemView.rarity_string.visibility = View.VISIBLE
        viewHolder.itemView.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(data))
        viewHolder.itemView.defense_value.text = viewHolder.resources.getString(
                R.string.armor_defense_value,
               data.defense_base,
                data.defense_max,
                data.defense_augment_max)

        val slotImages = data.slots.map {
            viewHolder.itemView.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        viewHolder.slot1.setImageDrawable(slotImages[0])
        viewHolder.slot2.setImageDrawable(slotImages[1])
        viewHolder.slot3.setImageDrawable(slotImages[2])

        viewHolder.itemView.setOnClickListener {
            onSelected(data)
        }
    }
}
