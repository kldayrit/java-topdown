import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatBox extends JFrame {
	private JTextArea chatTextArea;
	private JTextField chatTextField;
	private JButton sendButton;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public ChatBox() {
		initGUI();
		initNetwork();
	}

	private void initGUI() {
		setTitle("4th Impact Chat");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatTextArea);
		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());

		chatTextField = new JTextField();
		chatTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = chatTextField.getText().trim();
				if (!message.isEmpty()) {
					out.println(message);
					chatTextField.setText("");
				}
			}
		});

		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = chatTextField.getText().trim();
				if (!message.isEmpty()) {
					out.println(message);
					chatTextField.setText("");
				}
			}
		});

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(chatTextField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(inputPanel, BorderLayout.SOUTH);
	}

	private void initNetwork() {
		try {
			socket = new Socket("localhost", 8080);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String message;
						while ((message = in.readLine()) != null) {
							chatTextArea.append(message + "\n");
							JScrollBar verticalScrollBar = ((JScrollPane) chatTextArea.getParent().getParent()
									.getParent().getParent().getParent().getParent().getParent().getParent().getParent()
									.getParent()).getVerticalScrollBar();
							verticalScrollBar.setValue(verticalScrollBar.getMaximum());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatBox chatBox = new ChatBox();
		chatBox.setVisible(true);
	}
}
