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
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponFull
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.view_base_body_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.equipment_icon
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.equipment_name
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.rarity_string
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot1
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot2
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot3
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.*


class UserEquipmentSetWeaponSelectorAdapter(private val onSelected: (WeaponFull) -> Unit) : SimpleRecyclerViewAdapter<WeaponFull>() {

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

    override fun bindView(viewHolder: SimpleViewHolder, data: WeaponFull) {
        val weapon = data.weapon

        (viewHolder.itemView as ExpandableCardView).setCardElevation(1f)
        viewHolder.itemView.setHeader(R.layout.view_weapon_header_expandable_cardview)
        viewHolder.itemView.setBody(R.layout.view_base_body_expandable_cardview)
        viewHolder.itemView.equipment_name.text = weapon.name
        viewHolder.itemView.rarity_string.text = viewHolder.resources.getString(R.string.format_rarity, weapon.rarity)
        viewHolder.itemView.rarity_string.setTextColor(AssetLoader.loadRarityColor(weapon.rarity))
        viewHolder.itemView.rarity_string.visibility = View.VISIBLE
        viewHolder.itemView.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(weapon))
        viewHolder.itemView.attack_value.text = weapon.attack.toString()
        viewHolder.itemView.set_bonus_section.visibility = View.GONE

        viewHolder.itemView.setOnClick {
            onSelected(data)
        }

        val slotImages = weapon.slots.map {
            viewHolder.itemView.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        viewHolder.itemView.slot1.setImageDrawable(slotImages[0])
        viewHolder.itemView.slot2.setImageDrawable(slotImages[1])
        viewHolder.itemView.slot3.setImageDrawable(slotImages[2])

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
