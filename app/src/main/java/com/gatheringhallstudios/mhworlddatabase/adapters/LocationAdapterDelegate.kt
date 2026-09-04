package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.components.applyIconType
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemLargeBinding

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
        val binding = ListitemLargeBinding.bind(viewHolder.itemView)
        val icon = AssetLoader.loadIconFor(data)
        binding.itemIcon.applyIconType(IconType.PAPER)
        binding.itemIcon.setImageDrawable(icon)
        binding.itemName.text = data.name
        viewHolder.itemView.setOnClickListener { onSelected(data) }
    }
}
