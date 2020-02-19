package com.gatheringhallstudios.mhworlddatabase.features.quests.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.components.VerboseIconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestMonster
import com.gatheringhallstudios.mhworlddatabase.features.quests.detail.QuestDetailPagerFragment.Companion.ARG_QUEST_ID
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.fragment_quest_summary.*

/**
 * Fragment that shows view for the quest summary tab
 */
class QuestSummaryFragment : Fragment() {
    companion object {
        fun newInstance(questId: Int) = QuestSummaryFragment().applyArguments {
            putInt(ARG_QUEST_ID, questId)
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(QuestDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quest_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questId = arguments?.getInt(ARG_QUEST_ID) ?: -1
        viewModel.loadQuestData(questId).observe(viewLifecycleOwner, Observer(::populateQuest))
        viewModel.loadQuestLocation(questId).observe(viewLifecycleOwner, Observer(::populateQuestLocation))
        viewModel.loadQuestMonsters(questId).observe(viewLifecycleOwner, Observer(::populateQuestMonsters))
    }

    private fun populateQuest(quest: QuestBase?) {
        quest ?: return

        quest_header.setIconDrawable(AssetLoader.loadIconFor(quest))
        quest_header.setTitleText(quest.name)
        quest_header.setSubtitleText(getString(R.string.quest_category_combined,
                    AssetLoader.localizeQuestCategory(quest.category), quest.stars))
        quest_header.setDescriptionText(quest.objective)

        quest_description.text = quest.description
    }

    private fun populateQuestLocation(location: Location?) {
        location ?: return

        quest_location.setLeftIconType(IconType.PAPER)
        quest_location.setLeftIconDrawable(AssetLoader.loadIconFor(location))
        quest_location.setLabelText(location.name)
        quest_location.setOnClickListener {
            getRouter().navigateObject(location)
        }
    }

    private fun populateQuestMonsters(monsters: List<QuestMonster>?) {
        monsters ?: return

        quest_monsters.removeAllViews()
        for (qmonster in monsters) {
            val cell = VerboseIconLabelTextCell(context!!)
            with (cell.binder) {
                setIconDrawable(AssetLoader.loadIconFor(qmonster.monster))
                setLabelText(qmonster.monster.name)
                setSubValueText("x ${qmonster.quantity}")

                if (qmonster.is_objective) {
                    setSubLabelText(getString(R.string.quest_label_objective))
                }
            }

            cell.setOnClickListener {
                getRouter().navigateObject(qmonster.monster)
            }

            quest_monsters.addView(cell)
        }
        if (monsters.isEmpty()) {
            quest_monsters.addView(
                    layoutInflater.inflate(R.layout.listitem_empty_medium, quest_monsters, false))
        }
    }
}