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
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.Serializable;

public class Board extends JPanel implements ActionListener, Serializable {

    public Timer timer;
//    public SpaceShip spaceship;
//    private List<Alien> aliens;
    public boolean ingame;
    public final int ICRAFT_X = 0;
    public final int ICRAFT_Y = 0;
    public final int B_WIDTH = 600;
    public final int B_HEIGHT = 600;
    public final int DELAY = 15;

    // make list for players
    public List<Player> players;

    public Board() {

        initBoard();
    }

    private void initBoard() {
    	players = new ArrayList<>();
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        ingame = true;

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
//
//        spaceship = new SpaceShip(ICRAFT_X, ICRAFT_Y);
        players.add(new Player(400, 400,1,0));

        timer = new Timer(DELAY, this);
        timer.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ingame) {

            drawObjects(g);

        } else {

            drawGameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
    	for (Player pl: players){
    		if (pl.isVisible()) {
                g.drawImage(pl.getImage(), pl.getX(), pl.getY(),
                        this);
                g.setColor(Color.white);
                g.drawString(Integer.toString(players.indexOf(pl)), pl.getX(), pl.getY());
            }
    		List<Missile> ms = pl.getMissiles();

            for (Missile missile : ms) {
                if (missile.isVisible()) {
                    g.drawImage(missile.getImage(), missile.getX(),
                            missile.getY(), this);
                }
            }
    	}


        g.setColor(Color.WHITE);
    }

    private void drawGameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2,
                B_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        inGame();
        updateMissiles();
//        updateAliens();

        checkCollisions();

        repaint();
    }

    private void inGame() {
    	updateShip();
        if (!ingame) {
            timer.stop();
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

//    private void updateAliens() {
//
//        if (aliens.isEmpty()) {
//
//            ingame = false;
//            return;
//        }
//
//        for (int i = 0; i < aliens.size(); i++) {
//
//            Alien a = aliens.get(i);
//
//            if (a.isVisible()) {
//                a.move();
//            } else {
//                aliens.remove(i);
//            }
//        }
//    }

    public void checkCollisions() {
    	for (Player pl: players){
    		Rectangle r3 = pl.getBounds();

//          for (Alien alien : aliens) {
  //
//              Rectangle r2 = alien.getBounds();
  //
//              if (r3.intersects(r2)) {
  //
//                  spaceship.setVisible(false);
//                  alien.setVisible(false);
//                  ingame = false;
//              }
//          }

          List<Missile> ms = pl.getMissiles();

          for (Missile m : ms) {

              Rectangle r1 = m.getBounds();

//              for (Alien alien : aliens) {
  //
//                  Rectangle r2 = alien.getBounds();
  //
//                  if (r1.intersects(r2)) {
  //
//                      m.setVisible(false);
//                      alien.setVisible(false);
//                  }
//              }
          }
    	}

    }

    private class TAdapter extends KeyAdapter {

//        @Override
////        public void keyReleased(KeyEvent e) {
////            spaceship.keyReleased(e);
////        }

        @Override
        public void keyPressed(KeyEvent e) {
        	for (Player pl: players){
        		pl.keyPressed(e);
        	}

        }
    }
}