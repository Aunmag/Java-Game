package client;

import client.states.GamePlay;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.event.*;

/**
 * Created by Aunmag on 2016.10.05.
 */

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    boolean[] keys = new boolean[256];
    boolean[] mouseButtons = new boolean[8];

    private int mouseWheelRotation = 0;

    public boolean isMouseReleased = false;

    Input(JFrame frame, Canvas canvas) {
        frame.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);
    }

    public void reset() {
        isMouseReleased = false;
        mouseWheelRotation = 0;
    }

    public int getMouseWheelRotation() {
        return mouseWheelRotation;
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && DataManager.isGameStarted()) {
            if (DataManager.isGamePlay()) {
                DataManager.getGameMenu().activeMenuMain();
                DataManager.getGameMenu().getMenuMain().getSoundscape().loop();
                DataManager.getGamePlay().getAmbiance().stop();
                DataManager.getGamePlay().getAtmosphere().stop();
            } else {
                GamePlay.activate();
                DataManager.getGameMenu().getMenuMain().getSoundscape().stop();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_F1) {
            DataManager.setIsPerformanceData(!DataManager.isPerformanceData());
        }
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        mouseButtons[e.getButton()] = true;
        DataManager.setIsMousePressed(true);

        if (e.getButton() == MouseEvent.BUTTON1) {
            isMouseReleased = false;
        }
    }

    public void mouseReleased(MouseEvent e) {
        mouseButtons[e.getButton()] = false;
        DataManager.setIsMousePressed(false);

        if (e.getButton() == MouseEvent.BUTTON1) {
            isMouseReleased = true;
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        DataManager.setMousePosition(e.getX(), e.getY());
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
        DataManager.setMousePosition(e.getX(), e.getY());
        e.consume();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelRotation = e.getWheelRotation();
    }

}
