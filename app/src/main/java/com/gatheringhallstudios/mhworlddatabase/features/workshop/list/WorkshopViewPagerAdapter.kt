package com.gatheringhallstudios.mhworlddatabase.features.workshop.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import kotlinx.android.synthetic.main.listitem_skill_level_armor.view.*
import kotlin.math.ceil
import kotlin.math.roundToInt


class WorkshopViewPagerAdapter(private val context: Context, private val skills: List<SkillLevel>) : RecyclerView.Adapter<WorkshopViewPagerAdapter.SkillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LinearLayout(context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        view.orientation = LinearLayout.VERTICAL
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.bind(skills, position)
    }

    override fun getItemCount(): Int {
        return ceil(skills.size / 4.00).roundToInt()
    }

    fun getMaxPageSize(position: Int): Int {
        return if (skills.size > 4) 4
        else {
            val startIndex = position * 4
            val endIndex = if (startIndex + 4 > skills.size) skills.size else startIndex + 4
            return skills.subList(startIndex, endIndex).size
        }
    }

    inner class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(skills: List<SkillLevel>, position: Int) {
            val startIndex = position * 4
            val endIndex = if (startIndex + 4 > skills.size) skills.size else startIndex + 4
            val inflater = LayoutInflater.from(context)
            for (item in skills.subList(startIndex, endIndex)) {
                val skillLayout = inflater.inflate(R.layout.listitem_skill_level, itemView as ViewGroup, false)
                with(skillLayout) {
                    icon.setImageDrawable(AssetLoader.loadIconFor(item.skillTree))
                    label_text.text = item.skillTree.name
                    skill_level.maxLevel = item.skillTree.max_level
                    skill_level.level = item.level
                    skill_level.secretLevels = item.skillTree.secret
                    level_text.text = itemView.resources.getString(R.string.level_qty, item.level)
                }

                itemView.addView(skillLayout)
            }
        }
    }
}