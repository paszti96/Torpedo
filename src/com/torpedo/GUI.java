package com.torpedo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class GUI {
    static Component clickedAt;
    static Cell cells[];
    private static Component[] fleet;

    public static void main(String[] args) {
        GUI g = new GUI();

    }

    public static void colorCells(){
        List<Cell> new_territory = new ArrayList<>();
        for(int i = 0; i<cells.length; i++){
            cells[i].setBackground(null);
            if(
                (((cells[i].getLocationOnScreen().x + cells[i].getWidth()/2) > clickedAt.getLocationOnScreen().x) &&
                (cells[i].getLocationOnScreen().y + cells[i].getHeight()/2) > clickedAt.getLocationOnScreen().y) &&
                (((cells[i].getLocationOnScreen().x) < clickedAt.getLocationOnScreen().x + clickedAt.getWidth()) &&
                ((cells[i].getLocationOnScreen().y ) < clickedAt.getLocationOnScreen().y + clickedAt.getHeight()))
            ) {
//                cells[i].setBackground(Color.blue);
                new_territory.add(cells[i]);
            }
        }
        ((Ship)clickedAt).cells = new_territory;
        for(Component c: fleet){
            Ship s = (Ship) c;
            if (s.cells != null)
            for(Cell cell: s.cells)
            {
                cell.setBackground(Color.BLUE);
            }
        }
    }

    public GUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and set up new window
                JFrame frame = new JFrame("Torpedo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(900,600));
                frame.setLayout(new FlowLayout(FlowLayout.LEFT));
                frame.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if(e.getKeyCode() == KeyEvent.VK_SPACE && clickedAt!= null){
                            clickedAt.setSize(clickedAt.getHeight(),clickedAt.getWidth());
                        }

                    }
                });

                // Setting up grid
                Grid grid = new Grid();

                // Create a pane for overlay
                JLayeredPane pane = new JLayeredPane();
                pane.setLayout(new FlowLayout(FlowLayout.RIGHT));
                pane.setPosition(frame, 0);

                // Create the fleet
                Ship carrier = new Ship(5);
                Ship battleship = new Ship(4);
                Ship cruiser = new Ship(3);
                Ship submarine = new Ship(3);
                Ship destroyer = new Ship(2);

                fleet = new Component[]{carrier, battleship, cruiser, submarine, destroyer};

                JButton b = new JButton("Start");
                frame.add(b);

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
            cells = new Cell[100];
            int i_cell = 0;
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
                    cells[i_cell] = c;
                    i_cell++;
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
        Point id;
        boolean reserved;

        public Cell(int x, int y){
            this.id = new Point(x,y);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(40,40);
        }
    }
}