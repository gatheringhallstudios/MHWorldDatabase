package com.gatheringhallstudios.mhworlddatabase.features.armor.list

import android.graphics.Color
import android.graphics.drawable.Animatable
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.Router
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSet
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.viewbinding.BindableItem
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetArmorBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetHeaderBinding

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
class ArmorSetHeaderItem(val armorSet: ArmorSet) : BindableItem<ListitemArmorsetHeaderBinding>(), ExpandableItem {
    private lateinit var group: ExpandableGroup

    override fun getLayout() = R.layout.listitem_armorset_header

    override fun initializeViewBinding(view: View) = ListitemArmorsetHeaderBinding.bind(view)

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        group = onToggleListener
    }

    override fun bind(viewBinding: ListitemArmorsetHeaderBinding, position: Int) {
        val icon = AssetLoader.loadIconFor(armorSet)

        viewBinding.setIcon.setImageDrawable(icon)
        viewBinding.armorSetName.text = armorSet.armorset_name
        bindCurrentState(viewBinding)

        viewBinding.root.setOnClickListener {
            group.onToggleExpanded()
            bindCurrentState(viewBinding, stateChanging = true)
        }
    }

    /**
     * Updates view to match current expanded/collapsed state
     */
    private fun bindCurrentState(viewBinding: ListitemArmorsetHeaderBinding, stateChanging: Boolean = false) {
        val view = viewBinding.root
        view.setBackgroundColor(when (group.isExpanded) {
            true -> ContextCompat.getColor(view.context, R.color.backgroundColorSectionHeader)
            false -> Color.TRANSPARENT
        })

        // set dropdown arrow image
        viewBinding.dropdownIcon.setImageResource(when (group.isExpanded) {
            true -> compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less)
            false -> compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_more)
        })

        // animate (if can be animated)
        val drawable = viewBinding.dropdownIcon.drawable
        if (stateChanging && drawable is Animatable) {
            drawable.start()
        }
    }
}

/**
 * Body item for collapsible armor sets.
 * Each one represents a single armor in an armor set.
 */
class ArmorSetDetailItem(val armor: Armor, private val onSelected: (Armor) -> Unit) : BindableItem<ListitemArmorsetArmorBinding>() {
    override fun getLayout() = R.layout.listitem_armorset_armor

    override fun initializeViewBinding(view: View) = ListitemArmorsetArmorBinding.bind(view)

    override fun bind(viewBinding: ListitemArmorsetArmorBinding, position: Int) {
        val view = viewBinding.root

        viewBinding.armorName.text = armor.name
        viewBinding.rarityString.text = view.resources.getString(R.string.format_rarity, armor.rarity)
        viewBinding.defenseValue.text = view.resources.getString(
                R.string.armor_defense_value,
                armor.defense_base,
                armor.defense_max,
                armor.defense_augment_max)

        // load all images that represent the slots into an array first
        val slotImages = armor.slots.map {
            view.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        viewBinding.slot1.setImageDrawable(slotImages[0])
        viewBinding.slot2.setImageDrawable(slotImages[1])
        viewBinding.slot3.setImageDrawable(slotImages[2])

        val icon = AssetLoader.loadIconFor(armor)
        viewBinding.armorIcon.setImageDrawable(icon)
        viewBinding.rarityString.setTextColor(AssetLoader.loadRarityColor(armor.rarity))

        view.setOnClickListener {
            onSelected(armor)
        }
    }
}
