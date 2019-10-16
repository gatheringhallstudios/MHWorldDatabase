package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterReward
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_reward.view.*

class MonsterRewardAdapterDelegate(private val onSelected: (MonsterReward) -> Unit) : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is MonsterReward
    }

    override fun onCreateViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_reward, parent, false)

        return RewardViewHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val reward = items[position] as MonsterReward

        val vh = holder as RewardViewHolder
        vh.bind(reward)

        holder.view.setOnClickListener { onSelected(reward) }
    }

    internal inner class RewardViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(reward: MonsterReward) {
            val icon = AssetLoader.loadIconFor(reward.item)
            view.reward_icon.setImageDrawable(icon)
            view.reward_name.text = reward.item.name
            view.reward_stack.text = "x ${reward.stack}"
            view.reward_percent.text = when (reward.percentage) {
                0 -> "??%"
                else -> "${reward.percentage}%"
            }

        }
    }
}
