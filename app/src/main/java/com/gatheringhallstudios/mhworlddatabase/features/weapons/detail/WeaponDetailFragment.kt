package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.R.id.*
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader.loadIconFor
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader.loadNoteFromChar
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.components.SharpnessView
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.*
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_weapon_summary.*
import kotlinx.android.synthetic.main.listitem_bowgun_ammo.view.*
import kotlinx.android.synthetic.main.listitem_hunting_horn_melody.view.*
import kotlinx.android.synthetic.main.listitem_skill_level.view.*
import kotlinx.android.synthetic.main.section_bow_coating.*
import kotlinx.android.synthetic.main.view_bowgun_detail.*
import kotlinx.android.synthetic.main.view_hunting_horn_detail.*
import kotlinx.android.synthetic.main.view_hunting_horn_detail.view.*
import kotlinx.android.synthetic.main.view_weapon_recipe.view.*


/**
 * Fragment used to display the main weapon detail information.
 */
class WeaponDetailFragment : androidx.fragment.app.Fragment() {

    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: WeaponDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(WeaponDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weapon_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.weaponData.observe(this, Observer(::populateWeapon))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val weaponData = viewModel.weaponData.value
        if (weaponData != null && BookmarksFeature.isBookmarked(weaponData)) {
            menu.findItem(action_toggle_bookmark).icon = (context!!.getDrawableCompat(R.drawable.ic_ui_bookmark_on_white))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.weaponData.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    private fun populateWeapon(weaponData: WeaponFull?) {
        if (weaponData == null) return

        //Rerender the menu bar because we are 100% sure we have the weapon data now
        activity!!.invalidateOptionsMenu()

        setActivityTitle(weaponData.weapon.name)
        populateWeaponBasic(weaponData.weapon)
        populateWeaponSkills(weaponData.skills)
        populateComponents(weaponData.recipe)
        populateWeaponSpecificSection(weaponData)
    }

    /**
     * Populates view elements with global weapon information, like name, attack,
     * affinity, defense, rarity, etc.
     */
    private fun populateWeaponBasic(weapon: Weapon) {
        // Set header info
        weapon_header.setIconType(IconType.ZEMBELLISHED)
        weapon_header.setIconDrawable(AssetLoader.loadIconFor(weapon))
        weapon_header.setTitleText(weapon.name)
        weapon_header.setSubtitleText(getString(R.string.format_rarity, weapon.rarity))
        weapon_header.setSubtitleColor(AssetLoader.loadRarityColor(weapon.rarity))

        attack_value.text = weapon.attack.toString()

        // Affinity
        affinity_value.text = getString(when {
            weapon.affinity != 0 -> R.string.format_plus_percentage
            else -> R.string.format_percentage
        }, weapon.affinity)

        // Elderseal
        elderseal_value.text = when (weapon.elderseal) {
            ElderSealLevel.NONE -> getString(R.string.weapon_elderseal_none)
            ElderSealLevel.LOW -> getString(R.string.weapon_elderseal_low)
            ElderSealLevel.AVERAGE -> getString(R.string.weapon_elderseal_average)
            ElderSealLevel.HIGH -> getString(R.string.weapon_elderseal_high)
        }

        // Element
        val elementAttackStr = weapon.element1_attack?.toString()
                ?: getString(R.string.weapon_element_none)
        element_icon.setImageDrawable(AssetLoader.loadElementIcon(weapon.element1))
        element_icon.visibility = when (weapon.element1) {
            null -> View.GONE
            else -> View.VISIBLE
        }
        element_value.text = when (weapon.element_hidden) {
            true -> "($elementAttackStr)"
            false -> elementAttackStr
        }
        element_type_value.text = weapon.element1 //TODO: This element string needs to be localized in the DB
        element_layout.alpha = if (weapon.element_hidden) 0.5F else 1.0F

        //Slot information
        val slotImages = weapon.slots.map {
            context?.getDrawableCompat(SlotEmptyRegistry(it))
        }
        slot1.setImageDrawable(slotImages[0])
        slot2.setImageDrawable(slotImages[1])
        slot3.setImageDrawable(slotImages[2])

        // Defense
        defense_value.text = getString(when {
            weapon.defense != 0 -> R.string.format_plus
            else -> R.string.format_quantity_none
        }, weapon.defense)
    }

    private fun populateWeaponSkills(skills: List<SkillLevel>) {
        if (skills.isEmpty()) {
            skill_section.visibility = View.GONE
            return
        }

        skill_section.visibility = View.VISIBLE
        skill_list.removeAllViews()

        val inflater = LayoutInflater.from(context)

        for (skill in skills) {
            //Set the label for the Set name
            val view = inflater.inflate(R.layout.listitem_skill_level, skill_list, false)

            view.icon.setImageDrawable(AssetLoader.loadIconFor(skill.skillTree))
            view.label_text.text = skill.skillTree.name
            view.level_text.text = getString(R.string.skill_level_qty, skill.level)
            with(view.skill_level) {
                maxLevel = skill.skillTree.max_level
                level = skill.level
            }

            view.setOnClickListener {
                getRouter().navigateSkillDetail(skill.skillTree.id)
            }

            skill_list.addView(view)
        }
    }

    /** Method for binding sections that are specific to certain weapons **/
    private fun populateWeaponSpecificSection(weaponData: WeaponFull) {
        val weapon = weaponData.weapon
        val ammo = weaponData.ammo
        val melodies = weaponData.melodies

        when (weapon.weapon_type) {
            WeaponType.SWITCH_AXE, WeaponType.CHARGE_BLADE -> {
                weapon_specific_section.layoutResource = R.layout.view_blade_phial_detail
                val view = weapon_specific_section.inflate()
                bindChargeBladeSwitchAxe(weapon, view)
            }
            WeaponType.INSECT_GLAIVE -> {
                weapon_specific_section.layoutResource = R.layout.view_insect_glaive_detail
                val view = weapon_specific_section.inflate()
                bindInsectGlaive(weapon, view)
            }
            WeaponType.GUNLANCE -> {
                weapon_specific_section.layoutResource = R.layout.view_gunlance_detail
                val view = weapon_specific_section.inflate()
                bindGunlance(weapon, view)
            }
            WeaponType.HUNTING_HORN -> {
                weapon_specific_section.layoutResource = R.layout.view_hunting_horn_detail
                val view = weapon_specific_section.inflate()
                bindHuntingHorn(weapon, melodies, view)
            }
            WeaponType.BOW -> {
                weapon_specific_section.layoutResource = R.layout.section_bow_coating
                val view = weapon_specific_section.inflate()
                bindBow(weapon, view)
            }
            WeaponType.HEAVY_BOWGUN, WeaponType.LIGHT_BOWGUN -> {
                weapon_specific_section.layoutResource = R.layout.view_bowgun_detail
                val view = weapon_specific_section.inflate()
                bindBowgun(ammo, view)
            }
            else -> {
                weapon_specific_section.layoutResource = R.layout.view_weapon_sharpness
                val view = weapon_specific_section.inflate()
                bindBasicBladeWeapon(weapon, view)
            }
        }
    }

    /**
     * Converts the map of the different recipe types (craft, upgrade, etc.) into section headers and lists
     */
    private fun populateComponents(recipes: Map<String?, List<ItemQuantity>>) {
        if (recipes.isEmpty()) {
            weapon_recipes.visibility = View.GONE
            return
        }

        weapon_recipes.visibility = View.VISIBLE

        // Inner function to inflate a sub recipe view.
        fun inflateRecipe(type: String, items: List<ItemQuantity>): View {
            val view = layoutInflater.inflate(R.layout.view_weapon_recipe, weapon_recipes, false)

            view.weapon_components_list_title.setLabelText(when (type) {
                "Create" -> getString(R.string.header_required_materials_craft)
                else -> getString(R.string.header_required_materials_upgrade)
            })

            for (component in items) {
                val itemView = IconLabelTextCell(context)
                val icon = AssetLoader.loadIconFor(component.item)

                itemView.setLeftIconDrawable(icon)
                itemView.setLabelText(component.item.name)
                itemView.setValueText(getString(R.string.format_quantity_none, component.quantity))
                itemView.setOnClickListener {
                    getRouter().navigateItemDetail(component.item.id)
                }

                view.weapon_components_list.addView(itemView)
            }

            return view
        }

        weapon_recipes.removeAllViews()
        for (recipe in recipes) {
            weapon_recipes.addView(inflateRecipe(recipe.key ?: "", recipe.value))
        }
    }

    /**
     * Draws 5 sharpness bars for weapons affected by the handicraft skill, 1 bar for weapons that aren't
     * and hides the sharpness section for weapons that do not have sharpness
     */
    private fun populateSharpness(sharpness: WeaponSharpness?, view: View) {
        if (sharpness != null) {
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

        view.findViewById<TextView>(R.id.phial_type_value).text = AssetLoader.localizePhialType(weapon.phial)

        when (weapon.phial_power) {
            0 -> view.findViewById<TextView>(R.id.phial_power_value).visibility = View.GONE
            else -> {
                view.findViewById<TextView>(R.id.phial_power_value).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.phial_power_value).text = weapon.phial_power.toString()
            }
        }

        when (AssetLoader.loadIconFor(weapon.phial)) {
            null -> view.findViewById<ImageView>(R.id.phial_element_icon).visibility = View.GONE
            else -> {
                view.findViewById<TextView>(R.id.phial_power_value).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.phial_element_icon).setImageDrawable(AssetLoader.loadIconFor(weapon.phial))
            }
        }
    }

    private fun bindInsectGlaive(weapon: Weapon, view: View) {
        populateSharpness(weapon.sharpnessData, view)
        view.findViewById<TextView>(R.id.kinsect_bonus_value).text = AssetLoader.localizeKinsectBonus(weapon.kinsect_bonus)
    }

    private fun bindGunlance(weapon: Weapon, view: View) {
        populateSharpness(weapon.sharpnessData, view)
        view.findViewById<TextView>(R.id.shelling_type).text = AssetLoader.localizeShellingType(weapon.shelling)
        view.findViewById<TextView>(R.id.shelling_level_value).text = getString(R.string.skill_level_short_qty, weapon.shelling_level)
    }

    private fun bindHuntingHorn(weapon: Weapon, melodies: List<WeaponMelody>?, view: View) {
        populateSharpness(weapon.sharpnessData, view)
        notes_layout.removeAllViews()

        weapon.notes?.forEachIndexed { index, note ->
            val noteIcon = ImageView(context)
            noteIcon.layoutParams = ViewGroup.LayoutParams(
                    resources.getDimension(R.dimen.image_size_xsmall).toInt(),
                    resources.getDimension(R.dimen.image_size_xsmall).toInt())
            noteIcon.setImageDrawable(loadNoteFromChar(note, index))
            notes_layout.addView(noteIcon)
        }

        view.melody_layout.removeAllViews()
        melodies?.forEach { melody ->
            val melodyView = layoutInflater.inflate(R.layout.listitem_hunting_horn_melody, melody_layout, false)
            melody.notes.forEachIndexed { index, note ->
                val noteIcon = when (index) {
                    0 -> melodyView.note1_icon
                    1 -> melodyView.note2_icon
                    2 -> melodyView.note3_icon
                    3 -> melodyView.note4_icon
                    else -> null
                }

                val noteType = weapon.notes?.indexOf(note) ?: 0
                noteIcon?.setImageDrawable(loadNoteFromChar(note, noteType))
            }

            melodyView.effect1.text = melody.effect1
            melodyView.effect2.text = melody.effect2
            melodyView.duration_value.text = melody.duration
            melodyView.extension_value.text = melody.extension
            melody_layout.addView(melodyView)
        }
    }

    private fun bindBow(weapon: Weapon, view: View) {
        weapon.weaponCoatings?.iterator()?.forEach {
            when (it) {
                CoatingType.BLAST -> {
                    blast_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.BLAST))
                    blast_coating.visibility = View.VISIBLE
                }
                CoatingType.POWER -> {
                    power_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.POWER))
                    power_coating.visibility = View.VISIBLE
                }
                CoatingType.POISON -> {
                    poison_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.POISON))
                    poison_coating.visibility = View.VISIBLE
                }
                CoatingType.PARALYSIS -> {
                    paralysis_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.PARALYSIS))
                    paralysis_coating.visibility = View.VISIBLE
                }
                CoatingType.SLEEP -> {
                    sleep_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.SLEEP))
                    sleep_coating.visibility = View.VISIBLE
                }
                CoatingType.CLOSE_RANGE -> {
                    close_range_coating_icon.setImageDrawable(AssetLoader.loadIconFor(CoatingType.CLOSE_RANGE))
                    close_range.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun bindBasicBladeWeapon(weapon: Weapon, view: View) {
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
            val shotTypeStr = if (it.type == AmmoType.WYVERN_AMMO) {
                getString(R.string.weapon_bowgun_ammo_shot_rapid_fire)
            } else if (it.isRapid) {
                getString(R.string.weapon_bowgun_ammo_shot_rapid_fire)
            } else if (it.recoil == -1) {
                getString(R.string.weapon_bowgun_ammo_shot_auto_reload)
            } else if (!it.isRapid && it.recoil != -1) {
                getString(R.string.weapon_bowgun_ammo_shot_normal)
            } else {
                getString(R.string.general_none)
            }
            val recoilStr = if (it.recoil <= 0) "" else getString(R.string.format_plus, it.recoil)

            view.shot_type_value_recoil.text = String.format("%s%s", shotTypeStr, recoilStr)

            view.reload_value.text = when (it.reload) {
                ReloadType.NONE -> getString(R.string.general_none)
                ReloadType.VERY_SLOW -> getString(R.string.weapon_bowgun_ammo_reload_very_slow)
                ReloadType.SLOW -> getString(R.string.weapon_bowgun_ammo_reload_slow)
                ReloadType.NORMAL -> getString(R.string.weapon_bowgun_ammo_reload_normal)
                ReloadType.FAST -> getString(R.string.weapon_bowgun_ammo_reload_fast)
                ReloadType.VERY_FAST -> getString(R.string.weapon_bowgun_ammo_reload_very_fast)
            }

            view.ammo_icon.setImageDrawable(loadIconFor(it.type))
            ammo_layout.addView(view)
        }
    }
}