import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatBox extends JFrame {
    private JTextArea chatTextArea;
    private JTextField chatTextField;
    private JButton sendButton;
    private MulticastSocket socket;
    private InetAddress group;
    private int port;

    public ChatBox() {
        initGUI();
        initNetwork();
    }

    private void initGUI() {
        setTitle("Chat");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);

        chatTextField = new JTextField();

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatTextField.getText().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
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
            group = InetAddress.getByName("239.0.0.1");
            port = 8888;
            socket = new MulticastSocket(port);
            socket.setTimeToLive(0);
            socket.joinGroup(group);

            Thread receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[1000];
                    DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
                    while (true) {
                        try {
                            socket.receive(datagram);
                            String message = new String(buffer, 0, datagram.getLength());
                            appendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            receiveThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            message = "You: " + message;
            byte[] buffer = message.getBytes();
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(datagram);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendMessage(String message) {
        chatTextArea.append(message + "\n");
        chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
    }
} 