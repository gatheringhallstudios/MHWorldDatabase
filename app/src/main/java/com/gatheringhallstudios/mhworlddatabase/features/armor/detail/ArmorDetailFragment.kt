package com.gatheringhallstudios.mhworlddatabase.features.armor.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.*
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemSkillLevelBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.ListitemArmorsetBonusBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentArmorSummaryBinding

class ArmorDetailFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentArmorSummaryBinding? = null
    private val binding get() = _binding!!


    private val viewModel: ArmorDetailViewModel by lazy {
        ViewModelProvider(requireParentFragment()).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArmorSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.armor.observe(viewLifecycleOwner, Observer(::populateArmor))
    }

    private fun populateArmor(armorData: ArmorFull?) {
        if (armorData == null) return

        populateArmorBasic(armorData.armor)
        populateSkills(armorData.skills)
        populateSetBonuses(armorData.setBonuses)
        populateComponents(armorData.recipe)
    }

    private fun populateArmorBasic(armor: Armor) {
        // Set header info
        binding.armorHeader.setIconType(IconType.ZEMBELLISHED)
        binding.armorHeader.setIconDrawable(AssetLoader.loadIconFor(armor))
        binding.armorHeader.setTitleText(armor.name)
        binding.armorHeader.setSubtitleText(getString(R.string.format_rarity, armor.rarity))
        binding.armorHeader.setSubtitleColor(AssetLoader.loadRarityColor(armor.rarity))

        // set defense label
        binding.defenseValue.text = getString(
                R.string.armor_defense_value,
                armor.defense_base,
                armor.defense_max,
                armor.defense_augment_max)

        // set elemental defense values
        binding.fireValue.text = "${armor.fire}"
        binding.waterValue.text = "${armor.water}"
        binding.thunderValue.text = "${armor.thunder}"
        binding.iceValue.text = "${armor.ice}"
        binding.dragonValue.text = "${armor.dragon}"

        val slotImages = armor.slots.map {
            context?.getDrawableCompat(SlotEmptyRegistry(it))
        }

        binding.slot1.setImageDrawable(slotImages[0])
        binding.slot2.setImageDrawable(slotImages[1])
        binding.slot3.setImageDrawable(slotImages[2])
    }

    private fun populateSkills(skills: List<SkillLevel>) {
        if (skills.isEmpty()) {
            binding.armorSkillSection.visibility = View.GONE
            return
        }

        binding.armorSkillSection.visibility = View.VISIBLE
        binding.armorSkillList.removeAllViews()

        val inflater = LayoutInflater.from(context)

        for (skill in skills) {
            //Set the label for the Set name
            val skillBinding = ListitemSkillLevelBinding.inflate(inflater, binding.armorSkillList, false)

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

            binding.armorSkillList.addView(skillBinding.root)
        }
    }

    private fun populateSetBonuses(armorSetBonuses: List<ArmorSetBonus>) {
        if (armorSetBonuses.isEmpty()) {
            binding.armorSetBonusSection.visibility = View.GONE
            return
        }

        // show set bonus section
        binding.armorSetBonusSection.visibility = View.VISIBLE
        binding.armorSetBonusList.removeAllViews()

        //Set the label for the Set name
        binding.setBonusName.text = armorSetBonuses.first().name

        //Now to set the actual skills
        for (setBonus in armorSetBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = ListitemArmorsetBonusBinding.inflate(layoutInflater, binding.armorSetBonusList, false)

            listItem.bonusSkillIcon.setImageDrawable(skillIcon)
            listItem.bonusSkillName.text = setBonus.skillTree.name
            listItem.bonusRequirement.setImageResource(reqIcon)

            listItem.root.setOnClickListener {
                getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            binding.armorSetBonusList.addView(listItem.root)
        }
    }

    private fun populateComponents(components: List<ItemQuantity>) {
        if (components.isEmpty()) {
            binding.armorComponentsSection.visibility = View.GONE
            return
        }

        binding.armorComponentsSection.visibility = View.VISIBLE
        binding.armorComponentsList.removeAllViews()

        for (itemQuantity in components) {
            val view = IconLabelTextCell(context)
            val icon = AssetLoader.loadIconFor(itemQuantity.item)

            view.setLeftIconDrawable(icon)
            view.setLabelText(itemQuantity.item.name)
            view.setValueText(getString(R.string.format_quantity_none, itemQuantity.quantity))
            view.setOnClickListener {
                getRouter().navigateItemDetail(itemQuantity.item.id)
            }

            binding.armorComponentsList.addView(view)
        }
    }
}
