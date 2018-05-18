package com.gatheringhallstudios.mhworlddatabase.common.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

class MonsterAdapterDelegate(onSelected: Consumer<MonsterView>) : AdapterDelegate<List<MonsterView>>() {

    init {
        this.onSelected = onSelected
    }

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
        val (_, name) = items[position]

        val monVH = holder as MonsterViewHolder
        monVH.monsterName.text = name

        holder.itemView.setOnClickListener { v: View -> onSelected.accept(items[position]) }

        // TODO Set monster image
    }

    internal inner class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var monsterIcon: ImageView
        var monsterName: TextView

        init {
            monsterIcon = itemView.findViewById(R.id.monster_icon)
            monsterName = itemView.findViewById(R.id.monster_name)
        }
    }
}
