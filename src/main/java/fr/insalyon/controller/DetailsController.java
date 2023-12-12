package fr.insalyon.controller;

import fr.insalyon.controller.command.CommandList;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Tour;
import fr.insalyon.view.TourTextualView;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;

import java.util.List;

public class DetailsController implements Controller {

	@FXML
	private Accordion accordion;
	
	private DataModel dataModel;
	
	@Override
	public void initialize(DataModel dataModel, MainController parentController, CommandList commandList) {
		this.dataModel = dataModel;
		
		this.dataModel.getTours().addListener(this::onTourListChanged);
	}

	private void onTourListChanged(Change<? extends Tour> c) {
		while (c.next()) {
			if (c.wasAdded()) { // Add a new TitledPane to the Accordion
				for (Tour tour: c.getAddedSubList()) {
					TitledPane pane = new TitledPane();
					pane.setText("Tour of courier n°" + tour.getCourier().getId());
					ScrollPane scrollPane = new ScrollPane();
					scrollPane.setFitToWidth(true);
					scrollPane.setMinHeight(300);
					pane.setContent(scrollPane);
					TourTextualView view = new TourTextualView(pane, dataModel);
					scrollPane.setContent(view);
					accordion.getPanes().add(pane);
					view.setTour(tour);
				}
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
