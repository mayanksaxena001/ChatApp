package com.server;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fxml.ServerUIController;
import com.server.thread.ServerThread;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class ChatServer extends Thread {

	private ServerSocket serverSocket = null;
	
	private Socket socket;

	private Map<Socket,OutputStream> outputStreams = new HashMap<Socket,OutputStream>();
	
	private Map<Socket,Thread> threads = new HashMap<Socket,Thread>();
	
	private ServerUIController serverUIController=ServerUIController.getInstance();

	private String ip;

	private int port;

	public ChatServer(int port,String ip) throws IOException {
		this.port=port;
		this.ip=ip;
		setName(InetAddress.getLocalHost().getHostName());
			start();
	}
	
	@Override
	public void run() {
		super.run();
		threads.clear();
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen() throws IOException {
		InetAddress addr =InetAddress.getByName(ip);
		serverSocket = new ServerSocket(port,100,addr);
		String message="Listening on " + serverSocket;
		System.out.println(message);// display on console
		serverUIController.getDisplayTextArea().appendText(message+"\n");
		try {
			while ((socket = serverSocket.accept()) != null) {
				
				serverUIController.getDisplayTextArea().appendText("connected to "+socket+"\n");
			System.out.println("connected to "+socket);	
				// Create a DataOutputStream for writing data to the
				// other side
				DataOutputStream dout = new DataOutputStream(
						socket.getOutputStream());
				// Save this stream so we don't need to make it again
				outputStreams.put(socket, dout);
				// Create a new thread for this connection, and then forget
				// about it
				ServerThread thread=new ServerThread(this,socket);
				threads.put(socket, thread);
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
			threads.remove(socket);
			for (Map.Entry<Socket,OutputStream> map:getOutputStreams().entrySet()) {
				// ... get the output stream ...
				DataOutputStream dout = (DataOutputStream) map.getValue();
				// ... and send the message
				if(dout!=null && !map.getKey().isClosed()){
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

	public void destroyAllThreads() throws IOException{
		for(Entry<Socket, Thread> entry:threads.entrySet()){
			entry.getKey().close();
			entry.getValue().interrupt();
		}
		serverSocket.close();
		this.interrupt();
	}
	// Get an enumeration of all the OutputStreams, one for each client
	// connected to us
	Map<Socket,OutputStream> getOutputStreams() {
		return outputStreams;
	}
}
