import java.util.Arrays;
import java.awt.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.*;

public class Player {
	int x;
	int y;
	char direction;
	BufferedImage image;
	private int timeOfDeath;
	private int score = 0;
	private String bulletType = "normal";
	private boolean hasShield = false;

	static final int GAME_UNITS = (1200 * 600) / 25;
	static final int UNIT_SIZE = 50;

	final int bulletX[] = new int[GAME_UNITS];
	final int bulletY[] = new int[GAME_UNITS];
	final char bulletDirection[] = new char[GAME_UNITS];

	boolean alive = true;

	public Player(int x, int y, char dir, String imagePath) {
		this.x = x;
		this.y = y;
		this.direction = dir;
		Arrays.fill(bulletX, -1);
		Arrays.fill(bulletY, -1);
		Arrays.fill(bulletDirection, 'A');
		loadImage(imagePath);
	}

	public void draw(Graphics g) {
		if (this.hasShield) {			
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(5.0f));
			Ellipse2D circle = new Ellipse2D.Double(this.x-7, this.y-2, UNIT_SIZE+15, UNIT_SIZE+15);
			g2d.setColor(Color.WHITE);
			g2d.draw(circle);
		}
		g.drawImage(image, this.x, this.y, UNIT_SIZE, UNIT_SIZE, null);
	}

	private void loadImage(String filename) {
		try {
			image = ImageIO.read(getClass().getResource(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Rectangle getBoundingBox() {
        return new Rectangle(x, y, UNIT_SIZE, UNIT_SIZE);
	}
	
	public void respawn(int width, int height) {
		// respawn dito muna
		if (width != 0 && height != 0) {
			this.x = width / 2;
			this.y = height / 2;
		} else {
			this.x = width;
			this.y = height;
		}
		this.alive = true;
	}

	public String getBulletType() {
		return bulletType;
	}

	public void setBulletType(String bulletType) {
		this.bulletType = bulletType;
	}

	public boolean getHasShield() {
		return hasShield;
	}

	public void setHasShield(boolean hasShield) {
		this.hasShield = hasShield;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTimeOfDeath() {
		return timeOfDeath;
	}

	public void setTimeOfDeath(int timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}

//	public int getX() {
//    	return this.x;
//	}
//
//	public int getY() {
//    	return this.y;
//	}
//
//	public void setX(int x){
//    	this.x = x;
//	}
//
//	public void setY(int y) {
//		this.y = y;
//	}
}
