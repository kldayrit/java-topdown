/**
 * https://github.com/BotsheloRamela/CubeNet/blob/main/src/main/java/org/example/
 * A class representing a client handler for the game server.
 * Each instance of this class represents a client connected to the server.
 * It manages the communication with the client, exchanging player positions with the other client.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private int numPlayers = 0, maxPlayers = 4;
    private double[][] playerPositions = {{100, 400}, {490, 400}};
    private int playerId;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * Creates a new instance of the ClientHandler class.
     * @param playerId the ID of the player being handled by this client handler
     * @param socket the socket associated with the client
     * @param in the input stream associated with the socket
     * @param out the output stream associated with the socket
     */
    public ClientHandler(int playerId, Socket socket, DataInputStream in, DataOutputStream out) {
        this.playerId = playerId;
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    /**
     * Runs the client handler thread.
     * Sends the welcome message to the client and waits until the maximum number of players is reached.
     * Then, continuously exchanges player positions with the other client.
     */
    @Override
    public void run() {
        try {
            // Send welcome message and initial player position to client
            out.writeInt(numPlayers);
            out.writeUTF("Welcome, player #" + playerId + "!");
            out.writeDouble(playerPositions[playerId-1][0]);
            out.writeDouble(playerPositions[playerId-1][1]);
            out.flush();

            // Wait until the maximum number of players is reached
            while (numPlayers < maxPlayers) {
                Thread.sleep(1000);
            }

            // Continuously exchange player positions with the other client
            while (true) {
                int otherPlayerId = (playerId == 1) ? 2 : 1;
                double otherPlayerX = playerPositions[otherPlayerId-1][0];
                double otherPlayerY = playerPositions[otherPlayerId-1][1];
                out.writeDouble(otherPlayerX);
                out.writeDouble(otherPlayerY);
                out.flush();

                double myPlayerX = in.readDouble();
                double myPlayerY = in.readDouble();
                playerPositions[playerId-1][0] = myPlayerX;
                playerPositions[playerId-1][1] = myPlayerY;
            }
        } catch (IOException e) {
            System.out.println("IOException from ClientHandler run()");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException from ClientHandler run()");
        } finally {
            // Close the socket
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("IOException from ClientHandler finally block");
            }
        }
    }
}