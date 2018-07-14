package com.gatheringhallstudios.mhworlddatabase.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.DecorationView

class DecorationAdapterDelegate(private val onSelected: (DecorationView) -> Unit)
    : SimpleListDelegate<DecorationView, View>() {

    val TAG = this.javaClass.simpleName

    override fun getDataClass() = DecorationView::class

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }



    override fun bindView(view: View, data: DecorationView) {
        //TODO: Add explicit fetching of decoration level and color when they are available in the database
        val level = data.name?.get(data.name.length - 1)?.toInt()

        val icon: Drawable?
        icon = when (level!! - '0'.toInt()) { // .toInt() returns ascii value so subtract '0' to get int value
            1 -> view.context.getVectorDrawable(R.drawable.ic_ui_decoration_1_base)
            2 -> view.context.getVectorDrawable(R.drawable.ic_ui_decoration_2_base)
            3 -> view.context.getVectorDrawable(R.drawable.ic_ui_decoration_3_base)
            else -> {
                Log.d(TAG, "Failed to get level from " + data.name)
                view.context.getVectorDrawable(R.drawable.ic_ui_decoration_1_base)
            }
        }

        with(view as IconLabelTextCell) {
            view.setLeftIconDrawable(icon)
            view.setLabelText(data.name)
            view.removeDecorator()
        }

        view.setOnClickListener { onSelected(data) }
    }
}
