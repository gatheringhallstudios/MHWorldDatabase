package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.getAssetDrawable
import kotlinx.android.synthetic.main.fragment_location_summary.*

/**
 * Fragment for displaying Location Summary
 */
class LocationSummaryFragment : androidx.fragment.app.Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(LocationDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstance: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_summary, parent, false)
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.location.observe(this, Observer(::populateLocation))
    }

    private fun populateLocation(location: LocationView?) {
        if(location == null) return

        val ctx = view?.context
        val defaultIcon = R.drawable.question_mark_grey
        //Because the location screenshot is not available in the database
        val path : String = "locations/" + location.name?.replace(" ", "-")?.toLowerCase() + ".jpg";
        val icon = ctx?.getAssetDrawable(path, defaultIcon)

        location_name.text = location.name
        location_icon.setImageDrawable(icon)

    }
}