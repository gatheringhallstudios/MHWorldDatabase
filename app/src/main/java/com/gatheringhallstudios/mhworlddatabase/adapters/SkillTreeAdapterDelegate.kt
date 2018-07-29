package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTree

class SkillTreeAdapterDelegate(private val onSelected: (SkillTree) -> Unit)
    : SimpleListDelegate<SkillTree>() {

    override fun isForViewType(obj: Any) = obj is SkillTree

    override fun onCreateView(parent: ViewGroup): IconLabelTextCell {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: SkillTree) {
        val icon = AssetLoader.loadSkillIcon(data.icon_color)

        with(viewHolder.itemView as IconLabelTextCell) {
            setLeftIconDrawable(icon)
            setLabelText(data.name)
            removeDecorator()
        }

        viewHolder.itemView.setOnClickListener { onSelected(data) }
    }
}
