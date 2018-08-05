package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View

import com.gatheringhallstudios.mhworlddatabase.adapters.MonsterRewardAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterReward
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

/**
 * Fragment for a list of monster rewards
 */
class MonsterRewardFragment : RecyclerViewFragment() {
    companion object {
        private val ARG_RANK = "RANK"

        @JvmStatic
        fun newInstance(rank: Rank): MonsterRewardFragment {
            val fragment = MonsterRewardFragment()
            fragment.arguments = BundleBuilder()
                    .putSerializable(ARG_RANK, rank)
                    .build()
            return fragment
        }
    }

    /**
     * The viewmodel of the parent fragment (aka the monster)
     */
    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(MonsterDetailViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup Adapter to display rewards and headers
        val adapter = CategoryAdapter(
                MonsterRewardAdapterDelegate {
                    getRouter().navigateItemDetail(it.item.id)
                })
        this.setAdapter(adapter)

        // Add divider
        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))

        // Get the rank. We filter by this field once we retrieve the results
        val rank = arguments?.getSerializable(ARG_RANK) as Rank?

        // Load data
        viewModel.rewards.observe(this, Observer { rewards ->
            adapter.clear()
            if (rewards == null) return@Observer

            val grouped = rewards.asSequence()
                    .filter { it.rank == rank || rank == null }
                    .groupBy { it.condition_name }

            for ((condition, value) in grouped) {
                adapter.addSubSection(condition!!, value)
            }
        })
    }
}
