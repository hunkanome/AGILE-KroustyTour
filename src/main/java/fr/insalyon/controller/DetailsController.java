package fr.insalyon.controller;

import java.util.ArrayList;
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
		this.addTour();
		this.addTour();
	}
	
	private void addTour() {
		TitledPane pane = new TitledPane();
		pane.setText("New Tour");
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		pane.setContent(scrollPane);
		TourTextualView view = new TourTextualView(pane, this.dataModel);
		scrollPane.setContent(view);
		accordion.getPanes().add(pane);
		
		// TODO : remove this to use the real things
		Tour t = new Tour(null, null);
		Intersection i1 = new Intersection(1l, new GeoCoordinates(0f, 0f), 0);
		Intersection i2 = new Intersection(2l, new GeoCoordinates(1f, 1f), 1);
		Intersection i3 = new Intersection(3l, new GeoCoordinates(1f, 1f), 2);
		Path p = new Path();
		p.appendSegment(new Segment(i1, i2, "Test", 0));
		p.appendSegment(new Segment(i2, i3, "Test", 0));
		p.setStart(i1);
		p.setEnd(i3);
		t.setPath(p);

		Delivery d1 = new Delivery(null, i1, new TimeWindow(8));
		Delivery d2 = new Delivery(null, i2, new TimeWindow(9));
		Delivery d3 = new Delivery(null, i3, new TimeWindow(10));
		List<Delivery> set = new ArrayList<>();
		set.add(d1);
		set.add(d2);
		set.add(d3);
		t.setDeliveries(set);

		view.setTour(t);
	
	}
}
