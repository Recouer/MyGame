package items.equipment

class HumanoidArmor(
    val armorList: HashMap<ArmorList, Armor>
): ArmorSet() {
    private val emptyArmor = Armor(0.0, 0.0, "", 0.0, ArmorList.HEAD)
    private fun defaultArmorValue() = entity.stats.constitution * 0.1

    override fun amorValue(): Double {
        var sum = 0.0
        for (armor in ArmorList.values()) {
            if (armorList[armor] != null) {
                sum += armorList[armor]!!.armorValue
            }
        }

        return defaultArmorValue() + (sum / 8.0)
    }

    override fun equipArmor(armor: Armor) {
        entity.equipment.storage.listOfItems.remove(armor)
        if (armorList[armor.armorType] == null || armorList[armor.armorType] == emptyArmor) {
            armorList[armor.armorType] = armor
        } else {
            entity.equipment.storage.store(armorList[armor.armorType]!!)
            armorList[armor.armorType] = armor
        }
    }

    override fun removeArmor(armor: Armor) {
        entity.equipment.storage.store(armorList[armor.armorType]!!)
        armorList[armor.armorType] = emptyArmor
    }

}

