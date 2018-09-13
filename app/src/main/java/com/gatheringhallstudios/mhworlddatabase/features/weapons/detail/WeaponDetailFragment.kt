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
import com.gatheringhallstudios.mhworlddatabase.assets.*
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.fragment_weapon_summary.*

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

        //Draw sharpness bar/hide it for weapons that don't have it
        val sharpnessData = weapon.sharpnessData
        if (sharpnessData != null) {
            sharpness_value.drawSharpness(sharpnessData.get(0))
        } else {
            sharpness_value.visibility = View.INVISIBLE
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

    private fun populateComponents(components: List<ItemQuantity>) {
        if (components.isEmpty()) {
            weapon_components_section.visibility = View.GONE
            return
        }

        weapon_components_section.visibility = View.VISIBLE
        weapon_components_list.removeAllViews()

        for (itemQuantity in components) {
            val view = IconLabelTextCell(context)
            val icon = AssetLoader.loadIconFor(itemQuantity.item)

            view.setLeftIconDrawable(icon)
            view.setLabelText(itemQuantity.item.name)
            view.setValueText(getString(R.string.format_quantity_none, itemQuantity.quantity))
            view.setOnClickListener {
                getRouter().navigateItemDetail(itemQuantity.item.id)
            }

            weapon_components_list.addView(view)
        }
    }
}