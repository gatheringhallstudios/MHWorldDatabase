package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.EmptyStateAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.EmptyState
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.DetailHeaderCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.listitem_reward.*

/**
 * A wrapper for an all-in-one location detail that contains the location and the gatherable items.
 * Updating
 */
class LocationDetailAdapterWrapper {
    private var location: Location? = null
    private var items: MutableMap<String, List<LocationItem>>? = null

    val adapter = CategoryAdapter(
            LocationHeaderAdapterDelegate(),
            LocationItemsAdapterDelegate(),
            EmptyStateAdapterDelegate()
    )

    fun bindLocation(location: Location) {
        this.location = location
        rebuild()
    }

    fun bindEmpty() {}


    /**
     * Binds a list of items to the location detail, using the context
     * to retrieve area string translation
     */
    fun bindItems(context: Context, items: List<LocationItem>) {
        val grouped = items.groupBy { it.area }

        val newItems = mutableMapOf<String, List<LocationItem>>()
        for ((area, areaItems) in grouped) {
            newItems[context.getString(R.string.location_area, area)] = areaItems
        }
        this.items = newItems
        rebuild()
    }

    /**
     * Builds the list. Does nothing
     */
    private fun rebuild() {
        adapter.clear()

        if (location == null || items == null) {
            return
        }

        adapter.addSection(listOf(location!!))
        adapter.addSections(items!!)
    }
}

class LocationHeaderAdapterDelegate : SimpleListDelegate<Location>() {
    override fun isForViewType(obj: Any) = obj is Location

    override fun onCreateView(parent: ViewGroup): View {
        return DetailHeaderCell(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Location) {
        with (viewHolder.itemView as DetailHeaderCell) {
            setIconType(IconType.PAPER)
            setIconDrawable(AssetLoader.loadIconFor(data))
            setTitleText(data.name)
        }
    }
}

class LocationItemsAdapterDelegate : SimpleListDelegate<LocationItem>() {
    override fun isForViewType(obj: Any) = obj is LocationItem

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_reward, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: LocationItem) {
        viewHolder.reward_icon.setImageDrawable(AssetLoader.loadIconFor(data.item))
        viewHolder.reward_name.text = data.item.name
        viewHolder.reward_stack.text = viewHolder.resources.getString(R.string.quantity, data.stack)
        viewHolder.reward_percent.text = viewHolder.resources.getString(R.string.percentage, data.percentage)

        viewHolder.itemView.setOnClickListener { it.getRouter().navigateItemDetail(data.item.id) }
    }
}
