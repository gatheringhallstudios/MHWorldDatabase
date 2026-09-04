package com.gatheringhallstudios.mhworlddatabase.features.monsters.list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments

/**
 * Fragment for a list of monsters
 */
class MonsterListFragment : RecyclerViewFragment() {
    companion object {
        private const val ARG_TAB = "MONSTER_TAB"

        @JvmStatic
        fun newInstance(tab: MonsterSize) = MonsterListFragment().applyArguments {
            putSerializable(ARG_TAB, tab)
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MonsterListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = MonsterListAdapter()
        this.setAdapter(adapter)
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        val tab = arguments?.getSerializable(ARG_TAB) as MonsterSize?
        viewModel.setTab(tab)

        viewModel.monsters.observe(viewLifecycleOwner, Observer {
            if (it != null) adapter.items = it
        })
    }
}
