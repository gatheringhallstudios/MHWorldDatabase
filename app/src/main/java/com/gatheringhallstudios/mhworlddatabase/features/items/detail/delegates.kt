package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemLocation
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemReward
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.*
import kotlinx.android.synthetic.main.listitem_reward.*
import kotlinx.android.synthetic.main.listitem_reward.view.*


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

        viewHolder.reward_icon.setImageDrawable(AssetLoader.loadIconFor(data.location))
        viewHolder.reward_name.text = ctx.getString(R.string.location_area, data.area)
        viewHolder.reward_stack.text = ctx.getString(R.string.quantity, data.stack)
        viewHolder.reward_percent.text = ctx.getString(R.string.percentage, data.percentage)

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
            Rank.LOW -> R.string.low_rank_short
            Rank.HIGH -> R.string.high_rank_short
        })

        // The condition alongside the rank
        val source = viewHolder.resources.getString(
                R.string.item_source_reward_condition, rankStr, data.condition_name)

        viewHolder.icon.setImageDrawable(AssetLoader.loadIconFor(data.monster))
        viewHolder.label_text.text = data.monster.name
        viewHolder.sublabel_text.text = source
        viewHolder.value_text.text = viewHolder.resources.getString(R.string.percentage, data.percentage)
        viewHolder.subvalue_text.text = viewHolder.resources.getString(R.string.quantity, data.stack)

        viewHolder.itemView.setOnClickListener {
            it.getRouter().navigateMonsterDetail(data.monster.id)
        }
    }
}
