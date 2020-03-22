package com.torpedo;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;
import java.util.Set;


public class Ship extends JPanel {
    String Name;
    private int cell_size = 37;
    public List<GUI.Cell> cells;
    Rectangle rect;

    public Ship(int size){
        setBorder(new MatteBorder(1,1,1,1,Color.BLACK));
        setBackground(Color.RED);
        setPreferredSize(new Dimension(cell_size,size*cell_size));
        setMaximumSize(new Dimension(cell_size,size*cell_size));
    }

}


/****************************************
 *   Carrier, which has five holes       *
 *   Battleship, which has four holes    *
 *   Cruiser, which has three holes      *
 *   Submarine, which has three holes    *
 *   Destroyer, which has two holes      *
 *****************************************/