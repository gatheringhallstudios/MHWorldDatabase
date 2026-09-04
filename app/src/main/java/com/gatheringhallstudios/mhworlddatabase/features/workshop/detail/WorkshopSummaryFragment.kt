package com.gatheringhallstudios.mhworlddatabase.features.workshop.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.components.CompactStatCell
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.ToolType
import com.gatheringhallstudios.mhworlddatabase.features.workshop.UserEquipmentSetViewModel
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemWeaponBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetArmorBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetBonusBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemSkillLevelBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentWorkshopSummaryBinding

class WorkshopSummaryFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentWorkshopSummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserEquipmentSetViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserEquipmentSetViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWorkshopSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.activeUserEquipmentSet.observe(viewLifecycleOwner, Observer<UserEquipmentSet> {
            populateUserEquipmentSummary(it, AppSettings.showTrueAttackValues)
        })
    }

    private fun populateUserEquipmentSummary(userEquipmentSet: UserEquipmentSet, showTrueAttackValues: Boolean) {
        binding.armorSetPieceList.removeAllViews()
        binding.armorSetSkillList.removeAllViews()
        binding.armorSetSetBonusList.removeAllViews()
        binding.weaponList.removeAllViews()

        binding.armorSetHeader.setIconDrawable(requireContext().getVectorDrawable("ArmorChest", "rare${userEquipmentSet.maxRarity}"))

        binding.armorSetHeader.setTitleText(userEquipmentSet.name)
        binding.armorSetDefenseValue.text = getString(
                R.string.armor_defense_value,
                userEquipmentSet.defense_base,
                userEquipmentSet.defense_max,
                userEquipmentSet.defense_augment_max)

        binding.armorSetFireValue.text = userEquipmentSet.fireDefense.toString()
        binding.armorSetWaterValue.text = userEquipmentSet.waterDefense.toString()
        binding.armorSetThunderValue.text = userEquipmentSet.thunderDefense.toString()
        binding.armorSetIceValue.text = userEquipmentSet.iceDefense.toString()
        binding.armorSetDragonValue.text = userEquipmentSet.dragonDefense.toString()

        populateWeapon(userEquipmentSet.equipment.filter { it.type() == DataType.WEAPON }, showTrueAttackValues)
        populateArmorSetPieces(userEquipmentSet.equipment.filter { it.type() == DataType.ARMOR }.sortedWith(compareBy { (it as UserArmorPiece).armor.armor.armor_type }))
        populateCharm(userEquipmentSet.equipment.filter { it.type() == DataType.CHARM})
        populateTools(userEquipmentSet.equipment.filter { it.type() == DataType.TOOL }.sortedBy { (it as UserTool).orderId })
        populateArmorSkills(userEquipmentSet.skills)
        populateArmorSetBonuses(userEquipmentSet.setBonuses)
    }

    private fun populateWeapon(userWeapons: List<UserEquipment>, showTrueAttackValues: Boolean) {
        if (userWeapons.isNullOrEmpty()) {
            val view = layoutInflater.inflate(R.layout.listitem_empty_no_margin, binding.weaponList, false)
            binding.weaponList.addView(view)
            return
        }

        val weapon = (userWeapons.first() as UserWeapon).weapon
        val weaponBinding = ListitemWeaponBinding.inflate(layoutInflater, binding.weaponList, false)

        weaponBinding.weaponName.text = weapon.weapon.name
        weaponBinding.weaponImage.setImageDrawable(AssetLoader.loadIconFor(weapon.weapon))
        weaponBinding.weaponCraftableImage.visibility = when {
            weapon.weapon.craftable -> View.VISIBLE
            else -> View.GONE
        }

        // Populate static stats like attack, affinity...
        populateStaticStats(weapon.weapon, weaponBinding, showTrueAttackValues)
        // Populate decorationIds
        populateDecorations(weapon.weapon, weaponBinding)
        // Populate stats like element, defense...
        populateComplexStats(weapon.weapon, weaponBinding)

        weaponBinding.root.setOnClickListener {
            getRouter().navigateWeaponDetail(weapon.weapon.id)
        }

        binding.weaponList.addView(weaponBinding.root)
    }

    private fun populateComplexStats(weapon: Weapon, weaponView: ListitemWeaponBinding) {
        // Clear the placeholder layouts
        weaponView.complexStatLayout.removeAllViews()

        // Elemental Stat (added if there's a value)
        if (weapon.element1 != null) {
            val elementView = CompactStatCell(
                    context,
                    AssetLoader.loadElementIcon(weapon.element1),
                    createElementString(weapon.element1_attack, weapon.element_hidden))

            if (weapon.element_hidden) {
                elementView.labelView.alpha = 0.5.toFloat()
            } else {
                elementView.labelView.alpha = 1.0.toFloat()
            }

            weaponView.complexStatLayout.addView(elementView)
        }

        // Affinity (added if there's a value)
        if (weapon.affinity != 0) {
            val affinityValue = getString(R.string.format_plus_percentage, weapon.affinity)

            val affinityView = CompactStatCell(
                    context,
                    R.drawable.ic_ui_affinity,
                    affinityValue)

            affinityView.labelView.setTextColor(ContextCompat.getColor(requireContext(), when {
                weapon.affinity > 0 -> R.color.textColorGreen
                else -> R.color.textColorRed
            }))

            weaponView.complexStatLayout.addView(affinityView)
        }

        // Defense, added if there's a value
        if (weapon.defense != 0) {
            val defenseValue = getString(R.string.format_plus, weapon.defense)
            val defenseView = CompactStatCell(
                    context,
                    R.drawable.ic_ui_defense,
                    defenseValue
            )

            defenseView.labelView.setTextColor(ContextCompat.getColor(requireContext(), when {
                weapon.defense > 0 -> R.color.textColorGreen
                else -> R.color.textColorRed
            }))

            weaponView.complexStatLayout.addView(defenseView)
        }
    }

    private fun populateStaticStats(weapon: Weapon, view: ListitemWeaponBinding, showTrueAttackValues: Boolean) {
        view.attackValue.setLabelText(
                if (showTrueAttackValues) weapon.attack_true.toString()
                else weapon.attack.toString())

        //Render sharpness data if it exists, else hide the bars
        val sharpnessData = weapon.sharpnessData
        if (sharpnessData != null) {
            view.sharpnessContainer.visibility = View.VISIBLE
            view.sharpnessValue.drawSharpness(sharpnessData.min)
            view.sharpnessMaxValue.drawSharpness(sharpnessData.max)
        } else {
            view.sharpnessContainer.visibility = View.GONE
        }
    }

    private fun populateDecorations(weapon: Weapon, weaponView: ListitemWeaponBinding) {
        val slotImages = weapon.slots.map {
            weaponView.root.context.getDrawableCompat(SlotEmptyRegistry(it))
        }

        weaponView.slot1.setImageDrawable(slotImages[0])
        weaponView.slot2.setImageDrawable(slotImages[1])
        weaponView.slot3.setImageDrawable(slotImages[2])

        // Hide views if no slots
        weaponView.slot1.visibility = when (weapon.slots[0]) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
        weaponView.slot2.visibility = when (weapon.slots[1]) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
        weaponView.slot3.visibility = when (weapon.slots[2]) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun populateArmorSetPieces(armorPieces: List<UserEquipment>) {
        if (armorPieces.isNullOrEmpty()) {
            val view = layoutInflater.inflate(R.layout.listitem_empty_medium, binding.armorSetPieceList, false)
            binding.armorSetPieceList.addView(view)
            return
        }

        for (armorPiece in armorPieces) {
            val armorFull = (armorPiece as UserArmorPiece).armor
            val pieceBinding = ListitemArmorsetArmorBinding.inflate(layoutInflater, binding.armorSetPieceList, false)

            pieceBinding.armorIcon.setImageDrawable(AssetLoader.loadIconFor(armorFull.armor))
            pieceBinding.armorName.text = armorFull.armor.name
            pieceBinding.rarityString.text = getString(R.string.format_rarity, armorFull.armor.rarity)
            pieceBinding.defenseValue.text = resources.getString(
                    R.string.armor_defense_value,
                    armorFull.armor.defense_base,
                    armorFull.armor.defense_max,
                    armorFull.armor.defense_augment_max)
            val slotImages = armorFull.armor.slots.map {
                pieceBinding.root.context.getDrawableCompat(SlotEmptyRegistry(it))
            }

            pieceBinding.slot1.setImageDrawable(slotImages[0])
            pieceBinding.slot2.setImageDrawable(slotImages[1])
            pieceBinding.slot3.setImageDrawable(slotImages[2])

            pieceBinding.root.setOnClickListener {
                getRouter().navigateArmorDetail(armorFull.armor.id)
            }

            binding.armorSetPieceList.addView(pieceBinding.root)
        }
    }

    private fun populateCharm(userCharms: List<UserEquipment>) {
        if (userCharms.isNullOrEmpty()) return // Do nothing

        val charm = (userCharms.first() as UserCharm).charm
        val charmBinding = ListitemArmorsetArmorBinding.inflate(layoutInflater, binding.armorSetPieceList, false)

        charmBinding.armorName.text = charm.charm.name
        charmBinding.armorIcon.setImageDrawable(AssetLoader.loadIconFor(charm.charm))

        charmBinding.iconDefense.visibility = View.INVISIBLE
        charmBinding.defenseValue.visibility = View.INVISIBLE
        charmBinding.iconSlots.visibility = View.INVISIBLE
        charmBinding.slot1.visibility = View.INVISIBLE
        charmBinding.slot2.visibility = View.INVISIBLE
        charmBinding.slot3.visibility = View.INVISIBLE

        charmBinding.root.setOnClickListener {
            getRouter().navigateCharmDetail(charm.charm.id)
        }

        binding.armorSetPieceList.addView(charmBinding.root)
    }

    private fun populateTools(tools: List<UserEquipment>) {
        //Append to the bottom of the armor piece list, so do nothing if we have none
        if (tools.isNullOrEmpty()) return

        for (tool in tools) {
            tool as UserTool
            val toolBinding = ListitemArmorsetArmorBinding.inflate(layoutInflater, binding.armorSetPieceList, false)
            toolBinding.armorIcon.setImageDrawable(AssetLoader.loadIconFor(tool.tool))
            toolBinding.armorName.text = tool.tool.name
            toolBinding.rarityString.text = when (tool.tool.tool_type) {
                ToolType.MANTLE -> getString(R.string.tool_mantle)
                ToolType.BOOSTER -> getString(R.string.tool_booster)
            }
            toolBinding.iconDefense.visibility = View.INVISIBLE
            toolBinding.defenseValue.visibility = View.INVISIBLE

            val slotImages = tool.tool.slots.map {
                toolBinding.root.context.getDrawableCompat(SlotEmptyRegistry(it))
            }

            toolBinding.slot1.setImageDrawable(slotImages[0])
            toolBinding.slot2.setImageDrawable(slotImages[1])
            toolBinding.slot3.setImageDrawable(slotImages[2])

            toolBinding.root.setOnClickListener {
                getRouter().navigateToolDetail(tool.tool.id)
            }

            binding.armorSetPieceList.addView(toolBinding.root)
        }
    }

    private fun populateArmorSkills(skills: List<SkillLevel>) {
        if (skills.isNullOrEmpty()) {
            val view = layoutInflater.inflate(R.layout.listitem_empty_medium, binding.armorSetSkillList, false)
            binding.armorSetSkillList.addView(view)
            return
        }

        for (skill in skills) {
            //Set the label for the Set name
            val skillBinding = ListitemSkillLevelBinding.inflate(layoutInflater, binding.armorSetSkillList, false)
            skillBinding.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            skillBinding.labelText.text = skill.skillTree.name
            skillBinding.levelText.text = getString(R.string.level_qty, skill.level)
            with(skillBinding.skillLevel) {
                maxLevel = skill.skillTree.max_level
                secretLevels = skill.skillTree.secret
                level = skill.level
            }

            skillBinding.root.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            binding.armorSetSkillList.addView(skillBinding.root)
        }
    }

    private fun populateArmorSetBonuses(setBonuses: Map<String, List<ArmorSetBonus>>) {
        if (setBonuses.isEmpty()) {
            val view = layoutInflater.inflate(R.layout.listitem_empty_no_margin, binding.armorSetSetBonusList, false)
            binding.armorSetSetBonusList.addView(view)
            return
        }

        setBonuses.forEach {
            populateArmorSetBonusName(it.key)
            populateArmorSetBonuses(it.value)
        }
    }

    private fun populateArmorSetBonusName(setBonusName: String) {
        val textView = TextView(context)
        textView.text = setBonusName
        textView.setTextAppearance(context, R.style.TextHeadlineHigh)
        binding.armorSetSetBonusList.addView(textView)
    }

    private fun populateArmorSetBonuses(setBonuses: List<ArmorSetBonus>) {
        for (setBonus in setBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = ListitemArmorsetBonusBinding.inflate(layoutInflater, binding.armorSetSetBonusList, false)

            listItem.bonusSkillIcon.setImageDrawable(skillIcon)
            listItem.bonusSkillName.text = setBonus.skillTree.name
            listItem.bonusRequirement.setImageResource(reqIcon)

            listItem.root.setOnClickListener {
                getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            binding.armorSetSetBonusList.addView(listItem.root)
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
