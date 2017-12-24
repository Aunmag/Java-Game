package aunmag.shooter.client;

import aunmag.nightingale.input.Input;
import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.basics.BaseGrid;
import aunmag.nightingale.font.Font;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.client.graphics.CameraShaker;
import aunmag.shooter.client.graphics.Crosshair;
import aunmag.shooter.client.graphics.Hud;
import aunmag.shooter.client.graphics.Blackout;
import aunmag.nightingale.Application;
import aunmag.nightingale.data.DataEngine;
import aunmag.nightingale.gui.*;
import aunmag.nightingale.structures.Texture;
import aunmag.shooter.scenarios.ScenarioEncircling;
import org.lwjgl.glfw.GLFW;
import aunmag.shooter.world.World;

public class Game extends Application {

    private static boolean isPause = true;
    private static ScenarioEncircling scenario = null;
    private static World world;
    private static Player player;
    private static GuiButtonBack buttonContinue;
    private static AudioSource soundTheme;
    private Crosshair crosshair = null;

    public Game() {
        buttonContinue = new GuiButtonBack(4, 7, 4, 1, "Continue");
        buttonContinue.setIsAvailable(false);
        initializePages();
        initializeSoundTheme();
    }

    private void initializeWorld() {
        deleteWorld();
        world = new World();
        player = new Player(world);
        crosshair = new Crosshair(player.getActor());
        buttonContinue.setIsAvailable(true);
        scenario = new ScenarioEncircling(world);
    }

    private void initializePages() {
        initializePageMain();
    }

    private void initializePageMain() {
        BaseGrid grid = BaseGrid.grid24;
        String detailsEngine = "Made with " + DataEngine.title;
        String detailsGame = "version " + Constants.VERSION + " by " + Constants.DEVELOPER;
        GuiLabel[] labels = new GuiLabel[] {
                new GuiLabel(grid, 6, 8, 12, 1, detailsEngine, Font.fontDefault, 1),
                new GuiLabel(3, 3, 6, 1, Constants.TITLE),
                new GuiLabel(grid, 6, 9, 12, 1, detailsGame, Font.fontDefault, 1),
        };

        GuiButton[] buttons = new GuiButton[] {
                buttonContinue,
                new GuiButtonAction(4, 8, 4, 1, "New game", () -> {
                    initializeWorld();
                    setPause(false);
                }),
                new GuiButtonLink(4, 9, 4, 1, "Help", createPageHelp()),
                new GuiButtonLink(4, 10, 4, 1, "Exit", createPageExit()),
        };

        Texture wallpaper = Texture.getOrCreate("images/wallpapers/main_menu", true, false);
        wallpaper.scaleAsWallpaper();

        new GuiPage(labels, buttons, wallpaper).open();
    }

    private GuiPage createPageHelp() {
        GuiLabel[] labels = new GuiLabel[] {
                new GuiLabel(5, 1, 2, 1, "Help"),

                new GuiLabel(4, 3, 1, 1, "Movement", Font.fontDefault, 1),
                new GuiLabel(7, 3, 1, 1, "W, A, S, D", Font.fontDefault, 1),

                new GuiLabel(4, 4, 1, 1, "Rotation", Font.fontDefault, 1),
                new GuiLabel(7, 4, 1, 1, "Mouse", Font.fontDefault, 1),

                new GuiLabel(4, 5, 1, 1, "Sprint", Font.fontDefault, 1),
                new GuiLabel(7, 5, 1, 1, "Shift", Font.fontDefault, 1),

                new GuiLabel(4, 6, 1, 1, "Attack", Font.fontDefault, 1),
                new GuiLabel(7, 6, 1, 1, "LMB", Font.fontDefault, 1),

                new GuiLabel(4, 7, 1, 1, "Zoom in/out", Font.fontDefault, 1),
                new GuiLabel(7, 7, 1, 1, "+/-", Font.fontDefault, 1),

                new GuiLabel(4, 8, 1, 1, "Menu", Font.fontDefault, 1),
                new GuiLabel(7, 8, 1, 1, "Escape", Font.fontDefault, 1),
        };


        GuiButton[] buttons = new GuiButton[] {
            new GuiButtonBack(4, 10, 4, 1, "Back"),
        };

        Texture wallpaper = Texture.getOrCreate("images/wallpapers/help", true, false);
        wallpaper.scaleAsWallpaper();

        return new GuiPage(labels, buttons, wallpaper);
    }

    private GuiPage createPageExit() {
        GuiLabel[] labels = new GuiLabel[] {
                new GuiLabel(3, 3, 6, 1, "Are you sure you want to exit?"),
        };

        GuiButton[] buttons = new GuiButton[] {
                new GuiButtonAction(4, 8, 4, 1, "Yes", Application::stopRunning),
                new GuiButtonBack(4, 9, 4, 1, "No"),
        };

        Texture wallpaper = Texture.getOrCreate("images/wallpapers/exit", true, false);
        wallpaper.scaleAsWallpaper();

        return new GuiPage(labels, buttons, wallpaper);
    }

    private void initializeSoundTheme() {
        soundTheme = UtilsAudio.getOrCreateSoundOgg("sounds/music/menu");
        soundTheme.setIsLooped(true);
        themePlay();
    }

    protected void gameUpdate() {
        if (isPause) {
            GuiManager.update();
            if (GuiManager.isShouldClose() && isWorldCreated()) {
                setPause(false);
            }
        } else {
            player.updateInput();
            world.update();
            scenario.update();
            CameraShaker.update();
            player.updateCameraPosition();

            if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                setPause(true);
            }
        }
    }

    protected void gameRender() {
        if (isPause) {
            GuiManager.render();
        } else {
            world.render();
            Blackout.render();
            crosshair.render();

            if (player.getActor().getHasWeapon()) {
                player.getActor().getWeapon().magazine.renderHud();
            }

            scenario.render();
            Hud.render();
        }
    }

    protected void gameTerminate() {
        deleteWorld();
    }

    public static void deleteWorld() {
        player = null;

        if (isWorldCreated()) {
            world.remove();
            world = null;
        }

        buttonContinue.setIsAvailable(false);

        if (scenario != null) {
            scenario.remove();
            scenario = null;
        }
    }

    public static void themePlay() {
        soundTheme.play();
    }

    public static void themeStop() {
        soundTheme.stop();
    }

    /* Setters */

    public static void setPause(boolean isPause) {
        Game.isPause = isPause;
        Application.getWindow().setCursorGrabbed(!isPause);

        if (isPause) {
            GuiManager.activate();
            themePlay();
            if (isWorldCreated()) {
                world.stopSounds();
            }
        } else {
            themeStop();
            if (isWorldCreated()) {
                world.playSounds();
            }
        }
    }

    /* Getters */

    public static boolean isPause() {
        return isPause;
    }

    public static World getWorld() {
        return world;
    }

    public static Player getPlayer() {
        return player;
    }

    public static boolean isWorldCreated() {
        return world != null;
    }

}
