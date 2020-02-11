package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSetBonus
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_user_equipment_set_body_expandable_cardview.view.*



class UserEquipmentSetAdapterDelegate(private val dataSet: MutableList<UserEquipmentSet>, private val onSelect: (UserEquipmentSet) -> Unit,
                                      private val onDelete: (UserEquipmentSet, pos: Int,
                                                             adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType != 0) {
            val view = ExpandableCardView(parent.context)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val marginSize = parent.context.resources.getDimension(R.dimen.margin_medium).toInt()
            layoutParams.setMargins(marginSize, marginSize, marginSize, 0)
            view.layoutParams = layoutParams
            EquipmentSetHolder(view)
        } else {
            val view = ExpandableCardView(parent.context)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val marginSize = parent.context.resources.getDimension(R.dimen.margin_medium).toInt()
            layoutParams.setMargins(marginSize, marginSize, marginSize, 0)
            view.layoutParams = layoutParams
            NewEquipmentSetHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].id == 0) 0 else 1
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val userEquipmentSet = dataSet[position]

        if (viewHolder is EquipmentSetHolder) {
            (viewHolder.view as ExpandableCardView).setOnClick { onSelect(userEquipmentSet) }
            viewHolder.view.setOnSwipeRight {
                onDelete(userEquipmentSet, position, this)
                notifyItemRemoved(position)
            }

            viewHolder.view.setHeader(R.layout.view_user_equipment_set_header_expandable_cardview)
            viewHolder.view.setBody(R.layout.view_user_equipment_set_body_expandable_cardview)
            viewHolder.view.setCardElevation(1f)
            viewHolder.bind(userEquipmentSet)
        } else {
            val vh = viewHolder as NewEquipmentSetHolder
            (vh.view as ExpandableCardView).setHeader(R.layout.view_new_set_header_expandable_cardview)
            vh.view.setCardElevation(1f)
            vh.view.setOnClick { onSelect(userEquipmentSet) }
        }
    }

    internal inner class EquipmentSetHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceType")
        fun bind(data: UserEquipmentSet) {
            view.card_header.equipment_icon.setImageDrawable(view.context!!.getVectorDrawable("ArmorSet", "rare${data.maxRarity}"))

            view.card_header.equipment_name.text = data.name
            view.card_header.icon_slots.visibility = View.GONE
            view.card_header.slot1.visibility = View.GONE
            view.card_header.slot2.visibility = View.GONE
            view.card_header.slot3.visibility = View.GONE
            view.card_header.icon_defense.visibility = View.GONE
            view.card_header.defense_value.visibility = View.GONE
            view.card_body.skill_section.visibility = if (data.skills.isNotEmpty()) View.VISIBLE else View.GONE
            view.card_body.set_bonus_section.visibility = if (data.setBonuses.isNotEmpty()) View.VISIBLE else View.GONE
            val adapter = UserEquipmentSetViewPagerAdapter(view.context, data.skills)
            view.skill_pager.adapter = adapter
            view.skill_pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    //Adjust height
                    val pageSize = adapter.getMaxPageSize(position) * view.resources.getDimension(R.dimen.row_height_medium)
                    val layoutParams = view.skill_pager.layoutParams
                    layoutParams.height = pageSize.toInt()
                }
            })
            view.worm_dots_indicator.setViewPager2(view.skill_pager)

            data.setBonuses.forEach {
                populateArmorSetBonusName(it.key)
                populateArmorSetBonuses(it.value)
            }
        }

        private fun populateArmorSetBonusName(setBonusName: String) {
            val textView = TextView(view.context)
            textView.text = setBonusName
            TextViewCompat.setTextAppearance(textView, R.style.TextHeadlineMedium)
            view.card_body.set_bonus_list.addView(textView)
        }

        private fun populateArmorSetBonuses(setBonuses: List<ArmorSetBonus>) {
            for (setBonus in setBonuses) {
                val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
                val reqIcon = SetBonusNumberRegistry(setBonus.required)
                val inflater = LayoutInflater.from(view.context)
                val listItem = inflater.inflate(R.layout.listitem_armorset_bonus, null, false)

                listItem.bonus_skill_icon.setImageDrawable(skillIcon)
                listItem.bonus_skill_name.text = setBonus.skillTree.name
                listItem.bonus_requirement.setImageResource(reqIcon)

                view.card_body.set_bonus_list.addView(listItem)
            }
        }
    }

    internal inner class NewEquipmentSetHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}

