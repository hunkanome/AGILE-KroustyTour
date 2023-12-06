package fr.insalyon.view;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class DeliveryTextualView extends AnchorPane {

	@FXML
	private Label hourLabel;

	private Delivery delivery;

	private DataModel dataModel;
	
	private TitledPane parent;

	public DeliveryTextualView(DataModel dataModel, TitledPane parent) {
		this.dataModel = dataModel;
		this.parent = parent;

		this.dataModel.selectedDeliveryProperty().addListener(this::onSelectedDeliveryUpdate);

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DeliveryTextualView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (Exception e) {
			// TODO improve this error view
			loader.setRoot(new Pane(new Label("An error occurred while loading the view.")));
		}
	}

	public void setContent(Delivery delivery, String hour) {
		this.delivery = delivery;
		if (this.hourLabel != null) {
			this.hourLabel.setText(hour);
		}
	}

	@FXML
	public void handleClick(MouseEvent event) {
		this.dataModel.setSelectedDelivery(delivery);
	}

	private void onSelectedDeliveryUpdate(ObservableValue<? extends Delivery> observable, Delivery oldValue, Delivery newValue) {
		if (newValue == delivery) {
			getStyleClass().add("selected");
			parent.setExpanded(true);
		} else {
			getStyleClass().remove("selected");
		}
	}

}
