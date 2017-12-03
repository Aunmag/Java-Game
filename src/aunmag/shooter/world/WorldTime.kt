package aunmag.shooter.world

import aunmag.nightingale.data.DataTime

class WorldTime {

    var current = 0.0
        private set
    var delta = 0.0
        private set
    var speed = 1.0

    internal fun update() {
        delta = DataTime.getDelta() * speed
        current += delta
    }

}
