package com.server;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import com.server.thread.ServerThread;
import com.ui.ServerWindow;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class ChatServer {

	private static final int SERVER_PORT = 1991;
	
	private final static String IP="192.168.2.8";

	private ServerSocket serverSocket = null;

	private Map<Socket,OutputStream> outputStreams = new HashMap<Socket,OutputStream>();
	
	private ServerWindow window=null;
	
	private JTextArea textWindow=null;

	public ChatServer(int port) {
		try {
			initGUI();
			listen(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initGUI() {
		window=new ServerWindow();
		textWindow=window.getTextWindow();
	}

	private void listen(int port) throws IOException {
		InetAddress addr =InetAddress.getByName(IP);
		serverSocket = new ServerSocket(port,100,addr);
		window.setServerSocket(serverSocket);
		
		String message="Listening on " + serverSocket;
		System.out.println(message);// display on console
		textWindow.append(message+"\n");
		try {
			Socket socket;
			while ((socket = serverSocket.accept()) != null) {
				
			textWindow.append("connected to "+socket+"\n");	
				// Create a DataOutputStream for writing data to the
				// other side
				DataOutputStream dout = new DataOutputStream(
						socket.getOutputStream());
				// Save this stream so we don't need to make it again
				outputStreams.put(socket, dout);
				// Create a new thread for this connection, and then forget
				// about it
				new ServerThread(this, socket);
				socket = null;
			}
		} catch (Exception e) {
			System.out.println("Server Socket connection closed");
		} finally {
			serverSocket.close();
		}
	}

	public void sendToAll(String message) {
		// We synchronize on this because another thread might be
		// calling removeConnection() and this would screw us up
		// as we tried to walk through the list
		synchronized (outputStreams) {
			// For each client ...
			for (Map.Entry<Socket,OutputStream> map:getOutputStreams().entrySet()) {
				// ... get the output stream ...
				DataOutputStream dout = (DataOutputStream) map.getValue();
				// ... and send the message
				try {
					dout.writeUTF(message);
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	public void removeConnection(Socket socket) {
		// Synchronize so we don't mess up sendToAll() while it walks
		// down the list of all output streams
		synchronized (outputStreams) {
			// Tell the world
			String message="Removing connection to " + socket;
			System.out.println(message);
			textWindow.append(message+"\n");
			// Remove it from our hashtable/list
			outputStreams.remove(socket);
			for (Map.Entry<Socket,OutputStream> map:getOutputStreams().entrySet()) {
				// ... get the output stream ...
				DataOutputStream dout = (DataOutputStream) map.getValue();
				// ... and send the message
				try {
					dout.writeUTF("The socket "+socket+" has logged out");
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
			// Make sure it's closed
			try {
				socket.close();
			} catch (IOException ie) {
				System.out.println("Error closing " + socket);
				ie.printStackTrace();
			}
		}

	}

	// Get an enumeration of all the OutputStreams, one for each client
	// connected to us
	Map<Socket,OutputStream> getOutputStreams() {
		return outputStreams;
	}

	public static void main(String[] args) {

		// Create a Server object, which will automatically begin
		// accepting connections.
		new ChatServer(SERVER_PORT);

	}
}
