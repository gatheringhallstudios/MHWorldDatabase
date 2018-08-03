package com.gatheringhallstudios.mhworlddatabase.adapters.common

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.gatheringhallstudios.mhworlddatabase.R
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.listitem_section_header.*

/**
 * Adapter delegate to handle displaying SectionHeader objects inside RecyclerViews.
 */
class SectionHeaderAdapterDelegate: AdapterDelegate<List<Any>>() {
    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is SectionHeader
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_section_header, parent, false)
        return SimpleViewHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val sectionHeader = items[position] as SectionHeader

        val vh = holder as SimpleViewHolder
        vh.label_text.text = sectionHeader.text
        vh.itemView.setTag(R.id.view_type, sectionHeader.javaClass)

        val marginParams = vh.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if (position == 0) {
            marginParams.topMargin = 0
        } else {
            marginParams.topMargin = vh.resources.getDimensionPixelSize(R.dimen.margin_section_gap)
        }
    }
}
