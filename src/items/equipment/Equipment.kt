package items.equipment

import items.Inventory

data class Equipment(
    var weapon: Weapon?,
    var storage: Inventory,
    var armorSet: ArmorSet
)

