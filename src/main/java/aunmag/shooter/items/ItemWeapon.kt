package aunmag.shooter.items

import aunmag.nightingale.collision.Collision
import aunmag.nightingale.collision.CollisionCircle
import aunmag.nightingale.font.FontStyleDefault
import aunmag.nightingale.font.Text
import aunmag.nightingale.input.Input
import aunmag.nightingale.utilities.FluidValue
import aunmag.nightingale.utilities.Timer
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.environment.actor.Actor
import aunmag.shooter.data.player
import aunmag.shooter.environment.weapon.Weapon
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class ItemWeapon private constructor(
        x: Float,
        y: Float,
        val weapon: Weapon,
        private var giver: Actor? = null
) : CollisionCircle(Vector2f(x, y), 0f) {

    constructor(x: Float, y: Float, weapon: Weapon) : this(x, y, weapon, null)
    constructor(giver: Actor, weapon: Weapon) : this(
            giver.position.x,
            giver.position.y,
            weapon,
            giver
    )

    private val timer = Timer(weapon.world.time, 15.0)
    private val pulse = FluidValue(weapon.world.time, 0.4)
    private val pulseMin = 0.12f
    private val pulseMax = 0.18f
    private val text = Text(x, y, weapon.type.name, FontStyleDefault.simple)

    init {
        text.setOnWorldRendering(true)
        timer.next()
    }

    private fun drop() {
        giver?.let {
            position.x = it.position.x
            position.y = it.position.y
            text.position.x = it.position.x
            text.position.y = it.position.y
        }

        timer.next()
        giver = null
    }

    override fun update() {
        val owner = this.giver
        if (owner == null) {
            if (timer.isDone) {
                remove()
            } else {
                updateColor()
                updateRadius()
                updatePickup()
            }
        } else if (!owner.isAlive || owner.isRemoved) {
            drop()
        }
    }

    private fun updateColor() {
        val alpha = UtilsMath.limitNumber(
                4.0f * (1.0 - timer.calculateIsDoneRatio()).toFloat(),
                0.0f,
                0.8f
        )

        color.set(0f, 0f, 0f, alpha)
        text.setColour(Vector4f(1f, 1f, 1f, alpha))
    }

    private fun updateRadius() {
        pulse.update()

        if (pulse.isTargetReached) {
            pulse.target = if (pulse.target == pulseMin) {
                pulseMax
            } else {
                pulseMin
            }
        }

        radius = pulse.current
    }

    private fun updatePickup() {
        val player: Actor = player ?: return

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_E)
                && Collision.calculateIsCollision(this, player.hands)) {

            player.weapon?.let {
                player.world.itemsWeapon.add(ItemWeapon(position.x, position.y, it))
            }

            player.weapon = weapon
            remove()
        }
    }

    override fun render() {
        if (giver == null) {
            super.render()
            text.orderRendering()
        }
    }

    override fun remove() {
        if (isRemoved) {
            return
        }

        text.remove()
        super.remove()
    }

}
