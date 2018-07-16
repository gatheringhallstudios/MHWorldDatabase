package com.gatheringhallstudios.mhworlddatabase.features.decorations

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeFull
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.data.views.DecorationView
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
        viewModel.decorationData.observe(this, Observer<DecorationView>(::populateDecoration))
        viewModel.skillTreeData.observe(this, Observer<SkillTreeFull>(::populateSkill))
    }

    private fun populateSkill(skillTreeFull: SkillTreeFull?) {
        if(skillTreeFull == null) return

        if (decoration_skill_layout.childCount > 0)
            decoration_skill_layout.removeAllViews()

        val view = IconLabelTextCell(context)

        val icon = assetLoader.loadSkillIcon(skillTreeFull.icon_color)
        view.setLeftIconDrawable(icon)
        view.setLabelText(skillTreeFull.name)
        view.removeDecorator()
        view.setOnClickListener { v -> getRouter().navigateSkillDetail(skillTreeFull.id) }

        decoration_skill_layout.addView(view)
    }

    private fun populateDecoration(decoration: DecorationView?) {
        if (decoration == null) return

        val icon = assetLoader.loadDecorationIcon(decoration)
        decoration_name.text = decoration.name
        decoration_description.text = getString(R.string.rare_label, decoration.data.rarity)
        mysterious_feystone_chance_value.text = "${decoration.data.mysterious_feystone_chance}%"
        gleaming_feystone_chance_value.text = "${decoration.data.glowing_feystone_chance}%"
        worn_feystone_chance_value.text = "${decoration.data.worn_feystone_chance}%"
        warped_feystone_chance_value.text = "${decoration.data.warped_feystone_chance}%"
        decoration_icon.setImageDrawable(icon)
        removeDecorator()
    }

    private fun removeDecorator() {
        decoration_icon.background = null
        decoration_icon.setPadding(0, 0, 0, 0)
    }
}
