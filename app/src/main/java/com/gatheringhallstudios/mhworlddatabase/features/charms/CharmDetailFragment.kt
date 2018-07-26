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
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmFull
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
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

    private fun populateCharm(charmData: CharmFull?) {
        if (charmData == null) return

        val charm = charmData.charm

        charm_name.text = charm.name
        charm_rarity.text = getString(R.string.rarity_string, charm.rarity)
        charm_rarity.setTextColor(assetLoader.loadRarityColor(charm.rarity))

        val icon = assetLoader.loadIconFor(charm)
        charm_icon.setImageDrawable(icon)

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
        val icon = AssetLoader(view.context).loadIconFor(charmData.charm)
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
            view.setLeftIconDrawable(assetLoader.loadIconFor(component.item))
            view.setLabelText(component.item.name)
            view.setValueText("${component.quantity}")
            view.setOnClickListener { getRouter().navigateItemDetail(component.item.id) }
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

    private fun populateSkills(skills: List<SkillLevel>) {
        if (charm_skill_layout.childCount > 0)
            charm_skill_layout.removeAllViews()

        if (skills.isEmpty()) {
            insertEmptyState(charm_skill_layout)
            return
        }

        for (skillLevel in skills) {
            val view = IconLabelTextCell(context)

            val skillTree = skillLevel.skillTree

            val icon = assetLoader.loadIconFor(skillTree)
            view.setLeftIconDrawable(icon)
            view.setLabelText(skillTree.name)
            view.setValueText("+${skillLevel.level} ${resources.getQuantityString(R.plurals.skills_level, skillLevel.level)}")
            view.removeDecorator()
            view.setOnClickListener { getRouter().navigateSkillDetail(skillTree.id) }

            charm_skill_layout.addView(view)
        }
    }
}
