package z137;

import java.awt.Graphics;
import java.awt.Image;

public class Wall extends Sprite {
	
    public Wall(int x, int y, int width, int height, Image image) {
        super(x, y);
        this.image = image;
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}