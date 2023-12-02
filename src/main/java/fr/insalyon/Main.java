package fr.insalyon;

import fr.insalyon.controller.MainController;
import fr.insalyon.model.DataModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

		primaryStage.setTitle("Calculateur de tours de livraison en vélo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}