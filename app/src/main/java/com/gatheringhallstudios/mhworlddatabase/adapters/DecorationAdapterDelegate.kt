package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.DecorationView
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeView
import com.gatheringhallstudios.mhworlddatabase.getVectorDrawable

class DecorationAdapterDelegate(private val onSelected: (DecorationView) -> Unit)
    : SimpleListDelegate<DecorationView, View>() {

    override fun getDataClass() = DecorationView::class

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }



    override fun bindView(view: View, data: DecorationView) {
        //TODO: update the icon with the actual decoration icon once that's added
        val icon = view.context.getVectorDrawable(R.drawable.ic_armor)

        with(view as IconLabelTextCell) {
            view.setLeftIconDrawable(icon)
            view.setLabelText(data.name)
            view.removeDecorator()
        }

        view.setOnClickListener { onSelected(data) }
    }
}
