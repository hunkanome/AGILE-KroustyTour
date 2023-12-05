package fr.insalyon.controller;

import java.util.List;

import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;
import fr.insalyon.view.TourTextualView;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;

public class DetailsController {

	private DataModel dataModel;

	@FXML
	private Accordion accordion;

	public void initialize(DataModel dataModel) {
		this.dataModel = dataModel;

		// the cast is needed to avoid ambiguity with others method signature
		dataModel.getTours().addListener((ListChangeListener<Tour>) this::onTourListChanged);
	}
	
	@FXML
	private void addDelivery() {
		if (dataModel.getSelectedIntersection() != null) {
			Delivery d = new Delivery(null, dataModel.getSelectedIntersection(), new TimeWindow(9));
			dataModel.getSelectedTour().addDelivery(d);
		}
	}

	private void onTourListChanged(Change<? extends Tour> c) {
		System.out.println(c);
		while (c.next()) {
			if (c.wasAdded()) { // Add a new TitledPane to the Accordion
				Tour tour = c.getList().get(c.getFrom());
				TitledPane pane = new TitledPane();
				pane.setText("New Tour");
				ScrollPane scrollPane = new ScrollPane();
				scrollPane.setFitToWidth(true);
				pane.setContent(scrollPane);
				TourTextualView view = new TourTextualView(pane, dataModel);
				scrollPane.setContent(view);
				accordion.getPanes().add(pane);
				view.setTour(tour);

			} else if (c.wasRemoved()) { // Remove the corresponding TitledPane(s) from the Accordion
				List<TitledPane> panes = accordion.getPanes();
				if (c.getRemovedSize() == panes.size()) {
					panes.clear();
				} else {
					panes.remove(c.getFrom());
				}
			}
		}
	}
}
