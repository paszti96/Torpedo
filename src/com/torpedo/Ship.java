package com.torpedo;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Ship extends JPanel {
    private int cell_size = 37;
    public List<Cell> cells;
    public boolean placeable = false;
    private int size;
    public Game game;

    public Ship(Game game, int size) {
        this.game = game;
        this.size = size;
        setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        setBackground(Color.RED);
        setPreferredSize(new Dimension(cell_size, size * cell_size));
    }

    public void updatePosition(int x, int y) {
        setLocation(x, y);

        if (placeable = placeable()) {
            setBackground(Color.GREEN);
        } else {
            setBackground(Color.RED);
        }
    }

    public void place() {
        cells = cells();
        for (Cell cell : cells) {
            cell.setStatus(CellStatus.Taken);
        }
        this.getParent().remove(this);
        cells.get(0).getParent().repaint();

        for (Ship ship: game.ships){
            if(ship.cells == null){
                return;
            }
        }

        game.shipsPlaced();
    }

    private boolean placeable() {
        List<Cell> cells = cells();

        if (cells.size() != size) {
            return false;
        }

        Rectangle rectangle = new Rectangle(
                cells.get(0).x - 1,
                cells.get(0).y - 1,
                cells.get(size - 1).x - cells.get(0).x + 3,
                cells.get(size - 1).y - cells.get(0).y + 3
        );

        for (Ship ship : game.ships) {
            if (ship.equals(this) || ship.cells == null) {
                continue;
            }

            for (Cell cell : ship.cells) {
                if (rectangle.contains(cell.x, cell.y)) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Cell> cells() {
        Rectangle bounds = getBounds();
        bounds.setLocation(getLocationOnScreen());

        return game.my_grid.cellsInBound(bounds);
    }

    public void rotate() {
        setSize(getHeight(), getWidth());
    }
}


/****************************************
 *   Carrier, which has five holes       *
 *   Battleship, which has four holes    *
 *   Cruiser, which has three holes      *
 *   Submarine, which has three holes    *
 *   Destroyer, which has two holes      *
 *****************************************/