package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTree

class SkillTreeAdapterDelegate(private val onSelected: (SkillTree) -> Unit)
    : SimpleListDelegate<SkillTree, IconLabelTextCell>() {

    override fun isForViewType(obj: Any) = obj is SkillTree

    override fun onCreateView(parent: ViewGroup): IconLabelTextCell {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: IconLabelTextCell, data: SkillTree) {
        val icon = view.assetLoader.loadSkillIcon(data.icon_color)

        view.setLeftIconDrawable(icon)
        view.setLabelText(data.name)
        view.removeDecorator()

        view.setOnClickListener { onSelected(data) }
    }
}
