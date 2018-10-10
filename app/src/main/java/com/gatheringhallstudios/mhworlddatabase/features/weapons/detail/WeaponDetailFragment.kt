package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader.loadIconFor
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponFull
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponSharpness
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.ghstudios.android.ui.general.SharpnessView
import kotlinx.android.synthetic.main.fragment_weapon_summary.*
import kotlinx.android.synthetic.main.listitem_bow_detail.*
import kotlinx.android.synthetic.main.listitem_section_header.view.*

class WeaponDetailFragment : Fragment() {
    companion object {
        const val ARG_WEAPON_ID = "WEAPON"
    }

    private val viewModel: WeaponDetailViewModel by lazy {
        ViewModelProviders.of(this).get(WeaponDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val weaponId = arguments?.getInt(ARG_WEAPON_ID) ?: -1
        viewModel.loadWeapon(weaponId)
        return inflater.inflate(R.layout.fragment_weapon_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.weapon.observe(this, Observer(::populateWeapon))
    }

    private fun populateWeapon(weaponData: WeaponFull?) {
        if (weaponData == null) return

        setActivityTitle(weaponData.weapon.name)
        populateWeaponBasic(weaponData.weapon)
        populateComponents(weaponData.recipe)
        populateWeaponSpecificSection(weaponData.weapon)
    }

    private fun populateWeaponBasic(weapon: Weapon) {
        // Set header info
        weapon_header.setIconType(IconType.ZEMBELLISHED)
        weapon_header.setIconDrawable(AssetLoader.loadIconFor(weapon))
        weapon_header.setTitleText(weapon.name)
        weapon_header.setSubtitleText(getString(R.string.format_rarity, weapon.rarity))
        weapon_header.setSubtitleColor(AssetLoader.loadRarityColor(weapon.rarity))

        attack_value.text = weapon.attack.toString()
        affinity_value.text = weapon.affinity.toString()
        elderseal_value.text = when (weapon.elderseal) {
            ElderSealLevel.NONE -> getString(R.string.weapon_elderseal_none)
            ElderSealLevel.LOW -> getString(R.string.weapon_elderseal_low)
            ElderSealLevel.AVERAGE -> getString(R.string.weapon_elderseal_average)
            ElderSealLevel.HIGH -> getString(R.string.weapon_elderseal_high)
        }

        // Set elemental attack values
        element_value.text = createElementString(weapon.element1_attack, weapon.element_hidden)
        element_icon.setImageDrawable(getElementIcon(weapon.element1))
        element_type_value.text = weapon.element1 //TODO: This element string needs to be localized in the DB
        if (weapon.element_hidden) {
            element_layout.alpha = 0.5.toFloat()
        } else {
            element_layout.alpha = 1.0.toFloat()
        }

        //Slot information
        val slotImages = weapon.slots.map {
            context?.getDrawableCompat(SlotEmptyRegistry(it))
        }
        slot1.setImageDrawable(slotImages[0])
        slot2.setImageDrawable(slotImages[1])
        slot3.setImageDrawable(slotImages[2])

        defence_value.text = weapon.defense.toString()
    }

    private fun createElementString(element1_attack: Int?, element_hidden: Boolean): String {
        val workString = element1_attack ?: "-----"

        return when (element_hidden) {
            true -> "(${workString})"
            false -> workString.toString()
        }
    }

    private fun getElementIcon(element: String?): Drawable? {
        return when (element) {
            "Fire" -> context!!.getDrawableCompat(R.drawable.ic_element_fire)
            "Dragon" -> context!!.getDrawableCompat(R.drawable.ic_element_dragon)
            "Poison" -> context!!.getDrawableCompat(R.drawable.ic_status_poison)
            "Water" -> context!!.getDrawableCompat(R.drawable.ic_element_water)
            "Thunder" -> context!!.getDrawableCompat(R.drawable.ic_element_thunder)
            "Ice" -> context!!.getDrawableCompat(R.drawable.ic_element_ice)
            "Blast" -> context!!.getDrawableCompat(R.drawable.ic_status_blast)
            "Paralysis" -> context!!.getDrawableCompat(R.drawable.ic_status_paralysis)
            "Sleep" -> context!!.getDrawableCompat(R.drawable.ic_status_sleep)
            else -> context!!.getDrawableCompat(R.drawable.ic_ui_slot_none)
        }
    }

    /** Method for binding sections that are specific to certain weapons **/
    private fun populateWeaponSpecificSection(weapon: Weapon) {
        when (weapon.weapon_type) {
            WeaponType.SWITCH_AXE, WeaponType.CHARGE_BLADE -> {
                weapon_specific_section.layoutResource = R.layout.listitem_charge_blade_detail
                val view = weapon_specific_section.inflate()
                bindChargeBladeSwitchAxe(weapon, view)
            }
            WeaponType.INSECT_GLAIVE -> {
                weapon_specific_section.layoutResource = R.layout.listitem_insect_glaive_detail
                val view = weapon_specific_section.inflate()
                bindInsectGlaive(weapon, view)
            }
            WeaponType.GUNLANCE -> {
                weapon_specific_section.layoutResource = R.layout.listitem_gunlance_detail
                val view = weapon_specific_section.inflate()
                bindGunlance(weapon, view)
            }
            WeaponType.HUNTING_HORN -> {
                weapon_specific_section.layoutResource = R.layout.listitem_hunting_horn_detail
                val view = weapon_specific_section.inflate()
                bindHuntingHorn(weapon, view)
            }
            WeaponType.BOW -> {
                weapon_specific_section.layoutResource = R.layout.listitem_bow_detail
                val view = weapon_specific_section.inflate()
                bindBow(weapon, view)
            }
            else -> {
                weapon_specific_section.layoutResource = R.layout.listitem_generic_weapon_detail
                val view = weapon_specific_section.inflate()
                bindGenericWeapon(weapon, view)
            }
        }
    }

    /**
     * Converts the map of the different recipe types (craft, upgrade, etc.) into section headers and lists
     */
    private fun populateComponents(recipes: Map<String?, List<ItemQuantity>>) {
        weapon_components_section.removeAllViews()

        if (recipes.isEmpty()) {
            weapon_components_section.visibility = View.GONE
            return
        }

        weapon_components_section.visibility = View.VISIBLE

        for (recipe in recipes) {
            val sectionHeader = layoutInflater.inflate(R.layout.listitem_section_header, weapon_components_section, false)
            sectionHeader.label_text.text = when (recipe.key) {
                "Upgrade" -> getString(R.string.header_required_materials_upgrade)
                "Create" -> getString(R.string.header_required_materials_craft)
                else -> "Other"
            }

            weapon_components_section.addView(sectionHeader)

            for (component in recipe.value) {
                val view = IconLabelTextCell(context)
                val icon = AssetLoader.loadIconFor(component.item)

                view.setLeftIconDrawable(icon)
                view.setLabelText(component.item.name)
                view.setValueText(getString(R.string.format_quantity_none, component.quantity))
                view.setOnClickListener {
                    getRouter().navigateItemDetail(component.item.id)
                }
                weapon_components_section.addView(view)
            }
        }
    }

    /**
     * Draws 5 sharpness bars for weapons affected by the handicraft skill, 1 bar for weapons that aren't
     * and hides the sharpness section for weapons that do not have sharpness
     */
    private fun populateSharpness(sharpness: WeaponSharpness?, view: View) {
        if (sharpness != null) {
            //One bar for weapons not affected by sharpness
            if (sharpness.sharpness_maxed!!) {
                view.findViewById<SharpnessView>(R.id.sharpness_value).drawSharpness(sharpness.get(0))
                view.findViewById<LinearLayout>(R.id.sharpness_container1).visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.sharpness_container2).visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.sharpness_container3).visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.sharpness_container4).visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.sharpness_container5).visibility = View.GONE

            } else {
                view.findViewById<TextView>(R.id.sharpness_label).text = getString(R.string.format_plus, 0)
                view.findViewById<SharpnessView>(R.id.sharpness_value).drawSharpness(sharpness.get(0))

                view.findViewById<TextView>(R.id.sharpness_label1).text = getString(R.string.format_plus, 1)
                view.findViewById<SharpnessView>(R.id.sharpness_value1).drawSharpness(sharpness.get(1))

                view.findViewById<TextView>(R.id.sharpness_label2).text = getString(R.string.format_plus, 2)
                view.findViewById<SharpnessView>(R.id.sharpness_value2).drawSharpness(sharpness.get(2))

                view.findViewById<TextView>(R.id.sharpness_label3).text = getString(R.string.format_plus, 3)
                view.findViewById<SharpnessView>(R.id.sharpness_value3).drawSharpness(sharpness.get(3))

                view.findViewById<TextView>(R.id.sharpness_label4).text = getString(R.string.format_plus, 4)
                view.findViewById<SharpnessView>(R.id.sharpness_value4).drawSharpness(sharpness.get(4))

                view.findViewById<TextView>(R.id.sharpness_label5).text = getString(R.string.format_plus, 5)
                view.findViewById<SharpnessView>(R.id.sharpness_value5).drawSharpness(sharpness.get(5))
            }
        } else {
            hideSharpness(view)
        }
    }

    private fun hideSharpness(view: View) {
        view.findViewById<LinearLayout>(R.id.sharpness_container).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.sharpness_container1).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.sharpness_container2).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.sharpness_container3).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.sharpness_container4).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.sharpness_container5).visibility = View.GONE
    }

    private fun bindChargeBladeSwitchAxe(weapon: Weapon, view: View) {
        //Draw sharpness bar/hide it for weapons that don't have it
        populateSharpness(weapon.sharpnessData, view)

        view.findViewById<ImageView>(R.id.phial_element_icon).setImageDrawable(getElementIcon(weapon.phial.toString()))
        view.findViewById<TextView>(R.id.phial_type_value).text = when (weapon.phial) {
            PhialType.NONE -> ""
            PhialType.EXHAUST -> getString(R.string.weapon_charge_blade_exhaust)
            PhialType.POWER -> getString(R.string.weapon_charge_blade_power)
            PhialType.DRAGON -> getString(R.string.weapon_charge_blade_dragon)
            PhialType.POWER_ELEMENT -> getString(R.string.weapon_charge_blade_power_element)
            PhialType.PARALYSIS -> getString(R.string.weapon_charge_blade_paralysis)
            PhialType.IMPACT -> getString(R.string.weapon_charge_blade_impact)
        }
        view.findViewById<TextView>(R.id.phial_power_value).text = weapon.phial_power.toString()
    }

    private fun bindInsectGlaive(weapon: Weapon, view: View) {
        populateSharpness(weapon.sharpnessData, view)
        view.findViewById<TextView>(R.id.kinsect_bonus_value).text = when(weapon.kinsect_bonus) {
            KinsectBonus.NONE -> ""
            KinsectBonus.SEVER -> getString(R.string.weapon_kinsect_bonus_sever)
            KinsectBonus.SPEED -> getString(R.string.weapon_kinsect_bonus_speed)
            KinsectBonus.ELEMENT -> getString(R.string.weapon_kinsect_bonus_element)
            KinsectBonus.HEALTH -> getString(R.string.weapon_kinsect_bonus_health)
            KinsectBonus.STAMINA -> getString(R.string.weapon_kinsect_bonus_stamina)
            KinsectBonus.BLUNT -> getString(R.string.weapon_kinsect_bonus_blunt)
        }
    }

    private fun bindGunlance(weapon: Weapon, view: View) {
        populateSharpness(weapon.sharpnessData, view)
        view.findViewById<TextView>(R.id.shelling_type).text = when(weapon.shelling) {
            ShellingType.NONE -> ""
            ShellingType.NORMAL -> getString(R.string.weapon_gunlance_shelling_normal)
            ShellingType.WIDE -> getString(R.string.weapon_gunlance_shelling_wide)
            ShellingType.LONG -> getString(R.string.weapon_gunlance_shelling_long)
        }
        view.findViewById<TextView>(R.id.shelling_level_value).text = getString(R.string.skill_level_short_qty, weapon.shelling_level)
    }

    private fun bindHuntingHorn(weapon: Weapon, view: View) {
        populateSharpness(weapon.sharpnessData, view)
        view.findViewById<TextView>(R.id.notes_value).text = weapon.notes.toString()
    }

    private fun bindBow(weapon: Weapon, view: View) {
        weapon.weaponCoatings.iterator().forEach {
            when (it) {
                CoatingType.BLAST -> blast_coating_icon.setImageDrawable(loadIconFor(CoatingType.BLAST))
                CoatingType.POWER -> power_coating_icon.setImageDrawable(loadIconFor(CoatingType.POWER))
                CoatingType.POISON -> poison_coating_icon.setImageDrawable(loadIconFor(CoatingType.POISON))
                CoatingType.PARALYSIS -> paralysis_coating_icon.setImageDrawable(loadIconFor(CoatingType.PARALYSIS))
                CoatingType.SLEEP -> sleep_coating_icon.setImageDrawable(loadIconFor(CoatingType.SLEEP))
                CoatingType.CLOSE_RANGE -> close_range_coating_icon.setImageDrawable(loadIconFor(CoatingType.CLOSE_RANGE))
            }
        }
    }

    private fun bindGenericWeapon(weapon: Weapon, view: View) {
        populateSharpness(weapon.sharpnessData, view)
    }
}