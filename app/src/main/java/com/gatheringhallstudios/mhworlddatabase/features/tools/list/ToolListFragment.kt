package com.gatheringhallstudios.mhworlddatabase.features.tools.list

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.ToolAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment

class ToolListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ToolListFragment.ViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = BasicListDelegationAdapter(ToolAdapterDelegate {
            getRouter().navigateToolDetail(it.id)
        })

        setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        viewModel.skills.observe(this, Observer {
            if (it != null) adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = getString(R.string.title_tools)
    }

    class ViewModel(application: Application) : AndroidViewModel(application) {
        private val dao = MHWDatabase.getDatabase(application).toolDao()
        val skills = dao.loadTools(AppSettings.dataLocale)
    }
}
