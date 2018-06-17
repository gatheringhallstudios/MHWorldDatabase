package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeView
import kotlinx.android.synthetic.main.listitem_large.view.*

class SkillTreeAdapterDelegate(private val onSelected: (SkillTreeView) -> Unit)
    : SimpleListDelegate<SkillTreeView, View>() {

    override fun getDataClass() = SkillTreeView::class

    override fun onCreateView(parent: ViewGroup): View {

        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_large, parent, false)
    }

    override fun bindView(view: View, data: SkillTreeView) {
        view.item_name.text = data.name
    }
}
