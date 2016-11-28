package client;

// Created by Aunmag on 05.10.2016.

import client.states.GamePlay;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    boolean[] keys = new boolean[256];
    boolean[] mouseButtons = new boolean[8];
    public boolean isMouseReleased = false;

    Input(JFrame frame, Canvas canvas) {

        frame.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);

    }

    // Updaters:

    public void reset() {

        isMouseReleased = false;

    }

    // Key listener methods:

    @Override public void keyPressed(KeyEvent e) {

        keys[e.getKeyCode()] = true;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && Client.isGameStarted()) {
            if (Client.isGamePlay()) {
                Client.getGameMenu().activeMenuMain();
                Client.getGameMenu().getMenuMain().getSoundscape().loop();
                Client.getGamePlay().getAmbiance().stop();
                Client.getGamePlay().getAtmosphere().stop();
            } else {
                GamePlay.activate();
                Client.getGameMenu().getMenuMain().getSoundscape().stop();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_F1) {
            Client.setIsPerformanceData(!Client.isPerformanceData());
        }

    }

    @Override public void keyReleased(KeyEvent e) {

        keys[e.getKeyCode()] = false;

    }

    @Override public void keyTyped(KeyEvent e) {}

    // Mouse listener methods:

    @Override public void mousePressed(MouseEvent e) {

        mouseButtons[e.getButton()] = true;
        Client.setIsMousePressed(true);

        if (e.getButton() == MouseEvent.BUTTON1) {
            isMouseReleased = false;
        }

    }

    @Override public void mouseReleased(MouseEvent e) {

        mouseButtons[e.getButton()] = false;
        Client.setIsMousePressed(false);

        if (e.getButton() == MouseEvent.BUTTON1) {
            isMouseReleased = true;
        }

    }

    @Override public void mouseEntered(MouseEvent e) {}

    @Override public void mouseExited(MouseEvent e) {}

    @Override public void mouseClicked(MouseEvent e) {}

    // Mouse motion listener methods:

    @Override public void mouseDragged(MouseEvent e) {

        Client.setMousePosition(e.getX(), e.getY());
        e.consume();

    }

    @Override public void mouseMoved(MouseEvent e) {

        Client.setMousePosition(e.getX(), e.getY());
        e.consume();

    }

    // Mouse wheel listener methods:

    @Override public void mouseWheelMoved(MouseWheelEvent e) {

        Client.setZoom(Client.getZoom() + Client.getZoom() * 0.1 * -e.getWheelRotation());

    }

}
