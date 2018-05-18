package com.gatheringhallstudios.mhworlddatabase.common.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_monster.view.*

class MonsterAdapterDelegate(private val onSelected: (MonsterView) -> Unit) : AdapterDelegate<List<MonsterView>>() {

    override fun isForViewType(items: List<MonsterView>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_monster, parent, false)

        return MonsterViewHolder(v)
    }

    override fun onBindViewHolder(items: List<MonsterView>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val monster = items[position]

        val monVH = holder as MonsterViewHolder
        monVH.bind(monster)

        holder.itemView.setOnClickListener { onSelected(monster) }
    }

    internal inner class MonsterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(monster: MonsterView) {
            // TODO Set monster image
            view.monster_name.text = monster.name
        }
    }
}
