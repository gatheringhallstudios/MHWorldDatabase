package com.gatheringhallstudios.mhworlddatabase.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.gatheringhallstudios.mhworlddatabase.getAssetDrawable

import kotlinx.android.synthetic.main.listitem_monster.view.*

/**
 * Creates a view object to represent a monster list item.
 */
class MonsterListItemView(ctx: Context) : LinearLayout(ctx) {
    init {
        val inflater = LayoutInflater.from(ctx)
        inflater.inflate(R.layout.listitem_monster, this, true)
    }

    fun setMonster(monster : MonsterView) {
        val defaultIcon = R.drawable.question_mark_grey
        val icon = context.getAssetDrawable(monster.data.icon, defaultIcon)
        monster_icon.setImageDrawable(icon)
        monster_name.text = monster.name
    }
}

class MonsterAdapterDelegate(private val onSelected: (MonsterView) -> Unit)
    : SimpleListDelegate<MonsterView, MonsterListItemView>() {

    override fun getDataClass() = MonsterView::class

    override fun onCreateView(parent: ViewGroup): View {
        return MonsterListItemView(parent.context)
    }

    override fun bindView(view: MonsterListItemView, data: MonsterView) {
        view.setMonster(data)
        view.setOnClickListener { onSelected(data) }
    }
}
