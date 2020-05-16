package com.torpedo;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Grid extends JPanel {
    int gridnum = 10;
    String title;
    Cell[] cells;

    public Grid(Game game, String name) {
        title = name;
        setLayout(new GridBagLayout());
        setAlignmentX(LEFT_ALIGNMENT);
        setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        cells = new Cell[gridnum * gridnum];
        int i_cell = 0;
        for (int row = 0; row < gridnum; row++) {
            for (int col = 0; col < gridnum; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                Cell c = new Cell(game, col, row);
                c.setPreferredSize(new Dimension(40, 40));
                c.setBorder(new MatteBorder(1, 1, (row < gridnum - 1) ? 0 : 1, (col < gridnum - 1) ? 0 : 1, Color.BLACK));
                add(c, gbc);
                cells[i_cell] = c;
                i_cell++;
            }
        }
    }

    public Cell get(int x, int y) {
        return cells[y * 10 + x];
    }

    public void revealAround(int x, int y) {
        revealAround(x, y, x, y);
    }

    public void revealAround(int x, int y, int px, int py) {
        for (int i = Math.max(0, x - 1); i <= Math.min(9, x + 1); ++i) {
            for (int j = Math.max(0, y - 1); j <= Math.min(9, y + 1); ++j) {
                if ((i == px && j == py) || (i == x && j == y)) {
                    continue;
                }

                if (get(x, y).destroyed()) {
                    revealAround(i, j, x, y);
                } else {
                    get(x, y).setStatus(CellStatus.Missed);
                }
            }
        }
    }

    public java.util.List<Cell> cellsInBound(Rectangle bounds) {
        List<Cell> cells = new ArrayList<>();

        for (Cell cell : this.cells) {
            if (bounds.contains(cell.getCenter())) {
                cells.add(cell);
            }
        }

        return cells;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(410, 410);
    }


}
