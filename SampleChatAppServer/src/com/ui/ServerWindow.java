package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class ServerWindow extends JFrame {

	private JTextArea textWindow=null;
	
	private ServerSocket serverSocket=null;
	
	static{
		try 
	    { 
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
	    } 
	    catch(Exception e){ 
	    }	
	}
	
	public ServerWindow() {
		init();
	}
	private void init() {
		setTitle("Server Window");
		add(getMainPanel());
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(this);
		setSize(new Dimension(500,300));
		setVisible(true);
	}
	private Component getMainPanel() {
		JPanel mainPanel=new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder());
		
		textWindow=new JTextArea();
		textWindow.setBackground(new Color(211, 211, 211));
		textWindow.setEditable(false);
		textWindow.setLineWrap(true);
		textWindow.setWrapStyleWord(true);
		
		DefaultCaret caret=(DefaultCaret)textWindow.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane = new JScrollPane(textWindow);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(scrollPane,BorderLayout.CENTER);
		
		return mainPanel;
	}
	public static void main(String[] args) {
		new ServerWindow();
	}
	public JTextArea getTextWindow() {
		return textWindow;
	}
	public void setTextWindow(JTextArea textWindow) {
		this.textWindow = textWindow;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if(serverSocket!=null){
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
