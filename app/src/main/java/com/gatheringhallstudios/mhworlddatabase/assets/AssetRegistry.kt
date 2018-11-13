package com.gatheringhallstudios.mhworlddatabase.assets

import com.gatheringhallstudios.mhworlddatabase.R

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

        // TODO Add weapons
        "GreatSword" to R.xml.ic_equipment_greatsword_base,
        //"ChargeBlade" to R.drawable.ic_equipment_charge_blade_white,
        "Hammer" to R.xml.ic_equipment_hammer_base,
        "InsectGlaive" to R.xml.ic_equipment_insect_glaive_base,
        "Lance" to R.xml.ic_equipment_lance_base,
        "LongSword" to R.xml.ic_equipment_longsword_base,
        "SwordAndShield" to R.xml.ic_equipment_sword_and_shield_base,
        "DualBlades" to R.xml.ic_equipment_dual_blades_base,
        "HuntingHorn" to R.xml.ic_equipment_hunting_horn_base,

        "Charm" to R.xml.ic_equipment_charm_base,

        "Decoration1" to R.xml.ic_ui_decoration_1_base,
        "Decoration2" to R.xml.ic_ui_decoration_2_base,
        "Decoration3" to R.xml.ic_ui_decoration_3_base,

        "Ammo" to R.xml.ic_items_ammo_base,
        "Bait" to R.xml.ic_items_bait_base,
        "Barrel" to R.xml.ic_items_barrel_base,
        "BarrelBomb" to R.xml.ic_items_barrel_bomb_base,
        "Binoculars" to R.xml.ic_items_binoculars_base,
        "Body" to R.xml.ic_items_body_base,
        "Bone" to R.xml.ic_items_bone_base,
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
        "Wing" to R.xml.ic_items_wing_base
)

val SlotEmptyRegistry = fun(slot: Int) = when(slot) {
    1 -> R.drawable.ic_ui_slot_1_empty
    2 -> R.drawable.ic_ui_slot_2_empty
    3 -> R.drawable.ic_ui_slot_3_empty
    else -> R.drawable.ic_ui_slot_none
}

val SetBonusNumberRegistry = fun(num: Int) = when(num) {
    1 -> R.drawable.ic_ui_set_bonus_1
    2 -> R.drawable.ic_ui_set_bonus_2
    3 -> R.drawable.ic_ui_set_bonus_3
    4 -> R.drawable.ic_ui_set_bonus_4
    5 -> R.drawable.ic_ui_set_bonus_5
    else -> 0
}

val LocationDrawableRegistry = fun(id: Int) = when(id) {
    1 -> R.drawable.ic_location_ancient_forest
    2 -> R.drawable.ic_locations_wildspire_waste
    3 -> R.drawable.ic_locations_coral_highlands
    4 -> R.drawable.ic_locations_rotten_vale
    5 -> R.drawable.ic_locations_elders_recess
    else -> R.drawable.ic_ui_question_mark_paper
}

val ColorRegistry = createRegistry(
        "rare1" to R.color.icon_gray,
        "rare2" to R.color.icon_white,
        "rare3" to R.color.icon_lime,
        "rare4" to R.color.icon_green,
        "rare5" to R.color.icon_cyan,
        "rare6" to R.color.icon_blue,
        "rare7" to R.color.icon_violet,
        "rare8" to R.color.icon_orange,

        "White" to R.color.icon_white,
        "Gray" to R.color.icon_gray,
        "Pink" to R.color.icon_pink,
        "Red" to R.color.icon_red,
        "DarkRed" to R.color.icon_dark_red,
        "Orange" to R.color.icon_orange,
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
        "DarkPurple" to R.color.icon_dark_purple
)