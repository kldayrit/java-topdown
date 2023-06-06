package z137;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import common.Client;

import java.io.IOException;
import java.io.Serializable;

public class GameWindow extends JPanel implements ActionListener{

	GameState state;

	public List<Player> players;

	int myID;

	private volatile boolean connecting;

	private GameClient connection;
	public Timer timer;
	public final int DELAY = 15;
	public GameWindow(String hostName, int serverPortNumber) {
		// gui stuff
		players = new ArrayList<>();
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(600, 600));
        timer = new Timer(DELAY, this);
        timer.start();

        Thread connector = new Thread( () -> connect(hostName, serverPortNumber) );
        connector.start();
    } // end constructor
	private class GameClient extends Client {

        /**
         * Connect to the hub at a specified host name and port number.
         */
        public GameClient(String hubHostName,int hubPort) throws IOException {
            super(hubHostName, hubPort);
        }

        /**
         * Responds to a message received from the Hub.  The only messages that
         * are supported are TicTacToeGameState objects.  When one is received,
         * the newState() method in the TicTacToeWindow class is called. That
         * method is called using Platform.runLater() so that it will run on
         * the JavaFX application thread.
         */
        protected void messageReceived(Object message) {
            if (message instanceof GameState) {
            	SwingUtilities.invokeLater( () -> newState( (GameState)message ) );
            }
        }

        /**
         * If a shutdown message is received from the Hub, the user is notified
         * and the program ends.
         */
        protected void serverShutdown(String message) {
        	SwingUtilities.invokeLater( () -> {
        		System.out.println("server down");
        		System.exit(0);
            });
        }

    } // end nested class TicTacToeClient

	 private void connect(String hostName, int serverPortNumber) {
	        GameClient c;
	        int id;
	        try {
	            c = new GameClient(hostName, serverPortNumber);
	            id = c.getID();
	            SwingUtilities.invokeLater( () -> {
	                connecting = false;
	                connection = c;
	                myID = id-1;
	                repaint();
	               // start gui
	            });
	        }
	        catch (Exception e) {
	        	SwingUtilities.invokeLater( () -> {
	        		System.exit(0);
	            });
	        }
	    }
	 private void newState(GameState state) {
	        if ( state.playerDisconnected ) {
	        	System.exit(0);
	        }
	        this.state = state;
	        players = state.players;
	        //
	    }
	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
        	if (state.ingame) {

                drawObjects(g);

            } else {

                drawGameOver(g);
            }
        }
        catch (Exception e) {
        	SwingUtilities.invokeLater( () -> {
            });
        }

        Toolkit.getDefaultToolkit().sync();
    }
	private void inGame() {
    	updateShip();
    	try{
    		 if (!state.ingame) {
    			 timer.stop();
    		 }
    	}
    	catch (Exception e) {
        	SwingUtilities.invokeLater( () -> {
            });
        }


    }

    private void updateShip() {
    	for (Player pl: players){
    		if (pl.isVisible()) {

                pl.move();
            }
    	}

    }

    private void updateMissiles() {
    	for (Player pl: players){
    		List<Missile> ms = pl.getMissiles();

            for (int i = 0; i < ms.size(); i++) {

                Missile m = ms.get(i);

                if (m.isVisible()) {
                    m.move();
                } else {
                    ms.remove(i);
                }
            }
    	}

    }
    public void checkCollisions() {
    	for (Player pl: players){
    		Rectangle r3 = pl.getBounds();

          List<Missile> ms = pl.getMissiles();

          for (Missile m : ms) {

              Rectangle r1 = m.getBounds();
          }
    	}

    }
	private void drawObjects(Graphics g) {
    	for (Player pl: players){
    		if (pl.isVisible()) {
    			g.setColor(Color.CYAN);
    			g.fillOval(pl.x, pl.y, 20, 20);
                g.setColor(Color.WHITE);
                g.drawString(Integer.toString(players.indexOf(pl)), pl.getX(), pl.getY());
            }
    		List<Missile> ms = pl.getMissiles();

            for (Missile missile : ms) {
                if (missile.isVisible()) {
                	g.setColor(Color.YELLOW);
                	g.fillOval(missile.x, missile.y, 10, 10);
                }
            }
    	}


    }

	private void drawGameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (600 - fm.stringWidth(msg)) / 2,
                600 / 2);
    }
	@Override
    public void actionPerformed(ActionEvent e) {

        inGame();
        updateMissiles();

        checkCollisions();

        repaint();
    }
	private class TAdapter extends KeyAdapter {

      @Override
      public void keyPressed(KeyEvent e) {
      	players.get(myID).keyPressed(e);
      	connection.send(players);
      }
  }
}
