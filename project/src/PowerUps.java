import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class PowerUps {
	private String type;
	private int xPos, yPos; // position
	private static BufferedImage image;

	static final int UNIT_SIZE = 30;

	public final static String SHIELD_TYPE = "shield";
	public final static String LASER_TYPE = "laser";
	public final static String MULTI_TYPE = "multishot";
	private final static int POWERUP_WIDTH = 50;

	public PowerUps(int xPos, int yPos, int n) {
		this.xPos = xPos;
		this.yPos = yPos;
		// determines the type of power up that will spawn
		if (n == 1) {
			this.setType(PowerUps.SHIELD_TYPE);
		} else if (n == 0) {
			this.setType(PowerUps.LASER_TYPE);
		} else if (n == 2) {
			this.setType(PowerUps.MULTI_TYPE);
		}
		// load the appropriate powerup image depending on its type
		if (this.getType() == PowerUps.SHIELD_TYPE) {
			loadImage("src/images/tile_0102.png");
		} else if (this.getType() == PowerUps.LASER_TYPE) {
			loadImage("src/images/tile_0116.png");
		} else if (this.getType() == PowerUps.MULTI_TYPE) {
			loadImage("src/images/tile_0113.png");
		}
	}

	// returns the type of power up
	public String getType() {
		return type;
	}

	// set the power up type
	public void setType(String type) {
		this.type = type;
	}

	public void draw(Graphics g) {
		g.drawImage(image, this.xPos, this.yPos, UNIT_SIZE, UNIT_SIZE, null);
	}

	private void loadImage(String filename) {
		try {
			image = ImageIO.read(getClass().getResource(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
