package items

abstract class Inventory(
    open var name: String,
    open var weight: Double,
    open var volume: Double
) {
    val listOfItems: MutableList<Item> = mutableListOf()

    abstract fun store(item: Item): Boolean
}

class BackPack(
    override var name: String,
    override var volume: Double,
    override var weight: Double
) : Inventory(name, volume, weight
) {
    override fun store(item: Item): Boolean = listOfItems.add(item)
}

class NoInventory(
    override var name: String = "no inventory",
    override var volume: Double = 0.0,
    override var weight: Double = 0.0
) : Inventory(name, volume, weight
) {
    override fun store(item: Item): Boolean = listOfItems.add(item)
}