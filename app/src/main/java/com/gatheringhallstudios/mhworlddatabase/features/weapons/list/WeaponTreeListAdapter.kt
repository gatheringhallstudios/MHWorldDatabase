package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponTree
import com.gatheringhallstudios.mhworlddatabase.data.types.TreeFormatter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_weapon.view.*
import kotlinx.android.synthetic.main.listitem_weapontree.view.*

const val INDENT_SIZE = 42 //This value corresponds to the measured width of each of vectors used for drawing the tree

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
            view.weapon_image.setImageDrawable(AssetLoader.loadIconFor(weaponTree))

            view.attack_value.setLabelText(weaponTree.attack.toString())

            view.element_value.setLabelText(createElementString(weaponTree.element1_attack, weaponTree.element_hidden))
            view.element_value.setLeftIconDrawable(getElementIcon(view.context, weaponTree.element1))
            if (weaponTree.element_hidden) {
                view.element_value.alpha = 0.5.toFloat()
            } else {
                view.element_value.alpha = 1.0.toFloat()
            }

            view.defense_value.setLabelText(weaponTree.defense.toString())

            //Render sharpness data if it exists, else hide the bar
            val sharpnessData = weaponTree.sharpnessData?.get(5)
            if (sharpnessData != null) {
                view.sharpness_value.drawSharpness(sharpnessData)
            } else {
                view.sharpness_value.visibility = View.INVISIBLE
            }

            val slotImages = weaponTree.slots.map {
                view.context.getDrawableCompat(SlotEmptyRegistry(it))
            }

            view.slot1.setImageDrawable(slotImages[0])
            view.slot2.setImageDrawable(slotImages[1])
            view.slot3.setImageDrawable(slotImages[2])
            createTreeLayout(weaponTree.formatter)
        }

        fun createElementString(element1_attack: Int?, element_hidden: Boolean): String {
            val workString = element1_attack ?: "-----"

            return when (element_hidden) {
                true -> "(${workString})"
                false -> workString.toString()
            }
        }

        private fun createTreeLayout(formatter: List<TreeFormatter>) {
            val treeView = view.tree_components

            if (treeView.childCount != 0) treeView.removeAllViews()

            formatter.forEach {
                when (it) {
                    TreeFormatter.START -> {
                        treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_node_start))
                    }
                    TreeFormatter.INDENT -> {
                        val space = Space(treeView.context)
                        space.layoutParams = LinearLayout.LayoutParams(INDENT_SIZE, LinearLayout.LayoutParams.MATCH_PARENT)
                        treeView.addView(space)
                    }
                    TreeFormatter.STRAIGHT_BRANCH -> {
                        treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_space_line))
                    }
                    TreeFormatter.T_BRANCH -> {
                        treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_space_t))
                    }
                    TreeFormatter.MID -> {
                        treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_node_mid))
                    }
                    TreeFormatter.L_BRANCH -> {
                        treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_space_l))
                    }
                    TreeFormatter.END -> {
                        treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_node_end))
                    }
                }
            }
        }

        private fun createImageView(context: Context, resource: Int): ImageView {
            val imageView = ImageView(context)
            imageView.setPadding(0, 0, 0, 0)
            imageView.setImageResource(resource)
            return imageView
        }

        private fun getElementIcon(context: Context, element: String?): Drawable? {
            return when (element) {
                "Fire" -> context.getDrawableCompat(R.drawable.ic_element_fire)
                "Dragon" -> context.getDrawableCompat(R.drawable.ic_element_dragon)
                "Poison" -> context.getDrawableCompat(R.drawable.ic_status_poison)
                "Water" -> context.getDrawableCompat(R.drawable.ic_element_water)
                "Thunder" -> context.getDrawableCompat(R.drawable.ic_element_thunder)
                "Ice" -> context.getDrawableCompat(R.drawable.ic_element_ice)
                "Blast" -> context.getDrawableCompat(R.drawable.ic_status_blast)
                "Paralysis" -> context.getDrawableCompat(R.drawable.ic_status_paralysis)
                "Sleep" -> context.getDrawableCompat(R.drawable.ic_status_sleep)
                else -> context.getDrawableCompat(R.drawable.ic_ui_slot_none)
            }
        }
    }
}