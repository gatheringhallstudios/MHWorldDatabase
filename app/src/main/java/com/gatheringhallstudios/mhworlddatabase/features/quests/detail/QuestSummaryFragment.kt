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
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.features.quests.detail.QuestDetailPagerFragment.Companion.ARG_QUEST_ID
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.fragment_quest_summary.*

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
    }

    private fun populateQuest(quest: QuestBase?) {
        if (quest == null) return

        quest_header.setIconDrawable(AssetLoader.loadIconFor(quest))
        quest_header.setTitleText(quest.name)
        quest_header.setSubtitleText(getString(R.string.quest_category_combined,
                    AssetLoader.localizeQuestCategory(quest.category), quest.stars))
        quest_header.setDescriptionText(quest.objective)
    }
}