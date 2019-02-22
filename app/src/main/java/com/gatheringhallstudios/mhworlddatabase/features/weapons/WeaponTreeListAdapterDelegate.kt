package com.gatheringhallstudios.mhworlddatabase.features.weapons

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.CompactStatCell
import com.gatheringhallstudios.mhworlddatabase.components.CompactStatIconLayoutCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.CoatingType
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.util.px
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_weapon.view.*
import kotlinx.android.synthetic.main.listitem_weapontree.view.*
import kotlinx.android.synthetic.main.section_bow_coating.view.*

const val INDENT_SIZE = 16 //This value corresponds to the measured width of each of vectors used for drawing the tree in DP. Convert to pixels before use.

/**
 * Adapter delegate used to render weapons in a weapon tree. These take "RenderedTreeNode" objects directly.
 * Use this in the BasicDelegateAdapter or in a dedicated adapter like the WeaponTreeAdapter.
 */
class WeaponTreeListAdapterDelegate(
        private val onSelected: (Weapon) -> Unit,
        private val onLongSelect: ((Weapon) -> Unit)?
) : AdapterDelegate<List<Any>>() {

    constructor(onSelected: (Weapon) -> Unit) : this(onSelected, null)

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        val node = items[position] as? RenderedTreeNode<*>
        return node?.value is Weapon
    }

    override fun onCreateViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_weapontree, parent, false)

        return WeaponBaseHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        @Suppress("UNCHECKED_CAST")
        val weaponBaseTreeNode = items[position] as RenderedTreeNode<Weapon>

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

    internal inner class WeaponBaseHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        @SuppressLint("ResourceType")
        fun bind(weaponNode: RenderedTreeNode<Weapon>) {
            val weapon = weaponNode.value

            view.weapon_name.text = weapon.name
            view.weapon_image.setImageDrawable(AssetLoader.loadIconFor(weapon))
            view.weapon_craftable_image.visibility = when {
                weapon.craftable -> View.VISIBLE
                else -> View.GONE
            }

            // Populate static stats like attack, affinity...
            populateStaticStats(weapon)
            // Populate stats like horn notes, shelling type...
            populateWeaponSpecificStats(weapon)
            // Populate decorations
            populateDecorations(weapon)
            // Populate stats like element, defense...
            populateComplexStats(weapon)
            // Populate tree lines
            createTreeLayout(weaponNode.formatter, weaponNode.isCollapsed)

            view.invalidate()
        }

        private fun populateStaticStats(weapon: Weapon) {
            view.attack_value.setLabelText(weapon.attack.toString())

            //Render sharpness data if it exists, else hide the bars
            val sharpnessData = weapon.sharpnessData
            if (sharpnessData != null) {
                view.sharpness_container.visibility = View.VISIBLE
                view.sharpness_value.drawSharpness(sharpnessData.min)
                view.sharpness_max_value.drawSharpness(sharpnessData.max)
            } else {
                view.sharpness_container.visibility = View.GONE
            }
        }

        private fun populateDecorations(weapon: Weapon) {
            val slotImages = weapon.slots.map {
                view.context.getDrawableCompat(SlotEmptyRegistry(it))
            }

            view.slot1.setImageDrawable(slotImages[0])
            view.slot2.setImageDrawable(slotImages[1])
            view.slot3.setImageDrawable(slotImages[2])

            // Hide views if no slots
            view.slot1.visibility = when (weapon.slots[0]) {
                0 -> View.GONE
                else -> View.VISIBLE
            }
            view.slot2.visibility = when (weapon.slots[1]) {
                0 -> View.GONE
                else -> View.VISIBLE
            }
            view.slot3.visibility = when (weapon.slots[2]) {
                0 -> View.GONE
                else -> View.VISIBLE
            }
        }

        private fun populateWeaponSpecificStats(weapon: Weapon) {
            //Weapon Specific stats (e.g. phials, kinsect bonus, special ammo etc.)
            view.tree_weapon_specific_section.removeAllViews()

            when (weapon.weapon_type) {
                WeaponType.CHARGE_BLADE, WeaponType.SWITCH_AXE -> {
                    val phialValue = AssetLoader.localizePhialType(weapon.phial)
                    val phialPower = weapon.phial_power

                    val phialView = CompactStatCell(
                            view.context,
                            R.drawable.ic_ui_phials,
                            when (phialPower) {
                                0 -> phialValue
                                else -> "$phialValue $phialPower"
                            }
                    )
                    view.tree_weapon_specific_section.addView(phialView)

                }

                WeaponType.INSECT_GLAIVE -> {
                    val kinsectValue = AssetLoader.localizeKinsectBonus(weapon.kinsect_bonus)

                    val kinsectView = CompactStatCell(
                            view.context,
                            R.drawable.ic_ui_kinsect_white,
                            kinsectValue
                    )
                    view.tree_weapon_specific_section.addView(kinsectView)

                }

                WeaponType.HUNTING_HORN -> {
                    val notesView = CompactStatIconLayoutCell(view.context)

                    weapon.notes?.forEachIndexed { index, note ->
                        notesView.addLayoutIcon(AssetLoader.loadNoteFromChar(note, index)!!)
                    }

                    view.tree_weapon_specific_section.addView(notesView)
                }

                WeaponType.GUNLANCE -> {
                    val shellingValue = AssetLoader.localizeShellingType(weapon.shelling) + " " + view.context.getString(R.string.skill_level_short_qty, weapon.shelling_level)
                    val shellingView = CompactStatCell(
                            view.context,
                            R.drawable.ic_ui_shelling,
                            shellingValue
                    )
                    view.tree_weapon_specific_section.addView(shellingView)

                }

                WeaponType.LIGHT_BOWGUN, WeaponType.HEAVY_BOWGUN -> {
                    val specialAmmoValue = weapon.special_ammo.toString()
                    val specialAmmoView = CompactStatCell(
                            view.context,
                            R.drawable.ic_ui_special_ammo,
                            specialAmmoValue
                    )
                    view.tree_weapon_specific_section.addView(specialAmmoView)

                }

                WeaponType.BOW -> {
                    val coatingView = LayoutInflater.from(view.context)
                            .inflate(R.layout.section_bow_coating_compact, view.tree_weapon_specific_section, false)

                    weapon.weaponCoatings?.iterator()?.forEach {
                        when (it) {
                            CoatingType.CLOSE_RANGE -> {
                                coatingView.close_range_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.CLOSE_RANGE))
                            }
                            CoatingType.POWER -> {
                                coatingView.power_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.POWER))
                            }
                            CoatingType.PARALYSIS -> {
                                coatingView.paralysis_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.PARALYSIS))
                            }
                            CoatingType.POISON -> {
                                coatingView.poison_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.POISON))
                            }
                            CoatingType.SLEEP -> {
                                coatingView.sleep_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.SLEEP))
                            }
                            CoatingType.BLAST -> {
                                coatingView.blast_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.BLAST))
                            }
                        }
                    }

                    view.tree_weapon_specific_section.addView(coatingView)
                }

                else -> Unit
            }
        }

        private fun populateComplexStats(weapon: Weapon) {
            // Clear the placeholder layouts
            view.complex_stat_layout.removeAllViews()

            // Elemental Stat (added if there's a value)
            if (weapon.element1 != null) {
                val elementView = CompactStatCell(
                        view.context,
                        AssetLoader.loadElementIcon(weapon.element1),
                        createElementString(weapon.element1_attack, weapon.element_hidden))

                if (weapon.element_hidden) {
                    elementView.labelView.alpha = 0.5.toFloat()
                } else {
                    elementView.labelView.alpha = 1.0.toFloat()
                }

                view.complex_stat_layout.addView(elementView)
            }

            // Affinity (added if there's a value)
            if (weapon.affinity != 0) {
                val affinityValue = view.context.getString(R.string.format_plus_percentage, weapon.affinity)

                val affinityView = CompactStatCell(
                        view.context,
                        R.drawable.ic_ui_affinity,
                        affinityValue)

                affinityView.labelView.setTextColor(ContextCompat.getColor(view.context, when {
                    weapon.affinity > 0 -> R.color.textColorGreen
                    else -> R.color.textColorRed
                }))

                view.complex_stat_layout.addView(affinityView)
            }

            // Defense, added if there's a value
            if (weapon.defense != 0) {
                val defenseValue = view.context.getString(R.string.format_plus, weapon.defense)
                val defenseView = CompactStatCell(
                        view.context,
                        R.drawable.ic_ui_defense,
                        defenseValue
                )

                defenseView.labelView.setTextColor(ContextCompat.getColor(view.context, when {
                    weapon.defense > 0 -> R.color.textColorGreen
                    else -> R.color.textColorRed
                }))

                view.complex_stat_layout.addView(defenseView)
            }
        }

        private fun createElementString(element1_attack: Int?, element_hidden: Boolean): String {
            val workString = element1_attack ?: "-----"

            return when (element_hidden) {
                true -> "($workString)"
                false -> workString.toString()
            }
        }

        private fun createTreeLayout(formatter: List<TreeFormatter>, isCollapsed: Boolean) {
            val treeView = view.tree_components

            if (treeView.childCount != 0) treeView.removeAllViews()

            formatter.forEach {
                when (it) {
                    TreeFormatter.START -> {
                        if (!isCollapsed) {
                            treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_node_start))
                        } else {
                            treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_node_start_collapsed))
                        }
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
                        if (!isCollapsed) {
                            treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_node_mid))
                        } else {
                            treeView.addView(createImageView(treeView.context, R.drawable.ui_tree_node_mid_collapsed))
                        }
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
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            imageView.setPadding(0, 0, 0, 0)
            imageView.setImageResource(resource)
            return imageView
        }
    }
}