package com.gatheringhallstudios.mhworlddatabase.features.skills.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Skill
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeFull
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_skill_summary.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.*


class SkillDetailFragment : androidx.fragment.app.Fragment() {
    companion object {
        const val ARG_SKILLTREE_ID = "SKILL"
    }

    private val viewModel: SkillDetailViewModel by lazy {
        ViewModelProviders.of(this).get(SkillDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_skill_summary, parent, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_favoritable, menu)
        val weaponData = viewModel.skillTreeFull.value
        if (weaponData != null && BookmarksFeature.isBookmarked(weaponData)) {
            menu.findItem(R.id.action_toggle_favorite)
                    .setIcon((context!!.getDrawableCompat(android.R.drawable.btn_star_big_on)))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the favorites button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_favorite) {
            BookmarksFeature.toggleBookmark(viewModel.skillTreeFull.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapterBuilder = SkillDetailAdapterWrapper()

        // needs to also be removed in onDestroyView()
        recycler_view.adapter = adapterBuilder.adapter
        recycler_view.isNestedScrollingEnabled = false

        val divider = ChildDivider(DashedDividerDrawable(context!!))
        recycler_view.addItemDecoration(divider)

        viewModel.setSkill(arguments?.getInt(ARG_SKILLTREE_ID) ?: -1)
        viewModel.skillTreeFull.observe(this, Observer(::populateSkill))

        viewModel.decorations.observe(this, Observer {
            if (it != null) {
                val title = getString(R.string.header_decorations)
                adapterBuilder.setDecorations(title, it)
            }
        })

        viewModel.charms.observe(this, Observer {
            if (it != null) {
                val title = getString(R.string.header_charms)
                adapterBuilder.setCharms(title, it)
            }
        })

        viewModel.armorPieces.observe(this, Observer {
            if (it != null) {
                val title = getString(R.string.header_armor)
                adapterBuilder.setArmor(title, it)
            }
        })

        viewModel.bonuses.observe(this, Observer {
            if (it != null) {
                val title = getString(R.string.header_set_bonuses)
                adapterBuilder.setArmorSetBonuses(title, it)
            }
        })
    }

    private fun populateSkill(skillTreeFull: SkillTreeFull?) {
        if (skillTreeFull == null) return

        (activity as AppCompatActivity).supportActionBar?.title = skillTreeFull.name

        //Rerender the menu bar because we are 100% sure we have the skill tree data now
        activity!!.invalidateOptionsMenu()

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
            view.level_text.text = getString(R.string.skill_level_short_qty, i + 1)
            view.level_description.text = skill.description

            skill_level_descriptions.addView(view)
        }
    }

    private fun insertEmptyState(layout: LinearLayout) {
        val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_question_mark)
        val view = IconLabelTextCell(context)
        view.setLeftIconDrawable(icon)
        view.setLabelText(getString(R.string.general_none))

        layout.addView(view)
    }
}
