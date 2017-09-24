package aunmag.shooter.client;

import aunmag.shooter.scenarios.ScenarioEmpty;
import aunmag.shooter.client.graphics.Hud;
import aunmag.shooter.client.graphics.Blackout;
import aunmag.shooter.managers.SoundManager;
import aunmag.shooter.scenarios.ScenarioEncircling;
import aunmag.nightingale.Application;
import aunmag.nightingale.basics.BaseOperative;
import aunmag.nightingale.data.DataEngine;
import aunmag.nightingale.gui.*;
import aunmag.nightingale.structures.Texture;
import org.lwjgl.glfw.GLFW;
import aunmag.shooter.sprites.Actor;
import aunmag.shooter.world.World;

public class Game extends Application {

    private static boolean isPause = true;
    private static BaseOperative scenario = new ScenarioEmpty();
    private static World world;
    private static GuiButtonBack buttonContinue;
    private static SoundManager soundTheme = new SoundManager("/sounds/music/menu.wav");

    public Game() {
        Actor.loadSounds();
        buttonContinue = new GuiButtonBack(4, 7, 4, 1, "Continue");
        buttonContinue.setIsAvailable(false);
        initializePages();
        themePlay();
    }

    private void initializeWorld() {
        deleteWorld();
        world = new World();
        buttonContinue.setIsAvailable(true);
        scenario = new ScenarioEncircling();
    }

    private void initializePages() {
        initializePageMain();
    }

    private void initializePageMain() {
        GuiLabel[] labels = new GuiLabel[] {
                new GuiLabel(3, 3, 6, 1, DataEngine.name),
                new GuiLabel(5, 4, 2, 1, String.format(
                        "v%s by %s", DataEngine.version, DataEngine.developer
                )),
        };

        GuiButton[] buttons = new GuiButton[] {
//                Runnable action = () -> {
//                    sound.stop();
//                };
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

                new GuiLabel(4, 3, 1, 1, "Movement"),
                new GuiLabel(7, 3, 1, 1, "W, A, S, D"),

                new GuiLabel(4, 4, 1, 1, "Rotation"),
                new GuiLabel(7, 4, 1, 1, "Mouse"),

                new GuiLabel(4, 5, 1, 1, "Sprint"),
                new GuiLabel(7, 5, 1, 1, "Shift"),

                new GuiLabel(4, 6, 1, 1, "Attack"),
                new GuiLabel(7, 6, 1, 1, "LMB"),

                new GuiLabel(4, 7, 1, 1, "Zoom in/out"),
                new GuiLabel(7, 7, 1, 1, "+/- or Wheel Up/Down"),

                new GuiLabel(4, 8, 1, 1, "Menu"),
                new GuiLabel(7, 8, 1, 1, "Escape"),
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
                new GuiLabel(3, 5, 6, 1, "Are you sure you want to exit?"),
        };

        GuiButton[] buttons = new GuiButton[] {
                new GuiButtonBack(3, 9, 3, 1, "No"),
                new GuiButtonAction(6, 9, 3, 1, "Yes", Application::stopRunning),
        };

        Texture wallpaper = Texture.getOrCreate("images/wallpapers/exit", true, false);
        wallpaper.scaleAsWallpaper();

        return new GuiPage(labels, buttons, wallpaper);
    }

    protected void gameUpdate() {
        if (isPause) {
            GuiManager.update();
            if (GuiManager.isShouldClose() && isWorldCreated()) {
                setPause(false);
            }
        } else {
            updateInputForCamera();
            updateInputForPlayer();
            world.update();
            scenario.update();
            if (Application.getInput().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                setPause(true);
            }
        }
    }

    private void updateInputForCamera() {
        float zoom = Application.getCamera().getZoom();
        float zoomChange = zoom * 0.01f;

        if (Application.getInput().isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            Application.getCamera().setZoom(zoom + zoomChange);
        } else if (Application.getInput().isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            Application.getCamera().setZoom(zoom - zoomChange);
        }
    }

    private void updateInputForPlayer() {
        Actor player = Actor.getPlayer();

        if (player == null) {
            return;
        }

        float mouseVelocityX = Application.getInput().getMouseVelocity().x;
        player.addRadiansCarefully(mouseVelocityX * 0.005f);

        player.isWalkingForward = Application.getInput().isKeyDown(GLFW.GLFW_KEY_W);
        player.isWalkingBack = Application.getInput().isKeyDown(GLFW.GLFW_KEY_S);
        player.isWalkingLeft = Application.getInput().isKeyDown(GLFW.GLFW_KEY_A);
        player.isWalkingRight = Application.getInput().isKeyDown(GLFW.GLFW_KEY_D);
        player.isSprinting = Application.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT);
        player.isAttacking = Application.getInput().isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1);
    }

    protected void gameRender() {
        if (isPause) {
            GuiManager.render();
        } else {
            world.render();
            Blackout.render();
            scenario.render();
            Hud.render();
        }
    }

    protected void gameTerminate() {
        deleteWorld();
    }

    public static void deleteWorld() {
        if (isWorldCreated()) {
            world.remove();
            world = null;
        }

        buttonContinue.setIsAvailable(false);

        scenario.remove();
        scenario = new ScenarioEmpty();
    }

    public static void themePlay() {
        soundTheme.loop();
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
                world.stop();
            }
        } else {
            themeStop();
            if (isWorldCreated()) {
                world.play();
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

    public static boolean isWorldCreated() {
        return world != null;
    }

}
