package fr.insalyon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.DataModel;
import fr.insalyon.xml.BadlyFormedXMLException;
import fr.insalyon.xml.CityMapXMLParser;
import fr.insalyon.xml.XMLParserException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController {

	@FXML
	private HBox panelsContainer;

	private DataModel dataModel;

	public void initialize(DataModel dataModel) throws IOException {
		this.dataModel = dataModel;

		FXMLLoader controlPanelLoader = new FXMLLoader(getClass().getClassLoader().getResource("ControlPanel.fxml"));
		Parent controlPanel = controlPanelLoader.load();
		panelsContainer.getChildren().add(controlPanel);

		FXMLLoader cityMapPanelLoader = new FXMLLoader(getClass().getClassLoader().getResource("CityMapPanel.fxml"));
		Parent cityMapPanel = cityMapPanelLoader.load();
		CityMapController cityMapController = cityMapPanelLoader.getController();
		cityMapController.initialize(dataModel);
		panelsContainer.getChildren().add(cityMapPanel);

		FXMLLoader detailPanelLoader = new FXMLLoader(getClass().getClassLoader().getResource("DetailPanel.fxml"));
		Parent detailPanel = detailPanelLoader.load();
		panelsContainer.getChildren().add(detailPanel);
	}

	@FXML
	private void openMapFile() {
		// TODO : handle errors
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a CityMap XML file");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CityMap XML file", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(panelsContainer.getScene().getWindow());
		if (selectedFile != null) {
			FileInputStream inputStream;
			try {
				inputStream = new FileInputStream(selectedFile);
				CityMapXMLParser parser = new CityMapXMLParser(inputStream);
				CityMap map = parser.parse();
				dataModel.setMap(map);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (BadlyFormedXMLException e) {
				e.printStackTrace();
			} catch (XMLParserException e) {
				e.printStackTrace();
			}
		}
	}

}
