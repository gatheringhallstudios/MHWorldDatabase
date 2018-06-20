package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.MonsterRewardAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SubHeaderAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SubHeader
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

import java.util.ArrayList

/**
 * Fragment for a list of monsters
 */

class MonsterRewardFragment : RecyclerViewFragment() {
    companion object {
        private val ARG_MONSTER_ID = "MONSTER_ID"
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

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(MonsterDetailViewModel::class.java)
    }

    // todo: find a better way to have bundle null safety
    private lateinit var rank : Rank
    private lateinit var adapter: BasicListDelegationAdapter<Any>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup Adapter to display rewards and headers
        val rewardDelegate = MonsterRewardAdapterDelegate(::handleRewardSelection)
        val subHeaderDelegate = SubHeaderAdapterDelegate()
        adapter = BasicListDelegationAdapter(rewardDelegate, subHeaderDelegate)
        this.setAdapter(adapter)

        // Load data
        rank = arguments!!.getSerializable(ARG_RANK) as Rank
        viewModel.rewards.observe(this, Observer(::setItems))
    }

    /**
     * Set the rewards to be displayed in the fragment
     * @param rewards items be of type Reward.
     */
    fun setItems(rewards: List<MonsterRewardView>?) {
        if (rewards == null) return

        val grouped = rewards
                .filter { it.data.rank == rank }
                .groupBy { it.condition_name }

        val itemsWithHeaders = ArrayList<Any>()
        for ((condition, value) in grouped) {
            itemsWithHeaders.add(SubHeader(condition))
            itemsWithHeaders.addAll(value)
        }

        adapter.items = itemsWithHeaders
        adapter.notifyDataSetChanged()
    }

    /**
     * Handle onClick of rewards.
     * @param object Object is guaranteed to be a Reward that was clicked
     */
    private fun handleRewardSelection(`object`: Any) {
        val (_, _, item_name) = `object` as MonsterRewardView
        Toast.makeText(context, "Clicked " + item_name!!, Toast.LENGTH_SHORT).show()
        // TODO implement reward clicking to item details
        //        Navigator nav = (Navigator)getActivity();
        //        nav.navigateTo(MonsterDetailPagerFragment.getInstance(monster));
    }
}
