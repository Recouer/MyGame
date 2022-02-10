package items.equipment

import items.Item

class Armor(
    weight: Double,
    volume: Double,
    name: String,

    val armorValue: Double,
    val armorType: ArmorList
) : Item(weight, volume, name) {


}