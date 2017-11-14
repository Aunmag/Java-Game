package aunmag.shooter.scenarios

import aunmag.shooter.world.World

open class Scenario(val world: World) {

    private var isRemoved = false
        private set

    open fun update() {}

    open fun render() {}

    open fun remove() {
        isRemoved = true
    }

}
