package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.equipment_set_icon
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.equipment_set_name
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.fire_value
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.water_value
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.ice_value
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.thunder_value
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.dragon_value
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.defense_value
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.skill_pager
import kotlinx.android.synthetic.main.listitem_user_equipment_set.view.worm_dots_indicator

class UserEquipmentSetAdapterDelegate(private val dataSet: MutableList<UserEquipmentSet>, private val onSelect: (UserEquipmentSet) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType != 0) {
            val v = inflater.inflate(R.layout.listitem_user_equipment_set, parent, false)
            return EquipmentSetHolder(v, parent.context)
        } else {
            val v = inflater.inflate(R.layout.listitem_user_equipment_set_new, parent, false)
            return NewEquipmentSetHolder(v)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet[position].id == 0) return 0 else return 1
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val userEquipmentSet = dataSet[position]

        if (viewHolder is EquipmentSetHolder) {
            viewHolder.bind(userEquipmentSet)
            viewHolder.view.setOnClickListener { onSelect(userEquipmentSet) }
        } else {
            val vh = viewHolder as NewEquipmentSetHolder
            vh.view.setOnClickListener { onSelect(userEquipmentSet) }
        }
    }

    private fun getIconObject(equipment: MutableList<UserEquipment>): Drawable? {
        val item = equipment.first()
        return when (item.type()) {
            DataType.WEAPON -> AssetLoader.loadIconFor((item as UserWeapon).weapon.weapon)
            DataType.ARMOR -> AssetLoader.loadIconFor((item as UserArmorPiece).armor.armor)
            DataType.CHARM -> AssetLoader.loadIconFor((item as UserCharm).charm.charm)
            else -> null
        }
    }

    internal inner class EquipmentSetHolder(val view: View, val context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceType")
        fun bind(data: UserEquipmentSet) {
            if (data.equipment.isNotEmpty()) {
                view.equipment_set_icon.setImageDrawable(getIconObject(data.equipment))
            }

            view.equipment_set_name.text = data.name
            view.fire_value.text = data.fireDefense.toString()
            view.water_value.text = data.waterDefense.toString()
            view.ice_value.text = data.iceDefense.toString()
            view.thunder_value.text = data.thunderDefense.toString()
            view.dragon_value.text = data.dragonDefense.toString()
            view.fire_value.text = data.fireDefense.toString()
            view.defense_value.text = view.resources.getString(
                    R.string.armor_defense_value,
                    data.defense_base,
                    data.defense_max,
                    data.defense_augment_max)

            view.skill_pager.adapter = UserEquipmentSetViewPagerAdapter(context, data.skills.map { it.value })
            view.worm_dots_indicator.setViewPager(view.skill_pager)
        }
    }

    internal inner class NewEquipmentSetHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}

