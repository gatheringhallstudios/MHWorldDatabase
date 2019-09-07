package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.viewpager.widget.PagerAdapter
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import kotlinx.android.synthetic.main.listitem_skill_level_armor.view.*
import kotlinx.android.synthetic.main.view_user_equipment_skills.view.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class UserEquipmentSetViewPagerAdapter(private val mContext: Context, private val skills: List<SkillLevel>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val startIndex = position * 4
        val endIndex = if (startIndex + 4 > skills.size) skills.size else startIndex + 4
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.view_user_equipment_skills, collection, false) as ViewGroup
        for (item in skills.subList(startIndex, endIndex)) {
            val skillLayout = LayoutInflater.from(mContext).inflate(R.layout.listitem_skill_level, layout, false)
            skillLayout.icon.setImageDrawable(AssetLoader.loadIconFor(item.skillTree))
            skillLayout.label_text.text = item.skillTree.name
            skillLayout.skill_level.maxLevel = item.skillTree.max_level
            skillLayout.skill_level.level = item.level
            skillLayout.level_text.text = layout.resources.getString(R.string.skill_level_qty, item.level)
            layout.skill_layout.addView(skillLayout)
        }
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return ceil(skills.size / 4.00).roundToInt()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}