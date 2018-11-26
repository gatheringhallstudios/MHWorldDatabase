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
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader.loadNoteFromChar
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.ghstudios.android.ui.general.SharpnessView
import kotlinx.android.synthetic.main.fragment_weapon_summary.*
import kotlinx.android.synthetic.main.listitem_bow_detail.*
import kotlinx.android.synthetic.main.listitem_bowgun_ammo.view.*
import kotlinx.android.synthetic.main.listitem_bowgun_detail.*
import kotlinx.android.synthetic.main.listitem_hunting_horn_detail.*
import kotlinx.android.synthetic.main.listitem_hunting_horn_detail.view.*
import kotlinx.android.synthetic.main.listitem_hunting_horn_melody.*
import kotlinx.android.synthetic.main.listitem_hunting_horn_melody.view.*
import kotlinx.android.synthetic.main.listitem_section_header.view.*

class WeaponDetailFragment : Fragment() {

    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: WeaponDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(WeaponDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        populateWeaponSpecificSection(weaponData.weapon, weaponData.ammo, weaponData.melodies)
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
    private fun populateWeaponSpecificSection(weapon: Weapon, ammo: WeaponAmmoData?, melodies: List<WeaponMelody>?) {
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
                bindHuntingHorn(weapon, melodies, view)
            }
            WeaponType.BOW -> {
                weapon_specific_section.layoutResource = R.layout.listitem_bow_detail
                val view = weapon_specific_section.inflate()
                bindBow(weapon, view)
            }
            WeaponType.HEAVY_BOWGUN, WeaponType.LIGHT_BOWGUN -> {
                weapon_specific_section.layoutResource = R.layout.listitem_bowgun_detail
                val view = weapon_specific_section.inflate()
                bindBowgun(ammo, view)
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
        view.findViewById<TextView>(R.id.kinsect_bonus_value).text = when (weapon.kinsect_bonus) {
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
        view.findViewById<TextView>(R.id.shelling_type).text = when (weapon.shelling) {
            ShellingType.NONE -> ""
            ShellingType.NORMAL -> getString(R.string.weapon_gunlance_shelling_normal)
            ShellingType.WIDE -> getString(R.string.weapon_gunlance_shelling_wide)
            ShellingType.LONG -> getString(R.string.weapon_gunlance_shelling_long)
        }
        view.findViewById<TextView>(R.id.shelling_level_value).text = getString(R.string.skill_level_short_qty, weapon.shelling_level)
    }

    private fun bindHuntingHorn(weapon: Weapon, melodies: List<WeaponMelody>?, view: View) {
        populateSharpness(weapon.sharpnessData, view)
        notes_layout.removeAllViews()

        weapon.notes!!.forEachIndexed { index, note ->
            val noteIcon = ImageView(context)
            noteIcon.layoutParams = ViewGroup.LayoutParams(resources.getDimension(R.dimen.image_size_small).toInt(), resources.getDimension(R.dimen.image_size_small).toInt())
            noteIcon.setImageDrawable(loadNoteFromChar(note, index ))
            notes_layout.addView(noteIcon)
        }

        view.melody_layout.removeAllViews()
        melodies?.forEach {
            val melodyView = layoutInflater.inflate(R.layout.listitem_hunting_horn_melody, melody_layout, false )
            it.notes.forEachIndexed { index, it ->
                val noteIcon = when(index) {
                    0 -> melodyView.note1_icon
                    1 -> melodyView.note2_icon
                    2 -> melodyView.note3_icon
                    3 -> melodyView.note4_icon
                    else -> null
                }

                noteIcon!!.setImageDrawable(loadNoteFromChar(it, weapon.notes.indexOf(it)))
            }

            melodyView.effect1.text = it.effect1
            melodyView.effect2.text = it.effect2
            melodyView.duration_value.text = it.duration
            melodyView.extension_value.text = it.extension
            melody_layout.addView(melodyView)
        }
    }

    private fun bindBow(weapon: Weapon, view: View) {
        weapon.weaponCoatings?.iterator()?.forEach {
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

    private fun bindBowgun(ammo: WeaponAmmoData?, view: View) {
        if (ammo == null) return

        deviation_value.text = ammo.deviation
        special_ammo_value.text = ammo.special_ammo

        ammo.iterator().forEach {
            val view = layoutInflater.inflate(R.layout.listitem_bowgun_ammo, ammo_layout, false)
            view.ammo_type_name.text = when (it.type) {
                AmmoType.NORMAL_AMMO1 -> getString(R.string.weapon_bowgun_ammo_normal, 1)
                AmmoType.NORMAL_AMMO2 -> getString(R.string.weapon_bowgun_ammo_normal, 2)
                AmmoType.NORMAL_AMMO3 -> getString(R.string.weapon_bowgun_ammo_normal, 3)
                AmmoType.PIERCE_AMMO1 -> getString(R.string.weapon_bowgun_ammo_pierce, 1)
                AmmoType.PIERCE_AMMO2 -> getString(R.string.weapon_bowgun_ammo_pierce, 2)
                AmmoType.PIERCE_AMMO3 -> getString(R.string.weapon_bowgun_ammo_pierce, 3)
                AmmoType.SPREAD_AMMO1 -> getString(R.string.weapon_bowgun_ammo_spread, 1)
                AmmoType.SPREAD_AMMO2 -> getString(R.string.weapon_bowgun_ammo_spread, 2)
                AmmoType.SPREAD_AMMO3 -> getString(R.string.weapon_bowgun_ammo_spread, 3)
                AmmoType.STICKY_AMMO1 -> getString(R.string.weapon_bowgun_ammo_sticky, 1)
                AmmoType.STICKY_AMMO2 -> getString(R.string.weapon_bowgun_ammo_sticky, 2)
                AmmoType.STICKY_AMMO3 -> getString(R.string.weapon_bowgun_ammo_sticky, 3)
                AmmoType.CLUSTER_AMMO1 -> getString(R.string.weapon_bowgun_ammo_cluster, 1)
                AmmoType.CLUSTER_AMMO2 -> getString(R.string.weapon_bowgun_ammo_cluster, 2)
                AmmoType.CLUSTER_AMMO3 -> getString(R.string.weapon_bowgun_ammo_cluster, 3)
                AmmoType.RECOVER_AMMO1 -> getString(R.string.weapon_bowgun_ammo_recover, 1)
                AmmoType.RECOVER_AMMO2 -> getString(R.string.weapon_bowgun_ammo_recover, 2)
                AmmoType.POISON_AMMO1 -> getString(R.string.weapon_bowgun_ammo_poison, 1)
                AmmoType.POISON_AMMO2 -> getString(R.string.weapon_bowgun_ammo_poison, 2)
                AmmoType.PARALYSIS_AMMO1 -> getString(R.string.weapon_bowgun_ammo_paralysis, 1)
                AmmoType.PARALYSIS_AMMO2 -> getString(R.string.weapon_bowgun_ammo_paralysis, 2)
                AmmoType.SLEEP_AMMO1 -> getString(R.string.weapon_bowgun_ammo_sleep, 1)
                AmmoType.SLEEP_AMMO2 -> getString(R.string.weapon_bowgun_ammo_sleep, 2)
                AmmoType.EXHAUST_AMMO1 -> getString(R.string.weapon_bowgun_ammo_sleep, 1)
                AmmoType.EXHAUST_AMMO2 -> getString(R.string.weapon_bowgun_ammo_sleep, 2)
                AmmoType.FLAMING_AMMO -> getString(R.string.weapon_bowgun_ammo_flaming)
                AmmoType.WATER_AMMO -> getString(R.string.weapon_bowgun_ammo_water)
                AmmoType.FREEZE_AMMO -> getString(R.string.weapon_bowgun_ammo_freeze)
                AmmoType.THUNDER_AMMO -> getString(R.string.weapon_bowgun_ammo_thunder)
                AmmoType.DRAGON_AMMO -> getString(R.string.weapon_bowgun_ammo_dragon)
                AmmoType.SLICING_AMMO -> getString(R.string.weapon_bowgun_ammo_slicing)
                AmmoType.WYVERN_AMMO -> getString(R.string.weapon_bowgun_ammo_wyvern)
                AmmoType.DEMON_AMMO -> getString(R.string.weapon_bowgun_ammo_demon)
                AmmoType.ARMOR_AMMO -> getString(R.string.weapon_bowgun_ammo_armor)
                AmmoType.TRANQ_AMMO -> getString(R.string.weapon_bowgun_ammo_tranq)
            }
            view.capacity_value.text = it.capacity.toString()

            //Determining what kind of shot it actually is, is a combination of ammo type, rapid/normal,
            //And reload speed due to game logic. I know this looks like it makes 0 sense
            if (it.type == AmmoType.WYVERN_AMMO) {
                view.shot_type_value.text = getString(R.string.weapon_bowgun_ammo_shot_rapid_fire)
            } else if (it.isRapid == true) {
                view.shot_type_value.text = getString(R.string.weapon_bowgun_ammo_shot_rapid_fire)
            } else if (it.recoil == -1) {
                view.shot_type_value.text = getString(R.string.weapon_bowgun_ammo_shot_auto_reload)
            } else if (it.isRapid == false && it.recoil != -1) {
                view.shot_type_value.text = getString(R.string.weapon_bowgun_ammo_shot_normal)
            } else {
                view.shot_type_value.text = getString(R.string.general_none)
            }

            view.reload_value.text = when (it.reload) {
                ReloadType.NONE -> getString(R.string.general_none)
                ReloadType.VERY_SLOW -> getString(R.string.weapon_bowgun_ammo_reload_very_slow)
                ReloadType.SLOW -> getString(R.string.weapon_bowgun_ammo_reload_slow)
                ReloadType.NORMAL -> getString(R.string.weapon_bowgun_ammo_reload_normal)
                ReloadType.FAST -> getString(R.string.weapon_bowgun_ammo_reload_fast)
                ReloadType.VERY_FAST -> getString(R.string.weapon_bowgun_ammo_reload_very_fast)
            }

            view.recoil_value.text = if (it.recoil < 0) "-" else getString(R.string.format_plus, it.recoil)

            view.ammo_icon.setImageDrawable(loadIconFor(it.type))
            ammo_layout.addView(view)
        }
    }
}