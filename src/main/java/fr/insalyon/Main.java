package fr.insalyon;

import fr.insalyon.controller.MainController;
import fr.insalyon.model.DataModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		DataModel data = new DataModel();
		
		FXMLLoader rootLoader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
		Parent root = rootLoader.load();
		MainController mainController = rootLoader.getController();
		mainController.initialize(data);		

		Scene scene = new Scene(root);
		scene.getStylesheets().add("style.css");

		// Set the application icon
		primaryStage.getIcons().add(new Image("file:images/logo.png"));
		primaryStage.setTitle("KroustyTour");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}