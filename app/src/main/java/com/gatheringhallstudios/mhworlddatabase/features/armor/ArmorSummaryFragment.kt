package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.views.Armor
import kotlinx.android.synthetic.main.fragment_armor_summary.*

class ArmorSummaryFragment : Fragment() {

    private val viewModel : ArmorDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_armor_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.armor.observe(this, Observer(::populateArmor))
    }

    private fun populateArmor(armor: Armor?) {
        if(armor == null) return

        armor_detail_name.text = armor.name
        armor_detail_rarity.text = getString(
                R.string.armor_detail_rarity,
                armor.rarity)
        armor_icon.setImageDrawable(AssetLoader(context!!).loadArmorIcon(armor.armor_type, armor.rarity))



    }
}