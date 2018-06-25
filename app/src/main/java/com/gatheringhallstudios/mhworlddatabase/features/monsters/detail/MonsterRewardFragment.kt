package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.MonsterRewardAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SubHeaderAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SubHeader
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

import java.util.ArrayList

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

    /**
     * The rank pertaining to this reward fragment
     */
    private var rank : Rank? = null

    private val adapter = CategoryAdapter(
            MonsterRewardAdapterDelegate({
                getRouter().navigateItemDetail(it.data.item_id)
            }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup Adapter to display rewards and headers
        this.setAdapter(adapter)

        // Load data
        rank = arguments?.getSerializable(ARG_RANK) as Rank?
        viewModel.rewards.observe(this, Observer(::setItems))
    }

    /**
     * Set the rewards to be displayed in the fragment
     * @param rewards items be of type Reward.
     */
    fun setItems(rewards: List<MonsterRewardView>?) {
        adapter.clear()
        if (rewards == null) return

        val grouped = rewards.asSequence()
                .filter { it.data.rank == rank || rank == null }
                .groupBy { it.condition_name }

        for ((condition, value) in grouped) {
            adapter.addSubSection(condition!!, value)
        }

        adapter.notifyDataSetChanged()
    }
}
