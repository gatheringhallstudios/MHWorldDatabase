package com.gatheringhallstudios.mhworlddatabase.features.skills.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinderAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinding
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSkillLevel
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmSkillLevel
import com.gatheringhallstudios.mhworlddatabase.data.models.DecorationSkillLevel
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.listitem_skill_level_armor.*

/**
 * A wrapper used to build the adapter for skill details.
 * Waits until all items are loaded before updating the internal adapter
 */
class SkillDetailAdapterWrapper {
    val adapter = CategoryAdapter(
            NullObjectAdapterDelegate(),
            DecorationSkillLevelAdapterDelegate(),
            CharmSkillLevelAdapterDelegate(),
            ArmorSkillLevelAdapterDelegate(),
            SimpleUniversalBinderAdapterDelegate()
    )

    // once this = 3, all are loaded
    private var itemsLoaded = 0

    private lateinit var decorationTitle: String
    private lateinit var charmTitle: String
    private lateinit var armorTitle: String

    private lateinit var decorationList: List<DecorationSkillLevel>
    private lateinit var charmList: List<CharmSkillLevel>
    private lateinit var armorList: List<ArmorSkillLevel>

    fun setDecorations(title: String, decorations: List<DecorationSkillLevel>) {
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

        adapter.addSection(decorationTitle, switchList(decorationList))
        adapter.addSection(charmTitle, switchList(charmList))
        adapter.addSection(armorTitle, switchList(armorList))
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

class DecorationSkillLevelAdapterDelegate: SimpleListDelegate<DecorationSkillLevel>() {
    override fun isForViewType(obj: Any) = obj is DecorationSkillLevel

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_skill_level, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: DecorationSkillLevel) {
        viewHolder.icon.setImageDrawable(AssetLoader.loadIconFor(data.decoration))
        viewHolder.label_text.text = data.decoration.name
        viewHolder.skill_level.maxLevel = data.skillTree.max_level
        viewHolder.skill_level.level = data.level
        viewHolder.level_text.text = viewHolder.context.getString(R.string.skills_level_qty, data.level)
        viewHolder.itemView.setOnClickListener { it.getRouter().navigateDecorationDetail(data.decoration.id) }
    }
}

class CharmSkillLevelAdapterDelegate: SimpleListDelegate<CharmSkillLevel>() {
    override fun isForViewType(obj: Any) = obj is CharmSkillLevel

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_skill_level, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: CharmSkillLevel) {
        viewHolder.icon.setImageDrawable(AssetLoader.loadIconFor(data.charm))
        viewHolder.label_text.text = data.charm.name
        viewHolder.skill_level.maxLevel = data.skillTree.max_level
        viewHolder.skill_level.level = data.level
        viewHolder.level_text.text = viewHolder.context.getString(R.string.skills_level_qty, data.level)
        viewHolder.itemView.setOnClickListener { it.getRouter().navigateCharmDetail(data.charm.id) }
    }
}

class ArmorSkillLevelAdapterDelegate: SimpleListDelegate<ArmorSkillLevel>() {
    override fun isForViewType(obj: Any) = obj is ArmorSkillLevel

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_skill_level_armor, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ArmorSkillLevel) {
        viewHolder.icon.setImageDrawable(AssetLoader.loadIconFor(data.armor))
        viewHolder.label_text.text = data.armor.name
        viewHolder.skill_level.maxLevel = data.skillTree.max_level
        viewHolder.skill_level.level = data.level
        viewHolder.level_text.text = viewHolder.context.getString(R.string.skills_level_qty, data.level)
        viewHolder.itemView.setOnClickListener { it.getRouter().navigateArmorDetail(data.armor.id) }
    }
}