package items.equipment

import characters.Entity
import items.Item

class Weapon(
    weight: Double,
    volume: Double,
    name: String,

    val baseDamage: Double,
    val armorPenetration: Double,
    val range: Int
) : Item(weight, volume, name) {

    fun equip(entity: Entity) = this.also { entity.equipment.weapon = it }
    fun unequip(entity: Entity) = this.also { entity.equipment.weapon = null }
}