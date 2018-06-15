package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView
import kotlinx.android.synthetic.main.listitem_armorset.view.*

/**
 * Created by Carlos on 3/22/2018.
 */

class ArmorSetAdapterDelegate : SimpleListDelegate<ArmorSetView, View>() {

    override fun getDataClass() = ArmorSetView::class

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_armorset, parent, false)
    }

    override fun bindView(view: View, armorSet: ArmorSetView) {
        view.armor_set_name.text = armorSet.armorset_name

        view.head_icon.setOnClickListener({
            Toast.makeText(view.context,
                    "Clicked ${armorSet.head_armor?.name}",
                    Toast.LENGTH_LONG)
                    .show()
        })
        view.chest_icon.setOnClickListener({
            Toast.makeText(view.context,
                    "Clicked ${armorSet.chest_armor?.name}",
                    Toast.LENGTH_LONG)
                    .show()
        })
        view.arms_icon.setOnClickListener({
            Toast.makeText(view.context,
                    "Clicked ${armorSet.arms_armor?.name}",
                    Toast.LENGTH_LONG)
                    .show()
        })
        view.waist_icon.setOnClickListener({
            Toast.makeText(view.context,
                    "Clicked ${armorSet.waist_armor?.name}",
                    Toast.LENGTH_LONG)
                    .show()
        })
        view.legs_icon.setOnClickListener({
            Toast.makeText(view.context,
                    "Clicked ${armorSet.legs_armor?.name}",
                    Toast.LENGTH_LONG)
                    .show()
        })
    }
}
