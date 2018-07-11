package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeView
import com.gatheringhallstudios.mhworlddatabase.getVectorDrawable

class SkillTreeAdapterDelegate(private val onSelected: (SkillTreeView) -> Unit)
    : SimpleListDelegate<SkillTreeView, View>() {

    override fun getDataClass() = SkillTreeView::class

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: View, data: SkillTreeView) {
        val icon = view.context.getVectorDrawable(R.drawable.ic_ui_armor_skill_base, data.icon_color)

        with(view as IconLabelTextCell) {
            view.setLeftIconDrawable(icon)
            view.setLabelText(data.name)
            view.removeDecorator()
        }

        view.setOnClickListener { onSelected(data) }
    }
}
