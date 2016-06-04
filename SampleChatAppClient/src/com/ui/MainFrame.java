package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

/**
 * 
 * @author Mayank_Saxena
 *
 */
public class MainFrame extends JFrame {

	JTextArea textDisplayWindowArea;

	JTextArea textTypingWindowArea;

	JScrollPane scrollPane = null;
	
	private String clientName = "";
	
	private Socket socket=null;
	
	private DataInputStream dataInputStream;

	private DataOutputStream dataOutputStream;
	
	private final String CHAT_APP_NAME = "Sample Chat Client App";
	
	public MainFrame() {
		init();
		setClientNameWindow();
	}

	private void init() {
		setPreferredSize(new Dimension(400, 600));
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		add(getTopMenuPanel(),BorderLayout.NORTH);
		add(getTopChatPanel(), BorderLayout.CENTER);
		add(getBottomChatPanel(), BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}
	
	private Component getTopChatPanel() {
		JPanel topChatWindowPanel = new JPanel();

		topChatWindowPanel.setLayout(new BorderLayout());
		topChatWindowPanel.setBorder(BorderFactory.createEtchedBorder());
		topChatWindowPanel.setOpaque(true);
		topChatWindowPanel.setPreferredSize(new Dimension(350, 300));

		textDisplayWindowArea = new JTextArea();
		textDisplayWindowArea.setBackground(new Color(211, 211, 211));
		textDisplayWindowArea.setEditable(false);
		
		DefaultCaret caret=(DefaultCaret)textDisplayWindowArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		textDisplayWindowArea.setLineWrap(true);
		textDisplayWindowArea.setWrapStyleWord(true);

		scrollPane = new JScrollPane(textDisplayWindowArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		topChatWindowPanel.add(scrollPane);
		scrollPane = null;
		return topChatWindowPanel;
	}

	private Component getBottomChatPanel() {
		JPanel bottomTypingWindowPanel = new JPanel();

		bottomTypingWindowPanel.setLayout(new BorderLayout());
		bottomTypingWindowPanel.setBorder(BorderFactory.createEmptyBorder());
		bottomTypingWindowPanel.setPreferredSize(new Dimension(300, 100));
		
		textTypingWindowArea = new JTextArea();
		textTypingWindowArea.setBackground(new Color(211, 211, 211));
		textTypingWindowArea.addKeyListener(new TypingKeyListener());
		
		textTypingWindowArea.setLineWrap(true);
		textTypingWindowArea.setWrapStyleWord(true);

		
		DefaultCaret caret=(DefaultCaret)textTypingWindowArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollPane = new JScrollPane(textTypingWindowArea);
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMinimum() );
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		bottomTypingWindowPanel.add(scrollPane);
		return bottomTypingWindowPanel;
	}

	@SuppressWarnings("rawtypes")
	public void setClientNameWindow() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				String result = JOptionPane.showInputDialog(this,
						"Enter your chatting name otherwise default will be set");
				if (null != result && !result.isEmpty()) {
					clientName = result;
				}
				else{
					clientName="Default";
				}
				setTitle(CHAT_APP_NAME + " : Chat Name : "+clientName);
				
			}
		});
	}
	
	@Override
	public void dispose() {
		super.dispose();
		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
				dataInputStream = null;
				dataOutputStream = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Component getTopMenuPanel() {
		JPanel topPanel=new JPanel();
		
		JButton exitButton=new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(exitButton);
		
		return topPanel;
	}
	
	public JTextArea getTextDisplayWindowArea() {
		return textDisplayWindowArea;
	}

	public void setTextDisplayWindowArea(JTextArea textDisplayWindowArea) {
		this.textDisplayWindowArea = textDisplayWindowArea;
	}

	public JTextArea getTextTypingWindowArea() {
		return textTypingWindowArea;
	}

	public void setTextTypingWindowArea(JTextArea textTypingWindowArea) {
		this.textTypingWindowArea = textTypingWindowArea;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setDataInputStream(DataInputStream dataInputStream) {
		this.dataInputStream = dataInputStream;
	}

	public void setDataOutputStream(DataOutputStream dataOutputStream) {
		this.dataOutputStream = dataOutputStream;
	}



	class TypingKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				e.consume();
				try {
					// Send it to the server
					if (socket != null) {
						dataOutputStream.writeUTF(clientName + " : "
								+ textTypingWindowArea.getText());
						// Clear out text input field
						textTypingWindowArea.setText("");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try 
	    { 
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
	    } 
	    catch(Exception e){ 
	    }
		new MainFrame();
		
	}
}
