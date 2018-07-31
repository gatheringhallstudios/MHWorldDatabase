package com.gatheringhallstudios.mhworlddatabase.features.decorations.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.DecorationAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.getRouter

class DecorationListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    val adapter = BasicListDelegationAdapter(DecorationAdapterDelegate {
        getRouter().navigateDecorationDetail(it.id)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.decorations.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = getString(R.string.skills_title)
    }

    class ViewModel(application: Application) : AndroidViewModel(application) {
        private val dao = MHWDatabase.getDatabase(application).decorationDao()
        val decorations = dao.loadDecorations(AppSettings.dataLocale)
    }
}