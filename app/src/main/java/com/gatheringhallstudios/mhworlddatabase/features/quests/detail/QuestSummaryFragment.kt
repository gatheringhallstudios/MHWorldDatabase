package com.gatheringhallstudios.mhworlddatabase.features.quests.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentQuestSummaryBinding

/**
 * Fragment that shows view for the quest summary tab
 */
class QuestSummaryFragment : Fragment() {
    private var _binding: FragmentQuestSummaryBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(questId: Int) = QuestSummaryFragment().applyArguments {
            putInt(ARG_QUEST_ID, questId)
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(parentFragment!!).get(QuestDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentQuestSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        binding.questHeader.setIconDrawable(AssetLoader.loadIconFor(quest))
        binding.questHeader.setTitleText(quest.name)
        binding.questHeader.setSubtitleText(getString(
                R.string.quest_category_combined,
                AssetLoader.localizeQuestCategory(quest.category),
                if(quest.stars_raw > 9) " MR" else "",
                quest.stars))
        binding.questHeader.setDescriptionText(quest.objective)

        binding.questDescription.text = quest.description
    }

    private fun populateQuestLocation(location: Location?) {
        location ?: return

        binding.questLocation.setLeftIconType(IconType.PAPER)
        binding.questLocation.setLeftIconDrawable(AssetLoader.loadIconFor(location))
        binding.questLocation.setLabelText(location.name)
        binding.questLocation.setOnClickListener {
            getRouter().navigateObject(location)
        }
    }

    private fun populateQuestMonsters(monsters: List<QuestMonster>?) {
        monsters ?: return

        binding.questMonsters.removeAllViews()
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

            binding.questMonsters.addView(cell)
        }
        if (monsters.isEmpty()) {
            binding.questMonsters.addView(
                    layoutInflater.inflate(R.layout.listitem_empty_medium, binding.questMonsters, false))
        }
    }
}
