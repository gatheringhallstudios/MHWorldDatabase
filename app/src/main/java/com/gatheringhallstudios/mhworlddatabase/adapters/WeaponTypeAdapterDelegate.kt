package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import kotlinx.android.synthetic.main.listitem_large.*

/**
 * Defines an adapter delegate for weapon types
 */
class WeaponTypeAdapterDelegate(private val onSelected: (WeaponType) -> Unit) : SimpleListDelegate<WeaponType>() {
    override fun isForViewType(obj: Any) = obj is WeaponType

    override fun onCreateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.listitem_large, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: WeaponType) {
        viewHolder.item_name.text = AssetLoader.getNameFor(data)
        viewHolder.item_icon.setImageDrawable(AssetLoader.loadIconFor(data))

        viewHolder.itemView.setOnClickListener {onSelected(data)}
    }
}