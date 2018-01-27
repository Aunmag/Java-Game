package aunmag.shooter.scenarios

import aunmag.shooter.environment.World

open class Scenario(val world: World) {

    private var isRemoved = false

    open fun update() {}

    open fun render() {}

    open fun remove() {
        isRemoved = true
    }

}
