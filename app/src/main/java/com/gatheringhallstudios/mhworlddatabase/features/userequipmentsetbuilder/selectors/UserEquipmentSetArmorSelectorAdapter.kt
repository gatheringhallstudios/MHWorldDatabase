package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.Router
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleRecyclerViewAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorFull
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSetBonus
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.ConvertElevationToAlphaConvert
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.listitem_armorset_armor.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*


class UserEquipmentSetArmorSelectorAdapter(private val onSelected: (ArmorFull) -> Unit) : SimpleRecyclerViewAdapter<ArmorFull>() {

    val TAG = this.javaClass.simpleName
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var context: Context
    private lateinit var router : Router

    override fun onCreateView(parent: ViewGroup): View {
        return ExpandableCardView(parent.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        router = parent.getRouter()
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: ArmorFull) {
        val armor = data.armor
        (viewHolder.itemView as ExpandableCardView).setCardElevation(4f)
        viewHolder.itemView.equipment_name.text = armor.name
        viewHolder.itemView.rarity_string.text = viewHolder.resources.getString(R.string.format_rarity, armor.rarity)
        viewHolder.itemView.rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.rarity))
        viewHolder.itemView.rarity_string.visibility = View.VISIBLE
        viewHolder.itemView.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor))
        viewHolder.itemView.defense_value.text = viewHolder.resources.getString(
                R.string.armor_defense_value,
                armor.defense_base,
                armor.defense_max,
                armor.defense_augment_max)

        val slotImages = armor.slots.map {
            viewHolder.itemView.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        viewHolder.slot1.setImageDrawable(slotImages[0])
        viewHolder.slot2.setImageDrawable(slotImages[1])
        viewHolder.slot3.setImageDrawable(slotImages[2])

        viewHolder.itemView.setOnClick {
            onSelected(data)
        }

        viewHolder.itemView.decorations_section.visibility = View.GONE
        populateSkills(data.skills, viewHolder.itemView.skill_section)
        populateSetBonuses(data.setBonuses, viewHolder.itemView.set_bonus_section)
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

    private fun populateSetBonuses(armorSetBonuses: List<ArmorSetBonus>, setBonusSection: LinearLayout) {
        if (armorSetBonuses.isEmpty()) {
            setBonusSection.visibility = View.GONE
            return
        }

        // show set bonus section
        setBonusSection.visibility = View.VISIBLE
        setBonusSection.set_bonus_list.removeAllViews()

        //Now to set the actual skills
        for (setBonus in armorSetBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = layoutInflater.inflate(R.layout.listitem_armorset_bonus, setBonusSection.set_bonus_list, false)

            listItem.bonus_skill_icon.setImageDrawable(skillIcon)
            listItem.bonus_skill_name.text = setBonus.skillTree.name
            listItem.bonus_requirement.setImageResource(reqIcon)

            listItem.setOnClickListener {
                router.navigateSkillDetail(setBonus.skillTree.id)
            }

            setBonusSection.set_bonus_list.addView(listItem)
        }
    }
}
