package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet

class UserEquipmentSetDetailViewModel(application: Application) : AndroidViewModel(application) {
    var activeUserEquipmentSet = MutableLiveData<UserEquipmentSet>()

}