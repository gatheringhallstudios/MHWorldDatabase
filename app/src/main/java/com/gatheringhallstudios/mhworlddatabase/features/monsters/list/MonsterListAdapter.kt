package com.gatheringhallstudios.mhworlddatabase.features.monsters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterBase
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.listitem_monster.*


/**
 * A RecyclerView adapter for a homogeneous monster list
 */
class MonsterListAdapter: SimpleRecyclerViewAdapter<MonsterBase>() {
    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_monster, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: MonsterBase) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.icon.setImageDrawable(icon)
        viewHolder.monster_name.text = data.name
        
        if (data.ecology.isNullOrEmpty()) {
            viewHolder.ecology_text.visibility = View.GONE
        } else {
            viewHolder.ecology_text.visibility = View.VISIBLE
            viewHolder.ecology_text.text = data.ecology
        }

        viewHolder.itemView.setOnClickListener { it.getRouter().navigateMonsterDetail(data.id) }
    }
}
