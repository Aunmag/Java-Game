package aunmag.shooter.scenarios

import aunmag.nightingale.Application
import aunmag.nightingale.font.FontStyleDefault
import aunmag.nightingale.gui.GuiButton
import aunmag.nightingale.gui.GuiLabel
import aunmag.nightingale.gui.GuiPage
import aunmag.nightingale.structures.Texture
import aunmag.nightingale.utilities.Timer
import aunmag.nightingale.utilities.UtilsGraphics
import aunmag.nightingale.utilities.UtilsMath
import aunmag.nightingale.utilities.UtilsMath.limitNumber
import aunmag.shooter.environment.actor.Actor
import aunmag.shooter.environment.actor.ActorType
import aunmag.shooter.ai.Ai
import aunmag.shooter.client.App
import aunmag.shooter.data.soundGameOver
import aunmag.shooter.items.ItemWeapon
import aunmag.shooter.data.player
import aunmag.shooter.environment.World
import aunmag.shooter.environment.decorations.Ground
import aunmag.shooter.environment.decorations.GroundType
import aunmag.shooter.environment.weapon.Weapon
import aunmag.shooter.environment.weapon.WeaponType
import org.lwjgl.opengl.GL11

class ScenarioEncircling(world: World) : Scenario(world) {

    private val bordersDistance = 32f
    private var wave = 0
    private val waveFinal = 6
    private val zombiesQuantityInitial = 5
    private var zombiesQuantityToSpawn = 0
    private val zombiesSpawnTimer = Timer(world.time, 0.5)
    private var bonusDropChance = 0f

    init {
        initializeBluffs()
        startNextWave()
    }

    private fun initializeBluffs() {
        val ground = world.ground.all
        val quantity = bordersDistance / 2 + 1
        val step = 4
        val length = step * quantity
        val first = length / -2f + step / 2f
        val last = first + length - step

        val a = Math.PI.toFloat()
        val b = 0f
        val c = UtilsMath.PIx0_5.toFloat()
        val d = UtilsMath.PIx1_5.toFloat()

        var i = first + step
        while (i <= last - step) {
            ground.add(Ground(GroundType.bluff, i, first, a))
            ground.add(Ground(GroundType.bluff, i, last, b))
            ground.add(Ground(GroundType.bluff, first, i, c))
            ground.add(Ground(GroundType.bluff, last, i, d))
            i += step.toFloat()
        }

        ground.add(Ground(GroundType.bluffCorner, first, first, a))
        ground.add(Ground(GroundType.bluffCorner, last, last, b))
        ground.add(Ground(GroundType.bluffCorner, first, last, c))
        ground.add(Ground(GroundType.bluffCorner, last, first, d))
    }

    override fun update() {
        if (player?.isAlive != true) {
            gameOver(false)
            return
        }

        confinePlayerPosition()

        val areAllZombiesSpawned = zombiesQuantityToSpawn == 0
        if (areAllZombiesSpawned && world.actors.all.size == 1) {
            startNextWave()
        } else if (!areAllZombiesSpawned && zombiesSpawnTimer.isDone) {
            spawnZombie()
            zombiesSpawnTimer.next()
        }
    }

    override fun render() {
        if (App.main.isDebug) {
            renderBorders()
        }
    }

    private fun renderBorders() {
        val n = bordersDistance
        GL11.glLineWidth(2f)
        GL11.glColor3f(1f, 0f, 0f)
        UtilsGraphics.drawLine(-n, -n, +n, -n, true)
        UtilsGraphics.drawLine(+n, -n, +n, +n, true)
        UtilsGraphics.drawLine(+n, +n, -n, +n, true)
        UtilsGraphics.drawLine(-n, +n, -n, -n, true)
        GL11.glLineWidth(1f)
    }

    private fun startNextWave() {
        if (wave == waveFinal) {
            gameOver(true)
            return
        }

        wave++
        zombiesQuantityToSpawn = wave * wave * zombiesQuantityInitial
        bonusDropChance = wave * 1.2f / zombiesQuantityToSpawn

        world.notifications.add(
                "Wave $wave/$waveFinal",
                "Kill $zombiesQuantityToSpawn zombies"
        )
    }

    private fun confinePlayerPosition() {
        val position = player?.body?.position ?: return
        position.x = limitNumber(position.x, -bordersDistance, bordersDistance)
        position.y = limitNumber(position.y, -bordersDistance, bordersDistance)
    }

    private fun spawnZombie() {
        val distance = Application.getCamera().distanceView / 2f
        val direction = UtilsMath.randomizeBetween(0f, UtilsMath.PIx2.toFloat())

        val centerX = player?.body?.position?.x ?: 0f
        val centerY = player?.body?.position?.y ?: 0f
        val x = centerX - distance * Math.cos(direction.toDouble()).toFloat()
        val y = centerY - distance * Math.sin(direction.toDouble()).toFloat()

        // TODO: Spawn different types of zombies:
        val zombie = Actor(ActorType.zombieEasy, world, x, y, -direction)
        world.actors.all.add(zombie)
        world.ais.all.add(Ai(zombie))

        if (UtilsMath.random.nextFloat() < bonusDropChance) {
            createWeaponBonus(zombie)
        }

        zombiesQuantityToSpawn--
    }

    private fun createWeaponBonus(giver: Actor) {
        val indexMax = UtilsMath.limitNumber(wave.toFloat(), 1f, 5f).toInt()
        val index: Int = UtilsMath.randomizeBetween(1, indexMax)
        val weapon = Weapon(
                world,
                when (index) {
                    1 -> WeaponType.makarovPistol
                    2 -> WeaponType.mp27
                    3 -> WeaponType.aks74u
                    4 -> WeaponType.pkpPecheneg
                    5 -> WeaponType.saiga12k
                    else -> WeaponType.laserGun
                }
        )

        world.itemsWeapon.all.add(ItemWeapon(giver, weapon))
    }

    private fun gameOver(isVictory: Boolean) {
        createGameOverPage(isVictory)
        App.main.endGame()

        if (!isVictory) {
            soundGameOver.play()
        }
    }

    private fun createGameOverPage(isVictory: Boolean) {
        val wallpaperName = if (isVictory) "victory" else "death"
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/$wallpaperName", Texture.Type.WALLPAPER
        )
        val page = GuiPage(wallpaper)

        val title = if (isVictory) "Well done!" else "You have died"
        val kills = player?.kills ?: 0
        val wavesSurvived = if (isVictory) wave else wave - 1
        val score = "You killed $kills zombies and survived $wavesSurvived/$waveFinal waves."

        page.add(GuiLabel(4, 3, 4, 1, title))
        page.add(GuiLabel(4, 4, 4, 1, score, FontStyleDefault.labelLight))
        page.add(GuiButton(4, 9, 4, 1, "Back to main menu", GuiButton.actionBack))

        page.open()
        App.main.isPause = true
    }

}
