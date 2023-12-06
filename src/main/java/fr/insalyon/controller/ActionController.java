package fr.insalyon.controller;

import fr.insalyon.controller.command.CommandList;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.TimeWindow;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ActionController {

	@FXML
	private ComboBox<TimeWindow> timeWindowChooser;
	
	private DataModel dataModel;

	private MainController parentController;

	private CommandList commandList;

	/**
	 * Initialize the controller variables
	 * 
	 * @param dataModel      the controller data
	 * @param mainController the parent controller
	 * @param commandList    the command history
	 */
	public void initialize(DataModel dataModel, MainController mainController, CommandList commandList) {
		this.parentController = mainController;
		this.dataModel = dataModel;
		this.commandList = commandList;
		
		this.timeWindowChooser.setItems(TimeWindow.getTimeWindows());
	}

	@FXML
	private void addCourier() {
		System.out.println("nouveau courier");
	}

	@FXML
	private void removeSelectedCourier() {
		System.out.println("suppression courier");
	}

	@FXML
	private void addDelivery() {
		System.out.println("ajout delivery");
	}
	
	@FXML
	private void removeSelectedDelivery() {
		Delivery selectedDelivery = this.dataModel.getSelectedDelivery();
		if (selectedDelivery == null) {
			return;
		}
		
		System.out.println("suppression delivery");
	}
}
