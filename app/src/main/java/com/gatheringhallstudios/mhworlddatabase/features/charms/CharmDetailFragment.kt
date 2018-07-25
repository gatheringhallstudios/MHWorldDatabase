package com.gatheringhallstudios.mhworlddatabase.features.charms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.assetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmComponent
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmFull
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.fragment_charm_summary.*


class CharmDetailFragment : Fragment() {
    companion object {
        const val ARG_CHARM_ID = "CHARM_ID"
    }

    private val viewModel: CharmDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CharmDetailViewModel::class.java)
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

    fun populateCharm(charmFull: CharmFull?) {
        if (charmFull == null) return

        charm_name.text = charmFull.data.name
        charm_rarity.text = getString(R.string.rarity_string, charmFull.data.rarity)
        charm_rarity.setTextColor(assetLoader.loadRarityColor(charmFull.data.rarity))
        val icon = assetLoader.loadIconFor(charmFull)
        charm_icon.setImageDrawable(icon)

        populateComponents(charmFull.components)
        populateSkill(charmFull)
    }

    private fun populatePreviousItem(charmFull: CharmFull?) {
        if (previous_item_layout.childCount > 0) {
            previous_item_layout.removeAllViews()
        }

        if (charmFull == null) {
            insertEmptyState(previous_item_layout)
            return
        }

        val view = IconLabelTextCell(context)
        val icon = AssetLoader(view.context).loadIconFor(charmFull)
        view.setLeftIconDrawable(icon)
        view.setLabelText(charmFull.data.name)

        view.setOnClickListener { getRouter().navigateCharmDetail(charmFull.data.id) }

        previous_item_layout.addView(view)
    }

    private fun populateComponents(components: List<CharmComponent>) {
        if (charm_components_layout.childCount > 0) {
            charm_components_layout.removeAllViews()
        }

        if (components.isEmpty()) {
            insertEmptyState(charm_skill_layout)
            return
        }

        components.map {
            val view = IconLabelTextCell(context)
            view.setLeftIconDrawable(assetLoader.loadIconFor(it.result))
            view.setLabelText(it.result.name)
            view.setValueText("x${it.quantity}")
            view.setOnClickListener({ v -> getRouter().navigateItemDetail(it.result.id) })
            charm_components_layout.addView(view)
        }
    }

    private fun insertEmptyState(layout: LinearLayout) {
        val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_question_mark)
        val view = IconLabelTextCell(context)
        view.setLeftIconDrawable(icon)
        view.setLabelText(getString(R.string.none))

        layout.addView(view)
    }

    private fun populateSkill(charmFull: CharmFull) {

        if (charm_skill_layout.childCount > 0)
            charm_skill_layout.removeAllViews()

        if (charmFull.skills.isEmpty()) {
            insertEmptyState(charm_skill_layout)
            return
        }

        charmFull.skills.map {
            val view = IconLabelTextCell(context)

            val icon = assetLoader.loadSkillIcon(it.skill!!.icon_color)
            view.setLeftIconDrawable(icon)
            view.setLabelText(it.skill.name)
            view.setValueText("+${it.skillLevel} ${resources.getQuantityString(R.plurals.skills_level, it.skillLevel)}")
            view.removeDecorator()
            view.setOnClickListener { v -> getRouter().navigateSkillDetail(it.skill.id) }

            charm_skill_layout.addView(view)
        }
    }
}
