package aunmag.shooter.environment.magazine

import aunmag.shooter.environment.projectile.ProjectileType

class MagazineType(
        val projectile: ProjectileType,
        val isAutomatic: Boolean,
        val capacity: Int,
        val timeReloading: Float
) {
    val isUnlimited = capacity == 0
}
