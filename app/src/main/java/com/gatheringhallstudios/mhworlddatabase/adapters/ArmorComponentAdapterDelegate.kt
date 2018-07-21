package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorComponent

class ArmorComponentAdapterDelegate(private val onSelect: (ArmorComponent) -> Unit)
    : SimpleListDelegate<ArmorComponent, View>() {

    override fun isForViewType(obj: Any) = obj is ArmorComponent

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: View, data: ArmorComponent) {
        with (view as IconLabelTextCell) {
            val icon = AssetLoader(view.context).loadIconFor(data.result)
            setLeftIconDrawable(icon)
            setLabelText(data.result.name)
            setValueText("x${data.quantity}")
        }

        view.setOnClickListener { onSelect(data) }
    }
}
