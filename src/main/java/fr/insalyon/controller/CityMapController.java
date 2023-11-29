package fr.insalyon.controller;

import static java.lang.Float.max;

import java.util.Optional;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CityMapController {
	@FXML
	private Canvas canvasMap;

	public void initialize(CityMap map) {
		fillMap(map, canvasMap);
	}

	private <T> T getOptionalValue(Optional<T> value) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException("No value found");
		}
		return value.get();
	}

	private float getMaxLatitude(CityMap map) {
		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLatitude).max(Float::compare);
		return getOptionalValue(max);
	}

	private float getMinLatitude(CityMap map) {
		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLatitude).min(Float::compare);
		return getOptionalValue(max);
	}

	private float getMaxLongitude(CityMap map) {
		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLongitude).max(Float::compare);
		return getOptionalValue(max);
	}

	private float getMinLongitude(CityMap map) {
		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLongitude).min(Float::compare);
		return getOptionalValue(max);
	}

	private void fillMap(CityMap map, Canvas canvas) {
		// Getting max lat and long coordinates
		float maxLat = getMaxLatitude(map);
		float maxLong = getMaxLongitude(map);
		float minLat = getMinLatitude(map);
		float minLong = getMinLongitude(map);
		float latDiff = maxLat - minLat;
		float longDiff = maxLong - minLong;
		float coeff = max(latDiff, longDiff);
		// Scaling Graphic Context
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.scale(1, -1);
		gc.translate(0, -(latDiff * (float) (canvas.getHeight()) / coeff));
		// Scaling coordinates
		map.getIntersections().forEach(intersection -> {
			intersection.setLatitude((intersection.getLatitude() - minLat) * (float) (canvas.getHeight()) / coeff);
			intersection.setLongitude((intersection.getLongitude() - minLong) * (float) (canvas.getWidth()) / coeff);
		});
		map.getIntersections().forEach(
				intersection -> intersection.getOutwardSegments().forEach(segment -> drawSegment(gc, segment)));
	}

	private void drawSegment(GraphicsContext gc, Segment segment) {
		gc.setStroke(Color.BLUE);
		gc.strokeLine(segment.getOrigin().getLongitude(), segment.getOrigin().getLatitude(),
				segment.getDestination().getLongitude(), segment.getDestination().getLatitude());
	}
	
}
