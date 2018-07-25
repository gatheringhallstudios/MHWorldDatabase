package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm

class CharmAdapterDelegate(private val onSelected: (Charm) -> Unit)
    : SimpleListDelegate<Charm, IconLabelTextCell>() {

    val TAG = this.javaClass.simpleName

    override fun isForViewType(obj: Any) = obj is Charm

    override fun onCreateView(parent: ViewGroup): IconLabelTextCell {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: IconLabelTextCell, data: Charm) {
        val icon = AssetLoader(view.context).loadIconFor(data)

        view.setLeftIconDrawable(icon)
        view.setLabelText(data.name)
        view.removeDecorator()

        view.setOnClickListener { onSelected(data) }
    }
}
