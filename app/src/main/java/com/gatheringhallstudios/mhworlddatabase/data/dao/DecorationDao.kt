package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.DecorationBase

@Dao
abstract class DecorationDao {
    @Query("""
        SELECT d.id, dt.name, d.slot, d.icon_color
        FROM decoration d
            JOIN decoration_text dt
                ON dt.id = d.id
                AND dt.lang_id = :langId
        ORDER BY dt.name""")
    abstract fun loadDecorations(langId: String): LiveData<List<DecorationBase>>

    @Query("""
        SELECT d.*, dtext.name,
            s.id skill_skill_id, stext.name skill_skill_name, s.max_level skill_skill_max_level, s.icon_color skill_skill_icon_color, d.skilltree_level skill_level,
            s2.id skill2_skill_id, stext2.name skill2_skill_name, s2.max_level skill2_skill_max_level, s2.icon_color skill2_skill_icon_color, d.skilltree2_level skill2_level
        FROM decoration d
            JOIN decoration_text dtext
                ON dtext.id = d.id
            LEFT JOIN skilltree s
                ON s.id = d.skilltree_id
            LEFT JOIN skilltree_text stext
                ON stext.id = s.id
                AND stext.lang_id = dtext.lang_id
            LEFT JOIN skilltree s2
                ON s2.id = d.skilltree2_id
            LEFT JOIN skilltree_text stext2
                ON stext2.id = s2.id
                AND stext2.lang_id = dtext.lang_id
        WHERE dtext.lang_id = :langId
          """)
    abstract fun loadDecorationsWithSkills(langId: String): LiveData<List<Decoration>>

    @Query("""
        SELECT d.*, dtext.name,
            s.id skill_skill_id, stext.name skill_skill_name, s.max_level skill_skill_max_level, s.icon_color skill_skill_icon_color, d.skilltree_level skill_level,
            s2.id skill2_skill_id, stext2.name skill2_skill_name, s2.max_level skill2_skill_max_level, s2.icon_color skill2_skill_icon_color, d.skilltree2_level skill2_level
        FROM decoration d
            JOIN decoration_text dtext
                ON dtext.id = d.id
            LEFT JOIN skilltree s
                ON s.id = d.skilltree_id
            LEFT JOIN skilltree_text stext
                ON stext.id = s.id
                AND stext.lang_id = dtext.lang_id
            LEFT JOIN skilltree s2
                ON s2.id = d.skilltree2_id
            LEFT JOIN skilltree_text stext2
                ON stext2.id = s2.id
                AND stext2.lang_id = dtext.lang_id    
        WHERE dtext.lang_id = :langId
            """)
    abstract fun loadDecorationsWithSkillsSync(langId: String): List<Decoration>

    @Query("""
        SELECT d.*, dtext.name,
            s.id skill_skill_id, stext.name skill_skill_name, s.max_level skill_skill_max_level, s.icon_color skill_skill_icon_color, d.skilltree_level skill_level,
            s2.id skill2_skill_id, stext2.name skill2_skill_name, s2.max_level skill2_skill_max_level, s2.icon_color skill2_skill_icon_color, d.skilltree2_level skill2_level
        FROM decoration d
            JOIN decoration_text dtext
                ON dtext.id = d.id
            LEFT JOIN skilltree s
                ON s.id = d.skilltree_id
            LEFT JOIN skilltree_text stext
                ON stext.id = s.id
                AND stext.lang_id = dtext.lang_id
            LEFT JOIN skilltree s2
                ON s2.id = d.skilltree2_id
            LEFT JOIN skilltree_text stext2
                ON stext2.id = s2.id
                AND stext2.lang_id = dtext.lang_id    
                
        WHERE d.id = :decorationId
          AND dtext.lang_id = :langId""")
    abstract fun loadDecoration(langId: String, decorationId: Int): LiveData<Decoration>

    @Query("""
        SELECT d.*, dtext.name,
            s.id skill_skill_id, stext.name skill_skill_name, s.max_level skill_skill_max_level, s.icon_color skill_skill_icon_color, d.skilltree_level skill_level,
            s2.id skill2_skill_id, stext2.name skill2_skill_name, s2.max_level skill2_skill_max_level, s2.icon_color skill2_skill_icon_color, d.skilltree2_level skill2_level            
        FROM decoration d
            JOIN decoration_text dtext
                ON dtext.id = d.id
            LEFT JOIN skilltree s
                ON s.id = d.skilltree_id
            LEFT JOIN skilltree_text stext
                ON stext.id = s.id
                AND stext.lang_id = dtext.lang_id
            LEFT JOIN skilltree s2
                ON s2.id = d.skilltree2_id
            LEFT JOIN skilltree_text stext2
                ON stext2.id = s2.id
                AND stext2.lang_id = dtext.lang_id                   
        WHERE d.id = :decorationId
          AND dtext.lang_id = :langId""")
    abstract fun loadDecorationSync(langId: String, decorationId: Int): Decoration
}
