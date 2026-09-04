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
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemMonsterBinding


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
        val binding = ListitemMonsterBinding.bind(viewHolder.itemView)
        binding.icon.setImageDrawable(icon)
        binding.monsterName.text = data.name

        if (data.ecology.isNullOrEmpty()) {
            binding.ecologyText.visibility = View.GONE
        } else {
            binding.ecologyText.visibility = View.VISIBLE
            binding.ecologyText.text = data.ecology
        }

        viewHolder.itemView.setOnClickListener { it.getRouter().navigateMonsterDetail(data.id) }
    }
}
