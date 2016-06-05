package com.server.thread;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import com.server.ChatServer;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class ServerThread extends Thread {

	private ChatServer chatServer;
	private Socket socket;

	public ServerThread(ChatServer chatServer, Socket socket) {
		this.chatServer = chatServer;
		this.socket = socket;
		setName(Integer.toString(socket.getPort()));
		start();
	}

	@Override
	public void run() {
		try {
			// Create a DataInputStream for communication; the client
			// is using a DataOutputStream to write to us
			DataInputStream dataInputStream = new DataInputStream(
					socket.getInputStream());
			// Over and over, forever ...
			while (true && !Thread.currentThread().isInterrupted()) {
				// ... read the next message ...
				if (socket.isConnected() && !socket.isClosed()) {
					String message = dataInputStream.readUTF();
					// ... tell the world ...
					// System.out.println("Sending " + message);
					// ... and have the server send it to all clients
					chatServer.sendToAll(message);
				} else {
					socket.close();
					this.interrupt();
					break;
				}
			}
		} catch (Exception ie) {
			ie.getStackTrace();
			try {
				if (!socket.isClosed())
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			// The connection is closed for one reason or another,
			// so have the server dealing with it
			chatServer.removeConnection(socket);
		}

	}
}
