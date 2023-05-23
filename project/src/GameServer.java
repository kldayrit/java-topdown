
/**
 https://github.com/BotsheloRamela/CubeNet/blob/main/src/main/java/org/example
 The GameServer class represents a server that coordinates communication between two clients playing a game.
 The server listens for incoming connections, accepts two clients, and starts two threads to handle communication
 between the two clients. The server constantly updates the positions of the players and sends the updated positions
 to the clients.
 */

import java.io.*;
import java.net.*;

public class GameServer {
	private ServerSocket serverSocket; // The ServerSocket object used to listen for incoming connections.
	// The number of players currently connected to the server. | The maximum number
	// of players that can connect to the server.
	private int numPlayers = 0, maxPlayers = 4;

	/**
	 * Constructs a new GameServer object and creates a ServerSocket to listen for
	 * incoming connections.
	 */
	public GameServer() {
		System.out.println("===== GAME SERVER =====");
		try {
			serverSocket = new ServerSocket(4545);
		} catch (IOException e) {
			System.out.println("IOException from GameServer");
		}
	}

	/**
	 * Listens for incoming connections and accepts two players. Starts two threads
	 * to handle communication between the players.
	 */
	public void acceptConnections() {
		try {
			System.out.println("Waiting for connections...");
			while (numPlayers < maxPlayers) {
				Socket socket = serverSocket.accept();
				numPlayers++;

				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				ClientHandler handler = new ClientHandler(numPlayers, socket, in, out);
				handler.start();

				out.writeInt(numPlayers);
				System.out.println("Player #" + numPlayers + " has connected.");

				if (numPlayers == 2) {
					System.out.println("Starting game!");
				}
			}
			System.out.println("No longer accepting connections");
		} catch (IOException e) {
			System.out.println("IOException from acceptConnection() in GameServer");
		}
	}

	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
		gameServer.acceptConnections();
	}
}