package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.EmptyState
import com.gatheringhallstudios.mhworlddatabase.adapters.EmptyStateAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.DetailHeaderCell
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationCamp
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.DataSynchronizer
import com.gatheringhallstudios.mhworlddatabase.util.DataWatcher
import kotlinx.android.synthetic.main.listitem_reward.*

class LocationData: DataSynchronizer() {
    var location: Location by DataWatcher(this)
    var items: Map<String, List<LocationItem>> by DataWatcher(this)
    var camps: List<LocationCamp> by DataWatcher(this)
}

/**
 * A wrapper for an all-in-one location detail that contains the location and the gatherable items.
 * Will not do anything unless ALL data is binded.
 */
class LocationDetailAdapterWrapper {
    private val data = LocationData()

    private var campTitle: String = ""

    val adapter = CategoryAdapter(
            LocationHeaderAdapterDelegate(),
            LocationCampAdapterDelegate(),
            LocationItemsAdapterDelegate(),
            EmptyStateAdapterDelegate()
    )

    fun bindLocation(location: Location) {
        data.location = location
        buildAdapter()
    }

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
        data.items = newItems
        buildAdapter()
    }

    fun bindCamps(campTitle: String, camps: List<LocationCamp>) {
        this.campTitle = campTitle
        data.camps = camps
        buildAdapter()
    }

    /**
     * Builds the list. Does nothing until data is available.
     */
    private fun buildAdapter() {
        adapter.clear()

        if (!data.allInitialized) {
            return
        }

        adapter.addSection(listOf(data.location))

        if (data.camps.isNotEmpty()) {
            adapter.addSection(campTitle, data.camps)
        }

        if (data.items.isEmpty()) {
            // if empty, add the "empty" message
            adapter.addSection(listOf(EmptyState()))
        } else {
            // if not empty, add the items
            adapter.addSections(data.items)
        }
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

class LocationCampAdapterDelegate : SimpleListDelegate<LocationCamp>() {
    override fun isForViewType(obj: Any) = obj is LocationCamp

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: LocationCamp) {
        with(viewHolder.itemView as IconLabelTextCell) {
            setLeftIconDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ui_camp))
            setLabelText(data.name)
            setValueText(viewHolder.context.getString(R.string.location_area, data.area))
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
        viewHolder.reward_stack.text = viewHolder.resources.getString(R.string.format_quantity_x, data.stack)
        viewHolder.reward_percent.text = viewHolder.resources.getString(R.string.format_percentage, data.percentage)

        viewHolder.itemView.setOnClickListener { it.getRouter().navigateItemDetail(data.item.id) }
    }
}
