package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.assets.getAssetDrawable
import kotlinx.android.synthetic.main.listitem_large.*
import kotlinx.android.synthetic.main.listitem_large.view.*

/**
 * An adapter delegate used to display a list of locations
 */
class LocationAdapterDelegate(private val onSelected: (Location) -> Unit): SimpleListDelegate<Location>() {

    override fun isForViewType(obj: Any) = obj is Location

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_large, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Location) {
        val icon = viewHolder.assetLoader.loadIconFor(data)
        viewHolder.item_icon.setImageDrawable(icon)
        viewHolder.item_name.text = data.name
        viewHolder.itemView.setOnClickListener { onSelected(data) }
    }
}
