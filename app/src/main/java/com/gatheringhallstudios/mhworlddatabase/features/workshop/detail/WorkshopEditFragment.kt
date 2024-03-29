package com.gatheringhallstudios.mhworlddatabase.features.workshop.detail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.features.workshop.UserEquipmentCard
import com.gatheringhallstudios.mhworlddatabase.features.workshop.UserEquipmentSetViewModel
import com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors.WorkshopSelectorListFragment.Companion
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import kotlinx.android.synthetic.main.fragment_workshop_editor.*

class WorkshopEditFragment : androidx.fragment.app.Fragment(), RenameSetDialog.RenameDialogListener {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(UserEquipmentSetViewModel::class.java)
    }

    private lateinit var weaponCard: UserEquipmentCard
    private lateinit var headArmorCard: UserEquipmentCard
    private lateinit var armArmorCard: UserEquipmentCard
    private lateinit var chestArmorCard: UserEquipmentCard
    private lateinit var waistArmorCard: UserEquipmentCard
    private lateinit var legArmorCard: UserEquipmentCard
    private lateinit var charmCard: UserEquipmentCard
    private lateinit var tool1Card: UserEquipmentCard
    private lateinit var tool2Card: UserEquipmentCard
    private var scrollY = 0

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
    }

    override fun onDialogNegativeClick(dialog: RenameSetDialog) {
        // User touched the dialog's negative button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workshop_editor, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.activeUserEquipmentSet.observe(viewLifecycleOwner, Observer<UserEquipmentSet> {
            populateUserEquipmentSet(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workshop_editor, menu)
    }

    override fun onResume() {
        super.onResume()

        if (scrollY != 0) user_equipment_editor_scroll_view.scrollTo(0, scrollY)
        viewModel.armorSetCardStates.forEach { (key, value) ->
            when (key) {
                0 -> weaponCard.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                1 -> headArmorCard.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                2 -> armArmorCard.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                3 -> chestArmorCard.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                4 -> waistArmorCard.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                5 -> legArmorCard.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                6 -> charmCard.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                7 -> tool1Card.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
                8 -> tool2Card.setCardState(if (value) ExpandableCardView.CardState.EXPANDED else ExpandableCardView.CardState.COLLAPSED)
            }
        }

        resetCardIfNewEquipmentChosen()
    }

    override fun onPause() {
        super.onPause()
        scrollY = user_equipment_editor_scroll_view.scrollY
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.resetCardStates()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.weaponCard = UserEquipmentCard(user_equipment_weapon_slot)
        this.headArmorCard = UserEquipmentCard(user_equipment_head_slot)
        this.armArmorCard = UserEquipmentCard(user_equipment_arms_slot)
        this.chestArmorCard = UserEquipmentCard(user_equipment_chest_slot)
        this.waistArmorCard = UserEquipmentCard(user_equipment_waist_slot)
        this.legArmorCard = UserEquipmentCard(user_equipment_legs_slot)
        this.charmCard = UserEquipmentCard(user_equipment_charm_slot)
        this.tool1Card = UserEquipmentCard(user_equipment_tool_1_slot)
        this.tool2Card = UserEquipmentCard(user_equipment_tool_2_slot)
    }

    private fun populateUserEquipmentSet(userEquipmentSet: UserEquipmentSet) {
        setActivityTitle(userEquipmentSet.name)

        val weapon = userEquipmentSet.getWeapon()
        weaponCard.bindWeapon(weapon, userEquipmentSet.id,
                onClick = {
                    viewModel.activeUserEquipment = weapon
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.WEAPON, weapon,
                            userEquipmentSet.id, null, null, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(weapon!!)
                    viewModel.deleteUserEquipment(weapon!!.entityId(), userEquipmentSet.id, weapon.type())
                    viewModel.updateCardState(0, false)
                },
                onExpand = { viewModel.updateCardState(0, true) },
                onContract = { viewModel.updateCardState(0, false) })
        if (weapon != null) {
            populateDecorations(weapon, userEquipmentSet.id, weaponCard)
        }

        val headArmor = userEquipmentSet.getHeadArmor()
        headArmorCard.bindHeadArmor(headArmor, userEquipmentSet.id,
                onClick = {
                    viewModel.activeUserEquipment = headArmor
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, headArmor,
                            userEquipmentSet.id, headArmor!!.armor.armor.armor_type, null, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(headArmor!!)
                    viewModel.deleteUserEquipment(headArmor!!.entityId(), userEquipmentSet.id, headArmor!!.type())
                    viewModel.updateCardState(1, false)
                },
                onExpand = { viewModel.updateCardState(1, true) },
                onContract = { viewModel.updateCardState(1, false) })
        if (headArmor != null) {
            populateDecorations(headArmor, userEquipmentSet.id, headArmorCard)
        }

        val armArmor = userEquipmentSet.getArmArmor()
        armArmorCard.bindArmArmor(armArmor, userEquipmentSet.id,
                onClick = {
                    viewModel.activeUserEquipment = armArmor
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, armArmor,
                            userEquipmentSet.id, armArmor!!.armor.armor.armor_type, null, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(armArmor!!)
                    viewModel.deleteUserEquipment(armArmor!!.entityId(), userEquipmentSet.id, armArmor.type())
                    viewModel.updateCardState(2, false)
                },
                onExpand = { viewModel.updateCardState(2, true) },
                onContract = { viewModel.updateCardState(2, false) })
        if (armArmor != null) {
            populateDecorations(armArmor, userEquipmentSet.id, armArmorCard)
        }

        val chestArmor = userEquipmentSet.getChestArmor()
        chestArmorCard.bindChestArmor(chestArmor, userEquipmentSet.id,
                onClick = {
                    viewModel.activeUserEquipment = chestArmor
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, chestArmor,
                            userEquipmentSet.id, chestArmor!!.armor.armor.armor_type, null, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(chestArmor!!)
                    viewModel.deleteUserEquipment(chestArmor!!.entityId(), userEquipmentSet.id, chestArmor.type())
                    viewModel.updateCardState(3, false)
                },
                onExpand = { viewModel.updateCardState(3, true) },
                onContract = { viewModel.updateCardState(3, false) })
        if (chestArmor != null) {
            populateDecorations(chestArmor, userEquipmentSet.id, chestArmorCard)
        }

        val waistArmor = userEquipmentSet.getWaistArmor()
        waistArmorCard.bindWaistArmor(waistArmor, userEquipmentSet.id,
                onClick = {
                    viewModel.activeUserEquipment = waistArmor
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, waistArmor,
                            userEquipmentSet.id, waistArmor!!.armor.armor.armor_type, null, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(waistArmor!!)
                    viewModel.deleteUserEquipment(waistArmor!!.entityId(), userEquipmentSet.id, waistArmor.type())
                    viewModel.updateCardState(4, false)
                },
                onExpand = { viewModel.updateCardState(4, true) },
                onContract = { viewModel.updateCardState(4, false) })
        if (waistArmor != null) {
            populateDecorations(waistArmor, userEquipmentSet.id, waistArmorCard)
        }

        val legArmor = userEquipmentSet.getLegArmor()
        legArmorCard.bindLegArmor(legArmor, userEquipmentSet.id,
                onClick = {
                    viewModel.activeUserEquipment = legArmor
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.ARMOR, legArmor,
                            userEquipmentSet.id, legArmor!!.armor.armor.armor_type, null, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(legArmor!!)
                    viewModel.deleteUserEquipment(legArmor!!.entityId(), userEquipmentSet.id, legArmor.type())
                    viewModel.updateCardState(5, false)
                },
                onExpand = { viewModel.updateCardState(5, true) },
                onContract = { viewModel.updateCardState(5, false) })
        if (legArmor != null) {
            populateDecorations(legArmor, userEquipmentSet.id, legArmorCard)
        }

        val charm = userEquipmentSet.getCharm()
        charmCard.bindCharm(charm, userEquipmentSet.id,
                onClick = {
                    viewModel.activeUserEquipment = charm
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.CHARM, charm, userEquipmentSet.id,
                            null, null, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(charm!!)
                    viewModel.deleteUserEquipment(charm!!.entityId(), userEquipmentSet.id, charm.type())
                    viewModel.updateCardState(6, false)
                },
                onExpand = { viewModel.updateCardState(6, true) },
                onContract = { viewModel.updateCardState(6, false) })

        val tool1 = userEquipmentSet.getTool1()
        tool1Card.bindUserTool(tool1, userEquipmentSet.id,1,
                onClick = {
                    viewModel.activeUserEquipment = tool1
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.TOOL, tool1, userEquipmentSet.id,
                            null, 1, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(tool1!!)
                    viewModel.deleteUserEquipment(tool1!!.entityId(), userEquipmentSet.id, tool1.type())
                    viewModel.updateCardState(7, false)
                },
                onExpand = { viewModel.updateCardState(7, true) },
                onContract = { viewModel.updateCardState(7, false) })
        if (tool1 != null) {
            populateDecorations(tool1, userEquipmentSet.id, tool1Card)
        }
        val tool2 = userEquipmentSet.getTool2()
        tool2Card.bindUserTool(tool2, userEquipmentSet.id, 2,
                onClick = {
                    viewModel.activeUserEquipment = tool2
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.TOOL, tool2, userEquipmentSet.id,
                            null, 2, null)
                },
                onSwipeRight = {
                    viewModel.activeUserEquipmentSet.value?.equipment?.remove(tool2!!)
                    viewModel.deleteUserEquipment(tool2!!.entityId(), userEquipmentSet.id, tool2.type())
                    viewModel.updateCardState(8, false)
                },
                onExpand = { viewModel.updateCardState(8, true) },
                onContract = { viewModel.updateCardState(8, false) })
        if (tool2 != null) {
            populateDecorations(tool2, userEquipmentSet.id, tool2Card)
        }
    }

    private fun populateDecorations(userEquipment: UserEquipment?, userEquipmentSetId: Int, card: UserEquipmentCard) {
        val slots = if ((userEquipment as? UserArmorPiece) != null) {
            userEquipment.armor.armor.slots
        } else if ((userEquipment as? UserWeapon) != null) {
            userEquipment.weapon.weapon.slots
        } else if ((userEquipment as? UserTool) != null) {
            userEquipment.tool.slots
        } else {
            return
        }

        val decorations = if ((userEquipment as? UserArmorPiece) != null) {
            userEquipment.decorations
        } else if ((userEquipment as? UserWeapon) != null) {
            userEquipment.decorations
        } else if ((userEquipment as? UserTool) != null) {
            userEquipment.decorations
        } else {
            mutableListOf()
        }

        card.populateDecorations(slots, decorations,
                onEmptyClick = { slotNumber ->
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION, null,
                            userEquipmentSetId, null, null,
                            Companion.DecorationsConfig(userEquipment.entityId(), slotNumber, userEquipment.type(), slots[slotNumber - 1]))
                },
                onClick = { slotNumber, userDecoration ->
                    viewModel.activeUserEquipment = userDecoration
                    getRouter().navigateUserEquipmentPieceSelector(Companion.SelectorMode.DECORATION,
                            userDecoration, userEquipmentSetId, null, null,
                            Companion.DecorationsConfig(
                                    userEquipment.entityId(), userDecoration.slotNumber,
                                    userEquipment.type(), slots[slotNumber - 1]))

                },
                onDelete = { userDecoration ->
                    viewModel.deleteDecorationForEquipment(userDecoration.decoration.id,
                            userEquipment.entityId(), userDecoration.slotNumber, userEquipment.type(), userEquipmentSetId)

                }
        )
    }

    private fun resetCardIfNewEquipmentChosen() {
        when (viewModel.activeUserEquipment?.type()) {
            DataType.CHARM -> {
                if (viewModel.activeUserEquipment?.entityId() != viewModel.activeUserEquipmentSet.value?.getCharm()?.entityId()) {
                    charmCard.setCardState(ExpandableCardView.CardState.COLLAPSED)
                    viewModel.updateCardState(6, false)
                }
            }
            DataType.ARMOR -> {
                val armor = viewModel.activeUserEquipment as UserArmorPiece?
                when (armor?.armor?.armor?.armor_type) {
                    ArmorType.HEAD -> {
                        if (armor.entityId() != viewModel.activeUserEquipmentSet.value?.getHeadArmor()?.entityId()) {
                            headArmorCard.setCardState(ExpandableCardView.CardState.COLLAPSED)
                            viewModel.updateCardState(1, false)
                        }
                    }
                    ArmorType.CHEST -> {
                        if (armor.entityId() != viewModel.activeUserEquipmentSet.value?.getChestArmor()?.entityId()) {
                            chestArmorCard.setCardState(ExpandableCardView.CardState.COLLAPSED)
                            viewModel.updateCardState(2, false)
                        }
                    }
                    ArmorType.ARMS -> {
                        if (armor.entityId() != viewModel.activeUserEquipmentSet.value?.getArmArmor()?.entityId()) {
                            armArmorCard.setCardState(ExpandableCardView.CardState.COLLAPSED)
                            viewModel.updateCardState(3, false)
                        }
                    }
                    ArmorType.WAIST -> {
                        if (armor.entityId() != viewModel.activeUserEquipmentSet.value?.getWaistArmor()?.entityId()) {
                            waistArmorCard.setCardState(ExpandableCardView.CardState.COLLAPSED)
                            viewModel.updateCardState(4, false)
                        }
                    }
                    ArmorType.LEGS -> {
                        if (armor.entityId() != viewModel.activeUserEquipmentSet.value?.getLegArmor()?.entityId()) {
                            legArmorCard.setCardState(ExpandableCardView.CardState.COLLAPSED)
                            viewModel.updateCardState(5, false)
                        }
                    }
                }
            }
            DataType.WEAPON -> {
                if (viewModel.activeUserEquipment?.entityId() != viewModel.activeUserEquipmentSet.value?.getWeapon()?.entityId()) {
                    weaponCard.setCardState(ExpandableCardView.CardState.COLLAPSED)
                    viewModel.updateCardState(0, false)

                }
            }
            DataType.TOOL -> {
                if (viewModel.activeUserEquipment?.entityId() != viewModel.activeUserEquipmentSet.value?.getTool1()?.entityId()) {
                    tool1Card.setCardState(ExpandableCardView.CardState.COLLAPSED)
                    viewModel.updateCardState(7, false)
                }

                if (viewModel.activeUserEquipment?.entityId() != viewModel.activeUserEquipmentSet.value?.getTool2()?.entityId()) {
                    tool2Card.setCardState(ExpandableCardView.CardState.COLLAPSED)
                    viewModel.updateCardState(8, false)
                }
            }
            else -> {
            }
        }
    }
}