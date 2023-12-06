package fr.insalyon.controller;

import fr.insalyon.controller.command.CommandList;
import fr.insalyon.model.DataModel;
import javafx.fxml.FXML;

public class ActionController {

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
		System.out.println("suppression delivery");
	}
}
