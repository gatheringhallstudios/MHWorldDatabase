package com.gatheringhallstudios.mhworlddatabase.features.monsters

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterDetailViewModel
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterDamageFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterRewardFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.detail.MonsterSummaryFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

import butterknife.BindString
import butterknife.BindView

/**
 * Monster detail Hub. Displays information for a single monster.
 * All data is displayed in separate tabs.
 */

class MonsterDetailPagerFragment : BasePagerFragment() {
    companion object {
        private val ARG_MONSTER_ID = "MONSTER_ID"

        fun newInstance(monsterId: Int): MonsterDetailPagerFragment {
            val fragment = MonsterDetailPagerFragment()
            fragment.arguments = BundleBuilder()
                    .putSerializable(ARG_MONSTER_ID, monsterId)
                    .build()
            return fragment
        }

        fun newInstance(monster: MonsterView): MonsterDetailPagerFragment {
            return newInstance(monster.id)
        }
    }

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val monsterId = args!!.getInt(ARG_MONSTER_ID)

        // Retrieve and set up our ViewModel
        // note: We may want to undo the full viewmodel here, and use fragment specific viewmodels.
        // not sure, experiment with later.
        val viewModel = ViewModelProviders.of(this).get(MonsterDetailViewModel::class.java)
        viewModel.setMonster(monsterId)

        viewModel.monster.observe(this, Observer<MonsterView> { this.setTitle(it?.name) })

        // Now add our tabs
        tabs.addTab(getString(R.string.monsters_detail_tab_summary)) {
            MonsterSummaryFragment()
        }
        tabs.addTab(getString(R.string.monsters_detail_tab_damage)) {
            MonsterDamageFragment()
        }
        tabs.addTab(getString(R.string.monsters_detail_tab_rewards_high_rank)) {
            MonsterRewardFragment.newInstance(Rank.HIGH)
        }
        tabs.addTab(getString(R.string.monsters_detail_tab_rewards_low_rank)) {
            MonsterRewardFragment.newInstance(Rank.LOW)
        }
    }

    private fun setTitle(title: String?) {
        activity?.title = title
    }
}
