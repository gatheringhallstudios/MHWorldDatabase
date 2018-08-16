package com.gatheringhallstudios.mhworlddatabase.features.armor.list

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.support.v4.content.ContextCompat
import androidx.navigation.Navigation
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.Router
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSet
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.listitem_armorset_armor.*
import kotlinx.android.synthetic.main.listitem_armorset_header.*

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
class ArmorSetHeaderItem(val armorSet: ArmorSet) : Item(), ExpandableItem {
    private lateinit var group: ExpandableGroup

    override fun getLayout() = R.layout.listitem_armorset_header

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        group = onToggleListener
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val icon = AssetLoader.loadIconFor(armorSet)

        viewHolder.set_icon.setImageDrawable(icon)
        viewHolder.armor_set_name.text = armorSet.armorset_name
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
class ArmorSetDetailItem(val armor: Armor) : Item() {
    override fun getLayout() = R.layout.listitem_armorset_armor

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val view = viewHolder.itemView

        viewHolder.armor_name.text = armor.name
        viewHolder.rarity_string.text = view.resources.getString(R.string.format_rarity_string, armor.rarity)
        viewHolder.defense_value.text = view.resources.getString(
                R.string.armor_defense_value,
                armor.defense_base,
                armor.defense_max,
                armor.defense_augment_max)

        // load all images that represent the slots into an array first
        val slotImages = armor.slots.map {
            view.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        viewHolder.slot1.setImageDrawable(slotImages[0])
        viewHolder.slot2.setImageDrawable(slotImages[1])
        viewHolder.slot3.setImageDrawable(slotImages[2])

        val icon = AssetLoader.loadIconFor(armor)
        viewHolder.armor_icon.setImageDrawable(icon)
        viewHolder.rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.rarity))

        view.setOnClickListener {
            Router(Navigation.findNavController(view)).navigateArmorDetail(armor.id)
        }
    }
}
