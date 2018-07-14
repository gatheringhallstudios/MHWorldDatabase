package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.getVectorDrawable
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetBonusView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorView
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.android.synthetic.main.fragment_armor_summary.*
import kotlinx.android.synthetic.main.listitem_armorset_bonus.view.*
import kotlinx.android.synthetic.main.listitem_large.view.*

class ArmorSummaryFragment : Fragment() {

    private val viewModel : ArmorDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(ArmorDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_armor_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.armor.observe(this, Observer(::populateArmor))
        viewModel.armorSetSkill.observe(this, Observer(::populateSetBonuses))
    }

    private fun populateArmor(armor: ArmorView?) {
        if(armor == null) return
        val loader  = AssetLoader(context!!)

        armor_detail_name.text = armor.name
        armor_detail_rarity.setTextColor(loader.loadRarityColor(armor.rarity))
        armor_detail_rarity.text = getString(
                R.string.armor_detail_rarity,
                armor.rarity)
        armor_icon.setImageDrawable(loader.loadArmorIcon(armor.armor_type, armor.rarity))

        //Armor defense
        defense_cell.setLabelText(getString(
                R.string.armor_defense_value,
                armor.data.defense_base,
                armor.data.defense_max,
                armor.data.defense_augment_max))
        defense_cell.removeDecorator()

        populateSlots(armor.data.slot_1, armor.data.slot_2, armor.data.slot_3)
    }

    private fun populateSlots(slot_1 : Int, slot_2 : Int, slot_3: Int) {
        slots_cell.removeDecorator()
    }

    private fun populateSetBonuses(armorSetBonusViews: List<ArmorSetBonusView>?) {
        if(armorSetBonusViews == null) {
            hideSetBonuses()
            return
        }

        if(armor_set_bonus_layout.childCount > 0) {
            armor_set_bonus_layout.removeAllViews()
        }

        //Set the label for the Set name
        val view = layoutInflater.inflate(R.layout.listitem_large, null)
        view.item_name.text = armorSetBonusViews.first().name
        view.item_icon.visibility = View.GONE
        armor_set_bonus_layout.addView(view)

        //Now to set the actual skills
        for(armorSetBonusView in armorSetBonusViews) {
            val listItem = layoutInflater.inflate(R.layout.listitem_armorset_bonus, null)

            val icon = view.context.getVectorDrawable(R.drawable.ic_ui_armor_skill_base, armorSetBonusView.icon_color)

            listItem.skill_icon.setImageDrawable(icon)
            listItem.piece_bonus_text.text = getString(R.string.armor_detail_piece_bonus,
                    armorSetBonusView.required)
            listItem.skill_name.text = armorSetBonusView.skillName
            listItem.setOnClickListener({
                getRouter().navigateSkillDetail(armorSetBonusView.skilltree_id)
            })

            armor_set_bonus_layout.addView(listItem)
        }
    }

    private fun hideSetBonuses(){
        armor_set_bonus_header.visibility = View.GONE
        armor_set_bonus_layout.visibility = View.GONE
    }

    private fun populateSkills() {

    }
}