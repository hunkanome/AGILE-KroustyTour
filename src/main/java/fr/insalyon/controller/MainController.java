package fr.insalyon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import fr.insalyon.city_map_xml.BadlyFormedXMLException;
import fr.insalyon.city_map_xml.CityMapXMLParser;
import fr.insalyon.city_map_xml.XMLParserException;
import fr.insalyon.controller.command.CommandList;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Tour;
import fr.insalyon.seralization.TourDeserializer;
import fr.insalyon.seralization.TourSerializer;
import fr.insalyon.seralization.XMLTourDeserializer;
import fr.insalyon.seralization.XMLTourSerializer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController implements Controller {

	private static final int MESSAGE_DISPLAY_TIME = 5 * 1000;

	@FXML
	private HBox panelsContainer;

	@FXML
	private Label toolBarMessage;

	private DataModel dataModel;

	private CommandList commandList;

	/**
	 * In this implementation, the parent controller is ignored, as it is supposed
	 * to be this controller
	 */
	@Override
	public void initialize(DataModel dataModel, MainController parentController, CommandList commandList) {
		this.dataModel = dataModel;
		this.commandList = commandList;

		try {
			String[] panelPaths = { "ActionPanel.fxml", "CityMapPanel.fxml", "DetailPanel.fxml" };

			for (String panelPath : panelPaths) {
				loadPanel(panelPath);
			}

		} catch (Exception e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.severe("An error occurred while loading the view.");
			logger.severe(e.getMessage());
			panelsContainer.getChildren().clear();
			panelsContainer.getChildren().add(new Pane(new Label("An error occurred while loading the view.")));
		}
	}

	private void loadPanel(String path) throws IOException, IllegalStateException {
		FXMLLoader panelLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
		Parent panel = panelLoader.load();
		Controller controller = panelLoader.getController();
		controller.initialize(dataModel, this, commandList);
		panelsContainer.getChildren().add(panel);
	}

	/**
	 * Displays the given exception's message in the toolbar.<br/>
	 * The message is removed after a delay
	 * 
	 * @param e - the exception to display
	 */
	public void displayToolBarMessage(Exception e) {
		displayToolBarMessage(e.getMessage());
	}

	/**
	 * Displays the given message in the toolbar.<br/>
	 * The message is removed after a delay
	 * 
	 * @param message - the message to display
	 */
	public void displayToolBarMessage(String message) {
		toolBarMessage.setText(message);

		// Clear the message after some delay
		new Thread(() -> {
			try {
				Thread.sleep(MESSAGE_DISPLAY_TIME);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			Platform.runLater(() -> toolBarMessage.setText(""));
		}).start();
	}

	@FXML
	private void quitApplication() {
		System.exit(0);
	}

	@FXML
	private void saveTours() {
		if (this.dataModel == null || this.dataModel.getCityMap() == null) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save the tours");

		// Set default to user home directory
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if (!userDirectory.canRead()) {
			userDirectory = null;
		}
		fileChooser.setInitialDirectory(userDirectory);
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Tours XML file", "*.xml"));
		
		File selectedFile = fileChooser.showSaveDialog(null);
		

		try (OutputStream out = new FileOutputStream(selectedFile)) {
			// set the xml extension if not already
			if (!selectedFile.getName().endsWith(".xml")) {
				String newPath = selectedFile.getAbsolutePath() + ".xml";
				selectedFile = new File(newPath);
			}
			TourSerializer serializer = new XMLTourSerializer(); // choose the good serializer based on the extension (when there will be more of them)
			serializer.setTours(this.dataModel.getTours()).setCityMap(this.dataModel.getCityMap()).setFile(out)
					.serialize();
		} catch (Exception e) {
			this.displayToolBarMessage(e);
			return;
		}
		this.displayToolBarMessage("Tours saved to " + selectedFile.getName());
	}
	
	@FXML
	private void loadTours() {
		if (this.dataModel == null || this.dataModel.getCityMap() == null) {
			return;
		}
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load the tours");

		// Set default to user home directory
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if (!userDirectory.canRead()) {
			userDirectory = null;
		}
		fileChooser.setInitialDirectory(userDirectory);

		fileChooser.getExtensionFilters().add(new ExtensionFilter("Tours XML file", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(panelsContainer.getScene().getWindow());
		
		try (InputStream in = new FileInputStream(selectedFile)) {
			TourDeserializer deserializer = new XMLTourDeserializer(); // choose the good one
			deserializer.setCityMap(this.dataModel.getCityMap()).setInputFile(in).deserialize();
			List<Tour> tours = deserializer.getTours();
			
			this.dataModel.getTours().clear();
			this.dataModel.getTours().addAll(tours);
		} catch (Exception e) {
			this.displayToolBarMessage(e);
			return;
		}
		this.displayToolBarMessage("Tours loaded from " + selectedFile.getName());
	}

	@FXML
	private void openMapFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a CityMap XML file");

		// Set default to user home directory
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if (!userDirectory.canRead()) {
			userDirectory = null;
		}
		fileChooser.setInitialDirectory(userDirectory);

		fileChooser.getExtensionFilters().add(new ExtensionFilter("CityMap XML file", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(panelsContainer.getScene().getWindow());
		if (selectedFile != null) {
			FileInputStream inputStream;
			try {
				inputStream = new FileInputStream(selectedFile);
				CityMapXMLParser parser = new CityMapXMLParser(inputStream);
				CityMap map = parser.parse();
				this.dataModel.setMap(map);
			} catch (FileNotFoundException e) {
				this.displayToolBarMessage("The file " + selectedFile.getName() + " could not be found.");
			} catch (BadlyFormedXMLException | XMLParserException e) {
				this.displayToolBarMessage(e);
			}
		}
	}

	@FXML
	private void redoCommand() {
		this.commandList.redo();
	}

	@FXML
	private void undoCommand() {
		this.commandList.undo();
	}

	@FXML
    private void showPopupVersion(ActionEvent actionEvent) {
		// Opens another window that displays the current application version
		Stage window = new Stage();
		window.setTitle("About");
		window.setResizable(false);
		window.initOwner(panelsContainer.getScene().getWindow());
		window.initModality(javafx.stage.Modality.APPLICATION_MODAL);

		// Add a label to the window containing the version
		Label label = new Label("Version 1.0\n©Hunkanome");
		label.setAlignment(Pos.BASELINE_CENTER);
		label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
		label.setPrefHeight(100);
		label.setPrefWidth(200);


		// add the label to a pane
		Pane pane = new Pane();
		pane.getChildren().add(label);

		// add the pane to the window
		Scene scene = new Scene(pane);
		window.setScene(scene);

		window.show();
    }
}
