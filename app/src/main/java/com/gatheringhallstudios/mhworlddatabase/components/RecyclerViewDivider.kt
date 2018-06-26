package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.gatheringhallstudios.mhworlddatabase.R

/**
 * Defines a RecyclweView's DividerItemDecoration with default settings.
 * Accepts a recycler view with a linear layout manager as an argument,
 * and loads the default drawable.
 * Add to a recycler view using addItemDecoration().
 */
class RecyclerViewDivider(ctx : Context, orientation: Int) : DividerItemDecoration(ctx, orientation) {
    init {
        val drawable = ContextCompat.getDrawable(ctx, R.drawable.listitem_divider)
        drawable?.let {
            this.setDrawable(it)
        }
    }

    constructor(recyclerView: RecyclerView) :
            this(recyclerView.context, (recyclerView.layoutManager as LinearLayoutManager).orientation)
}