package com.gatheringhallstudios.mhworlddatabase.features.search

import android.arch.lifecycle.*
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.MainActivity
import com.gatheringhallstudios.mhworlddatabase.MainActivityViewModel
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.adapters.SearchResultAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment

class UniversalSearchFragment : RecyclerViewFragment() {

    // Universal Search results handle many types of data.
    // Create an adapter that handles all of them
    val adapter = BasicListDelegationAdapter<Any>(SearchResultAdapterDelegate())

    private val activityViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
    }

    private val searchViewModel by lazy {
        ViewModelProviders.of(this).get(UniversalSearchViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        // open up the search menu (if not open) if we're on this page
        // If the user hit back and returned to this page, we need to open it again
        activityViewModel.searchActive.value = true
        (activity as MainActivity).updateSearchView(searchViewModel.lastSearchFilter)

        activityViewModel.filter.observe(this, Observer {
            searchViewModel.searchData(it)
        })

        searchViewModel.searchResults.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDetach() {
        super.onDetach()

        activityViewModel.searchActive.value = false
    }

    override fun onDestroyView() {
        super.onDestroyView()

        activityViewModel.searchActive.value = false
    }
}