package fr.insalyon.controller;

import fr.insalyon.controller.command.CommandList;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.TimeWindow;
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
	
	@Override
	public void initialize(DataModel dataModel, MainController parentController, CommandList commandList) {
		this.dataModel = dataModel;
	
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
