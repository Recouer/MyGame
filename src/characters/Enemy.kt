package characters

import items.Item
import items.equipment.Equipment
import map.TerrainChunk
import java.lang.Error

class Enemy(
    position: TerrainChunk.TerrainCube,
    name: String,
    stats: Stats,
    equipment: Equipment
) : Entity(position, name, stats, equipment) {

    init {
        if (position.entity == null)
            position.entity = this
        else
            println("error on assign: already an entity when initializing enemy")
    }

    override fun attack(entity: Entity) = try {
        if (this.getAttackRange() < this.distanceFromEntity(entity))
            throw Error("Range too short\n")

        val resistance = entity.equipment.armorSet.amorValue()
        val baseDamage: Double
        val armorPenetration: Double

        if (this.equipment.weapon != null) {
            val weapon = this.equipment.weapon!!
            baseDamage = weapon.baseDamage
            armorPenetration = weapon.armorPenetration
        } else {
            baseDamage = 10.0
            armorPenetration = 0.0
        }

        var value = ((this.stats.strength - (entity.stats.constitution + (entity.stats.strength * 0.2))) / this.stats.strength)
        val impactDamage =  if (value < 0) 0.0 else value

        value = baseDamage * (armorPenetration + impactDamage) + baseDamage - resistance
        val damage = if (value > 0) value else 0.0
        entity.stats.health -= damage

        println("${entity.name} has received $damage damage")

        entity.onDeath()

    } catch (_: Error) {
    }

    override fun interact(entity: Entity) {
        println("I do nothing")
    }

    override fun onDeath() {
        if (stats.health > 0) return

        position.ItemList.add(
            Item(50.0, 50.0, "${name}'s corpse")
        )
        position.entity = null
    }

    override fun drop(item: Item) {
        this.equipment.storage.listOfItems.remove(item)
        position.ItemList.add(item)
    }

    override fun getAttackRange(): Int {
        return if (this.equipment.weapon == null) this.stats.baseRange
        else this.equipment.weapon!!.range + this.stats.baseRange
    }
}