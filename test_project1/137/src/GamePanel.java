import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH= 1200;
	static final int SCREEN_HEIGHT= 600;

	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;


	boolean running = false;

	int score =0;

	String[] mColors = {
            "#39add1", // light blue
            "#c25975", // mauve
            "#838cc7", // lavender
            "#f092b0", // pink
            "#3079ab", // dark blue
            "#e15258", // red
            "#7d669e", // purple
            "#b7c0c7" // light gray
    };

	Timer timer;

	int speed = 2;

	// pang 4 players
	ArrayList<Player> players;

	GamePanel(){
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());


		this.players = new ArrayList<Player>();
		this.players.add(new Player(0,0,'R'));
		this.players.add(new Player(SCREEN_WIDTH- UNIT_SIZE,0,'D'));
		this.players.add(new Player(SCREEN_WIDTH- UNIT_SIZE,SCREEN_HEIGHT- UNIT_SIZE,'L'));
		this.players.add(new Player(0,SCREEN_HEIGHT- UNIT_SIZE,'U'));

		startGame();
	}

	public void startGame(){
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g){
		if(running){
			// grids para lang makita
			for(int i=0; i<SCREEN_WIDTH/UNIT_SIZE;i++){
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); // vertical
			}
			for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE;i++){
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); // horizontal
			}

			// draw player
			for(int i = 0; i<this.players.size(); i++){
				Player p = this.players.get(i);
				g.setColor(Color.decode(mColors[i]));
				if(p.alive == true){
					g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
				}

			}

			// draw bullets
			for(int i = 0; i<this.players.size(); i++){
				Player p = this.players.get(i);
				g.setColor(Color.decode(mColors[i+4]));
				for(int j=0; j<GAME_UNITS; j++){
					if ((p.bulletX[j] !=-1 ) || (p.bulletY[j] !=-1 ) || (p.bulletDirection[j] != 'A')){
						g.fillOval(p.bulletX[j], p.bulletY[j], UNIT_SIZE/2, UNIT_SIZE/2);
					}
				}
			}

			// draw score
			g.setColor(Color.MAGENTA);
			g.setFont(new Font("Arial", Font.BOLD, 25));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: " + score, (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2,100);



		} else {
			gameOver(g);
		}
	}

	public void move(){
		// para lang mag stop if tumama sa edge pwede pa tanggalin to
		for(int i=0; i<this.players.size();i++){
			Player p = this.players.get(i);
			switch(p.direction){
			case 'U':
				p.y = p.y- UNIT_SIZE;
				break;
			case 'D':
				p.y = p.y+ UNIT_SIZE;
				break;
			case 'L':
				p.x = p.x- UNIT_SIZE;
				break;
			case 'R':
				p.x = p.x+ UNIT_SIZE;
				break;
			}
		}

	}

	public void botMove(){
		for(int i=1;i<this.players.size(); i++){
			Player p = this.players.get(i);
			Random r = new Random();
			if (r.nextInt(2) == 1){
				switch(p.direction){
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

	public void checkCollisions(){
		// left border
		for(int i=0; i<this.players.size();i++){
			Player p = this.players.get(i);
			if (p.x < 0){
				p.x=0;
			}
			// right border
			if (p.x==SCREEN_WIDTH){
				p.x = SCREEN_WIDTH- UNIT_SIZE;
			}
			// top border
			if (p.y < 0){
				p.y=0;
			}
			// bottom border
			if (p.y==SCREEN_HEIGHT){
				p.y= SCREEN_HEIGHT- UNIT_SIZE;
			}
		}

		// check if may tumama sa mga bullet ng bot sa player
		Player p = this.players.get(0);
		for(int i=1; i<this.players.size();i++){
			Player b = this.players.get(i);
			for(int j=0; j<GAME_UNITS;j++){
				// if tumama bullet ng kalaban sa player
				if(((b.bulletX[j] >= p.x) && (b.bulletX[j] <= p.x+UNIT_SIZE)) && ((b.bulletY[j] >= p.y) && (b.bulletY[j] <= p.y+UNIT_SIZE)) && b.alive){
					running = false;
				}
				// if tumama bullet ng player sa kalaban
				if(((p.bulletX[j] >= b.x) && (p.bulletX[j] <= b.x+UNIT_SIZE)) && ((p.bulletY[j] >= b.y) && (p.bulletY[j] <= b.y+UNIT_SIZE)) && b.alive){
					b.alive = false;
					score++;
					// respawn dito muna
					b.x = SCREEN_WIDTH/2;
					b.y = SCREEN_HEIGHT/2;
					b.alive = true;
				}
			}
		}
		// lagay so ng collision ng player to player


	}

	public void fireBullets(){
		for(int i =0; i<GAME_UNITS; i++){
			Player p = this.players.get(0);

			if ((p.bulletX[i] == -1) && (p.bulletY[i] == -1) && (p.bulletDirection[i] == 'A')){
				switch(p.direction){
				case 'U':
					p.bulletX[i] = p.x;
					p.bulletY[i] = p.y-UNIT_SIZE;
					p.bulletDirection[i] = 'U';
					break;
				case 'D':
					p.bulletX[i] = p.x;
					p.bulletY[i] = p.y+UNIT_SIZE;
					p.bulletDirection[i] = 'D';
					break;
				case 'L':
					p.bulletX[i] = p.x-UNIT_SIZE;
					p.bulletY[i] = p.y;
					p.bulletDirection[i] = 'L';
					break;
				case 'R':
					p.bulletX[i] = p.x+UNIT_SIZE;
					p.bulletY[i] = p.y;
					p.bulletDirection[i] = 'R';
					break;
				}
				break;
			}
		}
	}

	public void botBullets(){
		for(int i=1;i<this.players.size(); i++){
			Player p = this.players.get(i);
			Random r = new Random();
			if (r.nextInt(2) == 1  && p.alive){
				if ((p.bulletX[i] == -1) && (p.bulletY[i] == -1) && (p.bulletDirection[i] == 'A')){
					switch(p.direction){
					case 'U':
						p.bulletX[i] = p.x;
						p.bulletY[i] = p.y-UNIT_SIZE;
						p.bulletDirection[i] = 'U';
						break;
					case 'D':
						p.bulletX[i] = p.x;
						p.bulletY[i] = p.y+UNIT_SIZE;
						p.bulletDirection[i] = 'D';
						break;
					case 'L':
						p.bulletX[i] = p.x-UNIT_SIZE;
						p.bulletY[i] = p.y;
						p.bulletDirection[i] = 'L';
						break;
					case 'R':
						p.bulletX[i] = p.x+UNIT_SIZE;
						p.bulletY[i] = p.y;
						p.bulletDirection[i] = 'R';
						break;
					}
					break;
				}
			}
		}
	}

	public void moveBullets(){
		for(int j = 0; j<this.players.size(); j++){
			Player p = this.players.get(j);
			for(int i = 0; i < GAME_UNITS; i++){
				switch(p.bulletDirection[i]){
				case 'U':
					p.bulletY[i] = p.bulletY[i]-speed*UNIT_SIZE;
					break;
				case 'D':
					p.bulletY[i] = p.bulletY[i]+speed*UNIT_SIZE;
					break;
				case 'L':
					p.bulletX[i] = p.bulletX[i]-speed*UNIT_SIZE;
					break;
				case 'R':
					p.bulletX[i] = p.bulletX[i]+speed*UNIT_SIZE;
					break;
				}
				// tanggalin if lumampas na sa screen
				if((p.bulletX[i] > SCREEN_WIDTH ) || (p.bulletX[i] < 0 ) || (p.bulletY[i] > SCREEN_HEIGHT ) || (p.bulletY[i] < 0) ){
					p.bulletX[i] = -1;
					p.bulletY[i] = -1;
					p.bulletDirection[i] = 'A';
				}
			}
		}
//
	}



	public void gameOver(Graphics g){
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("Arial", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
	}

	// dito yung parang per frame
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(running){
			move();
			botMove();
			checkCollisions();
			botBullets();
			moveBullets();

		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			// kasi right key lang pwede
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				Player p = players.get(0);
				switch(p.direction){
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

			// dito yung bullet
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				fireBullets();
			}
		}
	}

}
