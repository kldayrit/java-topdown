package z137;

import java.io.Serializable;
import java.util.*;


public class GameState implements Serializable{
	public List<Player> players;
	public boolean ingame;
	public boolean playerDisconnected;


	public void applyMessage(int sender, Object message) {
		if(ingame){
			List<Player> sent = (List<Player>) message;
			this.players = sent;
		}
    }
	void startFirstGame() {
        startGame();
    }

	private void startGame() {
		ChatBox chatBox = new ChatBox();
        chatBox.setVisible(true);
		players = new ArrayList<>();
		players.add(new Player(0, 50,1,0));
		players.add(new Player(400, 50,0,1));
		players.add(new Player(400, 400,-1,0));
		players.add(new Player(0, 400,0,-1));
		ingame = true;
    }
}
