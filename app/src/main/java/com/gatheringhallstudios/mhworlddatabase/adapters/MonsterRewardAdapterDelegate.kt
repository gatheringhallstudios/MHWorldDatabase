package com.gatheringhallstudios.mhworlddatabase.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_reward.view.*

class MonsterRewardAdapterDelegate(private val onSelected: (MonsterRewardView) -> Unit) : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is MonsterRewardView
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
        val reward = items[position] as MonsterRewardView

        val vh = holder as RewardViewHolder
        vh.bind(reward)

        holder.view.setOnClickListener { _: View -> onSelected(reward) }
    }

    internal inner class RewardViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(reward: MonsterRewardView) {
            // TODO Set item image
            view.reward_name.text = reward.item_name
            view.reward_stack.text = "x ${reward.data.stack}"
            view.reward_percent.text = "${reward.data.percentage}%"
        }
    }
}
