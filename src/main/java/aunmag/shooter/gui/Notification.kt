package aunmag.shooter.gui

import aunmag.nightingale.basics.BaseOperative
import aunmag.nightingale.font.FontStyleDefault
import aunmag.nightingale.gui.GuiLabel
import aunmag.nightingale.utilities.TimeFlow
import aunmag.nightingale.utilities.Timer
import aunmag.nightingale.utilities.UtilsMath

internal class Notification(
        time: TimeFlow,
        title: String,
        details: String
) : BaseOperative {

    private var isRemoved = false
    private val timeFadeIn = 0.125f
    private val timeFadeOut = 0.5f
    private val timer = Timer(time, 3.0)
    private val title = GuiLabel(5, 4, 2, 1, title)
    private val details = GuiLabel(5, 5, 2, 1, details, FontStyleDefault.labelLight)

    init {
        timer.next()
    }

    override fun update() {
        if (timer.isDone) {
            remove()
        }
    }

    override fun render() {
        val timeInitial = timer.target - timer.duration
        val timePassed = timer.time.current - timeInitial
        val timeRemain = timer.target - timer.time.current
        val timeFade: Float

        if (timePassed < timeRemain) {
            timeFade = (timePassed / timeFadeIn).toFloat()
        } else {
            timeFade = (timeRemain / timeFadeOut).toFloat()
        }

        val alpha = UtilsMath.limitNumber(timeFade, 0f, 1f)

        title.setTextColour(1f, 1f, 1f, alpha)
        details.setTextColour(1f, 1f, 1f, alpha)

        title.render()
        details.render()
    }

    override fun remove() {
        title.delete()
        details.delete()
        isRemoved = true
    }

    /* Getters */

    override fun isRemoved(): Boolean {
        return isRemoved
    }

}
