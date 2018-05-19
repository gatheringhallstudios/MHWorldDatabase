package com.gatheringhallstudios.mhworlddatabase.common.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlin.reflect.KClass

abstract class SimpleListDelegate<T : Any>(val cls : KClass<T>): AdapterDelegate<List<Any>>() {
    abstract fun getLayoutId() : Int
    abstract fun bindItem(v: View, item : T)

    // subclasses don't require the viewholder. KTX is our viewholder
    // however the superclasses still use viewholders, so make them anyways
    class UselessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position]::class == cls
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(getLayoutId(), parent, false)
        return UselessViewHolder(v)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val item = items[position] as T
        bindItem(holder.itemView, item)
    }
}