package characters

import items.Item
import items.equipment.Equipment
import map.TerrainChunk
import kotlin.math.absoluteValue

abstract class Entity(
    var position: TerrainChunk.TerrainCube,
    val name: String,
    val stats: Stats,
    val equipment: Equipment

) {

    init { this.also { equipment.armorSet.entity = it } }

    fun move(newPosition: TerrainChunk.TerrainCube): Int {
        println("$name was at position ${position.xPosition} ${position.yPosition}")
        if (newPosition.entity != null) return 0
        this.position.entity = null
        this.position = newPosition
        this.position.entity = this
        println("$name is at position ${position.xPosition} ${position.yPosition}")
        return 1
    }

    abstract fun attack(entity: Entity)

    abstract fun interact(entity: Entity)

    abstract fun onDeath()

    abstract fun drop(item: Item)

    abstract fun getAttackRange(): Int

    fun getMaxTilePerTurn(): Int = (this.stats.speed / 30).toInt()

    fun distanceFromEntity(entity: Entity): Int =
        (entity.position.yPosition - this.position.yPosition).absoluteValue + (entity.position.xPosition - this.position.xPosition).absoluteValue
}
