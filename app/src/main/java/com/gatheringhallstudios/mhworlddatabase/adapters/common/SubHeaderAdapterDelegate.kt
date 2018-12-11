package com.gatheringhallstudios.mhworlddatabase.adapters.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_sub_header.*
import kotlinx.android.synthetic.main.listitem_sub_header.view.*

/**
 * Adapter delegate to handle displaying SubHeader objects inside RecyclerViews.
 */
class SubHeaderAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is SubHeader
    }

    override fun onCreateViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_sub_header, parent, false)
        return SimpleViewHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val subHeader = items[position] as SubHeader

        val vh = holder as SimpleViewHolder
        vh.label_text.text = subHeader.text
        vh.itemView.setTag(R.id.view_is_header, true)
    }
}
