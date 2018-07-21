package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import kotlinx.android.synthetic.main.listitem_reward.view.*

class LocationItemsAdapterDelegate(private val onSelected: (LocationItem) -> Unit)
    : SimpleListDelegate<LocationItem, View>() {

    override fun isForViewType(obj: Any) = obj is LocationItem

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_reward, parent, false)
    }

    override fun bindView(view: View, data: LocationItem) {
        val ctx = view.context
        val defaultIcon = R.drawable.ic_question_mark

        view.reward_name.text = data.item_name
        view.reward_stack.text =  "x ${data.data.stack}"
        view.reward_percent.text = "${data.data.percentage}%"

        view.setOnClickListener { onSelected(data) }
    }
}
