package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterBase
import kotlinx.android.synthetic.main.listitem_large.view.*

class MonsterAdapterDelegate(private val onSelected: (MonsterBase) -> Unit)
    : SimpleListDelegate<MonsterBase, View>() {

    override fun isForViewType(obj: Any) = obj is MonsterBase

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_large, parent, false)
    }

    override fun bindView(view: View, data: MonsterBase) {
        val icon = view.assetLoader.loadIconFor(data)

        view.item_icon.setImageDrawable(icon)
        view.item_name.text = data.name

        view.setOnClickListener { onSelected(data) }
    }
}
