package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorComponentView

import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetBonusView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSkillView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorView

class ArmorDetailViewModel(application: Application) : AndroidViewModel(application) {
    private var armorId : Int = -1
    private var armorSetBonusId: Int = -1
    private val dao = MHWDatabase.getDatabase(application).armorDao()

    lateinit var armor: LiveData<ArmorView>
    lateinit var armorSetSkill: LiveData<List<ArmorSetBonusView>>
    lateinit var armorComponents: LiveData<List<ArmorComponentView>>
    lateinit var armorSkill: LiveData<List<ArmorSkillView>>

    fun loadArmor(armorId: Int) {
        if(this.armorId == armorId) return

        armor = dao.loadArmor(AppSettings.dataLocale, armorId)
        armorSetSkill = Transformations.switchMap(armor, ::loadArmorSetBonus)
        armorComponents = dao.loadArmorComponentViews(AppSettings.dataLocale, armorId)
        armorSkill = dao.loadArmorSkills(AppSettings.dataLocale, armorId)
    }

    fun loadArmorSetBonus(armorView: ArmorView) : LiveData<List<ArmorSetBonusView>>? {
        if(armorView.data.armorset_bonus_id == null) return null

        this.armorSetBonusId = armorView.data.armorset_bonus_id
        return dao.loadArmorSetBonus(AppSettings.dataLocale, armorSetBonusId)
    }
}