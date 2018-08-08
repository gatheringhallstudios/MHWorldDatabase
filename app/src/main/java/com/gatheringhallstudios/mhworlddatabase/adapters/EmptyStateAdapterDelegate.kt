package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder

/**
 * A placeholder used to trigger a render of an embedded empty state.
 */
class EmptyState

/**
 * An adapter delegate used to render an "empty state".
 * Most of the time it is recommended to use the RecyclerViewFragment's showEmptyView()
 */
class EmptyStateAdapterDelegate : SimpleListDelegate<EmptyState>() {
    override fun bindView(viewHolder: SimpleViewHolder, data: EmptyState) {}

    override fun isForViewType(obj: Any) = obj is EmptyState

    override fun onCreateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.empty_padded, parent, false)
    }
}