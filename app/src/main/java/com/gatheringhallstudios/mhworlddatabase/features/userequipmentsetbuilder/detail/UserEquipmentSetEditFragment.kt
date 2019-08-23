package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list.UserEquipmentSetListViewModel
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.fragment_user_equipment_set_editor.*

class UserEquipmentSetEditFragment : androidx.fragment.app.Fragment() {
    private var isNewFragment = true

    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: UserEquipmentSetDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(UserEquipmentSetDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_equipment_set_editor, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.activeUserEquipmentSet.observe(this, Observer<UserEquipmentSet> {
            attachDefaultOnClickListeners(it.id)
            populateUserEquipment(it)
        })
    }

    override fun onResume() {
        super.onResume()
        //Try to avoid stale check on first round
        if (!isNewFragment && viewModel.isActiveUserEquipmentSetStale()) {
            val buffer = ViewModelProviders.of(activity!!).get(UserEquipmentSetListViewModel::class.java)
            viewModel.activeUserEquipmentSet.value = buffer.getEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
        }

        isNewFragment = false
    }

    private fun attachDefaultOnClickListeners(userEquipmentSetId: Int) {
        user_equipment_head_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(null, userEquipmentSetId, ArmorType.HEAD)
        }

        user_equipment_chest_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(null, userEquipmentSetId, ArmorType.CHEST)
        }

        user_equipment_arms_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(null, userEquipmentSetId, ArmorType.ARMS)
        }

        user_equipment_waist_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(null, userEquipmentSetId, ArmorType.WAIST)
        }

        user_equipment_legs_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(null, userEquipmentSetId, ArmorType.LEGS)
        }
    }

    private fun attachOnClickListeners(armorPiece: UserArmorPiece, userEquipmentSetId: Int) {
//        user_equipment_weapon_slot.card_arrow.setOnClick {
//            toggle(user_equipment_weapon_slot)
//        }
        //        user_equipment_charm_slot.setOnClick {
//            getRouter().navigateUserEquipmentCharmSelector(userEquipmentSet.id, 0)
//        }
//        user_equipment_charm_slot.card_arrow.setOnClick {
//            toggle(user_equipment_charm_slot)
//        }
        when (armorPiece.armor.armor.armor_type) {
            ArmorType.HEAD -> {
                user_equipment_head_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(armorPiece, userEquipmentSetId, ArmorType.HEAD)
                }
            }
            ArmorType.CHEST -> {
                user_equipment_chest_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(armorPiece, userEquipmentSetId, ArmorType.CHEST)
                }
            }
            ArmorType.ARMS -> {
                user_equipment_arms_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(armorPiece, userEquipmentSetId, ArmorType.ARMS)
                }
            }
            ArmorType.WAIST -> {
                user_equipment_waist_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(armorPiece, userEquipmentSetId, ArmorType.WAIST)
                }
            }
            ArmorType.LEGS -> {
                user_equipment_legs_slot.setOnClick {
                    viewModel.setActiveUserEquipment(armorPiece)
                    getRouter().navigateUserEquipmentPieceSelector(armorPiece, userEquipmentSetId, ArmorType.LEGS)
                }
            }
        }
    }

    private fun populateUserEquipment(userEquipmentSet: UserEquipmentSet) {
        userEquipmentSet.equipment.forEach {
            when (it.type()) {
                DataType.WEAPON -> {
                    populateWeapon(it as UserWeapon)
                }
                DataType.ARMOR -> {
                    populateArmor(it as UserArmorPiece, userEquipmentSet.id)
                    attachOnClickListeners(it, userEquipmentSet.id)
                }
                DataType.CHARM -> {
                    populateCharm(it as UserCharm, userEquipmentSet.id)
                }
                else -> {
                } //Skip
            }
        }
    }

    private fun populateArmor(userArmor: UserArmorPiece, userEquipmentSetId: Int) {
        val armor = userArmor.armor
        val layout: View
        when (armor.armor.armor_type) {
            ArmorType.HEAD -> {
                layout = user_equipment_head_slot
            }
            ArmorType.CHEST -> {
                layout = user_equipment_chest_slot
            }
            ArmorType.ARMS -> {
                layout = user_equipment_arms_slot
            }
            ArmorType.WAIST -> {
                layout = user_equipment_waist_slot
            }
            ArmorType.LEGS -> {
                layout = user_equipment_legs_slot
            }
        }

        layout.equipment_name.text = armor.armor.name
        layout.rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
        layout.rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.armor.rarity))
        layout.rarity_string.visibility = View.VISIBLE
        layout.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
        layout.defense_value.text = getString(
                R.string.armor_defense_value,
                armor.armor.defense_base,
                armor.armor.defense_max,
                armor.armor.defense_augment_max)
        for (userDecoration in userArmor.decorations) {
            when (userDecoration.slotNumber) {
                1 -> populateSlot1(layout, userDecoration.decoration)
                2 -> populateSlot2(layout, userDecoration.decoration)
                3 -> populateSlot3(layout, userDecoration.decoration)
            }
        }
    }

    private fun populateCharm(userCharm: UserCharm, userEquipmentSetId: Int) {
        user_equipment_charm_slot.equipment_name.text = userCharm.charm.charm.name
        user_equipment_charm_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(userCharm.charm.charm))
        user_equipment_charm_slot.rarity_string.text = getString(R.string.format_rarity, userCharm.charm.charm.rarity)
        user_equipment_charm_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(userCharm.charm.charm.rarity))
        user_equipment_charm_slot.rarity_string.visibility = View.VISIBLE
        user_equipment_charm_slot.setOnClick {
            viewModel.setActiveUserEquipment(userCharm)
            getRouter().navigateUserEquipmentCharmSelector(userEquipmentSetId, userCharm.charm.entityId)
        }
        user_equipment_charm_slot.hideSlots()
        hideDefense(user_equipment_charm_slot)
        user_equipment_charm_slot.card_arrow.visibility = View.INVISIBLE
    }

    private fun populateWeapon(userWeapon: UserWeapon) {
        val weapon = userWeapon.weapon.weapon
        user_equipment_weapon_slot.equipment_name.text = weapon.name
        user_equipment_weapon_slot.equipment_icon.setImageDrawable(AssetLoader.loadIconFor(weapon))
        user_equipment_weapon_slot.rarity_string.setTextColor(AssetLoader.loadRarityColor(weapon.rarity))
        user_equipment_weapon_slot.rarity_string.text = getString(R.string.format_rarity, weapon.rarity)
        user_equipment_weapon_slot.rarity_string.visibility = View.VISIBLE
        for (userDecoration in userWeapon.decorations) {
            when (userDecoration.slotNumber) {
                1 -> populateSlot1(user_equipment_weapon_slot, userDecoration.decoration)
                2 -> populateSlot2(user_equipment_weapon_slot, userDecoration.decoration)
                3 -> populateSlot3(user_equipment_weapon_slot, userDecoration.decoration)
            }
        }

        if (weapon.defense != 0) {
            val defenseValue = getString(R.string.format_plus, weapon.defense)
            user_equipment_weapon_slot.defense_value.text = defenseValue
        } else {
            user_equipment_weapon_slot.icon_defense.visibility = View.INVISIBLE
            user_equipment_weapon_slot.defense_value.visibility = View.INVISIBLE
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

    private fun hideDefense(view: View) {
        view.icon_defense.visibility = View.INVISIBLE
        view.defense_value.visibility = View.INVISIBLE
    }
}