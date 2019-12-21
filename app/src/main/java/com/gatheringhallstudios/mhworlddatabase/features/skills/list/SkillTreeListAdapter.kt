package com.gatheringhallstudios.mhworlddatabase.features.skills.list

import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTree
import com.gatheringhallstudios.mhworlddatabase.getRouter


/**
 * A RecyclerView adapter for a homogeneous SkillTree list
 */
class SkillTreeListAdapter(private val onSelected: (SkillTree) -> Unit): SimpleRecyclerViewAdapter<SkillTree>() {
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
