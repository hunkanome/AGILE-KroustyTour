package fr.insalyon.view;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

	private static final double WAREHOUSE_VIEW_HEIGHT = 30;

	private static final double LINE_LEFT_MARGIN = 50;
	private static final double LINE_RIGHT_MARGIN = 10;

	private static final double DASH_SPACING = 5;

	private Tour tour;

	private final TitledPane parent;
	private final DataModel dataModel;

	private double topAnchor = 0;

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
			this.getChildren().add(new Pane(new Label("An error occurred while loading the view.")));
		}
	}

	public void setTour(Tour tour) {
		this.tour = tour;
		this.tour.getDeliveriesList().addListener(this::onTourUpdate);
		try {
			this.showTour();
		} catch (IOException e) {
			this.getChildren().add(new Pane(new Label("An error occurred while loading the view.")));

		}
		this.dataModel.setSelectedTour(this.tour);
	}

	private void showTour() throws IOException {
		if (this.tour.getDeliveriesList().isEmpty()
				|| this.tour.getDeliveriesList().size() != this.tour.getPathList().size()) {
			return;
		}

		List<Delivery> deliveryList = new ArrayList<>();
		deliveryList.addAll(this.tour.getDeliveriesList());

		topAnchor = DELIVERY_VIEW_TOP_MARGIN;
		this.showWarehouse();

		LocalTime start = Tour.TOUR_START;
		float distance;
		Duration duration;
		Delivery delivery;

		for (int i = 0; i < deliveryList.size(); i++) {
			delivery = deliveryList.get(i);

			distance = this.tour.getPathList().get(i).getLength();
			duration = Duration.ofSeconds((long) (distance / 15 * 60L / 3.6f));
			start = start.plus(duration);
			Duration waitTime = Duration.ZERO;
			if (start.isBefore(LocalTime.of(delivery.getTimeWindow().getStartHour(), 0))) {
				LocalTime minArrivalTime = LocalTime.of(delivery.getTimeWindow().getStartHour(), 0);
				waitTime = Duration.ofMinutes((minArrivalTime.toSecondOfDay() - start.toSecondOfDay()) / 60);
				start = LocalTime.of(delivery.getTimeWindow().getStartHour(), 0);
			}
			this.showTravelTime(duration, waitTime);

			this.showDelivery(delivery, start);
			start = start.plus(Delivery.DURATION);
		}
	}

	private void showWarehouse() throws IOException {
		Parent warehouseView = FXMLLoader.load(getClass().getClassLoader().getResource("WarehouseTextualView.fxml"));
		AnchorPane.setTopAnchor(warehouseView, topAnchor);
		AnchorPane.setLeftAnchor(warehouseView, DELIVERY_VIEW_LEFT_MARGIN);
		this.getChildren().add(warehouseView);
		topAnchor += WAREHOUSE_VIEW_HEIGHT;

	}

	private void showDelivery(Delivery delivery, LocalTime arrivalTime) {
		DeliveryTextualView view = new DeliveryTextualView(this.dataModel, parent);
		AnchorPane.setTopAnchor(view, topAnchor);
		AnchorPane.setLeftAnchor(view, DELIVERY_VIEW_LEFT_MARGIN);
		this.getChildren().add(view);
		view.setContent(delivery, arrivalTime);
		topAnchor += DELIVERY_VIEW_HEIGHT;
	}

	private void showTravelTime(Duration duration, Duration waitTime) {
		Line line = new Line();
		line.setStartX(LINE_LEFT_MARGIN);
		line.setEndX(LINE_LEFT_MARGIN);
		double bottom = topAnchor + DELIVERY_VIEW_VERTICAL_SPACE - DELIVERY_VIEW_HEIGHT;
		line.setStartY(topAnchor);
		line.setEndY(bottom);
		line.getStrokeDashArray().add(DASH_SPACING);
		this.getChildren().add(line);

		double middle = (topAnchor + bottom) / 2d;
		String durationFormatted = String.format("Travel time : %d min", duration.toMinutes());
		Label timeLabel = new Label(durationFormatted);
		timeLabel.setLayoutX(LINE_LEFT_MARGIN + LINE_RIGHT_MARGIN);
		timeLabel.setLayoutY(middle - 10);
		this.getChildren().add(timeLabel);

		if (waitTime.toMinutes() > 0) {
			String waitingTimeFormatted = String.format("Waiting time : %02d min", waitTime.toMinutes());
			Label waitingTimeLabel = new Label(waitingTimeFormatted);
			waitingTimeLabel.setLayoutX(LINE_LEFT_MARGIN + LINE_RIGHT_MARGIN);
			waitingTimeLabel.setLayoutY(middle + 10);
			this.getChildren().add(waitingTimeLabel);
		}

		topAnchor = bottom;
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
			this.parent.setExpanded(true);
		}
	}

	private void onTourUpdate(Change<? extends Delivery> c) {
		this.getChildren().clear();
		try {
			this.showTour();
		} catch (IOException e) {
			this.getChildren().add(new Pane(new Label("An error occurred while loading the view.")));
			e.printStackTrace(); // TODO remove
		}
	}

}
