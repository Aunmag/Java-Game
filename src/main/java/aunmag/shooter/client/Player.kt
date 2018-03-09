package aunmag.shooter.client

import aunmag.nightingale.Application
import aunmag.nightingale.input.Input
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.environment.actor.Actor
import aunmag.shooter.environment.actor.ActorType
import aunmag.shooter.ux.Blackout
import aunmag.shooter.environment.World
import aunmag.shooter.environment.weapon.Weapon
import aunmag.shooter.environment.weapon.WeaponType
import org.lwjgl.glfw.GLFW

class Player(world: World) {

    val actor = Actor(ActorType.human, world)
    private val blackout = Blackout(actor)

    init {
        actor.radians = -UtilsMath.PIx0_5.toFloat()
        actor.weapon = Weapon(world, WeaponType.makarovPistol)
        world.actors.add(actor)
        App.getCamera().mount.holder = actor.position
    }

    fun updateInput() {
        updateInputForActions()
        updateInputForRotation()
        updateInputForCamera()
    }

    private fun updateInputForActions() {
        actor.isWalkingForward = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_W)
        actor.isWalkingBack = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_S)
        actor.isWalkingLeft = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_A)
        actor.isWalkingRight = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_D)
        actor.isSprinting = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)
        actor.isAttacking = Input.mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)

        if (Input.mouse.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
            actor.isAiming.toggle()
        }

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_R) && actor.hasWeapon) {
            actor.weapon.magazine.reload()
        }
    }

    private fun updateInputForRotation() {
        val mouseSensitivity = 0.005f * (1f - actor.isAiming.current * 0.75f)
        actor.radians += Input.mouse.velocityX * mouseSensitivity
        actor.correctRadians()
    }

    private fun updateInputForCamera() {
        var zoom = Input.mouse.wheel.velocitySmooth

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            zoom += 1.0f
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            zoom -= 1.0f
        }

        if (zoom != 0.0f) {
            val camera = Application.getCamera()
            camera.scaleZoom += zoom * camera.scaleZoom * Application.time.delta.toFloat()
        }
    }

    fun updateCameraPosition() {
        val camera = App.getCamera()
        val window = App.getWindow()
        val offsetMin = window.centerY / 2.0f / camera.scaleFull
        val offset = offsetMin * (1.0f + actor.isAiming.current)

        camera.radians = actor.radians
        camera.mount.length = offset
        camera.mount.radians = actor.radians
        camera.mount.apply()
    }

    fun renderUx() {
        blackout.render()
    }

}
