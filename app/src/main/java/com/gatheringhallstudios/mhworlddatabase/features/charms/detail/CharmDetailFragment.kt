package com.gatheringhallstudios.mhworlddatabase.features.charms.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmFull
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_charm_summary.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*


class CharmDetailFragment : androidx.fragment.app.Fragment() {
    companion object {
        const val ARG_CHARM_ID = "CHARM_ID"
    }

    private val viewModel: CharmDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CharmDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charm_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val decorationId = args!!.getInt(ARG_CHARM_ID)

        viewModel.setCharm(decorationId)
        viewModel.charmFullData.observe(this, Observer<CharmFull>(::populateCharm))
        viewModel.previousCharm.observe(this, Observer<CharmFull>(::populatePreviousItem))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val charmData = viewModel.charmFullData.value
        if (charmData != null && BookmarksFeature.isBookmarked(charmData)) {
            menu.findItem(R.id.action_toggle_bookmark)
                    .setIcon((context!!.getDrawableCompat(android.R.drawable.btn_star_big_on)))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.charmFullData.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    private fun populateCharm(charmData: CharmFull?) {
        if (charmData == null) return

        val charm = charmData.charm

        setActivityTitle(charm.name)

        //Rerender the menu bar because we are 100% sure we have the charm data now
        activity!!.invalidateOptionsMenu()

        charm_header.setIconDrawable(AssetLoader.loadIconFor(charm))
        charm_header.setTitleText(charm.name)
        charm_header.setSubtitleText(getString(R.string.format_rarity, charm.rarity))
        charm_header.setSubtitleColor(AssetLoader.loadRarityColor(charm.rarity))

        previous_item_layout.removeAllViews()
        insertEmptyState(previous_item_layout)

        populateComponents(charmData.components)
        populateSkills(charmData.skills)
    }

    private fun populatePreviousItem(charmData: CharmFull?) {
        if (previous_item_layout.childCount > 0) {
            previous_item_layout.removeAllViews()
        }

        if (charmData == null) {
            insertEmptyState(previous_item_layout)
            return
        }

        val view = IconLabelTextCell(context)
        val icon = AssetLoader.loadIconFor(charmData.charm)
        view.setLeftIconDrawable(icon)
        view.setLabelText(charmData.charm.name)

        view.setOnClickListener { getRouter().navigateCharmDetail(charmData.charm.id) }

        previous_item_layout.addView(view)
    }

    private fun populateComponents(components: List<ItemQuantity>) {
        if (charm_components_layout.childCount > 0) {
            charm_components_layout.removeAllViews()
        }

        if (components.isEmpty()) {
            insertEmptyState(charm_skill_layout)
            return
        }

        for (component in components) {
            val view = IconLabelTextCell(context)
            view.setLeftIconDrawable(AssetLoader.loadIconFor(component.item))
            view.setLabelText(component.item.name)
            view.setValueText("${component.quantity}")
            view.setOnClickListener { getRouter().navigateItemDetail(component.item.id) }
            charm_components_layout.addView(view)
        }
    }

    private fun insertEmptyState(layout: LinearLayout) {
        val icon = AppCompatResources.getDrawable(context!!, R.drawable.ic_question_mark)
        val view = IconLabelTextCell(context)
        view.setLeftIconDrawable(icon)
        view.setLabelText(getString(R.string.general_none))

        layout.addView(view)
    }

    private fun populateSkills(skills: List<SkillLevel>) {
        if (charm_skill_layout.childCount > 0)
            charm_skill_layout.removeAllViews()

        if (skills.isEmpty()) {
            insertEmptyState(charm_skill_layout)
            return
        }

        val inflater = LayoutInflater.from(context)

        for (skill in skills) {
            val view = inflater.inflate(R.layout.listitem_skill_level, charm_skill_layout, false)

            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            view.label_text.text = skill.skillTree.name
            view.level_text.text = getString(R.string.skill_level_qty, skill.level)
            with(view.skill_level) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            view.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            charm_skill_layout.addView(view)
        }
    }
}
