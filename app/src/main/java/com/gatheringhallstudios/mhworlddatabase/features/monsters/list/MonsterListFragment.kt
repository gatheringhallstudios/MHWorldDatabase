package com.gatheringhallstudios.mhworlddatabase.features.monsters.list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.MonsterAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

/**
 * Fragment for a list of monsters
 */

class MonsterListFragment : RecyclerViewFragment() {
    companion object {
        private val ARG_TAB = "MONSTER_TAB"

        @JvmStatic
        fun newInstance(tab: MonsterListViewModel.Tab): MonsterListFragment {
            val f = MonsterListFragment()
            f.arguments = BundleBuilder().putSerializable(ARG_TAB, tab).build()
            return f
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MonsterListViewModel::class.java)
    }

    // Setup adapter and navigation
    private val adapter = BasicListDelegationAdapter(MonsterAdapterDelegate({
        getRouter().navigateMonsterDetail(it.id)
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        var tab: MonsterListViewModel.Tab = MonsterListViewModel.Tab.LARGE // default
        val args = arguments
        if (args != null) {
            tab = args.getSerializable(ARG_TAB) as MonsterListViewModel.Tab
        }

        viewModel.setTab(tab)

        viewModel.monsters.observe(this, Observer<List<MonsterView>>({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))
    }
}
