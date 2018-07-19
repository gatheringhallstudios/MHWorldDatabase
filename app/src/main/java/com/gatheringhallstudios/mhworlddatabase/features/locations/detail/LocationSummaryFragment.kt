package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.assets.getAssetDrawable
import kotlinx.android.synthetic.main.fragment_location_summary.*

/**
 * Fragment for displaying Location Summary
 */
class LocationSummaryFragment : Fragment() {

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

    private fun populateLocation(location: Location?) {
        if(location == null) return

        val ctx = view?.context
        val defaultIcon = R.drawable.ic_question_mark
        //Because the location screenshot is not available in the database
        val path : String = "locations/" + location.name?.replace(" ", "-")?.toLowerCase() + ".jpg";
        val icon = ctx?.getAssetDrawable(path, defaultIcon)

        location_name.text = location.name
        location_icon.setImageDrawable(icon)

    }
}