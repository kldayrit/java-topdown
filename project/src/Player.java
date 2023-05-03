import java.util.Arrays;

public class Player {
	int x;
    int y;
    char direction;

	static final int GAME_UNITS = (1200*600) / 25;

	final int bulletX[] = new int[GAME_UNITS];
	final int bulletY[] = new int[GAME_UNITS];
	final char bulletDirection[] = new char[GAME_UNITS];

	boolean alive= true;
	public Player(int x, int y, char dir){
		this.x = x;
		this.y = y;
		this.direction = dir;
		Arrays.fill(bulletX, -1);
		Arrays.fill(bulletY, -1);
		Arrays.fill(bulletDirection, 'A');
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
