package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.EmptyState
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder

class EmptyStateAdapterDelegate : SimpleListDelegate<EmptyState>() {
    override fun bindView(viewHolder: SimpleViewHolder, data: EmptyState) {

    }

    override fun isForViewType(obj: Any) = obj is EmptyState

    override fun onCreateView(parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty, parent, false)
        return view
    }
}
