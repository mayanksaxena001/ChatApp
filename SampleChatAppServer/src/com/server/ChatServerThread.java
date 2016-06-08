package com.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import com.fxml.ServerUIController;
import com.server.thread.ServerThread;
import com.ui.ServerWindow;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class ChatServerThread extends Thread{

	private ServerSocket serverSocket = null;
	
	private Socket socket;

	private Map<Socket,OutputStream> outputStreams = new HashMap<Socket,OutputStream>();
	
	private ServerUIController serverUIController=ServerUIController.getInstance();

	public ChatServerThread(int port,String ip) throws IOException {
//			initGUI();
			listen(port,ip);
			setName(Integer.toString(socket.getPort()));
	}

	@Deprecated
	private void initGUI() {
//		window=new ServerWindow();
//		textWindow=window.getTextWindow();
	}

	private void listen(int port,String ip) throws IOException {
		InetAddress addr =InetAddress.getByName(ip);
		serverSocket = new ServerSocket(port,100,addr);
//		window.setServerSocket(serverSocket);
		String message="Listening on " + serverSocket;
		System.out.println(message);// display on console
		serverUIController.getDisplayTextArea().appendText(message+"\n");
		try {
			while ((socket = serverSocket.accept()) != null) {
				
				serverUIController.getDisplayTextArea().appendText("connected to "+socket+"\n");
			System.out.println("connected to "+socket+"\n");	
				// Create a DataOutputStream for writing data to the
				// other side
				DataOutputStream dout = new DataOutputStream(
						socket.getOutputStream());
				// Save this stream so we don't need to make it again
				outputStreams.put(socket, dout);
				// Create a new thread for this connection, and then forget
				// about it
				new Thread(this).start();;
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
			serverUIController.getDisplayTextArea().appendText(message+"\n");
			// Remove it from our hashtable/list
			outputStreams.remove(socket);
			for (Map.Entry<Socket,OutputStream> map:getOutputStreams().entrySet()) {
				// ... get the output stream ...
				DataOutputStream dout = (DataOutputStream) map.getValue();
				// ... and send the message
				if(dout!=null){
					try {
						dout.writeUTF("The socket "+socket+" has logged out");
					} catch (IOException e) {
						e.printStackTrace();
					}
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
					sendToAll(message);
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
			removeConnection(socket);
		}

	}
	// Get an enumeration of all the OutputStreams, one for each client
	// connected to us
	Map<Socket,OutputStream> getOutputStreams() {
		return outputStreams;
	}
}
