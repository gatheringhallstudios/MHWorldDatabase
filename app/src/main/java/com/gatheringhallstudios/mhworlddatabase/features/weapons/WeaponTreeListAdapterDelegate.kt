package com.gatheringhallstudios.mhworlddatabase.features.weapons

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
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
import com.gatheringhallstudios.mhworlddatabase.components.CompactStatCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.RenderedTreeNode
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.TreeFormatter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.util.px
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_weapon.view.*
import kotlinx.android.synthetic.main.listitem_weapontree.view.*

const val INDENT_SIZE = 16 //This value corresponds to the measured width of each of vectors used for drawing the tree in DP. Convert to pixels before use.

/**
 * Adapter delegate used to render weapons in a weapon tree. These take "RenderedTreeNode" objects directly.
 * Use this in the BasicDelegateAdapter or in a dedicated adapter like the WeaponTreeAdapter.
 */
class WeaponTreeListAdapterDelegate(
        private val onSelected: (Weapon) -> Unit,
        private val onLongSelect: ((Weapon) -> Unit)?
) : AdapterDelegate<List<RenderedTreeNode<Weapon>>>() {

    constructor(onSelected: (Weapon) -> Unit): this(onSelected, null)

    override fun isForViewType(items: List<RenderedTreeNode<Weapon>>, position: Int): Boolean {
        return items[position] is RenderedTreeNode<Weapon>
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_weapontree, parent, false)

        return WeaponBaseHolder(v)
    }

    override fun onBindViewHolder(items: List<RenderedTreeNode<Weapon>>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val weaponBaseTreeNode = items[position]

        val vh = holder as WeaponBaseHolder
        vh.bind(weaponBaseTreeNode)

        holder.view.setOnClickListener { onSelected(weaponBaseTreeNode.value) }
        if (onLongSelect != null) {
            holder.view.setOnLongClickListener {
                // note: cannot pass position as an optimization, as it will not change on list updates unless re-rendered
                onLongSelect.invoke(weaponBaseTreeNode.value)
                true // notify that it was consumed
            }
        }
    }

    internal inner class WeaponBaseHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weaponNode: RenderedTreeNode<Weapon>) {
            val weapon = weaponNode.value

            view.weapon_name.text = weapon.name
            view.weapon_image.setImageDrawable(AssetLoader.loadIconFor(weapon))

            // STATIC STATS

            view.attack_value.setLabelText(weapon.attack.toString())

            //Render sharpness data if it exists, else hide the bar
            val sharpnessData = weapon.sharpnessData
            if (sharpnessData != null) {
                view.sharpness_value.drawSharpness(sharpnessData.get(0))
            } else {
                view.sharpness_value.visibility = View.INVISIBLE
            }

            val slotImages = weapon.slots.map {
                view.context.getDrawableCompat(SlotEmptyRegistry(it))
            }

            view.slot1.setImageDrawable(slotImages[0])
            view.slot2.setImageDrawable(slotImages[1])
            view.slot3.setImageDrawable(slotImages[2])

            // DYNAMIC STATS

            // Clear the placeholder layouts
            view.complex_stat_layout.removeAllViews()

            // Elemental Stat
            if (weapon.element1 != null){
                val elementView = CompactStatCell(
                        view.context,
                        getElementIcon(view.context, weapon.element1),
                        createElementString(weapon.element1_attack, weapon.element_hidden))

                if (weapon.element_hidden) {
                    elementView.labelView.alpha = 0.5.toFloat()
                } else {
                    elementView.labelView.alpha = 1.0.toFloat()
                }

                view.complex_stat_layout.addView(elementView)
            }

            if (weapon.affinity != 0) {
                val affinitySb = StringBuilder()
                val prepend = if (weapon.affinity > 0) "+" else ""
                affinitySb.append(prepend).append(weapon.affinity).append("%")

                val affinityView = CompactStatCell(
                        view.context,
                        R.drawable.ic_ui_affinity,
                        affinitySb.toString())

                if (weapon.affinity > 0 ) {
                    affinityView.labelView.setTextColor(ContextCompat.getColor(view.context, R.color.textColorGreen))
                } else {
                    affinityView.labelView.setTextColor(ContextCompat.getColor(view.context, R.color.textColorRed))
                }

                view.complex_stat_layout.addView(affinityView)
            }

            if (weapon.defense != 0) {
                val defenseView = CompactStatCell(
                        view.context,
                        R.drawable.ic_ui_defense,
                        weapon.defense.toString()
                )

                view.complex_stat_layout.addView(defenseView)
            }

            createTreeLayout(weaponNode.formatter)
        }

        private fun createElementString(element1_attack: Int?, element_hidden: Boolean): String {
            val workString = element1_attack ?: "-----"

            return when (element_hidden) {
                true -> "($workString)"
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
                        space.layoutParams = LinearLayout.LayoutParams(INDENT_SIZE.px, LinearLayout.LayoutParams.MATCH_PARENT)
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

        // todo: there is already a registry for this...or there should be...
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