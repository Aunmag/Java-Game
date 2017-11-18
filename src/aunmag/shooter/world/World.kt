package aunmag.shooter.world

import aunmag.nightingale.utilities.UtilsBaseOperative
import aunmag.nightingale.utilities.UtilsGraphics
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.actor.Actor
import aunmag.shooter.actor.ActorType
import aunmag.shooter.ai.Ai
import aunmag.shooter.client.graphics.WorldGrid
import aunmag.shooter.data.soundAmbiance
import aunmag.shooter.data.soundAtmosphere
import aunmag.shooter.weapon.Projectile
import aunmag.shooter.weapon.WeaponFactory
import org.lwjgl.opengl.GL11
import java.util.ArrayList

class World {

    private val grid = WorldGrid() // TODO: Change this class
    val time = WorldTime()
    val ais: MutableList<Ai> = ArrayList()
    val actors: MutableList<Actor> = ArrayList()
    val projectiles: MutableList<Projectile> = ArrayList()

    init {
        initializePlayer() // TODO: World should not know about client's player
    }

    private fun initializePlayer() {
        val player = Actor(ActorType.human)
        player.radians = -UtilsMath.PIx0_5.toFloat()
        player.weapon = WeaponFactory.mp27()

        Actor.setPlayer(player)
        actors.add(player)
    }

    fun update() {
        time.update()
        UtilsBaseOperative.updateAll(ais)
        UtilsBaseOperative.updateAll(actors)
        Actor.finalizeUpdate() // TODO: Get rid of this
        UtilsBaseOperative.updateAll(projectiles)
    }

    // TODO: Optimize draw modes
    fun render() {
        UtilsGraphics.drawPrepare()
        grid.render()
        UtilsBaseOperative.renderAll(actors)
        UtilsGraphics.drawPrepare()
        UtilsBaseOperative.renderAll(projectiles)
        GL11.glLineWidth(1f)
        UtilsGraphics.drawFinish()
    }

    fun playSounds() {
        soundAmbiance.play()
        soundAtmosphere.play()
    }

    fun stopSounds() {
        soundAmbiance.stop()
        soundAtmosphere.stop()
    }

    fun remove() {
        UtilsBaseOperative.removeAll(ais)
        UtilsBaseOperative.removeAll(actors)
        UtilsBaseOperative.removeAll(projectiles)
        stopSounds()
    }

}