package com.gatheringhallstudios.mhworlddatabase.features.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import kotlinx.android.synthetic.main.listitem_bookmark.view.*

class ItemBookmarkDelegate(private val onSelect: (Item) -> Unit) : SimpleListDelegate<Item>() {
    override fun isForViewType(obj: Any) = obj is Item
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Item) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}

class LocationBookmarkDelegate(private val onSelect: (Location) -> Unit) : SimpleListDelegate<Location>() {
    override fun isForViewType(obj: Any) = obj is Location
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Location) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}

class CharmBookmarkDelegate(private val onSelect: (Charm) -> Unit) : SimpleListDelegate<Charm>() {
    override fun isForViewType(obj: Any) = obj is Charm
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Charm) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}

class DecorationBaseBookmarkDelegate(private val onSelect: (DecorationBase) -> Unit) : SimpleListDelegate<DecorationBase>() {
    override fun isForViewType(obj: Any) = obj is DecorationBase
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: DecorationBase) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}

class MonsterBaseBookmarkDelegate(private val onSelect: (MonsterBase) -> Unit) : SimpleListDelegate<MonsterBase>() {
    override fun isForViewType(obj: Any) = obj is MonsterBase
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: MonsterBase) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}

class SkillTreeBookmarkDelegate(private val onSelect: (SkillTree) -> Unit) : SimpleListDelegate<SkillTree>() {
    override fun isForViewType(obj: Any) = obj is SkillTree
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: SkillTree) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}

class WeaponBookmarkDelegate(private val onSelect: (Weapon) -> Unit) : SimpleListDelegate<Weapon>() {
    override fun isForViewType(obj: Any) = obj is Weapon
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Weapon) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}

class ArmorBookmarkDelegate(private val onSelect: (Armor) -> Unit) : SimpleListDelegate<Armor>() {
    override fun isForViewType(obj: Any) = obj is Armor
    private lateinit var context: Context

    override fun onCreateView(parent: ViewGroup): View {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_bookmark, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Armor) {
        val icon = AssetLoader.loadIconFor(data)
        viewHolder.itemView.generic_icon.setImageDrawable(icon)
        viewHolder.itemView.label_text.text = data.name
        viewHolder.itemView.setOnClickListener { onSelect(data) }
//        viewHolder.itemView.delete_bookmark_button.setOnClickListener {onDelete(data)}
    }
}
