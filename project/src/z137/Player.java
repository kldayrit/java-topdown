package z137;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Player extends Sprite {

	int dx;
	int dy;
	int id;
	private int score;
	private List<Missile> missiles;
	String imagePath = "/images/player.png";

	int speed = 2;

	public Player(int x, int y, int dx, int dy) {
		super(x, y);
		this.dx = dx;
		this.dy = dy;
		initCraft();
	}

	private void initCraft() {
		missiles = new ArrayList<>();
		loadImage(this.imagePath);
		getImageDimensions();
	}

	public void move() {

		x += dx * speed;
		y += dy * speed;

		if (x < 1) {
			x = 1;
		}

		if (y < 1) {
			y = 1;
		}
		if (x + 20 > 600) {
			x -= dx * speed;
		}

		if (y + 20 > 600) {
			y -= dy * speed;
		}
	}

	public List<Missile> getMissiles() {
		return missiles;
	}
	
	public void incrementScore() {
		this.score+=1;
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_SPACE) {
			fire();
		}

		if (key == KeyEvent.VK_RIGHT) {
			if (this.dx == 1 && this.dy == 0) {
				this.dx = 1;
				this.dy = 1;
			} else if (this.dx == 1 && this.dy == 1) {
				this.dx = 0;
				this.dy = 1;
			} else if (this.dx == 0 && this.dy == 1) {
				this.dx = -1;
				this.dy = 1;
			} else if (this.dx == -1 && this.dy == 1) {
				this.dx = -1;
				this.dy = 0;
			} else if (this.dx == -1 && this.dy == 0) {
				this.dx = -1;
				this.dy = -1;
			} else if (this.dx == -1 && this.dy == -1) {
				this.dx = 0;
				this.dy = -1;
			} else if (this.dx == 0 && this.dy == -1) {
				this.dx = 1;
				this.dy = -1;
			} else if (this.dx == 1 && this.dy == -1) {
				this.dx = 1;
				this.dy = 0;
			}
		}

	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, 20, 20);
	}

	public void fire() {
		missiles.add(new Missile(x + width, y + height / 2, this.dx, this.dy));
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}