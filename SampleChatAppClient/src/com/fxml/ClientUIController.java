package com.fxml;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.client.ChatClientThread;
import com.profile.Profile;
import com.server.Server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientUIController implements Initializable {

	private static ClientUIController clientUIController;
	
	private static final int SERVER_PORT = 1991;
	private static final String SERVER_IP = "192.168.2.8";
	
	@FXML
	private Button profileButton;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private Button serverButton;
	
	@FXML
	private Button exitButton;
	
	@FXML
	private TextArea displayTextArea;
	
	@FXML
	private TextArea chattingTextArea;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private VBox centralVbox;
	
	private Profile currentProfile;
	
	private Server currentServer; 
	
	private ChatClientThread currentChatClientThread;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
		setUpActions();
	}

	private void init() {
		clientUIController=this;
		String ipAddress,hostName="Guest";
		try {
			ipAddress=InetAddress.getLocalHost().getHostAddress();
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			ipAddress="";
		}
		currentServer=new Server(SERVER_PORT,SERVER_IP);
		currentProfile=new Profile(hostName,ipAddress);
		descriptionLabel.setText(ipAddress);
	}

	private void setUpActions() {
		profileButton.setOnAction((event) ->{
			handleProfileAction();
		});
		logoutButton.setOnAction((event) ->{
			handleLogOutAction();
		});
		exitButton.setOnAction((event) ->{
			handleExitAction();
		});
		serverButton.setOnAction((event) ->{
			handleServerAction();
		});
		chattingTextArea.setOnKeyReleased((event)->{
			if(event.getCode().equals(KeyCode.ENTER)){
				try {
					handleKeyReleasedAction();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void handleKeyReleasedAction() throws IOException {
		if (currentChatClientThread.getSocket() != null) {
			currentChatClientThread.getDataOutputStream().writeUTF(currentProfile.getName() + " : "
					+ chattingTextArea.getText());
			// Clear out text input field
			chattingTextArea.setText("");
		}
	}

	private void handleLogOutAction() {
		// TODO remove current profile
		
	}

	private void handleServerAction() {
		// TODO server window for changing server port and address
		Dialog<Server> dialog=new Dialog<>();
		dialog.setTitle("Server Details");
		dialog.setResizable(false);
		dialog.setHeaderText("Enter server details and \n" +
				    "press Okay (or click title bar 'X' for cancel).");
		Label serverPort=new Label("Port");
		Label serverIP=new Label("IP Address");
		TextField portTextField=new TextField();
		TextField ipTextField=new TextField();
		
		portTextField.setText(Integer.toString(currentServer.getPort()));
		ipTextField.setText(currentServer.getServerIp());
		
		GridPane contentGridPane=new GridPane();
		contentGridPane.add(serverPort, 1	, 1);
		contentGridPane.add(portTextField, 2	, 1);
		contentGridPane.add(serverIP, 1	, 2);
		contentGridPane.add(ipTextField, 2	, 2);
		
		dialog.getDialogPane().setContent(contentGridPane);
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

		dialog.setResultConverter((b)->{
			        if (b == buttonTypeOk) {
			        	currentServer.setPort(Integer.parseInt(portTextField.getText()));
			        	currentServer.setServerIp( ipTextField.getText());
			            return currentServer;
			        }
			        return null;
			});
		dialog.showAndWait();
	}

	private void handleExitAction() {
		//done
		Stage stage=(Stage)centralVbox.getScene().getWindow();
		stage.close();
	}

	private void handleProfileAction() {
		//TODO profile window containing name and hostname
	}

	public TextArea getDisplayTextArea() {
		return displayTextArea;
	}
	
	public static ClientUIController getInstance(){
		return clientUIController;
	}
}
