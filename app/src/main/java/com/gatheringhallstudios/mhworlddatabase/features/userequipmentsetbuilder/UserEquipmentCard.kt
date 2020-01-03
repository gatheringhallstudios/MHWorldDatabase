package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SetBonusNumberRegistry
import com.gatheringhallstudios.mhworlddatabase.components.ExpandableCardView
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_skill_description.view.level_text
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.view_base_body_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_base_header_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.*
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.equipment_icon
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.equipment_name
import kotlinx.android.synthetic.main.view_weapon_header_expandable_cardview.view.rarity_string

class UserEquipmentCard(val card: ExpandableCardView) {
    private fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return card.resources.getString(resId, *formatArgs)
    }

    fun bindWeapon(userWeapon: UserWeapon) {
        val weapon = userWeapon.weapon.weapon
        with (card) {
            setHeader(R.layout.view_weapon_header_expandable_cardview)
            setBody(R.layout.view_base_body_expandable_cardview)

            equipment_name.text = weapon.name
            equipment_icon.setImageDrawable(AssetLoader.loadIconFor(weapon))
            rarity_string.setTextColor(AssetLoader.loadRarityColor(weapon.rarity))
            rarity_string.text = getString(R.string.format_rarity, weapon.rarity)
            rarity_string.visibility = View.VISIBLE
            attack_value.text = weapon.attack.toString()
        }
    }

    fun bindArmor(userArmor: UserArmorPiece) {
        val armor = userArmor.armor

        with(card) {
            setHeader(R.layout.view_base_header_expandable_cardview)
            setBody(R.layout.view_base_body_expandable_cardview)

            equipment_name.text = armor.armor.name
            rarity_string.text = getString(R.string.format_rarity, armor.armor.rarity)
            rarity_string.setTextColor(AssetLoader.loadRarityColor(armor.armor.rarity))
            rarity_string.visibility = View.VISIBLE
            equipment_icon.setImageDrawable(AssetLoader.loadIconFor(armor.armor))
            defense_value.text = getString(
                    R.string.armor_defense_value,
                    armor.armor.defense_base,
                    armor.armor.defense_max,
                    armor.armor.defense_augment_max)
        }
    }
    
    fun bindCharm(userCharm: UserCharm) {
        with (card) {
            setHeader(R.layout.view_base_header_expandable_cardview)
            setBody(R.layout.view_base_body_expandable_cardview)

            equipment_name.text = userCharm.charm.charm.name
            equipment_icon.setImageDrawable(AssetLoader.loadIconFor(userCharm.charm.charm))
            rarity_string.text = getString(R.string.format_rarity, userCharm.charm.charm.rarity)
            rarity_string.setTextColor(AssetLoader.loadRarityColor(userCharm.charm.charm.rarity))
            rarity_string.visibility = View.VISIBLE

            defense_value.visibility = View.GONE
            icon_defense.visibility = View.GONE
        }
    }
    
    fun bindDecoration(userDecoration: UserDecoration) {
        val decoration = userDecoration.decoration

        with (card) {
            setHeader(R.layout.view_base_header_expandable_cardview)
            setBody(R.layout.view_base_body_expandable_cardview)

            equipment_name.text = decoration.name
            rarity_string.text = getString(R.string.format_rarity, decoration.rarity)
            rarity_string.setTextColor(AssetLoader.loadRarityColor(decoration.rarity))
            rarity_string.visibility = View.VISIBLE
            equipment_icon.setImageDrawable(AssetLoader.loadIconFor(decoration))
            defense_value.visibility = View.GONE
            icon_defense.visibility = View.GONE
        }
    }


    fun populateSkills(skills: List<SkillLevel>) {
        val skillLayout = card.skill_section
        if (skills.isEmpty()) {
            skillLayout.visibility = View.GONE
            return
        }

        skillLayout.visibility = View.VISIBLE
        skillLayout.skill_list.removeAllViews()

        val inflater = LayoutInflater.from(card.context)

        for (skill in skills) {
            //Set the label for the Set name
            val view = inflater.inflate(R.layout.listitem_skill_level, skillLayout.skill_list, false)

            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            view.label_text.text = skill.skillTree.name
            view.level_text.text = getString(R.string.skill_level_qty, skill.level)
            with(view.skill_level) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            view.setOnClickListener {
                card.getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            skillLayout.skill_list.addView(view)
        }
    }

    fun populateSetBonuses(armorSetBonuses: List<ArmorSetBonus>) {
        val setBonusSection = card.set_bonus_section

        if (armorSetBonuses.isEmpty()) {
            setBonusSection.visibility = View.GONE
            return
        }

        // show set bonus section
        setBonusSection.visibility = View.VISIBLE
        setBonusSection.set_bonus_list.removeAllViews()

        val inflater = LayoutInflater.from(card.context)

        //Now to set the actual skills
        for (setBonus in armorSetBonuses) {
            val skillIcon = AssetLoader.loadIconFor(setBonus.skillTree)
            val reqIcon = SetBonusNumberRegistry(setBonus.required)
            val listItem = inflater.inflate(R.layout.listitem_armorset_bonus, null, false)

            listItem.bonus_skill_icon.setImageDrawable(skillIcon)
            listItem.bonus_skill_name.text = setBonus.skillTree.name
            listItem.bonus_requirement.setImageResource(reqIcon)

            listItem.setOnClickListener {
                card.getRouter().navigateSkillDetail(setBonus.skillTree.id)
            }

            setBonusSection.set_bonus_list.addView(listItem)
        }
    }
}