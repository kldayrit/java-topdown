import java.util.Arrays;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Player {
	int x;
    int y;
    char direction;
    BufferedImage image;

	static final int GAME_UNITS = (1200*600) / 25;
	static final int UNIT_SIZE = 30;

	final int bulletX[] = new int[GAME_UNITS];
	final int bulletY[] = new int[GAME_UNITS];
	final char bulletDirection[] = new char[GAME_UNITS];

	boolean alive= true;
	public Player(int x, int y, char dir, String imagePath){
		this.x = x;
		this.y = y;
		this.direction = dir;
		Arrays.fill(bulletX, -1);
		Arrays.fill(bulletY, -1);
		Arrays.fill(bulletDirection, 'A');
		loadImage(imagePath);
	}

	 public void draw(Graphics g) {
		 g.drawImage(image,this.x, this.y, UNIT_SIZE, UNIT_SIZE, null);
	 }
	 
	 private void loadImage(String filename) {
		 try {
			 image = ImageIO.read(getClass().getResource(filename));
		 } catch (IOException e) {
			 e.printStackTrace();
	     }
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
