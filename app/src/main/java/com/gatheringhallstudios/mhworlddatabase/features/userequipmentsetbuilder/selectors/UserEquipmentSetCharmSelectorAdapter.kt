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
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmFull
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.view_base_body_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.*


class UserEquipmentSetCharmSelectorAdapter(private val onSelected: (CharmFull) -> Unit) : SimpleRecyclerViewAdapter<CharmFull>() {

    val TAG = this.javaClass.simpleName
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var context: Context
    private lateinit var router : Router
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

    override fun bindView(viewHolder: SimpleViewHolder, data: CharmFull) {
        val armor = data.charm

        (viewHolder.itemView as ExpandableCardView).setCardElevation(1f)
        viewHolder.itemView.setHeader(R.layout.view_base_header_expandable_cardview)
        viewHolder.itemView.setBody(R.layout.view_base_body_expandable_cardview)
        viewHolder.itemView.equipment_name.text = armor.name
        viewHolder.itemView.rarity_string.text = viewHolder.resources.getString(R.string.format_rarity, armor.rarity)
        viewHolder.itemView.rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.rarity))
        viewHolder.itemView.rarity_string.visibility = View.VISIBLE
        viewHolder.itemView.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor))

        viewHolder.itemView.defense_value.visibility = View.GONE
        viewHolder.itemView.icon_defense.visibility = View.GONE
        viewHolder.itemView.icon_slots.visibility = View.GONE
        viewHolder.itemView.slot1.visibility = View.GONE
        viewHolder.itemView.slot2.visibility = View.GONE
        viewHolder.itemView.slot3.visibility = View.GONE
        viewHolder.itemView.set_bonus_section.visibility = View.GONE
        viewHolder.itemView.decorations_section.visibility = View.GONE
        viewHolder.itemView.slot1_detail.visibility = View.GONE
        viewHolder.itemView.slot2_detail.visibility = View.GONE
        viewHolder.itemView.slot3_detail.visibility = View.GONE

        viewHolder.itemView.setOnClick {
            onSelected(data)
        }

        viewHolder.itemView.decorations_section.visibility = View.GONE
        populateSkills(data.skills, viewHolder.itemView.skill_section)
    }

    private fun populateSkills(skills: List<SkillLevel>, skillLayout: LinearLayout) {
        if (skills.isEmpty()) {
            skillLayout.visibility = View.GONE
            return
        }

        skillLayout.visibility = View.VISIBLE
        skillLayout.skill_list.removeAllViews()

        val inflater = LayoutInflater.from(context)

        for (skill in skills) {
            //Set the label for the Set name
            val view = inflater.inflate(R.layout.listitem_skill_level, skillLayout.skill_list, false)

            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            view.label_text.text = skill.skillTree.name
            view.level_text.text = skillLayout.resources.getString(R.string.skill_level_qty, skill.level)
            with(view.skill_level) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            view.setOnClickListener {
                router.navigateSkillDetail(skill.skillTree.id)
            }

            skillLayout.skill_list.addView(view)
        }
    }
}
