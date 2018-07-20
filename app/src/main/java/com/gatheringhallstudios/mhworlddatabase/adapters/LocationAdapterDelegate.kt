package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.assets.getAssetDrawable
import kotlinx.android.synthetic.main.listitem_large.view.*

class LocationAdapterDelegate(private val onSelected: (Location) -> Unit)
    : SimpleListDelegate<Location, View>() {

    override fun getDataClass() = Location::class

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_large, parent, false)
    }

    override fun bindView(view: View, data: Location) {
        val icon = view.assetLoader.loadIconFor(data)
        view.item_icon.setImageDrawable(icon)
        view.item_name.text = data.name
        view.setOnClickListener { onSelected(data) }
    }
}
