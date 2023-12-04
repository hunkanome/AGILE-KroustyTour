package fr.insalyon.view;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class TourTextualView extends AnchorPane {

	private static final double DELIVERY_VIEW_HEIGHT = 49;
	private static final double DELIVERY_VIEW_VERTICAL_SPACE = 100;
	private static final double DELIVERY_VIEW_LEFT_MARGIN = 20;
	private static final double DELIVERY_VIEW_TOP_MARGIN = 10;

	private static final double LINE_LEFT_MARGIN = 50;
	private static final double LINE_RIGHT_MARGIN = 10;

	private static final double DASH_SPACING = 5;

	private Tour tour;

	private TitledPane parent;
	private DataModel dataModel;

	public TourTextualView(TitledPane parent, DataModel dataModel) {
		this.parent = parent;
		this.dataModel = dataModel;

		parent.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleClick);
		dataModel.selectedTourProperty().addListener(this::onSelectedTourUpdate);

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("TourTextualView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (Exception e) {
			// TODO improve this error view
			loader.setRoot(new Pane(new Label("An error occurred while loading the view.")));
		}
	}

	public void setTour(Tour tour) {
		this.tour = tour;

		int i = 0;
		for (Delivery d : tour.getDeliveries()) {
			double bottom = this.showDeliveries(d, "8h30", i++);
			if (i < tour.getDeliveries().size()) {
				this.showTravelTime(bottom, "14 min");
			}
		}
	}

	private double showDeliveries(Delivery delivery, String hour, int index) {
		DeliveryTextualView view = new DeliveryTextualView(this.dataModel);
		double topAnchor = ((DELIVERY_VIEW_VERTICAL_SPACE * index) + DELIVERY_VIEW_TOP_MARGIN);
		AnchorPane.setTopAnchor(view, topAnchor);
		AnchorPane.setLeftAnchor(view, DELIVERY_VIEW_LEFT_MARGIN);
		this.getChildren().add(view);
		view.setContent(delivery, hour);
		return topAnchor + DELIVERY_VIEW_HEIGHT;
	}

	private void showTravelTime(double top, String time) {
		Line line = new Line();
		line.setStartX(LINE_LEFT_MARGIN);
		line.setEndX(LINE_LEFT_MARGIN);
		double bottom = top + DELIVERY_VIEW_VERTICAL_SPACE - DELIVERY_VIEW_HEIGHT;
		line.setStartY(top);
		line.setEndY(bottom);
		line.getStrokeDashArray().add(DASH_SPACING);
		this.getChildren().add(line);

		double middle = (top + bottom) / 2d;
		Label timeLabel = new Label(time);
		timeLabel.setLayoutX(LINE_LEFT_MARGIN + LINE_RIGHT_MARGIN);
		timeLabel.setLayoutY(middle - 10);
		this.getChildren().add(timeLabel);
	}
	
	private void handleClick(MouseEvent event) {
		if (this.parent.isExpanded()) {
			this.dataModel.setSelectedTour(this.tour);
		} else {
			this.dataModel.setSelectedTour(null);
		}
	}

	private void onSelectedTourUpdate(ObservableValue<? extends Tour> observable, Tour oldValue, Tour newValue) {
		if (newValue == tour) {
			// TODO check that it works
			this.parent.setExpanded(true);
		}
	}

}
