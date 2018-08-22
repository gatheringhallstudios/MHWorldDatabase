package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.provider.Settings.Global.getString
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.components.DetailHeaderCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterReward
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponBase
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_weapontree.view.*

class WeaponTreeListAdapterDelegate(private val onSelected: (WeaponBase) -> Unit) : AdapterDelegate<List<TreeNode<WeaponBase>>>() {
    override fun isForViewType(items: List<TreeNode<WeaponBase>>, position: Int): Boolean {
        return items[position] is TreeNode<WeaponBase>
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_weapontree, parent, false)

        return WeaponBaseHolder(v)
    }

    override fun onBindViewHolder(items: List<TreeNode<WeaponBase>>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val weaponBaseTreeNode = items[position]

        val vh = holder as WeaponBaseHolder
        vh.bind(weaponBaseTreeNode.value)

        holder.view.setOnClickListener { onSelected(weaponBaseTreeNode.value) }
    }

    internal inner class WeaponBaseHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weaponBase: WeaponBase) {
            view.weapon_name.text = weaponBase.name
            view.weapon_subtitle.text = view.resources.getString(R.string.format_rarity_string, weaponBase.rarity)
            view.tree_branch.text = weaponBase.formatting
        }
    }
}

