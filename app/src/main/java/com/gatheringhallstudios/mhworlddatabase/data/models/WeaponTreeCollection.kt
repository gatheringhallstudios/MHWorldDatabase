package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.common.TreeNode


/**
 * A collection of weapon trees for a particular weapon type
 * todo: move to another file once we decided where it should go.
 */
class WeaponTreeCollection(
        weapons: List<Weapon>
) {
    /**
     * A list of all tree roots for this collection
     */
    val roots: List<TreeNode<Weapon>>

    /**
     * A list of all "leaf nodes" in this weapon tree
     */
    val leaves: List<TreeNode<Weapon>>

    private val weaponMap: Map<Int, TreeNode<Weapon>>

    init {
        // create a mapping of id -> weapon treenode
        weaponMap = weapons.asSequence().map { TreeNode(it) }.associateBy { it.value.id }

        //Add child relationships
        for (weapon in weaponMap) {
            val parentId = weapon.value.value.previous_weapon_id
            if (parentId != null) {
                weaponMap[parentId]?.addChild(weapon.value)
            }
        }

        // Extract all root nodes and then all leaves
        roots = weaponMap.filter { it.value.isRoot }.values.toList()
        leaves = roots.map { it.leaves }.flatten()
    }

    /**
     * Method used to
     */
    fun getWeapon(weaponId: Int) = weaponMap[weaponId]
}