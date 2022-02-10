package map

import characters.Enemy
import characters.Entity
import characters.Player
import items.Item
import items.Material


class TerrainChunk(var seed: Int, var XOffSet: Double, var YOffSet: Double) {
    class TerrainCube(
        var Materials: Material,
        var height: Double,
        val xPosition: Int,
        val yPosition: Int,
        val ItemList: MutableList<Item>,
        var entity: Entity?
    )

    val size = 8
    private var cubeList: Array<TerrainCube> = Array(size * size) { index -> cubeInit(index, XOffSet, YOffSet) }

    private fun cubeInit(index: Int, XOffSet: Double, YOffSet: Double): TerrainCube {
        val noise = ((Perlin.noise(
            ((index % size) / size.toDouble()) + XOffSet * size,
            (index / size) / size.toDouble() + YOffSet * size, 0.0
        ) + 0.707) / 1.414).toFloat()

        val material = when ((noise * 100).toInt()) {
            in 100 downTo 50 -> Material.ROCK
            in 49 downTo 20 -> Material.SOIL
            else -> Material.WATER
        }
        val height = (noise * 100).toDouble()
        return TerrainCube(
            material,
            height,
            (index % size + XOffSet * size).toInt(),
            (index / size + YOffSet * size).toInt(),
            mutableListOf<Item>(),
            null
        )
    }

    override fun toString(): String {
        var str: String = ""
        val list = List<Int>(size) { index -> size * (index + 1) - 1 }
        cubeList.forEachIndexed() { i, cube ->
            run {
                when (i) {
                    in list -> str += "${cube.height.toInt()}\n"
                    else -> str += "${cube.height.toInt()} | "
                }
            }
        }

        return str
    }

    fun getTile(x: Int, y:Int): TerrainCube = cubeList[y * size + x]

    fun toString(i: Int): String {
        var str: String = ""
        for (index in size * i until size * (i + 1)) {
            if (cubeList[index].entity != null) {
                if (cubeList[index].entity!!::class == Enemy::class) str += "-E- "
                else if (cubeList[index].entity!!::class == Player::class) str += "-P- "
            }
            else str += "--- "
        }
        return str
    }
}