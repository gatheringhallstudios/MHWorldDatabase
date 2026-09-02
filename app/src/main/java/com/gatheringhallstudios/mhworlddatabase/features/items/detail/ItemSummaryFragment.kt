package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Item
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentItemSummaryBinding


/**
 * Binds item data values from the itemView object to the view
 */
private fun evaluateValue(value: Int?) = when(value) {
    0, null -> "-"
    else -> value.toString()
}

class ItemSummaryFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentItemSummaryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentItemSummaryBinding.inflate(inflater, parent, false)

        val viewmodel = ViewModelProvider(parentFragment!!).get(ItemDetailViewModel::class.java)
        viewmodel.item.observe(this, Observer(::populateItem))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun populateItem(item: Item?) {
        if (item == null) return

        //Set the summary information
        binding.itemHeader.setIconDrawable(AssetLoader.loadIconFor(item))
        binding.itemHeader.setTitleText(item.name)
        binding.itemHeader.setDescriptionText(item.description)

        binding.rarityValue.setTextColor(AssetLoader.loadRarityColor(item.rarity))
        binding.rarityValue.text = getString(R.string.format_rarity, item.rarity)

        binding.buyPriceValue.text = evaluateValue(item.buy_price)
        binding.carryCapacityValue.text = evaluateValue(item.carry_limit)

        // Set sell value. Swaps to research points if research points are available
        if (item.sell_price == 0 && item.points > 0) {
            binding.sellPriceValue.text = item.points.toString()
            binding.sellPriceIcon.setImageDrawable(context?.getDrawableCompat(R.drawable.ic_ui_research_points))
        } else {
            binding.sellPriceValue.text = evaluateValue(item.sell_price)
        }

    }
}
