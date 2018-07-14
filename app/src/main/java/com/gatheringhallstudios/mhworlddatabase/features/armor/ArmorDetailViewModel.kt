package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.ItemDao
import com.gatheringhallstudios.mhworlddatabase.data.views.Armor
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetBonusView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorView
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

class ArmorDetailViewModel(application: Application) : AndroidViewModel(application) {
    private var armorId : Int = -1
    private var armorSetBonusId: Int = -1
    private val dao = MHWDatabase.getDatabase(application).armorDao()
    private val itemDao = MHWDatabase.getDatabase(application).itemDao()
    private val skillDao

    lateinit var armor: LiveData<ArmorView>
    lateinit var armorSetSkill: LiveData<List<ArmorSetBonusView>>
    lateinit var armorComponents: LiveData<List<ItemView>>

    fun loadArmor(armorId: Int) {
        if(this.armorId == armorId) return

        armor = dao.loadArmor(AppSettings.dataLocale, armorId)
        armorSetSkill = Transformations.switchMap(armor, ::loadArmorSetBonus)
    }

    fun loadArmorSetBonus(armorView: ArmorView) : LiveData<List<ArmorSetBonusView>>? {
        if(armorView.data.armorset_bonus_id == null) return null

        this.armorSetBonusId = armorView.data.armorset_bonus_id
        return dao.loadArmorSetBonus(AppSettings.dataLocale, armorSetBonusId)
    }
}