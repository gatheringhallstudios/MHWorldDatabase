package com.gatheringhallstudios.mhworlddatabase.features.armor.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemSkillLevelBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetBonusBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetArmorBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentArmorSetSummaryBinding

class ArmorSetDetailFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentArmorSetSummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArmorDetailViewModel by lazy {
        ViewModelProvider(parentFragment!!).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArmorSetSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.armorSet.observe(viewLifecycleOwner, Observer(::populateArmorSet))
    }

    //Populate the stats from the other pieces of the armor set
    private fun populateArmorSet(armorSet: ArmorSetFull) {
        binding.armorSetHeader.setIconType(IconType.ZEMBELLISHED)
        binding.armorSetHeader.setTitleText(armorSet.armorset_name)
        binding.armorSetHeader.setSubtitleText(getString(R.string.format_rarity, armorSet.rarity))
        binding.armorSetHeader.setSubtitleColor(AssetLoader.loadRarityColor(armorSet.rarity))
        binding.armorSetHeader.setIconDrawable(AssetLoader.loadIconFor(armorSet))

        //Populate armor pieces section
        binding.armorSetPieceList.removeAllViews()
        armorSet.armor.forEach { armorFull -> populateArmorSetPieces(armorFull) }

        //Populate set bonuses
        binding.armorSetSetBonusList.removeAllViews()
        populateSetBonuses(armorSet.armor.first().setBonuses)

        //Calculate total skills
        populateArmorSetSkills(armorSet.armor)

        //Calculate total stats
        binding.armorSetFireValue.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.fire }.toString()
        binding.armorSetWaterValue.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.water }.toString()
        binding.armorSetThunderValue.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.thunder }.toString()
        binding.armorSetIceValue.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.ice }.toString()
        binding.armorSetDragonValue.text = armorSet.armor.sumBy { armorFull -> armorFull.armor.dragon }.toString()
        binding.armorSetDefenseValue.text = getString(R.string.armor_defense_value,
                armorSet.armor.sumBy { armorFull -> armorFull.armor.defense_base },
                armorSet.armor.sumBy { armorFull -> armorFull.armor.defense_max },
                armorSet.armor.sumBy { armorFull -> armorFull.armor.defense_augment_max })

        //Calculate total materials
        populateSetComponents(armorSet.armor)
    }

    private fun populateArmorSetPieces(armorFull: ArmorFull) {
        val pieceBinding = ListitemArmorsetArmorBinding.inflate(
                layoutInflater, binding.armorSetPieceList, false)

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

    private fun populateSetBonuses(armorSetBonuses: List<ArmorSetBonus>) {
        if (armorSetBonuses.isEmpty()) {
            binding.armorSetSetBonusSection.visibility = View.GONE
            return
        }

        // show set bonus section
        binding.armorSetSetBonusSection.visibility = View.VISIBLE
        binding.armorSetSetBonusList.removeAllViews()

        //Set the label for the Set name
        binding.armorSetSetBonusName.text = armorSetBonuses.first().name

        //Now to set the actual skills
        for (setBonus in armorSetBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = ListitemArmorsetBonusBinding.inflate(layoutInflater)

            listItem.bonusSkillIcon.setImageDrawable(skillIcon)
            listItem.bonusSkillName.text = setBonus.skillTree.name
            listItem.bonusRequirement.setImageResource(reqIcon)

            listItem.root.setOnClickListener {
                getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            binding.armorSetSetBonusList.addView(listItem.root)
        }
    }

    private fun populateSetComponents(armorSet: List<ArmorFull>) {
        //The armor set components need to be "added up" to create a cumulative list of components
        //Incoming item quantities are added to a map. If there is a repeated key, the existing ItemQuantity
        //object is replaced with an updated one because the models are immutable.
        val components = mutableMapOf<Int, ItemQuantity>()
        binding.armorSetComponentsList.removeAllViews()

        armorSet.forEach { armorFull ->
            armorFull.recipe.forEach { itemQuantity ->
                if (components.containsKey(itemQuantity.item.id)) {
                    val quantity = components[itemQuantity.item.id]!!.quantity + itemQuantity.quantity
                    components[itemQuantity.item.id] = ItemQuantity(itemQuantity.item, quantity, null)
                } else {
                    components[itemQuantity.item.id] = itemQuantity
                }
            }
        }

        //Iterate over the cumulative map instead of the individual ArmorFull objects
        components.forEach {
            val component = it.value
            val view = IconLabelTextCell(context)
            val icon = AssetLoader.loadIconFor(component.item)

            view.setLeftIconDrawable(icon)
            view.setLabelText(component.item.name)
            view.setValueText(getString(R.string.format_quantity_none, component.quantity))
            view.setOnClickListener {
                getRouter().navigateItemDetail(component.item.id)
            }

            binding.armorSetComponentsList.addView(view)
        }
    }

    private fun populateArmorSetSkills(armorSet: List<ArmorFull>) {
        //The armor skill levels need to be "added up" to create a cumulative list of skills
        //Incoming skill levels are added to a map. If there is a repeated key, the existing SkillLevel
        //object is replaced with an updated one because the models are immutable.
        val skills = mutableMapOf<Int, SkillLevel>()
        binding.armorSetSkillList.removeAllViews()

        armorSet.forEach { armorFull ->
            armorFull.skills.forEach { skill ->
                if (skills.containsKey(skill.skillTree.id)) {
                    val level = skills[skill.skillTree.id]!!.level + skill.level
                    val skillLevel = SkillLevel(level)
                    skillLevel.skillTree = skill.skillTree
                    skills[skill.skillTree.id] = skillLevel
                } else {
                    skills[skill.skillTree.id] = skill
                }
            }
        }

        //Iterate over the cumulative map instead of the individual ArmorFull objects
        skills.forEach {
            val skill = it.value
            //Set the label for the Set name
            val skillBinding = ListitemSkillLevelBinding.inflate(
                    layoutInflater, binding.armorSetSkillList, false)

            skillBinding.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            skillBinding.labelText.text = skill.skillTree.name
            skillBinding.levelText.text = getString(R.string.level_qty, skill.level)
            with(skillBinding.skillLevel) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            skillBinding.root.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            binding.armorSetSkillList.addView(skillBinding.root)
        }
    }
}
