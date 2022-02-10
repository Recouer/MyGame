package game

import characters.Enemy
import characters.Entity
import characters.Player
import characters.Stats
import items.BackPack
import items.NoInventory
import items.equipment.*
import map.Terrain
import java.lang.NumberFormatException
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor

class Game {
    private val terrain: Terrain = Terrain()
    private lateinit var player: Entity
    private val enemies: MutableList<Entity> = mutableListOf()

    init {
        val playerStats = Stats(
            100.0,
            100.0,
            100.0,
            100.0,
            100.0,
            100.0,
            100.0,
            1
        )

        val playerEquipment: Equipment = Equipment(
            Weapon(1.5, .300, "sword", 50.0, 0.1, 1),
            BackPack("leather BackPack", 50.0, 20.0),
            HumanoidArmor(
                hashMapOf(
                    Pair(
                        ArmorList.HEAD,
                        Armor(2.0, 0.500, "Iron Helmet", 5.0, ArmorList.HEAD)
                    ),
                    Pair(
                        ArmorList.CHEST,
                        Armor(10.0, 2.0, "Iron Chest Plate", 20.0, ArmorList.CHEST)
                    )
                )
            )
        )

        this.player = Player(terrain.getTile(5, 5), stats = playerStats, equipment = playerEquipment, name = "player")

    }

    fun play() {
        while (!gameOver()) {
            gameTurn()
        }
    }

    private fun gameTurn() {
        println(terrain.toString())

        showOptions()

        val enemyIterator = enemies.iterator()
        while (enemyIterator.hasNext()) {
            val enemy = enemyIterator.next()
            if (enemy.stats.health <= 0)
                enemyIterator.remove()

            if (enemy.stats.priority >= 100)
                enemyTurn(enemy)
        }

        val priorityIncrement = 100 - player.stats.priority
        player.stats.priority = 100
        for (enemy in enemies) {
            println("there was an enemy")
            enemy.stats.priority += (priorityIncrement * (player.stats.speed / enemy.stats.speed)).toInt()
        }
    }

    private fun enemyTurn(enemy: Entity) {

        run loop@{
            if (enemy.distanceFromEntity(player) <= enemy.getAttackRange()) {
                enemy.attack(player)
                enemy.stats.priority -= 50
                return
            }

            val xDistance = (enemy.position.xPosition - player.position.xPosition).absoluteValue
            val yDistance = (enemy.position.yPosition - player.position.yPosition).absoluteValue

            val xSign = if (enemy.position.xPosition - player.position.xPosition < 0) 1 else -1
            val ySign = if (enemy.position.yPosition - player.position.yPosition < 0) 1 else -1

            if (enemy.distanceFromEntity(player) >= enemy.getMaxTilePerTurn()) {

                val xRatio = xDistance.toDouble() / (xDistance + yDistance)
                val yRatio = yDistance.toDouble() / (xDistance + yDistance)

                val dist = enemy.getMaxTilePerTurn()
                var x: Int
                var y: Int

                if (xDistance == yDistance) {
                    x = floor(dist * 0.5).toInt() * xSign
                    y = ceil(dist * 0.5).toInt() * ySign


                } else {
                    x = floor(dist * xRatio).toInt() * xSign
                    y = floor(dist * yRatio).toInt() * ySign
                    val xRest = dist * xRatio - x.absoluteValue
                    val yRest = dist * yRatio - y.absoluteValue

                    if (xRest < yRest) y += 1 * ySign
                    else x += 1 * xSign

                }

                enemy.move(
                    terrain.getTile(
                        enemy.position.xPosition + x,
                        enemy.position.yPosition + y
                    )
                )
                enemy.stats.priority -= 30 * (x + y)
            } else {

                if (xDistance == 0) {
                    enemy.move(
                        terrain.getTile(
                            enemy.position.xPosition,
                            enemy.position.yPosition + (yDistance - 1) * ySign
                        )
                    )
                } else if (yDistance == 0) {
                    enemy.move(
                        terrain.getTile(
                            enemy.position.xPosition + (xDistance - 1) * xSign,
                            enemy.position.yPosition
                        )
                    )
                } else {
                    enemy.move(
                        terrain.getTile(
                            enemy.position.xPosition + (xDistance - 1) * xSign,
                            enemy.position.yPosition + yDistance * ySign
                        )
                    )
                }
            }
            if (enemy.stats.priority >= 100) return@loop
        }
    }

    private fun addEnemy() {
        val enemyStat = Stats(
            100.0,
            100.0,
            100.0,
            100.0,
            100.0,
            100.0,
            100.0,
            1,
            100
        )
        val enemyEquipment = Equipment(
            null,
            NoInventory(),
            HumanoidArmor(
                hashMapOf(
                    Pair(
                        ArmorList.CHEST, Armor(
                            10.0,
                            10.0,
                            "pelt",
                            30.0,
                            ArmorList.CHEST
                        )
                    )
                )
            )
        )
        val newEnemy = Enemy(terrain.getTile(20, 20), "wolf", enemyStat, enemyEquipment)
        enemies.add(newEnemy)
    }

    private fun showOptions(): Int {
        println("1. Attack")
        println("2. Move")
        println("3. Spawn enemy")

        run loop@ {
            val strInput: String = readLine()!!
            try {
                when (strInput.toInt()) {
                    1 -> attackPlayer()
                    2 -> movePlayer()
                    3 -> addEnemy()
                    else -> println("i did not understand")
                }
            } catch (e: Exception) {
                println("i did not understand: retry")
                return@loop
            }
        }

        return 0
    }

    private fun attackPlayer() {


        val enemyList: MutableList<Entity> = mutableListOf()
        var a = 0
        for (enemy in enemies) {
            if (player.distanceFromEntity(enemy) < player.getAttackRange()) {
                println("$a. enemy at position: ${enemy.position.xPosition} , ${enemy.position.yPosition} with health: ${enemy.stats.health}")
                enemyList.add(enemy)
                a++
            }
        }

        run loop@{
            if (a > 0) {
                println("choose an enemy to attack")
                val value = readLine()!!
                val attackedEnemy: Entity
                try {
                    if (value.toInt() in 0 until a) {
                        println("wrong number: retry")
                        return@loop
                    }

                    attackedEnemy = enemyList[value.toInt()]
                } catch (_: NumberFormatException) {
                    println("wrong format: retry")
                    return@loop
                }

                player.attack(attackedEnemy)
                player.stats.priority -= 50
            } else {
                println("there are no enemy that the player can attack")
            }
        }
    }

    private fun movePlayer() {
        var xCoordinate: Int = 0
        var yCoordinate: Int = 0

        run loop@{
            print("input coordinate: ")
            val buffer = Scanner(System.`in`)

            try {
                xCoordinate = buffer.nextInt()
                yCoordinate = buffer.nextInt()
            } catch (e: Exception) {
                println("wrong format: retry")
                return@loop
            }
            if (xCoordinate + yCoordinate > player.getMaxTilePerTurn()) {
                println("coordinates too far must be less than ${player.getMaxTilePerTurn()}")
                return@loop
            }
        }

        player.move(terrain.getTile(player.position.xPosition + xCoordinate, player.position.yPosition + yCoordinate))
        player.stats.priority -= 30 * (xCoordinate + yCoordinate)
    }

    private fun gameOver() = player.stats.health <= 0
}