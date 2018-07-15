package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorComponentView
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

class ArmorComponentAdapterDelegate(private val onSelect: (ArmorComponentView) -> Unit)
    : SimpleListDelegate<ArmorComponentView, View>() {

    override fun getDataClass() = ArmorComponentView::class

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: View, data: ArmorComponentView) {
        with (view as IconLabelTextCell) {
            val icon = AssetLoader(view.context).loadItemIcon(data.result)
            setLeftIconDrawable(icon)
            setLabelText(data.result.name)
            setValueText("x${data.quantity}")
        }

        view.setOnClickListener { onSelect(data) }
    }
}
