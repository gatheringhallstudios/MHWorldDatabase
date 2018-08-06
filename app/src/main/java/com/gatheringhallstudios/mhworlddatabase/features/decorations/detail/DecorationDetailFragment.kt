package com.gatheringhallstudios.mhworlddatabase.features.decorations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeBase
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import kotlinx.android.synthetic.main.fragment_decoration_summary.*
import kotlinx.android.synthetic.main.listitem_reward.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*


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

        decoration_skill_list.removeAllViews()

        val icon = AssetLoader.loadIconFor(decoration)
        decoration_header.setIconDrawable(icon)
        decoration_header.setTitleText(decoration.name)
        decoration_header.setSubtitleText(getString(R.string.rarity_string, decoration.rarity))
        decoration_header.setSubtitleColor(AssetLoader.loadRarityColor(decoration.rarity))

        val view2 = layoutInflater.inflate(R.layout.listitem_reward, decoration_drop_list, false)
        //TODO: load feystone icon
        //view.icon.setImageDrawable(AssetLoader.loadIconFor()
        view2.reward_name.text = getString(R.string.decorations_mysterious_feystone_chance)
        view2.reward_percent.text = getString(R.string.percentage, decoration.mysterious_feystone_chance.toString())
        decoration_drop_list.addView(view2)

        val view = layoutInflater.inflate(R.layout.listitem_reward, decoration_drop_list, false)
        //TODO: load feystone icon
        //view.icon.setImageDrawable(AssetLoader.loadIconFor()
        view.reward_name.text = getString(R.string.decorations_gleaming_feystone_chance)
        view.reward_percent.text = getString(R.string.percentage, decoration.glowing_feystone_chance.toString())
        decoration_drop_list.addView(view)

        val view3 = layoutInflater.inflate(R.layout.listitem_reward, decoration_drop_list, false)
        //TODO: load feystone icon
        //view.icon.setImageDrawable(AssetLoader.loadIconFor()
        view3.reward_name.text = getString(R.string.decorations_worn_feystone_chance)
        view3.reward_percent.text = getString(R.string.percentage, decoration.worn_feystone_chance.toString())
        decoration_drop_list.addView(view3)

        val view4 = layoutInflater.inflate(R.layout.listitem_reward, decoration_drop_list, false)
        view4.reward_name.text = getString(R.string.decorations_warped_feystone_chance)
        view4.reward_percent.text = getString(R.string.percentage, decoration.warped_feystone_chance.toString())
        decoration_drop_list.addView(view4)

        populateSkill(decoration.skillTree)
    }

    private fun populateSkill(skill: SkillTreeBase) {

        decoration_skill_list.removeAllViews()
        val view = layoutInflater.inflate(R.layout.listitem_skill_level, decoration_skill_list, false)

        view.icon.setImageDrawable(AssetLoader.loadIconFor(skill))
        view.label_text.text = skill.name

        //Decorations always only give 1 level. Sorry about the magic number
        view.level_text.text = getString(R.string.skills_level_qty, 1)
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
