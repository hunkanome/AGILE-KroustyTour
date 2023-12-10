package fr.insalyon.controller;

import fr.insalyon.controller.command.*;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ActionController implements Controller {

	@FXML
	private ComboBox<TimeWindow> timeWindowChooser;

	private DataModel dataModel;

	private CommandList commandList;

	private MainController parentController;

	/**
	 * Initialize the view by filling the time window chooser with the time windows
	 */
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
		Command command = new AddTourCommand(dataModel, new Tour());
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
			return;
		}
		Intersection selectedIntersection = this.dataModel.getSelectedIntersection();
		if (selectedIntersection == null) {
			return;
		}
		TimeWindow selectedTimeWindow = this.timeWindowChooser.getValue();
		if (selectedTimeWindow == null) {
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
			return;
		}
		Delivery selectedDelivery = this.dataModel.getSelectedDelivery();
		if (selectedDelivery == null) {
			return;
		}
		Command command = new RemoveSelectedDeliveryCommand(this.dataModel);
		this.commandList.execute(command);
		this.parentController.displayToolBarMessage("Delivery removed");

	}
}
