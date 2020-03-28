package com.gatheringhallstudios.mhworlddatabase.features.charms.list

import android.graphics.Color
import android.graphics.drawable.Animatable
import androidx.core.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.cell_icon_label_text.label_text
import kotlinx.android.synthetic.main.listitem_armorset_header.*
import kotlinx.android.synthetic.main.listitem_universal_simple.*

private val version = android.os.Build.VERSION.SDK_INT

/**
 * Returns desired if vectors are natively supported without fallbacks. Returns the fallback otherwise.
 * It checks if the version is >= Lollipop (API 21)
 */
fun <T> compatSwitchVector(desired: T, fallback: T) = when (version >= android.os.Build.VERSION_CODES.LOLLIPOP) {
    true -> desired
    false -> fallback
}

/**
 * Header item for collapsible armor sets
 */
class CharmHeaderItem(val charm: Charm) : Item(), ExpandableItem {
    private lateinit var group: ExpandableGroup

    override fun getLayout() = R.layout.listitem_armorset_header

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        group = onToggleListener
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val icon = AssetLoader.loadIconFor(charm)

        viewHolder.set_icon.setImageDrawable(icon)
        viewHolder.armor_set_name.text = charm.name
        bindCurrentState(viewHolder)

        viewHolder.itemView.setOnClickListener {
            group.onToggleExpanded()
            bindCurrentState(viewHolder, stateChanging = true)
        }
    }

    /**
     * Updates view to match current expanded/collapsed state
     */
    private fun bindCurrentState(viewHolder: ViewHolder, stateChanging: Boolean = false) {
        val view = viewHolder.itemView
        view.setBackgroundColor(when (group.isExpanded) {
            true -> ContextCompat.getColor(view.context, R.color.backgroundColorSectionHeader)
            false -> Color.TRANSPARENT
        })

        // set dropdown arrow image
        viewHolder.dropdown_icon.setImageResource(when (group.isExpanded) {
            true -> compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less)
            false -> compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_more)
        })

        // animate (if can be animated)
        val drawable = viewHolder.dropdown_icon.drawable
        if (stateChanging && drawable is Animatable) {
            drawable.start()
        }
    }
}

/**
 * Body item for collapsible armor sets.
 * Each one represents a single armor in an armor set.
 */
class CharmDetailItem(val charm: Charm, private val onSelected: (Charm) -> Unit) : Item() {
    override fun getLayout() = R.layout.listitem_universal_simple

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val view = viewHolder.itemView

        viewHolder.label_text.text = charm.name
        viewHolder.sublabel_text.text = view.resources.getString(R.string.format_rarity, charm.rarity)
        viewHolder.sublabel_text.setTextColor(AssetLoader.loadRarityColor(charm.rarity))

        val icon = AssetLoader.loadIconFor(charm)
        viewHolder.icon.setImageDrawable(icon)

        view.setOnClickListener {
            onSelected(charm)
        }
    }
}
