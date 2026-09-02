package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.xwray.groupie.viewbinding.BindableItem
import com.gatheringhallstudios.mhworlddatabase.databinding.CellIconVerboseLabelTextBinding

/**
 * Body item for collapsible quests.
 * Each one represents a single armor in an armor set.
 */
class QuestListDetailItem(val quest: QuestBase) : BindableItem<CellIconVerboseLabelTextBinding>() {

    override fun getLayout() = R.layout.cell_icon_verbose_label_text

    override fun initializeViewBinding(view: View) = CellIconVerboseLabelTextBinding.bind(view)

    override fun bind(viewBinding: CellIconVerboseLabelTextBinding, position: Int) {
        val view = viewBinding.root

        viewBinding.icon.setImageDrawable(AssetLoader.loadIconFor(quest))
        viewBinding.labelText.text = quest.name
        viewBinding.sublabelText.visibility = View.GONE

        view.setOnClickListener {
            view.getRouter().navigateQuestDetail(quest.id)
        }
    }
}