package aunmag.shooter.scenarios

import aunmag.nightingale.Application
import aunmag.nightingale.font.Font
import aunmag.nightingale.gui.GuiButton
import aunmag.nightingale.gui.GuiButtonBack
import aunmag.nightingale.gui.GuiLabel
import aunmag.nightingale.gui.GuiPage
import aunmag.nightingale.structures.Texture
import aunmag.nightingale.utilities.TimerDone
import aunmag.nightingale.utilities.TimerNext
import aunmag.nightingale.utilities.UtilsGraphics
import aunmag.nightingale.utilities.UtilsMath
import aunmag.nightingale.utilities.UtilsMath.limitNumber
import aunmag.shooter.actor.Actor
import aunmag.shooter.actor.ActorType
import aunmag.shooter.ai.Ai
import aunmag.shooter.client.Game
import aunmag.shooter.data.soundGameOver
import aunmag.shooter.world.World
import org.lwjgl.opengl.GL11

class ScenarioEncircling(world: World) : Scenario(world) {

    private val bordersDistance = 32f
    private var wave = 0
    private val waveFinal = 8
    private val zombiesQuantityInitial = 32
    private var zombiesQuantityToSpawn = 0
    private val zombiesSpawnTimer = TimerNext(0.5)
    private val notificationTimer = TimerDone(3.0)
    private var notificationWave: GuiLabel? = null
    private var notificationKills: GuiLabel? = null

    init {
        startNextWave()
    }

    override fun update() {
        if (getPlayer()?.isAlive != true) {
            gameOver(false)
            return
        }

        confinePlayerPosition()

        if (zombiesQuantityToSpawn > 0) {
            zombiesSpawnTimer.update(world.time.current)
            if (zombiesSpawnTimer.isNow) {
                spawnZombie()
            }
        } else if (world.actors.size == 1) { // TODO: Improve
            startNextWave()
        }
    }

    override fun render() {
        renderBorders()
        renderNotifications()
    }

    // TODO: Clean
    private fun renderBorders() {
        GL11.glLineWidth(2f)
        GL11.glColor3f(1f, 0f, 0f)
        UtilsGraphics.drawPrepare()

        val n = bordersDistance
        UtilsGraphics.drawLine(-n, -n, +n, -n, true)
        UtilsGraphics.drawLine(+n, -n, +n, +n, true)
        UtilsGraphics.drawLine(+n, +n, -n, +n, true)
        UtilsGraphics.drawLine(-n, +n, -n, -n, true)

        GL11.glLineWidth(1f)
    }

    private fun renderNotifications() {
        if (notificationTimer.calculateIsDone(world.time.current)) {
            removeNotifications()
        } else {
            notificationWave?.render()
            notificationKills?.render()
        }
    }

    override fun remove() {
        removeNotifications()
        super.remove()
    }

    private fun startNextWave() {
        if (wave == waveFinal) {
            gameOver(true)
            return
        }

        wave++
        zombiesQuantityToSpawn = zombiesQuantityInitial * wave
        createNotifications()
    }

    private fun confinePlayerPosition() {
        val player = getPlayer() ?: return
        player.x = limitNumber(player.x, -bordersDistance, bordersDistance)
        player.y = limitNumber(player.y, -bordersDistance, bordersDistance)
    }

    private fun spawnZombie() {
        val distance = Application.getCamera().distanceView / 2f
        val direction = UtilsMath.randomizeBetween(0f, UtilsMath.PIx2.toFloat())

        val centerX = getPlayer()?.x ?: 0f
        val centerY = getPlayer()?.y ?: 0f
        val x = centerX - distance * Math.cos(direction.toDouble()).toFloat()
        val y = centerY - distance * Math.sin(direction.toDouble()).toFloat()

        val zombie = Actor(ActorType.zombieEasy) // TODO: Spawn different types of zombies
        zombie.setPosition(x, y)
        zombie.radians = -direction
        world.actors.add(zombie)
        world.ais.add(Ai(zombie))

        zombiesQuantityToSpawn--
    }

    private fun createNotifications() {
        removeNotifications()

        val messageWave = "Wave $wave/$waveFinal"
        notificationWave = GuiLabel(5, 4, 2, 1, messageWave)

        val messageKills = "Kill $zombiesQuantityToSpawn zombies"
        notificationKills = GuiLabel(5, 5, 2, 1, messageKills, Font.fontDefault, 1f)

        notificationTimer.timeInitial = world.time.current
    }

    private fun removeNotifications() {
        notificationWave?.delete()
        notificationKills?.delete()

        notificationWave = null
        notificationKills = null
    }

    private fun gameOver(isVictory: Boolean) {
        createGameOverPage(isVictory)
        Game.deleteWorld()

        if (!isVictory) {
            soundGameOver.play()
        }
    }

    // TODO: Clean
    private fun createGameOverPage(isVictory: Boolean) {
        val kills = getPlayer()?.kills ?: 0
        val wavesSurvived = if (isVictory) wave else wave - 1
        val title = if (isVictory) "Well done!" else "You have died"
        val score = "You killed $kills zombies and survived $wavesSurvived/$waveFinal waves."

        val labels = arrayOf(
                GuiLabel(4, 3, 4, 1, title),
                GuiLabel(4, 4, 4, 1, score, Font.fontDefault, 1f)
        )

        val buttons = arrayOf<GuiButton>(GuiButtonBack(4, 9, 4, 1, "Back to main menu"))

        val wallpaper = Texture.getOrCreate(
                if (isVictory) "images/wallpapers/victory" else "images/wallpapers/death",
                true,
                false
        )
        wallpaper.scaleAsWallpaper()

        GuiPage(labels, buttons, wallpaper).open()
        Game.setPause(true)
    }

    private fun getPlayer(): Actor? {
        return Actor.getPlayer()
    }

}
