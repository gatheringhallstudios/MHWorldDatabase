package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.components.VerboseIconLabelTextCellBinder
import com.gatheringhallstudios.mhworlddatabase.components.applyIconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemLocation
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemMonsterReward
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuestReward
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemRewardBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.CellIconVerboseLabelTextBinding


/**
 * Renders list items for item location information
 */
class ItemLocationAdapterDelegate : SimpleListDelegate<ItemLocation>() {
    override fun isForViewType(obj: Any) = obj is ItemLocation

    override fun onCreateView(parent: ViewGroup): View {
        // todo: refactor listitem_reward into a general view
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_reward, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ItemLocation) {
        val ctx = viewHolder.context
        val binding = ListitemRewardBinding.bind(viewHolder.itemView)

        binding.rewardIcon.applyIconType(IconType.PAPER)
        binding.rewardIcon.setImageDrawable(AssetLoader.loadIconFor(data.location))
        binding.rewardName.text = ctx.getString(R.string.header_location_area, data.area)
        binding.rewardStack.text = ctx.getString(R.string.format_quantity_x, data.stack)
        binding.rewardPercent.text = ctx.getString(R.string.format_percentage, data.percentage)

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateLocationDetail(data.location.id)
        }
    }
}

/**
 * Used to display the monsters that a particular item can come from.
 * This is the "reverse" of MonsterRewardAdapterDelegate.
 */
class MonsterRewardSourceAdapterDelegate: SimpleListDelegate<ItemMonsterReward>() {
    override fun isForViewType(obj: Any) = obj is ItemMonsterReward

    override fun onCreateView(parent: ViewGroup): View {
        // todo: decide if we want to make this into a standalone view class or not
        // an alternative option is to upgrade the normal one to support sublabels and sub-values
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.cell_icon_verbose_label_text, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ItemMonsterReward) {
        // Returns LR/HR depending on the rank
        val rankStr = viewHolder.resources.getString(when (data.rank) {
            Rank.LOW -> R.string.rank_short_low
            Rank.HIGH -> R.string.rank_short_high
            Rank.MASTER -> R.string.rank_short_master
        })

        // The condition alongside the rank
        val source = viewHolder.resources.getString(
                R.string.item_crafting_source_condition_reward, rankStr, data.condition_name)

        val binding = CellIconVerboseLabelTextBinding.bind(viewHolder.itemView)
        binding.icon.setImageDrawable(AssetLoader.loadIconFor(data.monster))
        binding.labelText.text = data.monster.name
        binding.sublabelText.text = source
        binding.valueText.text = when (data.percentage) {
            0 -> viewHolder.resources.getString(R.string.format_percentage_unknown)
            else -> viewHolder.resources.getString(R.string.format_percentage, data.percentage)
        }

        binding.subvalueText.text = viewHolder.resources.getString(R.string.format_quantity_none, data.stack)

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateMonsterDetail(data.monster.id)
        }
    }
}

/**
 * Used to display the monsters that a particular item can come from.
 * This is the "reverse" of MonsterRewardAdapterDelegate.
 */
class QuestRewardSourceAdapterDelegate: SimpleListDelegate<ItemQuestReward>() {
    override fun isForViewType(obj: Any) = obj is ItemQuestReward

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.cell_icon_verbose_label_text, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ItemQuestReward) {
        val categoryText = AssetLoader.localizeQuestCategory(data.quest.category)
        val categoryCombined = viewHolder.resources.getString(
                R.string.quest_category_combined,
                categoryText,
                if(data.quest.stars_raw > 9) " MR" else "",
                data.quest.stars
        )

        with (VerboseIconLabelTextCellBinder(viewHolder.itemView)) {
            setIconDrawable(AssetLoader.loadIconFor(data.quest))
            setLabelText(data.quest.name)
            setSubLabelText(categoryCombined)
            setValueText(when (data.percentage) {
                0 -> viewHolder.resources.getString(R.string.format_percentage_unknown)
                else -> viewHolder.resources.getString(R.string.format_percentage, data.percentage)
            })
            setSubValueText(viewHolder.resources.getString(R.string.format_quantity_none, data.stack))
        }

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateQuestDetail(data.quest.id)
        }
    }
}
