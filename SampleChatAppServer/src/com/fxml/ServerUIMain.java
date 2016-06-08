package com.fxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerUIMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/serverUI.fxml"));
		Parent parent=loader.load();
		Scene scene=new Scene(parent);
		scene.getStylesheets().add("/application-style.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Sample Chat App Server");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
