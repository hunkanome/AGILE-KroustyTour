package fr.insalyon.controller;

import fr.insalyon.controller.command.AddDeliveryCommand;
import fr.insalyon.controller.command.AddTourCommand;
import fr.insalyon.controller.command.Command;
import fr.insalyon.controller.command.CommandList;
import fr.insalyon.controller.command.RemoveSelectedTourCommand;
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

	/**
	 * Initialize the view by filling the time window chooser with the time windows
	 */
	@Override
	public void initialize(DataModel dataModel, MainController parentController, CommandList commandList) {
		this.dataModel = dataModel;
		this.commandList = commandList;
		this.timeWindowChooser.setItems(TimeWindow.getTimeWindows());
	}

	@FXML
	private void addTour() {
		if (dataModel == null || dataModel.getCityMap() == null) {
			return;
		}
		Command command = new AddTourCommand(dataModel, new Tour());
		commandList.execute(command);
	}

	@FXML
	private void removeSelectedTour() {
		if (dataModel == null) {
			return;
		}
		Tour selectedTour = dataModel.getSelectedTour();
		if (selectedTour == null) {
			return;
		}
		Command command = new RemoveSelectedTourCommand(dataModel, selectedTour);
		commandList.execute(command);
	}

	@FXML
	private void addDelivery() {
		if (dataModel == null || dataModel.getCityMap() == null) {
			return;
		}
		Tour selectedTour = dataModel.getSelectedTour();
		if (selectedTour == null) {
			return;
		}
		Intersection selectedIntersection = dataModel.getSelectedIntersection();
		if (selectedIntersection == null) {
			return;
		}
		TimeWindow selectedTimeWindow = timeWindowChooser.getValue();
		if (selectedTimeWindow == null) {
			return;
		}

		Delivery delivery = new Delivery(selectedTour.getCourier(), selectedIntersection, selectedTimeWindow);
		Command command = new AddDeliveryCommand(selectedTour.getCityMapMatrix(), selectedTour, delivery);
		commandList.execute(command);
		dataModel.setSelectedIntersection(null);
		dataModel.setSelectedDelivery(delivery);
	}

	@FXML
	private void removeSelectedDelivery() {
		if (dataModel == null || dataModel.getSelectedTour() == null) {
			return;
		}
		Delivery selectedDelivery = this.dataModel.getSelectedDelivery();
		if (selectedDelivery == null) {
			return;
		}
		dataModel.getSelectedTour().getDeliveriesList().remove(dataModel.getSelectedDelivery());
	}
}
