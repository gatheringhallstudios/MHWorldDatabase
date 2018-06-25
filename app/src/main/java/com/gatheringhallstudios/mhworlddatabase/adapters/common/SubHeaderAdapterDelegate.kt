package com.gatheringhallstudios.mhworlddatabase.adapters.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
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

        return HeaderViewHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val subHeader = items[position] as SubHeader

        val vh = holder as HeaderViewHolder
        vh.setTitle(subHeader.text)

        // todo: allow collapsible header. Collapsible header via onSelected is not the proper way
    }

    internal inner class HeaderViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun setTitle(title : String?) {
            view.label_text.text = title
            //vh.labelText.setTypeface(vh.labelText.getTypeface(), Typeface.BOLD);
        }
    }
}
