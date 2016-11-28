package root.client;

import root.LogManager;
import root.ai.AI;
import root.managers.ArrayAverage;
import root.modes.Encircling;
import root.scripts.Inertia;
import root.scripts.Spawn;
import root.sprites.*;
import root.sprites.Object;
import root.sprites.Actor;

import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

// Created by Aunmag on 24.09.2016.

public class Client implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private JFrame frame;
    private Canvas canvas;
    private Robot robot;

    private int width, height;
    private boolean isSinglePlayerMode;

    private boolean isRunning = false;
    private Thread thread;

    private BufferStrategy bs;
    private Graphics2D g;
    private Graphics2D hud;
    public Actor subject;

    private boolean isZooming = false;

    public boolean[] keys = new boolean[256];
    public boolean[] mouseButtons = new boolean[8];

    public boolean isMouseActive = false;
    public boolean isMouseDragged = false;
    public int mouseScreenX;
    public int mouseScreenY;
    public int mouseSpeedX;
    public int mouseSpeedY;
    public int groundSize;

    // Time data:
    private int framesLimit = 75;
    private double framesCount;
    private long timeCurrent = System.nanoTime();
    private long timeLast = timeCurrent;
    private long timePass = 0;
    private long timePerformance = 0;
    private ArrayAverage timePerformanceAverage = new ArrayAverage(framesLimit);
    private double timeTick = 1000000000 / framesLimit;
    private double timeDelta = 0;

    // Effects:
    double subjectHealthLast;
    Inertia subjectHealthInertiaFX = new Inertia(0.08);
    private Image blackout;

    public Client(int width, int height, boolean isSinglePlayerMode) {

        this.width = width;
        this.height = height;
        this.isSinglePlayerMode = isSinglePlayerMode;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public void createDisplay() {

        frame = new JFrame("Game");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.pack();

        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);

        BufferedImage cursorImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

        try {
            BufferedImage image = ImageIO.read(Client.class.getResource("/images/gui/blackout1600.png"));
            blackout = image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            LogManager.log("Error", "Can't load blackout image.", e);
            blackout = null;
        }

        Cursor cursorBlank = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "");
        frame.getContentPane().setCursor(cursorBlank);

        frame.addKeyListener(this);

    }

    private void tick() {

        if (isMouseActive) {
            subject.setDegrees((subject.getDegrees() + mouseSpeedX / 2) % 360);
            frame.getLocationOnScreen().getX();
            Point windowPosition = frame.getLocationOnScreen();
            int putMouseX = (int) (windowPosition.getX() + width / 2);
            int putMouseY = (int) (windowPosition.getY() + height / 2);
            robot.mouseMove(putMouseX, putMouseY);
            isMouseActive = false;
        } else {
            mouseSpeedX = 0;
            mouseSpeedY = 0;
        }

        subject.isMovingForward = keys[KeyEvent.VK_W];
        subject.isMovingBack = keys[KeyEvent.VK_S];
        subject.isMovingLeft = keys[KeyEvent.VK_A];
        subject.isMovingRight = keys[KeyEvent.VK_D];
        subject.isRunning = keys[KeyEvent.VK_SHIFT];
        subject.isAttacking = mouseButtons[MouseEvent.BUTTON1];
        isZooming = keys[KeyEvent.VK_SPACE];

        if (isSinglePlayerMode) {

            Encircling.tick(subject.x, subject.y);

            // Tick AIs:
            for (int i = AI.allAIs.size() - 1; i >= 0; i--) {
                AI ai = AI.allAIs.get(i);
                if (!ai.isValid) {
                    AI.allAIs.remove(i);
                    continue;
                }
                ai.tick();
            }

            // Tick actors:
            for (int i = Actor.allActors.size() - 1; i >= 0; i--) {
                Actor zombie = Actor.allActors.get(i);
                if (!zombie.isValid) {
                    Actor.allActors.remove(i);
                    continue;
                }
                zombie.tick();
            }

            Encircling.confineActor(subject, groundSize / 2);

            // Tick weapons:
            for (int i = Weapon.allWeapons.size() - 1; i >= 0; i--) {
                Weapon weapon = Weapon.allWeapons.get(i);
                if (!weapon.isValid) {
                    Weapon.allWeapons.remove(i);
                    continue;
                }
                weapon.tick();
            }

            // Tick bullets:
            for (int i = Bullet.allBullets.size() - 1; i >= 0; i--) {
                Bullet bullet = Bullet.allBullets.get(i);
                if (!bullet.isValid) {
                    Bullet.allBullets.remove(i);
                    continue;
                }
                bullet.tick();
            }
        }
    }

    private void render() {

        bs = canvas.getBufferStrategy();

        if (bs == null) {
            canvas.createBufferStrategy(2);
            return;
        }

        g = (Graphics2D) bs.getDrawGraphics();
        g.setColor(new Color(108, 162, 66));
        g.fillRect(0, 0, width, height);

        double xCenter = width / 2;
        double yCenter = height - 20;
        double xPosition = subject.x - xCenter;
        double yPosition = subject.y - yCenter;

        if (isZooming) {
            g.scale(2.0, 2.0);
            xCenter = width / 4;
            yCenter = (height - 40) / 2;
            xPosition = subject.x - xCenter;
            yPosition = subject.y - yCenter;
        }

        g.rotate(Math.toRadians(-subject.getDegrees() - 90), xCenter, yCenter);

        for (Object object: Object.allGroundObjects) {
            object.render(g, xPosition, yPosition);
        }
        for (Weapon weapon: Weapon.allWeapons) {
            weapon.render(g, xPosition, yPosition);
        }
        for (Actor actor: Actor.allActors) {
            actor.render(g, xPosition, yPosition);
        }
        for (Bullet bullet: Bullet.allBullets) {
            bullet.render(g, xPosition, yPosition);
        }

        hud = (Graphics2D) bs.getDrawGraphics();

        if (blackout != null) {
            float alpha = (float) (1 - subject.getHealth() / 1.2);
            hud.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            hud.drawImage(blackout, 0, 0, null);
            hud.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            alpha = (float) ((1 - subject.getHealth()) * 0.6);
            hud.setColor(Color.BLACK);
            hud.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            hud.fillRect(0, 0, width, height);
        }

        // FX display:
        boolean isTargetHit = subjectHealthLast != subject.getHealth();
        float alpha;
        if (isTargetHit) {
            alpha = (float) subjectHealthInertiaFX.update(1, 0.75);
            if (!subjectHealthInertiaFX.getState()) {
                subjectHealthLast = subject.getHealth();
            }
        } else {
            alpha = (float) subjectHealthInertiaFX.update(1, 0);
        }
        hud.setColor(Color.BLACK);
        hud.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        hud.fillRect(0, 0, width, height);
        hud.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        hud.setColor(Color.BLACK);
        hud.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        hud.drawRect(0, 0, width, height);

        Encircling.renderHud(hud);
        hud.setColor(Color.WHITE);
        double t = timePerformanceAverage.getAverage() / 1_000_000;
        t = Math.round(t * 10.0) / 10.0;
        hud.drawString("alpha version 0.2.0", 20, 20);
        hud.drawString("Performance time: " + t + " ms", 20, 40);
        hud.drawString("Player position: x" + (int) subject.x + " y" + (int) subject.y, 20, 60);

        if (!subject.isAlive) {
            hud.drawString("GAME OVER (press Alt and close the game)", width / 2 - 120, height / 2);
        }

        bs.show();
        g.dispose();
        hud.dispose();

    }

    // Runnable methods:

    @Override public void run() {

        createDisplay();

        subject = Spawn.human(0, 0, 0);
        Weapon.allWeapons.add(new Weapon(subject));
        subjectHealthLast = subject.getHealth();

        int groundNumber = 8;
        int groundSizeBlock = 128;
        groundSize = groundSizeBlock * groundNumber;
        int groundStart = groundNumber / 2 * groundSizeBlock - groundSizeBlock / 2;
        for (int x = -groundStart; x < groundSize - groundStart; x += groundSizeBlock) {
            for (int y = -groundStart; y < groundSize - groundStart; y += groundSizeBlock) {
                Object.allGroundObjects.add(new Object("ground/grass/grass_1", false, x, y, 0));
            }
        }

        while (isRunning) {
            timeCurrent = System.nanoTime();
            timePass = timeCurrent - timeLast;
            timeLast = timeCurrent;
            timeDelta += timePass / timeTick;
            if (timeDelta >= 1) {
                tick();
                render();
                timeDelta--;
                timePerformance = System.nanoTime() - timeCurrent;
                timePerformanceAverage.addValue(timePerformance);
            }
        }

        stop();

    }

    public synchronized void start() {

        if (isRunning) {
            return;
        }

        isRunning = true;
        thread = new Thread(this);
        thread.start();

    }

    public synchronized void stop() {

        if (!isRunning) return;

        isRunning = false;

        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Key listener methods:

    @Override public void keyPressed(KeyEvent e) {

        keys[e.getKeyCode()] = true;

    }

    @Override public void keyReleased(KeyEvent e) {

        keys[e.getKeyCode()] = false;

    }

    @Override public void keyTyped(KeyEvent e) {}

    // Mouse listener methods:

    @Override public void mousePressed(MouseEvent e) {

        mouseButtons[e.getButton()] = true;

    }

    @Override public void mouseReleased(MouseEvent e) {

        mouseButtons[e.getButton()] = false;

    }

    @Override public void mouseEntered(MouseEvent e) {}

    @Override public void mouseExited(MouseEvent e) {}

    @Override public void mouseClicked(MouseEvent e) {}

    // Mouse motion listener methods:

    private void mouseMotionUpdate(MouseEvent e) {

        isMouseActive = true;
        mouseSpeedX = e.getX() - mouseScreenX;
        mouseSpeedY = e.getX() - mouseScreenX;
        mouseScreenX = e.getX();
        mouseScreenY = e.getY();

    }

    @Override public void mouseDragged(MouseEvent e) {

        mouseMotionUpdate(e);
        isMouseDragged = true;
        e.consume();

    }

    @Override public void mouseMoved(MouseEvent e) {

        mouseMotionUpdate(e);
        isMouseDragged = false;
        e.consume();

    }

}
