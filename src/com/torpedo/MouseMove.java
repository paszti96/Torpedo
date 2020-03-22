package com.torpedo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;

public class MouseMove implements MouseListener, MouseMotionListener {
    private int X,Y;

    public MouseMove(Component... panels){
        for(Component panel: panels){
            panel.addMouseListener(this);
            panel.addMouseMotionListener(this);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        e.getComponent().setLocation(e.getX() +e.getComponent().getX() - X,e.getY() +e.getComponent().getY() - Y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        GUI.clickedAt = e.getComponent();
        X = e.getX();
        Y = e.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GUI.colorCells();
        GUI.clickedAt = null;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}