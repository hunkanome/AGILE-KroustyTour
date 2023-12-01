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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		/* Loading map data */
		InputStream input = new FileInputStream("data/xml/smallMap.xml");
		CityMapXMLParser parser = new CityMapXMLParser(input);
		CityMap map = parser.parse();

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
		HBox panelsContainer = (HBox) root.lookup("#panelsContainer");
		
		FXMLLoader controlPanelLoader = new FXMLLoader(getClass().getClassLoader().getResource("ControlPanel.fxml"));
		Parent controlPanel = controlPanelLoader.load();
		panelsContainer.getChildren().add(controlPanel);

		FXMLLoader cityMapPanelLoader = new FXMLLoader(getClass().getClassLoader().getResource("CityMapPanel.fxml"));
		Parent cityMapPanel = cityMapPanelLoader.load();
		CityMapController cityMapController = cityMapPanelLoader.getController();
		cityMapController.initialize(map);
		panelsContainer.getChildren().add(cityMapPanel);
		
		FXMLLoader detailPanelLoader = new FXMLLoader(getClass().getClassLoader().getResource("DetailPanel.fxml"));
		Parent detailPanel = detailPanelLoader.load();
		panelsContainer.getChildren().add(detailPanel);

		Scene scene = new Scene(root, 1000, 700);

		primaryStage.setTitle("Calculateur de tours de livraison en vélo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}