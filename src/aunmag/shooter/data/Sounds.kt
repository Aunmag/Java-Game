package aunmag.shooter.data

import aunmag.nightingale.audio.AudioSource
import aunmag.nightingale.utilities.UtilsAudio

val soundGameOver = initializeSoundGameOver()

private fun initializeSoundGameOver() : AudioSource {
    val soundGameOver = UtilsAudio.getOrCreateSoundOgg("sounds/music/death")
    soundGameOver.setVolume(0.6f)
    return soundGameOver
}
