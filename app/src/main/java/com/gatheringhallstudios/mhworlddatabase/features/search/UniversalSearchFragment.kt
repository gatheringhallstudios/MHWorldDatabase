package com.gatheringhallstudios.mhworlddatabase.features.search

import androidx.lifecycle.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.MainActivity
import com.gatheringhallstudios.mhworlddatabase.MainActivityViewModel
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider

private const val SEARCH_FILTER = "SEARCH_FILTER"

/**
 * The main fragment used to display search results
 */
class UniversalSearchFragment : RecyclerViewFragment() {

    private val activityViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
    }

    private val searchViewModel by lazy {
        ViewModelProviders.of(this).get(UniversalSearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = SearchResultAdapter()
        setAdapter(adapter)

        // If we need to restore state, start a query in the search view model.
        // This will also update the current search filter, restoring the search title.
        val savedFilter = savedInstanceState?.getString(SEARCH_FILTER)
        if (!savedFilter.isNullOrBlank() && searchViewModel.searchFilter == "") {
            searchViewModel.searchData(savedFilter)
        }

        // add decorator
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        // open up the search menu (if not open) if we're on this page
        // If the user hit back and returned to this page, we need to open it again
        activityViewModel.searchActive.value = true
        (activity as MainActivity).updateSearchView(searchViewModel.searchFilter)

        // If the activity filter changes, update the fragment viewmodel
        activityViewModel.filter.observe(this, Observer {
            searchViewModel.searchData(it)
        })

        // If the search results have a value or changed, show them.
        searchViewModel.searchResults.observe(this, Observer {
            if (it != null) adapter.bindSearchResults(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty, menu)
    }

    override fun onDetach() {
        super.onDetach()

        activityViewModel.searchActive.value = false
    }

    override fun onDestroyView() {
        super.onDestroyView()

        activityViewModel.searchActive.value = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // save search filter to allow it to be restored
        outState.putString(SEARCH_FILTER, searchViewModel.searchFilter)
    }
}