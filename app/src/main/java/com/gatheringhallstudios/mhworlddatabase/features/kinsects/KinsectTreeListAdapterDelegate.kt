package com.gatheringhallstudios.mhworlddatabase.features.kinsects

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Kinsect
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectAttackType
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectDustEffect
import com.gatheringhallstudios.mhworlddatabase.features.weapons.INDENT_SIZE
import com.gatheringhallstudios.mhworlddatabase.util.px
import com.gatheringhallstudios.mhworlddatabase.util.tree.RenderedTreeNode
import com.gatheringhallstudios.mhworlddatabase.util.tree.TreeFormatter
import com.gatheringhallstudios.mhworlddatabase.util.tree.TreeNodeType
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemKinsecttreeBinding

class KinsectTreeListAdapterDelegate(
        private val onSelected: (Kinsect) -> Unit,
        private val onLongSelect: ((Kinsect) -> Unit)?
) : AdapterDelegate<List<Any>>() {

    constructor(onSelected: (Kinsect) -> Unit) : this(onSelected, null)

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        val node = items[position] as? RenderedTreeNode<*>
        return node?.value is Kinsect
    }

    override fun onCreateViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return KinsectBaseHolder(ListitemKinsecttreeBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        @Suppress("UNCHECKED_CAST")
        val kinsectBaseTreeNode = items[position] as RenderedTreeNode<Kinsect>

        val vh = holder as KinsectBaseHolder
        vh.bind(kinsectBaseTreeNode)

        holder.itemView.setOnClickListener { onSelected(kinsectBaseTreeNode.value) }
        if (onLongSelect != null) {
            holder.itemView.setOnLongClickListener {
                // note: cannot pass position as an optimization, as it will not change on list updates unless re-rendered
                onLongSelect.invoke(kinsectBaseTreeNode.value)
                true // notify that it was consumed
            }
        }
    }

    internal inner class KinsectBaseHolder(private val binding: ListitemKinsecttreeBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceType")
        fun bind(kinsectNode: RenderedTreeNode<Kinsect>) {
            val kinsect = kinsectNode.value

            binding.kinsectLayout.kinsectName.text = kinsect.name
            binding.kinsectLayout.kinsectImage.setImageDrawable(AssetLoader.loadIconFor(kinsect))
            binding.kinsectLayout.kinsectCraftableImage.visibility = if (kinsect.previous_kinsect_id == null) View.VISIBLE else View.GONE

            binding.kinsectLayout.attackType.setLabelText(when (kinsect.attack_type) {
                KinsectAttackType.SEVER -> itemView.context.getString(R.string.kinsect_attack_type_sever)
                KinsectAttackType.BLUNT -> itemView.context.getString(R.string.kinsect_attack_type_blunt)
            })
            binding.kinsectLayout.dustEffect.setLabelText(when (kinsect.dust_effect) {
                KinsectDustEffect.POISON -> itemView.context.getString(R.string.kinsect_dust_effect_poison)
                KinsectDustEffect.PARALYSIS -> itemView.context.getString(R.string.kinsect_dust_effect_paralysis)
                KinsectDustEffect.HEAL -> itemView.context.getString(R.string.kinsect_dust_effect_heal)
                KinsectDustEffect.BLAST -> itemView.context.getString(R.string.kinsect_dust_effect_blast)
            })
            binding.kinsectLayout.dustEffect.setLeftIconDrawable(AssetLoader.loadKinsectDustIcon(kinsect.dust_effect))

            binding.kinsectLayout.powerValue.setLabelText(kinsect.power.toString())
            binding.kinsectLayout.speedValue.setLabelText(kinsect.speed.toString())
            binding.kinsectLayout.healValue.setLabelText(kinsect.heal.toString())

            // Populate tree lines
            createTreeLayout(kinsectNode.formatter, kinsectNode.isCollapsed, kinsect.rarity)

            itemView.invalidate()
        }

        private fun createTreeLayout(formatter: List<TreeFormatter>, isCollapsed: Boolean, rarity: Int) {
            val treeView = binding.treeComponents

            if (treeView.childCount != 0) treeView.removeAllViews()

            formatter.forEach {
                val drawable = when (it) {
                    TreeFormatter.START -> if (!isCollapsed) {
                        AssetLoader.loadIconFor(TreeNodeType.START, rarity)
                    } else {
                        AssetLoader.loadIconFor(TreeNodeType.START_COLLAPSED, rarity)
                    }
                    TreeFormatter.MID -> if (!isCollapsed) {
                        AssetLoader.loadIconFor(TreeNodeType.MID, rarity)
                    } else {
                        AssetLoader.loadIconFor(TreeNodeType.MID_COLLAPSED, rarity)
                    }
                    TreeFormatter.THROUGH -> if (!isCollapsed) {
                        AssetLoader.loadIconFor(TreeNodeType.THROUGH, rarity)
                    } else {
                        AssetLoader.loadIconFor(TreeNodeType.THROUGH_COLLAPSED, rarity)
                    }
                    TreeFormatter.INDENT -> null
                    TreeFormatter.STRAIGHT_BRANCH -> ContextCompat.getDrawable(treeView.context, R.drawable.ui_tree_space_line)
                    TreeFormatter.T_BRANCH -> ContextCompat.getDrawable(treeView.context, R.drawable.ui_tree_space_t)
                    TreeFormatter.L_BRANCH -> ContextCompat.getDrawable(treeView.context, R.drawable.ui_tree_space_l)
                    TreeFormatter.END -> AssetLoader.loadIconFor(TreeNodeType.END, rarity)
                    TreeFormatter.END_INDENTED -> AssetLoader.loadIconFor(TreeNodeType.END_INDENTED, rarity)
                }

                treeView.addView(createImageView(treeView.context, drawable))
            }
        }

        private fun createImageView(context: Context, drawable: Drawable?): View {
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.setPadding(0, 0, 0, 0)
            imageView.setImageBitmap(drawable?.toBitmap()) // TODO raster only the required drawables instead of all of them

            return if (drawable == null) {
                val space = Space(context)
                space.layoutParams = LinearLayout.LayoutParams(INDENT_SIZE.px, LinearLayout.LayoutParams.MATCH_PARENT)
                space
            } else {
                imageView
            }
        }
    }
}