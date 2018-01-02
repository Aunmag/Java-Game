package aunmag.shooter.client.states

import aunmag.nightingale.Application
import aunmag.nightingale.basics.BaseGrid
import aunmag.nightingale.data.DataEngine
import aunmag.nightingale.font.Font
import aunmag.nightingale.gui.*
import aunmag.nightingale.utilities.UtilsAudio
import aunmag.shooter.client.Constants
import aunmag.shooter.client.App

class Pause {

    private val theme = UtilsAudio.getOrCreateSoundOgg("sounds/music/menu")
    val buttonContinue = GuiButtonBack(4, 7, 4, 1, "Continue")

    init {
        theme.setIsLooped(true)
        buttonContinue.setIsAvailable(false)
        createPageMain()
    }

    private fun createPageMain() {
        val page = GuiPage()

        page.add(GuiLabel(3, 3, 6, 1, Constants.TITLE))
        page.add(GuiLabel(
                BaseGrid.grid24,
                6, 8, 12, 1,
                "Made with " + DataEngine.TITLE,
                Font.fontDefault,
                1f
        ))
        page.add(GuiLabel(
                BaseGrid.grid24,
                6, 9, 12, 1,
                "version " + Constants.VERSION + " by " + Constants.DEVELOPER,
                Font.fontDefault,
                1f
        ))
        page.add(buttonContinue)
        page.add(GuiButtonAction(4, 8, 4, 1, "New game") { App.main.newGame() })
        page.add(GuiButtonLink(4, 9, 4, 1, "Help", createPageHelp()))
        page.add(GuiButtonLink(4, 10, 4, 1, "Exit", createPageExit()))

        page.open()
    }

    private fun createPageHelp(): GuiPage {
        val page = GuiPage()

        page.add(GuiLabel(5, 1, 2, 1, "Help"))
        page.add(GuiLabel(4, 3, 1, 1, "Movement", Font.fontDefault, 1f))
        page.add(GuiLabel(7, 3, 1, 1, "W, A, S, D", Font.fontDefault, 1f))
        page.add(GuiLabel(4, 4, 1, 1, "Rotation", Font.fontDefault, 1f))
        page.add(GuiLabel(7, 4, 1, 1, "Mouse", Font.fontDefault, 1f))
        page.add(GuiLabel(4, 5, 1, 1, "Sprint", Font.fontDefault, 1f))
        page.add(GuiLabel(7, 5, 1, 1, "Shift", Font.fontDefault, 1f))
        page.add(GuiLabel(4, 6, 1, 1, "Attack", Font.fontDefault, 1f))
        page.add(GuiLabel(7, 6, 1, 1, "LMB", Font.fontDefault, 1f))
        page.add(GuiLabel(4, 7, 1, 1, "Zoom in/out", Font.fontDefault, 1f))
        page.add(GuiLabel(7, 7, 1, 1, "+/-", Font.fontDefault, 1f))
        page.add(GuiLabel(4, 8, 1, 1, "Menu", Font.fontDefault, 1f))
        page.add(GuiLabel(7, 8, 1, 1, "Escape", Font.fontDefault, 1f))
        page.add(GuiButtonBack(4, 10, 4, 1, "Back"))

        return page
    }

    private fun createPageExit(): GuiPage {
        val page = GuiPage()

        page.add(GuiLabel(3, 3, 6, 1, "Are you sure you want to exit?"))
        page.add(GuiButtonAction(4, 8, 4, 1, "Yes") { Application.stopRunning() })
        page.add(GuiButtonBack(4, 9, 4, 1, "No"))

        return page
    }

    fun resume() {
        GuiManager.activate()
        theme.play()
    }

    fun suspend() {
        theme.stop()
    }

    fun update() {
        GuiManager.update()

        if (GuiManager.isShouldClose()) {
            App.main.isPause = false
        }
    }

    fun render() {
        GuiManager.render()
    }

}
