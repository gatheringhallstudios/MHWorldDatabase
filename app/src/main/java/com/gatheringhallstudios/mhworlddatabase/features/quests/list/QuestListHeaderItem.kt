package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
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
        val res = viewHolder.itemView.resources

        // TODO Change to MR once master rank quest are supported
        val name = if (categoryString == res.getString(R.string.quest_category_special))
            res.getString(R.string.quest_category_special_abbr)
            else stars.toString()

        viewHolder.quest_group_name.text = name

        addStarsToLayout(viewHolder.quest_star_layout, stars)

        bindCurrentState(viewHolder, false)
        viewHolder.itemView.setOnClickListener {
            group.onToggleExpanded()
            bindCurrentState(viewHolder, true)
        }
    }

    override fun getLayout() = R.layout.listitem_quest_header

    private fun bindCurrentState(viewHolder: ViewHolder, stateChanging: Boolean) {
        viewHolder.containerView.setBackgroundColor(when (group.isExpanded) {
            true -> ContextCompat.getColor(viewHolder.containerView.context, R.color.backgroundColorSectionHeader)
            false -> Color.TRANSPARENT
        })

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

    private fun addStarsToLayout(layout: ViewGroup, numStars: Int) {
        layout.removeAllViews()
        for (i in 1..numStars) {
            val star = ImageView(layout.context)

            // TODO Replace with appropriate star if LR/HR/MR
            star.setImageDrawable(AppCompatResources.getDrawable(layout.context, R.drawable.ic_ui_effective_star))
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.height = layout.resources.getDimensionPixelSize(R.dimen.image_size_small)
            lp.width = layout.resources.getDimensionPixelSize(R.dimen.image_size_small)
            star.layoutParams = lp

            layout.addView(star)
            // Invalidate to trigger layout update
            layout.invalidate()
        }
    }
}