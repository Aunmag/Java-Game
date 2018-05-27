package aunmag.shooter.client.states

import aunmag.nightingale.input.Input
import aunmag.nightingale.utilities.UtilsGraphics
import aunmag.shooter.client.App
import aunmag.shooter.client.Player
import aunmag.shooter.client.graphics.CameraShaker
import aunmag.shooter.client.graphics.Crosshair
import aunmag.shooter.scenarios.ScenarioEncircling
import aunmag.shooter.environment.World
import aunmag.shooter.ux.Hud
import org.lwjgl.glfw.GLFW

class Game {

    val world = World()
    val player = Player(world)
    private val scenario = ScenarioEncircling(world)
    private val crosshair = Crosshair(player.actor) // TODO: Change implementation
    private val hud = Hud()

    fun resume() {
        world.playSounds()
    }

    fun suspend() {
        world.stopSounds()
    }

    fun update() {
        player.updateInput()
        world.update()
        scenario.update()
        player.updateCameraPosition()
        CameraShaker.update()

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            App.main.isPause = true
        }

        hud.update()
    }

    fun render() {
        world.render()
        player.renderUx()
        UtilsGraphics.drawPrepare()
        crosshair.render()
        scenario.render()
        hud.render()
    }

    fun remove() {
        world.remove()
        scenario.remove()
        hud.remove()
    }

}
