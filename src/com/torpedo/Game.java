package com.torpedo;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.xml.validation.Validator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

enum GameState {
    Placement, Placed, Connecting, Play, Wait, Won, Lost
}

public class Game {
    private JButton[] buttons;
    public Ship[] ships;
    public JFrame frame;
    private JLayeredPane pane;
    private Communication comm;
    public Grid my_grid;
    private Grid opponent;
    public GameState state = GameState.Placement;

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public Game() {
        ships = new Ship[]{
                new Ship(this, 5),
                new Ship(this, 4),
                new Ship(this, 3),
                new Ship(this, 3),
                new Ship(this, 2)
        };
    }

    public void shipsPlaced() {
        buttons[0].setEnabled(true);
        buttons[1].setEnabled(true);
        state = GameState.Placed;
    }

    public void start() {
        Game game = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and set up new window
                frame = new JFrame("Torpedo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(880, 500));
                frame.setLayout(new FlowLayout(FlowLayout.LEFT));

                // Setting up my_grid
                my_grid = new Grid(game, "You");

                // Create a pane for overlay
                pane = new JLayeredPane();
                pane.setLayout(new FlowLayout(FlowLayout.TRAILING));
                pane.setPreferredSize(new Dimension(838, 420));
                pane.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));

                // add ships to the Panel
                pane.add(my_grid, FlowLayout.RIGHT);

                for (Component c : ships) pane.add(c, FlowLayout.LEFT);
                frame.add(pane);

                JButton b_join = new JButton("Join Game");
                b_join.setFocusable(false);
                b_join.setEnabled(false);
                b_join.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        {
                            boolean failed= true;
                            String IP = "";
                            while(failed) {
                                 IP = JOptionPane.showInputDialog(frame,
                                        "Give me an available, valid IP address to connect",
                                        "IP",
                                        JOptionPane.QUESTION_MESSAGE);
                                try {
                                    connect(new Client(game, IP));
                                    failed = false;
                                } catch (Exception a) {
                                    failed = true;
                                }
                            }
                            JOptionPane.showConfirmDialog(frame,
                                    "Your IP:"+ IP,
                                    "IP",
                                    JOptionPane.PLAIN_MESSAGE);
                            frame.setTitle("Torpedo Client: " + IP);
                        }
                    }
                });
                frame.add(b_join, FlowLayout.LEFT);

                JButton b_create = new JButton("Create Game");
                b_create.setFocusable(false);
                b_create.setEnabled(false);
                b_create.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean failed= true;
                        String IP = "";
                        while(failed){
                            IP = JOptionPane.showInputDialog(frame,
                                    "Give me an available, valid IP address",
                                    "IP",
                                    JOptionPane.QUESTION_MESSAGE);
                            try {
                                connect(new Server(game,IP));
                                failed = false;
                            } catch (Exception ex) {
                                failed = true;
                            }
                        }
                        JOptionPane.showConfirmDialog(frame,
                                "Your IP:"+ IP,
                                "IP",
                                JOptionPane.PLAIN_MESSAGE);
                        frame.setTitle("Torpedo Server: " + IP);
                    }
                });
                frame.add(b_create, FlowLayout.LEFT);

                buttons = new JButton[]{b_create, b_join};

                for (Ship ship : ships) {
                    new MouseMove(ship);
                }

                //Show window
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private void connect(Communication comm) {
        this.comm = comm;
        new Thread(this.comm).start();
        state = GameState.Connecting;
        buttons[0].setEnabled(false);
        buttons[1].setEnabled(false);
    }

    public static boolean validateIP(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }

    public void connected() {
        state = comm instanceof Server ? GameState.Play : GameState.Wait;
        opponent = new Grid(this, "Opponent");
        pane.add(opponent, FlowLayout.RIGHT);
        frame.revalidate();
        frame.repaint();
    }

    public void checkHit(int x, int y) {
        if (!my_grid.get(x, y).destroy()) {
            comm.fireResult(x, y, false, false, false);
            state = GameState.Play;
            return;
        }

        Ship target = null;
        boolean allDestroyed = true;
        boolean shipDestroyed = true;
        for (Ship ship : ships) {
            boolean destroyed = true;
            for (Cell cell : ship.cells) {
                if (x == cell.x && y == cell.y) {
                    target = ship;
                }
                if (!cell.destroyed()) {
                    allDestroyed = false;
                    destroyed = false;
                }
            }
            if (target != null && target.equals(ship)) {
                shipDestroyed = destroyed;
            }
        }

        comm.fireResult(x, y, true, shipDestroyed, allDestroyed);

        if (shipDestroyed) {
            my_grid.revealAround(x, y);
        }

        if (allDestroyed) {
            state = GameState.Lost;
            finished();
            return;
        }

        state = GameState.Play;
    }

    public void fireResult(int x, int y, boolean hit, boolean destroyed, boolean allDestroyed) {
        opponent.get(x, y).setStatus(hit ? CellStatus.Destroyed : CellStatus.Missed);
        if (destroyed) {
            opponent.revealAround(x, y);
        }
        if (allDestroyed) {
            state = GameState.Won;
            finished();
        }
    }

    public void fireOn(Cell cell) {
        if (state == GameState.Play && cell.destroyable()) {
            state = GameState.Wait;
            comm.fire(cell.x, cell.y);
        }
    }

    private void finished() {
        System.out.println(state);
        JOptionPane.showMessageDialog(frame,state);
    }

    public void checkHit(String msgCome) {
            String[] parts = msgCome.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            checkHit(x,y);
    }

    public void fireResult(String msg){
        int x, y;
        boolean hit, destroyed, allDestroyed;
        String[] arrived = msg.split(",");
        x = Integer.parseInt(arrived[0]);
        y = Integer.parseInt(arrived[1]);
        hit = Boolean.parseBoolean(arrived[2]);
        destroyed = Boolean.parseBoolean(arrived[3]);
        allDestroyed = Boolean.parseBoolean(arrived[4]);

        fireResult(x, y, hit, destroyed, allDestroyed);
    }
}

