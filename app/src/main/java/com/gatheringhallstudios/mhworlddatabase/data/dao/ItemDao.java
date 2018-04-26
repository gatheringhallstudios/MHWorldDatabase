package com.gatheringhallstudios.mhworlddatabase.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView;

import java.util.List;

@Dao
public abstract class ItemDao {
    @Query( "SELECT i.id, it.name, it.description, i.rarity, i.buy_price, i.sell_price, i.carry_limit " +
            "FROM item i " +
            "   JOIN item_text it" +
            "       ON it.id = i.id" +
            "       AND it.lang_id = :langId ")
    public abstract LiveData<List<ItemView>> getItems(String langId);

    @Query( "SELECT i.id, it.name, it.description, i.rarity, i.buy_price, i.sell_price, i.carry_limit " +
            "FROM item i " +
            "   JOIN item_text it" +
            "       ON it.id = i.id" +
            "       AND it.lang_id = :langId " +
            "WHERE i.id = :itemId ")
    public abstract LiveData<ItemView> getItem(String langId, int itemId);
}
