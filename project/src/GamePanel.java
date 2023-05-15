import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	int score = 0;
	int speed = 2;
	int timeRemaining;
	int commandNum = 0;
	String gameState = "IN GAME";
	boolean running = false;

	Timer timer, powerupTimer;
	JLabel timerLabel, scoreLabel;
	JButton button;
	GameTimer gameTime;
	BufferedImage image;
	BufferedImage background;

	// pang 4 players
	ArrayList<Player> players;
	private List<PowerUps> powerups = new ArrayList<>();

	static final int SCREEN_WIDTH = 1200;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 90;

	String[] mColors = { "images/tile_0084.png", // p1
			"images/tile_0085.png", // p2
			"images/tile_0086.png", // p3
			"images/tile_0087.png", // p4
			"#3079ab", // dark blue
			"#e15258", // red
			"#7d669e", // purple
			"#b7c0c7" // light gray
	};

	GamePanel() {
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.timerLabel = new JLabel("Time: ");
		add(timerLabel);
		this.scoreLabel = new JLabel("Score: ");
		add(scoreLabel);
		this.players = new ArrayList<Player>();
		spawnPlayers();
		startGame();
	}

	public void spawnPlayers() {
		this.players.add(new Player(0, 0, 'R', "images/tile_0084.png"));
		this.players.add(new Player(SCREEN_WIDTH - UNIT_SIZE, 0, 'D', "images/tile_0085.png"));
		this.players.add(new Player(SCREEN_WIDTH - UNIT_SIZE, SCREEN_HEIGHT - UNIT_SIZE, 'L', "images/tile_0086.png"));
		this.players.add(new Player(0, SCREEN_HEIGHT - UNIT_SIZE, 'U', "images/tile_0087.png"));
	}

	public void startGame() {
		running = true;
		timer = new Timer(DELAY, this);

		powerupTimer = new Timer(10000, e -> { // spawn powerups every 10 seconds
			if (this.powerups.size() < 3) {
				spawnPowerups();
			}
		});
		timer.start();
		gameTime = new GameTimer(60);
		powerupTimer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (gameTime.getTimeRemaining() == 0) {
			running = false;
		}

		if (running) {
			// paint background image
			try {
				background = ImageIO.read(getClass().getResource("images/tile_0049.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < SCREEN_WIDTH; i += UNIT_SIZE) {
				for (int j = 0; j < SCREEN_HEIGHT; j += UNIT_SIZE) {
					g.drawImage(background, i, j, UNIT_SIZE, UNIT_SIZE, null);
				}
			}

//			// grids para lang makita
//			for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
//				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // vertical
//			}
//			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // horizontal
//			}

			// draw player
			for (Player player : players) {
				player.draw(g);
			}

			// draw powerups
			for (PowerUps powerup : powerups) {
				powerup.draw(g);
			}

			// draw bullets
			for (int i = 0; i < this.players.size(); i++) {
				Player p = this.players.get(i);
				g.setColor(Color.decode(mColors[i + 4]));
				for (int j = 0; j < GAME_UNITS; j++) {
					if ((p.bulletX[j] != -1) || (p.bulletY[j] != -1) || (p.bulletDirection[j] != 'A')) {
						g.fillOval(p.bulletX[j], p.bulletY[j], UNIT_SIZE / 3, UNIT_SIZE / 3);
					}
				}
			}

			// draw score
			scoreLabel.setText("Score: " + score);
			timeRemaining = gameTime.getTimeRemaining();
			timerLabel.setText("Time: " + timeRemaining);
		} else {
			gameOver(g);
		}
	}

	public void move() {
		// para lang mag stop if tumama sa edge pwede pa tanggalin to
		for (int i = 0; i < this.players.size(); i++) {
			Player p = this.players.get(i);
			switch (p.direction) {
			case 'U':
				p.y = p.y - UNIT_SIZE;
				break;
			case 'D':
				p.y = p.y + UNIT_SIZE;
				break;
			case 'L':
				p.x = p.x - UNIT_SIZE;
				break;
			case 'R':
				p.x = p.x + UNIT_SIZE;
				break;
			}
		}
	}

	public void botMove() {
		for (int i = 1; i < this.players.size(); i++) {
			Player p = this.players.get(i);
			Random r = new Random();
			if (r.nextInt(2) == 1 && p.alive) {
				switch (p.direction) {
				case 'U':
					p.direction = 'R';
					break;
				case 'D':
					p.direction = 'L';
					break;
				case 'L':
					p.direction = 'U';
					break;
				case 'R':
					p.direction = 'D';
					break;
				}
			}
		}
	}

	public void spawnPowerups() {
		Random rand = new Random();
		// random location & type
		int x = rand.nextInt(SCREEN_WIDTH - UNIT_SIZE) + UNIT_SIZE;
		int y = rand.nextInt(SCREEN_HEIGHT - UNIT_SIZE) + UNIT_SIZE;
		// TODO: add logic so powerups will not spawn on top of players and obstacles
//		int type = rand.nextInt(2);
		// TODO: fix bug where all powerup on screen change type
		this.powerups.add(new PowerUps(x, y, 0)); // shield nalang muna yung powerup
	}

	public void checkCollisions() {
		// left border
		for (int i = 0; i < this.players.size(); i++) {
			Player p = this.players.get(i);
			if (p.x < 0) {
				p.x = 0;
			}
			// right border
			if (p.x == SCREEN_WIDTH) {
				p.x = SCREEN_WIDTH - UNIT_SIZE;
			}
			// top bordera
			if (p.y < 0) {
				p.y = 0;
			}
			// bottom border
			if (p.y == SCREEN_HEIGHT) {
				p.y = SCREEN_HEIGHT - UNIT_SIZE;
			}
		}

		// check if may tumama sa mga bullet ng bot sa player
		Player p = this.players.get(0);
		for (int i = 1; i < this.players.size(); i++) {
			Player b = this.players.get(i);
			for (int j = 0; j < GAME_UNITS; j++) {
				// if tumama bullet ng kalaban sa player
				if (((b.bulletX[j] >= p.x) && (b.bulletX[j] <= p.x + UNIT_SIZE - 1))
						&& ((b.bulletY[j] >= p.y) && (b.bulletY[j] <= p.y + UNIT_SIZE - 1)) && b.alive) {
					if (p.getHasShield() == true) {
						p.setHasShield(false); // remove shield if hit by enemy bullet
					} else {
						p.alive = false;
						if (score > 0) {
							score--; // decrease score if player dies until 0
						}
						// add delay
						p.respawn(0, 0);
					}
				}
				// if tumama bullet ng player sa kalaban
				if (((p.bulletX[j] >= b.x) && (p.bulletX[j] <= b.x + UNIT_SIZE - 1))
						&& ((p.bulletY[j] >= b.y) && (p.bulletY[j] <= b.y + UNIT_SIZE - 1)) && b.alive) {
					b.alive = false;
					score++;
					// add delay
					b.respawn(SCREEN_WIDTH, SCREEN_HEIGHT);
				}
			}
		}
		// check for powerups collision
		for (int i = 0; i < powerups.size(); i++) {
			PowerUps powerup = powerups.get(i);
			if (p.getBoundingBox().intersects(powerup.getBoundingBox())) {
				// TODO: logic for when player collides with powerup
				powerups.remove(powerup);
				if (powerup.getType() == "shield") {
					p.setHasShield(true);
				} else if (powerup.getType() == "laser") {

				} else if (powerup.getType() == "multishot") {

				}
			}
		}
		// lagay so ng collision ng player to player
		for (int i = 1; i < players.size(); i++) {
			Player bot = players.get(i);
			if (p.getBoundingBox().intersects(bot.getBoundingBox())) {
				// TODO: Collision detected player and bot
				changePlayerDirection(p);
				changePlayerDirection(bot);
			}
		}
	}

	public void changePlayerDirection(Player p) {
		switch (p.direction) {
		case 'U':
			p.direction = 'R';
			break;
		case 'D':
			p.direction = 'L';
			break;
		case 'L':
			p.direction = 'U';
			break;
		case 'R':
			p.direction = 'D';
			break;
		}
	}

	public void fireBullets() {
		for (int i = 0; i < GAME_UNITS; i++) {
			Player p = this.players.get(0);

			if ((p.bulletX[i] == -1) && (p.bulletY[i] == -1) && (p.bulletDirection[i] == 'A')) {
				switch (p.direction) {
				case 'U':
					p.bulletX[i] = p.x;
					p.bulletY[i] = p.y - UNIT_SIZE;
					p.bulletDirection[i] = 'U';
					break;
				case 'D':
					p.bulletX[i] = p.x;
					p.bulletY[i] = p.y + UNIT_SIZE;
					p.bulletDirection[i] = 'D';
					break;
				case 'L':
					p.bulletX[i] = p.x - UNIT_SIZE;
					p.bulletY[i] = p.y;
					p.bulletDirection[i] = 'L';
					break;
				case 'R':
					p.bulletX[i] = p.x + UNIT_SIZE;
					p.bulletY[i] = p.y;
					p.bulletDirection[i] = 'R';
					break;
				}
				break;
			}
		}
	}

	public void botBullets() {
		for (int i = 1; i < this.players.size(); i++) {
			Player p = this.players.get(i);
			Random r = new Random();
			if (r.nextInt(2) == 1 && p.alive) {
				if ((p.bulletX[i] == -1) && (p.bulletY[i] == -1) && (p.bulletDirection[i] == 'A')) {
					switch (p.direction) {
					case 'U':
						p.bulletX[i] = p.x;
						p.bulletY[i] = p.y - UNIT_SIZE;
						p.bulletDirection[i] = 'U';
						break;
					case 'D':
						p.bulletX[i] = p.x;
						p.bulletY[i] = p.y + UNIT_SIZE;
						p.bulletDirection[i] = 'D';
						break;
					case 'L':
						p.bulletX[i] = p.x - UNIT_SIZE;
						p.bulletY[i] = p.y;
						p.bulletDirection[i] = 'L';
						break;
					case 'R':
						p.bulletX[i] = p.x + UNIT_SIZE;
						p.bulletY[i] = p.y;
						p.bulletDirection[i] = 'R';
						break;
					}
					break;
				}
			}
		}
	}

	public void moveBullets() {
		for (int j = 0; j < this.players.size(); j++) {
			Player p = this.players.get(j);
			for (int i = 0; i < GAME_UNITS; i++) {
				switch (p.bulletDirection[i]) {
				case 'U':
					p.bulletY[i] = p.bulletY[i] - speed * UNIT_SIZE;
					break;
				case 'D':
					p.bulletY[i] = p.bulletY[i] + speed * UNIT_SIZE;
					break;
				case 'L':
					p.bulletX[i] = p.bulletX[i] - speed * UNIT_SIZE;
					break;
				case 'R':
					p.bulletX[i] = p.bulletX[i] + speed * UNIT_SIZE;
					break;
				}
				// tanggalin if lumampas na sa screen
				if ((p.bulletX[i] > SCREEN_WIDTH) || (p.bulletX[i] < 0) || (p.bulletY[i] > SCREEN_HEIGHT)
						|| (p.bulletY[i] < 0)) {
					p.bulletX[i] = -1;
					p.bulletY[i] = -1;
					p.bulletDirection[i] = 'A';
				}
			}
		}
	}

	public void gameOver(Graphics g) {
		gameState = "GAME OVER";

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, (SCREEN_HEIGHT / 2) - 50);

		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		metrics = getFontMetrics(g.getFont());
		int x = (SCREEN_WIDTH - metrics.stringWidth("Play Again")) / 2;
		int y = (SCREEN_HEIGHT / 2) + 100;
		g.drawString("Play Again", x, y);
		if (commandNum == 0) {
			g.drawString(">", x - 40, y);
		}
		x = (SCREEN_WIDTH - metrics.stringWidth("Exit Game")) / 2;
		y = (SCREEN_HEIGHT / 2) + 200;
		g.drawString("Exit Game", x, y);
		if (commandNum == 1) {
			g.drawString(">", x - 40, y);
		}
	}

	// dito yung parang per frame
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (running) {
			move();
			botMove();
			checkCollisions();
			botBullets();
			moveBullets();

		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			Player p = players.get(0);
			if (gameState == "IN GAME") {
				// kasi right key lang pwede
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					changePlayerDirection(p);
				}

				// dito yung bullet
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					fireBullets();
				}

				// TODO: show chat
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//				}
			} else if (gameState == "GAME OVER") { // for game over menu
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					commandNum--;
					if (commandNum < 0) {
						commandNum = 1;
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					commandNum++;
					if (commandNum > 1) {
						commandNum = 0;
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (commandNum == 0) { // play again, reset settings
						score = 0;
						players.clear();
						powerups.clear();
						timer.stop();
						powerupTimer.stop();
						gameState = "IN GAME";
						spawnPlayers();
						startGame();
					} else if (commandNum == 1) { // exit game
						setVisible(false);
						System.exit(0);
					}
				}
			}
		}
	}
}
