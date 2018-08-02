package com.gatheringhallstudios.mhworlddatabase.features.skills.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Skill
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeFull
import kotlinx.android.synthetic.main.fragment_skill_summary.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.*


class SkillDetailFragment : Fragment() {
    companion object {
        const val ARG_SKILLTREE_ID = "SKILL"
    }

    // Wraps the adapter used by SkillDetailFragment.
    // Make sure to remove the reference from recyclerview to avoid a leak
    private val adapterBuilder = SkillDetailAdapterWrapper()

    private val viewModel: SkillDetailViewModel by lazy {
        ViewModelProviders.of(this).get(SkillDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_skill_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // needs to also be removed in onDestroyView()
        recycler_view.adapter = adapterBuilder.adapter
        recycler_view.isNestedScrollingEnabled = false

        val divider = ChildDivider(DashedDividerDrawable(context!!))
        recycler_view.addItemDecoration(divider)

        viewModel.setSkill(arguments?.getInt(ARG_SKILLTREE_ID) ?: -1)
        viewModel.skillTreeFull.observe(this, Observer(::populateSkill))

        viewModel.decorations.observe(this, Observer {
            if (it != null) {
                val title = getString(R.string.skills_decoration_header)
                adapterBuilder.setDecorations(title, it)
            }
        })

        viewModel.charms.observe(this, Observer {
            if (it != null) {
                val title = getString(R.string.skills_charm_header)
                adapterBuilder.setCharms(title, it)
            }
        })

        viewModel.armorPieces.observe(this, Observer {
            if (it != null) {
                val title = getString(R.string.skills_armor_header)
                adapterBuilder.setArmor(title, it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // avoid mem leak. RecyclerViews create circular references inherently.
        recycler_view.adapter = null
    }

    private fun populateSkill(skillTreeFull: SkillTreeFull?) {
        if (skillTreeFull == null) return

        (activity as AppCompatActivity).supportActionBar?.title = skillTreeFull.name

        val icon = AssetLoader.loadIconFor(skillTreeFull)
        skill_label.setIconDrawable(icon)
        skill_label.setTitleText(skillTreeFull.name)
        skill_label.setDescriptionText(skillTreeFull.description)
        skill_label.removeDecorator()
        populateDescriptions(skillTreeFull.skills)
    }

    private fun populateDescriptions(skills: List<Skill>) {
        if (skill_level_descriptions.childCount > 0)
            skill_level_descriptions.removeAllViews()

        if (skills.isEmpty()) {
            insertEmptyState(skill_level_descriptions)
            return
        }

        for ((i, skill) in skills.withIndex()) {
            val view = layoutInflater.inflate(R.layout.listitem_skill_description, skill_level_descriptions, false)
            view.level_text.text = getString(R.string.skills_level_short_qty, i + 1)
            view.level_description.text = skill.description

            skill_level_descriptions.addView(view)
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
