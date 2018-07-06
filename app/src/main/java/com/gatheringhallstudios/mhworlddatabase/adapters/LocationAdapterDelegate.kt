package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.getAssetDrawable

import kotlinx.android.synthetic.main.listitem_large.view.*

class LocationAdapterDelegate(private val onSelected: (LocationView) -> Unit)
    : SimpleListDelegate<LocationView, View>() {

    override fun getDataClass() = LocationView::class

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_large, parent, false)
    }

    override fun bindView(view: View, data: LocationView) {
        val ctx = view.context
        val path = "locations/${data.id}.jpg"

        val icon = ctx.getAssetDrawable(path)
        view.item_icon.setImageDrawable(icon)
        view.item_name.text = data.name
        view.setOnClickListener { onSelected(data) }
    }
}
