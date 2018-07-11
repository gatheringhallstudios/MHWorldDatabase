package com.gatheringhallstudios.mhworlddatabase.features.skills

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.VectorArmorRegistry
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import kotlinx.android.synthetic.main.fragment_skill_summary.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.*


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
        viewModel.charms.observe(this, Observer<List<CharmSkillView>>(::populateCharms))
        viewModel.decorations.observe(this, Observer<List<DecorationView>>(::populateDecorations))
    }

    private fun populateSkill(skillTreeFull : SkillTreeFull?) {
        if(skillTreeFull == null) return

        val icon = view!!.context.getVectorDrawable(R.drawable.ic_ui_armor_skill_base, skillTreeFull.icon_color)
        skill_icon.setImageDrawable(icon)
        skill_name.text = skillTreeFull.name

        populateDescriptions(skillTreeFull.skills)
    }

    private fun populateArmor(armorSkillViews: List<ArmorSkillView>?) {
        if(armorSkillViews == null) return

        if(armor_layout.childCount > 0)
            armor_layout.removeAllViews()

        for(armorSkillView in armorSkillViews) {
            val view = IconLabelTextCell(context)
            val levels = "+${armorSkillView.skillLevel} ${resources.getQuantityString(R.plurals.skills_level, armorSkillView.skillLevel)}"

            val icon = AssetLoader(context!!).loadArmorIcon(armorSkillView.data)
            view.setLeftIconDrawable(icon)

            view.setLabelText(armorSkillView.name)
            view.setValueText(levels)
            //TODO: link up on click listener to armor detail page once done
            //view.setOnClickListener {v -> getRouter().navigateArmorDetail()}

            armor_layout.addView(view)
        }
    }

    private fun populateDescriptions(skills : List<Skill>) {
        if(skill_descriptions.childCount > 0)
            skill_descriptions.removeAllViews()

        for(i in 0..4) {
            val view = layoutInflater.inflate(R.layout.listitem_skill_description, null)
            val description : String? = skills.getOrNull(i)?.description

            if(description.isNullOrBlank()) return
            view.level_text.text = "${getString(R.string.skills_level_title)} ${i + 1}"
            view.level_description.text = description

            skill_descriptions.addView(view)
        }
    }

    private fun populateCharms(charmSkillViews: List<CharmSkillView>?) {
        if(charmSkillViews == null) return

        if(charm_layout.childCount > 0)
            charm_layout.removeAllViews()

        for(charmSkillView in charmSkillViews) {
            val view = IconLabelTextCell(context)

            val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_question_mark)
            val levels = "+${charmSkillView.skillLevel} ${resources.getQuantityString(R.plurals.skills_level, charmSkillView.skillLevel)}"

            view.setLeftIconDrawable(icon)
            view.setLabelText(charmSkillView.name)
            view.setValueText(levels)
            //TODO: link up on click listener to charm detail page once done
            //view.setOnClickListener {v -> getRouter().navigateCharmDetail()}

            charm_layout.addView(view)
        }
    }

    private fun populateDecorations(decorationViews: List<DecorationView>?) {
        if(decorationViews == null) return

        if(decoration_layout.childCount > 0) {
            decoration_layout.removeAllViews()
        }

        for(decorationView in decorationViews) {
            val view = IconLabelTextCell(context)

            val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_ui_armor_skill_base) // TODO Replace with decoration icon when available
            //Decorations are only ever +1 point
            val levels = "+1 ${resources.getQuantityString(R.plurals.skills_level, 1)}"

            view.setLeftIconDrawable(icon)
            view.setLabelText(decorationView.name)
            view.setValueText(levels)

            view.setOnClickListener({v -> getRouter().navigateDecorationDetail(decorationView.id)})
            decoration_layout.addView(view)
        }
    }
}
