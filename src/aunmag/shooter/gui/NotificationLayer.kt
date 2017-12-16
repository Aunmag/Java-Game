package aunmag.shooter.gui

import aunmag.nightingale.utilities.TimeFlow
import aunmag.nightingale.utilities.UtilsBaseOperative
import java.util.ArrayList

class NotificationLayer(
        private val time: TimeFlow
) {

    private val notifications: MutableList<Notification> = ArrayList()

    internal fun add(title: String, details: String) {
        notifications.add(Notification(time, title, details))
    }

    internal fun update() {
        UtilsBaseOperative.updateAll(notifications)
    }

    internal fun render() {
        UtilsBaseOperative.renderAll(notifications)
    }

    internal fun clear() {
        UtilsBaseOperative.removeAll(notifications)
    }

}
