package aunmag.shooter.scenarios

import aunmag.nightingale.Application
import aunmag.nightingale.utilities.UtilsMath
import aunmag.shooter.client.App

class ScenarioPause {

    private var direction = 0f
    private val velocity = 0.1f

    init {
        refresh()
    }

    fun refresh() {
        val camera = Application.getCamera()
        camera.position.set(0f, 0f)
        camera.radians = UtilsMath.randomizeBetween(0f, UtilsMath.PIx2.toFloat())
        direction = UtilsMath.randomizeBetween(0f, UtilsMath.PIx2.toFloat())
    }

    fun update() {
        val camera = Application.getCamera()
        val velocity = this.velocity * App.main.pause.world.time.delta.toFloat()
        camera.position.x += velocity * Math.cos(direction.toDouble()).toFloat()
        camera.position.y += velocity * Math.sin(direction.toDouble()).toFloat()
    }

}
