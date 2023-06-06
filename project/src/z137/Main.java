package z137;
import java.awt.Button;
import java.awt.Container;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import common.Hub;

public class Main  extends JFrame implements ActionListener {

	private JButton button1;
	private JButton button2;

    public Main(){

	    button1=new JButton("Server");
	    button1.setBounds(50,100,95,30);
	    this.button1.addActionListener(this);
	    add(button1);
	    button2=new JButton("Client");
	    button2.setBounds(200,100,95,30);
	    this.button2.addActionListener(this);
	    add(button2);
	    setSize(400,400);
	    setLayout(null);
	    setVisible(true);
        setTitle("Please Gumana ka");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	 public void actionPerformed(ActionEvent e) {
		 JFrame f = new JFrame();
	        if(e.getSource() == button1){
	        	int port = 5000;
	            try {
	                if (port <= 0)
	                    throw new Exception();
	            }
	            catch (Exception a) {
	                return;
	            }
	            Hub hub;
	            try {
	                hub = new GameHub(port);
	            }
	            catch (Exception a) {
	                return;
	            }
	            f.add(new GameWindow("localhost", port));

	        }
	        if(e.getSource() == button2){
	        	String host = "localhost";
	            int port = 5000;
	            if (host.length() == 0) {
	                return;
	            }
	            try {
	                if (port <= 0)
	                    throw new Exception();
	            }
	            catch (Exception a) {
	                return;
	            }
	            f.add(new GameWindow(host, port));
//
	        }
	        f.setResizable(false);
            f.setVisible(true);
            f.pack();
            f.setAlwaysOnTop(true);
            setTitle("Please Gumana ka");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setVisible(false);
	 }
	public static void main(String[] args) {
			Main main = new Main();
	}
}
