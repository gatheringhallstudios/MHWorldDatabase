package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.Tool
import com.gatheringhallstudios.mhworlddatabase.data.models.ToolBase

@Dao
abstract class ToolDao {
    @Query("""
        SELECT t.id, order_id, icon_color, name, name_base, description, tool_type
        FROM tool t
            JOIN tool_text tt
                ON tt.id = t.id
        WHERE lang_id = :langId          
        ORDER BY t.order_id ASC
    """)
    abstract fun loadTools(langId: String): LiveData<List<ToolBase>>

    @Query("""
        SELECT t.id, order_id, icon_color, name, name_base, description, tool_type, duration, 
        duration_upgraded, recharge, slot_1, slot_2, slot_3
        FROM tool t
            JOIN tool_text tt
                ON tt.id = t.id
        WHERE lang_id = :langId          
        ORDER BY t.order_id ASC
    """)
    abstract fun loadToolsSync(langId: String): List<Tool>

    @Query("""
        SELECT t.id, order_id, icon_color, name, name_base, description, tool_type, duration, 
        duration_upgraded, recharge, slot_1, slot_2, slot_3
        FROM tool t
            JOIN tool_text tt
                ON tt.id = t.id
        WHERE tt.id = :id
            AND lang_id = :langId          
        ORDER BY t.order_id ASC
    """)
    abstract fun loadTool(id: Int, langId: String): LiveData<Tool>

    @Query("""
        SELECT t.id, order_id, icon_color, name, name_base, description, tool_type, duration, 
        duration_upgraded, recharge, slot_1, slot_2, slot_3
        FROM tool t
            JOIN tool_text tt
                ON tt.id = t.id
        WHERE tt.id = :id
            AND lang_id = :langId          
        ORDER BY t.order_id ASC
    """)
    abstract fun loadToolSync(langId: String, id: Int): Tool
}

