package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponFull
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponSharpness
import com.gatheringhallstudios.mhworlddatabase.data.types.ElderSealLevel
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_weapon_summary.*
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

        //Draw sharpness bar/hide it for weapons that don't have it
        populateSharpness(weapon.sharpnessData)

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
    private fun populateSharpness(sharpness: WeaponSharpness?) {
        if (sharpness != null) {
            //One bar for weapons not affected by sharpness
            if (sharpness.sharpness_maxed!!) {
                sharpness_value.drawSharpness(sharpness.get(0))
                sharpness_container1.visibility = View.GONE
                sharpness_container2.visibility = View.GONE
                sharpness_container3.visibility = View.GONE
                sharpness_container4.visibility = View.GONE
                sharpness_container5.visibility = View.GONE
            } else {
                sharpness_label.text = getString(R.string.format_plus, 0)
                sharpness_value.drawSharpness(sharpness.get(0))

                sharpness_label1.text = getString(R.string.format_plus, 1)
                sharpness_value1.drawSharpness(sharpness.get(1))

                sharpness_label2.text = getString(R.string.format_plus, 2)
                sharpness_value2.drawSharpness(sharpness.get(2))

                sharpness_label3.text = getString(R.string.format_plus, 3)
                sharpness_value3.drawSharpness(sharpness.get(3))

                sharpness_label4.text = getString(R.string.format_plus, 4)
                sharpness_value4.drawSharpness(sharpness.get(4))

                sharpness_label5.text = getString(R.string.format_plus, 5)
                sharpness_value5.drawSharpness(sharpness.get(5))
            }
        } else {
            hideSharpness()
        }
    }

    private fun hideSharpness() {
        sharpness_container.visibility = View.GONE
        sharpness_container1.visibility = View.GONE
        sharpness_container2.visibility = View.GONE
        sharpness_container3.visibility = View.GONE
        sharpness_container4.visibility = View.GONE
        sharpness_container5.visibility = View.GONE
    }
}