package items.equipment

import characters.Entity

abstract class ArmorSet {
    lateinit var entity: Entity

    abstract fun amorValue() : Double

    abstract fun equipArmor(armor: Armor)

    abstract fun removeArmor(armor: Armor)
}