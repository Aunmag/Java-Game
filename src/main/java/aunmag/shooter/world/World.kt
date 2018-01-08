package aunmag.shooter.world

import aunmag.nightingale.Application
import aunmag.nightingale.utilities.TimeFlow
import aunmag.nightingale.utilities.UtilsBaseOperative
import aunmag.nightingale.utilities.UtilsGraphics
import aunmag.shooter.actor.Actor
import aunmag.shooter.ai.Ai
import aunmag.shooter.client.graphics.WorldGrid
import aunmag.shooter.data.soundAmbiance
import aunmag.shooter.data.soundAtmosphere
import aunmag.shooter.gui.NotificationLayer
import aunmag.shooter.items.ItemWeapon
import aunmag.shooter.weapon.Projectile
import org.lwjgl.opengl.GL11
import java.util.ArrayList

class World {

    private val grid = WorldGrid() // TODO: Change this class
    val time = TimeFlow()
    val ais: MutableList<Ai> = ArrayList()
    val actors: MutableList<Actor> = ArrayList()
    val projectiles: MutableList<Projectile> = ArrayList()
    val notifications = NotificationLayer(time)
    val itemsWeapon: MutableList<ItemWeapon> = ArrayList()

    fun update() {
        time.add(Application.time.delta, true)
        UtilsBaseOperative.updateAll(ais)
        UtilsBaseOperative.updateAll(actors)
        Actor.finalizeUpdate() // TODO: Get rid of this
        UtilsBaseOperative.updateAll(projectiles)
        updateItemsWeapon()
        notifications.update()
    }

    private fun updateItemsWeapon() {
        for (index in itemsWeapon.size - 1 downTo 0) {
            val item = itemsWeapon[index]

            item.update()

            if (item.isRemoved) {
                itemsWeapon.removeAt(index)
            }
        }
    }

    // TODO: Optimize draw modes
    fun render() {
        UtilsGraphics.drawPrepare()
        grid.render()
        UtilsBaseOperative.renderAll(itemsWeapon)
        UtilsBaseOperative.renderAll(actors)
        UtilsGraphics.drawPrepare()
        UtilsBaseOperative.renderAll(projectiles)
        GL11.glLineWidth(1f)
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
        UtilsBaseOperative.renderAll(itemsWeapon)
        stopSounds()
    }

}
