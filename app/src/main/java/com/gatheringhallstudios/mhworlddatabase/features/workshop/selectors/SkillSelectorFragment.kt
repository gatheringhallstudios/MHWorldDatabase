package com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.features.skills.list.SkillTreeListAdapter
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.fragment_equipment_filter.*
import kotlinx.android.synthetic.main.fragment_equipment_set_skill_selector.*

class SkillSelectorFragment : DialogFragment() {
    companion object {
        const val SELECTED_SKILL = "SELECTED_SKILL"
        const val SKILL_NUMBER = "SKILL_NUMBER"

        @JvmStatic
        fun newInstance(skillNumber: Int) = SkillSelectorFragment().applyArguments {
            putInt(SKILL_NUMBER, skillNumber)
        }
    }

    private val viewModel: WorkshopSelectorViewModel by lazy {
        ViewModelProviders.of(this).get(WorkshopSelectorViewModel::class.java)
    }

    var skillNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.skillNumber = arguments?.getInt(SKILL_NUMBER) ?: 0
        // makes the dialog into a full screen one
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_skill_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadSkills(AppSettings.dataLocale)

        skills_recycler_view.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))
        val adapter = SkillTreeListAdapter {
            val data = Intent()
            data.putExtra(SELECTED_SKILL, it)
            data.putExtra(SKILL_NUMBER, skillNumber)
            targetFragment?.onActivityResult(targetRequestCode, 1, data)
            dismiss()
        }

        skills_recycler_view.adapter = adapter
        viewModel.skills.observe(this, Observer {
            adapter.items = it
        })

        // Implement actions
        action_cancel.setOnClickListener {
            dismiss()
        }
    }
}