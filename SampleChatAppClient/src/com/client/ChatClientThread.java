package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.ui.MainFrame;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class ChatClientThread  implements Runnable {

	private static final int SERVER_PORT = 1991;

	private Socket socket = null;

	private DataInputStream dataInputStream;

	private DataOutputStream dataOutputStream;

	JTextArea textDisplayWindowArea;

	JTextArea textTypingWindowArea;

	private MainFrame mainframe=null;

	public ChatClientThread(String host, int port) {
		initGUI();
		initiateConnection(host, port);
	}

	private void initiateConnection(String host, int port) {
		try {
			socket = new Socket(host, port);
			mainframe.setSocket(socket);
			// We got a connection! Tell the world
			System.out.println("connected to " + socket);

			// Let's grab the streams and create DataInput/Output streams
			// from them
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			mainframe.setDataInputStream(dataInputStream);
			mainframe.setDataOutputStream(dataOutputStream);
			// Start a background thread for receiving messages
			new Thread(this).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// Receive messages one-by-one, forever
		while (socket.isConnected() && !socket.isClosed()) {
			// Get the next message
			String message = "";
			try {
				if (dataInputStream != null)
					message = dataInputStream.readUTF();
				// Print it to our text window
				textDisplayWindowArea.append(message + "\n");
			} catch (IOException e) {
				System.out.println("Socket connection closed");
			}
		}
	}

	private void initGUI() {
		mainframe=new MainFrame();
		textDisplayWindowArea=mainframe.getTextDisplayWindowArea();
		textTypingWindowArea=mainframe.getTextTypingWindowArea();
	}

	public static void main(String[] args) {
		try 
	    { 
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
	    } 
	    catch(Exception e){ 
	    }
		new ChatClientThread("192.168.2.8",
				SERVER_PORT);
		System.out.println("");
	}
}
