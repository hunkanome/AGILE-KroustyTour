package fr.insalyon.view;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
	
	@FXML
	private Label arrivalTimeLabel;
	
	@FXML
	private Label departureTimeLabel;

	private Delivery delivery;

	private final DataModel dataModel;
	
	private final TitledPane parent;

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

	public void setContent(Delivery delivery, LocalTime arrivalTime) {
		this.delivery = delivery;
		if (this.arrivalTimeLabel != null) {
			this.arrivalTimeLabel.setText(dtf.format(arrivalTime));
			LocalTime departureTime = arrivalTime.plus(Delivery.DURATION);
			this.departureTimeLabel.setText(dtf.format(departureTime));
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
