package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.Router
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeBase
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.view_base_body_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.*


class UserEquipmentSetDecorationSelectorAdapter(private val onSelected: (Decoration) -> Unit) : SimpleRecyclerViewAdapter<Decoration>() {

    val TAG = this.javaClass.simpleName
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var context: Context
    private lateinit var router: Router
    private lateinit var viewGroup : ViewGroup

    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        router = parent.getRouter()
        viewGroup = parent

        return super.onCreateViewHolder(parent, viewType)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: Decoration) {
        (viewHolder.itemView as ExpandableCardView).setCardElevation(1f)
        viewHolder.itemView.setHeader(R.layout.view_base_header_expandable_cardview)
        viewHolder.itemView.setBody(R.layout.view_base_body_expandable_cardview)
        viewHolder.itemView.equipment_name.text = data.name
        viewHolder.itemView.rarity_string.text = viewHolder.resources.getString(R.string.format_rarity, data.rarity)
        viewHolder.itemView.rarity_string.setTextColor(AssetLoader.loadRarityColor(data.rarity))
        viewHolder.itemView.rarity_string.visibility = View.VISIBLE
        viewHolder.itemView.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(data))
        viewHolder.itemView.setOnClick {
            onSelected(data)
        }

        viewHolder.itemView.decorations_section.visibility = View.GONE
        viewHolder.itemView.set_bonus_section.visibility = View.GONE
        populateSkills(data.skillTree, viewHolder.itemView.skill_section)
    }

    private fun populateSkills(skill: SkillTreeBase, skillLayout: LinearLayout) {
        skillLayout.visibility = View.VISIBLE
        skillLayout.skill_list.removeAllViews()

        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.listitem_skill_level, skillLayout.skill_list, false)

        view.icon.setImageDrawable(AssetLoader.loadIconFor(skill))
        view.label_text.text = skill.name
        view.level_text.text = skillLayout.resources.getString(R.string.skill_level_qty, 1)
        with(view.skill_level) {
            maxLevel = skill.max_level
            level = 1 //Decorations always only give 1 skill level
        }

        view.setOnClickListener {
            router.navigateSkillDetail(skill.id)
        }

        skillLayout.skill_list.addView(view)
    }
}
