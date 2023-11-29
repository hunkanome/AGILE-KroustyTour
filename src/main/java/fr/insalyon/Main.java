package fr.insalyon;

import java.io.FileInputStream;
import java.io.InputStream;

import fr.insalyon.controller.CityMapController;
import fr.insalyon.model.CityMap;
import fr.insalyon.xml.CityMapXMLParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		/* Loading map data */

		InputStream input = new FileInputStream("data/xml/mediumMap.xml");
		CityMapXMLParser parser = new CityMapXMLParser(input);
		CityMap map = parser.parse();

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
		FXMLLoader mapLoader = new FXMLLoader(getClass().getClassLoader().getResource("CityMap.fxml"));
		Parent cityMapCanvas = mapLoader.load();

		CityMapController cityMapController = mapLoader.getController();
		cityMapController.initialize(map);

		AnchorPane mapContainer = (AnchorPane) root.lookup("#mapContainer");
		mapContainer.getChildren().add(cityMapCanvas);
		Scene scene = new Scene(root, 1000, 700);

		primaryStage.setTitle("Calculateur de tours de livraison en vélo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}