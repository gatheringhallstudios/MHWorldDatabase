package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponTree
import com.gatheringhallstudios.mhworlddatabase.data.types.TreeFormatter
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_weapontree.view.*

class WeaponTreeListAdapterDelegate(private val onSelected: (WeaponTree) -> Unit) : AdapterDelegate<List<TreeNode<WeaponTree>>>() {
    override fun isForViewType(items: List<TreeNode<WeaponTree>>, position: Int): Boolean {
        return items[position] is TreeNode<WeaponTree>
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_weapontree, parent, false)

        return WeaponBaseHolder(v)
    }

    override fun onBindViewHolder(items: List<TreeNode<WeaponTree>>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val weaponBaseTreeNode = items[position]

        val vh = holder as WeaponBaseHolder
        vh.bind(weaponBaseTreeNode.value)

        holder.view.setOnClickListener { onSelected(weaponBaseTreeNode.value) }
    }

    internal inner class WeaponBaseHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weaponTree: WeaponTree) {
            view.weapon_name.text = weaponTree.name
            view.weapon_subtitle.text = "${weaponTree.depth}"
            view.tree_branch.text = createFormatString(weaponTree.formatter)
        }
    }

    //TODO: Convert this string builder into a proper tree drawing implementation
    private fun createFormatString(formatter: List<TreeFormatter>): String {
        var returnString = ""
        formatter.forEach {
            when (it) {
                TreeFormatter.INDENT -> returnString += "  "
                TreeFormatter.STRAIGHT_BRANCH -> returnString += "│"
                TreeFormatter.T_BRANCH -> returnString += "├"
                TreeFormatter.L_BRANCH -> returnString += "└"
            }
        }

        return returnString
    }
}