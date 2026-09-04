package com.gatheringhallstudios.mhworlddatabase.features.skills.detail

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Skill
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeFull
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemSkillDescriptionBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentSkillSummaryBinding


class SkillDetailFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentSkillSummaryBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_SKILLTREE_ID = "SKILL"
    }

    private val viewModel: SkillDetailViewModel by lazy {
        ViewModelProvider(this).get(SkillDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSkillSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val weaponData = viewModel.skillTreeFull.value
        if (weaponData != null && BookmarksFeature.isBookmarked(weaponData)) {
            menu.findItem(R.id.action_toggle_bookmark).icon = (context!!.getDrawableCompat(R.drawable.ic_sys_bookmark_on))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.skillTreeFull.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapterBuilder = SkillDetailAdapterWrapper()

        // needs to also be removed in onDestroyView()
        binding.recyclerView.adapter = adapterBuilder.adapter
        binding.recyclerView.isNestedScrollingEnabled = false

        val divider = ChildDivider(DashedDividerDrawable(context!!))
        binding.recyclerView.addItemDecoration(divider)

        viewModel.setSkill(arguments?.getInt(ARG_SKILLTREE_ID) ?: -1)
        viewModel.skillTreeFull.observe(viewLifecycleOwner, Observer(::populateSkill))

        viewModel.decorations.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val title = getString(R.string.header_decorations)
                adapterBuilder.setDecorations(title, it)
            }
        })

        viewModel.charms.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val title = getString(R.string.header_charms)
                adapterBuilder.setCharms(title, it)
            }
        })

        viewModel.armorPieces.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val title = getString(R.string.header_armor)
                adapterBuilder.setArmor(title, it)
            }
        })

        viewModel.bonuses.observe(viewLifecycleOwner, Observer {
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
        binding.skillLabel.setIconDrawable(icon)
        binding.skillLabel.setTitleText(skillTreeFull.name)
        binding.skillLabel.setDescriptionText(skillTreeFull.description)
        binding.skillLabel.removeDecorator()
        populateDescriptions(skillTreeFull.skills, skillTreeFull.max_level - skillTreeFull.secret)
    }

    private fun populateDescriptions(skills: List<Skill>, secretThreshold: Int) {
        if (binding.skillLevelDescriptions.childCount > 0)
            binding.skillLevelDescriptions.removeAllViews()

        if (skills.isEmpty()) {
            insertEmptyState(binding.skillLevelDescriptions)
            return
        }

        for ((i, skill) in skills.withIndex()) {
            val descriptionBinding = ListitemSkillDescriptionBinding.inflate(
                    layoutInflater, binding.skillLevelDescriptions, false)
            descriptionBinding.levelText.text = getString(R.string.level_short_qty, i + 1)
            descriptionBinding.levelDescription.text = skill.description

            if (i >= secretThreshold) {
                descriptionBinding.levelIconSecret.visibility = View.VISIBLE
            }

            binding.skillLevelDescriptions.addView(descriptionBinding.root)
        }
    }

    private fun insertEmptyState(layout: LinearLayout) {
        val icon = AppCompatResources.getDrawable(context!!, R.drawable.ic_question_mark)
        val view = IconLabelTextCell(context)
        view.setLeftIconDrawable(icon)
        view.setLabelText(getString(R.string.general_none))

        layout.addView(view)
    }
}
