package z137;


public class Missile extends Sprite {

    private final int BOARD_WIDTH = 590;
    private final int MISSILE_SPEED = 4;
    private int dx;
    private int dy;

    public Missile(int x, int y, int dx, int dy) {
        super(x, y);
        this.dx = dx;
        this.dy = dy;
        initMissile();
    }

    private void initMissile() {
        loadImage("./images/shot.png");
        getImageDimensions();
    }

    public void move() {

        x += dx * MISSILE_SPEED;
        y += dy * MISSILE_SPEED;

        if (x > BOARD_WIDTH)
            visible = false;
    }
}