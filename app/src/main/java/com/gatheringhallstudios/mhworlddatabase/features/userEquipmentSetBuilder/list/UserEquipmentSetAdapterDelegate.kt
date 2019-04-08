package com.gatheringhallstudios.mhworlddatabase.features.userEquipmentSetBuilder.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Item
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet

class UserEquipmentSetAdapterDelegate(private val onSelect: (Item) -> Unit): SimpleListDelegate<Item>() {
    override fun isForViewType(obj: Any) = obj is UserEquipmentSet

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_user_equipment_set, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Item) {
        with (viewHolder.itemView as IconLabelTextCell) {
            val icon = AssetLoader.loadIconFor(data)
            setLeftIconDrawable(icon)
            setLabelText(data.name)
        }

        viewHolder.itemView.setOnClickListener { onSelect(data) }
    }
}
