package com.gatheringhallstudios.mhworlddatabase.features.skills

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinderAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinding
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.adapters.createSimpleUniversalBinder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSkillLevel
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmSkillLevel
import com.gatheringhallstudios.mhworlddatabase.data.models.DecorationBase
import com.gatheringhallstudios.mhworlddatabase.getRouter

/**
 * A wrapper used to build the adapter for skill details.
 * Waits until all items are loaded before updating the internal adapter
 */
class SkillDetailAdapterWrapper {
    val adapter = CategoryAdapter(
            SimpleUniversalBinderAdapterDelegate(),
            NullObjectAdapterDelegate()
    )

    // once this = 3, all are loaded
    private var itemsLoaded = 0

    private lateinit var decorationTitle: String
    private lateinit var charmTitle: String
    private lateinit var armorTitle: String

    private lateinit var decorationList: List<DecorationBase>
    private lateinit var charmList: List<CharmSkillLevel>
    private lateinit var armorList: List<ArmorSkillLevel>

    fun setDecorations(title: String, decorations: List<DecorationBase>) {
        if (::decorationList.isInitialized) {
            return
        }

        this.decorationTitle = title
        this.decorationList = decorations
        handleItemLoaded()
    }

    fun setCharms(title: String, charms: List<CharmSkillLevel>) {
        if (::charmList.isInitialized) {
            return
        }

        this.charmTitle = title
        this.charmList = charms
        handleItemLoaded()
    }

    fun setArmor(title: String, armor: List<ArmorSkillLevel>) {
        if (::armorList.isInitialized) {
            return
        }

        this.armorTitle = title
        this.armorList = armor
        handleItemLoaded()
    }

    private fun handleItemLoaded() {
        itemsLoaded++
        if (itemsLoaded < 3) {
            return
        }

        fun switchList(list: List<Any>) = when (list.isEmpty()) {
            true -> listOf(NullObject())
            false -> list
        }

        adapter.addSection(decorationTitle, switchList(decorationList.map(::createDecorationSkillBinder)))
        adapter.addSection(charmTitle, switchList(charmList.map(::createCharmSkillBinder)))
        adapter.addSection(armorTitle, switchList(armorList.map(::createArmorSkillBinder)))
    }
}

/**
 * A placeholder object to say "no data exists"
 */
private class NullObject

private class NullObjectAdapterDelegate: SimpleListDelegate<NullObject>() {
    override fun isForViewType(obj: Any) = obj is NullObject

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_empty_medium, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: NullObject) {
        // do nothing
    }
}

private fun createDecorationSkillBinder(decoration: DecorationBase) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(decoration)
    val value = ctx.getString(R.string.plus, 1)
    SimpleUniversalBinding(decoration.name, value, IconType.EMBELLISHED, icon) {
        it.getRouter().navigateDecorationDetail(decoration.id)
    }
}

private fun createCharmSkillBinder(charm: CharmSkillLevel) = createSimpleUniversalBinder {ctx ->
    val icon = AssetLoader.loadIconFor(charm.charm)
    val value = ctx.getString(R.string.plus, charm.level)
    SimpleUniversalBinding(charm.charm.name, value, IconType.EMBELLISHED, icon) {
        it.getRouter().navigateCharmDetail(charm.charm.id)
    }
}

private fun createArmorSkillBinder(armorSkill: ArmorSkillLevel) = createSimpleUniversalBinder { ctx ->
    val icon = AssetLoader.loadIconFor(armorSkill.armor)
    val value = ctx.getString(R.string.plus, armorSkill.level)
    SimpleUniversalBinding(armorSkill.armor.name, value, IconType.ZEMBELLISHED, icon) {
        it.getRouter().navigateArmorDetail(armorSkill.armor.id)
    }
}