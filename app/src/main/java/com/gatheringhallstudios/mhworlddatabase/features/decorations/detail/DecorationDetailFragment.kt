package com.gatheringhallstudios.mhworlddatabase.features.decorations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeBase
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getColorCompat
import kotlinx.android.synthetic.main.fragment_decoration_summary.*
import kotlinx.android.synthetic.main.listitem_reward.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*

private val MYSTERIOUS_FEYSTONE_COLOR = "Gray"
private val GLOWING_FEYSTONE_COLOR = "Blue"
private val WORN_FEYSTONE_COLOR = "Beige"
private val WARPED_FEYSTONE_COLOR = "Red"

class DecorationDetailFragment : Fragment() {
    companion object {
        const val ARG_DECORATION_ID = "DECORATION_ID"
    }

    private val viewModel: DecorationDetailViewModel by lazy {
        ViewModelProviders.of(this).get(DecorationDetailViewModel::class.java)
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
                view.reward_percent.text = getString(R.string.format_percentage, chance.toString())
            }

            return view
        }

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_mysterious,
                decoration.mysterious_feystone_chance,
                MYSTERIOUS_FEYSTONE_COLOR
        ))

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_glowing,
                decoration.glowing_feystone_chance,
                GLOWING_FEYSTONE_COLOR
        ))

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_worn,
                decoration.worn_feystone_chance,
                WORN_FEYSTONE_COLOR
        ))

        decoration_drop_list.addView(inflateFeystoneChance(
                R.string.decorations_chance_warped,
                decoration.warped_feystone_chance,
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
