package aunmag.shooter.data

import aunmag.shooter.environment.actor.Actor
import aunmag.shooter.client.App

val player: Actor?
    get() = App.main.game?.player?.actor
