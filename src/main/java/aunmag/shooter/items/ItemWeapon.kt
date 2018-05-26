package aunmag.shooter.items

import aunmag.nightingale.font.FontStyleDefault
import aunmag.nightingale.font.Text
import aunmag.nightingale.input.Input
import aunmag.nightingale.math.CollisionCC
import aunmag.nightingale.math.BodyCircle
import aunmag.nightingale.utilities.FluidValue
import aunmag.nightingale.utilities.Operative
import aunmag.nightingale.utilities.Timer
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.environment.actor.Actor
import aunmag.shooter.data.player
import aunmag.shooter.environment.weapon.Weapon
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class ItemWeapon private constructor(
        x: Float,
        y: Float,
        val weapon: Weapon,
        private var giver: Actor? = null
) : Operative() {

    constructor(x: Float, y: Float, weapon: Weapon) : this(x, y, weapon, null)
    constructor(giver: Actor, weapon: Weapon) : this(
            giver.body.position.x,
            giver.body.position.y,
            weapon,
            giver
    )

    val body = BodyCircle(x, y, 0f, 0f)
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
            body.position.x = it.body.position.x
            body.position.y = it.body.position.y
            text.position.x = it.body.position.x
            text.position.y = it.body.position.y
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

        body.color.set(0f, 0f, 0f, alpha)
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

        body.radius = pulse.current
    }

    private fun updatePickup() {
        val player: Actor = player ?: return
        val collision = CollisionCC(body, player.body)

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_E) && collision.isTrue) {
            player.weapon?.let {
                val replacement = ItemWeapon(body.position.x, body.position.y, it)
                player.world.itemsWeapon.all.add(replacement)
            }

            player.weapon = weapon
            remove()
        }
    }

    override fun render() {
        if (giver == null) {
            body.render()
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
