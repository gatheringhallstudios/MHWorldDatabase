package com.gatheringhallstudios.mhworlddatabase.common;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.List;

public class BasicListDelegationAdapter<T> extends ListDelegationAdapter<List<T>> {
    public BasicListDelegationAdapter(AdapterDelegate<List<T>> delegate) {
        delegatesManager.addDelegate(delegate);
    }
}
