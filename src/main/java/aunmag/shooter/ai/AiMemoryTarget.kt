package aunmag.shooter.ai

import aunmag.shooter.environment.actor.Actor

class AiMemoryTarget : AiMemory() {

    var actor: Actor? = null
    var distance = 0f
    var direction = 0f
    var radiansDifference = 0f
    var radiansDifferenceAbsolute = 0f
    var isReached = false

    init {
        forget()
    }

    override fun forget() {
        actor = null
        isReached = false
    }

    override fun isInMemory() = actor != null

}
