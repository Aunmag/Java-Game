package client.input;

import client.Display;

import java.awt.event.*;

/**
 * Created by Aunmag on 2017.06.13.
 */

public class Listener implements
        KeyListener,
        MouseListener,
        MouseMotionListener,
        MouseWheelListener
{

    Listener() {
        Display.getFrame().addKeyListener(this);
        Display.getCanvas().addMouseListener(this);
        Display.getCanvas().addMouseMotionListener(this);
        Display.getCanvas().addMouseWheelListener(this);
    }

    /* Keyboard */

    public void keyPressed(KeyEvent e) {
        Input.keys[e.getKeyCode()] = true;
        Input.keysJustPressed[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        Input.keys[e.getKeyCode()] = false;
        Input.keysJustPressed[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {}

    /* Mouse */

    public void mousePressed(MouseEvent e) {
        Input.buttons[e.getButton()] = true;
        Input.buttonsJustPressed[e.getButton()] = true;
    }

    public void mouseReleased(MouseEvent e) {
        Input.buttons[e.getButton()] = false;
        Input.buttonsJustPressed[e.getButton()] = false;
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        Input.mouseX = e.getX();
        Input.mouseY = e.getY();
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
        Input.mouseX = e.getX();
        Input.mouseY = e.getY();
        e.consume();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        Input.mouseWheelRotation = e.getWheelRotation();
    }

}
