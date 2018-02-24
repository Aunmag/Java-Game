package aunmag.shooter.ux

import aunmag.nightingale.font.FontStyleDefault
import aunmag.nightingale.font.Text
import aunmag.nightingale.input.Input
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.client.App
import aunmag.shooter.data.player
import aunmag.shooter.gui.Parameter
import org.lwjgl.glfw.GLFW

class Hud {

    private val health = Parameter("Health", 0.0f, 30, 32)
    private val stamina = Parameter("Stamina", 0.0f, 30, 33)
    private val ammo = Parameter("Ammo", 0.0f, 30, 34)
    private val debug = Text(10f, 10f, "", FontStyleDefault.simple)

    fun update() {
        Parameter.update()
        health.value = player?.health ?: 0f
        stamina.value = player?.stamina?.current ?: 0f
        ammo.value = player?.weapon?.magazine?.calculateVolumeRatio() ?: 0f
        ammo.isPulsing = player?.weapon?.magazine?.isReloading ?: false
    }

    fun render() {
        health.render()
        stamina.render()
        ammo.render()

        if (App.main.isDebug) {
            renderDebug()
        }
    }

    private fun renderDebug() {
        val game = App.main.game ?: return
        val world = game.world

        var timeSpentUpdate = 0f // TODO: Invoke data
        var timeSpentRender = 0f // TODO: Invoke data
        var timeSpentTotal = timeSpentUpdate + timeSpentRender
        val round = 100f
        timeSpentUpdate = UtilsMath.calculateRoundValue(timeSpentUpdate, round)
        timeSpentRender = UtilsMath.calculateRoundValue(timeSpentRender, round)
        timeSpentTotal = UtilsMath.calculateRoundValue(timeSpentTotal, round)

        var message = ""
        message += String.format("Spent time on updating: %s ms\n", timeSpentUpdate)
        message += String.format("Spent time on rendering: %s ms\n", timeSpentRender)
        message += String.format("Spent time total: %s ms \n", timeSpentTotal)
        message += String.format("\nAIs: %s", world.ais.size)
        message += String.format("\nActors: %s", world.actors.size)
        message += String.format("\nBullets: %s", world.projectiles.size)

        debug.load(message)
        debug.orderRendering()
    }

    fun remove() {
        health.remove()
        stamina.remove()
        ammo.remove()
        debug.remove()
    }

}