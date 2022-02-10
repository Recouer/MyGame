package map

class Terrain {
    var chunkRadius: Int = 3
    var chunks = Array<TerrainChunk>(chunkRadius * chunkRadius) { i ->
        TerrainChunk(0, (i % chunkRadius).toDouble(), (i / chunkRadius).toDouble())
    }

    override fun toString(): String {
        var str: String = "   "
        for (j in 0 until chunkRadius)
            for (k in 0 until chunks[0].size) {
                val value = j * chunks[0].size + k
                if (value < 10)
                    str += "  ${j * chunks[0].size + k} "
                else
                    str += " ${j * chunks[0].size + k} "


            }
        var a = 0
        str += "\n${a++}   "
        for (i in 0 until chunkRadius)
            for (k in 0 until chunks[0].size) {
                for (j in 0 until chunkRadius)
                    str += chunks[chunkRadius * i + j].toString(k)
                if (a < 10)
                    str += "\n${a++}   "
                else str += "\n${a++}  "
            }

        return str
    }

    fun getTile(x: Int, y: Int): TerrainChunk.TerrainCube {
        val size = chunks[0].size
        val xOffSet = x / size
        val yOffSet = y / size
        return chunks[yOffSet * chunkRadius + xOffSet].getTile(x % size, y % size)
    }
}