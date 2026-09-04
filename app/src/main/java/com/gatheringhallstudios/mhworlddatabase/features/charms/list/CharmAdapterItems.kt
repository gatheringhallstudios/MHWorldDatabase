package com.gatheringhallstudios.mhworlddatabase.features.charms.list

import android.graphics.Color
import android.graphics.drawable.Animatable
import androidx.core.content.ContextCompat
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.viewbinding.BindableItem
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemUniversalSimpleBinding
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
class CharmHeaderItem(val charm: Charm) : BindableItem<ListitemArmorsetHeaderBinding>(), ExpandableItem {
    private lateinit var group: ExpandableGroup

    override fun getLayout() = R.layout.listitem_armorset_header

    override fun initializeViewBinding(view: View) = ListitemArmorsetHeaderBinding.bind(view)

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        group = onToggleListener
    }

    override fun bind(viewBinding: ListitemArmorsetHeaderBinding, position: Int) {
        val icon = AssetLoader.loadIconFor(charm)

        viewBinding.setIcon.setImageDrawable(icon)
        viewBinding.armorSetName.text = charm.name
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
class CharmDetailItem(val charm: Charm, private val onSelected: (Charm) -> Unit) : BindableItem<ListitemUniversalSimpleBinding>() {
    override fun getLayout() = R.layout.listitem_universal_simple

    override fun initializeViewBinding(view: View) = ListitemUniversalSimpleBinding.bind(view)

    override fun bind(viewBinding: ListitemUniversalSimpleBinding, position: Int) {
        val view = viewBinding.root

        viewBinding.labelText.text = charm.name
        viewBinding.sublabelText.text = view.resources.getString(R.string.format_rarity, charm.rarity)
        viewBinding.sublabelText.setTextColor(AssetLoader.loadRarityColor(charm.rarity))

        val icon = AssetLoader.loadIconFor(charm)
        viewBinding.icon.setImageDrawable(icon)

        view.setOnClickListener {
            onSelected(charm)
        }
    }
}
