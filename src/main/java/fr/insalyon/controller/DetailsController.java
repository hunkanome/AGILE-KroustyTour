package fr.insalyon.controller;

import java.util.List;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;
import fr.insalyon.view.TourTextualView;
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

		dataModel.getTours().addListener(this::onTourListChanged);
	}
	
	@FXML
	private void addDelivery() {
		if (dataModel.getSelectedIntersection() != null) {
			Delivery d = new Delivery(null, dataModel.getSelectedIntersection(), new TimeWindow(9));
			// TODO : choose the tour to use either from the details Pane, or from the controls Pane (actually : may crash)
			dataModel.getSelectedTour().addDelivery(d);
			dataModel.setSelectedDelivery(d);
			dataModel.setSelectedIntersection(null);
		}
	}

	private void onTourListChanged(Change<? extends Tour> c) {
		while (c.next()) {
			if (c.wasAdded()) { // Add a new TitledPane to the Accordion
				Tour tour = c.getList().get(c.getFrom());
				TitledPane pane = new TitledPane();
				pane.setText("Tour of courier n°" + tour.getCourier().getId());
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
