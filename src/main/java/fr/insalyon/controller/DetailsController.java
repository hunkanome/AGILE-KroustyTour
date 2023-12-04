package fr.insalyon.controller;

import java.util.HashSet;
import java.util.Set;

import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;
import fr.insalyon.observer.Observable;
import fr.insalyon.observer.Observer;
import fr.insalyon.view.TourTextualView;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;

public class DetailsController implements Observer {

	private DataModel dataModel;
	
	@FXML
	private TitledPane titledPane;

	public void initialize(DataModel dataModel) {
		this.dataModel = dataModel;
		this.dataModel.addObserver(this);
		TourTextualView view = new TourTextualView();
		this.titledPane.setContent(view);
		
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
		Delivery d2 = new Delivery(null, i3, new TimeWindow(8));
		Delivery d3 = new Delivery(null, i3, new TimeWindow(8));
		Set<Delivery> set = new HashSet<>();
		set.add(d1);
		set.add(d2);
		set.add(d3);
		t.setDeliveries(set);
		
		view.setTour(t);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg.getClass() == CityMap.class) {
			// TODO
		} else if (arg.getClass() == Intersection.class) {
			// TODO
		}
	}
}
