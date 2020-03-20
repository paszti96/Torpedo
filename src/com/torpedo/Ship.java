package com.torpedo;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Set;


public class Ship extends JPanel {
    String Name;
    int size;
    GUI.Cell[] cells;
    Rectangle rect;

    public Ship(int size){
        setBorder(new MatteBorder(1,1,1,1,Color.BLACK));
        setBackground(Color.RED);
        setPreferredSize(new Dimension(40,size*40));
        setMaximumSize(new Dimension(40,size*40));
    }

}


/****************************************
 *   Carrier, which has five holes       *
 *   Battleship, which has four holes    *
 *   Cruiser, which has three holes      *
 *   Submarine, which has three holes    *
 *   Destroyer, which has two holes      *
 *****************************************/