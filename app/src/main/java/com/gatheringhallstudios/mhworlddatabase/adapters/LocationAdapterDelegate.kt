package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.getAssetDrawable
import kotlinx.android.synthetic.main.listitem_location.view.*

import kotlinx.android.synthetic.main.listitem_monster.view.*

class LocationAdapterDelegate(private val onSelected: (LocationView) -> Unit)
    : SimpleListDelegate<LocationView, View>() {

    override fun getDataClass() = LocationView::class

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_location, parent, false)
    }

    override fun bindView(view: View, data: LocationView) {
        val ctx = view.context
        val defaultIcon = R.drawable.question_mark_grey
        //Because the location screenshot is not available in the database
        val path : String = "locations/" + data.name?.replace(" ", "-")?.toLowerCase() + ".jpg";

        val icon = ctx.getAssetDrawable(path, defaultIcon)

        view.location_icon.setImageDrawable(icon)
        view.location_name.text = data.name

        view.setOnClickListener { onSelected(data) }
    }
}
