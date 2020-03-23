package com.torpedo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GUI {
    static Component clickedAt;
    static Cell cells[];

    private static Component[] fleet;
    private static Set<Cell> my_reserved_territory = new HashSet<Cell>();
    private JFrame frame;
    private JLayeredPane pane;
    private Communication comm;


    public static boolean II;

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
                my_reserved_territory.add(cell);
            }
        }
    }

    private void change_context(){
        II = true;
        for(Component c : fleet)
            pane.remove(c);
        fleet = null;

        // Opponent my_grid
        Grid opponent = new Grid("Opponent");
        pane.add(opponent,FlowLayout.RIGHT);
        frame.revalidate();
        frame.repaint();
    }

    public GUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and set up new window
                frame = new JFrame("Torpedo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(880,500));
                frame.setLayout(new FlowLayout(FlowLayout.LEFT));
                frame.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        System.out.println("s");
                        if(e.getKeyCode() == KeyEvent.VK_SPACE && clickedAt!= null){
                            clickedAt.setSize(clickedAt.getHeight(),clickedAt.getWidth());
                        }

                    }
                });

                // Setting up my_grid
                Grid my_grid = new Grid("You");

                // Create a pane for overlay
                pane = new JLayeredPane();
                pane.setLayout(new FlowLayout(FlowLayout.TRAILING));
//                pane.setPosition(frame, 0);
//                pane.setPreferredSize(new Dimension(820,420));
                pane.setPreferredSize(new Dimension(838,420));
                pane.setBorder(new MatteBorder(1,1,1,1,Color.BLACK));

                // Create the fleet
                Ship carrier = new Ship(5);
                Ship battleship = new Ship(4);
                Ship cruiser = new Ship(3);
                Ship submarine = new Ship(3);
                Ship destroyer = new Ship(2);

                fleet = new Component[]{carrier, battleship, cruiser, submarine, destroyer};

                // add ships to the Panel
                pane.add(my_grid,FlowLayout.RIGHT);

                for(Component c : fleet) pane.add(c,FlowLayout.LEFT);

                frame.add(pane);


                JButton b_join = new JButton("Join Game");
                b_join.setFocusable(false);
                b_join.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//                        if(my_reserved_territory.size() == 17)
                        {
                            comm = new Client();
                            new Thread(comm).start();
                            change_context();
//                        }else{
                            // Todo: reset this state
                        }
                    }
                });
                frame.add(b_join,FlowLayout.LEFT);

                JButton b_create = new JButton("Create Game");
                b_create.setFocusable(false);
                b_create.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//                        if(my_reserved_territory.size() == 17)
                        {
                            comm = new Server();
                            new Thread(comm).start();
                            change_context();
//                        }else{
                            // Todo: reset this state
                        }
                    }
                });
                frame.add(b_create,FlowLayout.LEFT);


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
        String title;

        public Grid(String name){
            title = name;
            setLayout(new GridBagLayout());
            setAlignmentX(LEFT_ALIGNMENT);
            setBorder(new MatteBorder(1,1,1,1,Color.BLACK));
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
            return new Dimension(410, 410);
        }

    }
    public class Cell extends JPanel{
        String id;

        public Cell(int x, int y){

            this.id = String.valueOf(x) + String.valueOf(y);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mouseClicked(e);
                    if (II && ((Grid) getParent()).title == "Opponent") {
                        Container c = getParent();
//                        if(getBackground() == Color.GREEN)
//                        {
//                            setBackground(null);
//                        }else{
                        try {
                            System.out.println(id);
                            comm.send(id);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                         setBackground(Color.GREEN);


//                        }
                    }
                }
            });
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(40,40);
        }
    }
}