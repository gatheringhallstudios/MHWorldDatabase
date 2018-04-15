package com.gatheringhallstudios.mhworlddatabase.common.adapters;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.List;

/**
 * This is a general {@link android.support.v7.widget.RecyclerView} adapter that you
 * populate with more specific {@link AdapterDelegate}s.
 */

public class BasicListDelegationAdapter<T> extends ListDelegationAdapter<List<T>> {
    public BasicListDelegationAdapter(AdapterDelegate<List<T>> delegate) {
        delegatesManager.addDelegate(delegate);
    }
}
