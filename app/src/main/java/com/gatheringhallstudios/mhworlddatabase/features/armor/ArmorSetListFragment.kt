package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.xwray.groupie.ExpandableItem


class HeaderItemDivider(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

    fun childHasBorder(parent: RecyclerView, position: Int): Boolean {
        val adapter = parent.adapter as GroupAdapter

        val isAtEnd = position == (adapter.getItemCount() - 1)
        val nextItem = when (isAtEnd) {
                true -> null
                false -> adapter.getItem(position + 1)
        }

        return nextItem is ExpandableItem
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        canvas.save()

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val trueIdx = parent.getChildAdapterPosition(child)

            if (!childHasBorder(parent, trueIdx)) {
                continue
            }

            val mBounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
        canvas.restore()
    }
}

/**
 * Created by Carlos on 3/22/2018.
 */

class ArmorSetListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ArmorSetListViewModel::class.java)
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        // TODO Add switching for High and Low rank Armor Set List Fragments
        viewModel.getArmorSetList(Rank.HIGH).observe(this, Observer<List<ArmorSetView>> {
            val items = it?.map {
                val headerItem = ArmorSetHeaderItem(it)
                val bodyItems = it.armor.map { ArmorSetDetailItem(it) }

                return@map ExpandableGroup(headerItem, false).apply {
                    addAll(bodyItems)
                }
            }

            adapter.update(items ?: emptyList())
        })

        // Add dividers between items
        val dividerDrawable = ContextCompat.getDrawable(context!!, R.drawable.listitem_divider)
        val itemDecor = HeaderItemDivider(dividerDrawable!!)
        recyclerView.addItemDecoration(itemDecor)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.armor_title)
    }
}
