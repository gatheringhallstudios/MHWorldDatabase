package com.gatheringhallstudios.mhworlddatabase.common;

import androidx.lifecycle.MutableLiveData;

/**
 * A variation of mutable live data that extends LiveData
 * to not fire an additional event if the value is the same.
 *
 * NOTE: Keep as Java. Overriding getters/setters in kotlin changes the signature.
 * This class needs to be used as a stand-in
 * @param <T>
 */
public class StableMutableLiveData<T> extends MutableLiveData<T> {
    @Override
    public void setValue(T value) {
        T oldValue = getValue();

        // the first check should also handle null
        if (value != oldValue || !value.equals(oldValue)) {
            super.setValue(value);
        }
    }
}
