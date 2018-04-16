package com.gatheringhallstudios.mhworlddatabase.common.adapters;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.List;

/**
 * This is a general {@link android.support.v7.widget.RecyclerView} adapter that you
 * populate with more specific {@link AdapterDelegate}s. Accepts multiple AdapterDelegates
 * and creates an Adapter capable of displaying the appropriate any object of type T into a
 * RecyclerView as long as the appropriate AdapterDelegate is given.
 */
public class BasicListDelegationAdapter<T> extends ListDelegationAdapter<List<T>> {
    public BasicListDelegationAdapter(AdapterDelegate<List<T>>... delegates) {
        for (AdapterDelegate<List<T>> delegate : delegates) {
            delegatesManager.addDelegate(delegate);
        }
    }
}
