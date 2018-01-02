package aunmag.shooter.utils

import aunmag.shooter.actor.Actor
import aunmag.shooter.client.App

val player: Actor?
    get() = App.main.game?.player?.actor
