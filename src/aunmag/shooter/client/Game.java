package aunmag.shooter.client;

import aunmag.nightingale.Camera;
import aunmag.nightingale.Input;
import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.basics.BaseGrid;
import aunmag.nightingale.font.Font;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.client.graphics.CameraShaker;
import aunmag.shooter.client.graphics.Crosshair;
import aunmag.shooter.scenarios.ScenarioEmpty;
import aunmag.shooter.client.graphics.Hud;
import aunmag.shooter.client.graphics.Blackout;
import aunmag.shooter.scenarios.ScenarioEncircling;
import aunmag.nightingale.Application;
import aunmag.nightingale.basics.BaseOperative;
import aunmag.nightingale.data.DataEngine;
import aunmag.nightingale.gui.*;
import aunmag.nightingale.structures.Texture;
import aunmag.shooter.weapon.Weapon;
import aunmag.shooter.weapon.WeaponFactory;
import aunmag.shooter.world.WorldTime;
import org.lwjgl.glfw.GLFW;
import aunmag.shooter.actor.Actor;
import aunmag.shooter.world.World;

public class Game extends Application {

    private static boolean isPause = true;
    private static boolean isVirtualMode = false;
    private static BaseOperative scenario = new ScenarioEmpty();
    private static World world;
    private static GuiButtonBack buttonContinue;
    private static AudioSource soundTheme;
    private Crosshair crosshair = null;

    private Weapon laserGun = WeaponFactory.laserGun();
    private Weapon mp27 = WeaponFactory.mp27();

    public Game() {
        buttonContinue = new GuiButtonBack(4, 7, 4, 1, "Continue");
        buttonContinue.setIsAvailable(false);
        initializePages();
        initializeSoundTheme();
    }

    private void initializeWorld() {
        deleteWorld();
        world = new World();
        crosshair = new Crosshair(Actor.getPlayer());
        buttonContinue.setIsAvailable(true);
        scenario = new ScenarioEncircling();
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
            updateInputForCamera();
            updateInputForPlayer();
            world.update();

            CameraShaker.update();
            updateCamera();

            scenario.update();
            if (Input.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                setPause(true);
            }
        }
    }

    private void updateInputForCamera() {
        Camera camera = Application.getCamera();

        float zoom = camera.getScaleZoom();
        float zoomChange = zoom * 0.01f;

        if (Input.isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            camera.setScaleZoom(zoom + zoomChange);
        } else if (Input.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            camera.setScaleZoom(zoom - zoomChange);
        }
    }

    private void updateInputForPlayer() {
        Actor player = Actor.getPlayer();

        if (player == null) {
            return;
        }

        player.isWalkingForward = Input.isKeyDown(GLFW.GLFW_KEY_W);
        player.isWalkingBack = Input.isKeyDown(GLFW.GLFW_KEY_S);
        player.isWalkingLeft = Input.isKeyDown(GLFW.GLFW_KEY_A);
        player.isWalkingRight = Input.isKeyDown(GLFW.GLFW_KEY_D);
        player.isSprinting = Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT);
        player.isAttacking = Input.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1);

        if (Input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
            player.isAiming.toggle(WorldTime.getCurrentMilliseconds());
        }

        float mouseSensitivity = 0.005f;
        mouseSensitivity -= mouseSensitivity * player.isAiming.getValueCurrent() * 0.75f;
        player.addRadiansCarefully(Input.getMouseVelocity().x() * mouseSensitivity);

        if (Input.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE)) {
            isVirtualMode = !isVirtualMode;
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_R) && player.getHasWeapon()) {
            player.getWeapon().magazine.reload();
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_0)) {
            Actor.getPlayer().setWeapon(laserGun);
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_1)) {
            Actor.getPlayer().setWeapon(mp27);
        }
    }

    private void updateCamera() {
        Actor player = Actor.getPlayer();

        if (player == null) {
            return;
        }

        Camera camera = Application.getCamera();
        camera.setPosition(player.getX(), player.getY());
        camera.setRadians(player.getRadians());

        float offset = Application.getWindow().getCenterY() / 2f;
        camera.addOffset(0, offset, true);
        camera.addOffset(0, offset * player.isAiming.getValueCurrent(), true);
    }

    protected void gameRender() {
        if (isPause) {
            GuiManager.render();
        } else {
            world.render();
            Blackout.render();
            crosshair.render();

            if (Actor.getPlayer().getHasWeapon()) {
                Actor.getPlayer().getWeapon().magazine.renderHud();
            }

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

    public static boolean isVirtualMode() {
        return isVirtualMode;
    }

    public static World getWorld() {
        return world;
    }

    public static boolean isWorldCreated() {
        return world != null;
    }

}
