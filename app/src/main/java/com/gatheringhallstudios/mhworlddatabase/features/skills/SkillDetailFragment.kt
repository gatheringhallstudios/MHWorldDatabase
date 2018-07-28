package com.gatheringhallstudios.mhworlddatabase.features.skills

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.fragment_skill_summary.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.*


class SkillDetailFragment : Fragment() {
    companion object {
        const val ARG_SKILLTREE_ID = "SKILL"
    }

    private val viewModel: SkillDetailViewModel by lazy {
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
        viewModel.skillTreeFull.observe(this, Observer(::populateSkill))
        viewModel.armorPieces.observe(this, Observer(::populateArmor))
        viewModel.charms.observe(this, Observer(::populateCharms))
        viewModel.decorations.observe(this, Observer(::populateDecorations))
    }

    private fun populateSkill(skillTreeFull: SkillTreeFull?) {
        if (skillTreeFull == null) return

        val icon = assetLoader.loadIconFor(skillTreeFull)
        skill_label.setIconDrawable(icon)
        skill_label.setTitleText(skillTreeFull.name)
        skill_label.setDescriptionText(skillTreeFull.description)
        skill_label.removeDecorator()
        populateDescriptions(skillTreeFull.skills)
    }

    private fun populateArmor(armorSkills: List<ArmorSkillLevel>?) {
        if (armor_layout.childCount > 0)
            armor_layout.removeAllViews()

        if (armorSkills.orEmpty().isEmpty()) {
            insertEmptyState(armor_layout)
            return
        }

        for (armorSkillView in armorSkills!!) {
            val view = IconLabelTextCell(context)
            val levels = "+${armorSkillView.level} ${resources.getQuantityString(R.plurals.skills_level, armorSkillView.level)}"

            val icon = assetLoader.loadIconFor(armorSkillView.armor)
            view.setLeftIconDrawable(icon)

            view.setLabelText(armorSkillView.armor.name)
            view.setValueText(levels)
            view.setOnClickListener { getRouter().navigateArmorDetail(armorSkillView.armor.id) }

            armor_layout.addView(view)
        }
    }

    private fun populateDescriptions(skills: List<Skill>) {
        if (skill_level_descriptions.childCount > 0)
            skill_level_descriptions.removeAllViews()

        if (skills.isEmpty()) {
            insertEmptyState(skill_level_descriptions)
            return
        }

        for (i in 0..4) {
            val view = layoutInflater.inflate(R.layout.listitem_skill_description, skill_level_descriptions, false)
            val description: String? = skills.getOrNull(i)?.description

            if (description.isNullOrBlank()) return
            view.level_text.text = "${getString(R.string.skills_level_title)} ${i + 1}"
            view.level_description.text = description

            skill_level_descriptions.addView(view)
        }
    }

    private fun populateCharms(charmSkills: List<CharmSkillLevel>?) {
        if (charm_layout.childCount > 0)
            charm_layout.removeAllViews()

        if (charmSkills.orEmpty().isEmpty()) {
            insertEmptyState(charm_layout)
            return
        }

        for (charmSkillView in charmSkills!!) {
            val view = IconLabelTextCell(context)

            val icon = assetLoader.loadIconFor(charmSkillView.charm!!)
            val levels = "+${charmSkillView.level} ${resources.getQuantityString(R.plurals.skills_level, charmSkillView.level)}"

            view.setLeftIconDrawable(icon)
            view.setLabelText(charmSkillView.charm.name)
            view.setValueText(levels)
            view.setOnClickListener { getRouter().navigateCharmDetail(charmSkillView.charm.id)}

            charm_layout.addView(view)
        }
    }

    private fun populateDecorations(decorations: List<DecorationBase>?) {
        if (decoration_layout.childCount > 0) {
            decoration_layout.removeAllViews()
        }

        if(decorations.orEmpty().isEmpty()) {
            insertEmptyState(decoration_layout)
            return
        }

        for (decorationView in decorations!!) {
            val view = IconLabelTextCell(context)

            val icon = assetLoader.loadIconFor(decorationView)

            //Decorations are only ever +1 point
            val levels = "+1 ${resources.getQuantityString(R.plurals.skills_level, 1)}"

            view.setLeftIconDrawable(icon)
            view.setLabelText(decorationView.name)
            view.setValueText(levels)

            view.setOnClickListener {
                getRouter().navigateDecorationDetail(decorationView.id)
            }

            decoration_layout.addView(view)
        }
    }

    private fun insertEmptyState(layout: LinearLayout) {
        val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_question_mark)
        val view = IconLabelTextCell(context)
        view.setLeftIconDrawable(icon)
        view.setLabelText(getString(R.string.none))

        layout.addView(view)
    }
}
