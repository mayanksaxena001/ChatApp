package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.fxml.ClientUIController;
import com.ui.MainFrame;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class ChatClientThread extends Thread {

	private Socket socket = null;

	private DataInputStream dataInputStream;

	private DataOutputStream dataOutputStream;
	
	private ClientUIController clientUIController=ClientUIController.getInstance();

	public ChatClientThread(String host, int port) throws IOException {
//		initGUI();
		try {
			initiateConnection(host, port);
			setName(Integer.toString(socket.getPort()));
		} catch (IOException e) {
			Thread.currentThread().interrupt();
			throw new IOException();
		}
	}

	private void initiateConnection(String host, int port) throws UnknownHostException, IOException {
			socket = new Socket(host, port);
//			mainframe.setSocket(socket);
			// We got a connection! Tell the world
			System.out.println("connected to " + socket);

			// Let's grab the streams and create DataInput/Output streams
			// from them
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
//			mainframe.setDataInputStream(dataInputStream);
//			mainframe.setDataOutputStream(dataOutputStream);
			// Start a background thread for receiving messages
			new Thread(this).start();
	}

	public Socket getSocket() {
		return socket;
	}

	@Override
	public void run() {
		// Receive messages one-by-one, forever
		try {
		while (true && !Thread.currentThread().isInterrupted()) {
			// Get the next message
			String message = "";
				if ( socket.isConnected() && !socket.isClosed()){
					message = dataInputStream.readUTF();
					// Print it to our text window
					clientUIController.getDisplayTextArea().appendText(message + "\n");
				}
				else{
					this.interrupt();
					break;
				}
		}
		} catch (IOException e) {
			this.interrupt();
			System.out.println("Socket connection closed");
		}finally {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public  DataInputStream getDataInputStream() {
		return dataInputStream;
	}

	public DataOutputStream getDataOutputStream() {
		return dataOutputStream;
	}

	public void  destroy(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.interrupt();
 }
//	public static void main(String[] args) {
//		try 
//	    { 
//	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
//	    } 
//	    catch(Exception e){ 
//	    }
//		new ChatClientThread("192.168.2.8",
//				SERVER_PORT);
//		System.out.println("");
//	}
}
