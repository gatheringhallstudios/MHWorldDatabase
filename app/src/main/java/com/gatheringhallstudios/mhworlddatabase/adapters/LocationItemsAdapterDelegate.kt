package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import kotlinx.android.synthetic.main.listitem_reward.*

// todo: update after location item refactor
class LocationItemsAdapterDelegate(private val onSelected: (LocationItem) -> Unit) : SimpleListDelegate<LocationItem>() {

    override fun isForViewType(obj: Any) = obj is LocationItem

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_reward, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: LocationItem) {
        // todo: set icon
        viewHolder.reward_name.text = data.item_name
        viewHolder.reward_stack.text =  viewHolder.resources.getString(R.string.quantity, data.data.stack)
        viewHolder.reward_percent.text = viewHolder.resources.getString(R.string.percentage, data.data.percentage)

        viewHolder.itemView.setOnClickListener { onSelected(data) }
    }
}
