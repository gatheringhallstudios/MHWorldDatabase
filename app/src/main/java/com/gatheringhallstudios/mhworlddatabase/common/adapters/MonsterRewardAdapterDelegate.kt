package com.gatheringhallstudios.mhworlddatabase.common.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.Consumer
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

class RewardAdapterDelegate(private val onSelected: () -> Unit) : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is MonsterRewardView
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_reward, parent, false)

        return RewardViewHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val reward = items[position] as MonsterRewardView

        val vh = holder as RewardViewHolder
        // TODO Set monster image
        vh.rewardName.text = reward.item_name

        val stack = "x " + Integer.toString(reward.stack_size)
        vh.rewardStack.text = stack

        val percent = Integer.toString(reward.percentage) + "%"
        vh.rewardPercent.text = percent

        holder.itemView.setOnClickListener { v: View -> onSelected.accept(items[position]) }
    }

    internal inner class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rewardIcon: ImageView
        var rewardName: TextView
        var rewardStack: TextView
        var rewardPercent: TextView

        init {
            rewardIcon = itemView.findViewById(R.id.reward_icon)
            rewardName = itemView.findViewById(R.id.reward_name)
            rewardStack = itemView.findViewById(R.id.reward_stack)
            rewardPercent = itemView.findViewById(R.id.reward_percent)
        }
    }
}
