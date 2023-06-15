package z137;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import common.Hub;

public class Main  extends JFrame implements ActionListener {

	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JLabel label;

    public Main(){
    	JLabel label = new JLabel("4TH IMPACT");
        label.setBounds(95, 50, 200, 50); // Set x, y, width, height
        label.setFont(new Font("Arial", Font.BOLD, 32)); // Set font style
        add(label);
	    button1=new JButton("Server");
	    button1.setBounds(80,150,95,30);
	    this.button1.addActionListener(this);
	    add(button1);
	    button2=new JButton("Client");
	    button2.setBounds(200,150,95,30);
	    this.button2.addActionListener(this);
	    add(button2);
	    button3=new JButton("Solo");
	    button3.setBounds(140,200,95,30);
	    this.button3.addActionListener(this);
	    add(button3);
	    setSize(400,400);
	    setLayout(null);
	    setVisible(true);
	    setTitle("4thImpact : Menu");
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
	            f.add(new GameWindow("localhost", port, f));

	        }
	        if(e.getSource() == button2){
	        	String host = JOptionPane.showInputDialog(this, "Enter the host address:");
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
	            f.add(new GameWindow(host, port, f));
//
	        }
	        if(e.getSource() == button3){
	            f.add(new Board());
//
	        }
	        f.setResizable(false);
            f.setVisible(true);
            f.pack();
            f.setAlwaysOnTop(true);
            setTitle("4thImpact : SPACE - FIRE, RIGHT KEY - MOVE");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setVisible(false);
	 }
	public static void main(String[] args) {
			Main main = new Main();
	}
}
