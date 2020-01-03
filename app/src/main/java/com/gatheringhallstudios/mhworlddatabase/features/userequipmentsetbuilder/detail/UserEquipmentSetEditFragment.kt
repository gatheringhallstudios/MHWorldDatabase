package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.UserEquipmentCard
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list.UserEquipmentSetListViewModel
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors.UserEquipmentSetSelectorListFragment.Companion
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlinx.android.synthetic.main.fragment_user_equipment_set_editor.*
import kotlinx.android.synthetic.main.view_base_body_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.icon_slots
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot1
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot2
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.slot3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserEquipmentSetEditFragment : androidx.fragment.app.Fragment(), RenameSetDialog.RenameDialogListener {
    fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = RenameSetDialog()
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager!!, "RenameDialogFragment")
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    override fun onDialogPositiveClick(dialog: RenameSetDialog) {
        // User touched the dialog's positive button
        viewModel.renameEquipmentSet(dialog.resultName, viewModel.activeUserEquipmentSet.value!!.id)
        val buffer = ViewModelProviders.of(activity!!).get(UserEquipmentSetListViewModel::class.java)
        viewModel.activeUserEquipmentSet.value = buffer.getEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
    }

    override fun onDialogNegativeClick(dialog: RenameSetDialog) {
        // User touched the dialog's negative button
    }

    private var isNewFragment = true

    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: UserEquipmentSetDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(UserEquipmentSetDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_equipment_set_editor, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.activeUserEquipmentSet.observe(this, Observer<UserEquipmentSet> {
            populateUserEquipment(it)
        })
    }

    override fun onResume() {
        super.onResume()
        if (!isNewFragment) {
            val buffer = ViewModelProviders.of(activity!!).get(UserEquipmentSetListViewModel::class.java)
            viewModel.activeUserEquipmentSet.value = buffer.getEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
        }

        isNewFragment = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_equipment_set_editor, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return when (id) {
            R.id.action_delete_set -> {
                viewModel.deleteEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
                getRouter().goBack()
                true
            }
            R.id.action_rename_set -> {
                showNoticeDialog()
                true
            }
            else -> false
        }
    }

    private fun populateUserEquipment(userEquipmentSet: UserEquipmentSet) {
        setActivityTitle(userEquipmentSet.name)
        populateDefaults(userEquipmentSet.id)
        userEquipmentSet.equipment.forEach {
            when (it.type()) {
                DataType.WEAPON -> {
                    populateWeapon(it as UserWeapon, userEquipmentSet.id)
                }
                DataType.ARMOR -> {
                    populateArmor(it as UserArmorPiece, userEquipmentSet.id)
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

        val card = UserEquipmentCard(layout)
        card.bindArmor(userArmor)

        //Combine the skills from the armor piece and the decorations
        val skillsList = combineEquipmentSkillsWithDecorationSkills(armor.skills, userArmor.decorations.map {
            val skillLevel = SkillLevel(level = 1)
            skillLevel.skillTree = it.decoration.skillTree
            skillLevel
        })

        card.populateSkills(skillsList)
        card.populateSetBonuses(armor.setBonuses)
        populateDecorations(userArmor, userEquipmentSetId, layout)
        attachArmorOnClickListeners(userArmor, userEquipmentSetId, layout)
    }

    private fun populateCharm(userCharm: UserCharm, userEquipmentSetId: Int) {
        val card = UserEquipmentCard(user_equipment_charm_slot)
        card.bindCharm(userCharm)

        card.populateSkills(emptyList())
        card.populateSetBonuses(emptyList())
        populateDecorations(null, userEquipmentSetId, user_equipment_charm_slot)

        user_equipment_charm_slot.setOnClick {
            viewModel.setActiveUserEquipment(userCharm)
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.CHARM, userCharm, userEquipmentSetId, null, null)
        }
        user_equipment_charm_slot.setOnSwipeRight {
            viewModel.activeUserEquipmentSet.value?.equipment?.remove(userCharm)
            viewModel.deleteUserEquipment(userCharm.entityId(), userEquipmentSetId, userCharm.type())
            val currentFragment = this
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.detach(currentFragment)
            fragmentTransaction.attach(currentFragment)
            fragmentTransaction.commit()
        }

        hideDefense(user_equipment_charm_slot)
        hideSlots(user_equipment_charm_slot)
        card.populateSkills(userCharm.charm.skills)
    }

    private fun populateWeapon(userWeapon: UserWeapon, userEquipmentSetId: Int) {
        val card = UserEquipmentCard(user_equipment_weapon_slot)
        card.bindWeapon(userWeapon)

        card.populateSkills(emptyList())
        card.populateSetBonuses(emptyList())
        populateDecorations(null, userEquipmentSetId, user_equipment_weapon_slot)

        val skillsList = combineEquipmentSkillsWithDecorationSkills(userWeapon.weapon.skills, userWeapon.decorations.map {
            val skillLevel = SkillLevel(level = 1)
            skillLevel.skillTree = it.decoration.skillTree
            skillLevel
        })

        card.populateSkills(skillsList)
        populateDecorations(userWeapon, userEquipmentSetId, user_equipment_weapon_slot)
        attachWeaponOnClickListeners(userWeapon, userEquipmentSetId, user_equipment_weapon_slot)
    }

    private fun populateSlot1(layout: View, decoration: Decoration?, slot: Int) {
        layout.card_body.slot1_detail.removeDecorator()

        if (decoration != null) {
            layout.card_header.slot1.setImageDrawable(AssetLoader.loadFilledSlotIcon(decoration, slot))
            layout.card_body.slot1_detail.visibility = View.VISIBLE
            layout.card_body.slot1_detail.setLabelText(decoration.name)
            layout.card_body.slot1_detail.setLeftIconDrawable(AssetLoader.loadFilledSlotIcon(decoration, slot))
        } else {
            layout.card_header.slot1.setImageDrawable(context!!.getDrawableCompat(SlotEmptyRegistry(slot)))
            layout.card_body.slot1_detail.setLeftIconDrawable(context!!.getDrawableCompat(SlotEmptyRegistry(slot)))
            layout.card_body.slot1_detail.setLabelText(getString(R.string.user_equipment_set_no_decoration))
            layout.card_body.slot1_detail.visibility = View.GONE
        }
    }

    private fun populateSlot2(layout: View, decoration: Decoration?, slot: Int) {
        layout.card_body.slot2_detail.removeDecorator()

        if (decoration != null) {
            layout.card_header.slot2.setImageDrawable(AssetLoader.loadFilledSlotIcon(decoration, slot))
            layout.card_body.slot2_detail.visibility = View.VISIBLE
            layout.card_body.slot2_detail.setLabelText(decoration.name)
            layout.card_body.slot2_detail.setLeftIconDrawable(AssetLoader.loadFilledSlotIcon(decoration, slot))
        } else {
            layout.card_header.slot2.setImageDrawable(context!!.getDrawableCompat(SlotEmptyRegistry(slot)))
            layout.card_body.slot2_detail.setLeftIconDrawable(context!!.getDrawableCompat(SlotEmptyRegistry(slot)))
            layout.card_body.slot2_detail.setLabelText(getString(R.string.user_equipment_set_no_decoration))
            layout.card_body.slot2_detail.visibility = View.GONE
        }
    }

    private fun populateSlot3(layout: View, decoration: Decoration?, slot: Int) {
        layout.card_body.slot3_detail.removeDecorator()

        if (decoration != null) {
            layout.card_header.slot3.setImageDrawable(AssetLoader.loadFilledSlotIcon(decoration, slot))
            layout.card_body.slot3_detail.visibility = View.VISIBLE
            layout.card_body.slot3_detail.setLabelText(decoration.name)
            layout.card_body.slot3_detail.setLeftIconDrawable(AssetLoader.loadFilledSlotIcon(decoration, slot))
        } else {
            layout.card_header.slot3.setImageDrawable(context!!.getDrawableCompat(SlotEmptyRegistry(slot)))
            layout.card_body.slot3_detail.setLeftIconDrawable(context!!.getDrawableCompat(SlotEmptyRegistry(slot)))
            layout.card_body.slot3_detail.setLabelText(getString(R.string.user_equipment_set_no_decoration))
            layout.card_body.slot3_detail.visibility = View.GONE
        }
    }

    private fun hideDefense(view: View) {
        view.icon_defense.visibility = View.INVISIBLE
        view.defense_value.visibility = View.INVISIBLE
    }

    private fun hideSlots(view: View) {
        view.icon_slots.visibility = View.GONE
        view.slot1.visibility = View.GONE
        view.slot2.visibility = View.GONE
        view.slot3.visibility = View.GONE
    }

    private fun populateDecorations(userEquipment: UserEquipment?, userEquipmentSetId: Int, layout: View) {
        layout.decorations_section.visibility = View.GONE
        layout.decorations_section.slot1_detail.visibility = View.GONE
        layout.decorations_section.slot2_detail.visibility = View.GONE
        layout.decorations_section.slot3_detail.visibility = View.GONE
        val slots = if ((userEquipment as? UserArmorPiece) != null) {
            userEquipment.armor.armor.slots
        } else if ((userEquipment as? UserWeapon) != null) {
            userEquipment.weapon.weapon.slots
        } else {
            return
        }

        //Populate defaults
        layout.decorations_section.visibility = if (slots.isEmpty()) View.GONE else View.VISIBLE
        slots.active.forEachIndexed { idx, slot ->
            when (idx + 1) {
                1 -> {
                    populateSlot1(layout, null, slots[0])
                    layout.slot1_detail.visibility = View.VISIBLE
                    layout.slot1_detail.setOnClickListener {
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, null,
                                userEquipmentSetId, null,
                                Companion.DecorationsConfig(userEquipment.entityId(), 1, userEquipment.type(), slot))
                    }
                }
                2 -> {
                    populateSlot2(layout, null, slots[1])
                    layout.slot2_detail.visibility = View.VISIBLE
                    layout.slot2_detail.setOnClickListener {
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, null,
                                userEquipmentSetId, null,
                                Companion.DecorationsConfig(userEquipment.entityId(), 2, userEquipment.type(), slot))
                    }
                }
                3 -> {
                    populateSlot3(layout, null, slots[2])
                    layout.slot3_detail.visibility = View.VISIBLE
                    layout.slot3_detail.setOnClickListener {
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, null,
                                userEquipmentSetId, null,
                                Companion.DecorationsConfig(userEquipment.entityId(), 3, userEquipment.type(), slot))
                    }
                }
            }
        }

        populateSavedDecorations(layout, userEquipment, userEquipmentSetId)
    }

    private fun populateSavedDecorations(layout: View, userEquipment: UserEquipment, userEquipmentSetId: Int) {
        val decorations = if ((userEquipment as? UserArmorPiece) != null) {
            userEquipment.decorations
        } else if ((userEquipment as? UserWeapon) != null) {
            userEquipment.decorations
        } else {
            emptyList()
        }

        //Maximum slot values, not the slot number on the equipment
        val slots = if ((userEquipment as? UserArmorPiece) != null) {
            userEquipment.armor.armor.slots
        } else if ((userEquipment as? UserWeapon) != null) {
            userEquipment.weapon.weapon.slots
        } else {
            return
        }

        for (userDecoration in decorations) {
            when (userDecoration.slotNumber) {
                1 -> {
                    populateSlot1(layout, userDecoration.decoration, slots[0])
                    layout.slot1_detail.setOnClickListener {
                        viewModel.setActiveUserEquipment(userDecoration)
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION,
                                userDecoration, userEquipmentSetId, null,
                                Companion.DecorationsConfig(userEquipment.entityId(), 1,
                                        userEquipment.type(), userDecoration.decoration.slot))
                    }
                    layout.decorations_section.slot1_detail.setButtonClickFunction {
                        GlobalScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                viewModel.deleteDecorationForEquipment(userDecoration.decoration.id, userEquipment.entityId(), userDecoration.slotNumber, userEquipment.type(), userEquipmentSetId)
                            }
                            withContext(Dispatchers.Main) {
                                val buffer = ViewModelProviders.of(activity!!).get(UserEquipmentSetListViewModel::class.java)
                                viewModel.activeUserEquipmentSet.value = buffer.getEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
                                (layout as ExpandableCardView).toggle()
                            }
                        }
                    }
                }
                2 -> {
                    populateSlot2(layout, userDecoration.decoration, slots[1])
                    layout.slot2_detail.setOnClickListener {
                        viewModel.setActiveUserEquipment(userDecoration)
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, userDecoration, userEquipmentSetId, null,
                                Companion.DecorationsConfig(userEquipment.entityId(), 2,
                                        userEquipment.type(), userDecoration.decoration.slot))
                    }
                    layout.decorations_section.slot2_detail.setButtonClickFunction {
                        GlobalScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                viewModel.deleteDecorationForEquipment(userDecoration.decoration.id, userEquipment.entityId(), userDecoration.slotNumber, userEquipment.type(), userEquipmentSetId)
                            }
                            withContext(Dispatchers.Main) {
                                val buffer = ViewModelProviders.of(activity!!).get(UserEquipmentSetListViewModel::class.java)
                                viewModel.activeUserEquipmentSet.value = buffer.getEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
                                (layout as ExpandableCardView).toggle()
                            }
                        }
                    }
                }
                3 -> {
                    populateSlot3(layout, userDecoration.decoration, slots[2])
                    layout.slot3_detail.setOnClickListener {
                        viewModel.setActiveUserEquipment(userDecoration)
                        getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, userDecoration, userEquipmentSetId, null,
                                Companion.DecorationsConfig(userEquipment.entityId(), 3,
                                        userEquipment.type(), userDecoration.decoration.slot))
                    }
                    layout.decorations_section.slot3_detail.setButtonClickFunction {
                        GlobalScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                viewModel.deleteDecorationForEquipment(userDecoration.decoration.id, userEquipment.entityId(), userDecoration.slotNumber, userEquipment.type(), userEquipmentSetId)
                            }
                            withContext(Dispatchers.Main) {
                                val buffer = ViewModelProviders.of(activity!!).get(UserEquipmentSetListViewModel::class.java)
                                viewModel.activeUserEquipmentSet.value = buffer.getEquipmentSet(viewModel.activeUserEquipmentSet.value!!.id)
                                (layout as ExpandableCardView).toggle()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun populateDefaults(userEquipmentSetId: Int) {
        user_equipment_weapon_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.WEAPON, null, userEquipmentSetId, null, null)
        }

        user_equipment_head_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.HEAD, null)
        }

        user_equipment_chest_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.CHEST, null)
        }

        user_equipment_arms_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.ARMS, null)
        }

        user_equipment_waist_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.WAIST, null)
        }

        user_equipment_legs_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, null, userEquipmentSetId, ArmorType.LEGS, null)
        }

        user_equipment_charm_slot.setOnClick {
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.CHARM, null, userEquipmentSetId, null, null)
        }
    }

    private fun attachArmorOnClickListeners(armorPiece: UserArmorPiece, userEquipmentSetId: Int, layout: ExpandableCardView) {
        val armor = armorPiece.armor.armor
        layout.setOnClick {
            viewModel.setActiveUserEquipment(armorPiece)
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, armorPiece, userEquipmentSetId, armor.armor_type, null)
        }
        layout.setOnSwipeRight {
            viewModel.activeUserEquipmentSet.value?.equipment?.remove(armorPiece)
            viewModel.deleteUserEquipment(armorPiece.entityId(), userEquipmentSetId, armorPiece.type())
            val currentFragment = this
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.detach(currentFragment)
            fragmentTransaction.attach(currentFragment)
            fragmentTransaction.commit()
        }
    }

    private fun attachWeaponOnClickListeners(userWeapon: UserWeapon, userEquipmentSetId: Int, layout: ExpandableCardView) {
        layout.setOnClick {
            viewModel.setActiveUserEquipment(userWeapon)
            getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.WEAPON, userWeapon, userEquipmentSetId, null, null)
        }
        layout.setOnSwipeRight {
            viewModel.activeUserEquipmentSet.value?.equipment?.remove(userWeapon)
            viewModel.deleteUserEquipment(userWeapon.entityId(), userEquipmentSetId, userWeapon.type())
            val currentFragment = this
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.detach(currentFragment)
            fragmentTransaction.attach(currentFragment)
            fragmentTransaction.commit()
        }
    }

    private fun combineEquipmentSkillsWithDecorationSkills(armorSkills: List<SkillLevel>, decorationSkills: List<SkillLevel>): List<SkillLevel> {
        val skills = armorSkills.associateBy({ it.skillTree.id }, { it }).toMutableMap()
        for (skill in decorationSkills) {
            if (skills.containsKey(skill.skillTree.id)) {
                val level = skills.getValue(skill.skillTree.id).level + skill.level
                val skillLevel = SkillLevel(level)
                skillLevel.skillTree = skill.skillTree
                skills[skill.skillTree.id] = skillLevel
            } else {
                skills[skill.skillTree.id] = skill
            }
        }
        val result = skills.values.toMutableList()
        result.sortWith(compareByDescending<SkillLevel> { it.level }.thenBy { it.skillTree.id })
        return result
    }
}