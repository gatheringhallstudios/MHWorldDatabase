package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.UserArmorPiece
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.features.armor.list.ArmorSetListFragment
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.fragment_user_equipment_set_selector.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserEquipmentSetSelectorListFragment : Fragment() {
    companion object {
        const val ARG_ACTIVE_EQUIPMENT = "ACTIVE_EQUIPMENT"
        const val ARG_SET_ID = "ACTIVE_SET_ID" //The equipment set that is currently being handled when in builder mode
        const val ARG_ITEM_FILTER = "ACTIVE_ITEM_FILTER" //What class armor to limit the selector to
    }

    private val viewModel: UserEquipmentSetSelectorViewModel by lazy {
        ViewModelProviders.of(this).get(UserEquipmentSetSelectorViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_equipment_set_selector, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filter = arguments?.getSerializable(ARG_ITEM_FILTER) as? ArmorType
        val activeArmorPiece = arguments?.getSerializable(ARG_ACTIVE_EQUIPMENT) as? UserArmorPiece
        val activeEquipmentSetId = arguments?.getInt(ARG_SET_ID)

        if (filter != null) {
            viewModel.loadArmor(AppSettings.dataLocale, filter)
        }

        val adapter = UserEquipmentSetSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateArmorPieceForArmorSet(it, activeEquipmentSetId!!, activeArmorPiece?.armor?.entityId)
                }

                getRouter().goBack()
            }
        }

        //If this is going to be new piece of armor, do not populate the active armor piece
        if (activeArmorPiece != null) {
            populateActiveArmor(activeArmorPiece)
        }

        equipment_list.adapter = adapter

        viewModel.armor.observe(this, Observer {
            adapter.items = it
        })
    }

    private fun populateActiveArmor(userArmor: UserArmorPiece) {
        val armor = userArmor.armor

        active_equipment_slot.equipment_name.text = armor.armor.name
        active_equipment_slot.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
        active_equipment_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.armor.rarity))
        active_equipment_slot.rarity_string.visibility = View.VISIBLE
        active_equipment_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
        active_equipment_slot.defense_value.text = getString(
                R.string.armor_defense_value,
                armor.armor.defense_base,
                armor.armor.defense_max,
                armor.armor.defense_augment_max)

        for (userDecoration in userArmor.decorations) {
            when (userDecoration.slotNumber) {
                1 -> populateSlot1(active_equipment_slot, userDecoration.decoration)
                2 -> populateSlot2(active_equipment_slot, userDecoration.decoration)
                3 -> populateSlot3(active_equipment_slot, userDecoration.decoration)
            }
        }
    }

    private fun populateSlot1(view: View, decoration: Decoration) {
        view.slot1.setImageDrawable(AssetLoader.loadIconFor(decoration))
        view.slot1_detail.setLabelText(decoration.name)
        view.slot1_detail.setLeftIconDrawable(AssetLoader.loadIconFor(decoration))
    }

    private fun populateSlot2(view: View, decoration: Decoration) {
        view.slot2.setImageDrawable(AssetLoader.loadIconFor(decoration))
        view.slot2_detail.setLabelText(decoration.name)
        view.slot2_detail.setLeftIconDrawable(AssetLoader.loadIconFor(decoration))
    }

    private fun populateSlot3(view: View, decoration: Decoration) {
        view.slot3.setImageDrawable(AssetLoader.loadIconFor(decoration))
        view.slot3_detail.setLabelText(decoration.name)
        view.slot3_detail.setLeftIconDrawable(AssetLoader.loadIconFor(decoration))
    }
}