package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.graphics.Color
import android.support.v4.content.ContextCompat
import androidx.navigation.Navigation
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.Router
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorView
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.listitem_armorset_armor.*
import kotlinx.android.synthetic.main.listitem_armorset_header.*


/**
 * Header item for collapsible armor sets
 */
class ArmorSetHeaderItem(val armorSet: ArmorSetView) : Item(), ExpandableItem {
    private lateinit var group: ExpandableGroup

    override fun getLayout() = R.layout.listitem_armorset_header

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        group = onToggleListener
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val icon = AssetLoader(viewHolder.itemView.context).loadArmorSetIcon(armorSet)

        viewHolder.set_icon.setImageDrawable(icon)
        viewHolder.armor_set_name.text = armorSet.armorset_name
        bindCurrentState(viewHolder)

        viewHolder.itemView.setOnClickListener {
            group.onToggleExpanded()
            bindCurrentState(viewHolder)
        }
    }

    /**
     * Updates view to match current expanded/collapsed state
     */
    private fun bindCurrentState(viewHolder: ViewHolder) {
        val view = viewHolder.itemView
        view.setBackgroundColor(when (group.isExpanded) {
            true -> ContextCompat.getColor(view.context, R.color.backgroundColorSectionHeader)
            false -> Color.TRANSPARENT
        })
    }
}

/**
 * Body item for collapsible armor sets.
 * Each one represents a single armor in an armor set.
 */
class ArmorSetDetailItem(val armor: ArmorView) : Item() {
    override fun getLayout() = R.layout.listitem_armorset_armor

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val view = viewHolder.itemView

        viewHolder.armor_name.text = armor.name
        viewHolder.rarity_string.text = view.resources.getString(R.string.rare_label, armor.rarity)
        viewHolder.defense_value.text = view.resources.getString(
                R.string.armor_defense_value,
                armor.data.defense_base,
                armor.data.defense_max,
                armor.data.defense_augment_max)

        // load all images that represent the slots into an array first
        val slotImages = armor.slots.map {
            view.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        viewHolder.slot1.setImageDrawable(slotImages[0])
        viewHolder.slot2.setImageDrawable(slotImages[1])
        viewHolder.slot3.setImageDrawable(slotImages[2])

        val loader = AssetLoader(viewHolder.itemView.context)
        val icon = loader.loadArmorIcon(armor.armor_type, armor.rarity)
        viewHolder.armor_icon.setImageDrawable(icon)

        view.setOnClickListener {
            Router(Navigation.findNavController(view)).navigateArmorDetail(armor.id)
        }
    }
}
