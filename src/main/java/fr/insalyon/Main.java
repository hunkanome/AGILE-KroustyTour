package fr.insalyon;

import fr.insalyon.xml.CityMapXMLParser;
import fr.insalyon.geometry.CoordinateTransformer;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.geometry.Position;
import fr.insalyon.model.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.Stage;

import static java.lang.Float.max;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		/* Loading map data */

		InputStream input = new FileInputStream("data/xml/largeMap.xml");
		CityMapXMLParser parser = new CityMapXMLParser(input);
		CityMap map = parser.parse();

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
		Scene scene = new Scene(root, 1000, 700);
		Canvas canvas = (Canvas) scene.lookup("#canvas_map");

		primaryStage.setTitle("Calculateur de tours de livraison en vélo");
		primaryStage.setScene(scene);
		primaryStage.show();
		fillMap(map, canvas);
	}

	private <T> T getOptionalValue(Optional<T> value) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException("No value found");
		}
		return value.get();
	}

	private float getMaxLatitude(CityMap map) {
//		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLatitude).max(Float::compare);
//		return getOptionalValue(max);
		return 0;
	}

	private float getMinLatitude(CityMap map) {
//		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLatitude).min(Float::compare);
//		return getOptionalValue(max);
		return 0;
	}

	private float getMaxLongitude(CityMap map) {
//		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLongitude).max(Float::compare);
//		return getOptionalValue(max);
		return 0;
	}

	private float getMinLongitude(CityMap map) {
//		Optional<Float> max = map.getIntersections().stream().map(Intersection::getLongitude).min(Float::compare);
//		return getOptionalValue(max);
		return 0;
	}

	private void fillMap(CityMap map, Canvas canvas) {
		// Getting max lat and long coordinates
		float maxLat = getMaxLatitude(map);
		float maxLong = getMaxLongitude(map);
		float minLat = getMinLatitude(map);
		float minLong = getMinLongitude(map);
		// Scaling Graphic Context
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Scaling coordinates
		GeoCoordinates northWest = new GeoCoordinates(maxLat, minLong);
		GeoCoordinates southEast = new GeoCoordinates(minLat, maxLong);
		CoordinateTransformer transformer = new CoordinateTransformer(northWest, southEast, (float) canvas.getWidth(),
				(float) canvas.getHeight());
		map.getIntersections().forEach(intersection -> {
			intersection.getOutwardSegments().forEach(segment -> {
				drawSegment(gc, segment, transformer);
			});
		});
	}

	private void drawSegment(GraphicsContext gc, Segment segment, CoordinateTransformer transformer) {
		gc.setStroke(Color.BLUE);

		Position origin = transformer.transformToPosition(segment.getOrigin().getCoordinates());
		Position destination = transformer.transformToPosition(segment.getDestination().getCoordinates());
		gc.strokeLine(origin.getX(), origin.getY(), destination.getX(), destination.getY());
	}

	public static void main(String[] args) {
		launch(args);
	}
}