package aunmag.shooter.client.states

import aunmag.nightingale.input.Input
import aunmag.shooter.client.App
import aunmag.shooter.client.Player
import aunmag.shooter.client.graphics.CameraShaker
import aunmag.shooter.client.graphics.Crosshair
import aunmag.shooter.client.graphics.Hud
import aunmag.shooter.scenarios.ScenarioEncircling
import aunmag.shooter.environment.World
import org.lwjgl.glfw.GLFW

class Game {

    val world = World()
    val player = Player(world)
    private val scenario = ScenarioEncircling(world)
    private val crosshair = Crosshair(player.actor) // TODO: Change implementation

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
        CameraShaker.update()
        player.updateCameraPosition()

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            App.main.isPause = true
        }
    }

    fun render() {
        world.render()
        player.renderUx()
        crosshair.render()

        if (player.actor.hasWeapon) {
            player.actor.weapon.magazine.renderHud()
        }

        scenario.render()
        Hud.render()
    }

    fun remove() {
        world.remove()
        scenario.remove()
    }

}
