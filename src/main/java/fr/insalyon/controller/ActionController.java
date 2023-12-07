package fr.insalyon.controller;

import fr.insalyon.controller.command.AddCourierCommand;
import fr.insalyon.controller.command.AddDeliveryCommand;
import fr.insalyon.controller.command.CommandList;
import fr.insalyon.controller.command.RemoveCourierCommand;
import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.TimeWindow;
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
	private void addCourier() {
		commandList.execute(new AddCourierCommand(dataModel, new Courier()));
	}

	@FXML
	private void removeSelectedCourier() {
		commandList.execute(new RemoveCourierCommand(dataModel));
		dataModel.setSelectedTour(null);
	}

	@FXML
	private void addDelivery() {
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
		if (selectedDelivery == null) {
			return;
		}
		
		System.out.println("suppression delivery");
	}
}
