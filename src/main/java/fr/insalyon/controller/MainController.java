package fr.insalyon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.insalyon.controller.command.CommandList;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.DataModel;
import fr.insalyon.xml.BadlyFormedXMLException;
import fr.insalyon.xml.CityMapXMLParser;
import fr.insalyon.xml.XMLParserException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController implements Controller {

	private static final int MESSAGE_DISPLAY_TIME = 5 * 1000;

	@FXML
	private HBox panelsContainer;

	@FXML
	private Label toolBarMessage;
	
	private DataModel dataModel;
	
	private CommandList commandList; // TODO use this from the menu bar

	/**
	 * In this implementation, the parent controller is ignored, as it is supposed to be this controller
	 */
	@Override
	public void initialize(DataModel dataModel, MainController parentController, CommandList commandList) {
		this.dataModel = dataModel;
		this.commandList = commandList;
		
		try {
			String[] panelPaths = {"ActionPanel.fxml", "CityMapPanel.fxml", "DetailPanel.fxml"};

			for (String panelPath : panelPaths) {
				loadPanel(panelPath);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadPanel(String path) throws IOException {
		FXMLLoader panelLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
		Parent panel = panelLoader.load();
		Controller controller = panelLoader.getController();
		controller.initialize(dataModel, this, commandList);
		panelsContainer.getChildren().add(panel);
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

	/**
	 * Quit the application
	 */
	public void quitApplication() {
		System.exit(0);
	}

}
