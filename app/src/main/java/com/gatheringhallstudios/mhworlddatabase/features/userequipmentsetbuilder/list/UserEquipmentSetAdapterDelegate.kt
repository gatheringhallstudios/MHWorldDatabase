package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
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
        //Load the icon of the first piece of equipment or no icon if the set is empty
        if (data.equipment.isNotEmpty()) {
            viewHolder.equipment_set_icon.setImageDrawable(getIconObject(data.equipment))
        }

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

        viewHolder.skill_pager.adapter = UserEquipmentSetViewPagerAdapter(this.parent!!.context, data.skills.map { it.value })
        viewHolder.worm_dots_indicator.setViewPager(viewHolder.skill_pager)
        viewHolder.itemView.setOnClickListener { onSelect(data) }
    }

    private fun getIconObject(equipment: MutableList<UserEquipment>) : Drawable? {
        val item = equipment.first()
        return when (item.getType()) {
            DataType.WEAPON -> AssetLoader.loadIconFor((item as UserWeapon).weapon.weapon)
            DataType.ARMOR -> AssetLoader.loadIconFor((item as UserArmorPiece).armor.armor)
            DataType.CHARM -> AssetLoader.loadIconFor((item as UserCharm).charm.charm)
            else -> null
        }
    }
}

