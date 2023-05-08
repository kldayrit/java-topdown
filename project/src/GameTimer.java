import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;

public class GameTimer implements ActionListener {
	private Timer timer;
	private int timeRemaining;

	public GameTimer(int seconds) {
		timeRemaining = seconds;
		timer = new Timer(1000, this);
		timer.setInitialDelay(0);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (timeRemaining > 0) {
			timeRemaining--;
			// update game state here
		} else {
			timer.stop();
			// game over logic here
		}
	}

	public int getTimeRemaining() {
		return timeRemaining;
	}

}
