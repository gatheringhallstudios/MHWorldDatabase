package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.graphics.drawable.Animatable
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory
import com.gatheringhallstudios.mhworlddatabase.features.armor.list.compatSwitchVector
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.listitem_quest_header.*

class QuestListHeaderItem(val category: QuestCategory, val stars: Int) : Item(), ExpandableItem {
    private lateinit var group: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        group = onToggleListener
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val categoryString = AssetLoader.localizeQuestCategory(category)
        val name = viewHolder.itemView.resources.getString(
                R.string.quest_category_combined, categoryString, stars)

        viewHolder.quest_group_name.text = name

        bindCurrentState(viewHolder, false)
        viewHolder.itemView.setOnClickListener {
            group.onToggleExpanded()
            bindCurrentState(viewHolder, true)
        }
    }

    override fun getLayout() = R.layout.listitem_quest_header

    private fun bindCurrentState(viewHolder: ViewHolder, stateChanging: Boolean) {
        // set dropdown arrow image
        viewHolder.dropdown_icon.setImageResource(when (group.isExpanded) {
            true -> compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less)
            false -> compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_more)
        })

        // animate (if can be animated)
        val drawable = viewHolder.dropdown_icon.drawable
        if (stateChanging && drawable is Animatable) {
            drawable.start()
        }
    }
}