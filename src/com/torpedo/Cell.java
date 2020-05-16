package com.torpedo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

enum CellStatus {
    Empty, Taken, Destroyed, Hit, Missed
}

public class Cell extends JPanel {
    String id;
    int x;
    int y;
    CellStatus status;

    public Cell(Game game, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = String.valueOf(x) + String.valueOf(y);
        status = CellStatus.Empty;

        Cell that = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (((Grid) getParent()).title == "Opponent") {
                    game.fireOn(that);
                }
            }
        });
    }

    public void setStatus(CellStatus status) {
        this.status = status;

        switch (status) {
            case Taken:
                setBackground(Color.BLUE);
                break;
            case Hit:
            case Destroyed:
                setBackground(Color.GREEN);
                break;
            case Missed:
                setBackground(Color.RED);
                break;
        }
    }

    public Point getCenter() {
        Point point = getLocationOnScreen();
        point.translate(20, 20);
        return point;
    }

    public boolean destroy() {
        setStatus((status == CellStatus.Taken) ? CellStatus.Destroyed : CellStatus.Missed);

        return destroyed();
    }

    public boolean destroyed() {
        return status == CellStatus.Destroyed;
    }
}
