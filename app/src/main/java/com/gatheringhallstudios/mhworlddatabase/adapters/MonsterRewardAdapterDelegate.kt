package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterReward
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemRewardBinding

class MonsterRewardAdapterDelegate(private val onSelected: (MonsterReward) -> Unit) : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is MonsterReward
    }

    override fun onCreateViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RewardViewHolder(ListitemRewardBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val reward = items[position] as MonsterReward

        val vh = holder as RewardViewHolder
        vh.bind(reward)

        holder.itemView.setOnClickListener { onSelected(reward) }
    }

    internal inner class RewardViewHolder(private val binding: ListitemRewardBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(reward: MonsterReward) {
            val icon = AssetLoader.loadIconFor(reward.item)
            binding.rewardIcon.setImageDrawable(icon)
            binding.rewardName.text = reward.item.name
            binding.rewardStack.text = "x ${reward.stack}"
            binding.rewardPercent.text = when (reward.percentage) {
                0 -> itemView.context.getString(R.string.format_percentage_unknown)
                else -> "${reward.percentage}%"
            }

        }
    }
}
