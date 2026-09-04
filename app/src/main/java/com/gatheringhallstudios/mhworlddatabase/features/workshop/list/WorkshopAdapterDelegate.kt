package com.gatheringhallstudios.mhworlddatabase.features.workshop.list

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
import com.gatheringhallstudios.mhworlddatabase.databinding.ViewWorkshopBodyCardviewBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ViewWorkshopHeaderExpandableCardviewBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetBonusBinding



class WorkshopAdapterDelegate(private val dataSet: MutableList<UserEquipmentSet>, private val onSelect: (UserEquipmentSet) -> Unit,
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
            viewHolder.view.setOnClick { onSelect(userEquipmentSet) }
            viewHolder.view.setOnSwipeRight {
                onDelete(userEquipmentSet, position, this)
                notifyItemRemoved(position)
            }

            viewHolder.view.setHeader(R.layout.view_workshop_header_expandable_cardview)
            viewHolder.view.setBody(R.layout.view_workshop_body_cardview)
            viewHolder.view.setCardElevation(1f)
            viewHolder.bind(userEquipmentSet)
        } else {
            val vh = viewHolder as NewEquipmentSetHolder
            vh.view.setHeader(R.layout.view_workshop_header_expandable_cardview_new_set)
            vh.view.setCardElevation(1f)
            vh.view.setOnClick { onSelect(userEquipmentSet) }
        }
    }

    internal inner class EquipmentSetHolder(val view: ExpandableCardView) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceType")
        fun bind(data: UserEquipmentSet) {
            val header = ViewWorkshopHeaderExpandableCardviewBinding.bind(view.headerView)
            val body = ViewWorkshopBodyCardviewBinding.bind(view.bodyView)

            header.equipmentIcon.setImageDrawable(view.context.getVectorDrawable("ArmorSet", "rare${data.maxRarity}"))

            header.equipmentName.text = data.name
            header.iconSlots.visibility = View.GONE
            header.slot1.visibility = View.GONE
            header.slot2.visibility = View.GONE
            header.slot3.visibility = View.GONE
            header.iconDefense.visibility = View.GONE
            header.defenseValue.visibility = View.GONE
            body.skillSection.visibility = if (data.skills.isNotEmpty()) View.VISIBLE else View.GONE
            body.setBonusSection.visibility = if (data.setBonuses.isNotEmpty()) View.VISIBLE else View.GONE
            val adapter = WorkshopViewPagerAdapter(view.context, data.skills)
            body.skillPager.adapter = adapter
            body.skillPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    //Adjust height
                    val pageSize = adapter.getMaxPageSize(position) * view.resources.getDimension(R.dimen.row_height_medium)
                    val layoutParams = body.skillPager.layoutParams
                    layoutParams.height = pageSize.toInt()
                }
            })
            body.wormDotsIndicator.setViewPager2(body.skillPager)

            data.setBonuses.forEach {
                populateArmorSetBonusName(body, it.key)
                populateArmorSetBonuses(body, it.value)
            }
        }

        private fun populateArmorSetBonusName(body: ViewWorkshopBodyCardviewBinding, setBonusName: String) {
            val textView = TextView(view.context)
            textView.text = setBonusName
            TextViewCompat.setTextAppearance(textView, R.style.TextHeadlineMedium)
            body.setBonusList.addView(textView)
        }

        private fun populateArmorSetBonuses(body: ViewWorkshopBodyCardviewBinding, setBonuses: List<ArmorSetBonus>) {
            for (setBonus in setBonuses) {
                val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
                val reqIcon = SetBonusNumberRegistry(setBonus.required)
                val inflater = LayoutInflater.from(view.context)
                val listItem = ListitemArmorsetBonusBinding.inflate(inflater)

                listItem.bonusSkillIcon.setImageDrawable(skillIcon)
                listItem.bonusSkillName.text = setBonus.skillTree.name
                listItem.bonusRequirement.setImageResource(reqIcon)

                body.setBonusList.addView(listItem.root)
            }
        }
    }

    internal inner class NewEquipmentSetHolder(val view: ExpandableCardView) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}

