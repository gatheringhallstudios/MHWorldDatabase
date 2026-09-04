package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.features.armor.list.compatSwitchVector
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.viewbinding.BindableItem
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemQuestHeaderBinding


class QuestListHeaderItem(val category: QuestCategory, val stars: Int) : BindableItem<ListitemQuestHeaderBinding>(), ExpandableItem {
    private lateinit var group: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        group = onToggleListener
    }

    override fun initializeViewBinding(view: View) = ListitemQuestHeaderBinding.bind(view)

    override fun bind(viewBinding: ListitemQuestHeaderBinding, position: Int) {
        val categoryString = AssetLoader.localizeQuestCategory(category)
        val res = viewBinding.root.resources

        // TODO Change to MR once master rank quest are supported
        val name =
                when {
                    categoryString == res.getString(R.string.quest_category_special) -> res.getString(R.string.quest_category_special_abbr)
                    stars > 9 -> (stars - 10).toString()
                    else -> stars.toString()
                }

        viewBinding.questGroupName.text = name

        when (stars) {
            in 1..5 -> addStarsToLayoutLow(viewBinding.questStarLayout, stars, Rank.LOW)
            in 6..9 -> addStarsToLayoutLow(viewBinding.questStarLayout, stars, Rank.HIGH)
            else -> addStarsToLayoutLow(viewBinding.questStarLayout, stars - 10, Rank.MASTER)
        }

        bindCurrentState(viewBinding, false)
        viewBinding.root.setOnClickListener {
            group.onToggleExpanded()
            bindCurrentState(viewBinding, true)
        }
    }

    override fun getLayout() = R.layout.listitem_quest_header

    private fun bindCurrentState(viewBinding: ListitemQuestHeaderBinding, stateChanging: Boolean) {
        viewBinding.root.setBackgroundColor(when (group.isExpanded) {
            true -> ContextCompat.getColor(viewBinding.root.context, R.color.backgroundColorSectionHeader)
            false -> Color.TRANSPARENT
        })

        // set dropdown arrow image
        viewBinding.dropdownIcon.setImageResource(when (group.isExpanded) {
            true -> compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less)
            false -> compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_more)
        })

        // animate (if can be animated)
        val drawable = viewBinding.dropdownIcon.drawable
        if (stateChanging && drawable is Animatable) {
            drawable.start()
        }
    }

    private fun addStarsToLayoutLow(layout: ViewGroup, numStars: Int, rank: Rank) {
        layout.removeAllViews()
        for (i in 1..numStars) {
            val star = ImageView(layout.context)

            when (rank) {
                Rank.LOW -> star.setImageDrawable(AssetLoader.loadIconFor(Rank.LOW))
                Rank.HIGH -> star.setImageDrawable(AssetLoader.loadIconFor(Rank.HIGH))
                else -> star.setImageDrawable(AssetLoader.loadIconFor(Rank.MASTER))
            }

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