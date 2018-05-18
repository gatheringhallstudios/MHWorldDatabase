package com.gatheringhallstudios.mhworlddatabase.common.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.models.SubHeader
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

/**
 * Adapter delegate to handle displaying SubHeader objects inside RecyclerViews.
 */
class SubHeaderAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is SubHeader
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.listitem_sub_header, parent, false)

        return HeaderViewHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: List<Any>) {
        val subHeader = items[position] as SubHeader

        val vh = holder as HeaderViewHolder

        vh.labelText.text = subHeader.text
        //vh.labelText.setTypeface(vh.labelText.getTypeface(), Typeface.BOLD);
    }

    internal inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelText: TextView

        init {
            labelText = itemView.findViewById(R.id.label_text)
        }
    }
}
