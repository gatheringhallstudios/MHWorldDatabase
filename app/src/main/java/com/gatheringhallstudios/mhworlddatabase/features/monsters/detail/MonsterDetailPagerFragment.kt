package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.models.Monster
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle

/**
 * Monster detail Hub. Displays information for a single monster.
 * All data is displayed in separate tabs.
 */

class MonsterDetailPagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_MONSTER_ID = "MONSTER_ID"
    }

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val monsterId = args!!.getInt(ARG_MONSTER_ID)

        // Retrieve and set up our ViewModel
        val viewModel = ViewModelProviders.of(this).get(MonsterDetailViewModel::class.java)
        viewModel.setMonster(monsterId)

        viewModel.monster.observe(this, Observer<Monster> {
            this.setActivityTitle(it?.name)
        })

        // Now add our tabs
        tabs.addTab(getString(R.string.tab_monsters_detail_summary)) {
            MonsterSummaryFragment()
        }
        tabs.addTab(getString(R.string.tab_monsters_detail_damage)) {
            MonsterDamageFragment()
        }
        tabs.addTab(getString(R.string.tab_monsters_detail_rewards_high_rank)) {
            MonsterRewardFragment.newInstance(Rank.HIGH)
        }
        tabs.addTab(getString(R.string.tab_monsters_detail_rewards_low_rank)) {
            MonsterRewardFragment.newInstance(Rank.LOW)
        }
    }
}
