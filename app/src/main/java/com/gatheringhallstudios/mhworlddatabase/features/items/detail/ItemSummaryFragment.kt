package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Item
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat

import kotlinx.android.synthetic.main.fragment_item_summary.*

/**
 * Binds item data values from the itemView object to the view
 */
private fun evaluateValue(value: Int?) = when(value) {
    0, null -> "-"
    else -> value.toString()
}

class ItemSummaryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_summary, parent, false)

        val viewmodel = ViewModelProviders.of(parentFragment!!).get(ItemDetailViewModel::class.java)
        viewmodel.item.observe(this, Observer(::populateItem))

        return view
    }

    private fun populateItem(item: Item?) {
        if (item == null) return

        setActivityTitle(item.name)

        //Set the summary information
        item_header.setIconDrawable(AssetLoader.loadIconFor(item))
        item_header.setTitleText(item.name)
        item_header.setDescriptionText(item.description)

        rarity_value.setTextColor(AssetLoader.loadRarityColor(item.rarity))
        rarity_value.text = getString(R.string.rarity_string, item.rarity)

        buy_price_value.text = evaluateValue(item.buy_price)
        carry_capacity_value.text = evaluateValue(item.carry_limit)

        // Set sell value. Swaps to research points if research points are available
        if (item.sell_price == 0 && item.points > 0) {
            sell_price_value.text = item.points.toString()
            sell_price_icon.setImageDrawable(context?.getDrawableCompat(R.drawable.ic_ui_research_points))
        } else {
            sell_price_value.text = evaluateValue(item.sell_price)
        }

    }
}
