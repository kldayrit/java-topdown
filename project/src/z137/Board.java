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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class Board extends JPanel implements ActionListener, Serializable {

    public Timer timer;
//    private List<Alien> aliens;
    public boolean ingame;
    public final int ICRAFT_X = 0;
    public final int ICRAFT_Y = 0;
    public final int B_WIDTH = 600;
    public final int B_HEIGHT = 600;
    public final int DELAY = 15;
    BufferedImage pImage;
	BufferedImage bImage;

    // make list for players
    public List<Player> players;
    int winner;
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
        players.add(new Player(0, 50,1,0));
		players.add(new Player(400, 50,0,1));
		players.add(new Player(400, 400,-1,0));
		players.add(new Player(0, 400,0,-1));

        timer = new Timer(DELAY, this);
        timer.start();

        try {
			pImage = ImageIO.read(getClass().getResource("/images/player.png"));
			bImage = ImageIO.read(getClass().getResource("/images/shot.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
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
		for (Player pl : players) {
			if (pl.isVisible()) {
				g.drawImage(pImage,pl.x, pl.y, 20, 20, null);
				g.setColor(Color.WHITE);
				String playerInfo = Integer.toString(players.indexOf(pl)) + " : " + Integer.toString(pl.getHealth());
				if(players.indexOf(pl) == 0){
					playerInfo = " YOU : " + Integer.toString(pl.getHealth());
				}
				g.drawString(playerInfo, pl.getX(), pl.getY());
//                pl.draw(g);

			}
			List<Missile> ms = pl.getMissiles();
			int diff = 0;
			for (Missile missile : ms) {
				if (missile.isVisible()) {
					g.drawImage(bImage,missile.x, missile.y, bImage.getHeight(), bImage.getWidth(), null);
					g.drawImage(bImage,pl.x + diff, pl.y + 20, bImage.getHeight(), bImage.getWidth(), null );
					diff +=5;
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
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2,
                B_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        inGame();
        updateMissiles();
//        updateAliens();

        checkCollisions();
        checkWinCondition();
        repaint();
    }

    private void inGame() {
    	updateShip();
    	botMove();
    	botFire();
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

    private void botMove(){
    	for(Player pl: players){
    		if(players.indexOf(pl) != 0){
    			Random r = new Random();
    			if (r.nextInt(10) == 1 && pl.isVisible()) {
    				pl.changeDirection();
    			}
    		}
    	}
    }

    private void botFire(){
    	for(Player pl: players){
    		if(players.indexOf(pl) != 0){
    			Random r = new Random();
    			if (r.nextInt(2) == 1 && pl.isVisible() && pl.getMissiles().size() <4) {
    				pl.fire();;
    			}
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

    public void checkWinCondition() {
		int count = 0;

	    for (Player pl : players) {
	        if (pl.isVisible()) {
	            count++;
	            winner = players.indexOf(pl);
	        }
	    }

	    if(count == 1) {
	    	ingame = false;
	    }
	}

    public void checkCollisions() {
        Iterator<Player> spriteIterator = players.iterator();
        while (spriteIterator.hasNext()) {
            Player pl = spriteIterator.next();
            List<Missile> ms = pl.getMissiles();
            // Iterate over the projectiles of the current sprite
            Iterator<Missile> projectileIterator =ms.iterator();
            while (projectileIterator.hasNext()) {
                Missile projectile = projectileIterator.next();
                if (projectile.visible) {
	                // Check collision with other sprites
	                for (Player otherSprite : players) {
	                    // Skip checking collision with itself
	                    if (otherSprite == pl) {
	                        continue;
	                    }

	                    // Check if the projectile intersects with the bounds of the other sprite
	                    if (projectile.getBounds().intersects(otherSprite.getBounds())) {
	                        otherSprite.decrementHealth();
	                        if(otherSprite.getHealth() == 0) {
	                        	otherSprite.setVisible(false);  // Set isVisible to false for the hit sprite with health = 0
	                        }
	                        projectileIterator.remove();    // Remove the projectile from the list
	                        break;                          // Break the loop since a collision occurred
	                    }
	                }
	                if (projectile.x >= 600 || projectile.y >= 600 || projectile.x <= 0 || projectile.y <= 0) {
	                    // Projectile hit the window boundaries, remove it from the sprite's list
	                    projectileIterator.remove();
	                }
                }
            }
        }
    }

    private class TAdapter extends KeyAdapter {

//        @Override
//        public void keyReleased(KeyEvent e) {
//            spaceship.keyReleased(e);
//        }

        @Override
        public void keyPressed(KeyEvent e) {
        	players.get(0).keyPressed(e);

        }
    }
}