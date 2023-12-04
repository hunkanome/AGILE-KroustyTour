package fr.insalyon.view;

import fr.insalyon.model.Delivery;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DeliveryTextualView extends AnchorPane {

	@FXML
	private Label hourLabel;
	
	private Delivery delivery;
	
	private String hour;
	
	public DeliveryTextualView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DeliveryTextualView.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (Exception e) {
			// TODO handle error
			e.printStackTrace();
		}
	}
	
	public void setContent(Delivery delivery, String hour) {
		this.delivery = delivery;
		this.hour = hour;
		this.hourLabel.setText(hour);
	}
	
}
