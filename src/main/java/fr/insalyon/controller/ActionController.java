package fr.insalyon.controller;

import fr.insalyon.controller.command.AddDeliveryCommand;
import fr.insalyon.controller.command.AddTourCommand;
import fr.insalyon.controller.command.CommandList;
import fr.insalyon.controller.command.RemoveTourCommand;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
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
		commandList.execute(new AddTourCommand(dataModel, new Tour()));
	}

	@FXML
	private void removeSelectedTour() {
		if (dataModel == null){
			return;
		}
		if (dataModel.getSelectedTour() == null){
			return;
		}
		commandList.execute(new RemoveTourCommand(dataModel, dataModel.getSelectedTour()));
		dataModel.setSelectedTour(null);
	}

	@FXML
	private void addDelivery() {
		if (dataModel == null){
			return;
		}
		if (dataModel.getSelectedTour() == null) {
			return;
		}
		commandList.execute(new AddDeliveryCommand(
			dataModel.getSelectedTour().getCityMapMatrix(),
			dataModel.getSelectedTour(),
			new Delivery(
				dataModel.getSelectedTour().getCourier(),
				dataModel.getSelectedIntersection(),
				timeWindowChooser.getValue()
			)
		));
	}
	
	@FXML
	private void removeSelectedDelivery() {
		Delivery selectedDelivery = this.dataModel.getSelectedDelivery();
		if (dataModel.getSelectedTour() == null){
			return;
		}
		if (selectedDelivery == null) {
			return;
		}
		dataModel.getSelectedTour().getDeliveriesList().remove(dataModel.getSelectedDelivery());
	}
}
