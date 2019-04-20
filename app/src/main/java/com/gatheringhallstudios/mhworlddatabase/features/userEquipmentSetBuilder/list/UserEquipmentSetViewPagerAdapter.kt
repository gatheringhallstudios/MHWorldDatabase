package com.gatheringhallstudios.mhworlddatabase.features.userEquipmentSetBuilder.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.viewpager.widget.PagerAdapter
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import kotlinx.android.synthetic.main.listitem_skill_level_armor.view.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class UserEquipmentSetViewPagerAdapter(private val mContext: Context, private val skills: List<SkillLevel>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val startIndex = position * 5
        val endIndex = if (startIndex + 5 > skills.size) skills.size else startIndex + 5
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.view_user_equipment_skills, collection, false) as ViewGroup
        for (item in skills.subList(startIndex, endIndex)) {
            val skillLayout = LayoutInflater.from(mContext).inflate(R.layout.listitem_skill_level_compact, layout, false)
            skillLayout.label_text.text = item.skillTree.name
            skillLayout.skill_level.maxLevel = item.skillTree.max_level
            skillLayout.skill_level.level = item.level
            layout.addView(skillLayout)
        }
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return ceil(skills.size / 5.00).roundToInt()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        val customPagerEnum = ModelObject.values()[position]
//        return mContext.getString(customPagerEnum.titleResId)
//    }

}