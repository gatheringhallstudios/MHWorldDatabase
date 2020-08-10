package com.gatheringhallstudios.mhworlddatabase.assets

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.types.ElementStatus
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectDustEffect
import com.gatheringhallstudios.mhworlddatabase.data.types.PhialType

private fun <T, K> createRegistry(vararg pairs: Pair<T, K>): (T) -> K? {
    val registry = mapOf(*pairs)
    return { registry[it] }
}

/**
 * Contains all mappings from icon name to a recolorable vector.
 * Do not create any additional registries, if anything, create mappings
 * from enums to entries in this registry.
 * This may or may not become automated
 */
val VectorRegistry = createRegistry(
        "Skill" to R.xml.ic_ui_armor_skill_base,

        "ArmorSet" to R.xml.ic_equipment_armor_set_base,
        "ArmorHead" to R.xml.ic_equipment_head_base,
        "ArmorChest" to R.xml.ic_equipment_chest_base,
        "ArmorArms" to R.xml.ic_equipment_arm_base,
        "ArmorWaist" to R.xml.ic_equipment_waist_base,
        "ArmorLegs" to R.xml.ic_equipment_leg_base,

        "GreatSword" to R.xml.ic_equipment_greatsword_base,
        "LongSword" to R.xml.ic_equipment_longsword_base,
        "SwordAndShield" to R.xml.ic_equipment_sword_and_shield_base,
        "DualBlades" to R.xml.ic_equipment_dual_blades_base,
        "Hammer" to R.xml.ic_equipment_hammer_base,
        "HuntingHorn" to R.xml.ic_equipment_hunting_horn_base,
        "Lance" to R.xml.ic_equipment_lance_base,
        "Gunlance" to R.xml.ic_equipment_gunlance_base,
        "SwitchAxe" to R.xml.ic_equipment_switch_axe_base,
        "ChargeBlade" to R.xml.ic_equipment_charge_blade_base,
        "InsectGlaive" to R.xml.ic_equipment_insect_glaive_base,
        "Bow" to R.xml.ic_equipment_bow_base,
        "LightBowgun" to R.xml.ic_equipment_light_bowgun_base,
        "HeavyBowgun" to R.xml.ic_equipment_heavy_bowgun_base,
        "HuntingHornNote1" to R.xml.ic_ui_note_1_base,
        "HuntingHornNote2" to R.xml.ic_ui_note_2_base,
        "HuntingHornNote3" to R.xml.ic_ui_note_3_base,

        "Charm" to R.xml.ic_equipment_charm_base,

        "Star" to R.xml.ic_ui_quest_star_base,

        "Decoration1" to R.xml.ic_ui_decoration_1_base,
        "Decoration2" to R.xml.ic_ui_decoration_2_base,
        "Decoration3" to R.xml.ic_ui_decoration_3_base,
        "Decoration4" to R.xml.ic_ui_decoration_4_base,

        "Slot1" to R.drawable.ic_ui_slot_1_empty,
        "Slot2" to R.drawable.ic_ui_slot_2_empty,
        "Slot3" to R.drawable.ic_ui_slot_3_empty,
        "Slot4" to R.drawable.ic_ui_slot_4_empty,

        "Slot1Jewel1" to R.xml.ic_ui_slot_1_jewel_1_base,
        "Slot2Jewel1" to R.xml.ic_ui_slot_2_jewel_1_base,
        "Slot2Jewel2" to R.xml.ic_ui_slot_2_jewel_2_base,
        "Slot3Jewel1" to R.xml.ic_ui_slot_3_jewel_1_base,
        "Slot3Jewel2" to R.xml.ic_ui_slot_3_jewel_2_base,
        "Slot3Jewel3" to R.xml.ic_ui_slot_3_jewel_3_base,
        "Slot4Jewel1" to R.xml.ic_ui_slot_4_jewel_1_base,
        "Slot4Jewel2" to R.xml.ic_ui_slot_4_jewel_2_base,
        "Slot4Jewel3" to R.xml.ic_ui_slot_4_jewel_3_base,
        "Slot4Jewel4" to R.xml.ic_ui_slot_4_jewel_4_base,

        "KinsectSever" to R.xml.ic_ui_kinsect_sever_base,
        "KinsectBlunt" to R.xml.ic_ui_kinsect_blunt_base,

        "NodeStart" to R.xml.ui_tree_node_start_base,
        "NodeStartCollapsed" to R.xml.ui_tree_node_start_collapsed_base,
        "NodeMid" to R.xml.ui_tree_node_mid_base,
        "NodeMidCollapsed" to R.xml.ui_tree_node_mid_collapsed_base,
        "NodeThrough" to R.xml.ui_tree_node_through_base,
        "NodeThroughCollapsed" to R.xml.ui_tree_node_through_collapsed_base,
        "NodeEnd" to R.xml.ui_tree_node_end_base,
        "NodeEndIndented" to R.xml.ui_tree_node_end_indented_base,

        "Ammo" to R.xml.ic_items_ammo_base,
        "Bait" to R.xml.ic_items_bait_base,
        "Barrel" to R.xml.ic_items_barrel_base,
        "BarrelBomb" to R.xml.ic_items_barrel_bomb_base,
        "Binoculars" to R.xml.ic_items_binoculars_base,
        "Body" to R.xml.ic_items_body_base,
        "Bone" to R.xml.ic_items_bone_base,
        "Book" to R.xml.ic_items_book_base,
        "Boomerang" to R.xml.ic_items_boomerang_base,
        "Bottle" to R.xml.ic_items_bottle_base,
        "Bug" to R.xml.ic_items_bug_base,
        "Carapace" to R.xml.ic_items_carapace_base,
        "CharmOre" to R.xml.ic_items_charm_ore_base,
        "Coin" to R.xml.ic_items_coin_base,
        "Dung" to R.xml.ic_items_dung_base,
        "EmptyBottle" to R.xml.ic_items_empty_bottle_base,
        "Fang" to R.xml.ic_items_fang_base,
        "Feystone" to R.xml.ic_items_feystone_base,
        "Gem" to R.xml.ic_items_gem_base,
        "Herb" to R.xml.ic_items_herb_base,
        "Hide" to R.xml.ic_items_hide_base,
        "Honey" to R.xml.ic_items_honey_base,
        "Husk" to R.xml.ic_items_husk_base,
        "Jaw" to R.xml.ic_items_monster_jaw_base,
        "Knife" to R.xml.ic_items_knife_base,
        "Liquid" to R.xml.ic_items_liquid_base,
        "Mantle" to R.xml.ic_items_mantle_base,
        "Meat" to R.xml.ic_items_meat_base,
        "Mushroom" to R.xml.ic_items_mushroom_base,
        "Ore" to R.xml.ic_items_ore_base,
        "Pellets" to R.xml.ic_items_pellets_base,
        "Plate" to R.xml.ic_items_plate_base,
        "Question" to R.xml.ic_ui_question_mark_base,
        "Sac" to R.xml.ic_items_sac_base,
        "Scale" to R.xml.ic_items_scale_base,
        "Seed" to R.xml.ic_items_seed_base,
        "Slinger" to R.xml.ic_items_slinger_base,
        "Smoke" to R.xml.ic_items_smoke_base,
        "Sphere" to R.xml.ic_items_sphere_base,
        "Streamstone" to R.xml.ic_items_streamstone_base,
        "Tail" to R.xml.ic_items_tail_base,
        "Trap" to R.xml.ic_items_trap_base,
        "TrapTool" to R.xml.ic_items_trap_tool_base,
        "Voucher" to R.xml.ic_items_voucher_base,
        "Web" to R.xml.ic_items_web_base,
        "Webbing" to R.xml.ic_items_honey_base,
        "Wing" to R.xml.ic_items_wing_base,
        "Booster" to R.xml.ic_equipment_booster_base,
        "ToolMantle" to R.xml.ic_equipment_mantle_base
)

val ElementRegistry = fun(element: ElementStatus?) = when (element) {
    ElementStatus.FIRE -> R.drawable.ic_element_fire
    ElementStatus.WATER -> R.drawable.ic_element_water
    ElementStatus.THUNDER -> R.drawable.ic_element_thunder
    ElementStatus.ICE -> R.drawable.ic_element_ice
    ElementStatus.DRAGON -> R.drawable.ic_element_dragon
    ElementStatus.POISON -> R.drawable.ic_status_poison
    ElementStatus.SLEEP -> R.drawable.ic_status_sleep
    ElementStatus.PARALYSIS -> R.drawable.ic_status_paralysis
    ElementStatus.BLAST -> R.drawable.ic_status_blast
    else -> R.drawable.ic_ui_slot_none
}

val PhialRegistry = fun(phial: PhialType?) = when (phial) {
    // TODO Add icons for these phial types if they exist
    PhialType.EXHAUST -> 0
    PhialType.POWER -> 0
    PhialType.DRAGON -> R.drawable.ic_element_dragon
    PhialType.POWER_ELEMENT -> 0
    PhialType.POISON -> R.drawable.ic_status_poison
    PhialType.PARALYSIS -> R.drawable.ic_status_paralysis
    PhialType.IMPACT -> 0
    else -> 0
}

val KinsectDustRegistry = fun(dustEffect: KinsectDustEffect?) = when (dustEffect) {
    KinsectDustEffect.POISON -> R.drawable.ic_status_poison
    KinsectDustEffect.PARALYSIS -> R.drawable.ic_status_paralysis
    KinsectDustEffect.HEAL -> R.drawable.ic_ui_kinsect_heal
    KinsectDustEffect.BLAST -> R.drawable.ic_status_blast
    else -> 0
}

val SlotEmptyRegistry = fun(slot: Int) = when (slot) {
    1 -> R.drawable.ic_ui_slot_1_empty
    2 -> R.drawable.ic_ui_slot_2_empty
    3 -> R.drawable.ic_ui_slot_3_empty
    4 -> R.drawable.ic_ui_slot_4_empty
    else -> R.drawable.ic_ui_slot_none
}

val SetBonusNumberRegistry = fun(num: Int) = when (num) {
    1 -> R.drawable.ic_ui_set_bonus_1
    2 -> R.drawable.ic_ui_set_bonus_2
    3 -> R.drawable.ic_ui_set_bonus_3
    4 -> R.drawable.ic_ui_set_bonus_4
    5 -> R.drawable.ic_ui_set_bonus_5
    else -> 0
}

val LocationDrawableRegistry = fun(id: Int) = when (id) {
    1 -> R.drawable.ic_location_ancient_forest
    2 -> R.drawable.ic_locations_wildspire_waste
    3 -> R.drawable.ic_locations_coral_highlands
    4 -> R.drawable.ic_locations_rotten_vale
    5 -> R.drawable.ic_locations_elders_recess
    12 -> R.drawable.ic_locations_hoarfrost_reach
    14 -> R.drawable.ic_locations_the_guiding_lands
    else -> R.drawable.ic_ui_question_mark_paper
}

val ColorRegistry = createRegistry(
        "rare1" to R.color.icon_gray,
        "rare2" to R.color.icon_white,
        "rare3" to R.color.icon_lime,
        "rare4" to R.color.icon_green,
        "rare5" to R.color.icon_dull_cyan,
        "rare6" to R.color.icon_dark_purple,
        "rare7" to R.color.icon_violet,
        "rare8" to R.color.icon_orange,
        "rare9" to R.color.icon_red,
        "rare10" to R.color.icon_blue,
        "rare11" to R.color.icon_gold,
        "rare12" to R.color.icon_cyan,

        "LowRank" to R.color.icon_blue,
        "HighRank" to R.color.icon_bright_orange,

        "White" to R.color.icon_white,
        "Gray" to R.color.icon_gray,
        "Pink" to R.color.icon_pink,
        "Red" to R.color.icon_red,
        "DarkRed" to R.color.icon_dark_red,
        "Orange" to R.color.icon_orange,
        "BrightOrange" to R.color.icon_bright_orange,
        "LightBeige" to R.color.icon_light_beige,
        "Beige" to R.color.icon_beige,
        "DarkBeige" to R.color.icon_dark_beige,
        "Gold" to R.color.icon_gold,
        "Yellow" to R.color.icon_yellow,
        "Violet" to R.color.icon_violet,
        "Blue" to R.color.icon_blue,
        "Cyan" to R.color.icon_cyan,
        "Green" to R.color.icon_green,
        "DarkGreen" to R.color.icon_dark_green,
        "DarkBlue" to R.color.icon_dark_blue,
        "DarkPurple" to R.color.icon_dark_purple
)