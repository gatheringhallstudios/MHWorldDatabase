package com.gatheringhallstudios.mhworlddatabase.features.decorations.detail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getColorCompat
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import java.text.DecimalFormat
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemSkillLevelBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemRewardBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentDecorationSummaryBinding

private val MYSTERIOUS_FEYSTONE_COLOR = "Gray"
private val GLOWING_FEYSTONE_COLOR = "Blue"
private val WORN_FEYSTONE_COLOR = "Beige"
private val WARPED_FEYSTONE_COLOR = "Red"
private val ANCIENT_FEYSTONE_COLOR = "Violet"
private val CARVED_FEYSTONE_COLOR = "Green"
private val SEALED_FEYSTONE_COLOR = "Gold"

class DecorationDetailFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentDecorationSummaryBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_DECORATION_ID = "DECORATION_ID"
    }

    private val viewModel: DecorationDetailViewModel by lazy {
        ViewModelProvider(this).get(DecorationDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val decorationData = viewModel.decorationData.value
        if (decorationData != null && BookmarksFeature.isBookmarked(decorationData)) {
            menu.findItem(R.id.action_toggle_bookmark).icon = (context!!.getDrawableCompat(R.drawable.ic_sys_bookmark_on))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.decorationData.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDecorationSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val decorationId = args!!.getInt(ARG_DECORATION_ID)

        viewModel.setDecoration(decorationId)
        viewModel.decorationData.observe(this, Observer<Decoration>(::populateDecoration))
    }

    private fun populateDecoration(decoration: Decoration?) {
        if (decoration == null) return

        setActivityTitle(decoration.name)
        //Rerender the menu bar because we are 100% sure we have the decoration data now
        activity!!.invalidateOptionsMenu()

        binding.decorationDropList.removeAllViews()

        val icon = AssetLoader.loadIconFor(decoration)
        binding.decorationHeader.setIconDrawable(icon)
        binding.decorationHeader.setTitleText(decoration.name)
        binding.decorationHeader.setSubtitleText(getString(R.string.format_rarity, decoration.rarity))
        binding.decorationHeader.setSubtitleColor(AssetLoader.loadRarityColor(decoration.rarity))

        // inner function used to inflate a feystone change row
        fun inflateFeystoneChance(nameResource: Int, chance: Double, iconColor: String): View {
            val rewardBinding = ListitemRewardBinding.inflate(
                    layoutInflater, binding.decorationDropList, false)
            rewardBinding.rewardIcon.setImageDrawable(context?.getVectorDrawable("Feystone", iconColor))
            rewardBinding.rewardName.text = getString(nameResource)
            if (chance == 0.0) {
                rewardBinding.rewardPercent.text = "-"
                val color = context?.getColorCompat(R.color.textColorMedium) ?: 0
                rewardBinding.rewardPercent.setTextColor(color)
            } else {
                val formatter = DecimalFormat()
                formatter.maximumFractionDigits = 10 // arbitrary large number
                rewardBinding.rewardPercent.text = getString(
                        R.string.format_percentage, formatter.format(chance))
            }

            return rewardBinding.root
        }

        binding.decorationDropList.addView(inflateFeystoneChance(
                R.string.decorations_chance_mysterious,
                decoration.mysterious_feystone_percent,
                MYSTERIOUS_FEYSTONE_COLOR
        ))

        binding.decorationDropList.addView(inflateFeystoneChance(
                R.string.decorations_chance_glowing,
                decoration.glowing_feystone_percent,
                GLOWING_FEYSTONE_COLOR
        ))

        binding.decorationDropList.addView(inflateFeystoneChance(
                R.string.decorations_chance_worn,
                decoration.worn_feystone_percent,
                WORN_FEYSTONE_COLOR
        ))

        binding.decorationDropList.addView(inflateFeystoneChance(
                R.string.decorations_chance_warped,
                decoration.warped_feystone_percent,
                WARPED_FEYSTONE_COLOR
        ))

        binding.decorationDropList.addView(inflateFeystoneChance(
                R.string.decorations_chance_ancient,
                decoration.ancient_feystone_percent,
                ANCIENT_FEYSTONE_COLOR
        ))

        binding.decorationDropList.addView(inflateFeystoneChance(
                R.string.decorations_chance_carved,
                decoration.carved_feystone_percent,
                CARVED_FEYSTONE_COLOR
        ))

        binding.decorationDropList.addView(inflateFeystoneChance(
                R.string.decorations_chance_sealed,
                decoration.sealed_feystone_percent,
                SEALED_FEYSTONE_COLOR
        ))

        populateSkills(decoration.getSkillLevels())
    }

    private fun populateSkills(skills: List<SkillLevel>) {
        binding.decorationSkillList.removeAllViews()

        skills.forEach {skillLevel ->
            val skillBinding = ListitemSkillLevelBinding.inflate(
                    layoutInflater, binding.decorationSkillList, false)

            skillBinding.icon.setImageDrawable(AssetLoader.loadIconFor(skillLevel.skillTree))
            skillBinding.labelText.text = skillLevel.skillTree.name
            skillBinding.levelText.text = getString(R.string.level_qty, skillLevel.level)
            with(skillBinding.skillLevel) {
                maxLevel = skillLevel.skillTree.max_level
                secretLevels = skillLevel.skillTree.secret
                level = skillLevel.level
            }

            skillBinding.root.setOnClickListener {
                getRouter().navigateSkillDetail(skillLevel.skillTree.id)
            }

            binding.decorationSkillList.addView(skillBinding.root)
        }

    }
}
