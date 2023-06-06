package z137;

import java.awt.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.*;

public class Sprite implements Serializable {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected boolean visible;
	transient protected Image image;

	public Sprite(int x, int y) {
		this.x = x;
		this.y = y;
		visible = true;
	}

	protected void getImageDimensions() {
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	protected void loadImage(String filename) {
		try {
			image = ImageIO.read(getClass().getResource(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {
//		if (this.hasShield) {			
//			Graphics2D g2d = (Graphics2D) g;
//			g2d.setStroke(new BasicStroke(5.0f));
//			Ellipse2D circle = new Ellipse2D.Double(this.x-7, this.y-2, UNIT_SIZE+15, UNIT_SIZE+15);
//			g2d.setColor(Color.WHITE);
//			g2d.draw(circle);
//		}
		g.drawImage(image, this.x, this.y, this.width, this.height, null);
	}

	public Image getImage() {
		return image;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

//	public Rectangle getBounds() {
//		return new Rectangle(x, y, width, height);
//	}
}
