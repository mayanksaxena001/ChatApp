package com.fxml;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientUIController implements Initializable {

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
	
	private String ipAddress;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpActions();
		try {
			ipAddress=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			ipAddress="";
		}
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
		
	}

	private void handleLogOutAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleServerAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleExitAction() {
		Stage stage=(Stage)centralVbox.getScene().getWindow();
		stage.close();
	}

	private void handleProfileAction() {
		// TODO Auto-generated method stub
		
	}

}
