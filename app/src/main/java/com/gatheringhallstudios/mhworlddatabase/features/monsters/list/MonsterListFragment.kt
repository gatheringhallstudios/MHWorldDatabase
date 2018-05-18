package com.gatheringhallstudios.mhworlddatabase.features.monsters.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.adapters.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.Navigator
import com.gatheringhallstudios.mhworlddatabase.common.adapters.MonsterAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView
import com.gatheringhallstudios.mhworlddatabase.features.monsters.MonsterDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

import butterknife.ButterKnife

/**
 * Fragment for a list of monsters
 */

class MonsterListFragment : Fragment() {
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

    lateinit var adapter: BasicListDelegationAdapter<MonsterView>

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Setup Adapter
        val delegate = MonsterAdapterDelegate(::handleMonsterSelection)
        adapter = BasicListDelegationAdapter(delegate)

        // Setup RecyclerView
        val recyclerView = inflater.inflate(R.layout.list_generic, parent, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(parent!!.context)
        recyclerView.adapter = adapter

        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var tab: MonsterListViewModel.Tab = MonsterListViewModel.Tab.LARGE // default
        val args = arguments
        if (args != null) {
            tab = args.getSerializable(ARG_TAB) as MonsterListViewModel.Tab
        }

        viewModel.setTab(tab)
        viewModel.monsters.observe(this, Observer<List<MonsterView>>(::setItems))
    }

    private fun setItems(monsters: List<MonsterView>?) {
        adapter.items = monsters
        adapter.notifyDataSetChanged()
    }

    private fun handleMonsterSelection(monster: MonsterView) {
        val nav = activity as Navigator
        nav.navigateTo(MonsterDetailPagerFragment.newInstance(monster))
    }
}
