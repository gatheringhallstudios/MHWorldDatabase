package com.gatheringhallstudios.mhworlddatabase.features.charms.detail

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemSkillLevelBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentCharmSummaryBinding


class CharmDetailFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentCharmSummaryBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_CHARM_ID = "CHARM_ID"
    }

    private val viewModel: CharmDetailViewModel by lazy {
        ViewModelProvider(this).get(CharmDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCharmSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            menu.findItem(R.id.action_toggle_bookmark).icon = (context!!.getDrawableCompat(R.drawable.ic_sys_bookmark_on))
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

        binding.charmHeader.setIconDrawable(AssetLoader.loadIconFor(charm))
        binding.charmHeader.setTitleText(charm.name)
        binding.charmHeader.setSubtitleText(getString(R.string.format_rarity, charm.rarity))
        binding.charmHeader.setSubtitleColor(AssetLoader.loadRarityColor(charm.rarity))

        binding.previousItemLayout.removeAllViews()
        insertEmptyState(binding.previousItemLayout)

        populateComponents(charmData.components)
        populateSkills(charmData.skills)
    }

    private fun populatePreviousItem(charmData: CharmFull?) {
        if (binding.previousItemLayout.childCount > 0) {
            binding.previousItemLayout.removeAllViews()
        }

        if (charmData == null) {
            insertEmptyState(binding.previousItemLayout)
            return
        }

        val view = IconLabelTextCell(context)
        val icon = AssetLoader.loadIconFor(charmData.charm)
        view.setLeftIconDrawable(icon)
        view.setLabelText(charmData.charm.name)

        view.setOnClickListener { getRouter().navigateCharmDetail(charmData.charm.id) }

        binding.previousItemLayout.addView(view)
    }

    private fun populateComponents(components: List<ItemQuantity>) {
        if (binding.charmComponentsLayout.childCount > 0) {
            binding.charmComponentsLayout.removeAllViews()
        }

        if (components.isEmpty()) {
            insertEmptyState(binding.charmSkillLayout)
            return
        }

        for (component in components) {
            val view = IconLabelTextCell(context)
            view.setLeftIconDrawable(AssetLoader.loadIconFor(component.item))
            view.setLabelText(component.item.name)
            view.setValueText("${component.quantity}")
            view.setOnClickListener { getRouter().navigateItemDetail(component.item.id) }
            binding.charmComponentsLayout.addView(view)
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
        if (binding.charmSkillLayout.childCount > 0)
            binding.charmSkillLayout.removeAllViews()

        if (skills.isEmpty()) {
            insertEmptyState(binding.charmSkillLayout)
            return
        }

        val inflater = LayoutInflater.from(context)

        for (skill in skills) {
            val skillBinding = ListitemSkillLevelBinding.inflate(inflater, binding.charmSkillLayout, false)

            skillBinding.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            skillBinding.labelText.text = skill.skillTree.name
            skillBinding.levelText.text = getString(R.string.level_qty, skill.level)
            with(skillBinding.skillLevel) {
                maxLevel = skill.skillTree.max_level
                secretLevels = skill.skillTree.secret
                level = skill.level
            }

            skillBinding.root.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            binding.charmSkillLayout.addView(skillBinding.root)
        }
    }
}
