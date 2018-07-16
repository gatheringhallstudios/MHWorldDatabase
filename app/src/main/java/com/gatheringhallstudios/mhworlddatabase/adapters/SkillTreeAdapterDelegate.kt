package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeView
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable

class SkillTreeAdapterDelegate(private val onSelected: (SkillTreeView) -> Unit)
    : SimpleListDelegate<SkillTreeView, IconLabelTextCell>() {

    override fun getDataClass() = SkillTreeView::class

    override fun onCreateView(parent: ViewGroup): IconLabelTextCell {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: IconLabelTextCell, data: SkillTreeView) {
        val icon = view.assetLoader.loadSkillIcon(data.icon_color)

        view.setLeftIconDrawable(icon)
        view.setLabelText(data.name)
        view.removeDecorator()

        view.setOnClickListener { onSelected(data) }
    }
}
