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
        actor.addRadiansCarefully(Input.mouse.velocityX * mouseSensitivity)
    }

    private fun updateInputForCamera() {
        val camera = Application.getCamera()
        val delta = Application.time.delta.toFloat()

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            camera.scaleZoom += camera.scaleZoom * delta
        } else if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            camera.scaleZoom -= camera.scaleZoom * delta
        }

        camera.scaleZoom += Input.mouse.wheel.velocitySmooth * delta
    }

    fun updateCameraPosition() {
        val camera = Application.getCamera()
        camera.setPosition(actor.x, actor.y)
        camera.radians = actor.radians

        val offset = Application.getWindow().centerY / 2f * (1f + actor.isAiming.current)
        camera.addOffset(0f, offset, true)
    }

    fun renderUx() {
        blackout.render()
    }

}
