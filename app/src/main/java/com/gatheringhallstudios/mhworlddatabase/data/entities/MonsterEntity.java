package com.gatheringhallstudios.mhworlddatabase.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryElemental;
import com.gatheringhallstudios.mhworlddatabase.data.embeds.WeaknessSummaryStatus;
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize;

/**
 * Entity for monster
 * Created by Carlos on 3/4/2018.
 */
@Entity(tableName = "monster")
public class MonsterEntity {
    @PrimaryKey
    public int id;
    public MonsterSize size;

    @Embedded(prefix = "weakness_")
    public WeaknessSummaryElemental weaknesses;

    @Embedded(prefix = "weakness_")
    public WeaknessSummaryStatus status_weaknesses;

    public boolean has_alt_weakness;

    @Embedded(prefix = "alt_weakness_")
    public WeaknessSummaryElemental alt_weaknesses;
}
