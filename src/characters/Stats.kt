package characters

import kotlin.Double as Double


data class Stats(
    var health: Double,
    var mana: Double,
    var speed: Double,
    var strength: Double,
    var intelligence: Double,
    var constitution: Double,
    var stamina: Double,

    var baseRange: Int = 1,
    var priority: Int = 0
)
