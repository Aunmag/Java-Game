package aunmag.shooter.world

import aunmag.nightingale.data.DataTime

class WorldTime {

    var currentMilliseconds = 0L
        private set
    var current = 0.0
        private set
    var delta = 0.0
        private set
    var speed = 1.0

    internal fun update() {
        delta = DataTime.getTimeDelta() * speed
        current += delta
        currentMilliseconds = (current * 1000).toLong()
    }

}
