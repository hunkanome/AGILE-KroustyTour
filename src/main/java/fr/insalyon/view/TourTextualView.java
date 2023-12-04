package fr.insalyon.view;

import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class TourTextualView extends AnchorPane {

	private Tour tour;
	
	public TourTextualView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("TourTextualView.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (Exception e) {
			// TODO handle error
			e.printStackTrace();
		}
	}
	
	public void setTour(Tour tour) {
		this.tour = tour;
		
		int i = 0;
		for (Delivery d : tour.getDeliveries()) {
			this.showDeliveries(d, "8h30", i++);
		}
	}
	
	
	private void showDeliveries(Delivery delivery, String hour, int index) {
		DeliveryTextualView view = new DeliveryTextualView();
		double topAnchor = (double) ((100 * index) + 10);
		AnchorPane.setTopAnchor(view, topAnchor);
		AnchorPane.setLeftAnchor(view, 20d);
		this.getChildren().add(view);
		view.setContent(delivery, hour);
	}
}
