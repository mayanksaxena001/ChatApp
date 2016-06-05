package com.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ClientUIController implements Initializable {

	@FXML
	Button logoutButton;
	
	@FXML
	Button connectButton;
	
	@FXML
	Button serverButton;
	
	@FXML
	Button exitButton;
	
	@FXML
	TextArea displayTextArea;
	
	@FXML
	TextArea chattingTextArea;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}


}
