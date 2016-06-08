package com.fxml;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.server.ChatServerThread;
import com.server.Server;

import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerUIController implements Initializable {

	private static final int SERVER_PORT = 1991;
	@FXML
	private Button logoutButton;
	
	@FXML
	private Button exitButton;
	
	@FXML
	private Button serverButton;
	
	@FXML
	private TextArea displayTextArea;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private VBox centralVbox;
	
	private Server currentServer; 
	
	private static ServerUIController serverUIController;
	
	private ChatServerThread currentServerThread;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		init();
		setUpActions();
		try {
			currentServerThread=new ChatServerThread(currentServer.getPort(), currentServer.getServerIp());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() {
		serverUIController=this;
		String ipAddress="",hostName="";
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		currentServer=new Server(SERVER_PORT,ipAddress,hostName);
		descriptionLabel.setText(ipAddress);
	}
	
	private void setUpActions() {
		logoutButton.setOnAction((event) ->{
			handleLogOutAction();
		});
		exitButton.setOnAction((event) ->{
			handleExitAction();
		});
		serverButton.setOnAction((event) ->{
			handleServerAction();
		});
	
	}

	private void handleServerAction() {
		// TODO Auto-generated method stub
		createDialog();
	}

	private void handleExitAction() {
		//done
		Stage stage=(Stage)centralVbox.getScene().getWindow();
		stage.close();
		clear();
	}

	private void clear() {
		// TODO Auto-generated method stub
		try {
			currentServerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void handleLogOutAction() {
		clear();
		descriptionLabel.setText("Please connect to server ");
		
	}
	
	private void createDialog() {
		Dialog<Server> dialog=new Dialog<>();
		dialog.setTitle("Server Details");
		dialog.setResizable(false);
		dialog.setHeaderText("Enter server details and \n" +
				    "press Okay (or click title bar 'X' for cancel).");
		Label serverPort=new Label("Port");
		Label serverHostName=new Label("Host Name");
		Label serverIP=new Label("IP Address");
		TextField portTextField=new TextField();
		TextField hostNameTextField=new TextField();
		TextField ipTextField=new TextField();
		
		hostNameTextField.setText(currentServer.getHostName());
		portTextField.setText(Integer.toString(currentServer.getPort()));
		ipTextField.setText(currentServer.getServerIp());
		
		GridPane contentGridPane=new GridPane();
		contentGridPane.setHgap(10);
		contentGridPane.setVgap(10);
		contentGridPane.add(serverPort, 1	, 1);
		contentGridPane.add(portTextField, 2	, 1);
		contentGridPane.add(serverHostName, 1	, 2);
		contentGridPane.add(hostNameTextField, 2	, 2);
		contentGridPane.add(serverIP, 1	, 3);
		contentGridPane.add(ipTextField, 2	, 3);
		
		dialog.getDialogPane().setContent(contentGridPane);
		ButtonType buttonTypeOk = new ButtonType("Reset", ButtonData.OK_DONE);
		ButtonType buttonTypeConnect = new ButtonType("Reset Server");
		Observable property=portTextField.textProperty().isEmpty().and(ipTextField.textProperty().isEmpty());
		dialog.getDialogPane().getButtonTypes().addAll(buttonTypeConnect,buttonTypeOk);

		dialog.setResultConverter((b)->{
			        if (b == buttonTypeOk) {
			        	currentServer.setPort(Integer.parseInt(portTextField.getText()));
			        	currentServer.setServerIp( ipTextField.getText());
			        	currentServer.setHostName(hostNameTextField.getText());
			            return currentServer;
			        }else if(b==buttonTypeConnect){
//			        	clear();
			        	try {
			    			currentServerThread=new ChatServerThread(currentServer.getPort(), currentServer.getServerIp());
			    			Alert alert=new Alert(AlertType.INFORMATION);
			    			alert.setHeaderText("Successfully connected to server...");
			    			alert.setContentText(currentServer.toString());
			    			alert.showAndWait();
			    			dialog.close();
			    		} catch (IOException e) {
			    			Alert alert=new Alert(AlertType.ERROR);
							alert.setContentText(e.getMessage());
							alert.show();
							e.printStackTrace();
			    		}
			        }
			        return null;
			});
		dialog.showAndWait();
	}
	
	public TextArea getDisplayTextArea() {
		return displayTextArea;
	}

	public static ServerUIController getInstance(){
		return serverUIController;
	}
}
