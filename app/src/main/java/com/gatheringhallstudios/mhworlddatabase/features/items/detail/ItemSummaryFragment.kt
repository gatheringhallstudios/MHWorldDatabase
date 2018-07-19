package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Item

import kotlinx.android.synthetic.main.fragment_item_summary.*
import kotlinx.android.synthetic.main.listitem_item_data_summary.*

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

        //Set the summary information
        item_icon.setImageDrawable(assetLoader.loadItemIcon(item))
        item_name.text = item.name
        item_description.text = item.description

        buy_price_value.text = evaluateValue(item.buy_price)
        sell_price_value.text = evaluateValue(item.sell_price)
        carry_capacity_value.text = evaluateValue(item.carry_limit)
        rarity_value.text = evaluateValue(item.rarity)
    }
}
