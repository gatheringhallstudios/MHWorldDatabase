package com.gatheringhallstudios.mhworlddatabase.features.workshop

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.ToolType
import com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors.WorkshopSelectorListFragment
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.ViewWorkshopWeaponCardviewBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ViewWorkshopHeaderExpandableCardviewEmptyBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ViewWorkshopHeaderExpandableCardviewBaseBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ViewWorkshopBodyCardviewBaseBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetBonusBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemSkillLevelBinding

/**
 * Wrapper over the ExpandableCardView used to display equipment data.
 * Used to
 */
class UserEquipmentCard(private val card: ExpandableCardView) {
    /** Bindings for the header layout currently installed on the card. */
    private val baseHeader get() = ViewWorkshopHeaderExpandableCardviewBaseBinding.bind(card.headerView)
    private val weaponHeader get() = ViewWorkshopWeaponCardviewBinding.bind(card.headerView)
    private val emptyHeader get() = ViewWorkshopHeaderExpandableCardviewEmptyBinding.bind(card.headerView)

    /** Binding for the standard card body, which every non-empty card uses. */
    private val baseBody get() = ViewWorkshopBodyCardviewBaseBinding.bind(card.bodyView)

    /**
     * The header layout differs per equipment type, so views shared between the base and
     * weapon headers are looked up by id rather than through a single binding.
     */
    private fun <T : View> header(id: Int): T = card.headerView.findViewById(id)

    private fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return card.resources.getString(resId, *formatArgs)
    }

    fun setCardState(cardState: ExpandableCardView.CardState) {
        card.setCardState(cardState)
    }

    /**
     * Binds a view only tool card
     */
    fun bindActiveTool(tool: UserTool?) {
        if (tool != null) {
            bindTool(tool.tool, null, null)
        } else {
            bindEmptyTool()
        }
    }

    /**
     * Binds a clickable tool card
     */
    fun bindUserTool(tool: UserTool?, setId: Int, orderId: Int, onClick: (() -> Unit)?, onSwipeRight: (() -> Unit)?,
                     onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        if (tool != null) {
            bindTool(tool.tool, onClick, onSwipeRight, onExpand, onContract)
            //Repopulate the skills section to include the decoration skills
            baseBody.skillList.removeAllViews()
            val skillsList = combineEquipmentSkillsWithDecorationSkills(emptyList(), tool.decorations.map {
                it.decoration.getSkillLevels()
            }.flatten())

            populateSkills(skillsList)
        } else {
            bindEmptyTool()
            card.setOnClick {
                card.getRouter().navigateUserEquipmentPieceSelector(WorkshopSelectorListFragment.Companion.SelectorMode.TOOL,
                        null, setId, null, orderId, null)
            }
        }
    }

    /**
     * Binds a tool entity to the card
     */
    fun bindTool(tool: Tool, onClick: (() -> Unit)?, onSwipeRight: (() -> Unit)?,
                     onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        card.setHeader(R.layout.view_workshop_header_expandable_cardview_base)
        card.setBody(R.layout.view_workshop_body_cardview_base)
        card.setCardElevation(1f)

        val header = baseHeader
        header.equipmentName.text = tool.name
        header.equipmentIcon.setImageDrawable(AssetLoader.loadIconFor(tool))
        header.defenseValue.visibility = View.INVISIBLE
        header.iconDefense.visibility = View.INVISIBLE
        header.rarityString.text = when (tool.tool_type) {
            ToolType.MANTLE -> getString(R.string.tool_mantle)
            ToolType.BOOSTER -> getString(R.string.tool_booster)
        }
        header.rarityString.visibility = View.VISIBLE

        val body = baseBody
        body.decorationsSection.visibility = View.GONE
        body.setBonusSection.visibility = View.GONE

        populateSetBonuses(emptyList())
        populateSkills(emptyList())

        if (onClick != null) card.setOnClick(onClick)
        if (onSwipeRight != null) card.setOnSwipeRight(onSwipeRight)
        if (onExpand != null) card.setOnExpand { onExpand() }
        if (onContract != null) card.setOnContract { onContract() }
    }

    fun bindActiveWeapon(userWeapon: UserWeapon?) {
        if (userWeapon != null) {
            bindWeapon(userWeapon.weapon, null, null)
            card.setCardElevation(2f)
        } else {
            bindEmptyWeapon()
        }
    }

    /**
    Binds a clickable user weapon card to the encapsulated card
     */
    fun bindWeapon(userWeapon: UserWeapon?, setId: Int, onClick: (() -> Unit)?,
                   onSwipeRight: (() -> Unit)?, onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        if (userWeapon != null) {
            bindWeapon(userWeapon.weapon, onClick, onSwipeRight, onExpand, onContract)

            //Repopulate the skills section to include the decoration skills
            baseBody.skillList.removeAllViews()
            val skillsList = combineEquipmentSkillsWithDecorationSkills(userWeapon.weapon.skills, userWeapon.decorations.map {
                it.decoration.getSkillLevels()
            }.flatten())

            populateSkills(skillsList)
        } else {
            bindEmptyWeapon()
            card.setOnClick {
                card.getRouter().navigateUserEquipmentPieceSelector(WorkshopSelectorListFragment.Companion.SelectorMode.WEAPON,
                        null, setId, null, null, null)
            }
        }
    }

    /**
     * Binds a weapon entity to the encapsulated card
     */
    fun bindWeapon(weaponFull: WeaponFull, onClick: (() -> Unit)?, onSwipeRight: (() -> Unit)?,
                   onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        val weapon = weaponFull.weapon
        card.setHeader(R.layout.view_workshop_weapon_cardview)
        card.setBody(R.layout.view_workshop_body_cardview_base)
        card.setCardElevation(1f)

        val header = weaponHeader
        header.equipmentName.text = weapon.name
        header.equipmentIcon.setImageDrawable(AssetLoader.loadIconFor(weapon))
        populateStaticWeaponStats(weaponFull.weapon)
        populateComplexStats(weaponFull.weapon)

        val body = baseBody
        body.decorationsSection.visibility = View.GONE
        body.setBonusSection.visibility = View.GONE

        bindRarity(weapon.rarity)
        populateSkills(weaponFull.skills)
        populateSetBonuses(emptyList())

        if (onClick != null) card.setOnClick(onClick)
        if (onSwipeRight != null) card.setOnSwipeRight(onSwipeRight)
        if (onExpand != null) card.setOnExpand { onExpand() }
        if (onContract != null) card.setOnContract { onContract() }
    }

    /**
     *
     */
    fun bindActiveArmor(userArmor: UserArmorPiece?, armorType: ArmorType) {
        if (userArmor != null) {
            bindArmor(userArmor.armor, null, null)
            card.setCardElevation(2f)
        } else {
            bindEmptyArmor(armorType)
        }
    }

    /**
    Binds a clickable head armor card to the encapsulated card
     */
    fun bindHeadArmor(userArmor: UserArmorPiece?, setId: Int, onClick: () -> Unit, onSwipeRight: () -> Unit,
                      onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        bindUserArmor(userArmor, ArmorType.HEAD, setId, onClick, onSwipeRight, onExpand, onContract)
    }

    /**
    Binds a clickable arm armor card to the encapsulated card
     */
    fun bindArmArmor(userArmor: UserArmorPiece?, setId: Int, onClick: () -> Unit, onSwipeRight: () -> Unit,
                     onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        bindUserArmor(userArmor, ArmorType.ARMS, setId, onClick, onSwipeRight, onExpand, onContract)
    }

    /**
    Binds a clickable chest armor card to the encapsulated card
     */
    fun bindChestArmor(userArmor: UserArmorPiece?, setId: Int, onClick: () -> Unit, onSwipeRight: () -> Unit,
                       onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        bindUserArmor(userArmor, ArmorType.CHEST, setId, onClick, onSwipeRight, onExpand, onContract)
    }

    /**
    Binds a clickable leg armor card to the encapsulated card
     */
    fun bindLegArmor(userArmor: UserArmorPiece?, setId: Int, onClick: () -> Unit, onSwipeRight: () -> Unit,
                     onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        bindUserArmor(userArmor, ArmorType.LEGS, setId, onClick, onSwipeRight, onExpand, onContract)
    }

    /**
    Binds a view only waist armor card to the encapsulated card
     */
    fun bindWaistArmor(userArmor: UserArmorPiece?, setId: Int, onClick: () -> Unit, onSwipeRight: () -> Unit,
                       onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        bindUserArmor(userArmor, ArmorType.WAIST, setId, onClick, onSwipeRight, onExpand, onContract)
    }

    private fun bindUserArmor(userArmor: UserArmorPiece?, armorType: ArmorType, setId: Int? = null,
                              onClick: (() -> Unit)? = null, onSwipeRight: (() -> Unit)? = null,
                              onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        if (userArmor != null) {
            val armor = userArmor.armor
            bindArmor(armor, onClick, onSwipeRight, onExpand, onContract)
            //Repopulate the skills section to include the decoration skills
            baseBody.skillList.removeAllViews()
            val skillsList = combineEquipmentSkillsWithDecorationSkills(armor.skills, userArmor.decorations.map {
                it.decoration.getSkillLevels()
            }.flatten())

            populateSkills(skillsList)
        } else {
            bindEmptyArmor(armorType)
            card.setOnClick {
                card.getRouter().navigateUserEquipmentPieceSelector(WorkshopSelectorListFragment.Companion.SelectorMode.ARMOR,
                        null, setId, armorType, null, null)
            }
        }
    }

    fun bindArmor(armor: ArmorFull, onClick: (() -> Unit)?, onSwipeRight: (() -> Unit)?,
                  onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        card.setHeader(R.layout.view_workshop_header_expandable_cardview_base)
        card.setBody(R.layout.view_workshop_body_cardview_base)
        card.setCardElevation(1f)

        val header = baseHeader
        header.equipmentName.text = armor.armor.name
        header.equipmentIcon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
        header.defenseValue.text = getString(
                R.string.armor_defense_value,
                armor.armor.defense_base,
                armor.armor.defense_max,
                armor.armor.defense_augment_max)

        baseBody.decorationsSection.visibility = View.GONE
        bindRarity(armor.armor.rarity)
        populateSkills(armor.skills)
        populateSetBonuses(armor.setBonuses)
        if (onClick != null) card.setOnClick(onClick)
        if (onSwipeRight != null) card.setOnSwipeRight(onSwipeRight)
        if (onExpand != null) card.setOnExpand { onExpand() }
        if (onContract != null) card.setOnContract { onContract() }
    }

    /**
     * Binds a view only active charm card to the encapsulated card. The active card is the top most card on the
     * selector fragment
     */
    fun bindActiveCharm(userCharm: UserCharm?) {
        bindCharm(userCharm)
        card.setCardElevation(2f)
    }

    /**
     * Binds a view only charm card to the encapsulated card
     */
    fun bindCharm(userCharm: UserCharm?) {
        if (userCharm != null) {
            bindCharm(userCharm.charm, null, null)
        } else {
            bindEmptyCharm()
        }
    }

    /**
     * Binds a clickable charm card to the encapsulated card
     */
    fun bindCharm(userCharm: UserCharm?, setId: Int, onClick: () -> Unit, onSwipeRight: () -> Unit,
                  onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {
        if (userCharm != null) {
            bindCharm(userCharm.charm, onClick, onSwipeRight)

        } else {
            bindEmptyCharm()
            card.setOnClick {
                card.getRouter().navigateUserEquipmentPieceSelector(WorkshopSelectorListFragment.Companion.SelectorMode.CHARM,
                        null, setId, null, null, null)
            }
        }
    }

    /**
     * Bind a charm entity to the encapsulated card
     */
    fun bindCharm(charm: CharmFull, onClick: (() -> Unit)?, onSwipeRight: (() -> Unit)?,
                  onExpand: (() -> Unit)? = null, onContract: (() -> Unit)? = null) {

        card.setHeader(R.layout.view_workshop_header_expandable_cardview_base)
        card.setBody(R.layout.view_workshop_body_cardview_base)
        card.setCardElevation(1f)

        val header = baseHeader
        header.equipmentName.text = charm.charm.name
        header.equipmentIcon.setImageDrawable(AssetLoader.loadIconFor(charm.charm))
        header.defenseValue.visibility = View.GONE
        header.iconDefense.visibility = View.GONE

        val body = baseBody
        body.decorationsSection.visibility = View.GONE
        bindRarity(charm.charm.rarity)
        populateSkills(charm.skills)
        populateSetBonuses(emptyList())
        hideSlots()

        if (onClick != null) card.setOnClick(onClick)
        if (onSwipeRight != null) card.setOnSwipeRight(onSwipeRight)
        if (onExpand != null) card.setOnExpand { onExpand() }
        if (onContract != null) card.setOnContract { onContract() }
    }

    /**
     * Internal function to enable the rarity string and display the value
     */
    private fun bindRarity(rarity: Int) {
        val rarityString = header<TextView>(R.id.rarity_string)
        rarityString.text = getString(R.string.format_rarity, rarity)
        rarityString.setTextColor(AssetLoader.loadRarityColor(rarity))
        rarityString.visibility = View.VISIBLE
    }

    /**
     * Binds a view only decoration card to the encapsulated card
     */
    fun bindDecoration(userDecoration: UserDecoration?, slotSize: Int) {
        if (userDecoration != null) {
            bindDecoration(userDecoration.decoration, null)
        } else {
            bindEmptyDecoration(slotSize)
        }
    }

    /**
     * Bind a charm entity to the encapsulated card
     */
    fun bindDecoration(decoration: Decoration, onClick: (() -> Unit)?) {

        card.setHeader(R.layout.view_workshop_header_expandable_cardview_base)
        card.setBody(R.layout.view_workshop_body_cardview_base)
        card.setCardElevation(1f)

        card.setOnClick {
            onClick?.invoke()
        }

        val header = baseHeader
        header.equipmentName.text = decoration.name
        header.equipmentIcon.setImageDrawable(AssetLoader.loadIconFor(decoration))
        header.defenseValue.visibility = View.GONE
        header.iconDefense.visibility = View.GONE
        header.iconSlots.visibility = View.GONE
        header.slotSection.visibility = View.GONE

        val body = baseBody
        body.setBonusSection.visibility = View.GONE
        body.decorationsSection.visibility = View.GONE

        bindRarity(decoration.rarity)
        populateSkills(decoration.getSkillLevels())
    }

    fun bindEmptyWeapon() {
        setEmptyView(R.string.user_equipment_set_no_equipment, R.drawable.ic_equipment_weapon_empty)
    }

    fun bindEmptyArmor(type: ArmorType?) {
        setEmptyView(R.string.user_equipment_set_no_equipment, when (type) {
            ArmorType.HEAD -> R.drawable.ic_equipment_head_empty
            ArmorType.CHEST -> R.drawable.ic_equipment_chest_empty
            ArmorType.ARMS -> R.drawable.ic_equipment_arm_empty
            ArmorType.WAIST -> R.drawable.ic_equipment_waist_empty
            ArmorType.LEGS -> R.drawable.ic_equipment_leg_empty
            else -> R.drawable.ic_equipment_charm_empty
        })
    }

    fun bindEmptyCharm() {
        setEmptyView(R.string.user_equipment_set_no_equipment, R.drawable.ic_equipment_charm_empty)
    }

    fun bindEmptyDecoration(slotSize: Int) {
        setEmptyView(R.string.user_equipment_set_no_decoration, when (slotSize) {
            1 -> R.drawable.ic_ui_slot_1_empty
            2 -> R.drawable.ic_ui_slot_2_empty
            3 -> R.drawable.ic_ui_slot_3_empty
            4 -> R.drawable.ic_ui_slot_4_empty
            else -> R.drawable.ic_ui_slot_none
        })
    }

    fun bindEmptyTool() {
        setEmptyView(R.string.user_equipment_set_no_equipment, R.drawable.ic_equipment_mantle_empty)
    }

    fun populateSkills(skills: List<SkillLevel>) {
        val body = baseBody
        val skillList = body.skillList

        skillList.removeAllViews()
        body.skillSection.isVisible = !skills.isEmpty()

        val inflater = LayoutInflater.from(card.context)
        for (skill in skills) {
            //Set the label for the Set name
            val skillBinding = ListitemSkillLevelBinding.inflate(inflater, skillList, false)

            skillBinding.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            skillBinding.labelText.text = skill.skillTree.name
            skillBinding.levelText.text = getString(R.string.level_qty, skill.level)
            with(skillBinding.skillLevel) {
                maxLevel = skill.skillTree.max_level
                secretLevels = skill.skillTree.secret
                level = skill.level
            }

            skillBinding.root.setOnClickListener {
                card.getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            skillList.addView(skillBinding.root)
        }
    }

    fun populateSetBonuses(setBonuses: List<ArmorSetBonus>) {
        val cardBody = baseBody
        cardBody.setBonusList.removeAllViews()
        cardBody.setBonusSection.visibility = if (setBonuses.isEmpty()) View.GONE else View.VISIBLE
        val inflater = LayoutInflater.from(cardBody.root.context)
        //Now to set the actual skills
        for (setBonus in setBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = ListitemArmorsetBonusBinding.inflate(inflater, cardBody.setBonusList, false)
            listItem.bonusSkillIcon.setImageDrawable(skillIcon)
            listItem.bonusSkillName.text = setBonus.skillTree.name
            listItem.bonusRequirement.setImageResource(reqIcon)
            listItem.root.setOnClickListener {
                card.getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }
            cardBody.setBonusList.addView(listItem.root)
        }
    }

    /**
     * Populates the slot icons, but hides the decorations section.
     * If you want decorations to be selectable, use populateDecorations instead.
     */
    fun populateSlots(slots: EquipmentSlots?) {
        if (slots != null) {
            this.populateDecorations(slots, emptyList())
            baseBody.decorationsSection.visibility = View.GONE
        }
    }

    /**
     * Populates the decoration section for most equipment pieces.
     * For each callback, it will either return the UserDecoration that was clicked, or
     * the slot number (1-indexed) of the clicked slot.
     */
    fun populateDecorations(slots: EquipmentSlots, decorations: List<UserDecoration>,
                            onEmptyClick: ((Int) -> Unit)? = null,
                            onClick: ((Int, UserDecoration) -> Unit)? = null,
                            onDelete: ((UserDecoration) -> Unit)? = null) {
        val body = baseBody
        body.decorationsSection.visibility = if (slots.isEmpty()) View.GONE else View.VISIBLE
        body.slot1Detail.visibility = View.GONE
        body.slot2Detail.visibility = View.GONE
        body.slot3Detail.visibility = View.GONE

        // Bind decorations that exist first
        slots.active.forEachIndexed { idx, slotSize ->
            val slotNumber = idx + 1
            val userDecoration = decorations.find { it.slotNumber == slotNumber }
            val decoration = userDecoration?.decoration

            val imageView = when (slotNumber) {
                1 -> header<ImageView>(R.id.slot1)
                2 -> header<ImageView>(R.id.slot2)
                3 -> header<ImageView>(R.id.slot3)
                else -> throw IndexOutOfBoundsException("SlotIdx is out of range 1-3: $slotNumber")
            }

            val detailView = when (slotNumber) {
                1 -> body.slot1Detail
                2 -> body.slot2Detail
                3 -> body.slot3Detail
                else -> throw IndexOutOfBoundsException("SlotIdx is out of range 1-3: $slotNumber")
            }

            detailView.visibility = View.VISIBLE
            detailView.removeDecorator()

            if (decoration != null) {
                imageView.setImageDrawable(AssetLoader.loadFilledSlotIcon(decoration, slotSize))
                detailView.setLabelText(decoration.name)
                detailView.setLeftIconDrawable(AssetLoader.loadFilledSlotIcon(decoration, slotSize))

                detailView.setOnClickListener {
                    onClick?.invoke(slotNumber, userDecoration)
                }

                detailView.setButtonClickFunction {
                    onDelete?.invoke(userDecoration)
                }
            } else {
                imageView.setImageDrawable(card.context!!.getDrawableCompat(SlotEmptyRegistry(slotSize)))
                detailView.setLeftIconDrawable(card.context!!.getDrawableCompat(SlotEmptyRegistry(slotSize)))
                detailView.setLabelText(getString(R.string.user_equipment_set_no_decoration))
                detailView.hideButton()

                detailView.setOnClickListener {
                    onEmptyClick?.invoke(slotNumber)
                }
            }
        }
    }

    private fun setEmptyView(@StringRes title: Int, @DrawableRes icon: Int) {
        card.setHeader(R.layout.view_workshop_header_expandable_cardview_empty)
        card.setBody(R.layout.view_workshop_body_cardview_empty)
        val header = emptyHeader
        header.newEquipmentSetLabel.text = getString(title)
        header.equipmentSetIcon2.setImageResource(icon)

    }

    private fun hideSlots() {
        header<View>(R.id.icon_slots).visibility = View.GONE
        header<View>(R.id.slot1).visibility = View.GONE
        header<View>(R.id.slot2).visibility = View.GONE
        header<View>(R.id.slot3).visibility = View.GONE
    }

    private fun combineEquipmentSkillsWithDecorationSkills(equipmentSkills: List<SkillLevel>, decorationSkills: List<SkillLevel>): List<SkillLevel> {
        val skills = equipmentSkills.associateBy({ it.skillTree.id }, { it }).toMutableMap()
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

    /**
     * Populate weapon static stats
     */
    private fun populateStaticWeaponStats(weapon: Weapon) {
        val header = weaponHeader
        header.attackValue.text = if (AppSettings.showTrueAttackValues) weapon.attack_true.toString()
        else weapon.attack.toString()

        //Render sharpness data if it exists, else hide the bars
        val sharpnessData = weapon.sharpnessData
        if (sharpnessData != null) {
            header.sharpnessContainer.visibility = View.VISIBLE
            header.sharpnessValue.drawSharpness(sharpnessData.min)
            header.sharpnessMaxValue.drawSharpness(sharpnessData.max)
        } else {
            header.sharpnessContainer.visibility = View.GONE
        }
    }

    /**
     * Populate weapon complex stats
     */
    private fun populateComplexStats(weapon: Weapon) {
        val header = weaponHeader
        // Elemental Stat (added if there's a value)
        if (weapon.element1 != null) {
            header.elementValue.setLeftIconDrawable(AssetLoader.loadElementIcon(weapon.element1))
            header.elementValue.setLabelText(createElementString(weapon.element1_attack, weapon.element_hidden))
            if (weapon.element_hidden) {
                header.elementValue.labelView.alpha = 0.5.toFloat()
            } else {
                header.elementValue.labelView.alpha = 1.0.toFloat()
            }
            header.elementValue.visibility = View.VISIBLE
        } else {
            header.elementValue.visibility = View.INVISIBLE
        }

        // Affinity (added if there's a value)
        if (weapon.affinity != 0) {
            val affinityValue = card.headerView.context.getString(R.string.format_plus_percentage, weapon.affinity)
            header.affinityValue.setLabelText(affinityValue)
            header.affinityValue.labelView.setTextColor(ContextCompat.getColor(card.context, when {
                weapon.affinity > 0 -> R.color.textColorGreen
                else -> R.color.textColorRed
            }))

            header.affinityValue.visibility = View.VISIBLE
        } else {
            header.affinityValue.visibility = View.INVISIBLE
        }

        // Defense, added if there's a value
        if (weapon.defense != 0) {
            val defenseValue = card.context.getString(R.string.format_plus, weapon.defense)
            header.weaponDefenseValue.setLabelText(defenseValue)
            header.weaponDefenseValue.labelView.setTextColor(ContextCompat.getColor(card.context, when {
                weapon.defense > 0 -> R.color.textColorGreen
                else -> R.color.textColorRed
            }))
            header.weaponDefenseValue.visibility = View.VISIBLE
        } else {
            header.weaponDefenseValue.visibility = View.INVISIBLE
        }
    }

    private fun createElementString(element1_attack: Int?, element_hidden: Boolean): String {
        val workString = element1_attack ?: "-----"

        return when (element_hidden) {
            true -> "($workString)"
            false -> workString.toString()
        }
    }
}