package aunmag.shooter.client

import aunmag.nightingale.Application
import aunmag.nightingale.input.Input
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.actor.Actor
import aunmag.shooter.actor.ActorType
import aunmag.shooter.weapon.WeaponFactory
import aunmag.shooter.world.World
import org.lwjgl.glfw.GLFW

class Player(world: World) {

    val actor = Actor(ActorType.human, world)
    private val laserGun = WeaponFactory.laserGun(world)
    private val makarovPistol = WeaponFactory.makarovPistol(world)
    private val mp27 = WeaponFactory.mp27(world)
    private val aks74u = WeaponFactory.aks74u(world)
    private val pecheneg = WeaponFactory.pecheneg(world)
    private val saiga12k = WeaponFactory.saiga12k(world)

    init {
        actor.radians = -UtilsMath.PIx0_5.toFloat()
        actor.weapon = mp27
        world.actors.add(actor)
    }

    fun updateInput() {
        updateInputForActions()
        updateInputForRotation()
        updateInputForWeapon()
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
    }

    private fun updateInputForRotation() {
        val mouseSensitivity = 0.005f * (1f - actor.isAiming.current * 0.75f)
        actor.addRadiansCarefully(Input.mouse.velocityX * mouseSensitivity)
    }

    private fun updateInputForWeapon() {
        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_R) && actor.hasWeapon) {
            actor.weapon.magazine.reload()
        }

        when (true) {
            Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_0) -> actor.weapon = laserGun
            Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_1) -> actor.weapon = makarovPistol
            Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_2) -> actor.weapon = mp27
            Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_3) -> actor.weapon = aks74u
            Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_4) -> actor.weapon = pecheneg
            Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_5) -> actor.weapon = saiga12k
        }
    }

    private fun updateInputForCamera() {
        val camera = Application.getCamera()
        val zoomChange = camera.scaleZoom * 0.01f

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            camera.scaleZoom += zoomChange
        } else if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            camera.scaleZoom -= zoomChange
        }
    }

    fun updateCameraPosition() {
        val camera = Application.getCamera()
        camera.setPosition(actor.x, actor.y)
        camera.radians = actor.radians

        val offset = Application.getWindow().centerY / 2f * (1f + actor.isAiming.current)
        camera.addOffset(0f, offset, true)
    }

}
