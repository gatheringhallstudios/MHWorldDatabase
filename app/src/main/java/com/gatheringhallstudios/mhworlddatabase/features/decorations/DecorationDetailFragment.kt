package com.gatheringhallstudios.mhworlddatabase.features.decorations

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeFull
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeBase
import kotlinx.android.synthetic.main.fragment_decoration_summary.*


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

        (activity as AppCompatActivity).supportActionBar!!.title = decoration.name

        val icon = assetLoader.loadIconFor(decoration)
        decoration_icon.setImageDrawable(icon)
        decoration_name.text = decoration.name
        decoration_description.text = getString(R.string.rarity_string, decoration.rarity)
        decoration_description.setTextColor(assetLoader.loadRarityColor(decoration.rarity))

        mysterious_feystone_chance_value.text = getString(R.string.percentage, decoration.mysterious_feystone_chance)
        gleaming_feystone_chance_value.text = getString(R.string.percentage, decoration.glowing_feystone_chance)
        worn_feystone_chance_value.text = getString(R.string.percentage, decoration.worn_feystone_chance)
        warped_feystone_chance_value.text = getString(R.string.percentage, decoration.warped_feystone_chance)

        populateSkill(decoration.skillTree)
    }

    private fun populateSkill(skillTreeFull: SkillTreeBase?) {
        if(skillTreeFull == null) return

        if (decoration_skill_layout.childCount > 0)
            decoration_skill_layout.removeAllViews()

        val view = IconLabelTextCell(context)

        val icon = assetLoader.loadSkillIcon(skillTreeFull.icon_color)
        view.setLeftIconDrawable(icon)
        view.setLabelText(skillTreeFull.name)
        view.removeDecorator()
        view.setOnClickListener { getRouter().navigateSkillDetail(skillTreeFull.id) }

        decoration_skill_layout.addView(view)
    }
}
