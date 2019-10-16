package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.components.applyIconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemLocation
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemReward
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.*
import kotlinx.android.synthetic.main.listitem_reward.*


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

        viewHolder.reward_icon.applyIconType(IconType.PAPER)
        viewHolder.reward_icon.setImageDrawable(AssetLoader.loadIconFor(data.location))
        viewHolder.reward_name.text = ctx.getString(R.string.header_location_area, data.area)
        viewHolder.reward_stack.text = ctx.getString(R.string.format_quantity_x, data.stack)
        viewHolder.reward_percent.text = ctx.getString(R.string.format_percentage, data.percentage)

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateLocationDetail(data.location.id)
        }
    }
}

/**
 * Used to display the monsters that a particular item can come from.
 * This is the "reverse" of MonsterRewardAdapterDelegate.
 */
class MonsterRewardSourceAdapterDelegate: SimpleListDelegate<ItemReward>() {
    override fun isForViewType(obj: Any) = obj is ItemReward

    override fun onCreateView(parent: ViewGroup): View {
        // todo: decide if we want to make this into a standalone view class or not
        // an alternative option is to upgrade the normal one to support sublabels and sub-values
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.cell_icon_verbose_label_text, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ItemReward) {
        // Returns LR/HR depending on the rank
        val rankStr = viewHolder.resources.getString(when (data.rank) {
            Rank.LOW -> R.string.rank_short_low
            Rank.HIGH -> R.string.rank_short_high
        })

        // The condition alongside the rank
        val source = viewHolder.resources.getString(
                R.string.item_crafting_source_condition_reward, rankStr, data.condition_name)

        viewHolder.icon.setImageDrawable(AssetLoader.loadIconFor(data.monster))
        viewHolder.label_text.text = data.monster.name
        viewHolder.sublabel_text.text = source
        viewHolder.value_text.text = when (data.percentage) {
            0 -> viewHolder.resources.getString(R.string.format_percentage_unknown)
            else -> viewHolder.resources.getString(R.string.format_percentage, data.percentage)
        }

        viewHolder.subvalue_text.text = viewHolder.resources.getString(R.string.format_quantity_none, data.stack)

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateMonsterDetail(data.monster.id)
        }
    }
}
