package com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.features.skills.list.SkillTreeListAdapter
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentSkillFilterBinding

class SkillSelectorFragment : DialogFragment() {
    private var _binding: FragmentSkillFilterBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val SELECTED_SKILL = "SELECTED_SKILL"
        const val SKILL_NUMBER = "SKILL_NUMBER"

        @JvmStatic
        fun newInstance(skillNumber: Int) = SkillSelectorFragment().applyArguments {
            putInt(SKILL_NUMBER, skillNumber)
        }
    }

    private val viewModel: WorkshopSelectorViewModel by lazy {
        ViewModelProvider(this).get(WorkshopSelectorViewModel::class.java)
    }

    var skillNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.skillNumber = arguments?.getInt(SKILL_NUMBER) ?: 0
        // makes the dialog into a full screen one
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSkillFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadSkills(AppSettings.dataLocale)

        binding.scrollBody.skillsRecyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))
        val adapter = SkillTreeListAdapter {
            val data = Intent()
            data.putExtra(SELECTED_SKILL, it)
            data.putExtra(SKILL_NUMBER, skillNumber)
            targetFragment?.onActivityResult(targetRequestCode, 1, data)
            dismiss()
        }

        binding.scrollBody.skillsRecyclerView.adapter = adapter
        viewModel.skills.observe(this, Observer {
            adapter.items = it
        })

        // Implement actions
        binding.actionCancel.setOnClickListener {
            dismiss()
        }
    }
}
