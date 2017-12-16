package aunmag.shooter.world

import aunmag.nightingale.utilities.TimeFlow
import aunmag.nightingale.utilities.UtilsBaseOperative
import aunmag.nightingale.utilities.UtilsGraphics
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.actor.Actor
import aunmag.shooter.actor.ActorType
import aunmag.shooter.ai.Ai
import aunmag.shooter.client.graphics.WorldGrid
import aunmag.shooter.data.soundAmbiance
import aunmag.shooter.data.soundAtmosphere
import aunmag.shooter.gui.NotificationLayer
import aunmag.shooter.weapon.Projectile
import aunmag.shooter.weapon.WeaponFactory
import org.lwjgl.opengl.GL11
import java.util.ArrayList

class World {

    private val grid = WorldGrid() // TODO: Change this class
    val time = TimeFlow()
    val ais: MutableList<Ai> = ArrayList()
    val actors: MutableList<Actor> = ArrayList()
    val projectiles: MutableList<Projectile> = ArrayList()
    val notifications: NotificationLayer

    // TODO: World shouldn't know about player's weapons
    val laserGun = WeaponFactory.laserGun(this)
    val makarovPistol = WeaponFactory.makarovPistol(this)
    val mp27 = WeaponFactory.mp27(this)
    val aks74u = WeaponFactory.aks74u(this)
    val pecheneg = WeaponFactory.pecheneg(this)
    val saiga12k = WeaponFactory.saiga12k(this)

    init {
        initializePlayer() // TODO: World should not know about client's player
        notifications = NotificationLayer(time)
    }

    private fun initializePlayer() {
        val player = Actor(ActorType.human, this)
        player.radians = -UtilsMath.PIx0_5.toFloat()
        player.weapon = mp27

        Actor.setPlayer(player)
        actors.add(player)
    }

    fun update() {
        time.update()
        UtilsBaseOperative.updateAll(ais)
        UtilsBaseOperative.updateAll(actors)
        Actor.finalizeUpdate() // TODO: Get rid of this
        UtilsBaseOperative.updateAll(projectiles)
        notifications.update()
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
        notifications.render()
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
        notifications.clear()
        UtilsBaseOperative.removeAll(ais)
        UtilsBaseOperative.removeAll(actors)
        UtilsBaseOperative.removeAll(projectiles)
        stopSounds()
    }

}
