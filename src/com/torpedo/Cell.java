package com.torpedo;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

enum CellStatus {
    Empty, Taken, Destroyed, Hit, Missed
}

public class Cell extends JPanel {
    int x;
    int y;
    CellStatus status;

    public Cell(Game game, GridBagConstraints constraints) {
        this(game, constraints.gridx, constraints.gridy);
    }

    public Cell(Game game, int x, int y) {
        this.x = x;
        this.y = y;
        status = CellStatus.Empty;

        setPreferredSize(new Dimension(40, 40));
        setBorder(new MatteBorder(1, 1, y == 9 ? 1 : 0, x == 9 ? 1 : 0, Color.BLACK));

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
