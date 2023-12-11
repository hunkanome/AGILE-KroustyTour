package fr.insalyon.view;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.time.Duration;
import java.time.LocalTime;

public class TourTextualView extends AnchorPane {
// TODO : add the warehouse start and end in the view

	private static final double DELIVERY_VIEW_HEIGHT = 49;
	private static final double DELIVERY_VIEW_VERTICAL_SPACE = 100;
	private static final double DELIVERY_VIEW_LEFT_MARGIN = 20;
	private static final double DELIVERY_VIEW_TOP_MARGIN = 10;

	private static final double LINE_LEFT_MARGIN = 50;
	private static final double LINE_RIGHT_MARGIN = 10;

	private static final double DASH_SPACING = 5;

	private Tour tour;

	private final TitledPane parent;
	private final DataModel dataModel;

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
		this.showTour();
		this.dataModel.setSelectedTour(this.tour);
	}

	private void showTour() {
		if (!this.tour.getDeliveriesList().isEmpty()) {
			// TODO : prendre en compte le temps depuis le warehouse (départ à 8h)
			// TODO : si la delivery n'est pas à 8h, possibilité de partir plus tard du coup

//			float distance = this.tour.getPathList().get(0).getLength();
//			Duration duration = Duration.ofSeconds((long) (distance / 15 * 60L / 3.6f));
//			start = start.plus(duration);

			LocalTime start = LocalTime.of(this.tour.getDeliveriesList().get(0).getTimeWindow().getStartHour(), 0);
			Delivery delivery = this.tour.getDeliveriesList().get(0);
			double bottom = this.showDelivery(delivery, start, 0);
			start = start.plus(Delivery.DURATION);

			for (int i = 1; i < this.tour.getDeliveriesList().size(); i++) {
				delivery = this.tour.getDeliveriesList().get(i);

				float distance = this.tour.getPathList().get(i - 1).getLength();
				Duration duration = Duration.ofSeconds((long) (distance / 15 * 60L / 3.6f));
				start = start.plus(duration);
				this.showTravelTime(bottom, start, duration, delivery.getTimeWindow());

				if (start.isBefore(LocalTime.of(delivery.getTimeWindow().getStartHour(), 0))) {
					start = LocalTime.of(delivery.getTimeWindow().getStartHour(), 0);
				}

				bottom = this.showDelivery(delivery, start, i);
				start = start.plus(Delivery.DURATION);
			}
		}
	}

	private double showDelivery(Delivery delivery, LocalTime arrivalTime, int index) {
		DeliveryTextualView view = new DeliveryTextualView(this.dataModel, parent);
		double topAnchor = ((DELIVERY_VIEW_VERTICAL_SPACE * index) + DELIVERY_VIEW_TOP_MARGIN);
		AnchorPane.setTopAnchor(view, topAnchor);
		AnchorPane.setLeftAnchor(view, DELIVERY_VIEW_LEFT_MARGIN);
		this.getChildren().add(view);
		view.setContent(delivery, arrivalTime);
		return topAnchor + DELIVERY_VIEW_HEIGHT;
	}

	private void showTravelTime(double top, LocalTime startTime, Duration duration, TimeWindow timeWindow) {
		Line line = new Line();
		line.setStartX(LINE_LEFT_MARGIN);
		line.setEndX(LINE_LEFT_MARGIN);
		double bottom = top + DELIVERY_VIEW_VERTICAL_SPACE - DELIVERY_VIEW_HEIGHT;
		line.setStartY(top);
		line.setEndY(bottom);
		line.getStrokeDashArray().add(DASH_SPACING);
		this.getChildren().add(line);

		double middle = (top + bottom) / 2d;
		String durationFormatted = String.format("Travel time : %d min", duration.toMinutes());
		Label timeLabel = new Label(durationFormatted);
		timeLabel.setLayoutX(LINE_LEFT_MARGIN + LINE_RIGHT_MARGIN);
		timeLabel.setLayoutY(middle - 10);
		this.getChildren().add(timeLabel);

		LocalTime endTime = startTime.plus(duration);
		LocalTime deliveryTime = LocalTime.of(timeWindow.getStartHour(), 0);

		if (endTime.isBefore(deliveryTime)) {
			Duration delta = Duration.between(endTime, deliveryTime);
			String waitingTimeFormatted = String.format("Waiting time : %02d min", delta.toMinutes());
			Label waitingTimeLabel = new Label(waitingTimeFormatted);
			waitingTimeLabel.setLayoutX(LINE_LEFT_MARGIN + LINE_RIGHT_MARGIN);
			waitingTimeLabel.setLayoutY(middle + 10);
			this.getChildren().add(waitingTimeLabel);
		}
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
		this.showTour();
	}

}
