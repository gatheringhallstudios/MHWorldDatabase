package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationItemView
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.getAssetDrawable
import kotlinx.android.synthetic.main.listitem_location.view.*

import kotlinx.android.synthetic.main.listitem_monster.view.*
import kotlinx.android.synthetic.main.listitem_reward.view.*

class LocationItemsAdapterDelegate(private val onSelected: (LocationItemView) -> Unit)
    : SimpleListDelegate<LocationItemView, View>() {

    override fun getDataClass() = LocationItemView::class

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_reward, parent, false)
    }

    override fun bindView(view: View, data: LocationItemView) {
        val ctx = view.context
        val defaultIcon = R.drawable.question_mark_grey

        view.reward_name.text = data.item_name
        view.reward_stack.text = data.data.stack.toString()
        view.reward_percent.text = data.data.stack.toString()
        view.setOnClickListener { onSelected(data) }
    }
}
