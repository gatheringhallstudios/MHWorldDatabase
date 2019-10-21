package com.gatheringhallstudios.mhworlddatabase.features.decorations.detail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeBase
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getColorCompat
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_decoration_summary.*
import kotlinx.android.synthetic.main.listitem_reward.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import java.text.DecimalFormat

private val MYSTERIOUS_FEYSTONE_COLOR = "Gray"
private val GLOWING_FEYSTONE_COLOR = "Blue"
private val WORN_FEYSTONE_COLOR = "Beige"
private val WARPED_FEYSTONE_COLOR = "Red"

class DecorationDetailFragment : androidx.fragment.app.Fragment() {
    companion object {
        const val ARG_DECORATION_ID = "DECORATION_ID"
    }

    private val viewModel: DecorationDetailViewModel by lazy {
        ViewModelProviders.of(this).get(DecorationDetailViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_decoration_summary, parent, false)
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

        decoration_drop_list.removeAllViews()

        val icon = AssetLoader.loadIconFor(decoration)
        decoration_header.setIconDrawable(icon)
        decoration_header.setTitleText(decoration.name)
        decoration_header.setSubtitleText(getString(R.string.format_rarity, decoration.rarity))
        decoration_header.setSubtitleColor(AssetLoader.loadRarityColor(decoration.rarity))

        // inner function used to inflate a feystone change row
        fun inflateFeystoneChance(nameResource: Int, chance: Double, iconColor: String): View {
            val view = layoutInflater.inflate(R.layout.listitem_reward, decoration_drop_list, false)
            view.reward_icon.setImageDrawable(context?.getVectorDrawable("Feystone", iconColor))
            view.reward_name.text = getString(nameResource)
            if (chance == 0.0) {
                view.reward_percent.text = "-"
                val color = context?.getColorCompat(R.color.textColorMedium) ?: 0
                view.reward_percent.setTextColor(color)
            } else {
                val formatter = DecimalFormat()
                formatter.maximumFractionDigits = 10 // arbitrary large number
                view.reward_percent.text = getString(
                        R.string.format_percentage, formatter.format(chance))
            }

            return view
        }

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_mysterious,
                decoration.mysterious_feystone_percent,
                MYSTERIOUS_FEYSTONE_COLOR
        ))

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_glowing,
                decoration.glowing_feystone_percent,
                GLOWING_FEYSTONE_COLOR
        ))

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_worn,
                decoration.worn_feystone_percent,
                WORN_FEYSTONE_COLOR
        ))

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_warped,
                decoration.warped_feystone_percent,
                WARPED_FEYSTONE_COLOR
        ))

        populateSkill(decoration.skillTree)
    }

    private fun populateSkill(skill: SkillTreeBase) {

        decoration_skill_list.removeAllViews()
        val view = layoutInflater.inflate(R.layout.listitem_skill_level, decoration_skill_list, false)

        view.icon.setImageDrawable(AssetLoader.loadIconFor(skill))
        view.label_text.text = skill.name

        //Decorations always only give 1 level. Sorry about the magic number
        view.level_text.text = getString(R.string.skill_level_qty, 1)
        with(view.skill_level) {
            maxLevel = skill.max_level
            level = 1
        }

        view.setOnClickListener {
            getRouter().navigateSkillDetail(skill.id)
        }

        decoration_skill_list.addView(view)
    }
}
