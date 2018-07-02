package com.gatheringhallstudios.mhworlddatabase.features.skills

import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasicView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSkillView
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeFull
import com.gatheringhallstudios.mhworlddatabase.getVectorDrawable
import kotlinx.android.synthetic.main.fragment_skill_summary.*


class SkillDetailFragment : Fragment() {
    companion object {
        const val ARG_SKILLTREE_ID = "SKILL"
    }

    private val viewModel : SkillDetailViewModel by lazy {
        ViewModelProviders.of(this).get(SkillDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_skill_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val skillTreeId = args!!.getInt(ARG_SKILLTREE_ID)

        viewModel.setSkill(skillTreeId)
        viewModel.skillTreeFull.observe(this, Observer<SkillTreeFull>(::populateSkill))
        viewModel.armorPieces.observe(this, Observer<List<ArmorSkillView>>(::populateArmor))
    }

    private fun populateSkill(skillTreeFull : SkillTreeFull?) {
        if(skillTreeFull == null) return

        val icon = view!!.context.getVectorDrawable(R.drawable.ic_armor_skill, skillTreeFull.icon_color)
        skill_icon.setImageDrawable(icon)
        skill_name.text = skillTreeFull.name

        for(i in 0..4) {
            val description : String = skillTreeFull.skills.getOrNull(i)?.description ?: "-"

            when(i) {
                0 -> lvl1_description.text = description
                1 -> lvl2_description.text = description
                2 -> lvl3_description.text = description
                3 -> lvl4_description.text = description
                4 -> lvl5_description.text = description
            }
        }
    }

    private fun populateArmor(armorSkillViews: List<ArmorSkillView>?) {
        if(armorSkillViews == null) return

        if(armor_layout.childCount > 0)
            armor_layout.removeAllViews()

        for(armorSkillView in armorSkillViews) {
            val view = IconLabelTextCell(context)

            val icon : Drawable?
            when(armorSkillView.data.armor_type) {
                ArmorType.HEAD -> icon = ContextCompat.getDrawable(context!!, R.drawable.ic_head)
                ArmorType.ARMS -> icon = ContextCompat.getDrawable(context!!, R.drawable.ic_arm)
                ArmorType.CHEST -> icon = ContextCompat.getDrawable(context!!, R.drawable.ic_chest)
                ArmorType.LEGS -> icon = ContextCompat.getDrawable(context!!, R.drawable.ic_leg)
                ArmorType.WAIST -> icon = ContextCompat.getDrawable(context!!, R.drawable.ic_waist)
            }

            val levels = "+${armorSkillView.skillLevel} ${resources.getQuantityString(R.plurals.skills_level, armorSkillView.skillLevel, armorSkillView)}"

            view.setLeftIconDrawable(icon)
            view.setLabelText(armorSkillView.name)
            view.setValueText(levels)

            armor_layout.addView(view)
        }
    }
}
