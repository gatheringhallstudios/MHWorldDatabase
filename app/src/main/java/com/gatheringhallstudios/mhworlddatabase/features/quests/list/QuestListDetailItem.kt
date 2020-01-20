package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.*

/**
 * Body item for collapsible quests.
 * Each one represents a single armor in an armor set.
 */
class QuestListDetailItem(val quest: QuestBase) : Item() {
    override fun getLayout() = R.layout.cell_icon_verbose_label_text

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val view = viewHolder.itemView

        viewHolder.icon.setImageDrawable(AssetLoader.loadIconFor(quest))
        viewHolder.label_text.text = quest.name
        viewHolder.sublabel_text.visibility = View.GONE

        viewHolder.itemView.setOnClickListener {
            view.getRouter().navigateQuestDetail(quest.id)
        }
    }
}