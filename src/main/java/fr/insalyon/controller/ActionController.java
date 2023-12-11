package fr.insalyon.controller;

import fr.insalyon.controller.command.*;
import fr.insalyon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 * Controller for the action panel (left side)
 * @see Controller
 */
public class ActionController implements Controller {

	@FXML
	private ComboBox<TimeWindow> timeWindowChooser;

	private DataModel dataModel;

	private CommandList commandList;

	private MainController parentController;

	@Override
	public void initialize(DataModel dataModel, MainController parentController, CommandList commandList) {
		this.dataModel = dataModel;
		this.commandList = commandList;
		this.parentController = parentController;

		this.timeWindowChooser.setItems(TimeWindow.getTimeWindows());
	}

	@FXML
	private void addTour() {
		if (dataModel == null || dataModel.getCityMap() == null) {
			return;
		}
		Courier courier = new Courier();
		Tour newTour = new Tour(courier);
		Command command = new AddTourCommand(dataModel, newTour);
		this.commandList.execute(command);
		this.parentController.displayToolBarMessage("Tour added");
	}

	@FXML
	private void removeSelectedTour() {
		if (this.dataModel == null) {
			return;
		}
		Tour selectedTour = this.dataModel.getSelectedTour();
		if (selectedTour == null) {
			this.parentController.displayToolBarMessage("No tour selected");
			return;
		}
		Command command = new RemoveSelectedTourCommand(this.dataModel, selectedTour);
		this.commandList.execute(command);
		this.parentController.displayToolBarMessage("Tour removed");
	}

	@FXML
	private void addDelivery() {
		if (this.dataModel == null || this.dataModel.getCityMap() == null) {
			return;
		}
		Tour selectedTour = this.dataModel.getSelectedTour();
		if (selectedTour == null) {
			this.parentController.displayToolBarMessage("No tour selected");
			return;
		}
		Intersection selectedIntersection = this.dataModel.getSelectedIntersection();
		if (selectedIntersection == null) {
			this.parentController.displayToolBarMessage("No intersection selected");
			return;
		}
		TimeWindow selectedTimeWindow = this.timeWindowChooser.getValue();
		if (selectedTimeWindow == null) {
			this.parentController.displayToolBarMessage("No time window selected");
			return;
		}
		Delivery newDelivery = new Delivery(selectedTour.getCourier(), selectedIntersection, selectedTimeWindow);
		Command command = new AddDeliveryCommand(this.dataModel, newDelivery);
		this.commandList.execute(command);
		this.parentController.displayToolBarMessage("Delivery added");
	}

	@FXML
	private void removeSelectedDelivery() {
		if (this.dataModel == null) {
			return;
		}
		Tour selectedTour = this.dataModel.getSelectedTour();
		if (selectedTour == null) {
			this.parentController.displayToolBarMessage("No tour selected");
			return;
		}
		Delivery selectedDelivery = this.dataModel.getSelectedDelivery();
		if (selectedDelivery == null) {
			this.parentController.displayToolBarMessage("No delivery selected");
			return;
		}
		Command command = new RemoveSelectedDeliveryCommand(this.dataModel);
		this.commandList.execute(command);
		this.parentController.displayToolBarMessage("Delivery removed");

	}
}