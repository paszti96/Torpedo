package com.torpedo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;



public class GUI {
    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and set up new window
                JFrame frame = new JFrame("Torpedo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(800,800));

                // Setting up grid
                Grid grid = new Grid();

                // Create a pane for overlay
                JLayeredPane pane = new JLayeredPane();
                pane.setLayout(new OverlayLayout(pane));


                // Create the fleet
                Component[] fleet;
                Ship carrier = new Ship(5);
                Ship battleship = new Ship(4);
                Ship cruiser = new Ship(3);
                Ship submarine = new Ship(3);
                Ship destroyer = new Ship(2);

                fleet = new Component[]{carrier, battleship, cruiser, submarine, destroyer};

                // add ships to the Panel
                for(Component c : fleet) pane.add(c);
                pane.add(new Grid());
                frame.add(pane);

                // add mousevent listener for the fleet
                MouseMove mv = new MouseMove(fleet);

                //Show window
                frame.pack();
                frame.setVisible(true);
            }
        });

    }

    public class Grid extends JPanel{
        int gridnum = 10;

        public Grid(){

            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            for(int row = 0; row < gridnum; row++){
                for(int col = 0; col < gridnum; col++){
                    gbc.gridx = col;
                    gbc.gridy = row;

                    Cell c = new Cell(col,row);
                    c.setPreferredSize(new Dimension(40,40));
                    Border border = null;
                    if(row < gridnum - 1){
                        if(col < gridnum -1){
                            border = new MatteBorder(1,1, 0,0, Color.BLACK);
                        } else{
                            border = new MatteBorder(1,1,0,1,Color.BLACK);
                        }
                    }else{
                        if(col < gridnum -1){
                            border = new MatteBorder(1,1, 1,0, Color.BLACK);
                        } else{
                            border = new MatteBorder(1,1,1,1,Color.BLACK);
                        }
                    }
                    c.setBorder(border);
                    add(c,gbc);
                }
            }

//            gbc = new GridBagConstraints();
//            gbc.gridheight = 10;
//            JPanel p = new JPanel();
//            p.setBorder(new MatteBorder(1,1,1,1,Color.BLACK));
//            p.setPreferredSize(new Dimension(200,400));
//            add(p,gbc);


        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }

    }
    public class Cell extends JPanel{
        private Color defaultBackground;
        Point id;

        public Cell(int x, int y){
            this.id = new Point(x,y);

        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(40,40);
        }
    }
}