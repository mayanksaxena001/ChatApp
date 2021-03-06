package com.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientUIMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/clientUI.fxml"));
		Parent parent=loader.load();
		Scene scene=new Scene(parent);
		scene.getStylesheets().add("/application-style.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Sample Client Chat App");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
