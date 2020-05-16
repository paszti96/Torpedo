package com.torpedo;

import javax.swing.*;
//import java.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MouseMove implements MouseListener, MouseMotionListener, KeyListener {
    private int X, Y;
    private Ship ship;

    public MouseMove(Ship ship) {
        this.ship = ship;
        ship.addMouseListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        ship.updatePosition(e.getX() + e.getComponent().getX() - X, e.getY() + e.getComponent().getY() - Y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            X = e.getX();
            Y = e.getY();
            ship.game.frame.addKeyListener(this);
            ship.addMouseMotionListener(this);
        } else {
            ship.rotate();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        if (ship.placeable) {
            ship.place();
            ship.removeMouseListener(this);
        }

        ship.removeMouseMotionListener(this);
        ship.game.frame.removeKeyListener(this);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            ship.rotate();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}