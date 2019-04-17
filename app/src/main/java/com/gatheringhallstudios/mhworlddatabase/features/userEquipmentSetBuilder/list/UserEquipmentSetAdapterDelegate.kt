package com.gatheringhallstudios.mhworlddatabase.features.userEquipmentSetBuilder.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSetIds
import kotlinx.android.synthetic.main.listitem_skill_level_armor.view.*
import kotlinx.android.synthetic.main.listitem_user_equipment_set.*

class UserEquipmentSetAdapterDelegate(private val onSelect: (UserEquipmentSet) -> Unit) : SimpleListDelegate<UserEquipmentSet>() {
    private var parent: ViewGroup? = null
    override fun isForViewType(obj: Any) = obj is UserEquipmentSet

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        this.parent = parent
        return inflater.inflate(R.layout.listitem_user_equipment_set, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: UserEquipmentSet) {

        viewHolder.equipment_set_name.text = data.name
        viewHolder.fire_value.text = data.fireDefense.toString()
        viewHolder.water_value.text = data.waterDefense.toString()
        viewHolder.ice_value.text = data.iceDefense.toString()
        viewHolder.thunder_value.text = data.thunderDefense.toString()
        viewHolder.dragon_value.text = data.dragonDefense.toString()
        viewHolder.fire_value.text = data.fireDefense.toString()
        viewHolder.defense_value.text = viewHolder.resources.getString(
                R.string.armor_defense_value,
                data.defense_base,
                data.defense_max,
                data.defense_augment_max)
        for (skillTree in data.skills) {
            val layout = LayoutInflater.from(viewHolder.context).inflate(R.layout.listitem_skill_level, parent, false)
            layout.icon.setImageDrawable(AssetLoader.loadIconFor(skillTree.value.skillTree))
            layout.label_text.text = skillTree.value.skillTree.name
            layout.skill_level.maxLevel = skillTree.value.skillTree.max_level
            layout.skill_level.level = skillTree.value.level
            viewHolder.skill_layout_1.addView(layout)
        }

        viewHolder.itemView.setOnClickListener { onSelect(data) }
    }
}
